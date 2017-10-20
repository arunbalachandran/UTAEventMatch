package com.example.utaeventmatch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button signInButton;
    private Button signUpButton;
    private EditText userNameText;
    private EditText passWordText;
    private List<String> credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = (Button) findViewById(R.id.signinButton);
        signUpButton = (Button) findViewById(R.id.signupButton);
        userNameText = (EditText) findViewById(R.id.usernameText);
        passWordText = (EditText) findViewById(R.id.passwordText);
        signInButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    public void onClick(View v) {
//        if (v.getId() == signInButton.getId()) {
//            credentials = new ArrayList<String>();
//            credentials.add(userNameText.getText().toString());
//            credentials.add(passWordText.getText().toString());
//            if (credentials.get(0).equals("arun")) {  // simple check to see if the username is arun
//                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
//                intent.putStringArrayListExtra("credentials", (ArrayList<String>) credentials);
//                Toast toast1 = new Toast(getApplicationContext());
//                toast1.makeText(MainActivity.this, "Goodbye", Toast.LENGTH_SHORT).show();
//            }
//        }

        if (v.getId() == signUpButton.getId()) {
            Toast toast1 = new Toast(getApplicationContext());
            System.out.println("ghkjgjh");
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }
    }
}
