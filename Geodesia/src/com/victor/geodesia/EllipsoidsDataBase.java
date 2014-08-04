package com.victor.geodesia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class EllipsoidsDataBase extends SQLiteOpenHelper 
{
	String sqlCreate = "CREATE TABLE Ellipsoids (name TEXT, a TEXT, f TEXT)";
	
	public EllipsoidsDataBase(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST Ellipsoids");
		db.execSQL(sqlCreate);
	}
}
