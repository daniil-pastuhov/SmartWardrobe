package by.genlife.smartwardrobe.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import by.genlife.smartwardrobe.constants.Category;
import by.genlife.smartwardrobe.constants.Style;
import by.genlife.smartwardrobe.constants.Tags;
import by.genlife.smartwardrobe.listener.TaskSuccessListener;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "catalog";
    private static DBHelper instance = null;
    private Context context;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            Context appContext = context.getApplicationContext();
            instance = new DBHelper(appContext);
            instance.context = appContext;
        }
        return instance;
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Instance is not created yet. Call getInstance(Context).");
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_NAME + " (" + "name text," + "id integer primary key autoincrement," + "image_path text" + "size text," + "color text," + "temperature_min int," + "temperature_max int," + "styles text," + "category text," + "tags text," + "wear_progress integer," + "date_of_buying text," + "date_of_last_wearing text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(Apparel data) {
        new InsertTask(data).execute();
    }

    public void getAll(TaskSuccessListener listener) {
        new GetAllTask(listener).execute();
    }

    public void deleteAll() {
        new DeleteAllTask().execute();
    }

    public void delete(Apparel data) {
        new DeleteTask(data).execute();
    }

    private class GetAllTask extends AsyncTask<Void, Void, ArrayList<Apparel>> {

        private TaskSuccessListener listener;

        public GetAllTask(TaskSuccessListener listener) {
            this.listener = listener;
        }

        @Override
        protected ArrayList<Apparel> doInBackground(Void... params) {
            ArrayList<Apparel> all = new ArrayList<>();
            Cursor c = getReadableDatabase().query(DB_NAME, null, null, null, null, null, null);
            while (c.moveToNext()) {
                String imagePath, name, size, color;
                Category category;
                List<String> tags;
                HashSet<Style> styles = Style.parseString(c.getString(c.getColumnIndex("styles")));
                Integer minT, maxT;
                String date_of_last_wearing, date_of_buying;
                imagePath = c.getString(c.getColumnIndex("image_path"));
                name = c.getString(c.getColumnIndex("name"));
                size = c.getString(c.getColumnIndex("size"));
                color = c.getString(c.getColumnIndex("color"));
                date_of_buying = c.getString(c.getColumnIndex("date_of_buying"));
                date_of_last_wearing = c.getString(c.getColumnIndex("date_of_last_wearing"));
                category = Category.valueOf(c.getString(c.getColumnIndex("category")));
                minT = c.getInt(c.getColumnIndex("temperature_min"));
                maxT = c.getInt(c.getColumnIndex("temperature_max"));
                tags = Tags.parseString(c.getString(c.getColumnIndex("tags")));
                Integer id = c.getInt(c.getColumnIndex("id"));
                Integer wearProgress = c.getInt(c.getColumnIndex("wear_progress"));
                Apparel data = new Apparel(id, imagePath, name, size, color, category, styles, tags, minT, maxT, date_of_last_wearing, date_of_buying);
                data.setWearProgress(wearProgress);
                all.add(data);
            }
            return all;
        }

        @Override
        protected void onPostExecute(ArrayList<Apparel> data) {
            listener.success(data);
        }
    }


    private class DeleteTask extends AsyncTask<Void, Void, Void> {

        private Apparel data;

        public DeleteTask(Apparel data) {
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Integer id = data.getId();
            getWritableDatabase().delete(DB_NAME, "id = " + "('" + id + "')", null);
            return null;
        }
    }

    private class InsertTask extends AsyncTask<Void, Void, Void> {

        private Apparel data;

        public InsertTask(Apparel data) {
            this.data = data;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues cv = new ContentValues();
            cv.put("color", data.getColor());
            cv.put("name", data.getName());
            cv.put("date_of_buying", data.getDate_of_buying());
            cv.put("date_of_last_wearing", data.getDate_of_last_wearing());
            cv.put("styles", Style.parseToString(data.getStyles()));
            cv.put("temperature_min", data.getMinT());
            cv.put("temperature_max", data.getMaxT());
            cv.put("image_path", data.getImagePath());
            cv.put("id", data.getId());
            cv.put("size", data.getSize());
            cv.put("category", data.getCategory().name());
            cv.put("tags", Tags.parseToString(data.getTags()));
            getWritableDatabase().insert(DB_NAME, null, cv);
            return null;
        }
    }

    private class DeleteAllTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            getWritableDatabase().execSQL("DELETE FROM " + DB_NAME);
            return null;
        }
    }
}