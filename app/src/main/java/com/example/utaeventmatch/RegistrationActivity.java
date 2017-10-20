package com.example.utaeventmatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arun on 01/10/17.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextBtn;
    private EditText usernameText;
    private EditText passwordText;
    private EditText firstname;
    private EditText lastname;
    private EditText contact;
//    private EditText emailid;
    private List<String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);
        nextBtn = (Button) findViewById(R.id.nextButton);
        usernameText = (EditText) findViewById(R.id.usernameRegText);
        passwordText = (EditText) findViewById(R.id.passwordRegText);
        firstname = (EditText) findViewById(R.id.firstnameText);
        lastname = (EditText) findViewById(R.id.lastnameText);
        contact = (EditText) findViewById(R.id.contactText);
//        emailid = (EditText) findViewById(R.id.emailText);
        nextBtn.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == nextBtn.getId()) {
            userData = new ArrayList<String>();
            userData.add(usernameText.getText().toString());
            userData.add(passwordText.getText().toString());
            userData.add(firstname.getText().toString());
            userData.add(lastname.getText().toString());
            userData.add(contact.getText().toString());
//            userData.add(emailid.getText().toString());
            System.out.println("Almost done with registration");
            Intent intent = new Intent(RegistrationActivity.this, HobbyActivity.class);
            intent.putStringArrayListExtra("userData", (ArrayList<String>) userData);
            startActivity(intent);
        }
    }
}
