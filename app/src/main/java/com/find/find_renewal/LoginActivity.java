package com.find.find_renewal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    private AQuery aq = new AQuery(this); // AQuery 사용

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        EditText editID = (EditText)findViewById(R.id.editID);
        EditText editPW = (EditText)findViewById(R.id.editPW);
        Button loginBtn = (Button)findViewById(R.id.loginBtn);
        Button joinBtn = (Button)findViewById(R.id.joinBtn);

        /**
         *  Login Button 에 AQuery 적용하여 node.js 서버와 통신
         */
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // url주소를 통해 nodejs 서버로 파라미터값 전송 /users 다음 '?'로 파라미터값을 구분한다
                // 안드로이드 에뮬레이터 > 로컬 서버 접속 시엔 IP 주소를 10.0.2.2 로 해야한다
                String url = "http://10.0.2.2:3000/users?id=" + editID.getText() + "&pwd=" + editPW.getText();
                aq.ajax(url, JSONObject.class, this, "jsonCallback");
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, JoinActivity.class));
            }
        });
    }

    // ajax callback 함수
    public void jsonCallback (String url, JSONObject json, AjaxStatus status) {
        Log.d("jsonCallback", json.toString());
        if (json!=null) {
            // successful ajax call
            try {
                // 결과 메세지를 받아와서 출력한다
                String msg = json.getString("msg");
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            } catch(Exception e) {
                // ajax error
            }
        }
    }
}
