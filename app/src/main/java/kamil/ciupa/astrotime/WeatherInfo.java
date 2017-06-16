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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_weather_info, container, false);


        try {
            Timer autoUpdate = new Timer();
            autoUpdate.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                setData(((MainActivity) getActivity()).getWIwiatrKierunek(),
                                        ((MainActivity) getActivity()).getWIwiatrSila(),
                                        ((MainActivity) getActivity()).getWIwidocznosc(),
                                        ((MainActivity) getActivity()).getWIwilgotnosc(),
                                        view);
                            }
                        });
                    } catch(Exception e) {}
                }

            }, 0, 60000);
        } catch(Exception e) {}


        return view;
    }


    public void initializeElements(View view){
        wiatrSila = (TextView) view.findViewById(R.id.tWiatrSilaWart);
        wiatrKierunek = (TextView) view.findViewById(R.id.tKierunekWart);
        wilgotnosc = (TextView) view.findViewById(R.id.tWilgotWart);
        widocznosc = (TextView) view.findViewById(R.id.tWidoczWart);
    }

    public void setData(String wiatrKier, String wiatrSil, String widocz, String wilgo, View view){


        initializeElements(view);
        wiatrSila.setText(wiatrSil + " mph");
        wiatrKierunek.setText(wiatrKier);
        wilgotnosc.setText(wilgo);
        widocznosc.setText(widocz);


    }

}
