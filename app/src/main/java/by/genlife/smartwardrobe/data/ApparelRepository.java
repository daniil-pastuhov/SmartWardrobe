package by.genlife.smartwardrobe.data;

import java.util.List;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public interface ApparelRepository {
    List<Apparel> getAll();
    List<Apparel> getByCategory(String category);
    List<Apparel> getByTarget(String target);
    List<Apparel> getDirty();
    List<Apparel> getNotInWash();
    List<Apparel> getInWash();

    List<Apparel> getTopByTarget(String target);
    List<Apparel> getBottomByTarget(String target);
    List<Apparel> getMiddleByTarget(String target);
    List<Apparel> getAccessoriseByTarget(String target);

    Apparel getById(Integer id);
    void setWash(Apparel app, boolean flag);
    void addApparel(Apparel app);
    void deleteApparel(Apparel app);

    List<String> getTargetCategories();
    void addTarget(String s);
    void removeTarget(String s);
}
