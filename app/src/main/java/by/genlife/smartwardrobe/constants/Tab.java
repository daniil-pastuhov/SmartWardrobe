package by.genlife.smartwardrobe.constants;

import java.util.ArrayList;
import java.util.List;

public enum Tab {
    AUTO_SEARCH("Подобрать одежду"),
    CATALOG("Каталог"),
    SEARCHING("Поиск"),
    WASHING("Корзина для стирки"),
    TRAVEL("В путешествие"),
    ADD("Добавить новое");

    private String description;

    private Tab(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getTypes() {
        List<String> res = new ArrayList<String>();
        for (Tab c : values()) {
            res.add(c.getDescription());
        }
        return res;
    }

    public static Tab getType(int position) {
        return values()[position];
    }

    public static Tab getType(String name) {
        Tab res = null;
        try {
            res = valueOf(name);
        } catch (Exception e) {
        }
        return res;
    }

    public static int getIndexOf(String name) {
        if (name == null || name.isEmpty()) return -1;
        Tab[] list = values();
        Tab type = valueOf(name);
        for (int i = 0; i < list.length; ++i) {
            if (list[i].equals(type)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        return description;
    }

}