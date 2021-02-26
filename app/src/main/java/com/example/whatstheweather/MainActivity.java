package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url= new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection.connect();
                /*if(String.valueOf(urlConnection.getResponseCode()).equals("400")){
                    return result;
                }*/
                InputStream istream =  urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(istream);
                int data = reader.read();

                while (data != -1){
                    char c = (char) data;
                    result += c;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "";

            }
            //return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equals("")) {
                resultTextView.setText("Invalid City");
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    //Log.i("Weather Content", jsonObject.getJSONObject("current").getString("temp_c"));
                    String location = jsonObject.getJSONObject("location").getString("name") + ", " + jsonObject.getJSONObject("location").getString("region") + ", " + jsonObject.getJSONObject("location").getString("country");

                    String weather = "\nTemperature - " + jsonObject.getJSONObject("current").getString("temp_c") + "°C\n" + jsonObject.getJSONObject("current").getJSONObject("condition").getString("text") + "\nCloud Cover - " + jsonObject.getJSONObject("current").getString("cloud") + "%\nHumidity - " + jsonObject.getJSONObject("current").getString("humidity");

                    resultTextView.setText(location + weather);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("HELLO 2", s);
                }
            }
        }
    }

    public void getWeather(View view){

        DownloadTask task = new DownloadTask();
        task.execute("https://api.weatherapi.com/v1/current.json?key=0713474ed80f42baa2c174704212102&q="+ editText.getText().toString());

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

    }
}