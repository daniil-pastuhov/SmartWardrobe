package by.genlife.smartwardrobe.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Style;

public class Apparel {

    public static int idCounter = 0;
    private static DateFormat df = new SimpleDateFormat("Mmm dd, yyyy");


    public static int getNextId() {
        return idCounter++;
    }

    private Integer id;
    private String imagePath;
    private String name;
    private String size; //String: different format of closes
    private String color;
    private Category category;
    private HashSet<Style> styles;
    private Set<String> tags;
    private Integer minT; //min temperature
    private Integer maxT;
    private String date_of_last_wearing;
    private String date_of_buying;
    private Bitmap cover;
    private int wearProgress = 0;

    public Apparel(Integer id) {
        this.id = id;
    }

    public HashSet<Style> getStyles() {
        return styles;
    }

    public Apparel(String imagePath, String name, String size, String color, Category category, HashSet<Style> styles, List<String> tags, Integer minT, Integer maxT, String date_of_last_wearing, String date_of_buying) {
        this.imagePath = imagePath;
        id = getNextId();
        this.name = name;
        this.size = size;
        this.styles = styles;
        cover = loadCover(imagePath);
        this.color = color;
        this.category = category;
        this.tags = new HashSet<>();
        this.tags.addAll(tags);
        this.minT = minT;
        this.maxT = maxT;
        this.date_of_last_wearing = date_of_last_wearing;
        this.date_of_buying = date_of_buying;
    }

    private Bitmap loadCover(String imagePath) {
        return BitmapFactory.decodeFile(imagePath);
    }

    @Deprecated
    public Apparel(Integer ID, String imagePath, String name, String size, String color, Category category, HashSet<Style> styles, List<String> tags, Integer minT, Integer maxT, String date_of_last_wearing, String date_of_buying) {
        this.imagePath = imagePath;
        this.name = name;
        this.size = size;
        this.styles = styles;
        this.id = ID;
        this.color = color;
        this.category = category;
        cover = loadCover(imagePath);
        this.tags = new HashSet<>();
        this.tags.addAll(tags);
        this.minT = minT;
        this.maxT = maxT;
        this.date_of_last_wearing = date_of_last_wearing;
        this.date_of_buying = date_of_buying;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public int getWearProgress() {
        return wearProgress;
    }

    public void setWearProgress(int wearProgress) {
        this.wearProgress = wearProgress;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Integer getId() {
        return id;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Integer getMinT() {
        return minT;
    }

    public void setMinT(Integer minT) {
        this.minT = minT;
    }

    public Integer getMaxT() {
        return maxT;
    }

    public void setMaxT(Integer maxT) {
        this.maxT = maxT;
    }

    public String getDate_of_last_wearing() {
        return date_of_last_wearing;
    }

    public void setDate_of_last_wearing(String date_of_last_wearing) {
        this.date_of_last_wearing = date_of_last_wearing;
    }

    public String getDate_of_buying() {
        return date_of_buying;
    }

    public void setDate_of_buying(String date_of_buying) {
        this.date_of_buying = date_of_buying;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Apparel)
            return id.equals(((Apparel) o).id);
        return false;
    }
}