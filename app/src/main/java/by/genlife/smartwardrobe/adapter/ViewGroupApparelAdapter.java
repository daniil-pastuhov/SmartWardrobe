package by.genlife.smartwardrobe.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import by.genlife.fancycoverflow.FancyCoverFlow;
import by.genlife.fancycoverflow.FancyCoverFlowAdapter;
import by.genlife.smartwardrobe.data.Apparel;
import by.genlife.smartwardrobe.view.CustomViewGroup;

/**
 * Created by NotePad.by on 07.05.2015.
 */
public class ViewGroupApparelAdapter extends FancyCoverFlowAdapter {

    // =============================================================================
    // Private members
    // =============================================================================

    private List<Apparel> content = new ArrayList<Apparel>();

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

        if (reuseableView != null) {
            customViewGroup = (CustomViewGroup) reuseableView;
        } else {
            customViewGroup = new CustomViewGroup(viewGroup.getContext());
            customViewGroup.setLayoutParams(new FancyCoverFlow.LayoutParams(300, 600));
        }
        System.err.println(this.getItem(i).getImagePath());
        customViewGroup.getImageView().setImageURI(Uri.parse("file://" + this.getItem(i).getImagePath()));
                customViewGroup.getTextView().setText(this.getItem(i).getName());
        return customViewGroup;
    }

    public void clear() {

    }

    public void addAll(List<Apparel> apparels) {

    }
}
