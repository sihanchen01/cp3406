package au.edu.jcu.uploadtofiredb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button bUpload;
    Button bGetPage;
    EditText etBookName;
    EditText etPageNumber;
    TextView tvPageNumber;
    TextView tvReadingList;
    TextView tvPagesRead;

    private DatabaseReference databaseReference;
    private HashMap<String, String> bookList;
    private int pageRead;

    public static final String FAIL_MSG = "Failed to retrieve data!";
    public static final String BOOK_DOES_NOT_EXIST = "This book does not exist in your list";
    public static final String NEW_PAGE_RECORDED = "New page number recorded";
    public static final String LIST_UPDATED = "Reading list updated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        bUpload = findViewById(R.id.bUpload);
        bGetPage = findViewById(R.id.bGetPage);
        etBookName = findViewById(R.id.etBookName);
        etPageNumber = findViewById(R.id.etPageNumber);
        tvPageNumber = findViewById(R.id.tvPageNumber);
        tvReadingList = findViewById(R.id.tvReadingList);
        tvPagesRead = findViewById(R.id.tvPagesRead);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String bookName = etBookName.getText().toString();
                if (bookName != null && !bookName.isEmpty()){
                    if (bookList.containsKey(bookName)) {
                        // update pagesRead if book exist in reading list, o/w log new book
                        int newPageNumber = Integer.parseInt(etPageNumber.getText().toString());
                        int oldPageNumber = Integer.parseInt(bookList.get(bookName));
                        if (newPageNumber > oldPageNumber) {
                            int changes = newPageNumber - oldPageNumber;
                            pageRead += changes;
                            tvPagesRead.setText(String.format("So far, you read %s pages.", pageRead));
                        }
                    }
                }

                bookList = (HashMap) snapshot.getValue();
                String books = "";
                for (String book : bookList.keySet()){
                    books = books + book + " (" + bookList.get(book) + ")\n";
                }
                tvReadingList.setText(books);
                Toast.makeText(MainActivity.this, LIST_UPDATED, Toast.LENGTH_SHORT).show();
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


        bUpload.setOnClickListener(view -> {
            String bookName = etBookName.getText().toString();
            String pageNumber = etPageNumber.getText().toString();
            databaseReference.child(bookName).setValue(pageNumber);
            Toast.makeText(MainActivity.this, NEW_PAGE_RECORDED, Toast.LENGTH_SHORT).show();
        });

        bGetPage.setOnClickListener(view -> databaseReference.get().addOnCompleteListener(task -> {
            String msg;
            if (!task.isSuccessful()){
                msg = FAIL_MSG;
            } else {
                String bookName = etBookName.getText().toString();
                if (bookList.get(bookName) == null) {
                    msg = BOOK_DOES_NOT_EXIST;
                    tvPageNumber.setText("0");
                } else {
                    msg = LIST_UPDATED;
                    String pageNumber = bookList.get(bookName);
                    tvPageNumber.setText(pageNumber);
                }
            }
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }));

    }
}