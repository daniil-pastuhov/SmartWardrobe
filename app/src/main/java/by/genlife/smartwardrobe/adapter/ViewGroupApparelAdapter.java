package by.genlife.smartwardrobe.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import by.genlife.fancycoverflow.FancyCoverFlow;
import by.genlife.fancycoverflow.CoverFlowAdapter;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.data.WardrobeManager;
import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;
import by.genlife.smartwardrobe.view.CustomViewGroup;

/**
 * Created by NotePad.by on 07.05.2015.
 */
public class ViewGroupApparelAdapter extends CoverFlowAdapter {

    // =============================================================================
    // Private members
    // =============================================================================

    private List<Apparel> content = new ArrayList<Apparel>();
    private WeakHashMap<Apparel, Button> map = new WeakHashMap<>();

    // =============================================================================
    // Supertype overrides
    // =============================================================================

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public Apparel getItem(int i) {
        return content.get(i);
    }

    @Override
    public long getItemId(int i) {
        return content.get(i).hashCode();
    }

    @Override
    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
        CustomViewGroup customViewGroup = null;
        final Apparel apparel = getItem(i);

        if (reuseableView != null) {
            customViewGroup = (CustomViewGroup) reuseableView;
        } else {
            customViewGroup = new CustomViewGroup(viewGroup.getContext(), new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setEnabled(false);
                    clearOtherChoises();
                    map.put(apparel, (Button)v);
                    WardrobeManager.getInstance().putOn(apparel.getImagePath(), new OnTaskCompleteListener() {
                        @Override
                        public void success(Object result) {
                            apparel.putOn();
                        }

                        @Override
                        public void error(String message) {
                            v.setEnabled(true);
                        }
                    });
                }
            });
            customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(300, 300));
        }
        customViewGroup.getImageView().setImageURI(Uri.parse("file://" + apparel.getImagePath()));
                customViewGroup.getTextView().setText(apparel.getName());
        return customViewGroup;
    }

    public void clear() {
        content.clear();
    }

    public void add(Apparel apparel) {
        content.add(apparel);
    }

    public void addAll(List<Apparel> apparels) {
        content.addAll(apparels);
        notifyDataSetChanged();
    }

    private void clearOtherChoises(){
        for (Apparel apparel: map.keySet()) {
            Button b = map.get(apparel);
            if (b != null && !b.isEnabled()) {
                b.setEnabled(true);
                //TODO
//                apparel.cancel();
            }
        }
        map.clear();
    }
}
