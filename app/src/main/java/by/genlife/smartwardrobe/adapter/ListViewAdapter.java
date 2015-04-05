package by.genlife.smartwardrobe.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import by.genlife.smartwardrobe.R;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.data.Apparel;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public final class ListViewAdapter extends ArrayAdapter<Apparel> {

    private ArrayList<Apparel> mObjects;
    private ArrayList<Apparel> mOriginalValues;
    private LayoutInflater inflater;
    private Filter filter;
    private Object lock = new Object();
    private Context context;

    public ListViewAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        mObjects = new ArrayList<Apparel>();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            if (holder.needInvalidate) {
                convertView = inflater.inflate(R.layout.listview_item, parent, false);
                holder = createViewHolder(convertView);
                convertView.setTag(holder);
            }
        }

        Apparel apparel = getItem(position);
        if (apparel != null) {
            holder.name.setText(apparel.getName());
            holder.date.setText(apparel.getDate_of_last_wearing());
            if (new File(apparel.getImagePath()).exists()) {
                holder.cover.setImageBitmap(BitmapFactory.decodeFile(apparel.getImagePath()));
            } else {
                holder.cover.setImageResource(R.drawable.fallback_cover);
            }
            int progress = apparel.getWearProgress();
            holder.wearProgress.setProgress(progress);
            holder.styles.setText(Style.parseToString(apparel.getStyles()));
            holder.temperature.setText(apparel.getMinT() + context.getString(R.string.deg) + "—" + apparel.getMaxT() + context.getString(R.string.deg));
            if (progress > 90) holder.miniLabel.setImageResource(R.drawable.fu);
            else {
                if (apparel.getDate_of_buying().equals(apparel.getDate_of_last_wearing()))
                    holder.miniLabel.setImageResource(R.drawable.ne);
            }
        }
        convertView.setTag(holder);
        return convertView;
    }

    private ViewHolder createViewHolder(View convertView) {
        ViewHolder holder;
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.tvApparelName);
        holder.styles = (TextView) convertView.findViewById(R.id.tvApparelStyle);
        holder.temperature = (TextView) convertView.findViewById(R.id.tvApparelTemperature);
        holder.cover = (ImageView) convertView.findViewById(R.id.cover);
        holder.date = (TextView) convertView.findViewById(R.id.tvAppareldates);
        holder.wearProgress = (ProgressBar) convertView.findViewById(R.id.progressBar);
        holder.miniLabel = (ImageView) convertView.findViewById(R.id.mini_label);
        return holder;
    }

    private class ViewHolder {
        TextView name;
        TextView temperature;
        TextView date;
        TextView styles;
        ImageView cover;
        ImageView miniLabel;
        ProgressBar wearProgress;
        boolean needInvalidate = false;
    }

    private void insertData(ArrayList<Apparel> apparels) {
        synchronized (lock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(apparels);
            }
            mObjects.addAll(apparels);
        }
    }

    @Override
    public void add(Apparel object) {
        synchronized (lock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(object);
            }
            mObjects.add(object);
        }
    }

    @Override
    public void insert(Apparel object, int index) {
        synchronized (lock) {
            if (mOriginalValues != null) {
                mOriginalValues.add(index, object);
            }
            if (mObjects == null) {
                mObjects = new ArrayList<>();
            }
            mObjects.add(index, object);
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void addAll(Apparel... items) {
        super.addAll(items);
    }

    @SuppressLint("NewApi")
    @Override
    public void addAll(Collection<? extends Apparel> collection) {
        synchronized (lock) {
            if (mOriginalValues != null) {
                mOriginalValues.addAll(collection);
            }
            mObjects.addAll(collection);
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            if (mOriginalValues != null) {
                mOriginalValues.clear();
            }
            mObjects.clear();
        }
    }

    @Override
    public void remove(Apparel object) {
        synchronized (lock) {
            if (mOriginalValues != null) {
                mOriginalValues.remove(object);
            }
            mObjects.remove(object);
        }
    }

    @Override
    public int getCount() {
        synchronized (lock) {
            if (mObjects == null) {
                return 0;
            }
            return mObjects.size();
        }
    }

    @Override
    public Apparel getItem(int position) {
        synchronized (lock) {
            return mObjects.get(position);
        }
    }

    @Override
    public int getPosition(Apparel item) {
        synchronized (lock) {
            return mObjects.indexOf(item);
        }
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ResultFilter();
        }
        return filter;
    }

    private class ResultFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();
            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<>(mObjects);
            }
            if (prefix == null || prefix.length() == 0) {
                ArrayList<Apparel> list = new ArrayList<>(mOriginalValues);
                results.values = list;
                results.count = list.size();
            } else {
                ArrayList<Apparel> list = new ArrayList<>(mOriginalValues);
                ArrayList<Apparel> nlist = new ArrayList<>();
                int count = list.size();
                for (int i = 0; i < count; i++) {
                    Apparel data = list.get(i);
                    String value = data.toString();
                    if (value.contains(prefix)) {
                        nlist.add(data);
                    }
                    results.values = nlist;
                    results.count = nlist.size();
                }
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, final FilterResults results) {
            mObjects = (ArrayList<Apparel>) results.values;
        }
    }
}