package emergency.com.uemergency;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import android.support.annotation.NonNull;


import java.util.List;
import java.util.Locale;

public class MainActivity  extends AppCompatActivity {

    ImageView viewpolice;
    ImageView viewhealth;
    ImageView viewfire;
    double latti=0;
    double longi=0;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpolice= (ImageView)findViewById(R.id.imageView2);
        viewhealth=(ImageView)findViewById(R.id.imageView1);
        viewfire=(ImageView) findViewById(R.id.imageView3);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();






        viewpolice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sp1=getSharedPreferences("Login", 0);;

                String unm=sp1.getString("Unm", null);
                //v.getId() will give you the image id


                new HttpAsyncTask().execute("http://192.168.1.4:8080/UEmergency/webresources/sosuser/"+longi+"/"+latti+"/"+2+"/"+unm);


                Toast.makeText(getApplicationContext(),
                        "Alert to the police is send Successfully ",
                        Toast.LENGTH_SHORT).show();

            }
        });


        viewhealth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sp1=getSharedPreferences("Login", 0);;

                String unm=sp1.getString("Unm", null);
                //v.getId() will give you the image id


                new HttpAsyncTask().execute("http://192.168.1.4:8080/UEmergency/webresources/sosuser/"+longi+"/"+latti+"/"+1+"/"+unm);


                Toast.makeText(getApplicationContext(),
                        "Alert to the hospital is send Successfully ",
                        Toast.LENGTH_SHORT).show();

            }
        });

        viewfire.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences sp1 = getSharedPreferences("Login", 0);
                ;

                String unm = sp1.getString("Unm", null);
                //v.getId() will give you the image id


                new HttpAsyncTask().execute("http://192.168.1.4:8080/UEmergency/webresources/sosuser/"+longi+"/"+latti+"/"+3+"/"+unm);


                Toast.makeText(getApplicationContext(),
                        "Alert to the FireFighter is send Successfully ",
                        Toast.LENGTH_SHORT).show();

            }
        });
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

        }
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




    void getLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                 latti = location.getLatitude();
                 longi = location.getLongitude();

                Toast.makeText(getApplicationContext(),
                        "lattitude= "+latti+" longitude="+longi,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Unable to find correct location. ",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

}
