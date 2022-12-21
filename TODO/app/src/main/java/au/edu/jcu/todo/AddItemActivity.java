package au.edu.jcu.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class AddItemActivity extends AppCompatActivity {

    private EditText userInput;
    private SharedPreferences dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        userInput = findViewById(R.id.userInput);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

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
                Toast.makeText(this, "No item name given", Toast.LENGTH_SHORT).show();
                return true;
            }

            Set<String> items = dataSource.getStringSet("items", null);
            assert items != null; // can be commented out while nothing is added to dataSource

            Set<String> newItems = new HashSet<>(items);
            newItems.add(item_description);

            dataSource.edit().clear().putStringSet("items", newItems).apply();

            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}