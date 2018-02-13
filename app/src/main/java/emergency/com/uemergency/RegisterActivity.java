package emergency.com.uemergency;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by MFEYET Daniel Steven on 28/03/2017.
 */
public class RegisterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    Button signup;
    EditText name;
    String nametxt;
    EditText FirstName;
    String firstnametxt;
    EditText numcni;
    String cni;
    EditText phone;
    Integer phonenumber;
    EditText phonesec;
    Integer phones;
    EditText datebirth;
    String datetxt;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    Spinner pays;
    String paystxt;
    Spinner sex;
    String sextxt;
    TextView errorMsg;
    DatePickerDialog date;
    SimpleDateFormat dateFormatter;
    String[] listcountry = { "Cameroun", "Nigeria", "Togo", "Guinnea", "Congo",  };
    String[] listsex = { "Male", "Female"  };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp1=getSharedPreferences("Login",0);

        String unm=sp1.getString("Unm", null);
        if(unm !=null){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }

        setContentView(R.layout.register);
        signup = (Button) findViewById(R.id.signup);
        name=(EditText)findViewById(R.id.username);



        pays=(Spinner)findViewById(R.id.pays);
       pays.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listcountry);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pays.setAdapter(aa);

        sex=(Spinner)findViewById(R.id.sex);
        sex.setOnItemSelectedListener(this);
        ArrayAdapter ab = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listsex);
        ab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(ab);


        FirstName=(EditText)findViewById(R.id.txtfirstname);
        phone=(EditText)findViewById(R.id.phone);
        phonesec=(EditText)findViewById(R.id.numsec);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        datebirth=(EditText)findViewById(R.id.date);
        datebirth.setInputType(InputType.TYPE_NULL);

        datebirth.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View arg0) {
               date.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        date = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                datebirth.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        numcni =(EditText)findViewById(R.id.cni);

        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                nametxt=name.getText().toString();
                firstnametxt=FirstName.getText().toString();
                paystxt=pays.getSelectedItem().toString();
                phonenumber=Integer.parseInt(phone.getText().toString());
                datetxt=datebirth.getText().toString();
                cni=numcni.getText().toString();
                phones=Integer.parseInt(phonesec.getText().toString());
                sextxt=sex.getSelectedItem().toString();




                // Invoke RESTful Web Service with Http parameters
                new HttpAsyncTask().execute("http://172.16.100.244:8080/UEmergency/webresources/sosuser/"+nametxt+"/"+firstnametxt+"/"+paystxt+"/"+phonenumber+"/"+cni+"/"+sextxt+"/"+phones);
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("Unm",cni );

                Ed.commit();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public static String GET(String url){

        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream


            // convert inputstream to string


        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return "Work";
    }




    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),
                    "Successfully Logged in with "+nametxt,
                    Toast.LENGTH_SHORT).show();

        }
    }
}
