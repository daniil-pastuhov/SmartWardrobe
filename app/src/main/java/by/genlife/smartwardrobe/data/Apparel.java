package by.genlife.smartwardrobe.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Style;

public class Apparel {

    private String imagePath;
    private String name;
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

    public Apparel(String imagePath, String name, String color, Category category, HashSet<Style> styles, List<String> tags, Integer minT, Integer maxT, String date_of_last_wearing, String date_of_buying) {
        this.imagePath = imagePath;
        this.name = name;
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

    public HashSet<Style> getStyles() {
        return styles;
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
        if (o instanceof Apparel) {
            Apparel ap = (Apparel) o;
            imagePath.equals(ap.getImagePath());
        }
        return false;
    }
}