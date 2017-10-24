package com.find.find_renewal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        EditText editID = (EditText)findViewById(R.id.editID);
        EditText editPW = (EditText)findViewById(R.id.editPW);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        Button joinBtn = (Button)findViewById(R.id.joinBtn);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, JoinActivity.class));
            }
        });
    }
}
