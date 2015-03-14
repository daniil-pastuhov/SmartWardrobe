package by.genlife.smartwardrobe.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.listener.TaskSuccessListener;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public class WardrobeManager implements TaskSuccessListener, ApparelRepository {
    public static final String REPO_NAME = "";
    protected Context context;
    private static WardrobeManager instance;
    private Object lock = new Object();
    private Map<Category, List<Apparel>> catalog = new HashMap<Category, List<Apparel>>();
    private List<Apparel> wash = new ArrayList<Apparel>();
    private List<Apparel> clothes = new ArrayList<Apparel>();

    private List<String> targets = new ArrayList<String>();

    public static WardrobeManager getInstance(Context context) {
        if (instance == null) {
            instance = new WardrobeManager(context);
        }
        instance.context = context;
        return instance;
    }

    public static WardrobeManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("Instance is not created yet. Call init(Context).");
        else return instance;
    }

    public static WardrobeManager init(Context context) {
        instance = new WardrobeManager(context);
        return instance;
    }

    private WardrobeManager(Context context) {
        this.context = context;
        DBHelper.getInstance(context).getAll(this);
    }

    @Override
    public List<Apparel> getAll() {
        return clothes;
    }

    @Override
    public List<Apparel> getByCategory(String category) {
        Category c = Category.getByType(category);
        if (c == null || !catalog.containsKey(c)) return new ArrayList<Apparel>();
        return catalog.get(c);
    }

    @Override
    public List<Apparel> getByTarget(String target) {
        List<Apparel> list = new ArrayList<Apparel>();
        for (Apparel ap : getNotInWash()) {
            if (ap.getTags().contains(target)) {
                list.add(ap);
            }
        }
        return list;
    }

    @Override
    public List<Apparel> getDirty() {
        List<Apparel> list = new ArrayList<Apparel>();
//        for (Apparel app : clothes) {
//            if (app.getWear() > WEAR_LEVEL) {
//                list.add(app);
//            }
//        }
//        Collections.sort(list, ne Comparator<Apparel>() {
//            @Override
//            public int compare(Apparel apparel, Apparel apparel2) {
//                return apparel2.getWear() - apparel.getWear();
//            }
//        });
        return list;
    }

    @Override
    public List<Apparel> getNotInWash() {
        List ret = (new ArrayList<Apparel>(clothes));
        ret.removeAll(wash);
        return ret;
    }

    @Override
    public List<Apparel> getInWash() {
        return wash;
    }

    @Override
    public List<Apparel> getTopByTarget(String target) {
        List<Apparel> list = new ArrayList<Apparel>();
        for (Apparel ap : getByTarget(target)) {
            if (Category.SWEATER.equals(ap.getCategory()) ||
                    Category.SHIRT.equals(ap.getCategory()) ||
                    Category.TSHIRTS.equals(ap.getCategory())) {
                list.add(ap);
            }
        }
        return list;
    }

    @Override
    public List<Apparel> getBottomByTarget(String target) {
        List<Apparel> list = new ArrayList<Apparel>();
        for (Apparel ap : getByTarget(target)) {
            if (Category.SHOES.equals(ap.getCategory())) {
                list.add(ap);
            }
        }
        return list;
    }

    @Override
    public List<Apparel> getMiddleByTarget(String target) {
        List<Apparel> list = new ArrayList<Apparel>();
        for (Apparel ap : getByTarget(target)) {
            if (Category.TROUSERS.equals(ap.getCategory())) {
                list.add(ap);
            }
        }
        return list;
    }

    @Override
    public List<Apparel> getAccessoriseByTarget(String target) {
        List<Apparel> list = new ArrayList<Apparel>();
        for (Apparel ap : getByTarget(target)) {
            if (Category.ACCESSORIES.equals(ap.getCategory())) {
                list.add(ap);
            }
        }
        return list;
    }

    @Override
    public Apparel getById(Integer id) {
        int index = Collections.binarySearch(clothes, new Apparel(id), new Comparator<Apparel>() {
            @Override
            public int compare(Apparel apparel, Apparel apparel2) {
                return (int) (apparel2.getId() - apparel.getId());
            }
        });
        return clothes.get(index);
    }

    @Override
    public void addApparel(Apparel app) {
//        clothes.add(app);
//        if (app.getInWash()) {
//            wash.add(app);
//        }
//        if (catalog.containsKey(app.getCategory()))
//            catalog.get(app.getCategory()).add(app);
//        else {
//            List<Apparel> list = ne ArrayList<Apparel>();
//            list.add(app);
//            catalog.put(app.getCategory(), list);
//        }

    }

    @Override
    public void deleteApparel(Apparel app) {
//        if (app.getInWash()) {
//            wash.remove(app);
//        }
//        catalog.get(app.getCategory()).remove(app);
//        clothes.remove(app);
    }

    @Override
    public void setWash(Apparel app, boolean flag) {
//        app.setInWash(flag);
//        if (flag) {
//            wash.add(app);
//        } else {
//            wash.remove(app);
//        }
    }

    @Override
    public List<String> getTargetCategories() {
        return targets;
    }

    @Override
    public void addTarget(String s) {
        targets.add(s);
    }

    @Override
    public void removeTarget(String s) {
        targets.remove(s);
    }

    @Override
    public void success(final ArrayList<Apparel> result) {
        synchronized (lock) {
            clothes.addAll(result);
        }
        ArrayList<Apparel>[] arr = new ArrayList[Category.values().length];
        for (int i = 0; i < arr.length; ++i) {
            arr[i] = new ArrayList<Apparel>();
            catalog.put(Category.values()[i], arr[i]);
        }
        for (Apparel apparel : clothes) {
            arr[apparel.getCategory().ordinal()].add(apparel);
        }
    }
}