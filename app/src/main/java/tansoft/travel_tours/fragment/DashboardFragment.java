package tansoft.travel_tours.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tansoft.travel_tours.R;
import tansoft.travel_tours.activity.preference.SettingsActivity;

public class DashboardFragment extends Fragment {
    private View contentView;
    private TextView weatherIcon;
    Typeface weatherFont;

    ImageView booking,weather_icon,configure,resort_search;

    private Button preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        contentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        booking = contentView.findViewById(R.id.booking);

        weather_icon = contentView.findViewById(R.id.weather_icon);

        configure = contentView.findViewById(R.id.configure);

        resort_search = contentView.findViewById(R.id.resort_search);



    weather_icon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager settingsFm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = settingsFm.beginTransaction();
            ft.replace(R.id.frag_container, new WeatherFragment());
            ft.addToBackStack("WeatherFragment");
            ft.commit();
        }
    });

        configure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);


            }
        });
        resort_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager settingsFm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = settingsFm.beginTransaction();
                ft.replace(R.id.frag_container, new SearchFragment());
                ft.addToBackStack("SearchFragment");
                ft.commit();
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager settingsFm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = settingsFm.beginTransaction();
                ft.replace(R.id.frag_container, new BookingFragment());
                ft.addToBackStack("BookingFragment");
                ft.commit();
            }
        });




        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

}
