package by.genlife.smartwardrobe.data;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Constants;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public class WardrobeManager implements ApparelRepository, Constants {
    protected Context context;
    private static WardrobeManager instance;
    private Object lock = new Object();
    private List<Apparel> clothes = new ArrayList<Apparel>();
    private OnTaskCompleteListener listener;

    public static WardrobeManager getInstance(Context context, OnTaskCompleteListener<Void> listener) {
        if (instance == null) {
            instance = new WardrobeManager(context);
        } else {
            listener.success(null);
        }
        instance.listener = listener;
        instance.context = context;
        return instance;
    }

    public static WardrobeManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("Instance is not created yet. Call init(Context).");
        else return instance;
    }

    private WardrobeManager(Context context) {
        this.context = context;
        DBHelper.getInstance(context).getAll(new OnTaskCompleteListener<ArrayList<Apparel>>() {


            @Override
            public void success(final ArrayList<Apparel> result) {
                synchronized (lock) {
                    clothes.addAll(result);
                }
                listener.success(null);
            }

            @Override
            public void error(String message) {
                System.err.println(message);
                listener.error(null);
            }
        });
    }

    @Override
    public List<Apparel> getAll() {
        return clothes;
    }

    @Override
    public List<Apparel> getByParams(Parameters parameters) {
        boolean clean = parameters.isClean();
        boolean isNew = parameters.isNewP();
        boolean weather = parameters.isWeather();
        int temperatute = parameters.getTemperatute();
        Style style = parameters.getStyle();
        Category category = parameters.getCategory();
        String color = parameters.getColor();
        List<Apparel> res = new ArrayList<>(clothes);
        for (Apparel apparel : clothes) {
            if (clean && apparel.getWearProgress() > CLEAN_LEVEL || isNew && !apparel.isNew()
                    || weather && (apparel.getMinT() > temperatute || apparel.getMaxT() < temperatute)
                    || style != null && !apparel.getStyles().contains(style)
                    || category != null && !apparel.getCategory().equals(category)
                    || color != null && !apparel.getColor().equals(color)) {
                res.remove(apparel);
            }
        }
        return res;
    }

    @Override
    public Map<Category, List<Apparel>> getTodaySuits(Parameters parameters) {
        Map<Category, List<Apparel>> res = new HashMap<>(Category.getCategories().size());
        for (Category category : Category.values()) {
            res.put(category, new ArrayList<Apparel>());
        }
        for (Apparel apparel : getByParams(parameters)) {
            res.get(apparel.getCategory()).add(apparel);
        }
        return res;
    }

    @Override
    public void addApparel(Apparel app) {
        clothes.add(app);
        DBHelper.getInstance(context).insert(app);
    }

    @Override
    public void deleteApparel(Apparel app) {
        clothes.remove(app);
        DBHelper.getInstance(context).delete(app);
    }

    @Override
    public List<Apparel> searchByTags(List<String> tags) {
        List<Apparel> res = new ArrayList<>();
        for (Apparel apparel : clothes) {
            for (String tag : tags) {
                if (apparel.getTags().contains(tag)) {
                    res.add(apparel);
                    break;
                }
            }
        }
        return res;
    }

    public List<String> getAllColors() {
        HashSet<String> resSet = new HashSet<>();
        for (Apparel apparel : clothes) {
            resSet.add(apparel.getColor());
        }
        return new ArrayList<>(resSet);
    }

    public void putOn(String path, OnTaskCompleteListener listener) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String today = simpleDateFormat.format(new Date());
        for (Apparel apparel : clothes) {
            if (path.equals(apparel.getImagePath())) {
                apparel.setDate_of_last_wearing(today);
                apparel.putOn();
                DBHelper.getInstance().update(apparel, listener);
                break;
            }
        }
    }

    public List<Apparel> getDirty() {
        List<Apparel> res = new ArrayList<>();
        for (Apparel apparel : clothes) {
            if (apparel.getWearProgress() > 0 && apparel.getRepository().equals(homeRepository)) {
                res.add(apparel);
            }
        }
        return res;
    }

    public void putToRepository(Apparel apparel, String repository) {
        apparel.setRepository(repository);
        DBHelper.getInstance().update(apparel, OnTaskCompleteListener.getEmptyListener());
    }

    public void backToWardrobe(Apparel apparel) {
        putToRepository(apparel, Constants.homeRepository);
    }

    public static Apparel findByPath(String path) {
        for (Apparel apparel : instance.clothes) {
            if (apparel.getImagePath().equals(path))
                return apparel;
        }
        return null;
    }

    public List<Apparel> getFromRepository(String repositoryName) {
        ArrayList<Apparel> res = new ArrayList<>();
        for (Apparel apparel : clothes) {
            if (apparel.getRepository().equals(repositoryName))
                res.add(apparel);
        }
        return res;
    }
}