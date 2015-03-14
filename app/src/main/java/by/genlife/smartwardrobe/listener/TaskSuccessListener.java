package by.genlife.smartwardrobe.listener;

import java.util.ArrayList;

import by.genlife.smartwardrobe.data.Apparel;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public interface TaskSuccessListener {
    void success(ArrayList<Apparel> result);
}
