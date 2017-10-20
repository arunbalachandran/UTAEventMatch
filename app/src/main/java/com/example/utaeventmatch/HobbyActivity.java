package com.example.utaeventmatch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class HobbyActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<String> selectedHobbies = new ArrayList<String>();
    private Button checkSubmitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hobby_list);
        checkSubmitButton = (Button) findViewById(R.id.checkedListButton);
        // the getIntent() method only starts inside the onCreate method
//        final ArrayList<String> userData = getIntent().getStringArrayListExtra("userData");
        ListView hobbiesList = (ListView) findViewById(R.id.checkableList);
        hobbiesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String[] Items = {"Badminton", "Cycling", "Swimming"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.rowlayout_registration, R.id.checkedText, Items);
        checkSubmitButton.setOnClickListener(this);
        hobbiesList.setAdapter(adapter);
        hobbiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = ((TextView) view).getText().toString();
                selectedHobbies.add(selectedItem);
                System.out.println("Current seleectedion " + selectedItem);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == checkSubmitButton.getId()) {
            ArrayList<String> userData = getIntent().getStringArrayListExtra("userData");
            sendDataServer(userData, selectedHobbies);
            Intent intent = new Intent(HobbyActivity.this, UserHomeActivity.class);
            intent.putStringArrayListExtra("userData", (ArrayList<String>) selectedHobbies);
            startActivity(intent);
        }
    }

    public String formatDataAsJson(ArrayList<String> userData, ArrayList<String> selectedHobbies) {
        // userData.get(0) = userName;
        String Email = userData.get(0);
        String Passwrd = userData.get(1);
        String Fname = userData.get(2);
        String Lname = userData.get(3);
        String Contact = userData.get(4);
        ArrayList<String> hobbies = selectedHobbies;

        final JSONObject root = new JSONObject();
        try {
            root.put("Fname", Fname);
            root.put("Lname", Lname);
            root.put("Contact", Contact);
            root.put("Email", Email);
            root.put("Passwrd", Passwrd);
            root.put("Hobbies", hobbies);
            return root.toString(1);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    private void sendDataServer(ArrayList<String> userData, ArrayList<String> selectedHobbies) {
        final String json = formatDataAsJson(userData, selectedHobbies);
        System.out.println("Sending data to server");
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            System.out.println("Done SEnding data");
//            Intent intent = new Intent(HobbyActivity.this, UserHomeActivity.class);
//            intent.putStringArrayListExtra("userData", selectedHobbies);
//            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private String getServerResponse(String json) {
//
//        return "Unable to contact server";
//    }

/*
    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                System.out.println("http connection ****** httpURLConnection" + params[0] + "*****");
                httpURLConnection.setRequestMethod("POST");
                System.out.print(httpURLConnection);
                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes("PostData=" + params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            String resultString = "";
            try {
                resultString = new JSONObject(result).getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray hobbiesList = null;
            ArrayList<String> hobbiesListStrings = new ArrayList<String>();
            try {
                System.out.println("************ Result value is" + result + "********");
                hobbiesList = new JSONObject(result).getJSONArray("Hobbies");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < hobbiesList.length(); i++) {
                try {
                    System.out.println(hobbiesList.getString(i));
                    hobbiesListStrings.add(hobbiesList.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // server response comes here
            if (resultString.equalsIgnoreCase("Successful")) {
                Intent intent = new Intent(HobbyActivity.this, UserHomeActivity.class);
                intent.putStringArrayListExtra("userData", hobbiesListStrings);
                startActivity(intent);
            }
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
*/

}
