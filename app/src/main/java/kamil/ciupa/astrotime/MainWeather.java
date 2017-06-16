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
 * Created by Kamil on 2017-06-15.
 */

public class MainWeather extends Fragment {

    View view;
    TextView nazwaMiejsc;
    TextView dlugosc;
    TextView szerokosc;
    TextView temperatura;
    TextView cisnienie;
    TextView opis;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_main_weather, container, false);


        try {
            Timer autoUpdate = new Timer();
            autoUpdate.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                setData(((MainActivity) getActivity()).getMWcisnienie(),
                                        ((MainActivity) getActivity()).getMWdlugosc(),
                                        ((MainActivity) getActivity()).getMWszerokosc(),
                                        ((MainActivity) getActivity()).getMWnazwaMiejsc(),
                                        ((MainActivity) getActivity()).getMWtemperatura(),
                                        (((MainActivity) getActivity()).getMWdesc())
                                        , view);
                            }
                        });
                    } catch(Exception e) {}
                }

            }, 0, 60000);
        } catch(Exception e) {}


        return view;
    }



    public void initiateElements(View view){
        nazwaMiejsc = (TextView) view.findViewById(R.id.tNameCity);
        dlugosc = (TextView) view.findViewById(R.id.tDluWart);
        szerokosc = (TextView) view.findViewById(R.id.tSzerWart);
        temperatura = (TextView) view.findViewById(R.id.tTempWart);
        cisnienie = (TextView) view.findViewById(R.id.tCisWart);
        opis = (TextView) view.findViewById(R.id.tOpis);
    }


    public void setData(String cis, String dl, String szer, String nazwa, String temp, String desc, View view){

        initiateElements(view);

        nazwaMiejsc.setText(nazwa);
        dlugosc.setText(dl);
        szerokosc.setText(szer);
        temperatura.setText(temp + " F");
        cisnienie.setText(cis + " in");
        //opis.setText(desc);

    }











}
