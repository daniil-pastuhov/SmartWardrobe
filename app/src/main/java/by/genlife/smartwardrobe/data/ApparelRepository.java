package by.genlife.smartwardrobe.data;

import java.util.List;
import java.util.Map;

import by.genlife.smartwardrobe.constants.Category;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public interface ApparelRepository {
    List<Apparel> getAll();

    List<Apparel> getByParams(Parameters parameters);

    Map<Category, List<Apparel>> getTodaySuits(Parameters parameters);

    void addApparel(Apparel app);

    void deleteApparel(Apparel app);

    List<String> seachByTags(String... tags);

    List<String> getAllColors();
}
