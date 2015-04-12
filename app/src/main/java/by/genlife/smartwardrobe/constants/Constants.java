package by.genlife.smartwardrobe.constants;

/**
 * Created by NotePad.by on 14.03.2015.
 */

public interface Constants {
    String PACK = "by.genlife.smartwardrobe.";
    String styleSeparator = ";";
    String tagsSeparator = ";";
    String homeDirectory = "/.SmartWardrobeImage/";
    String STATE_CUR_FRAGMENT = "current.fragment.state";
    String ACTION = PACK + "action.";
    String ACTION_WEATHER = ACTION + "weather";

    String EXTRA = PACK + "extra.";
    String EXTRA_WEATHER = EXTRA + "weather";
    String EXTRA_APPAREL = EXTRA + "apparel";

    String STATE = "state.";
    String STATE_STYLES_CHECKED = STATE + "styles.checked";
    String STATE_SPINNER_CATALOG = STATE + "spinner.catalog";
    String STATE_WEATHER = STATE + "weather";

    int PHOTO_REQUEST_CODE = 54987;
    int CLEAN_LEVEL = 10;
    int COLD = -10;
    int WARM = 10;
    int HOT = 20;
}
