package by.genlife.smartwardrobe.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.activity.MainActivity;
import by.genlife.smartwardrobe.adapter.ListViewAdapter;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.WardrobeManager;

/**
 * Created by NotePad.by on 12.04.2015.
 */
public class ToWashFragment extends Fragment {
    public static final String TAG = ToWashFragment.class.getSimpleName();

    Context context;
    ListViewAdapter inWash;
    ListViewAdapter toWash;

    public ToWashFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.to_wash_layout, container, false);
        this.context = inflater.getContext();
        toWash = new ListViewAdapter(context, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String path = ((TextView)v.findViewById(R.id.path)).getText().toString();
                Apparel apparel = WardrobeManager.findByPath(path);
                WardrobeManager.getInstance().putToRepository(apparel, "wash");
                apparel.setWearProgress(100);
                inWash.add(apparel);
                toWash.remove(apparel);
                inWash.notifyDataSetChanged();
                toWash.notifyDataSetChanged();
                return false;
            }
        });
        toWash.addAll(WardrobeManager.getInstance().getDirty());
        ListView toWashList = ((ListView) rootView.findViewById(R.id.lvToWash));
        toWashList.setAdapter(toWash);
        inWash = new ListViewAdapter(context, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String path = ((TextView)v.findViewById(R.id.path)).getText().toString();
                Apparel apparel = WardrobeManager.findByPath(path);
                apparel.setWearProgress(0);
                WardrobeManager.getInstance().backToWardrobe(apparel);
                inWash.remove(apparel);
                inWash.notifyDataSetChanged();
                toWash.notifyDataSetChanged();
                return false;
            }
        });
        inWash.addAll(WardrobeManager.getInstance().getFromRepository("wash"));
        ListView fromWashList = ((ListView) rootView.findViewById(R.id.lvFromWashWash));
        fromWashList.setAdapter(inWash);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached();
    }
}