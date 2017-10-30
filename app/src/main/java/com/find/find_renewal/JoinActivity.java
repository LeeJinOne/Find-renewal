package com.find.find_renewal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

public class JoinActivity extends AppCompatActivity {
    private Context context;
    AQuery aq = new AQuery(this);

    // 기본 서버 주소 & route
    final String serverURL = "http://10.0.2.2:3000/";
    final String idcheck_route = "idcheck?";
    final String join_route = "join?";

    // 전역 변수로 설정
    EditText editID, editPWD, editPWD2, editName, editPho;
    TextView checkID, checkPWD;
    Button idCheckBtn, joinBtn, cancelBtn;

    // 가입 가능, 불가능 체크 변수
    Boolean canID = false, canPWD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        context = this;

        // EditTexts
        editID = (EditText)findViewById(R.id.editID);
        editPWD = (EditText)findViewById(R.id.editPWD);
        editPWD2 = (EditText)findViewById(R.id.editPWD2);
        editName = (EditText)findViewById(R.id.editName);
        editPho = (EditText)findViewById(R.id.editPho);

        // TextViews
        checkID = (TextView)findViewById(R.id.checkID);
        checkPWD = (TextView)findViewById(R.id.checkPWD);

        // Buttons
        idCheckBtn = (Button)findViewById(R.id.idCheckBtn);
        joinBtn = (Button)findViewById(R.id.joinBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        // Passoword 입력 란에 TextWatcher 연결
        editPWD.addTextChangedListener(textWatcher);
        editPWD2.addTextChangedListener(textWatcher);

        idCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idcheckClick(idCheckBtn);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinClick(joinBtn);
            }
        });

        /*
        * * cancel 버튼을 누르면 경고 다이얼로그 생성
        * */
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(JoinActivity.this);
                dialog  .setTitle("NOTICE")
                        .setMessage("Are you sure cancel writing?")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                Toast.makeText(JoinActivity.this
                                            , "Back to Login screen"
                                            , Toast.LENGTH_SHORT).show();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Nothing to do.
                            }
                        });
                dialog.create();
                dialog.show();
            }
        });

    }

    /**
     * Click Methods
     * */
    public void idcheckClick(View view) {
        String url = serverURL + idcheck_route +
                "id=" + editID.getText();
        aq.ajax(url, JSONObject.class, this, "checkCallback");
    }

    public void joinClick(View view) {
        String url = serverURL + join_route +
                "id=" + editID.getText().toString() +
                "&pwd=" + editPWD.getText().toString() +
                "&name=" + editName.getText().toString() +
                "&number=" + editPho.getText().toString() +
                "&con=" + 0 +
                "&date=now()";
        aq.ajax(url, JSONObject.class, this, "joinCallback");
    }

    /**
     * Callback Methods
     * */
    public void checkCallback(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
            try {
                Log.d("checkCallback: ", json.getString("result"));
                String msg = json.getString("msg");
                String result = json.getString("result");
                if (result.equals("success")) {
                    checkID.setText("You can use this ID");
                    checkID.setTextColor(Color.GREEN);
                    canID = true;
                } else {
                    checkID.setText("You can't use this ID");
                    checkID.setTextColor(Color.RED);
                    canID = false;
                }
            } catch (Exception e) {
                // ajax error
            }
        }
    }

    public void joinCallback(String url, JSONObject json, AjaxStatus status) {
        if (json != null) {
            try {
                Log.d("joinCallback: ", json.getString("result"));
                String msg = json.getString("msg");
                String result = json.getString("result");
                //TODO: 위치가 애매함, 다시 한번 생각해봐야할 위치
                if(!canID) {
                    Toast.makeText(context, "Duplicate ID.", Toast.LENGTH_LONG).show();
                    return;
                } else if (!canPWD) {
                    Toast.makeText(context, "Passwords are not the same.", Toast.LENGTH_LONG).show();
                    return;
                }
                if (result.equals("success")) {
                    Toast.makeText(context, msg,
                            Toast.LENGTH_LONG).show();
                    startActivity(new Intent(context, LoginActivity.class));
                } else {
                    Toast.makeText(context, msg,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                // ajax error
            }
        }
    }

    /*
    *  위,아래 Password 를 입력하는 EditText 필드의 값이 같은지 실시간 체크
    * */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pwd = editPWD.getText().toString();
            String pwd2 = editPWD2.getText().toString();

            if(pwd.equals(pwd2)) {
                checkPWD.setText("you can use this password.");
                checkPWD.setTextColor(Color.GREEN);
                canPWD = true;
            } else {
                checkPWD.setText("please, check your password agian.");
                checkPWD.setTextColor(Color.RED);
                canPWD = false;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
