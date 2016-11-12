package com.cs442.dsuraj.paymentprocessing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Suraj Didwania on 04-11-2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Movie.db";
    public static final String TABLE_NAME1 = "Movies";
    public static final String TABLE_NAME2 = "Movie_Timings";
    public static final String TABLE_NAME3 = "Movies_Booked";
    public static final String TABLE_NAME4 = "Seats";
    public static final String T1_COL_1 = "MovieName";
    public static final String T1_COL_2 = "Movie_Id";
    public static final String T1_COL_3 = "Information";
    public static final String T1_COL_4 = "Ratings";
    public static final String T1_COL_5 = "User_Reviews";
    public static final String T1_COL_6 = "ComingSoon";
    public static final String T1_COL_7 = "Seats_Available";
    public static final String T2_COL_1 = "Theatres";
    public static final String T2_COL_2 = "Timings";
    public static final String T2_COL_3 = "Price";
    public static final String T2_COL_4 = "Movie_ID";
    public static final String T3_COL_1 = "Movie_Id";
    public static final String T3_COL_2 = "Timings";
    public static final String T3_COL_3 = "Theatres";
    public static final String T3_COL_4 = "Email";
    public static final String T3_COL_5 = "Phone";
    public static final String T3_COL_6 = "Booking_ID";
    public static final String T3_COL_7 = "Quantity";

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME1 +" (NAME VARCHAR, MOVIE_ID INTEGER PRIMARY KEY AUTOINCREMENT,INFORMATION VARCHAR, RATINGS INTEGER)");
        db.execSQL("create table " + TABLE_NAME2 +" (THEATRES VARCHAR, TIMINGS TIME,PRICE INTEGER,MOVIE_ID INTEGER, FOREIGN KEY(MOVIE_ID) REFERENCES Movies(MOVIE_ID))");
        db.execSQL("create table " + TABLE_NAME3 +" (BOOKINGID INTEGER PRIMARY KEY AUTOINCREMENT,MOVIE_ID INTEGER,TIMINGS TIME,THEATRES VARCHAR,EMAIL VARCHAR, PHONE STRING, QUANTITY INTEGER,FOREIGN KEY(MOVIE_ID) REFERENCES Movies(MOVIE_ID))");
        db.execSQL("create table " + TABLE_NAME4 +" (SEAT VARCHAR, THEATRES VARCHAR,TIMINGS TIME, DATE_CURRENT DATE, MOVIE_ID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME4);
        onCreate(db);
    }
    //Sushma Database part
    public boolean insertmoviebooked(int id, String Seats, String theatre, String time, int price, String Email, String phone)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("Movie_ID",id);
        content.put("TIMINGS",time);
        content.put("THEATRES",theatre);
        content.put("EMAIL",Email);
        content.put("PHONE",phone);
        content.put("QUANTITY",Seats);
        long result = db.insert(TABLE_NAME3,null,content);
        if(result == -1) return false; else return true;
    }
    //Seat Selection
    public boolean insertseatbooked(int id, String Seats, String theatre, String time, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("Movie_ID",id);
        content.put("TIMINGS",time);
        content.put("THEATRES",theatre);
        content.put("DATE_CURRENT", date);
        content.put("SEATS", Seats);
        long result = db.insert(TABLE_NAME4,null,content);
        if(result == -1) return false; else return true;
    }
    public boolean insertdatamovie(String movie_name, String Details, String ratings, String User_review, boolean comingsoon, int seats_available )
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME4);
        onCreate(db);

        ContentValues content = new ContentValues();
        content.put("NAME",movie_name);
        content.put("INFORMATION",Details);
        content.put("RATINGS",ratings);
        long result = db.insert(TABLE_NAME1,null,content);
        if(result == -1) return false; else return true;
    }
    public boolean insertdatatimimgs(int MOVIE_ID,String Theatres, String Timings, int price)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put("MOVIE_ID",MOVIE_ID);
        content.put("THEATRES",Theatres);
        content.put("TIMINGS",Timings);
        content.put("PRICE",price);
        long result = db.insert(TABLE_NAME2,null,content);
        if(result == -1) return false; else return true;
    }


    public Cursor getData(SQLiteDatabase db,String  Booking_ID)
    {
        Cursor cursor;
        String sql="SELECT t1.NAME,t2.THEATRES,t3.PRICE,t2.EMAIL,t2.PHONE FROM Movies_Booked t2,Movies t1,Movie_Timings t3 WHERE t3.MOVIE_ID=t2.MOVIE_ID AND t1.MOVIE_ID=t2.MOVIE_ID AND t2.BOOKINGID=? ";

        cursor = db.rawQuery(sql,new String[]{Booking_ID});
        System.out.println(Booking_ID);

        return cursor;
    }
    //Suraj Part

    public Cursor getAllDataMovies()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select MovieName,Information,Rating from " + TABLE_NAME1,null);
        return res;

    }
    public Cursor getAllDataTimings()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select MovieName,Information,Rating from " + TABLE_NAME2,null);
        return res;

    }
    public Cursor getAllDataBooked()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select MovieName,Theatre,Timing from " + TABLE_NAME2,null);
        return res;

    }
}

