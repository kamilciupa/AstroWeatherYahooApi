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

public class MainWeather extends Fragment {

    View view;
    TextView nazwaMiejsc;
    TextView dlugosc;
    TextView szerokosc;
    TextView temperatura;
    TextView cisnienie;
    TextView opis;
int i =  1 ;

    String cis;
    String dl;
    String szer;
    String nazwa = "initial";
    String kraj;
    String temp;
    String desc;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_main_weather, container, false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(5000);
                        getData(((MainActivity) getActivity()).getMWcisnienie(),
                                ((MainActivity) getActivity()).getMWdlugosc(),
                                ((MainActivity) getActivity()).getMWszerokosc(),
                                ((MainActivity) getActivity()).getMWnazwaMiejsc(),
                                ((MainActivity) getActivity()).getMNkraj(),
                                ((MainActivity) getActivity()).getMWtemperatura(),
                                (((MainActivity) getActivity()).getMWdesc()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();



        return view;
    }

    public void getData(String cis, String dl, String szer, String nazwa, String kraj, String temp, String desc){

        this.cis = cis;
        this.dl = dl;
        this.szer = szer;
        this.nazwa = nazwa;
        this.kraj = kraj;
        this.temp = temp;
        this.desc = desc;
        //setData(this.cis, this.dl, this.szer, this.nazwa, this.kraj, this.temp, this.desc);
        update();
        }
    public void update(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setData(cis,
                        dl,
                        szer,
                        nazwa,
                        kraj,
                        temp,
                        desc);
            }
        });
    }

    public void initiateElements(View view){
        nazwaMiejsc = (TextView) view.findViewById(R.id.tNameCity);
        dlugosc = (TextView) view.findViewById(R.id.tDluWart);
        szerokosc = (TextView) view.findViewById(R.id.tSzerWart);
        temperatura = (TextView) view.findViewById(R.id.tTempWart);
        cisnienie = (TextView) view.findViewById(R.id.tCisWart);
       // opis = (TextView) view.findViewById(R.id.tOpis);
    }


    public void setData(String cis, String dl, String szer, String nazwa, String kraj, String temp, String desc){

        initiateElements(view);

        nazwaMiejsc.setText(nazwa + ", " + kraj);
        dlugosc.setText(dl);
        szerokosc.setText(szer);
        temperatura.setText(temp);
        cisnienie.setText(cis);
        //opis.setText(desc);

    }











}
