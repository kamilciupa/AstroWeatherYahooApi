package kamil.ciupa.astrotime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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













}
