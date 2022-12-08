package au.edu.jcu.assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowCollection extends AppCompatActivity {
    private RecyclerView myRecyclerView;
    private ImageAdapter myAdapter;
    // Track if it is first time view got created, only show snackbar on first run
    private boolean firstTime = true;
    private List<Upload> myUploads;
    // Connect to firebase database, get image reference url
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("imageUpload");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_collection);

        myRecyclerView = findViewById(R.id.recycler_view);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Read saved instance value
        if (savedInstanceState != null){
            firstTime = savedInstanceState.getBoolean("firstTime");
        }

       // Snackbar to remind user they could click book image to search it on Google
        final Snackbar reminder = Snackbar.make(findViewById(R.id.layoutShowCollection),
                "Reminder: you can click on book image to search it on Google",
                Snackbar.LENGTH_INDEFINITE);
        reminder.setAction("Got it", v -> {
            reminder.dismiss();
            firstTime = false;
        });
        // Only show snack bar at first time
        if (firstTime) reminder.show();

        // Listen to Firebase Database, everytime Database changes, re-initiate book collection array
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myUploads = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    myUploads.add(upload);
                }

                // Using ImageAdapter to display images in app
                myAdapter = new ImageAdapter(ShowCollection.this, myUploads);
                myRecyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowCollection.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("firstTime", firstTime);
    }
}