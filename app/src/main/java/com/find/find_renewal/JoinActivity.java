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

public class JoinActivity extends AppCompatActivity {
    private Context context;

    DataHandler handler;

    EditText editID, editPWD, editPWD2, editName, editPho;
    TextView checkID, checkPWD;
    Button idCheckBtn, joinBtn, cancelBtn;

    Boolean canID = false, canPWD = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        handler = new DataHandler();

        context = this;

        editID = (EditText)findViewById(R.id.editID);
        editPWD = (EditText)findViewById(R.id.editPWD);
        editPWD2 = (EditText)findViewById(R.id.editPWD2);
        editName = (EditText)findViewById(R.id.editName);
        editPho = (EditText)findViewById(R.id.editPho);

        checkID = (TextView)findViewById(R.id.checkID);
        checkPWD = (TextView)findViewById(R.id.checkPWD);

        idCheckBtn = (Button)findViewById(R.id.idCheckBtn);
        joinBtn = (Button)findViewById(R.id.joinBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);

        editPWD.addTextChangedListener(textWatcher);
        editPWD2.addTextChangedListener(textWatcher);

        idCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "id_check.php",
                        "id=" + editID.getText().toString());
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpTask(handler).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "join.php",
                        "id=" + editID.getText().toString() + "&pwd=" + editPWD.getText().toString() +
                 "&name=" + editName.getText().toString() + "&number=" + editPho.getText().toString());
            }
        });

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



    class DataHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString().trim();
            Log.e("", "result = " + result);

            String[] strs = result.split("\\|"); // 결과값을 split 으로 나눔
            for (String str : strs) {
                Log.e("!!!!!", "str = " + str);
            }

            switch (strs[0]) {
                case "id_check.php":
                    if(strs[1].equals("fail")) {
                        checkID.setText("You can use this ID");
                        checkID.setTextColor(Color.GREEN);
                        canID = true;
                    } else {
                        checkID.setText("You can't use this ID");
                        checkID.setTextColor(Color.RED);
                        canID = false;
                    }
                    break;
                case "join.php":
                    if(!canID) {
                        Toast.makeText(context, "Duplicate ID.", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!canPWD) {
                        Toast.makeText(context, "Passwords are not the same.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    switch (strs[1]) {
                        case "fail":
                            Toast.makeText(context, "Failed to sign up",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case "success":
                            Toast.makeText(context, "Welcome to being our family",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(context, LoginActivity.class));
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
