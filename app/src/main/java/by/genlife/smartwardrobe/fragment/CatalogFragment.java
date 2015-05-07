package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.ListViewAdapter;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.Parameters;
import by.genlife.smartwardrobe.data.WardrobeManager;
import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public class CatalogFragment extends Fragment implements Constants {
    public static final String TAG = CatalogFragment.class.getSimpleName();

    final String categoryStateStr = "categoryState";
    Button backButton;
    Spinner season, style, color, clean;
    ListView lst;
    ArrayAdapter<String> adapterMain;
    WardrobeManager manager;
    Context context;
    SharedPreferences prefs;
    String curCategory;

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.catalog, container, false);
        this.context = inflater.getContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        manager = WardrobeManager.getInstance(context, OnTaskCompleteListener.<Void>getEmptyListener());
        createSpinners(rootView, savedInstanceState);
        backButton = (Button) rootView.findViewById(R.id.btnBack);
        lst = (ListView) rootView.findViewById(R.id.list_of_categories);
        adapterMain = new ArrayAdapter<>(context, R.layout.list_item_category, Category.getCategories());
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (curCategory == null) {
                    curCategory = adapterView.getAdapter().getItem(i).toString();
                    showApparelsByCategory(curCategory);
                    prefs.edit().putString(categoryStateStr, curCategory).commit();
                } else {
                    curCategory = null;
                    prefs.edit().remove(categoryStateStr).commit();
                }
            }
        });
        backButton.setFocusable(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick(v);
            }
        });
        backButton.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    onBackClick(null);
                }
                return false;
            }
        });
        if (prefs.contains(categoryStateStr)) {
            curCategory = prefs.getString(categoryStateStr, Category.OTHER.getType());
            showApparelsByCategory(curCategory);
        } else {
            lst.setAdapter(adapterMain);
        }
        return rootView;
    }

    private void showApparelsByCategory(String curCategory) {
        Parameters.Builder builder = new Parameters.Builder();
        builder.add(Category.getByType(curCategory));
        showBackButton(true);
        addFilter(builder);
        List<Apparel> tempList = manager.getByParams(builder.build());
        ListViewAdapter adapter = new ListViewAdapter(context, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.showContextMenu();
                //TODO
                String path = ((TextView)v.findViewById(R.id.path)).getText().toString();
                WardrobeManager.getInstance().putOn(path, new OnTaskCompleteListener() {
                    @Override
                    public void success(Object result) {
                        lst.invalidateViews();
                    }

                    @Override
                    public void error(String message) {

                    }
                });
                return false;
            }
        });
        adapter.clear();
        adapter.addAll(tempList);
        lst.setAdapter(adapter);
    }

    private void addFilter(Parameters.Builder builder) {
        boolean weather = false;
        int temperature = 15;
        switch (season.getSelectedItemPosition()) {
            case 1:
                temperature = COLD;
                weather = true;
                break;
            case 2:
                temperature = WARM;
                weather = true;
                break;
            case 3:
                temperature = HOT;
                weather = true;
                break;
            default:
                break;
        }
        if (weather) {
            builder.setTemperature(temperature);
        }
        if (style.getSelectedItemPosition() != 0)
            builder.add(Style.getStyle(style.getSelectedItem().toString()));
        if (color.getSelectedItemPosition() != 0)
            builder.setColor(color.getSelectedItem().toString());
        if (clean.getSelectedItemPosition() != 0) {
            switch (clean.getSelectedItemPosition()) {
                case 1: //new
                    builder.setNew(true);
                    break;
                case 2: //clean
                    builder.setClean(true);
                    break;
            }
        }

    }

    private void createSpinners(View rootView, Bundle savedInstanceState) {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showApparelsByCategory(curCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        season = (Spinner) rootView.findViewById(R.id.spinner2);
        style = (Spinner) rootView.findViewById(R.id.spinner3);
        color = (Spinner) rootView.findViewById(R.id.spinner4);
        clean = (Spinner) rootView.findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> ad1 = ArrayAdapter.createFromResource(context, R.array.season, android.R.layout.simple_spinner_item);
        List<String> styles = Style.getStylesStr();
        List<String> colors = manager.getAllColors();
        styles.add(0, "~");
        colors.add(0, "~");
        ArrayAdapter<String> ad2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, styles);
        ArrayAdapter<String> ad3 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, colors);
        ArrayAdapter<CharSequence> ad4 = ArrayAdapter.createFromResource(context, R.array.state, android.R.layout.simple_spinner_item);
        season.setAdapter(ad1);
        style.setAdapter(ad2);
        color.setAdapter(ad3);
        clean.setAdapter(ad4);
        if (savedInstanceState != null) {
            season.setOnItemSelectedListener(null);
            style.setOnItemSelectedListener(null);
            color.setOnItemSelectedListener(null);
            clean.setOnItemSelectedListener(null);
            int[] pos = savedInstanceState.getIntArray(STATE_SPINNER_CATALOG);
            season.setSelection(pos[0]);
            style.setSelection(pos[0]);
            color.setSelection(pos[0]);
            clean.setSelection(pos[0]);
        }
        season.setOnItemSelectedListener(listener);
        style.setOnItemSelectedListener(listener);
        color.setOnItemSelectedListener(listener);
        clean.setOnItemSelectedListener(listener);
    }

    public void onBackClick(View v) {
        curCategory = null;
        showBackButton(false);
        prefs.edit().remove(categoryStateStr).commit();
        lst.setAdapter(adapterMain);
    }

    private void showBackButton(boolean visibility) {
        if (visibility) backButton.setVisibility(View.VISIBLE);
        else backButton.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (curCategory != null)
            prefs.edit().putString(categoryStateStr, curCategory).commit();
        else prefs.edit().remove(categoryStateStr).commit();
        int pos[] = new int[4];
        pos[0] = season.getSelectedItemPosition();
        pos[0] = style.getSelectedItemPosition();
        pos[0] = color.getSelectedItemPosition();
        pos[0] = clean.getSelectedItemPosition();
        outState.putIntArray(STATE_SPINNER_CATALOG, pos);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}