package kamil.ciupa.astrotime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kamil on 2017-06-21.
 */

public class AstroWeatherDbAdapter {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "databaseAstro2.db";
    private static final String DB_ASTROWEATHER_TABLE = "astroTable";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_DESCRIPTION = "description";
    public static final String DESCRIPTION_OPTIONS = "TEXT NOT NULL";
    public static final int DESCRIPTION_COLUMN = 1;
    public static final String KEY_COMPLETED = "completed";
    public static final String COMPLETED_OPTIONS = "INTEGER DEFAULT 0";
    public static final int COMPLETED_COLUMN = 2;


    private static final String DB_CREATE_ASTROWEATHER_TABLE =
            "CREATE TABLE " + DB_ASTROWEATHER_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_DESCRIPTION + " " + DESCRIPTION_OPTIONS + ", " +
                    KEY_COMPLETED + " " + COMPLETED_OPTIONS +
                    ");";

    private static final String DB_DROP_ASTROWEATHER_TABLE =
            "DROP TABLE IF EXISTS " + DB_ASTROWEATHER_TABLE;


    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;


    public AstroWeatherDbAdapter(Context context){
        this.context = context;
    }

    public AstroWeatherDbAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException exception){
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close(){
        dbHelper.close();
    }


    public long insertCity(String cityName){
        ContentValues newCity = new ContentValues();
        newCity.put(KEY_DESCRIPTION, cityName);
        return  db.insert(DB_ASTROWEATHER_TABLE, null, newCity);
    }

    public boolean updateCity(CityDataModel cityDataModel){
        long id = cityDataModel.getId();
        String cityName = cityDataModel.getCityName();
        return updateCity(id, cityName);
    }

    public boolean updateCity(long id, String cityName){
        String where = KEY_ID + "=" + id;
        ContentValues updateTodoValues = new ContentValues();
        updateTodoValues.put(KEY_DESCRIPTION, cityName);
        return db.update(DB_ASTROWEATHER_TABLE, updateTodoValues, where, null) > 0;
    }

    public boolean deleteCity(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_ASTROWEATHER_TABLE, where, null) > 0;
    }

    public List<CityDataModel> getAllCities() {
        List<CityDataModel> outputList = new ArrayList<>();
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_COMPLETED};
        Cursor cursor =  db.query(DB_ASTROWEATHER_TABLE, columns, null, null, null, null, null);
        cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CityDataModel city = cursorToCity(cursor);
                 outputList.add(city);
                cursor.moveToNext();
            }
             cursor.close();
            return outputList;
    }

    private CityDataModel cursorToCity(Cursor cursor) {
        CityDataModel city = new CityDataModel();
        city.setId(cursor.getLong(0));
        city.setCityName(cursor.getString(1));
        return city;
    }

    public CityDataModel getTodo(long id) {
        String[] columns = {KEY_ID, KEY_DESCRIPTION, KEY_COMPLETED};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_ASTROWEATHER_TABLE, columns, where, null, null, null, null);
        CityDataModel model = null;
        if(cursor != null && cursor.moveToFirst()) {
            String city = cursor.getString(DESCRIPTION_COLUMN);
            boolean completed = cursor.getInt(COMPLETED_COLUMN) > 0 ? true : false;
            model = new CityDataModel(id, city);
        }
        return model;
    }







    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_ASTROWEATHER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DB_DROP_ASTROWEATHER_TABLE);
            onCreate(db);
        }
    }





}
