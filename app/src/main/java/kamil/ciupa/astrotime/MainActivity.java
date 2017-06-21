package kamil.ciupa.astrotime;

import android.app.ProgressDialog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.BaseColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import cz.msebera.android.httpclient.Header;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    double latitude = 10.0;
    double longitude = 10.0;
    int refreshtime = 1;
    AlertDialog b;
    PagerAdapter pagerAdapter;
    ViewPager viewPager;
    List<String> fragmentsList;
    String baseurl = "https://query.yahooapis.com/v1/public/yql?q=";
    String city;
    String weatherQueryIMP;
    String weatherQueryMetric;
    WeatherInfo weatherInfo;

    String MWdesc;
    String MNkraj;
    String MWnazwaMiejsc;
    String MWdlugosc ;
    String MWszerokosc ;
    String MWtemperatura ;
    String WIwiatrSila;

    public String getD1() {
        return d1;
    }

    public String getD2() {
        return d2;
    }

    public String getD3() {
        return d3;
    }

    public String getD4() {
        return d4;
    }

    String d1;
    String d2;
    String d3;
    String d4;

    public String getMNkraj(){
        return MNkraj;
    }

    public String getMWdesc(){
        return MWdesc;
    }

    public String getWIwiatrSila() {
        return WIwiatrSila;
    }

    public String getWIwiatrKierunek() {
        return WIwiatrKierunek;
    }

    public String getWIwilgotnosc() {
        return WIwilgotnosc;
    }

    public String getWIwidocznosc() {
        return WIwidocznosc;
    }

    String WIwiatrKierunek;
    String WIwilgotnosc;
    String WIwidocznosc;

    public String getMWnazwaMiejsc() {
        return MWnazwaMiejsc;
    }

    public String getMWdlugosc() {
        return MWdlugosc;
    }

    public String getMWszerokosc() {
        return MWszerokosc;
    }

    public String getMWtemperatura() {
        return MWtemperatura;
    }

    public String getMWcisnienie() {
        return MWcisnienie;
    }

    String MWcisnienie ;
    AstroWeatherDbAdapter astroWeatherDbAdapter;
    ArrayAdapter<String> citiesAdapter;


    public int getRefTime() { return refreshtime;}
    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        fragmentsList = new ArrayList<>();
        fragmentsList.add(FragmentSun.class.getName());
        fragmentsList.add(FragmentMoon.class.getName());
        fragmentsList.add(MainWeather.class.getName());
        fragmentsList.add(WeatherInfo.class.getName());
        fragmentsList.add(Forecast.class.getName());
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new MainActivity.MyPagerAdapter(getSupportFragmentManager());
        try{
        viewPager.setAdapter(pagerAdapter);
        } catch (Exception e) {}
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        city = "london";
        weatherQueryIMP = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)&format=json";
        weatherQueryMetric ="select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)and%20u=\"c\"&format=json";







          //  getDataFromInternet();
        getData();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putDouble("longitude", longitude);
        outState.putDouble("latitude", latitude);
        outState.putInt("refresh", refreshtime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        longitude = savedInstanceState.getDouble("longitude");
        latitude = savedInstanceState.getDouble("latitude");
        refreshtime = savedInstanceState.getInt("refresh");
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0){
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showOptionsDialog();
        }
        if( id == R.id.refreshButton){
            getDataFromInternet();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getData(){
        if(accessToInternet()){
            getDataFromInternet();
        } else {
            getDataFromLocal();
        }
    }

    public boolean accessToInternet(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    private void writeToFile(String data) {
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("dane", Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String readFromFile() {
        String output = "";

        FileInputStream inputStream;
        try {
            byte[] b = new byte[3000];
            inputStream = openFileInput("dane");
            int i = inputStream.read(b);
            if(i == -1){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Local file loaded", Toast.LENGTH_LONG).show();
            }
            output = new String(b);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;


    }


    public void getDataFromLocal(){

        String response = readFromFile();

        try{
            JSONObject responsee = new JSONObject(response);
            JSONObject query = responsee.getJSONObject("query");
            JSONObject results = query.getJSONObject("results");
            JSONObject channel = results.getJSONObject("channel");

            JSONObject item = channel.getJSONObject("item");
            JSONObject condition = item.getJSONObject("condition");
            JSONObject loc = channel.getJSONObject("location");
            JSONObject atmosphere = channel.getJSONObject("atmosphere");
            JSONObject wind = channel.getJSONObject("wind");


            String descript = item.getString("description");
            String[] descArr  = descript.split("<BR />");


            d1 = descArr[6];
            d2 = descArr[7];
            d3 = descArr[8];
            d4 = descArr[9];


            MWnazwaMiejsc = loc.getString("city");
            MNkraj = loc.getString("country");
            MWcisnienie = atmosphere.getString("pressure");
            MWdlugosc = item.getString("lat");
            MWszerokosc = item.getString("long");
            MWtemperatura = condition.getString("temp");
            MWdesc = item.getString("description");



            WIwiatrKierunek = wind.getString("direction");
            WIwiatrSila = wind.getString("speed");
            WIwidocznosc = atmosphere.getString("visibility");
            WIwilgotnosc = atmosphere.getString("humidity");



        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getDataFromInternet(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(baseurl + "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)and%20u=\"c\"&format=json",
                new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                String response = new String(responseBody);
                writeToFile(response);

                try{
                    JSONObject responsee = new JSONObject(response);
                    JSONObject query = responsee.getJSONObject("query");
                    JSONObject results = query.getJSONObject("results");
                    JSONObject channel = results.getJSONObject("channel");

                    JSONObject item = channel.getJSONObject("item");
                    JSONObject condition = item.getJSONObject("condition");
                    JSONObject loc = channel.getJSONObject("location");
                    JSONObject atmosphere = channel.getJSONObject("atmosphere");
                    JSONObject wind = channel.getJSONObject("wind");


                    String descript = item.getString("description");
                    String[] descArr  = descript.split("<BR />");


                    d1 = descArr[6];
                    d2 = descArr[7];
                    d3 = descArr[8];
                    d4 = descArr[9];


                    MWnazwaMiejsc = loc.getString("city");
                    MNkraj = loc.getString("country");
                    MWcisnienie = atmosphere.getString("pressure");
                    MWdlugosc = item.getString("lat");
                    MWszerokosc = item.getString("long");



                    MWtemperatura = condition.getString("temp");
                    MWdesc = item.getString("description");



                    WIwiatrKierunek = wind.getString("direction");
                    WIwiatrSila = wind.getString("speed");
                    WIwidocznosc = atmosphere.getString("visibility");
                    WIwilgotnosc = atmosphere.getString("humidity");



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    public void saveToDB(String cityName){
        astroWeatherDbAdapter.insertCity(cityName);
    }

    public void loadFromDB(){
        List<CityDataModel> citiesList = astroWeatherDbAdapter.getAllCities();
        if(citiesList.isEmpty()){
            citiesList.add(new CityDataModel("Lodz"));
        }

        List<String> stringCitiesList = new ArrayList<>();

        for(CityDataModel row : citiesList){
            stringCitiesList.add(row.getCityName());
        }

        citiesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringCitiesList);
    }

    /*
    Shows dialog with options. Allow to change longitude, latitude and frequency of refresh data.
     */
    private void showOptionsDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.options_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Change parametes");
        dialogBuilder.setMessage("Please give data");

        b = dialogBuilder.create();
        Button a = (Button) dialogView.findViewById(R.id.bOK);
        Button fav = (Button) dialogView.findViewById(R.id.buttAddFav);
        final EditText cityet = (EditText) dialogView.findViewById(R.id.CityET);
        astroWeatherDbAdapter = new AstroWeatherDbAdapter(this);
        astroWeatherDbAdapter.open();
        loadFromDB();
        Spinner dropdown = (Spinner) dialogView.findViewById(R.id.spinner);
        Spinner favSpinner = (Spinner) dialogView.findViewById(R.id.spinFav);
       favSpinner.setAdapter(citiesAdapter);
        cityet.setText(city);
//
        fav.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveToDB(cityet.getText().toString());
            }
        });

        a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            try {
                city = cityet.getText().toString();
                getData();
                b.dismiss();
            } catch (NumberFormatException e){
                Toast.makeText(MainActivity.this, "Błędne dane" , Toast.LENGTH_SHORT).show();
            }
            }
        });
        b.show();
    }



    private class MyPagerAdapter extends FragmentPagerAdapter {

        public List<String> fragmentsListAdapter = new ArrayList<>();
        private int NUM_COUNT = 5;


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            fragmentsListAdapter = fragmentsList;
        }

        @Override
        public int getCount() {
            return NUM_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(getBaseContext(), fragmentsListAdapter.get(position));
        }

    }
}




