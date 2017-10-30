package com.find.find_renewal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jin-won on 2017. 10. 30..
 */

public class IntroActivity extends AppCompatActivity {
    private Context context;

    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        h = new Handler();
        h.postDelayed(mrun, 2000);
    }

    Runnable mrun = new Runnable() {
        @Override
        public void run() {
            Intent i = new Intent (IntroActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        h.removeCallbacks(mrun);
    }
}
