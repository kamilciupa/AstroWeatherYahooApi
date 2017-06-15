package kamil.ciupa.astrotime;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentMoon extends Fragment {


    View view;
    TextView wschodTime;
    TextView zachodTime;
    TextView newMoon;
    TextView fullMoon;
    TextView phase;
    TextView lunear;
    TextView clock;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_moon, container, false);
        setMoonData(10.0, 10.0, view);
        clock = (TextView) view.findViewById(R.id.tTimeMoon);

        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar calendar = Calendar.getInstance();
                clock.setText(String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND)));
            }
            public void onFinish() {
            }
        };
        newtimer.start();
        try {
            Timer autoUpdate = new Timer();
            autoUpdate.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                setMoonData(((MainActivity) getActivity()).getLatitude(), ((MainActivity) getActivity()).getLongitude(), view);
                            }
                        });
                    } catch(Exception e) {}
                }

            }, 0,300*((MainActivity) getActivity()).getRefTime());
        } catch(Exception e) {}

        return view;
    }




//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//    }

    public void initiateElements(View view){

        wschodTime = (TextView) view.findViewById(R.id.tMoonWschodCzasWart);
        zachodTime = (TextView) view.findViewById(R.id.tMoonZachodCzasWart);
        newMoon = (TextView) view.findViewById(R.id.tMoonNowWart);
        fullMoon = (TextView) view.findViewById(R.id.tMoonpelniaWart);
        phase = (TextView) view.findViewById(R.id.tMoonFazaWart);
        lunear = (TextView) view.findViewById(R.id.tMoonDzienWart);
    }


    public void setMoonData(double latitude, double longitude, View view){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int timezoneOffset = c.get(Calendar.ZONE_OFFSET);

        AstroCalculator calculator = new AstroCalculator(new AstroDateTime(year, month, day + 1, hour, minute, second, timezoneOffset, true),
                new AstroCalculator.Location(latitude, longitude));

        initiateElements(view);

        wschodTime.setText(calculator.getMoonInfo().getMoonrise().toString().substring(10,16));
        zachodTime.setText(calculator.getMoonInfo().getMoonset().toString().substring(10,16));
        newMoon.setText(calculator.getMoonInfo().getNextNewMoon().toString().substring(0,9));
        fullMoon.setText(calculator.getMoonInfo().getNextFullMoon().toString().substring(0,9));
        phase.setText(Double.toString(Math.floor(calculator.getMoonInfo().getIllumination() * 100) / 100) + "%");
        lunear.setText(Double.toString(Math.floor(calculator.getMoonInfo().getAge() * 100) / 100));

    }
}