package by.genlife.smartwardrobe.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.data.Apparel;

public final class PageViewAdapter extends ArrayAdapter<Apparel> {

    private LayoutInflater inflater;
    private Context context;

    public PageViewAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mini_item, parent, false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            if (holder.needInvalidate) {
                convertView = inflater.inflate(R.layout.mini_item, parent, false);
                holder = createViewHolder(convertView);
                convertView.setTag(holder);
            }
        }

        Apparel apparel = getItem(position);
        if (apparel != null) {
            holder.name.setText(apparel.getName());
            if (apparel.getCover() != null) {
                holder.cover.setImageBitmap(apparel.getCover());
            } else {
                holder.cover.setImageResource(R.drawable.fallback_cover);
            }
            int progress = apparel.getWearProgress();
        }
        convertView.setTag(holder);
        return convertView;
    }

    private ViewHolder createViewHolder(View convertView) {
        ViewHolder holder;
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.tvApparelName);
        holder.cover = (ImageView) convertView.findViewById(R.id.cover);
        return holder;
    }

    private class ViewHolder {
        TextView name;
        ImageView cover;
        boolean needInvalidate = false;
    }
}