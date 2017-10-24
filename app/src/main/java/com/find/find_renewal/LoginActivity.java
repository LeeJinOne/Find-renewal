package com.find.find_renewal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editID = (EditText)findViewById(R.id.editID);
        EditText editPW = (EditText)findViewById(R.id.editPW);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        Button joinBtn = (Button)findViewById(R.id.joinBtn);
    }
}
