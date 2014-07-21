package com.victor.levantamientostopograficos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatosObservaciones extends SQLiteOpenHelper 
{
	String sqlCreate = "CREATE TABLE Observaciones (nombreTrabajo TEXT, nombreEstacion TEXT, nombreVisado TEXT, ah TEXT, av TEXT, dg TEXT, m TEXT, i TEXT, tipo TEXT)";
	
	public BaseDatosObservaciones(Context context, String name,	CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXIST Observaciones");
		db.execSQL(sqlCreate);
	}

}
