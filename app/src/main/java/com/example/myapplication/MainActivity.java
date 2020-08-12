package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {


    EditText usernameEditText;
    EditText passwordEditText;
    Button button;
    TextView textView;
    ImageView logoImageView;
    ConstraintLayout backgroundLayout;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        logoImageView = findViewById(R.id.logoImageView);
        backgroundLayout = findViewById(R.id.backgroundLayout);
        logoImageView.setOnClickListener(MainActivity.this);
        backgroundLayout.setOnClickListener(MainActivity.this);

        passwordEditText.setOnKeyListener(MainActivity.this);

        if (ParseUser.getCurrentUser() != null){
            showUsers();
        }


        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    //METHODS

    public void setUser(View view){
        if(flag == 0){
            login();
        }else{
            signup();
        }
    }

    public void changeButton(View view){
        if(flag == 0){
            textView.setText("Or, Login");
            button.setText("Sign up");
            flag = 1;
        }else{
            textView.setText("Or, Sign Up");
            button.setText("Login");
            flag = 0;
        }
    }

    public void signup(){
        final ParseUser newUser = new ParseUser();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(username.matches("") || password.matches("")){
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Error!")
                    .setMessage("A username and a password is required")
                    .setPositiveButton("Ok",null)
                    .show();
        }else{
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null){
//                        new AlertDialog.Builder(MainActivity.this)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .setTitle("Yaayy!")
//                                .setMessage("You are signed up!")
//                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(MainActivity.this, "Your username is " + newUser.getUsername(), Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .show();
                        showUsers();
                    }else{
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Error!")
                                .setMessage(e.getMessage())
                                .setPositiveButton("Ok",null)
                                .show();
                    }
                }
            });
        }
    }

    public void login(){
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(username.matches("") || password.matches("")){
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Error!")
                    .setMessage("A username and a password is required")
                    .setPositiveButton("Ok",null)
                    .show();
        }else{
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(final ParseUser user, ParseException e) {
                    if(user != null && e == null){
//                        new AlertDialog.Builder(MainActivity.this)
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .setTitle("Yaayy!")
//                                .setMessage("You are logged in!")
//                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        Toast.makeText(MainActivity.this, "Your username is " + user.getUsername(), Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .show();
                        showUsers();
                    }else{
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Error!")
                                .setMessage(e.getMessage())
                                .setPositiveButton("Ok",null)
                                .show();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.backgroundLayout || v.getId() == R.id.logoImageView){
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == event.ACTION_DOWN){
            setUser(v);
        }
        return false;
    }

    public void showUsers(){
        Intent intent = new Intent(getApplicationContext(),UsersListActivity.class);
        startActivity(intent);
        finish();
    }
}
