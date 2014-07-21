package com.victor.levantamientostopograficos;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class RegistroDatos extends Activity 
{
	//----- Declaracion de elementos
	TextView lblTitulo;
	TextView lblListaEstaciones;
	TextView lblObservaciones;
	
	EditText txtNombreEstacion;
	EditText txtAlturaEstacion;
	EditText txtAlturaMira;
	
	Button btnAnadir;
	Button btnBaseReferencia;
	Button btnObservarPunto;
	
	ListView lstEstaciones;
	ListView lstObservaciones;
	
	RadioGroup radioGroupTipoPunto;
	
		//----- Dialogs personalizados
		//Dialogo actualizar nombre estaci�n
	EditText campoDialogo;
	
		//Dialogo observar a base o referencia
	TextView lblEstacionActual;
	Spinner spnListaEstaciones;
	RadioButton estacionRadio0;
	RadioButton estacionRadio1;
	EditText txtAnguloHorizontalD;
	EditText txtAnguloVerticalD;
	EditText txtDistanciaD;
	EditText txtAnguloHorizontalI;
	EditText txtAnguloVerticalI;
	EditText txtDistanciaI;
	
		//Dialogo observar a punto
	ToggleButton toggleButton1;
	LinearLayout layoutCirculoInverso; //Punto en el que se a�ade el view con el circulo inverso
	View viewInsertar;
	TextView txtEstacionActual;
	EditText txtObsIAnguloHorizontal;
	EditText txtObsIAnguloVertical;
	EditText txtObsIAnguloDistancia;
		
		//Dialogo actualizar punto
	EditText txtNombrePuntoActualizar;
	EditText txtActDAnguloHorizontal;
	EditText txtActDAnguloVertical;
	EditText txtActDDistancia;
	
	
	//----- Declaracion de Variables
	private static final int DIALOGO_OBSERVAR_ESTACION = 200;
	private static final int DIALOGO_OBSERVAR_PUNTO = 201;
	private static final int DIALOGO_PUNTO_SELECCINADO = 300;
	private static final int DIALOGO_ACTUALIZAR_PUNTO = 301;
	private static final int DIALOGO_BORRAR_REGISTRO = 302;
	
	String listaEstaciones = "";
	String listaReferencias = "";
	String trabajoSeleccionado = "";
	String estacionSeleccionada = "";
	String puntoSeleccionado = "";
	String tipoEstacion = "base";
	String msgCamposVacios = "";
	String msgExisteRegistro = "";
	String literalObservaciones = "";
	String literalEstacionActual = "";
	ArrayList<String> estaciones = new ArrayList<String>();
	ArrayList<String> referencias = new ArrayList<String>();
	ArrayList<String> observaciones = new ArrayList<String>();
	ArrayList<String> vacio = new ArrayList<String>();
	boolean base = true;
	boolean toogleButtonObservaciones = false;
	
	
	//----- Declaracion de bases de datos
	SQLiteDatabase dbObservaciones;
	ContentValues valuesObservaciones = new ContentValues();
	SQLiteDatabase dbEstaciones;
	ContentValues valuesEstaciones = new ContentValues();
	
	//----- Declaraci�n de clase para adapter personalizado
	BeanObservaciones[] fila;
	
	DecimalFormat cuatroDigitos = new DecimalFormat("0.0000");
	DecimalFormat tresDigitos = new DecimalFormat("0.000");
	
	
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_datos);
		
		//----- Inicializaci�n de elementos
		lblTitulo = (TextView)findViewById(R.id.textView1);
		lblListaEstaciones = (TextView)findViewById(R.id.textView3);
		lblObservaciones = (TextView)findViewById(R.id.textView4);
		
		txtNombreEstacion = (EditText)findViewById(R.id.editText1);
		txtAlturaEstacion = (EditText)findViewById(R.id.editText2);
		txtAlturaMira = (EditText)findViewById(R.id.editText3);
		
		btnAnadir = (Button)findViewById(R.id.button1);
		btnBaseReferencia = (Button)findViewById(R.id.button2);
		btnObservarPunto = (Button)findViewById(R.id.button3);
		
		lstEstaciones = (ListView)findViewById(R.id.listView1);
		lstObservaciones = (ListView)findViewById(R.id.listView2);
		
		radioGroupTipoPunto = (RadioGroup)findViewById(R.id.radioGroup1);
		
		btnBaseReferencia.setEnabled(false);
		btnObservarPunto.setEnabled(false);
		
		//----- Inicializaci�n de variables
		trabajoSeleccionado = VariablesPaso.getTrabajoSeleccionado();
		String titulo = getResources().getString(R.string.poligonal) + " - " + trabajoSeleccionado.replace("_", " ");
		lblTitulo.setText(titulo);
		listaEstaciones = getResources().getString(R.string.lista_estaciones);
		listaReferencias = getResources().getString(R.string.lista_referencias);
		msgCamposVacios = getResources().getString(R.string.msg_campos_vacios);
		msgExisteRegistro = getResources().getString(R.string.msg_existe_registro);
		literalObservaciones = getResources().getString(R.string.observaciones);
		literalEstacionActual = getResources().getString(R.string.estacion_actual);
		
		
		//----- Inicializaci�n de base de datos
		BaseDatosObservaciones bdObservaciones =  new BaseDatosObservaciones(this, "Observaciones", null, 1);
		dbObservaciones = bdObservaciones.getWritableDatabase();
		BaseDatosEstaciones bdEstaciones = new BaseDatosEstaciones(this, "Estaciones", null, 1);
		dbEstaciones = bdEstaciones.getWritableDatabase();
		
		//----- Inicializaci�n de listado de bases
		refrescarArrayEstaciones();
		refrescarListadoEstaciones(estaciones, lstEstaciones);
		
		
		//----- Programacion de eventos
		radioGroupTipoPunto.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup arg0, int arg1) 
			{
				switch(arg1)
				{
					case R.id.radio0:
						tipoEstacion = "base";
						lblListaEstaciones.setText(listaEstaciones);
						refrescarArrayEstaciones();
						refrescarListadoEstaciones(estaciones, lstEstaciones);
						base = true;
					break;
					
					case R.id.radio1:
						tipoEstacion = "referencia";
						lblListaEstaciones.setText(listaReferencias);
						refrescarArrayReferencias();
						refrescarListadoEstaciones(referencias, lstEstaciones);
						base= false;
						btnBaseReferencia.setEnabled(false);
						btnObservarPunto.setEnabled(false);
					break;
				}
			}
		});
		
		btnAnadir.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				String nombreRecogido = txtNombreEstacion.getText().toString().replace(" ", "_");
				
				if(!nombreRecogido.contentEquals(""))
				{
					boolean existeNombre = validarNombreEstacion(nombreRecogido);
					
					if(existeNombre)
					{
						Toast.makeText(getApplicationContext(), msgExisteRegistro, Toast.LENGTH_SHORT).show();
					}
					else
					{
						valuesEstaciones.put("nombreTrabajo", trabajoSeleccionado);
						valuesEstaciones.put("nombreEstacion", nombreRecogido);
						// Solo debe enze�ar las bases, ya que las referencias s�lo las mostrar� en el dialog de observaciones
						valuesEstaciones.put("tipo", tipoEstacion);
						valuesEstaciones.put("x", 0);
						valuesEstaciones.put("y", 0);
						valuesEstaciones.put("z", 0);
						
						dbEstaciones.insert("Estaciones", null, valuesEstaciones);
						
	    				if(base)
	    				{
	    					refrescarArrayEstaciones();
	    					refrescarListadoEstaciones(estaciones, lstEstaciones);
	    				}
	    				else
	    				{
	    					refrescarArrayReferencias();
	    					refrescarListadoEstaciones(referencias, lstEstaciones);
	    				}
					}
				}
				
			}
		});
		
		lstEstaciones.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				estacionSeleccionada = lstEstaciones.getItemAtPosition(arg2).toString().replace(" ", "_");
				
				refrescarArrayObservaciones(estacionSeleccionada);
				refrescarListadoPuntos(observaciones, lstObservaciones);
				
				if(base)
				{
					lblObservaciones.setText(literalObservaciones + " - " + estacionSeleccionada.replace("_", " "));
					btnBaseReferencia.setEnabled(true);
					btnObservarPunto.setEnabled(true);
				}
			}
		});
		
		lstEstaciones.setOnItemLongClickListener(new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				estacionSeleccionada = lstEstaciones.getItemAtPosition(arg2).toString().replace(" ", "_");
				showDialog(DIALOGO_BORRAR_REGISTRO);
				
				return false;
			}
		});
		
		btnBaseReferencia.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				showDialog(DIALOGO_OBSERVAR_ESTACION);
				
				lblEstacionActual.setText(literalEstacionActual + ": " + estacionSeleccionada);
				refrescarSpinner(vacio, spnListaEstaciones, estacionSeleccionada); //Cargo vac�o para obligar a seleccionar una opci�n
				
				 txtAnguloHorizontalD.setText("");
				 txtAnguloVerticalD.setText("");
				 txtDistanciaD.setText("");
				 txtAnguloHorizontalI.setText("");
				 txtAnguloVerticalI.setText("");
				 txtDistanciaI.setText("");
			}
		});
		
		btnObservarPunto.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				showDialog(DIALOGO_OBSERVAR_PUNTO);
				txtEstacionActual.setText(literalEstacionActual + ": " + estacionSeleccionada);
			}
		});
		
		lstObservaciones.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
			{
				puntoSeleccionado = fila[arg2].getPunto().toString();
				showDialog(DIALOGO_PUNTO_SELECCINADO);
				return false;
			}
		});
		
	}
	
	
	
    public void onToggleClicked (View view)
    {
    	toogleButtonObservaciones = ((ToggleButton) view).isChecked();
    	ViewGroup layoutPadre = layoutCirculoInverso;
    	
    	if (toogleButtonObservaciones)
    	{
    		layoutPadre.addView(viewInsertar);
    		
    		txtObsIAnguloHorizontal = (EditText) viewInsertar.findViewById(R.id.editText10);
    		txtObsIAnguloVertical = (EditText) viewInsertar.findViewById(R.id.editText11);
    		txtObsIAnguloDistancia = (EditText) viewInsertar.findViewById(R.id.editText12);
    	}
    	else
    	{
    		layoutPadre.removeView(viewInsertar);
    	}
    }
	
	
	
	public boolean validarNombreEstacion(String nombreEstacion)
	{
		boolean existeNombre = false;
		String[] campos = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
		String[] args = new String[]{trabajoSeleccionado, nombreEstacion};
		Cursor c = dbEstaciones.query("Estaciones", campos, "nombreTrabajo=? AND nombreEstacion=?", args, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String nombreEstacionRecogida = c.getString(1);
					if(nombreEstacionRecogida.contentEquals(nombreEstacion))
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
	
	public boolean validarRegistroPuntos(String registro)
	{
		boolean existeNombre = false;
		String[] camposBaseDatos = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
		String[] args = new String[] {trabajoSeleccionado, estacionSeleccionada, registro};
		Cursor c = dbObservaciones.query("Observaciones", camposBaseDatos, "nombreTrabajo=? AND nombreEstacion=? AND nombreVisado=?", args, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String nombreRegistroRecogido = c.getString(2);
					if(nombreRegistroRecogido.contentEquals(registro))
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
	
    public void borrarEstacion(String estacionSeleccionada)
    {
		String[] valoresRecogidos = {trabajoSeleccionado, estacionSeleccionada};
		
		dbEstaciones.execSQL("DELETE FROM Estaciones WHERE nombreTrabajo=? AND nombreEstacion=?", valoresRecogidos);
		dbObservaciones.execSQL("DELETE FROM Observaciones WHERE nombreTrabajo=? AND nombreEstacion=?", valoresRecogidos);
    }
    
    public void actualizarPunto(String ah, String av, String distancia, String m, String i)
    {
    	String[] valoresPuntoSeleccionado = puntoSeleccionado.split(" ");
    	String nombrePuntoSeleccionado = valoresPuntoSeleccionado[0];
    	Log.i("punto que va a actualizarse", "valores: "+ 
    			trabajoSeleccionado+ " " + estacionSeleccionada + " " + nombrePuntoSeleccionado);
    	
    	ContentValues valuesActualizar = new ContentValues();
		valuesActualizar.put("ah", ah);
		valuesActualizar.put("av", av);
		valuesActualizar.put("dg", distancia);
		valuesActualizar.put("m", m);
		valuesActualizar.put("i", i);

		String[] args = new String[]{trabajoSeleccionado, estacionSeleccionada, nombrePuntoSeleccionado};
		dbObservaciones.update("Observaciones", valuesActualizar, "nombreTrabajo=? AND nombreEstacion=? AND nombreVisado=?", args);		
    }
    
    public void borrarPunto(String puntoSeleccionado)
    {
    	String punto = trabajoSeleccionado + " " + estacionSeleccionada + " " + puntoSeleccionado;
    	String[] valores = punto.split(" ");
    	
		dbObservaciones.execSQL("DELETE FROM Observaciones WHERE nombreTrabajo=? AND nombreEstacion=? AND nombreVisado=? AND ah=? AND av=? AND dg=? AND m=? AND i=?", valores);
    }
    
    public void borrarObservacionesReferencia(String referenciaSeleccionada)
    {
    	String[] valores = new String[] {trabajoSeleccionado, referenciaSeleccionada};
		dbObservaciones.execSQL("DELETE FROM Observaciones WHERE nombreTrabajo=? AND nombreVisado=?", valores);
    }
	
	public void refrescarArrayEstaciones()
	{
		estaciones.clear();
		String[] campos = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
		// Solo debe enze�ar las bases, ya que las referencias s�lo las mostrar� en el dialog de observaciones
		String[] args = new String[] {trabajoSeleccionado, "base"};
		Cursor c = dbEstaciones.query("Estaciones", campos, "nombreTrabajo=? AND tipo=?", args, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String estacionRecogida = c.getString(1);
					estaciones.add(estacionRecogida);
				}
				while(c.moveToNext());
			}
		}
	}
	
	public void refrescarArrayReferencias()
	{
		referencias.clear();
		String[] campos = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
		// Solo debe enze�ar las referencias, para el radigroup principal
		String[] args = new String[] {trabajoSeleccionado, "referencia"};
		Cursor c = dbEstaciones.query("Estaciones", campos, "nombreTrabajo=? AND tipo=?", args, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String estacionRecogida = c.getString(1);
					referencias.add(estacionRecogida);
				}
				while(c.moveToNext());
			}
		}
	}
	
	public void refrescarListadoEstaciones(ArrayList<String> lista, ListView listView)
	{
		int lineas = lista.size();
		String[] datos = new String [lineas];
		
		for (int i= 0; i < lineas; i++)
		{
			datos[i] = lista.get(i).toString().replace("_", " ");
		}
		
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_negro, datos);
        listView.setAdapter(adaptador);
	}
	
	public void refrescarSpinner(ArrayList<String> lista, Spinner spinner, String estacionDescartar) //- estacion Descartar: para que no muestre la estaci�n en la que estoy en ese momento
	{
		lista.remove(estacionDescartar);
		// Quitar tambi�n las estaciones que ya hayan sido visadas
		for (int j = 0; j < lista.size(); j++)
		{
			String estacionComparar = lista.get(j).toString();
			
			for(int k = 0; k < observaciones.size(); k++)
			{
				String[] lecturaLeida = observaciones.get(k).split(" ");
				String estacionObservacionComparar = lecturaLeida[0];
				
				if(estacionObservacionComparar.contentEquals(estacionComparar))
				{
					lista.remove(estacionObservacionComparar);
				}
			}
		}
		
		int lineas = lista.size();
		String[] datos = new String [lineas];
		
		for(int i = 0; i < lineas; i++)
		{
			datos[i] = lista.get(i).toString();
		}

		ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_spinner_selected, datos);
		adaptador.setDropDownViewResource(R.layout.row_spinner);
		spinner.setAdapter(adaptador);
	}
	
    public String calcularAnguloHorizontal(String horizontalCD, String horizontalCI)
    {
    	String mediaHorizontal = "";
    	double horizontalDirecto = Double.parseDouble(horizontalCD);
    	double horizontalInverso = Double.parseDouble(horizontalCI);
    	double horizontalMedio = 0;
    	
    	if(horizontalDirecto < 200)
    	{
    		horizontalMedio = (horizontalDirecto + (horizontalInverso - 200))/2;
    	}
    	else
    	{
    		horizontalMedio = (horizontalDirecto + (horizontalInverso + 200))/2;
    	}
    	
    	mediaHorizontal = cuatroDigitos.format(horizontalMedio).replace(",", ".");
    	return mediaHorizontal;
    }
    
    public String calcularAnguloVertical(String verticalCD, String verticalCI)
    {
    	String mediaVertical = "";
    	double verticalDirecto = Double.parseDouble(verticalCD);
    	double verticalInverso = Double.parseDouble(verticalCI);
    	double verticalMedio = 0;
    	
    	verticalMedio = (verticalDirecto + (400 - verticalInverso))/2;
    	
    	mediaVertical = cuatroDigitos.format(verticalMedio).replace(",", ".");
    	return mediaVertical;
    }
    
    public String calcularDistanciaMedia(String distanciaCD, String distanciaCI)
    {
    	String distanciaMedia = "";
    	double distanciaDirecto = Double.parseDouble(distanciaCD);
    	double distanciaInverso = Double.parseDouble(distanciaCI);
    	double distanciaMed = 0;
    	
    	distanciaMed = (distanciaDirecto + distanciaInverso)/2;
    	
    	distanciaMedia = tresDigitos.format(distanciaMed).replace(",", ".");
    	return distanciaMedia;		
    }
    
	public void anadirRegistroObservacion(String nombreTrabajo, String nombreEstacion, String nombreVisado, String ah, String av, String dg, String m, String i, String tipo)
	{
		valuesObservaciones.put("nombreTrabajo", nombreTrabajo);
		valuesObservaciones.put("nombreEstacion", nombreEstacion);
		valuesObservaciones.put("nombreVisado", nombreVisado);
		valuesObservaciones.put("ah", ah);
		valuesObservaciones.put("av", av);
		valuesObservaciones.put("dg", dg);
		valuesObservaciones.put("m", m);
		valuesObservaciones.put("i", i);
		valuesObservaciones.put("tipo", tipo);
		
		dbObservaciones.insert("Observaciones", null, valuesObservaciones);
	}
	
	public void refrescarArrayObservaciones(String estacion)
	{
		observaciones.clear();
		String[] campos = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
		String[] args = new String[] {trabajoSeleccionado, estacion};
		Cursor c = dbObservaciones.query("Observaciones", campos, "nombreTrabajo=? AND nombreEstacion=?", args, null, null, null);
		Log.i("refrescar array observaciones", "estacion: "+ trabajoSeleccionado + " " + estacion);
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String visadoRecogido = c.getString(2);
					String ahRecogido = c.getString(3);
					String avRecogido = c.getString(4);
					String dgRecogida = c.getString(5);
					String mRecogida = c.getString(6);
					String iRecogida = c.getString(7);
					String tipoRecogido = c.getString(8);
					Log.i("refrescar array observaciones", "datos encontrados: "+ visadoRecogido + " " + tipoRecogido);
					observaciones.add(visadoRecogido + " " + ahRecogido + " " + avRecogido + " " + dgRecogida + " " 
										+ mRecogida + " " + iRecogida + " " + tipoRecogido);
				}
				while(c.moveToNext());
			}
		}
	}
	
	public void refrescarListadoPuntos(ArrayList<String> lista, ListView listView)
	{
		//- Separar las bases de los puntos
		ArrayList<String> listaBases = new ArrayList<String>();
		ArrayList<String> listaPuntos = new ArrayList<String>();
		ArrayList<String> listaTipoPunto = new ArrayList<String>();
		
		for(int i = 0; i < lista.size(); i++)
		{
			String[] puntoRecibido = lista.get(i).toString().split(" ");
			String tipoPunto = puntoRecibido[6];
			listaTipoPunto.add(tipoPunto);
			if(tipoPunto.contentEquals("base") || tipoPunto.contentEquals("referencia"))
			{
				listaBases.add(lista.get(i).toString());
			}
			else
			{
				listaPuntos.add(lista.get(i).toString());
			}
		}
		
		//- Unir los dos arrays
		listaBases.addAll(listaPuntos);

		fila = new BeanObservaciones[listaBases.size()];
		
		for(int i = 0; i < listaBases.size(); i++)
		{
			String[] puntoRecibido = listaBases.get(i).toString().split(" ");
			String tipoPunto = puntoRecibido[6];
			
			fila[i] = new BeanObservaciones(listaBases.get(i).replace(" punto", "").replace(" base", "").replace("referencia", ""), tipoPunto);
		}
		
		AdaptadorObservaciones adaptador = new AdaptadorObservaciones(this);
		listView.setAdapter(adaptador);
	}
	
	class AdaptadorObservaciones extends ArrayAdapter<Object>
	{
		Activity context;
		
		public AdaptadorObservaciones(Activity context)
		{
			super(context, R.layout.row_personalizado, fila);
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.row_personalizado, null);
			
			TextView lblNombrePunto = (TextView) item.findViewById(R.id.textView1);
			TextView lblDatosPunto = (TextView) item.findViewById(R.id.textView2);
			
			String[] punto = fila[position].getPunto().split(" ");
			String nombrePunto = punto[0];
			String datosPunto = "";
			for(int i = 1; i < punto.length; i++)
			{
				datosPunto = datosPunto + punto[i].toString().replace(",",".") + " ";
			}
			
			lblNombrePunto.setText(nombrePunto);
			lblDatosPunto.setText(datosPunto);
			
			String tipoPunto = fila[position].getTipoPunto();
			
			if(tipoPunto.contentEquals("base") || tipoPunto.contentEquals("referencia"))
			{
				lblNombrePunto.setTextColor(Color.RED);
				lblDatosPunto.setTextColor(Color.RED);
			}
			else
			{
				lblNombrePunto.setTextColor(Color.BLACK);
				lblDatosPunto.setTextColor(Color.BLACK);
			}
			
			return item;
		}
		
	}
	
	
	
	
	
	//------------------- DIALOGOS ------------------------------------------------------------------------
    protected Dialog onCreateDialog (int id)
    {
    	Dialog ventanaDialogo = null;

    	switch (id)
    	{
    		case DIALOGO_OBSERVAR_ESTACION:
    			ventanaDialogo = crearDialogoObservacionEstacion();
    		break;
    		
    		case DIALOGO_OBSERVAR_PUNTO:
    			ventanaDialogo = crearDialogoObservaciones();
    		break;
    		
    		case DIALOGO_PUNTO_SELECCINADO:
    			ventanaDialogo = crearDialogoPuntoSeleccionado();
    		break;
    		
    		case DIALOGO_ACTUALIZAR_PUNTO:
    			ventanaDialogo = crearDialogoActualizarPunto();
    		break;
    		
    		case DIALOGO_BORRAR_REGISTRO:
    			ventanaDialogo = crearDialogoBorrarRegistro();
    		break;
    	}
    	
		return ventanaDialogo;
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
				borrarEstacion(estacionSeleccionada);
				borrarObservacionesReferencia(estacionSeleccionada);
				
				refrescarArrayEstaciones();
				refrescarListadoEstaciones(estaciones, lstEstaciones);
				
				refrescarArrayObservaciones(estacionSeleccionada);
				refrescarListadoPuntos(observaciones, lstObservaciones);
				
				Log.i(" long listener lstEstaciones", "elementos: " + lstEstaciones.getCount());
				estacionSeleccionada = "";
				lblObservaciones.setText(literalObservaciones);
				
				if(lstEstaciones.getCount() < 1)
				{
					btnBaseReferencia.setEnabled(false);
					btnObservarPunto.setEnabled(false);
				}
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
    
    private Dialog crearDialogoPuntoSeleccionado()
    {
    	String titulo = getResources().getString(R.string.modificar_punto);
    	String mensaje = getResources().getString(R.string.msg_modificar_punto);
    	String actualizar = getResources().getString(R.string.actualizar);
    	String borrar = getResources().getString(R.string.borrar);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	
    	builder.setPositiveButton(actualizar, new OnClickListener()
    	{	
			@Override
			public void onClick(DialogInterface arg0, int arg1) 
			{
				showDialog(DIALOGO_ACTUALIZAR_PUNTO);
				txtEstacionActual.setText(estacionSeleccionada);
				String[] valoresPunto = puntoSeleccionado.split(" ");
				String nombre = valoresPunto[0].toString();
				txtNombrePuntoActualizar.setText(nombre);
				txtNombrePuntoActualizar.setEnabled(false);
				txtActDAnguloHorizontal.setText(valoresPunto[1]);
				txtActDAnguloVertical.setText(valoresPunto[2]);
				txtActDDistancia.setText(valoresPunto[3]);
			}
		});
    	
    	builder.setNegativeButton(borrar, new OnClickListener()
    	{	
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				borrarPunto(puntoSeleccionado);
				refrescarArrayObservaciones(estacionSeleccionada);
				refrescarListadoPuntos(observaciones, lstObservaciones);
			}
		});
    	
    	
    	return builder.create();
    }
    
    private Dialog crearDialogoActualizarPunto()
    {
    	String titulo = getResources().getString(R.string.actualizar_punto);
    	String mensaje = getResources().getString(R.string.msg_actualizar_punto);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	LayoutInflater inflater = this.getLayoutInflater();
    	View layout = inflater.inflate(R.layout.dialog_observaciones, null);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	builder.setView(layout);
    	
    	toggleButton1 = (ToggleButton) layout.findViewById(R.id.toggleButton1);
    	layoutCirculoInverso = (LinearLayout) layout.findViewById(R.id.layout_circulo_inverso);
    	viewInsertar = inflater.inflate(R.layout.observaciones_circulo_inverso, null);
    	txtEstacionActual = (TextView) layout.findViewById(R.id.textView1);
    	txtNombrePuntoActualizar = (EditText) layout.findViewById(R.id.editText4);
    	
    	txtActDAnguloHorizontal = (EditText) layout.findViewById(R.id.editText1);
    	txtActDAnguloVertical = (EditText) layout.findViewById(R.id.editText2);
    	txtActDDistancia = (EditText) layout.findViewById(R.id.editText3);

		final Button btnAceptar = (Button) layout.findViewById(R.id.button2);
		final Button btnCancelar = (Button) layout.findViewById(R.id.button1);
		
		btnAceptar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				String nombrePunto = txtNombrePuntoActualizar.getText().toString();
				String DanguloHorizontal = txtActDAnguloHorizontal.getText().toString().replace(",", ".");
				String DanguloVertical = txtActDAnguloVertical.getText().toString().replace(",", ".");
				String Ddistancia = txtActDDistancia.getText().toString().replace(",", ".");

				String alturaEstacion = txtAlturaEstacion.getText().toString();
				String alturaMira = txtAlturaMira.getText().toString();
				
				if(toogleButtonObservaciones)
				{
					String IanguloHorizontal = txtObsIAnguloHorizontal.getText().toString();
					String IanguloVertical = txtObsIAnguloVertical.getText().toString();
					String Idistancia = txtObsIAnguloDistancia.getText().toString();
					
					if(nombrePunto.contentEquals("") || 
						DanguloHorizontal.contentEquals("") || DanguloVertical.contentEquals("") || Ddistancia.contentEquals("") ||
						IanguloHorizontal.contentEquals("") || IanguloVertical.contentEquals("") || Idistancia.contentEquals("") ||
						alturaEstacion.contentEquals("") ||alturaMira.contentEquals(""))
					{
						if(alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_faltan_alturas), Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_campos_vacios), Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						String horizontalMedio = calcularAnguloHorizontal(DanguloHorizontal, IanguloHorizontal);
						String verticalMedio = calcularAnguloVertical(DanguloVertical, IanguloVertical);
						String distancia = calcularDistanciaMedia(Ddistancia, Idistancia);
						double i = Double.parseDouble(alturaEstacion);
						double m = Double.parseDouble(alturaMira);
						
						actualizarPunto(horizontalMedio, verticalMedio, distancia, tresDigitos.format(m).replace(",", "."), tresDigitos.format(i).replace(",", "."));
						
						refrescarArrayObservaciones(estacionSeleccionada);
						refrescarListadoPuntos(observaciones, lstObservaciones);
						dismissDialog(DIALOGO_ACTUALIZAR_PUNTO);
					}
				}
				else
				{
					if(nombrePunto.contentEquals("") || 
					DanguloHorizontal.contentEquals("") || DanguloVertical.contentEquals("") || Ddistancia.contentEquals("") ||
					alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
					{
						if(alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_faltan_alturas), Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_campos_vacios), Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						//Igualo formatos
						double aHorizontal = Double.parseDouble(DanguloHorizontal);
						double aVertical = Double.parseDouble(DanguloVertical);
						double distancia = Double.parseDouble(Ddistancia);
						double i = Double.parseDouble(alturaEstacion);
						double m = Double.parseDouble(alturaMira);
						
						String ah= cuatroDigitos.format(aHorizontal).replace(",", ".");
						String av = cuatroDigitos.format(aVertical).replace(",", ".");
						String d = tresDigitos.format(distancia).replace(",", ".");
						
						actualizarPunto(ah, av, d, tresDigitos.format(m).replace(",", "."), tresDigitos.format(i).replace(",", "."));
						
						refrescarArrayObservaciones(estacionSeleccionada);
						refrescarListadoPuntos(observaciones, lstObservaciones);
						dismissDialog(DIALOGO_ACTUALIZAR_PUNTO);
					}
				}
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dismissDialog(DIALOGO_ACTUALIZAR_PUNTO);
			}
		});
    	
    	
    	return builder.create();
    }
    
    private Dialog crearDialogoObservacionEstacion()
    {
    	String titulo = getResources().getString(R.string.observar_estacion);
    	String mensaje = getResources().getString(R.string.msg_observar_estacion);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	LayoutInflater inflater = this.getLayoutInflater();
    	View layout = inflater.inflate(R.layout.dialog_observacion_estacion, null);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	builder.setView(layout);
    	
    	spnListaEstaciones = (Spinner) layout.findViewById(R.id.spinner1);
    	lblEstacionActual = (TextView) layout.findViewById(R.id.textView1);
    	final RadioGroup radioTipoEstacion = (RadioGroup) layout.findViewById(R.id.radioGroup1);
    	estacionRadio0 = (RadioButton) layout.findViewById(R.id.dialog_radio0);
    	estacionRadio1 = (RadioButton) layout.findViewById(R.id.dialog_radio1);
    	
		txtAnguloHorizontalD = (EditText) layout.findViewById(R.id.editText1);
		txtAnguloVerticalD = (EditText) layout.findViewById(R.id.editText2);
		txtDistanciaD = (EditText) layout.findViewById(R.id.editText3);
		txtAnguloHorizontalI = (EditText) layout.findViewById(R.id.editText4);
		txtAnguloVerticalI = (EditText) layout.findViewById(R.id.editText5);
		txtDistanciaI = (EditText) layout.findViewById(R.id.editText6);
    	final Button btnAceptar = (Button) layout.findViewById(R.id.button2);
    	final Button btnCancelar = (Button) layout.findViewById(R.id.button1);
    	
    	radioTipoEstacion.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(RadioGroup arg0, int arg1) 
			{
				switch(arg1)
				{
					case R.id.dialog_radio0:
						tipoEstacion = "base";
						refrescarArrayEstaciones();
						refrescarSpinner(estaciones, spnListaEstaciones, estacionSeleccionada);
					break;
					
					case R.id.dialog_radio1:
						tipoEstacion = "referencia";
						refrescarArrayReferencias();
						refrescarSpinner(referencias, spnListaEstaciones, estacionSeleccionada);
					break;
				}
			}
		});
    	
    	btnAceptar.setOnClickListener(new View.OnClickListener()
    	{
			@Override
			public void onClick(View arg0) 
			{
				String estacionVisada = spnListaEstaciones.getSelectedItem().toString().replace(" ", "_");
				String ahdRecogido = txtAnguloHorizontalD.getText().toString();
				String avdRecogido = txtAnguloVerticalD.getText().toString();
				String dgdRecogido = txtDistanciaD.getText().toString();
				String ahiRecogido = txtAnguloHorizontalI.getText().toString();
				String aviRecogido = txtAnguloVerticalI.getText().toString();
				String dgiRecogido = txtDistanciaI.getText().toString();
				String alturaEstacion = txtAlturaEstacion.getText().toString();
				String alturaMira = txtAlturaMira.getText().toString();
				
				if(ahdRecogido.contentEquals("") || avdRecogido.contentEquals("") || dgdRecogido.contentEquals("")
						|| ahiRecogido.contentEquals("") || aviRecogido.contentEquals("") || dgiRecogido.contentEquals("")
						|| alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
				{
					if(alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
					{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_faltan_alturas), Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_campos_vacios), Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					String horizontalMedio = calcularAnguloHorizontal(ahdRecogido, ahiRecogido);
					String verticalMedio = calcularAnguloVertical(avdRecogido, aviRecogido);
					String distancia = calcularDistanciaMedia(dgdRecogido, dgiRecogido);
					double i = Double.parseDouble(alturaEstacion);
					double m = Double.parseDouble(alturaMira);
					
					anadirRegistroObservacion(trabajoSeleccionado, estacionSeleccionada, estacionVisada.replace(" ", "_"), 
												horizontalMedio, verticalMedio, distancia, tresDigitos.format(m).replace(",", "."), tresDigitos.format(i).replace(",", "."), tipoEstacion);
					
					refrescarArrayObservaciones(estacionSeleccionada);
					refrescarListadoPuntos(observaciones, lstObservaciones);
					
					dismissDialog(DIALOGO_OBSERVAR_ESTACION);
				}
			}
		});
    	
    	btnCancelar.setOnClickListener(new View.OnClickListener()
    	{	
			@Override
			public void onClick(View v) 
			{
				dismissDialog(DIALOGO_OBSERVAR_ESTACION);
			}
		});

    	
    	return builder.create();
    }
    
    private Dialog crearDialogoObservaciones()
    {
    	String titulo = getResources().getString(R.string.observar_punto);
    	String mensaje = getResources().getString(R.string.msg_observar_punto);
    	final String literalPunto = getResources().getString(R.string.punto);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	LayoutInflater inflater = this.getLayoutInflater();
    	View layout = inflater.inflate(R.layout.dialog_observaciones, null);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	builder.setView(layout);
    	
    	toggleButton1 = (ToggleButton) layout.findViewById(R.id.toggleButton1);
    	layoutCirculoInverso = (LinearLayout) layout.findViewById(R.id.layout_circulo_inverso);
    	viewInsertar = inflater.inflate(R.layout.observaciones_circulo_inverso, null);
    	txtEstacionActual = (TextView) layout.findViewById(R.id.textView1);
    	final TextView lblNombrePunto = (TextView) layout.findViewById(R.id.textView6);
    	final EditText txtNombrePunto = (EditText) layout.findViewById(R.id.editText4);
    	final EditText txtObsDAnguloHorizontal = (EditText) layout.findViewById(R.id.editText1);
    	final EditText txtObsDAnguloVertical = (EditText) layout.findViewById(R.id.editText2);
    	final EditText txtObsDDistancia = (EditText) layout.findViewById(R.id.editText3);

		final Button btnAceptar = (Button) layout.findViewById(R.id.button2);
		final Button btnCancelar = (Button) layout.findViewById(R.id.button1);
    	
    	lblNombrePunto.setOnClickListener(new View.OnClickListener()
    	{
			@Override
			public void onClick(View arg0) 
			{
				txtNombrePunto.setText(literalPunto + (lstObservaciones.getCount() + 1));
			}
		});
		
		btnAceptar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				String nombrePunto = txtNombrePunto.getText().toString();
				String DanguloHorizontal = txtObsDAnguloHorizontal.getText().toString();
				String DanguloVertical = txtObsDAnguloVertical.getText().toString();
				String Ddistancia = txtObsDDistancia.getText().toString();

				String alturaEstacion = txtAlturaEstacion.getText().toString();
				String alturaMira = txtAlturaMira.getText().toString();
				
				if(toogleButtonObservaciones)
				{
					String IanguloHorizontal = txtObsIAnguloHorizontal.getText().toString();
					String IanguloVertical = txtObsIAnguloVertical.getText().toString();
					String Idistancia = txtObsIAnguloDistancia.getText().toString();
					
					if(nombrePunto.contentEquals("") || 
						DanguloHorizontal.contentEquals("") || DanguloVertical.contentEquals("") || Ddistancia.contentEquals("") ||
						IanguloHorizontal.contentEquals("") || IanguloVertical.contentEquals("") || Idistancia.contentEquals("") ||
						alturaEstacion.contentEquals("") ||alturaMira.contentEquals(""))
					{
						if(alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_faltan_alturas), Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_campos_vacios), Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						boolean nombreExistente = validarRegistroPuntos(nombrePunto);
						
						if(nombreExistente)
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.registro_existe), Toast.LENGTH_SHORT).show();
						}
						else
						{
							String horizontalMedio = calcularAnguloHorizontal(DanguloHorizontal, IanguloHorizontal);
							String verticalMedio = calcularAnguloVertical(DanguloVertical, IanguloVertical);
							String distancia = calcularDistanciaMedia(Ddistancia, Idistancia);
							double i = Double.parseDouble(alturaEstacion);
							double m = Double.parseDouble(alturaMira);
							
							anadirRegistroObservacion(trabajoSeleccionado, estacionSeleccionada, nombrePunto.replace(" ", "_"), 
														horizontalMedio, verticalMedio, distancia, tresDigitos.format(m).replace(",", "."), tresDigitos.format(i).replace(",", "."), "punto");
							
							refrescarArrayObservaciones(estacionSeleccionada);
							refrescarListadoPuntos(observaciones, lstObservaciones);
							dismissDialog(DIALOGO_OBSERVAR_PUNTO);
						}
					}
				}
				else
				{
					if(nombrePunto.contentEquals("") || 
					DanguloHorizontal.contentEquals("") || DanguloVertical.contentEquals("") || Ddistancia.contentEquals("") ||
					alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
					{
						if(alturaEstacion.contentEquals("") || alturaMira.contentEquals(""))
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_faltan_alturas), Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_campos_vacios), Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						boolean nombreExistente = validarRegistroPuntos(nombrePunto);
						
						if(nombreExistente)
						{
							Toast.makeText(getApplicationContext(), getResources().getString(R.string.registro_existe), Toast.LENGTH_SHORT).show();
						}
						else
						{
							//Igualo formatos
							double aHorizontal = Double.parseDouble(DanguloHorizontal);
							double aVertical = Double.parseDouble(DanguloVertical);
							double distancia = Double.parseDouble(Ddistancia);
							double i = Double.parseDouble(alturaEstacion);
							double m = Double.parseDouble(alturaMira);
							
							String ah= cuatroDigitos.format(aHorizontal).replace(",", ".");
							String av = cuatroDigitos.format(aVertical).replace(",", ".");
							String d = tresDigitos.format(distancia).replace(",", ".");
							
							anadirRegistroObservacion(trabajoSeleccionado, estacionSeleccionada, nombrePunto.replace(" ", "_"), 
														ah, av, d, tresDigitos.format(m).replace(",", "."), tresDigitos.format(i).replace(",", "."), "punto");
		
							refrescarArrayObservaciones(estacionSeleccionada);
							refrescarListadoPuntos(observaciones, lstObservaciones);
							dismissDialog(DIALOGO_OBSERVAR_PUNTO);
						}
					}
				}
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				dismissDialog(DIALOGO_OBSERVAR_PUNTO);
			}
		});

    	
    	return builder.create();
    }
    
}
