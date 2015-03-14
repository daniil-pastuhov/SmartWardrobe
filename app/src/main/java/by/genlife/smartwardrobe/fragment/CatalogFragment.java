package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.ListViewAdapter;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.WardrobeManager;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public class CatalogFragment extends Fragment {

    final MainMenuState mainMenuState = new MainMenuState();
    final CategoryMenuState categoryMenuState = new CategoryMenuState();
    final ArrayList<Apparel> apparels = new ArrayList<>(5);
    final String mainMenuStateStr = "MainMenuState";
    final String categoryStateStr = "categoryState";
    Button backButton;
    ListView lst;
    State state;
    ArrayAdapter<String> adapterMain;
    private static CatalogFragment instance;
    Context context;

    public static CatalogFragment getInstance() {
        if (instance == null) {
            instance = new CatalogFragment();
        }
        return instance;
    }

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.catalog, container, false);
        this.context = inflater.getContext();
        WardrobeManager.init(context);
        //TODO delete this section
        Spinner sp1, sp2, sp3, sp4;
        sp1 = (Spinner) rootView.findViewById(R.id.spinner2);
        sp2 = (Spinner) rootView.findViewById(R.id.spinner3);
        sp3 = (Spinner) rootView.findViewById(R.id.spinner4);
        sp4 = (Spinner) rootView.findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> ad1 = ArrayAdapter.createFromResource(context, R.array.n1, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> ad2 = ArrayAdapter.createFromResource(context, R.array.n2, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> ad3 = ArrayAdapter.createFromResource(context, R.array.n3, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> ad4 = ArrayAdapter.createFromResource(context, R.array.n4, android.R.layout.simple_spinner_item);
        sp1.setAdapter(ad1);
        sp2.setAdapter(ad2);
        sp3.setAdapter(ad3);
        sp4.setAdapter(ad4);
        //
        if (savedInstanceState != null && !savedInstanceState.getBoolean(mainMenuStateStr, true)) {
            state = categoryMenuState;
            state.setCategory(savedInstanceState.getString(categoryStateStr));
        } else {
            state = mainMenuState;
        }
        lst = (ListView) rootView.findViewById(R.id.list_of_categories);
        adapterMain = new ArrayAdapter<>(context, R.layout.list_item_category, Category.getCategories());
        backButton = (Button) rootView.findViewById(R.id.btnBack);
        backButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
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
                    if (state != mainMenuState) onBackClick(null);
                }
                return false;
            }
        });
        System.err.println("created");
        state.init();
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state.onItemClicked(adapterView, i, l);
            }
        });

        return rootView;
    }

    private void showBackButton(boolean visibility) {
        if (visibility) backButton.setVisibility(View.VISIBLE);
        else backButton.setVisibility(View.GONE);
    }

    public void onBackClick(View v) {
        state.prevState();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(mainMenuStateStr, state instanceof MainMenuState);
        outState.putString(categoryStateStr, state.getCategory());
    }

    abstract private class State {
        protected String category;

        abstract public void onItemClicked(AdapterView adapterView, int i, long l);

        abstract public void nextState();

        abstract public void prevState();

        abstract public void init();

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    private class MainMenuState extends State {
        @Override
        public void onItemClicked(AdapterView adapterView, int i, long l) {
            nextState();
            state.setCategory(adapterView.getAdapter().getItem(i).toString());
            state.init();
        }

        @Override
        public void nextState() {
            state = categoryMenuState;
        }

        @Override
        public void prevState() {
            //do nothing
        }

        @Override
        public void init() {
            showBackButton(false);
            lst.setAdapter(adapterMain);
        }
    }

    private class CategoryMenuState extends State {
        @Override
        public void onItemClicked(AdapterView adapterView, int i, long l) {
            getFragmentManager().beginTransaction().replace(R.id.container, DetailFragment.getInstance()).commit();
            category = adapterView.getAdapter().getItem(i).toString();
            nextState();
        }

        @Override
        public void nextState() {
            //go nowhere
        }

        @Override
        public void prevState() {
            state = mainMenuState;
            state.init();
        }

        @Override
        public void init() {
            showBackButton(true);
            List<Apparel> tempList = WardrobeManager.getInstance().getByCategory(category);
            ListViewAdapter adapter = new ListViewAdapter(context, R.layout.listview_item);
            adapter.addAll(tempList);
            lst.setAdapter(adapter);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}