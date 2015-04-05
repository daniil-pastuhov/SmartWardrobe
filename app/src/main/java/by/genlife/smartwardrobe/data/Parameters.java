package by.genlife.smartwardrobe.data;

import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Style;

/**
 * Created by NotePad.by on 05.04.2015.
 */
public class Parameters {
    private Style style;
    private Category category;
    private String color;
    private boolean newP;
    private boolean clean;
    private boolean weather;
    private int temperatute;

    public Parameters(Style style, Category category, String color, boolean newP, boolean clean, boolean weather, int temperatute) {
        this.style = style;
        this.category = category;
        this.color = color;
        this.newP = newP;
        this.clean = clean;
        this.weather = weather;
        this.temperatute = temperatute;
    }

    public static class Builder {
        private Style style;
        private Category category;
        private String color;
        private boolean newP;
        private boolean clean;
        private boolean weather;
        private int temperatute;

        public Builder add(Style style) {
            this.style = style;
            return this;
        }

        public Builder add(Category category) {
            this.category = category;
            return this;
        }

        public Builder setClean(boolean clean) {
            this.clean = clean;
            return this;
        }

        public Builder setNew(boolean newP) {
            this.newP = newP;
            return this;
        }

        public Builder setForWeather(boolean flag) {
            weather = flag;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setTemperature(int temperature) {
            this.temperatute = temperature;
            return this;
        }

        public Parameters build() {
            return new Parameters(style, category, color, newP, clean, weather, temperatute);
        }


    }

    public Style getStyle() {
        return style;
    }

    public Category getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public boolean isNewP() {
        return newP;
    }

    public boolean isClean() {
        return clean;
    }

    public boolean isWeather() {
        return weather;
    }

    public int getTemperatute() {
        return temperatute;
    }
}
