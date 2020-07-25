package com.example.whatistheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    final String API_ID = "7750c2358c630c09f52e6bce8fa76b84";

    public void check(View view){
        EditText cityEditText = (EditText) findViewById(R.id.cityEditText);

        if(TextUtils.isEmpty(cityEditText.getText())){
            Toast.makeText(MainActivity.this,"Enter a City Name",Toast.LENGTH_SHORT).show();
            return;
        }

        String city = cityEditText.getText().toString();

        String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+API_ID;

        DownloadTask task = new DownloadTask();

        try {
            String json = task.execute(url).get();
            JSONObject object = new JSONObject(json);
            JSONArray weather = object.getJSONArray("weather");
            JSONObject part = weather.getJSONObject(0);
            TextView main = (TextView) findViewById(R.id.textView);
            TextView description = (TextView) findViewById(R.id.textView2);
            main.setText(part.getString("main")+" : ");
            description.setText(part.getString("description"));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
        }
    }


    public class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);

                String res = "";
                int data = reader.read();
                while (data!=-1){
                    char current = (char) data;
                    res+=current;
                    data = reader.read();
                }
                return res;
            }catch (Exception e){
                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.i("JSON",s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather Info",weatherInfo);
                JSONArray array = new JSONArray(weatherInfo);
                for(int i = 0;i<array.length();i++){
                    JSONObject jsonPart = array.getJSONObject(i);
                    Log.i("Main",jsonPart.getString("main"));
                    Log.i("Description",jsonPart.getString("description"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}