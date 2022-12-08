package au.edu.jcu.assignment1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    Button bUpdate;
    Button bAddNewBook;
    Button bSortByName;
    Button bSortByValue;
    Button bShowCollection;
    Button bSettings;
    EditText etBookName;
    EditText etPageNumber;
    TableLayout tlReadingListRows;
    TextView tvPagesRead;

    // Connect to Firebase database, use path 'books' to store books info,
    // use path 'pages' to store reading goal number
    private final DatabaseReference databaseRefReadingList = FirebaseDatabase.getInstance().getReference("books");
    private final DatabaseReference databaseRefPageRead = FirebaseDatabase.getInstance().getReference("pages");

    private HashMap<String, Integer> readingListUnsorted;
    private TreeMap<String, Integer> readingListSortByName;
    private TreeMap readingListSortByNameReverse = new TreeMap(Collections.reverseOrder());
    private HashMap<String, Integer> readingListSortByValue;

    // current display style, default for sortByName, 1 for sortByValue
    private int currentSortStyle;
    // The number of pages user already read
    private int pageRead;
    // The number of pages of user's reading goal, default is 100
    private int monthlyGoal = 100;
    private boolean descOrder = false;
    // Name of book to be update
    private String bookName;
    // Number of pages to be update
    private String pageNumber;

    // Constant strings for toast message
    public static final String FAIL_MSG = "Failed to retrieve data!";
    public static final String BOOK_DOES_NOT_EXIST = "This book does not exist in your list";
    public static final String NEW_PAGE_RECORDED = "New page number recorded";
    public static final String LIST_UPDATED = "Reading list updated";
    public static final String NEW_BOOK_ADDED = "New book added into your list!";


    // Settings activity launcher, allow user to set a custom reading goal
    ActivityResultLauncher<Intent> settingsLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    monthlyGoal = result.getData().getIntExtra("newGoal", monthlyGoal);
                    // update reading progress message
                    updatePagesReadPrompt();
                }
            }
    );

    // Add new book activity launcher, allow user to add new book into reading list
    ActivityResultLauncher<Intent> addNewBookLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    String newBookName = result.getData().getStringExtra("bookName");
                    databaseRefReadingList.child(newBookName).setValue(0);
                    Toast.makeText(MainActivity.this, NEW_BOOK_ADDED, Toast.LENGTH_SHORT).show();
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bUpdate = findViewById(R.id.bUpdate);
        bAddNewBook = findViewById(R.id.bAddNewBook);
        bSortByName = findViewById(R.id.bSortByName);
        bSortByValue = findViewById(R.id.bSortByValue);
        bShowCollection = findViewById(R.id.bShowCollection);
        bSettings = findViewById(R.id.bSettings);
        etBookName = findViewById(R.id.etBookName);
        etPageNumber = findViewById(R.id.etPageNumber);
        tvPagesRead = findViewById(R.id.tvPagesRead);
        tlReadingListRows = findViewById(R.id.tlReadingListRows);

        // Check if there are saved instance, so app won't crash when rotate
        if (savedInstanceState != null) {
            bookName = savedInstanceState.getString("bookName");
            pageNumber = savedInstanceState.getString("pageNumber");
            readingListUnsorted = (HashMap) savedInstanceState.getSerializable("readingList");
            // Re-assign value from saved instance
            etBookName.setText(bookName);
            etPageNumber.setText(pageNumber);
        }

        // Snackbar to instruct user they could click book text to update page number
        final Snackbar reminder = Snackbar.make(findViewById(R.id.root),
                "Reminder: you can click on existing book to fill in name and page number",
                Snackbar.LENGTH_INDEFINITE);
        reminder.setAction("Got it", v -> reminder.dismiss());
        reminder.show();

        // Firebase database 'pages read' event listener, update reading progress message
        databaseRefPageRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if reading goal number already exist, if not, initiate with zero
                pageRead = snapshot.exists() ?
                        ((Number) Objects.requireNonNull(snapshot.getValue())).intValue() : 0;
                updatePagesReadPrompt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, FAIL_MSG, Toast.LENGTH_SHORT).show();
            }
        });

        // Firebase database 'book reading list' event listener, update reading list message
        databaseRefReadingList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bookName = etBookName.getText().toString();
                if (!bookName.isEmpty()) {
                    if (readingListUnsorted.containsKey(bookName)) {
                        // Update pages read only if book name exist in reading list
                        int newPageNumber = Integer.parseInt(etPageNumber.getText().toString());
                        int oldPageNumber = ((Number) readingListUnsorted.get(bookName)).intValue();
                        if (newPageNumber > oldPageNumber) {
                            int changes = newPageNumber - oldPageNumber;
                            pageRead += changes;
                            databaseRefPageRead.setValue(pageRead);
                            tvPagesRead.setText(String.format("So far, you read %s pages.", pageRead));
                        }
                    }
                }

                // Update Hashmap with new values from DB
                if (snapshot.exists()) {
                    readingListUnsorted = (HashMap) snapshot.getValue();
                    // Use treemap to sort reading list by book name
                    assert readingListUnsorted != null;
                    readingListSortByName = new TreeMap<>(readingListUnsorted);
                    // Use helper function to sort reading list by page value
                    readingListSortByValue = sortReadingListByValue(readingListUnsorted, descOrder);

                    if (currentSortStyle == 1) {
                        generateReadingList(readingListSortByValue);
                    } else {
                        generateReadingList(readingListSortByName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, FAIL_MSG, Toast.LENGTH_SHORT).show();
            }
        });

        bSettings.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsLauncher.launch(settingsIntent);
        });

        bUpdate.setOnClickListener(view -> {
            // Update book page
            String bookName = etBookName.getText().toString();
            String pageNumber = etPageNumber.getText().toString();
            if (readingListUnsorted.containsKey(bookName)) {
                // Update book page in database
                databaseRefReadingList.child(bookName).setValue(Integer.parseInt(pageNumber));
                Toast.makeText(MainActivity.this, NEW_PAGE_RECORDED, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, BOOK_DOES_NOT_EXIST, Toast.LENGTH_SHORT).show();
            }
        });

        bAddNewBook.setOnClickListener(view -> {
            Intent addNewBookIntent = new Intent(this, AddNewBookActivity.class);
            addNewBookLauncher.launch(addNewBookIntent);
        });

        bSortByName.setOnClickListener(view -> {
            // Sort by keys
            if (descOrder) {
                generateReadingList(readingListSortByName);
            } else {
                readingListSortByNameReverse.putAll(readingListSortByName);
                generateReadingList(readingListSortByNameReverse);
            }
            descOrder = !descOrder;
        });

        bSortByValue.setOnClickListener(view -> {
            descOrder = !descOrder;
            currentSortStyle = 1;
            // Sort by value
            readingListSortByValue = sortReadingListByValue(readingListUnsorted, descOrder);
            generateReadingList(readingListSortByValue);
        });

        // TODO: add sort by time added function

        bShowCollection.setOnClickListener(view -> {
            Intent showCollectionIntent = new Intent(this, ShowCollection.class);
            startActivity(showCollectionIntent);
        });
    } // End of onCreate


    // Saved instance state so app will not crash when rotate
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("bookName", bookName);
        outState.putString("pageNumber", pageNumber);
        outState.putSerializable("readingList", readingListUnsorted);
    }

    // Update reading goal progress percentage, and prompt user with message accordingly
    @SuppressLint("DefaultLocale")
    private void updatePagesReadPrompt() {
        double progress = ((double) pageRead / (double) monthlyGoal) * 100;
        tvPagesRead.setText(String.format("So far, you read %s pages.\n" +
                        "You have accomplished %.1f%% of your monthly goal (%s)."
                , pageRead, progress, monthlyGoal));
    }


    // Sort reading list by page number
    public LinkedHashMap<String, Integer> sortReadingListByValue(HashMap<String, Integer> hashMap, boolean reverse) {
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            list.add(((Number) entry.getValue()).intValue());
        }
        if (reverse) {
            Collections.sort(list, (i1, i2) -> i2.compareTo(i1));
        } else {
            Collections.sort(list, (i1, i2) -> i1.compareTo(i2));
        }

        for (Integer i : list) {
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
                Object x = ((Number) entry.getValue()).intValue();
                if (x.equals(i)) {
                    sortedMap.put(entry.getKey(), i);
                }
            }
        }
        return sortedMap;
    } // end of sortReadingListByValue


    // Take in reading list array, convert to table row and inflate into Scroll View on Main
    private void generateReadingList(Map<String, Integer> list) {
        tlReadingListRows.removeAllViews();
        for (String book : list.keySet()) {
            String output = String.format("%s (%s)", book, list.get(book));
            getLayoutInflater().inflate(R.layout.book, tlReadingListRows);

            View lastRow = tlReadingListRows.getChildAt(tlReadingListRows.getChildCount() - 1);
            TableRow addedBookRow = (TableRow) lastRow;
            View bookText = addedBookRow.getChildAt(0);
            TextView bookTextView = (TextView) bookText;
            bookTextView.setText(output);

            addedBookRow.setOnClickListener(view -> {
                etBookName.setText(book);
                etPageNumber.setText(String.valueOf(list.get(book)));
            });
        }
        Toast.makeText(this, LIST_UPDATED, Toast.LENGTH_SHORT).show();
    }
}
