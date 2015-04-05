package by.genlife.smartwardrobe.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Style;

public class Apparel implements Parcelable {

    public static final Parcelable.Creator<Apparel> CREATOR = new Creator<Apparel>() {
        @Override
        public Apparel[] newArray(int size) {
            return new Apparel[size];
        }

        @Override
        public Apparel createFromParcel(Parcel source) {
            return new Apparel(source);
        }
    };

    private String imagePath;
    private String name;
    private String color;
    private Category category;
    private HashSet<Style> styles;
    private HashSet<String> tags;
    private Integer minT; //min temperature
    private Integer maxT;
    private String date_of_last_wearing;
    private String date_of_buying;
    private int wearProgress = 0;

    public Apparel(String imagePath, String name, String color, Category category, HashSet<Style> styles, List<String> tags, Integer minT, Integer maxT, String date_of_last_wearing, String date_of_buying) {
        this.imagePath = imagePath;
        this.name = name;
        this.styles = styles;
        this.color = color;
        this.category = category;
        this.tags = new HashSet<String>();
        this.tags.addAll(tags);
        this.minT = minT;
        this.maxT = maxT;
        this.date_of_last_wearing = date_of_last_wearing;
        this.date_of_buying = date_of_buying;
    }

    private Apparel(Parcel parcel) {
        imagePath = parcel.readString();
        name = parcel.readString();
        color = parcel.readString();
        category = (Category) parcel.readSerializable();
        styles = (HashSet<Style>) parcel.readSerializable();
        tags = (HashSet<String>) parcel.readSerializable();
        minT = parcel.readInt();
        maxT = parcel.readInt();
        date_of_last_wearing = parcel.readString();
        date_of_buying = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(imagePath);
        parcel.writeString(name);
        parcel.writeString(color);
        parcel.writeSerializable(category);
        parcel.writeSerializable(styles);
        parcel.writeSerializable(tags);
        parcel.writeInt(minT);
        parcel.writeInt(maxT);
        parcel.writeString(date_of_last_wearing);
        parcel.writeString(date_of_buying);
    }

    private Bitmap loadCover(String imagePath) {
        return BitmapFactory.decodeFile(imagePath);
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
        this.tags = new HashSet<>(tags);
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

    public boolean isNew() {
        return date_of_buying.equals(date_of_last_wearing);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Apparel) {
            Apparel ap = (Apparel) o;
            return imagePath.equals(ap.getImagePath());
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 654548941;
    }
}