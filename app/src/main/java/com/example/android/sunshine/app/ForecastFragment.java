package com.example.android.sunshine.app;

/**
 * Created by aaronskiy on 28.07.2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {

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

        // Создаем фоновый поток для загрузки данных
        FetchWeatherTask myTask = new FetchWeatherTask();
        myTask.execute();

        return rootView;
    }
}