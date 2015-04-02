package by.genlife.smartwardrobe.constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public enum Style {
    WORK("Рабочий"),
    DAILY("Повседневный"),
    HOME("Домашний"),
    TRAINING("Спортивный"),
    THEATRE("В театр"),
    PICKNIK("На природу");

    private String description;

    Style(String description) {
        this.description = description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static List<String> getStylesStr() {
        List<String> res = new ArrayList<String>();
        for (Style c : values()) {
            res.add(c.getDescription());
        }
        return res;
    }

    public static HashSet<Style> parseString(String s) {
        HashSet<Style> res = new HashSet<>(values().length);
        StringTokenizer st = new StringTokenizer(s, Constants.styleSeparator);
        while (st.hasMoreTokens()) {
            res.add(Style.valueOf(st.nextToken()));
        }
        return res;
    }

    public static String parseToString(HashSet<Style> styles) {
        StringBuilder sb = new StringBuilder();
        for (Style style : styles) {
            sb.append(style.name());
            sb.append(Constants.styleSeparator);
        }
        return sb.toString();
    }

    public static Style getStyle(String description) {
        for (Style style : Style.values())
            if (style.description.equals(description))
                return style;
        return null;
    }
}