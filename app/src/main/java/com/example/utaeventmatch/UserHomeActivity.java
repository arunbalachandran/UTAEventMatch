package com.example.utaeventmatch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.utaeventmatch.R.id.parent;

/**
 * Created by arun on 01/10/17.
 */

public class UserHomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, AdapterView.OnItemClickListener, View.OnClickListener {

    ArrayList<String> finalOutput = new ArrayList<String>();
    Button datePick;
    TextView textResult;
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    private Button checkedListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);
        ArrayList<String> hobbies = getIntent().getStringArrayListExtra("userData");
        ListView hobbiesList = (ListView) findViewById(R.id.checkableList12);
        hobbiesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String[] Items = hobbies.toArray(new String[hobbies.size()]);
        checkedListBtn = (Button) findViewById(R.id.checkedListButton12);
        checkedListBtn.setOnClickListener(this);
//        String[] Items = {"Badminton", "Cycling", "Swimming"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.user_rowlayout, R.id.checkedTextUser, Items);
        hobbiesList.setAdapter(adapter);
        hobbiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = ((TextView) view).getText().toString();
//              sendDataServer();
            }
        });

        datePick = (Button) findViewById(R.id.datePickerButton);
        textResult = (TextView) findViewById(R.id.dateTextView);

        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserHomeActivity.this, UserHomeActivity.this,
                        year, month, day);
                datePickerDialog.show();
            }
        });


        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UserHomeActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.hobbies));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(myAdapter);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2 + 2;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(UserHomeActivity.this, UserHomeActivity.this,
                hour, minute, true);  // what the hell did I pass as the last parameter?
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hourFinal = i;
        minuteFinal = i1;
        textResult.setText(yearFinal + "\n" + monthFinal + "\n" + dayFinal + "\n" + hourFinal + "\n" + minuteFinal);
        finalOutput.add(yearFinal+"-"+monthFinal+"-"+dayFinal);
        finalOutput.add(hourFinal+":"+minuteFinal);
        finalOutput.add("0");
    }

    public String formatDataAsJson(ArrayList<String> finalOutput) {
        // userData.get(0) = userName;
        String Date = finalOutput.get(0);
        String Time = finalOutput.get(1);
        String Flag = finalOutput.get(2);
        String Hobby = finalOutput.get(3);

        final JSONObject root = new JSONObject();
        try {
            root.put("Date", Date);
            root.put("Time", Time);
            root.put("Flag", Flag);
            root.put("Hobby", Hobby);
            return root.toString(1);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    private void submitDataServer() {
        final String json = formatDataAsJson(finalOutput);
        try {
            new SendDeviceDetails().execute("http://ec2-34-228-53-107.compute-1.amazonaws.com/query", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String itemSelected = adapterView.getItemAtPosition(i).toString();
        finalOutput.add(itemSelected);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == checkedListBtn.getId()) {
            submitDataServer();
        }
    }

    private class SendDeviceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

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
            String resultString = "";
            try {
                resultString = new JSONObject(result).getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray hobbiesList = null;
            ArrayList<String> hobbiesListStrings = new ArrayList<String>();
            try {
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
                Toast toast = new Toast(getApplicationContext());
                toast.makeText(UserHomeActivity.this, "Received confirmation", Toast.LENGTH_SHORT).show();
            }
            Log.e("TAG", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
}
