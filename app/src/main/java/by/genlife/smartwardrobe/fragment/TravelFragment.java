package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.genlife.fancycoverflow.FancyCoverFlow;
import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.ViewGroupApparelAdapter;
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
    private static final HashMap<Integer, String> bagSize = new HashMap<Integer, String>();
    private WardrobeManager manager;

    Context context;
    CheckBox cbNew, cbClean, cbForWeater;
    View layoutParams;
    Button showParams, find;
    ProgressBar progressBar;
    View content;
    FancyCoverFlow fancyCoverFlow;
    ViewGroupApparelAdapter adapter;
    TextView sizeName;
    EditText dayNumber;
    Map<String, Boolean> styles = new HashMap<>(Style.size());
    boolean isApparelLoaded = false;
    int size = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.travel_fragment, container, false);
        this.context = inflater.getContext();
        fancyCoverFlow = (FancyCoverFlow) rootView.findViewById(R.id.fancyCoverFlow);
        adapter = new ViewGroupApparelAdapter();
        fancyCoverFlow.setAdapter(adapter);
        initMap(context);
        createViews(rootView);
        manager = WardrobeManager.getInstance(context, new OnTaskCompleteListener<Void>() {
            @Override
            public void success(Void result) {
                try {
                    progressBar.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
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
            find.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            content.setVisibility(View.GONE);
            find.setVisibility(View.GONE);
        }
        return rootView;
    }

    private void initMap(Context context) {
        String [] types = context.getResources().getStringArray(R.array.bag_type);
        for (int i = 0; i < types.length; i++) {
            bagSize.put(i, types[i]);
        }
    }

    private void createViews(View rootView) {
        LinearLayout styleLayout = (LinearLayout) rootView.findViewById(R.id.checkboxes);
        for (Style style : Style.values()) {
            CheckBox cb = new CheckBox(context);
            cb.setText(style.getDescription());
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    styles.put(buttonView.getText().toString(), isChecked);
                }
            });
            styleLayout.addView(cb);
        }
        cbNew = (CheckBox) rootView.findViewById(R.id.cb_new);
        cbClean = (CheckBox) rootView.findViewById(R.id.cb_clean);
        cbForWeater = (CheckBox) rootView.findViewById(R.id.cb_for_weather);
        layoutParams = rootView.findViewById(R.id.search_params);
        dayNumber = (EditText) rootView.findViewById(R.id.day_number);
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
        find = (Button) rootView.findViewById(R.id.find);
        sizeName = (TextView) rootView.findViewById(R.id.tv_bag_size);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.bag_size);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sizeName.setText(bagSize.get(progress));
                size = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutParams.setVisibility(View.GONE);
                showParams.setVisibility(View.VISIBLE);
                fancyCoverFlow.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                Parameters.Builder builder = new Parameters.Builder();
                builder.setClean(cbClean.isChecked())
                        .setNew(cbNew.isChecked());
                List<Style> styles_t = new ArrayList<Style>();
                for (String style : styles.keySet()) {
                    if (styles.get(style))
                        styles_t.add(Style.getStyle(style));
                }
                setTodaySuits(manager.toTravel(styles_t, builder.build(), 4*(size + 1) * Integer.parseInt(dayNumber.getText().toString())));
                ((MainActivity)getActivity()).hideKeyboard();
            }
        });
    }

    private void setTodaySuits(List<Apparel> suits) {
        adapter.clear();
        adapter.addAll(suits);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}