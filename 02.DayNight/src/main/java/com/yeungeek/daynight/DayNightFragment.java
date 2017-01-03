package com.yeungeek.daynight;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DayNightFragment extends Fragment {
    private String title;
    private int page;

    public static DayNightFragment newInstance(int page, String title) {
        DayNightFragment fragmentFirst = new DayNightFragment();
        Bundle args = new Bundle();
        args.putInt("_int", page);
        args.putString("_title", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = getArguments().getInt("_int", 0);
        title = getArguments().getString("_title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_night, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.id_tv);
        tvLabel.setText(page + " -- " + title);
        return view;
    }
}
