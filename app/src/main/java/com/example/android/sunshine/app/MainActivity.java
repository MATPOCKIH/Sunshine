package com.example.android.sunshine.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            String[] data = {
                "Сегодня - солнечно - 25/21",
                "Завтра - переменная облачность - 22/19",
                "Послезавтра - Пасмурно - 22/17",
                "27.07 - пасмурно - 24/19",
                "28.07 - солнечно - 25/21",
                "29.07 - переменная облачность - 22/19",
                "30.07 - Пасмурно - 22/17",
                "31.07 - пасмурно - 24/19",
                "01.08 - переменная облачность - 22/19",
                "02.08 - Пасмурно - 22/17",
                "02.08 - гроза - Дождь - 17/16"
            };

            List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

            // Адаптер, который будет управлять выводом данных в listView
            ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

            ListView lView = (ListView) rootView.findViewById(R.id.listview_forecast);

            lView.setAdapter(stringArrayAdapter);

            return rootView;
        }
    }
}
