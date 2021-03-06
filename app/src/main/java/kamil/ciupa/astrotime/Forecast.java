package kamil.ciupa.astrotime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kamil on 2017-06-15.
 */

public class Forecast extends Fragment {

    View view;
    TextView day1;
    TextView day2;
    TextView day3;
    TextView day4;
    String d1 = "initial";
     String d2;
     String d3;
     String d4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_forecast, container, false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5000);
                       getData(((MainActivity) getActivity()).getD1(),
                                ((MainActivity) getActivity()).getD2(),
                                ((MainActivity) getActivity()).getD3(),
                                ((MainActivity) getActivity()).getD4());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
        return view;
    }




    public void getData(String d1, String d2, String d3, String d4){

        this.d1 = d1;
        this.d2 = d2;
        this.d3 = d3;
        this.d4 = d4;
       // setData(this.d1, this.d2, this.d3, this.d4);
        update();
    }
    public void update(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setData(d1,
                        d2,
                        d3,
                        d4
                        );
            }
        });
            }


    public void initiateElements(View view){
        day1 = (TextView) view.findViewById(R.id.tFor1);
        day2 = (TextView) view.findViewById(R.id.tFor2);
        day3 = (TextView) view.findViewById(R.id.tFor3);
        day4 = (TextView) view.findViewById(R.id.tFor4);
    }


    public void setData(String d1, String d2, String d3, String d4){

        initiateElements(view);

        day1.setText(d1);
        day2.setText(d2);
        day3.setText(d3);
        day4.setText(d4);

    }











}
