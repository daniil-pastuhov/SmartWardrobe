package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.List;
import java.util.Map;

import by.genlife.fancycoverflow.FancyCoverFlow;
import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.ViewGroupApparelAdapter;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.Parameters;
import by.genlife.smartwardrobe.data.WardrobeManager;
import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;

/**
 * Created by NotePad.by on 07.05.2015.
 */

public class TravelFragment extends Fragment {
    public static final String TAG = TravelFragment.class.getSimpleName();
    private WardrobeManager manager;

    Context context;
    Spinner styles;
    Spinner colors;
    CheckBox cbStyle, cbColor, cbNew, cbClean, cbForWeater;
    View layoutParams;
    Button showParams, find;
    ProgressBar progressBar;
    View content;
    FancyCoverFlow fancyCoverFlow;
    boolean isApparelLoaded = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.travel_fragment, container, false);
        this.context = inflater.getContext();
        fancyCoverFlow = (FancyCoverFlow) rootView.findViewById(R.id.fancyCoverFlow);
        fancyCoverFlow.setAdapter(new ViewGroupApparelAdapter());
        createViews(rootView);
        manager = WardrobeManager.getInstance(context, new OnTaskCompleteListener<Void>() {
            @Override
            public void success(Void result) {
                try {
                    progressBar.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                    colors.setAdapter(createSpinnerAdapter(manager.getAllColors()));
                    find.setVisibility(View.VISIBLE);
                    isApparelLoaded = true;
                } catch (Exception e) {
                    //TODO
                }
            }

            @Override
            public void error(String message) {
                //TODO
            }
        });
        isApparelLoaded = manager.getAll() != null;
        if (isApparelLoaded) {
            progressBar.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
            colors.setAdapter(createSpinnerAdapter(manager.getAllColors()));
            find.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            find.setVisibility(View.GONE);
        }
        return rootView;
    }
    private void createViews(View rootView) {
        styles = (Spinner) rootView.findViewById(R.id.style_spinner);
        colors = (Spinner) rootView.findViewById(R.id.color_spinner);
        cbStyle = (CheckBox) rootView.findViewById(R.id.cb_style);
        cbColor = (CheckBox) rootView.findViewById(R.id.cb_color);
        cbNew = (CheckBox) rootView.findViewById(R.id.cb_new);
        cbClean = (CheckBox) rootView.findViewById(R.id.cb_clean);
        cbForWeater = (CheckBox) rootView.findViewById(R.id.cb_for_weather);
        layoutParams = rootView.findViewById(R.id.search_params);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        content = rootView.findViewById(R.id.content);
        showParams = (Button) rootView.findViewById(R.id.show_params);
        showParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                find.setVisibility(View.VISIBLE);
                layoutParams.setVisibility(View.VISIBLE);
            }
        });
        styles.setAdapter(createSpinnerAdapter(Style.getStylesStr()));
        find = (Button) rootView.findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutParams.setVisibility(View.GONE);
                showParams.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                Parameters.Builder builder = new Parameters.Builder();
                builder.setClean(cbClean.isChecked())
                        .setForWeather(cbForWeater.isChecked())
                        .setNew(cbNew.isChecked());
                if (cbStyle.isChecked())
                    builder.add(Style.getStyle(styles.getSelectedItem().toString()));
                if (cbColor.isChecked())
                    builder.setColor(colors.getSelectedItem().toString());
                setTodaySuits(manager.getTodaySuits(builder.build()));
            }
        });
    }

    private void setTodaySuits(Map<Category, List<Apparel>> todaySuits) {
//        fancyCoverFlow.
    }

    private SpinnerAdapter createSpinnerAdapter(List<String> list) {
        return new ArrayAdapter<String>(context, R.layout.list_item_category, list);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}