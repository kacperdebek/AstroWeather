package com.example.astroweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public class MainWeatherFragment extends Fragment {

    private View view;
    private APICaller apiCaller = new APICaller();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_mainweather, container, false);
        TextView weather = view.findViewById(R.id.textView);
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
                    apiCaller.callApi(weather, currentQuery);
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
}
