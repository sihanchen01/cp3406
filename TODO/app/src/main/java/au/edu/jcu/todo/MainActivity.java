package au.edu.jcu.todo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ListView todoList;
    private ConstraintLayout homePage;
    private ArrayAdapter<String> adapter;
    private SharedPreferences dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoList = findViewById(R.id.todoList);
        homePage = findViewById(R.id.homePageConstraint);
        adapter = new ArrayAdapter<>(this, R.layout.item);
        todoList.setAdapter(adapter);

        todoList.setOnItemClickListener((adapterView, view, position, id) -> {
            TextView itemView = (TextView) view;
            Toast.makeText(this, "Task \"" + itemView.getText() + "\" completed!",
                    Toast.LENGTH_SHORT).show();
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
        if (newItems != null && newItems.size() != 0) {
            adapter.addAll(newItems);
        } else {
            TextView noItem = new TextView(this);
            noItem.setText("There is no task.");
            noItem.setId(View.generateViewId());
            homePage.addView(noItem,0);
            ConstraintSet set = new ConstraintSet();
            set.clone(homePage);
            set.connect(noItem.getId(), ConstraintSet.TOP, homePage.getId(), ConstraintSet.TOP);
            set.connect(noItem.getId(), ConstraintSet.START, homePage.getId(), ConstraintSet.START);
            set.connect(noItem.getId(), ConstraintSet.BOTTOM, homePage.getId(), ConstraintSet.BOTTOM);
            set.connect(noItem.getId(), ConstraintSet.END, homePage.getId(), ConstraintSet.END);
            set.applyTo(homePage);
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
    protected void onDestroy() {
        super.onDestroy();

        dataSource.edit().clear().apply();
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