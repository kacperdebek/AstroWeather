package com.example.astroweather;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainWeatherFragment extends Fragment {

    private View view;
    private APICaller apiCaller = new APICaller();
    TextView weather;
    TextView pressure;
    TextView temperature;
    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mainweather, container, false);

        weather = view.findViewById(R.id.weatherDescription);
        pressure = view.findViewById(R.id.pressure);
        temperature = view.findViewById(R.id.temperature);
        imageView = view.findViewById(R.id.weatherIcon);

        FloatingSearchView floatingSearchView = view.findViewById(R.id.floating_search_view);
        List<SearchSuggestion> newSuggestions = new ArrayList<>();
        floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
            floatingSearchView.swapSuggestions(getPossibleStrings(newSuggestions, newQuery));
        });
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                newSuggestions.add(new Suggestions(currentQuery));
                try {
                    apiCaller.callApi(currentQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    @SuppressLint("ParcelCreator")
    private static class Suggestions implements SearchSuggestion {
        private String name;

        Suggestions(String name) {
            this.name = name;
        }

        @Override
        public String getBody() {
            return name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }

    public static List<SearchSuggestion> getPossibleStrings(List<SearchSuggestion> strings, String query) {
        List<SearchSuggestion> result = new ArrayList<>();
        for (SearchSuggestion s : strings) {
            if (s.getBody().startsWith(query))
                result.add(s);
        }
        return result;
    }

    private void sendMessage(String windDirection, String windSpeed, String humidity, String visibility) {
        EventBus.getDefault().post(new ApiRespondedEvent(windDirection, windSpeed, humidity, visibility));
    }

    private void sendForecast(Map<Integer, ArrayList<String>> forecasts) {
        EventBus.getDefault().post(new ApiRespondedEvent(forecasts));
    }

    private class APICaller {
        private OkHttpClient client;
        private Request todayWeatherRequest;
        private Request weatherForecastRequest;
        private Response todayWeatherResponse = null;
        private Response weatherForecastResponse = null;

        public void callApi(String cityName) throws Exception {
            client = new OkHttpClient();
            todayWeatherRequest = createRequest(cityName, "weather");
            weatherForecastRequest = createRequest(cityName, "forecast");
            new ExecuteCallTask().execute();
            new ExecuteForecastCallTask().execute();
        }

        class ExecuteCallTask extends AsyncTask<String, Void, Void> {
            protected Void doInBackground(String... urls) {
                try {
                    todayWeatherResponse = client.newCall(todayWeatherRequest).execute();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @SuppressLint("SetTextI18n")
            protected void onPostExecute(Void param) {
                if (todayWeatherResponse != null) {
                    try {
                        JSONObject todayResponse = new JSONObject(todayWeatherResponse.body().string());
                        JSONArray weatherArray = todayResponse.getJSONArray("weather");
                        JSONObject detailsObject = todayResponse.getJSONObject("main");
                        JSONObject windObject = todayResponse.getJSONObject("wind");
                        int vis = todayResponse.getInt("visibility");

                        Picasso.get().load("http://openweathermap.org/img/wn/" + weatherArray.getJSONObject(0).getString("icon") + "@2x.png").into(imageView);

                        weather.setText("Weather: " + weatherArray.getJSONObject(0).getString("description"));
                        pressure.setText("Pressure: " + detailsObject.getString("pressure") + " hPa");
                        temperature.setText("Temperature: " + detailsObject.getString("temp") + "°C");

                        sendMessage(windObject.getString("deg"), windObject.getString("speed"), detailsObject.getString("humidity"), Integer.toString(vis));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("null response");
                }
            }
        }

        class ExecuteForecastCallTask extends AsyncTask<String, Void, Void> {
            JSONObject forecastResponse;

            @Override
            protected Void doInBackground(String... strings) {
                try {
                    weatherForecastResponse = client.newCall(weatherForecastRequest).execute();
                    forecastResponse = new JSONObject(weatherForecastResponse.body().string());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @SuppressLint("SetTextI18n")
            protected void onPostExecute(Void param) {
                if (weatherForecastResponse != null) {
                    try {

                        JSONArray forecastList = forecastResponse.getJSONArray("list");
                        Map<Integer, ArrayList<String>> forecasts = new HashMap<>();
                        for (int i = 0, j = 0; i < forecastList.length(); i++) {
                            if (i % 8 == 0) {
                                ArrayList<String> details = new ArrayList<>();
                                details.add(forecastList.getJSONObject(i).getJSONObject("main").getString("temp"));
                                details.add(forecastList.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                                System.out.println("I was here" + i);
                                forecasts.put(j++, details);
                            }
                        }
                        sendForecast(forecasts);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        private Request createRequest(String cityName, String requestType) {
            HttpUrl httpUrl = new HttpUrl.Builder()
                    .scheme("https")
                    .host("api.openweathermap.org")
                    .addPathSegment("data")
                    .addPathSegment("2.5")
                    .addPathSegment(requestType)
                    .addQueryParameter("q", cityName)
                    .addQueryParameter("appid", "77540b5cd880c96ba142a09ec6717938")
                    .addQueryParameter("units", "metric") //TODO: units as parameter
                    .build();
            return new Request.Builder()
                    .url(httpUrl)
                    .get()
                    .build();
        }
    }

}
