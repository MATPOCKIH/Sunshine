package com.example.android.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aaronskiy on 28.07.2016.
 */

public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
    public Locale CURRENT_LOCALE = new Locale("ru", "RU");

    @Override
    protected void onPostExecute(String[] strings) {
        super.onPostExecute(strings);
        Log.d(LOG_TAG, "onPostExecute");
    }

    @Override
    protected String[] doInBackground(String... params) {

        // Переменные нужно открыть вне блока try/catch
        // соединение нужно будет закрыть в блоке finally
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Строка будет содержать ответ сервера в формате JSON
        String forecastJsonStr = null;

        String[] weatherDataFormatted = null;

        String format = "json";
        String units = "metric";
        String language = "ru";
        int numDays = 14;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            if (params.length == 0) {
                return null;
            }

            Uri.Builder apiUri = new Uri.Builder();
            apiUri.scheme("http");
            apiUri.authority("api.openweathermap.org")
                    .path("/data/2.5/forecast/daily")
                    .appendQueryParameter("q", params[0])
                    .appendQueryParameter("mode", format)
                    .appendQueryParameter("units", units)
                    .appendQueryParameter("cnt", Integer.toString(numDays))
                    .appendQueryParameter("lang", language)
                    .appendQueryParameter("APPID", BuildConfig.OPEN_WEATHER_MAP_API_KEY);

            URL url = new URL(apiUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            // Получаем массив из форматированных строк c прогнозом для помещения в ListView
            weatherDataFormatted = getWeatherDataFromJson(forecastJsonStr, numDays);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return weatherDataFormatted;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



    private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays) throws JSONException {
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMP = "temp";
        final String OWM_MIN = "min";
        final String OWM_MAX = "max";
        final String OWM_DESCRIPTION = "description";

        String[] forecastResultArray = new String[numDays];

        JSONObject forecastJSon = new JSONObject(forecastJsonStr);
        JSONArray forecastArray = forecastJSon.getJSONArray(OWM_LIST);

        for (int i = 0; i < forecastArray.length(); i++) {

            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Вся информация о прогнозе на день
            JSONObject daylyTempForecast = forecastArray.getJSONObject(i);

            // Дата в человеко понятном формате
            day = getReadableDateString(daylyTempForecast.getLong("dt"), CURRENT_LOCALE);

            // Вся информация о температуре на день
            JSONObject tempObject = daylyTempForecast.getJSONObject(OWM_TEMP);

            double dayMax = tempObject.getDouble(OWM_MAX);
            double dayMin = tempObject.getDouble(OWM_MIN);
            highAndLow = formatHighAndLow(dayMax, dayMin);

            // Вся информация о погоде на день
            JSONArray weatherArray = daylyTempForecast.getJSONArray(OWM_WEATHER);
            // Вся информация о погоде на день
            JSONObject daylyWeatherForecast = weatherArray.getJSONObject(0);

            // Информация солнечно, облачно и т.п.
            description = daylyWeatherForecast.getString(OWM_DESCRIPTION);

            // Формируем строку
            forecastResultArray[i] = day + " - " + description + " - " + highAndLow;
        }

        return forecastResultArray;
    }

    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(long time, Locale local) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.

        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE, MMM dd", local);
        return shortenedDateFormat.format(new Date(time * 1000));
    }

    /**
     * @param high Maximum temp for a day
     * @param low  minimum temp for a day
     * @return
     */
    private String formatHighAndLow(Double high, Double low) {
        String returnHighAndLowTemp = Math.round(high) + "°/" + Math.round(low) + "°";
        return returnHighAndLowTemp;
    }
}