package si.uni_lj.fe.tnuv.groupsound2_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;

    private UserDatabase userDatabase;

    private String loggedInUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        userDatabase = new UserDatabase(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if the credentials are valid
                if (userDatabase.isValidCredentials(username, password)) {

                    // Store the logged-in username
                    loggedInUsername = username;


                    // Successful login, proceed to the main activity
                    Intent intent = new Intent(LoginActivity.this, MyplaylistsActivity.class);
                    intent.putExtra("loggedInUsername", loggedInUsername);
                    startActivity(intent);
                    // Show a success message
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                } else {
                    // Invalid credentials, show an error message
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }


            }
        });


        Button goToSignupButton = findViewById(R.id.SignupButton);
        goToSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }





}
