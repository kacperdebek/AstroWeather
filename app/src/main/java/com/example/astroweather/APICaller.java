package com.example.astroweather;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APICaller {
    private OkHttpClient client;
    private Request request;
    private TextView textView;
    private Response response = null;

    public void callApi(TextView textView) throws Exception {
        client = new OkHttpClient();
        this.textView = textView;
        request = new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q=London&appid=77540b5cd880c96ba142a09ec6717938")
                .get()
                .build();
        new ExecuteCallTask().execute();
    }

    class ExecuteCallTask extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... urls) {
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void param) {
            if (response != null) {
                String resStr = null;
                try {
                    resStr = response.body().string();
                    JSONObject jsonResponse = new JSONObject(resStr);
                    textView.setText(jsonResponse.toString());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("null response");
            }
        }
    }
}