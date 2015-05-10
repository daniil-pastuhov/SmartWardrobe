package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.ListViewAdapter;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.WardrobeManager;

/**
 * Created by NotePad.by on 11.04.2015.
 */
public class FindByTagFragment extends Fragment {
    public static final String TAG = FindByTagFragment.class.getSimpleName();

    private Context context;
    private EditText searchStr;
    private ListViewAdapter results;
    private View noResult;

    public FindByTagFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.find_by_teg_layout, container, false);
        this.context = inflater.getContext();
        results = new ListViewAdapter(context, null);
        ((ListView) rootView.findViewById(R.id.lvFounded)).setAdapter(results);
        searchStr = (EditText) rootView.findViewById(R.id.tags);
        noResult = rootView.findViewById(R.id.no_result);
        ((Button) rootView.findViewById(R.id.findByTegs)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringTokenizer st = new StringTokenizer(searchStr.getText().toString());
                ArrayList<String> tags = new ArrayList<String>();
                while (st.hasMoreTokens()) {
                    tags.add(st.nextToken().toLowerCase());
                }
                results.clear();
                List<Apparel> res = WardrobeManager.getInstance().searchByTags(tags);
                if (!res.isEmpty()) {
                    results.addAll(res);
                    noResult.setVisibility(View.GONE);
                } else {
                    noResult.setVisibility(View.VISIBLE);
                }
                hideKeyboard();
            }
        });
        return rootView;
    }

    private void hideKeyboard() {
        Activity activity = getActivity();
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                view.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}