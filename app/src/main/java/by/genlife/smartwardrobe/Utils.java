package by.genlife.smartwardrobe;

import android.os.Environment;

import by.genlife.smartwardrobe.constants.Constants;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public class Utils implements Constants{


    public static String getHomeDirectory() {
        return Environment.getExternalStorageDirectory() + homeDirectory;
    }
}
