package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.ListViewAdapter;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.constants.Tab;
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
    @Bind(R.id.btnBack) Button backButton;
    @Bind({R.id.spinner2, R.id.spinner3, R.id.spinner4, R.id.spinner5}) Spinner season, style, color, clean;
    @Bind(R.id.list_of_categories) ListView lst;
    ArrayAdapter<String> adapterMain;
    WardrobeManager manager;
    Context context;
    SharedPreferences prefs;
    String curCategory;
    ListViewAdapter adapter;
    Apparel selectedApparel;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.catalog, container, false);
        ButterKnife.bind(this, rootView);
        this.context = inflater.getContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        manager = WardrobeManager.getInstance(context, OnTaskCompleteListener.<Void>getEmptyListener());
        createSpinners(rootView, savedInstanceState);
        if (savedInstanceState != null) {
            selectedApparel = savedInstanceState.getParcelable(STATE_SELECTED_APPAREL);
        }
        registerForContextMenu(lst);
        adapterMain = new ArrayAdapter<>(context, R.layout.list_item_category, Category.getCategories());
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (curCategory == null) {
                    curCategory = adapterView.getAdapter().getItem(i).toString();
                    showApparelsByCategory(curCategory);
                    prefs.edit().putString(categoryStateStr, curCategory).apply();
                } else {
                    curCategory = null;
                    prefs.edit().remove(categoryStateStr).apply();
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
        adapter = new ListViewAdapter(context);
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
        if (visibility)
            registerForContextMenu(lst);
        else
            unregisterForContextMenu(lst);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (curCategory != null)
            prefs.edit().putString(categoryStateStr, curCategory).apply();
        else prefs.edit().remove(categoryStateStr).apply();
        int pos[] = new int[4];
        pos[0] = season.getSelectedItemPosition();
        pos[0] = style.getSelectedItemPosition();
        pos[0] = color.getSelectedItemPosition();
        pos[0] = clean.getSelectedItemPosition();
        outState.putIntArray(STATE_SPINNER_CATALOG, pos);
        outState.putParcelable(STATE_SELECTED_APPAREL, selectedApparel);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list_of_categories) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            selectedApparel = (Apparel) lv.getItemAtPosition(acmi.position);
            menu.setHeaderIcon(Drawable.createFromPath(selectedApparel.getImagePath()));
            menu.setHeaderTitle(selectedApparel.getName());
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.element_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (selectedApparel == null) return false;
        switch (item.getItemId()) {
            case R.id.action_delete:
                manager.deleteApparel(selectedApparel);
                adapter.remove(selectedApparel);
                break;
            case R.id.action_edit:
                Bundle arg = new Bundle();
                arg.putParcelable(EXTRA_APPAREL, selectedApparel);
                ((MainActivity)getActivity()).showFragment(Tab.getIndexOf(Tab.ADD_ELEMENT.name()), arg);
                break;
            case R.id.action_wash:
                manager.putToRepository(selectedApparel, "wash");
                selectedApparel.setWearProgress(100);
                break;
            case R.id.action_put_on:
                manager.putOn(selectedApparel.getImagePath(), OnTaskCompleteListener.getEmptyListener());
                break;
            case R.id.action_share:
                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                imageUris.add(Uri.parse("file:\\"+selectedApparel.getImagePath()));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent, "Share images to.."));
                break;
            case R.id.action_to_favorite:
                //TODO
                break;
        }
        selectedApparel = null;
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}