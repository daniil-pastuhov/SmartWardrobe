package by.genlife.smartwardrobe.fragment;

/**
 * Created by NotePad.by on 14.03.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.Utils;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.PageViewAdapter;
import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.WardrobeManager;

/**
 * A placeholder fragment containing a simple view.
 */
public class AutoSearchFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static AutoSearchFragment instance;
    ViewFlipper suitToDay[];
    AdapterViewFlipper adapterViewFlipper;
    Context context;
    ProgressBar progressBar;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AutoSearchFragment getInstance() {
        if (instance == null) {
            instance = new AutoSearchFragment();
        }
        return instance;
    }

    public AutoSearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.today_suit, container, false);
        final EditText editText = (EditText) rootView.findViewById(R.id.findByTegs);
        //TODO delete this code below
        context = inflater.getContext();
        int adapterViewFlipperIds[] = {R.id.vf_head, R.id.vf_under_body, R.id.vf_body_out, R.id.vf_pants, R.id.vf_boots};
        suitToDay = new ViewFlipper[adapterViewFlipperIds.length];
        adapterViewFlipper = (AdapterViewFlipper) rootView.findViewById(R.id.aaa);

        for (int i = 0; i < adapterViewFlipperIds.length; ++i) {
            suitToDay[i] = (ViewFlipper) rootView.findViewById(adapterViewFlipperIds[i]);
        }
        final List<String> arr1 = WardrobeManager.getInstance(context).getTargetCategories();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        final Spinner sp1 = (Spinner) rootView.findViewById(R.id.spinner);
        final SpinnerAdapter spAdapter1 = new ArrayAdapter<String>(context, R.layout.list_item_category, arr1);
        sp1.setAdapter(spAdapter1);
        String filePath = Utils.getHomeDirectory();
        final ArrayList<Apparel> apparelList = new ArrayList<Apparel>(Arrays.asList(new Apparel(filePath + "head.jpg", "Самая модная шапка", "45", "Зелёная", Category.HEADDRESS, new HashSet<>(new ArrayList<Style>() {{
            add(Style.DAILY);
        }}), new LinkedList<String>() {{
            add("Красивый");
        }}, 18, 25, "25-06-1994", "25-06-1994"), new Apparel(filePath + "ex.jpg", "рубашка", "М", "Синяя", Category.SHIRT, new HashSet<>(new ArrayList<Style>() {{
            add(Style.DAILY);
        }}), new LinkedList<String>() {{
            add("Подарок");
        }}, 10, 20, "25-07-1994", "25-07-1994"), new Apparel(filePath + "trousers.jpg", "Любимые брюки", "48-52", "Темно-синие", Category.TROUSERS, new HashSet<>(new ArrayList<Style>() {{
            add(Style.HOME);
        }}), new LinkedList<String>() {{
            add("Школьные ещё");
        }}, -1, 25, "25-06-1994", "25-06-1994")));
        View v = inflater.inflate(R.layout.listview_item, null);
        PageViewAdapter adapter = new PageViewAdapter(context, R.layout.mini_item);
        adapter.addAll(apparelList);
        adapterViewFlipper.setAdapter(adapter);
        adapterViewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                adapterViewFlipper.showNext();
                return false;
            }
        });

        Button btnChange = (Button) rootView.findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
//                String a = arr1.get(sp1.getSelectedItemPosition());
//                String b = editText.getText().toString();
                //TODO apprel = getTodaySuit(s, k, g, d, t, k);
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}