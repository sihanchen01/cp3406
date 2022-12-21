package au.edu.jcu.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ListView todoList;
    private ArrayAdapter<String> adapter;

    private SharedPreferences dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoList = findViewById(R.id.todoList);

        adapter = new ArrayAdapter<>(this, R.layout.item);
        todoList.setAdapter(adapter);

        todoList.setOnItemClickListener((adapterView, view, position, id) -> {
            TextView itemView = (TextView) view;
            adapter.remove(itemView.getText().toString());
        });

        dataSource = getSharedPreferences("todo items", Context.MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_foreground);

//        adapter.addAll("buy milk", "wah car", "call mum");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.clear();
        Set<String> newItems = dataSource.getStringSet("items", null);
        if (newItems != null) {
            adapter.addAll(newItems);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Set<String> currentItems = new HashSet<>();
        for (int i = 0; i < adapter.getCount(); ++i) {
            currentItems.add(adapter.getItem(i));
        }
        dataSource.edit().putStringSet("items", currentItems).apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_item) {
            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}