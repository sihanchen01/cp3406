package au.edu.jcu.uploadtofiredb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
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

    private List<Upload> myUploads;
    // Connect to firebase database, get image reference url
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("imageUpload");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_collection);

        myRecyclerView = findViewById(R.id.recycler_view);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

       // Snackbar to remind user they could click book image to search it on Google
        final Snackbar reminder = Snackbar.make(findViewById(R.id.layoutShowCollection),
                "Reminder: you can click on book image to search it on Google",
                Snackbar.LENGTH_INDEFINITE);
        reminder.setAction("Got it", v -> reminder.dismiss());
        reminder.show();

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
}