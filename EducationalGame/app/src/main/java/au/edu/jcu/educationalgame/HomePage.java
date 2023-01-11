package au.edu.jcu.educationalgame;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    EditText etUsername;
    EditText etPIN;
    Button bSignIn;
    Button bRegister;

    String username;
    String pinNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        etUsername = findViewById(R.id.etUsername);
        etPIN = findViewById(R.id.etPIN);
        bSignIn = findViewById(R.id.bSignIn);
        bRegister = findViewById(R.id.bRegister);

        DataBaseHelper dataBaseHelper = new DataBaseHelper(HomePage.this);

        bSignIn.setOnClickListener(v -> {
            username = etUsername.getText().toString();
            pinNumber = etPIN.getText().toString();
            if (validateInput()){
                if (dataBaseHelper.validateUser(username, pinNumber)) {
                    UserModel currentUser = dataBaseHelper.getUser(username);
                    Intent pickGame = new Intent(HomePage.this, PickGamePage.class);
                    pickGame.putExtra("currentUser", currentUser);
                    startActivity(pickGame);
                } else {
                    Toast.makeText(HomePage.this, "Invalid password, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Create a new user.
        bRegister.setOnClickListener(v -> {
           username = etUsername.getText().toString();
           pinNumber = etPIN.getText().toString();
           if (validateInput()) {
               if (dataBaseHelper.checkDuplicateUsername(username)){
                   Toast.makeText(HomePage.this, "Username already exist!", Toast.LENGTH_LONG).show();
               }
               else {
                   UserModel newUser = new UserModel(username, pinNumber);
                   boolean success = dataBaseHelper.createNewUser(newUser);
                   if (success) {
                       Toast.makeText(HomePage.this, "New User "+ username + " created!", Toast.LENGTH_LONG).show();
                   }
               }
           }
        });
    }

    /**
     * Check if username and pin number are following rules.
     * @return true if validation pass, false otherwise
     */
    public boolean validateInput() {
        // Username too long
        if (username.length() > 8 ){
            Toast.makeText(HomePage.this, "Username exceeds 8 letters!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Username empty
        if (username.equals("")){
            Toast.makeText(HomePage.this, "Empty Username!", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Pin number length is wrong, or Pin contains non digits
        if (!(pinNumber.matches("[0-9]{6}"))) {
            Toast.makeText(HomePage.this, "Invalid PIN, try again!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}