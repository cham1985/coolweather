package app.coolweather.com.coolweather.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.coolweather.com.coolweather.db.CoolWeatherOpenHelper;

/**
 * Created by Ebates on 15/12/28.
 */
//封装数据库操作
public class CoolWeatherDB {
    //数据库名
    public static final String DB_NAME="cool_weather";

    //数据库版本
    public static final int VERSION=1;

    //单例
    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        db=dbHelper.getWritableDatabase();
    }

    //获取CoolWeatherDB单例
    public synchronized static CoolWeatherDB getInstance(Context context){
        if (coolWeatherDB==null){
            coolWeatherDB=new CoolWeatherDB(context);
        }
        return coolWeatherDB;
    }

    //将Province实例存到数据库
    public void saveProvince(Province province){
        if (province!=null){
            ContentValues values=new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }

    //从数据库读取全国所有省份信息
    public List<Province> loadProvinces(){
        List<Province> list=new ArrayList<Province>();

        Cursor cursor=db.query("Province",null,null,null,null,null,null);
        if (cursor.moveToFirst()){//遍历每一行,一行对应一个model
            do {
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
            } while (cursor.moveToNext());
        }

        return list;
    }

    //将City实例存到数据库
    public void saveCity(City city){
        if (city!=null){
            ContentValues values=new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());

            db.insert("City",null,values);
        }
    }

    //从数据库读取某省下所有城市信息
    public List<City> loadCities(int provinceId){
        List<City> list=new ArrayList<City>();
        Cursor cursor=db.query("City",null, "province_id= ? ", new String[]{String.valueOf(provinceId)},
                null,null,null);

        if (cursor.moveToFirst()){
            do {
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);

            } while (cursor.moveToNext());
        }

        return list;
    }

    //将County实例存储到数据库
    public void saveCounty(County county){
        if (county!=null){
            ContentValues values=new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null, values);
        }
    }

    //从数据库读取某城市下所有县的信息
    public List<County> loadCounties(int cityId){
        List<County> list=new ArrayList<County>();
         Cursor cursor=db.query("County", null, "city_id = ? ", new String[]{
                 String.valueOf(cityId)
         }, null, null ,null );
        if (cursor.moveToFirst()){
            do {
                County county=new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);

            } while (cursor.moveToNext());
        }

        return list;
    }
}
