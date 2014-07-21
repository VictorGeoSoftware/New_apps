package com.victor.levantamientostopograficos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	// Declaracion de elementos
	EditText txtNombreTrabajo;
	Button btnAnadir;
	ListView listView;
	
	
	
	// Declaracion de variables
	private static final int DIALOGO_SELECCIONAR_TRABAJO = 100;
	private static final int DIALOGO_BORRAR_REGISTRO = 302;
	ArrayList<String> trabajos = new ArrayList<String>();
	String trabajoSeleccionado = "";
	
	
	// Declaracion de base de datos
	SQLiteDatabase dbTrabajos;
	SQLiteDatabase dbEstaciones;
	SQLiteDatabase dbObservaciones;
	ContentValues valuesTrabajo = new ContentValues();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//----- Inicializaci�n de elementos
		txtNombreTrabajo = (EditText)findViewById(R.id.editText1);
		btnAnadir = (Button)findViewById(R.id.button1);
		listView = (ListView)findViewById(R.id.listView1);
		
		
		//------ Inicializaci�n Base de Datos
		BaseDatosEstaciones bdEstaciones = new BaseDatosEstaciones(this, "Estaciones", null, 1);
		dbEstaciones = bdEstaciones.getWritableDatabase();
		
		BaseDatosObservaciones bdObservaciones = new BaseDatosObservaciones(this, "Observaciones", null, 1);
		dbObservaciones = bdObservaciones.getWritableDatabase();
		
		BaseDatosTrabajos bdTrabajos = new BaseDatosTrabajos(this, "Trabajos", null, 1);
		dbTrabajos = bdTrabajos.getWritableDatabase();
		
		
		//----- Inicializaci�n de listado
		refrescarArrayTrabajos();
		refrescarListados(trabajos, listView);
		
		
		//----- Programacion de eventos
		btnAnadir.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				String nombreRecogido = txtNombreTrabajo.getText().toString().replace(" ", "_");
				boolean existeNombre = validarNombreTrabajo(nombreRecogido);
				
				if(existeNombre)
				{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.registro_existe), Toast.LENGTH_SHORT).show();
				}
				else
				{
					valuesTrabajo.put("nombreTrabajo", nombreRecogido);
					dbTrabajos.insert("Trabajos", null, valuesTrabajo);
					
					refrescarArrayTrabajos();
					refrescarListados(trabajos, listView);
				}
				
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				trabajoSeleccionado = listView.getItemAtPosition(arg2).toString().replace(" ", "_");
				VariablesPaso.setTrabajoSeleccionado(trabajoSeleccionado);
				showDialog(DIALOGO_SELECCIONAR_TRABAJO);
			}
			
		});
		
		
	}


	
    protected Dialog onCreateDialog (int id)
    {
    	Dialog ventanaDialogo = null;

    	switch (id)
    	{
    		case DIALOGO_SELECCIONAR_TRABAJO:
    			ventanaDialogo = crearDialogoSeleccionarEstacion();
    		break;
    		
    		case DIALOGO_BORRAR_REGISTRO:
    			ventanaDialogo = crearDialogoBorrarRegistro();
    		break;
    	}
    	
		return ventanaDialogo;
    }
    
    private Dialog crearDialogoSeleccionarEstacion()
    {
    	String titulo = getResources().getString(R.string.seleccionar_trabajo);
    	String mensaje = getResources().getString(R.string.msg_seleccionar_trabajo);
    	String anadirDatos = getResources().getString(R.string.anadir_datos);
    	String calcularPoligonal = getResources().getString(R.string.calcular_poligonal);
    	String borrarTrabajo = getResources().getString(R.string.borrar_trabajo);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	
    	builder.setPositiveButton(anadirDatos, new OnClickListener ()
    	{
			public void onClick(DialogInterface arg0, int arg1) 
			{
				Intent iDatos = new Intent(MainActivity.this, RegistroDatos.class);
				startActivity(iDatos);
			}
    	});
    	
    	builder.setNeutralButton(calcularPoligonal, new OnClickListener()
    	{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				Intent iCalculos = new Intent(MainActivity.this, CalculosTrabajo.class);
				startActivity(iCalculos);
			}
		});
    	
    	builder.setNegativeButton(borrarTrabajo, new OnClickListener()
    	{
			public void onClick(DialogInterface dialog, int which) 
			{
				showDialog(DIALOGO_BORRAR_REGISTRO);
			}
		});
    	
    	return builder.create();
    }
    
    private Dialog crearDialogoBorrarRegistro()
    {
    	String titulo = getResources().getString(R.string.borrar_registros);
    	String mensaje = getResources().getString(R.string.msg_borrar_registros);
    	String aceptar = getResources().getString(R.string.aceptar);
    	String cancelar = getResources().getString(R.string.cancelar);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	
    	builder.setPositiveButton(aceptar, new OnClickListener()
    	{
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				borrarTrabajo(trabajoSeleccionado);
				refrescarArrayTrabajos();
				refrescarListados(trabajos, listView);
			}
		});
    	
    	builder.setNegativeButton(cancelar, new OnClickListener()
    	{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
    	
    	
    	return builder.create();
    }
	
	
	public void refrescarArrayTrabajos()
	{
		trabajos.clear();
		String[] campos = new String[] {"nombreTrabajo"};
		Cursor c = dbTrabajos.query("Trabajos", campos, null, null, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String trabajoRecogido = c.getString(0);
					trabajos.add(trabajoRecogido);
				}
				while(c.moveToNext());
			}
		}
	}
	
	public void refrescarListados(ArrayList<String> lista, ListView listView)
	{
		int lineas = lista.size();
		String[] datos = new String [lineas];
		
		for (int i= 0; i < lineas; i++)
		{
			datos[i] = lista.get(i).toString().replace("_", " ");
		}
		
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_trabajos, datos);
        listView.setAdapter(adaptador);
	}
	
	public boolean validarNombreTrabajo(String nombreTrabajo)
	{
		boolean existeNombre = false;
		String[] campos = new String[] {"nombreTrabajo"};
		Cursor c = dbTrabajos.query("Trabajos", campos, null, null, null, null, null);
		Log.i("validar nombre", "elementos bd: " +c.getColumnCount() + " " + c.getColumnName(0));
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String nombreTrabajoRecogido = c.getString(0);
					if(nombreTrabajoRecogido.contentEquals(nombreTrabajo))
					{
						existeNombre = true;
						break;
					}
				}
				while(c.moveToNext());
			}
		}
		
		return existeNombre;
	}
	
    public void borrarTrabajo(String filaSeleccionada)
    {
		String[] valoresRecogidos = filaSeleccionada.split(" ");
		dbTrabajos.execSQL("DELETE FROM Trabajos WHERE nombreTrabajo=?", valoresRecogidos);
		dbEstaciones.execSQL("DELETE FROM Estaciones WHERE nombreTrabajo=?", valoresRecogidos);
		dbObservaciones.execSQL("DELETE FROM Observaciones WHERE nombreTrabajo=?", valoresRecogidos);
    }


}
