package kamil.ciupa.astrotime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Kamil on 2017-06-16.
 */

public class WeatherInfo extends Fragment {


    View view;
    TextView wiatrSila;
    TextView wiatrKierunek;
    TextView wilgotnosc;
    TextView widocznosc;
    TextView miastoo;
    String wiatrKier;
     String wiatrSil;
     String widocz;
     String wilgo;
    String kraj;
     String miasto = "initial";



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_weather_info, container, false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5000);
                        getData(((MainActivity) getActivity()).getWIwiatrKierunek(),
                                ((MainActivity) getActivity()).getWIwiatrSila(),
                                ((MainActivity) getActivity()).getWIwidocznosc(),
                                ((MainActivity) getActivity()).getWIwilgotnosc(),
                                ((MainActivity) getActivity()).getMNkraj(),
                                ((MainActivity) getActivity()).getMWnazwaMiejsc());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


        return view;
    }


    public void getData(String wiatrKier, String wiatrSil, String widocz, String wilgo,String kraj, String miasto){

        this.wiatrKier = wiatrKier;
        this.wiatrSil = wiatrSil;
        this.widocz = widocz;
        this.wilgo = wilgo;
        this.kraj = kraj;
        this.miasto = miasto;
       update();
    }

    public void update(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                setData(wiatrKier,
                        wiatrSil,
                        widocz,
                        wilgo,
                        kraj,
                        miasto,
                        view);
            }
        });
    }



    public void initializeElements(View view){
        wiatrSila = (TextView) view.findViewById(R.id.tWiatrSilaWart);
        wiatrKierunek = (TextView) view.findViewById(R.id.tKierunekWart);
        wilgotnosc = (TextView) view.findViewById(R.id.tWilgotWart);
        widocznosc = (TextView) view.findViewById(R.id.tWidoczWart);
        miastoo = (TextView) view.findViewById(R.id.tMiastoInfo);
    }

    public void setData(String wiatrKier, String wiatrSil, String widocz, String wilgo,String kraj, String miasto ,View view){


        initializeElements(view);
        wiatrSila.setText(wiatrSil);
        wiatrKierunek.setText(wiatrKier);
        wilgotnosc.setText(wilgo);
        widocznosc.setText(widocz);
        miastoo.setText(miasto + ", " + kraj);

    }

}
