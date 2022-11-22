package au.edu.jcu.uploadtofiredb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    Button bUpdate;
    Button bAddNewBook;
    Button bSortByName;
    Button bSortByValue;
    Button bShowCollection;
    EditText etBookName;
    EditText etPageNumber;
    TextView tvReadingList;
    TextView tvPagesRead;

    private DatabaseReference databaseRefReadingList;
    private DatabaseReference databaseRefPageRead;
    private HashMap<String, Integer> readingListUnsorted;
    private TreeMap<String, Integer> readingListSortByName;
    private HashMap<String, Integer> readingListSortByValue;
    // current display style, default for sortByName, 1 for sortByValue
    private int currentSortStyle;
    private int pageRead;
    private int monthlyGoal = 85;
    private boolean descOrder = false;


    public static final String FAIL_MSG = "Failed to retrieve data!";
    public static final String BOOK_DOES_NOT_EXIST = "This book does not exist in your list";
    public static final String NEW_PAGE_RECORDED = "New page number recorded";
    public static final String LIST_UPDATED = "Reading list updated";
    public static final String NEW_BOOK_ADDED = "New book added into your list!";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234) {
            if (resultCode == RESULT_OK && data != null) {
                String newBookName = data.getStringExtra("bookName");
                databaseRefReadingList.child(newBookName).setValue(0);
                Toast.makeText(MainActivity.this, newBookName, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bUpdate = findViewById(R.id.bUpdate);
        bAddNewBook = findViewById(R.id.bAddNewBook);
        bSortByName = findViewById(R.id.bSortByName);
        bSortByValue = findViewById(R.id.bSortByValue);
        bShowCollection = findViewById(R.id.bShowCollection);
        etBookName = findViewById(R.id.etBookName);
        etPageNumber = findViewById(R.id.etPageNumber);
        tvPagesRead = findViewById(R.id.tvPagesRead);
        tvReadingList = findViewById(R.id.tvReadingList);
        // Make reading list vertical scrollable
        tvReadingList.setMovementMethod(new ScrollingMovementMethod());

        databaseRefReadingList = FirebaseDatabase.getInstance().getReference("books");
        databaseRefPageRead = FirebaseDatabase.getInstance().getReference("pages");
        databaseRefPageRead.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pageRead = snapshot.exists() ?
                        ((Number) snapshot.getValue()).intValue() : 0;
                updatePagesReadPrompt();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, FAIL_MSG, Toast.LENGTH_SHORT).show();
            }
        });

        databaseRefReadingList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String bookName = etBookName.getText().toString();
                if (!bookName.isEmpty()){
                    if (readingListUnsorted.containsKey(bookName)) {
                        // update pagesRead if book exist in reading list, o/w log new book
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
                    readingListSortByName = new TreeMap<>(readingListUnsorted);
                    readingListSortByValue = sortReadingListByValue(readingListUnsorted, descOrder);

                    if (currentSortStyle == 1) {
                        displayReadingList(readingListSortByValue);
                    } else {
                        displayReadingList(readingListSortByName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, FAIL_MSG, Toast.LENGTH_SHORT).show();
            }
        });

//        databaseReference.get().addOnCompleteListener(task -> {
//            if (!task.isSuccessful()){
//                Toast.makeText(MainActivity.this, FAIL_MSG, Toast.LENGTH_SHORT).show();
//            } else {
//                bookList = (HashMap) task.getResult().getValue();
//                String books = tvReadingList.getText().toString();
//                for (String book : bookList.keySet()){
//                    books = books + book + " (" + bookList.get(book) + ")\n";
//                }
//                tvReadingList.setText(books);
//            }
//        });


        bUpdate.setOnClickListener(view -> {
            String bookName = etBookName.getText().toString();
            String pageNumber = etPageNumber.getText().toString();
            if (readingListUnsorted.containsKey(bookName)){
                databaseRefReadingList.child(bookName).setValue(Integer.parseInt(pageNumber));
                Toast.makeText(MainActivity.this, NEW_PAGE_RECORDED, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, BOOK_DOES_NOT_EXIST, Toast.LENGTH_SHORT).show();
            }
        });

//        bGetPage.setOnClickListener(view -> databaseReference.get().addOnCompleteListener(task -> {
//            String msg;
//            if (!task.isSuccessful()){
//                msg = FAIL_MSG;
//            } else {
//                String bookName = etBookName.getText().toString();
//                if (readingList.get(bookName) == null) {
//                    msg = BOOK_DOES_NOT_EXIST;
//                } else {
//                    msg = LIST_UPDATED;
//                    String pageNumber = Integer.toString(((Number) readingList.get(bookName)).intValue());
//                }
//            }
//            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//        }));

        bAddNewBook.setOnClickListener(view -> {
            Intent addNewBookIntent = new Intent(this, AddNewBookActivity.class);
            startActivityForResult(addNewBookIntent, 1234);

        });

        bSortByName.setOnClickListener(view -> {
            // Sort by keys
            displayReadingList(readingListSortByName);
        });

        bSortByValue.setOnClickListener(view -> {
            descOrder = !descOrder;
            currentSortStyle = 1;
            // Sort by value
            readingListSortByValue = sortReadingListByValue(readingListUnsorted, descOrder);
            displayReadingList(readingListSortByValue);
        });

        // TODO: add sort by time added function

        bShowCollection.setOnClickListener(view -> {
            Intent showCollectionIntent = new Intent(this, ShowCollection.class);
            startActivity(showCollectionIntent);
        });
    }

    @SuppressLint("DefaultLocale")
    private void updatePagesReadPrompt() {
        double progress = ((double) pageRead / (double) monthlyGoal ) * 100;
        tvPagesRead.setText(String.format("So far, you read %s pages.\nYou have accomplished %.1f%% of your monthly goal.", pageRead, progress));
    }

    public LinkedHashMap<String, Integer> sortReadingListByValue (HashMap<String, Integer> hashMap,
                                                                  boolean reverse) {
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
            for (Map.Entry<String, Integer> entry : hashMap.entrySet()){
                Object x = ((Number) entry.getValue()).intValue();
                if (x.equals(i)){
                    sortedMap.put(entry.getKey(), i);
                }
            }
        }
        return sortedMap;
    } // end of sortReadingListByValue

    private void displayReadingList (Map<String, Integer> list) {
        String books = "";

        for (String book : list.keySet()){
            books = books + book + " (" + list.get(book) + ")\n\n";
        }

        tvReadingList.setText(books);
        Toast.makeText(MainActivity.this, LIST_UPDATED, Toast.LENGTH_SHORT).show();
    }

}