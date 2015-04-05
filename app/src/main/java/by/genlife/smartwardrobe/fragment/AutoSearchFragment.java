package by.genlife.smartwardrobe.fragment;

/**
 * Created by NotePad.by on 14.03.2015.
 */

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.PageViewAdapter;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.Parameters;
import by.genlife.smartwardrobe.data.WardrobeManager;
import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class AutoSearchFragment extends Fragment implements Constants{
    public static final String TAG = AutoSearchFragment.class.getSimpleName();

    BroadcastReceiver weatherReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            tvWeather.setVisibility(View.VISIBLE);
            tvWeather.setText(intent.getStringExtra(EXTRA_WEATHER));
            tvWeatherParam.setVisibility(View.VISIBLE);
            cbForWeater.setVisibility(View.VISIBLE);
        }
    };
    private static AutoSearchFragment instance;
    private WardrobeManager manager;

    AdapterViewFlipper suitToDay[];
    PageViewAdapter[] suitAdapters = new PageViewAdapter[6];
    Context context;
    TextView tvWeather;
    Spinner styles;
    Spinner colors;
    CheckBox cbStyle, cbColor, cbNew, cbClean, cbForWeater;
    View layoutParams;
    Button showParams, find;
    ProgressBar progressBar;
    View tvWeatherParam;
    View content;

    public static AutoSearchFragment getInstance() {
        if (instance == null) {
            instance = new AutoSearchFragment();
        }
        return instance;
    }

    public AutoSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = inflater.getContext();
        View rootView = inflater.inflate(R.layout.today_suit, container, false);
        createViews(rootView);
        if (savedInstanceState == null)
            createAdapterViewFlippers(rootView);
        manager = WardrobeManager.getInstance(context, new OnTaskCompleteListener<Void>() {
            @Override
            public void success(Void result) {
                try {
                    progressBar.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);
                    colors.setAdapter(createSpinnerAdapter(manager.getAllColors()));
                    find.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    //TODO
                }
            }

            @Override
            public void error(String message) {
                //TODO
            }
        });
        context.registerReceiver(weatherReceiver, new IntentFilter(ACTION_WEATHER));
        return rootView;
    }

    private void createAdapterViewFlippers(View rootView) {
        final View.OnTouchListener listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((AdapterViewFlipper) v).showNext();
                return false;
            }
        };
        int adapterViewFlipperIds[] = {R.id.vf_head, R.id.vf_under_body, R.id.vf_body_sweater, R.id.vf_body_out, R.id.vf_pants, R.id.vf_boots};
        suitToDay = new AdapterViewFlipper[adapterViewFlipperIds.length];
        for (int i = 0; i < adapterViewFlipperIds.length; ++i) {
            suitToDay[i] = (AdapterViewFlipper) rootView.findViewById(adapterViewFlipperIds[i]);
            suitToDay[i].setOnTouchListener(listener);
            suitAdapters[i] = new PageViewAdapter(context, R.layout.mini_item);
            suitToDay[i].setAdapter(suitAdapters[i]);
        }
    }

    private void createViews(View rootView) {
        tvWeather = (TextView) rootView.findViewById(R.id.tvWeather);
        styles = (Spinner) rootView.findViewById(R.id.style_spinner);
        colors = (Spinner) rootView.findViewById(R.id.color_spinner);
        cbStyle = (CheckBox) rootView.findViewById(R.id.cb_style);
        cbColor = (CheckBox) rootView.findViewById(R.id.cb_color);
        cbNew = (CheckBox) rootView.findViewById(R.id.cb_new);
        cbClean = (CheckBox) rootView.findViewById(R.id.cb_clean);
        cbForWeater = (CheckBox) rootView.findViewById(R.id.cb_for_weather);
        layoutParams = rootView.findViewById(R.id.search_params);
        tvWeatherParam = rootView.findViewById(R.id.tv_for_weather);
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
                if (cbForWeater.isChecked()) {
                    builder.setTemperature(getTemperature(tvWeather.getText().toString()));
                }
                if (cbStyle.isChecked())
                    builder.add(Style.getStyle(styles.getSelectedItem().toString()));
                if (cbColor.isChecked())
                    builder.setColor(colors.getSelectedItem().toString());
                setTodaySuits(manager.getTodaySuits(builder.build()));
            }
        });
    }

    private int getTemperature(String s) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(s);
        int avr = 15;
        if (m.find()) {
            avr = Integer.parseInt(m.group());
            if (m.find()) {
                avr += Integer.parseInt(m.group());
                avr /= 2;
            }
        }
        return avr;
    }

    private void setTodaySuits(Map<Category, List<Apparel>> todaySuits) {
        for (int i = 0; i < suitAdapters.length; i++) {
            suitAdapters[i].clear();
        }
        suitAdapters[0].addAll(todaySuits.get(Category.HEADDRESS));
        suitAdapters[1].addAll(todaySuits.get(Category.TSHIRTS));
        suitAdapters[2].addAll(todaySuits.get(Category.SWEATER));
        suitAdapters[3].addAll(todaySuits.get(Category.JACKET));
        suitAdapters[4].addAll(todaySuits.get(Category.TROUSERS));
        suitAdapters[5].addAll(todaySuits.get(Category.SHOES));
        for (int i = 0; i < suitToDay.length; i++) {
            suitToDay[i].setVisibility(suitAdapters[i].isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    private SpinnerAdapter createSpinnerAdapter(List<String> list) {
        return new ArrayAdapter<String>(context, R.layout.list_item_category, list);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }

    @Override
    public void onDestroy() {
        context.unregisterReceiver(weatherReceiver);
        super.onDestroy();
    }
}