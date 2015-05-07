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
import by.genlife.smartwardrobe.listener.OnTaskCompleteListener;

/**
 * Created by NotePad.by on 14.03.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 5;
    private static final String DB_NAME = "catalog";
    private static DBHelper instance = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBHelper getInstance(Context context) {
        if (instance == null) {
            Context appContext = context.getApplicationContext();
            instance = new DBHelper(appContext);
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
        db.execSQL("CREATE TABLE " + DB_NAME + " (" + "name text," + "image_path text primary key,"
                + "size text," + "color text," + "temperature_min int," + "temperature_max int,"
                + "styles text," + "category text," + "tags text," + "wear_progress integer,"
                + "date_of_buying text," + "date_of_last_wearing text," + "repository text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    public void insert(Apparel data) {
        new InsertTask(data).execute();
    }

    public void update(Apparel data, OnTaskCompleteListener listener) {
        new UpdateTask(data, listener).execute();
    }

    public void getAll(OnTaskCompleteListener listener) {
        new GetAllTask(listener).execute();
    }

    public void deleteAll() {
        new DeleteAllTask().execute();
    }

    public void delete(Apparel data) {
        new DeleteTask(data).execute();
    }

    private class GetAllTask extends AsyncTask<Void, Void, ArrayList<Apparel>> {

        private OnTaskCompleteListener listener;

        public GetAllTask(OnTaskCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected ArrayList<Apparel> doInBackground(Void... params) {
            ArrayList<Apparel> all = new ArrayList<>();
            Cursor c = getReadableDatabase().query(DB_NAME, null, null, null, null, null, null);
            while (c.moveToNext()) {
                String imagePath, name, color;
                Category category;
                List<String> tags;
                HashSet<Style> styles = Style.parseString(c.getString(c.getColumnIndex("styles")));
                Integer minT, maxT;
                String date_of_last_wearing, date_of_buying;
                imagePath = c.getString(c.getColumnIndex("image_path"));
                name = c.getString(c.getColumnIndex("name"));
                color = c.getString(c.getColumnIndex("color"));
                date_of_buying = c.getString(c.getColumnIndex("date_of_buying"));
                date_of_last_wearing = c.getString(c.getColumnIndex("date_of_last_wearing"));
                category = Category.valueOf(c.getString(c.getColumnIndex("category")));
                minT = c.getInt(c.getColumnIndex("temperature_min"));
                maxT = c.getInt(c.getColumnIndex("temperature_max"));
                tags = Tags.parseString(c.getString(c.getColumnIndex("tags")));
                Integer wearProgress = c.getInt(c.getColumnIndex("wear_progress"));
                String repository = c.getString(c.getColumnIndex("repository"));
                Apparel data = new Apparel(imagePath, name, color, category, styles, tags, minT, maxT, date_of_last_wearing, date_of_buying, wearProgress, repository);
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
            getWritableDatabase().delete(DB_NAME, "image_path = " + "('" + data.getImagePath() + "')", null);
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
            cv.put("category", data.getCategory().name());
            cv.put("tags", Tags.parseToString(data.getTags()));
            cv.put("repository", data.getRepository());
            getWritableDatabase().insert(DB_NAME, null, cv);
            return null;
        }
    }

    private class UpdateTask extends AsyncTask<Void, Void, Void> {

        private Apparel data;
        private OnTaskCompleteListener listener;

        public UpdateTask(Apparel data, OnTaskCompleteListener listener) {
            this.data = data;
            this.listener = listener;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.success(null);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String sql = "UPDATE " + DB_NAME +
                    " SET color = ?, "+
                    " name = ?, "+
                    " date_of_buying = ?, "+
                    " date_of_last_wearing = ?, "+
                    " styles = ?, "+
                    " temperature_min = ?, "+
                    " temperature_max = ?, "+
                    " category = ?, "+
                    " wear_progress = ?, "+
                    " tags = ?, "+
                    " repository = ? " +
                    "WHERE image_path = ? ";
            Object[] bindArgs = new Object[]{data.getColor(), data.getName(), data.getDate_of_buying(),
                    data.getDate_of_last_wearing(), Style.parseToString(data.getStyles()), data.getMinT(),
                    data.getMaxT(), data.getCategory().name(), data.getWearProgress(), Tags.parseToString(data.getTags()), data.getRepository(), data.getImagePath()};
            getWritableDatabase().execSQL(sql, bindArgs);
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