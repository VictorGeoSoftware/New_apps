package com.victor.levantamientostopograficos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatosEstaciones extends SQLiteOpenHelper 
{
	String sqlCreate = "CREATE TABLE Estaciones (nombreTrabajo TEXT, nombreEstacion TEXT, tipo TEXT, x TEXT, y TEXT, z TEXT)";
	
	public BaseDatosEstaciones(Context context, String name, CursorFactory factory, int version) {
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
		db.execSQL("DROP TABLE IF EXIST Estaciones");
		db.execSQL(sqlCreate);
	}

}
