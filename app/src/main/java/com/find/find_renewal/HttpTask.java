package com.find.find_renewal;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MIRAESON_NOTE on 2017-09-29.
 */

public class HttpTask extends AsyncTask<String, Void, String> {
    private Handler handler = null;
    private String flag = "";

    public HttpTask(Handler hand) {
        this.handler = hand;
    }

    // Background 에서 작업 진행
    @Override
    protected String doInBackground(String... params) {
        String resultStr = "";
        HttpURLConnection conn = null;
        try {
            this.flag = params[0]; // 0번째 값을 flag 에 (php 파일)
            String urlString = "http://13.124.222.23" + this.flag; // EC2 인스턴스 Elastic IPs
            URL url = new URL(urlString); // ulrString 을 url 객체로 변환

            conn = (HttpURLConnection)url.openConnection(); // url 연결
            conn.setDoInput(true); // 넣기 O
            conn.setDoOutput(false); // 가져오기 X
            conn.setUseCaches(false); // 캐쉬 사용 X
            conn.setReadTimeout(20000); // 읽어오는데 20초 걸리면 땡
            conn.setConnectTimeout(3000); // 연결시간 3초로 제한

            conn.setRequestMethod("POST"); // POST 방식으로 통신

            StringBuffer sb = new StringBuffer(""); // 스트링버퍼 생성

            sb.append(params[1]); // 버퍼에 다음값 추가
            PrintWriter output = new PrintWriter(conn.getOutputStream());
            output.print(sb.toString());
            output.close();

            StringBuffer sb2 = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            for (;;) { // Background 에서 계속 실행
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb2.append(line + "\n");
                br.close();
                conn.disconnect();
                br = null;
                conn = null;

                resultStr = sb2.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return resultStr;
    }
}
