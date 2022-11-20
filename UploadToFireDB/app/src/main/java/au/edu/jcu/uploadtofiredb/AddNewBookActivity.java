package au.edu.jcu.uploadtofiredb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddNewBookActivity extends AppCompatActivity {
    ImageView ivBookImage;
    EditText etBookname;
    Button bAddBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_book);

        ivBookImage = findViewById(R.id.ivBookImage);
        etBookname = findViewById(R.id.etBookName);
        bAddBook = findViewById(R.id.bAddBook);

        bAddBook.setOnClickListener(view -> {
            String bookName = etBookname.getText().toString();
            if (!bookName.isEmpty()) {
                Intent bookInfo = new Intent(this, MainActivity.class);
                bookInfo.putExtra("bookName", bookName);
                setResult(RESULT_OK, bookInfo);
                finish();
            } else {
                Toast.makeText(AddNewBookActivity.this, "Book Name can not be empty!",
                        Toast.LENGTH_LONG).show();
            }
        });

        // TODO: allow user to upload book image
    }
}
