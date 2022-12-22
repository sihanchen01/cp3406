package au.edu.jcu.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Set;

public class AddItemActivity extends AppCompatActivity {

    private EditText userInput;
    private Button addTask;
    private SharedPreferences dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        userInput = findViewById(R.id.userInput);
        addTask = findViewById(R.id.bAddTask);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        addTask.setOnClickListener(v -> {
            String item_description = userInput.getText().toString();
            Set<String> items = dataSource.getStringSet("items", null);
            assert items != null; // can be commented out while nothing is added to dataSource

            Set<String> newItems = new HashSet<>(items);
            newItems.add(item_description);

            dataSource.edit().clear().putStringSet("items", newItems).apply();

            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            finish();
        });
        dataSource = getSharedPreferences("todo items", Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            String item_description = userInput.getText().toString();
            if (item_description.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure to go back, there is no task input?");
                builder.setPositiveButton(Html.fromHtml("<b>"+"Yes"+"</b>"), (dialog, id) -> {
                    // User clicked OK button
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, id) -> {
                    // User cancelled the dialog

                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}