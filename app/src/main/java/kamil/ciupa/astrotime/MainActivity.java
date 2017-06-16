package kamil.ciupa.astrotime;

import android.os.Bundle;
import android.os.PersistableBundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
    String weatherQuery;

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
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new MainActivity.MyPagerAdapter(getSupportFragmentManager());
        try{
        viewPager.setAdapter(pagerAdapter);
        } catch (Exception e) {}
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        city = "lodz";
        weatherQuery = "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+city+"%22)&format=json";
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
        return super.onOptionsItemSelected(item);
    }





    private void getDataFromAPI(){

        // isOnline
            // get city from option
        //String cityName = "lodz";
       



        //AsyncHttpClient


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
        final EditText lt = (EditText) dialogView.findViewById(R.id.LatitudeET);
        final EditText lg = (EditText) dialogView.findViewById(R.id.LongitudeET);
        final EditText refTime = (EditText) dialogView.findViewById(R.id.RefreshTimeET);

                    lt.setText(Double.toString(latitude));
                    lg.setText(Double.toString(longitude));
                    refTime.setText(Integer.toString(refreshtime));

        a.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            try {
                double lattest;
                double lontest;
                lattest = Double.parseDouble(lt.getText().toString());
                lontest = Double.parseDouble(lg.getText().toString());
                refreshtime = Integer.parseInt(refTime.getText().toString());
                if(lattest >= -90 && lattest <= 90 &&  lontest >= -180 && lontest <= 180){
                    latitude = lattest;
                    longitude = lontest;
                    b.dismiss();
                } else {
                    latitude = 0;
                    longitude = 0;
                    Toast.makeText(MainActivity.this, "Wpisz poprawną wartość", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e){
                Toast.makeText(MainActivity.this, "Błędne dane" , Toast.LENGTH_SHORT).show();
            }

            }
        });
        b.show();
    }



    private class MyPagerAdapter extends FragmentPagerAdapter {

        public List<String> fragmentsListAdapter = new ArrayList<>();
        private int NUM_COUNT = 4;
        double longitude, latitude;

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

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return super.getPageTitle(position);
//        }
//    }
    }
}



