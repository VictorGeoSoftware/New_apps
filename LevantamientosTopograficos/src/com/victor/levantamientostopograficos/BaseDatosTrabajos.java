package com.victor.levantamientostopograficos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDatosTrabajos extends SQLiteOpenHelper
{
	String sqlCreate = "CREATE TABLE Trabajos (nombreTrabajo TEXT)";

	public BaseDatosTrabajos(Context context, String name, CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) 
	{
		db.execSQL("DROP TABLE IF EXIST Trabajos");
		db.execSQL(sqlCreate);
	}

}
