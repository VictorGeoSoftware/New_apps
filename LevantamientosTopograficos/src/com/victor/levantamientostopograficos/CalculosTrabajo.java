package com.victor.levantamientostopograficos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CalculosTrabajo extends Activity 
{
	//----- Declaración de elementos
	ListView lstEstaciones;
	ListView lstBasesPoligonal;
	Button btnCalcularPoligonal;
	TextView lblTrabajoSeleccionado;
	EditText txtEsfericidadRefraccion;
	ArrayAdapter<String> adaptador;
	
	
	//----- Declaracion de variables
	private static final int DIALOGO_ACTUALIZAR_ESTACION = 100;
	private static final int DIALOGO_COMPRAR_BASEDATOS = 101;
	private static final int DIALOGO_FICHERO_OBSERVACIONES = 8;
	private static final int DIALOGO_IMPRIMIR_INFORME = 9;
	private static final int DIALOGO_FICHERO_POLIGONAL = 10;
	
	String trabajoSeleccionado = "";
	String estacionSeleccionada = "";
	String coordConocidasPrimeraBase = "";
	String coordConocidasUltimaBase = "";
	String coordenadasInforme = "";
	ArrayList<String> estaciones = new ArrayList<String>();
	ArrayList<String> observaciones = new ArrayList<String>();
	ArrayList<String> listaBasesCompleta = new ArrayList<String>();
	ArrayList<String> desorientacionEstacion = new ArrayList<String>();
	ArrayList<String> acimutEstacionVisada = new ArrayList<String>();
	
	ArrayList<Double> desorientacionesCompensadas = new ArrayList<Double>();
	ArrayList<String> coordenadasCompensadas = new ArrayList<String>();
	ArrayList<Double> cotasCompensadas = new ArrayList<Double>();
	
	BeanCalculosEstaciones[] filaEstaciones;
	BeanCalculosEstaciones[] filaBases;
	
	
	//----- Declaracion de bases de datos
	SQLiteDatabase dbObservaciones;
	ContentValues valuesObservaciones = new ContentValues();
	SQLiteDatabase dbEstaciones;
	ContentValues valuesEstaciones = new ContentValues();
	
	
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculos_trabajo);
		
		
		//----- Inicialización de elementos
		lstEstaciones = (ListView)findViewById(R.id.listView1);
		lstBasesPoligonal = (ListView)findViewById(R.id.listView2);
		btnCalcularPoligonal = (Button)findViewById(R.id.button1);
		lblTrabajoSeleccionado = (TextView)findViewById(R.id.textView4);
		txtEsfericidadRefraccion = (EditText)findViewById(R.id.editText1);
		txtEsfericidadRefraccion.setText("0.999654");
		
		
		//----- Inicializacion de base de datos
		BaseDatosObservaciones bdObservaciones =  new BaseDatosObservaciones(this, "Observaciones", null, 1);
		dbObservaciones = bdObservaciones.getWritableDatabase();
		BaseDatosEstaciones bdEstaciones = new BaseDatosEstaciones(this, "Estaciones", null, 1);
		dbEstaciones = bdEstaciones.getWritableDatabase();
		
		
		//----- Inicializacion de variables
		trabajoSeleccionado = VariablesPaso.getTrabajoSeleccionado();
		lblTrabajoSeleccionado.setText(trabajoSeleccionado);
		
		
		//----- Inicialización de listados
		refrescarArrayEstaciones();
		refrescarListadoEstaciones(estaciones, lstEstaciones);
		
		
		//----- Programación de eventos
		lstEstaciones.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) 
			{
				estacionSeleccionada = filaEstaciones[position].getNombreEstacion().replace(" ", "_");
				showDialog(DIALOGO_ACTUALIZAR_ESTACION);
				return false;
			}
		});
		
		btnCalcularPoligonal.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(lstEstaciones.getCount() > 0) // Pongo esto para evitar que pete si a algún usuario se le ocurre calcular sin haber grabado registros 
				{
					coordenadasInforme = "";
					calculoPoligonal();
					calculoRadiacion();
				}
			}
		});
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId())
		{				
			case R.id.imprimir_registros:
				showDialog(DIALOGO_IMPRIMIR_INFORME);
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	

	public void calculoRadiacion()
	{
		ArrayList<String> basesCoordenadas = new ArrayList<String>();
		ArrayList<String> puntosCoordenadas = new ArrayList<String>();
		DecimalFormat metros = new DecimalFormat("0.000");
		
		for(int i = 0; i < listaBasesCompleta.size(); i++)
		{
			String[] valoresEstacion = listaBasesCompleta.get(i).split(" ");
			String estacionActual = valoresEstacion[0];
			String[] coordenadasEstacion = coordenadasCompensadas.get(i).split(" ");
			double xEstacion = Double.parseDouble(coordenadasEstacion[0]);
			double yEstacion = Double.parseDouble(coordenadasEstacion[1]);
			double zEstacion = cotasCompensadas.get(i);
			double desorientacionEstacion = desorientacionesCompensadas.get(i);
			
			basesCoordenadas.add(estacionActual + " " + xEstacion + " " + yEstacion + " " + zEstacion);
			coordenadasInforme =  coordenadasInforme + estacionActual + " " + metros.format(xEstacion) + " " + metros.format(yEstacion) + " " + metros.format(zEstacion) + "\n";
			
			puntosCoordenadas = getPuntosRadiados(estacionActual, xEstacion, yEstacion, zEstacion, desorientacionEstacion);
			for(int j = 0; j < puntosCoordenadas.size(); j++)
			{
				String[] valoresPunto = puntosCoordenadas.get(j).split(" ");
				String nombrePunto = valoresPunto[0];
				double xPunto = Double.parseDouble(valoresPunto[1]);
				double yPunto = Double.parseDouble(valoresPunto[2]);
				double zPunto = Double.parseDouble(valoresPunto[3]);
				
				coordenadasInforme = coordenadasInforme + nombrePunto + " " + metros.format(xPunto) + " " + metros.format(yPunto)+ " "
									+ metros.format(zPunto) + "\n";
			}
			
			coordenadasInforme = coordenadasInforme + "\n";
		}
		
		refrescarListadoBases(basesCoordenadas, lstBasesPoligonal);
	}
	
	public ArrayList<String> getPuntosRadiados(String estacionActual, double xEstacion, double yEstacion, double zEstacion, double desorientacionEstacion)
	{
		ArrayList<String> puntosRadiados = new ArrayList<String>();
		
		String[] camposObservaciones = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
		String[] argsObservaciones = new String[] {trabajoSeleccionado, estacionActual, "punto"};
		Cursor c = dbObservaciones.query("Observaciones", camposObservaciones, "nombreTrabajo=? AND nombreEstacion=? AND tipo=?", argsObservaciones, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String nombrePunto = c.getString(2);
					double ah = Double.parseDouble(c.getString(3));
					double av = Double.parseDouble(c.getString(4))*Math.PI/200;
					double dg = Double.parseDouble(c.getString(5));
					double m = Double.parseDouble(c.getString(6));
					double instrumento = Double.parseDouble(c.getString(7));
	
					double acimutPunto = desorientacionEstacion + ah;
					
					if(acimutPunto >= 400)
					{
						acimutPunto = acimutPunto - 400;
					}
					
					double dHorizontal = dg*Math.sin(av);
					double xPunto = xEstacion + dHorizontal*Math.sin(acimutPunto);
					double yPunto = yEstacion + dHorizontal*Math.cos(acimutPunto);
					double zPunto = zEstacion + dg*Math.cos(av) + instrumento - m;
					
					puntosRadiados.add(nombrePunto + " " + xPunto + " " + yPunto + " " + zPunto);
				}
				while(c.moveToNext());
			}
		}
		
		return puntosRadiados;
	}
	
	public void refrescarListadoBases(ArrayList<String> lista, ListView listView)
	{
		filaBases = new BeanCalculosEstaciones[lista.size()];
		
		for(int j = 0; j < lista.size(); j++)
		{
			String[] valoresEstacion = lista.get(j).split(" ");
			String nombreEstacion = valoresEstacion[0];
			String coordenadasEstacion = valoresEstacion[1] + " " + valoresEstacion[2] + " " +  valoresEstacion[3];
			Log.i("refrescar listados", "valores: " + nombreEstacion + " " + coordenadasEstacion);
			filaBases[j] = new BeanCalculosEstaciones(nombreEstacion, coordenadasEstacion);
		}
		
		AdaptadorListadoBases adaptador = new AdaptadorListadoBases(this);
		listView.setAdapter(adaptador);
	}
	
	public boolean comprobarObservaciones()
	{
		boolean todoOk = false;
		
		//----- Comprorar observaciones desde primera y última estación a referencias
		
		
		//----- Comprobar observaciones recíprocas entre estaciones
		
		return todoOk;		
	}
	
	
	public void calculoPoligonal()
	{
		ArrayList<Double> acimutesCompensados = new ArrayList<Double>();
		ArrayList<Double> distanciasCompensadas = new ArrayList<Double>();
		
		double desorientacionInicial = desorientacionMedia();
		Log.i("calculo poligonal", "desorientacion inicial: " + desorientacionInicial);
		
		//Corrida de acimutes: relleno de arrays de desorientaciones y acimutes a estaciones visadas, y cierre angular.
		double cierreAngular = corridaAcimutes(desorientacionInicial);
		
		//Compensación angular: se calcula la compensación, y se aplica a desorientaciones y azimutes
			//Desorientaciones
		double compensacionAngular = cierreAngular/listaBasesCompleta.size();
		for(int i = 0; i < desorientacionEstacion.size(); i++)
		{
			String desorientacionOriginal = desorientacionEstacion.get(i);
			double desOriginal = Double.parseDouble(desorientacionOriginal);
			double desCompensada = desOriginal - (compensacionAngular*(i+1));
			desorientacionesCompensadas.add(desCompensada);
			Log.i("calculo poligonal", "desorientacion compensada: " + desCompensada);
		}
		
			//Acimutes
		for(int j = 0; j < acimutEstacionVisada.size(); j++)
		{
			String anguloOriginal = acimutEstacionVisada.get(j);
			double angOriginal = Double.parseDouble(anguloOriginal);
			double angCompensado = 0;
			Log.i("calculo poligonal", "acimut original: " + angOriginal);
			if(j > acimutEstacionVisada.size()-3)
			{
				angCompensado = angOriginal - (compensacionAngular*(desorientacionEstacion.size()));
			}
			else
			{
				angCompensado = angOriginal - (compensacionAngular*(j+1));
			}
			
			acimutesCompensados.add(angCompensado);
			Log.i("calculo poligonal", "acimut compensado: " + angCompensado);
		}
		
		// Altimetría: se hace para sacar luego distancias por reduccion a la cuerda
		cotasCompensadas = calcularAltimetria();
		
		// Planimetría de poligonal: ya con altimetria compensada
			//- Compensación planimétrica en función de las distancias
		distanciasCompensadas = distanciasUtmPromediadas(cotasCompensadas);
		String valoresPrimeraBase[] = coordConocidasPrimeraBase.split(" ");
		String coordenadasPrimeraBase = valoresPrimeraBase[2] + " " + valoresPrimeraBase[3];
		coordenadasCompensadas = calcularPlanimetriaCompensada(coordenadasPrimeraBase, distanciasCompensadas, acimutesCompensados);
		Log.i("calculo poligonal", "coordenadas compensadas: " + coordenadasCompensadas);
	}
	
	public ArrayList<String> calcularPlanimetriaCompensada(String coordenadasPrimeraBase, ArrayList<Double> distanciasCompensadas, ArrayList<Double> acimutesCompensados)
	{
		ArrayList<String> planimetriaCalculada =  new ArrayList<String>();
		ArrayList<String> planimetriaCompensada =  new ArrayList<String>();
		
		planimetriaCompensada.add(coordenadasPrimeraBase);
		String[] valoresPrimeraBase = coordenadasPrimeraBase.split(" ");
		double xPrimeraBase = Double.parseDouble(valoresPrimeraBase[0]);
		double yPrimeraBase = Double.parseDouble(valoresPrimeraBase[1]);
		double xBase = 0;
		double yBase = 0;
		
		for(int i = 0; i < distanciasCompensadas.size(); i++)
		{
			double acimut = acimutesCompensados.get(i)*Math.PI/200;
					
			if(i == 0)
			{
				
				xBase = xPrimeraBase + xBase + distanciasCompensadas.get(i)*Math.sin(acimut);
				yBase = yPrimeraBase + yBase + distanciasCompensadas.get(i)*Math.cos(acimut);
			}
			else
			{
				xBase = xBase + distanciasCompensadas.get(i)*Math.sin(acimut);
				yBase = yBase + distanciasCompensadas.get(i)*Math.cos(acimut);
			}

			planimetriaCalculada.add(xBase + " " + yBase);
		}
		
		String[] ultimaCoordenadaObservada = planimetriaCalculada.get(planimetriaCalculada.size()-1).split(" ");
		double xUltimaObservada = Double.parseDouble(ultimaCoordenadaObservada[0]);
		double yUltimaObservada = Double.parseDouble(ultimaCoordenadaObservada[1]);
		String[] valoresUltimaCoordenada = coordConocidasUltimaBase.split(" ");
		double xUltimaCoordenada = Double.parseDouble(valoresUltimaCoordenada[2]);
		double yUltimaCoordenada = Double.parseDouble(valoresUltimaCoordenada[3]);
		double errorCierreX = xUltimaObservada - xUltimaCoordenada;
		double errorCierreY = yUltimaObservada - yUltimaCoordenada;
		
		double compensacionAcumuladaX = 0;
		double compensacionAcumuladaY = 0;
		double sumatorioDistancias = 0;
		for(int i = 0; i < distanciasCompensadas.size(); i++)
		{
			sumatorioDistancias = sumatorioDistancias + distanciasCompensadas.get(i);
		}
		
		for(int j = 0; j < planimetriaCalculada.size(); j++)
		{
			String[] coordenadas = planimetriaCalculada.get(j).split(" ");
			double xCalculada = Double.parseDouble(coordenadas[0]);
			double yCalculada = Double.parseDouble(coordenadas[1]);
			
			double compensacionX = -errorCierreX*distanciasCompensadas.get(j)/sumatorioDistancias;
			double compensacionY = -errorCierreY*distanciasCompensadas.get(j)/sumatorioDistancias;
			compensacionAcumuladaX = compensacionAcumuladaX + compensacionX;
			compensacionAcumuladaY = compensacionAcumuladaY + compensacionY;
			
			double xCompensada = xCalculada + compensacionAcumuladaX;
			double yCompensada = yCalculada + compensacionAcumuladaY;
			
			planimetriaCompensada.add(xCompensada + " " + yCompensada);
		}
		
		return planimetriaCompensada;
	}
	
	
	public ArrayList<Double> distanciasUtmPromediadas(ArrayList<Double> cotasCompensadas)
	{
		ArrayList<Double> distanciasPromediadas = new ArrayList<Double>();
		double k = Double.parseDouble(txtEsfericidadRefraccion.getText().toString());
		
		for(int i = 0; i < (listaBasesCompleta.size()-1); i++)
		{			
			String[] valoresEstacion = listaBasesCompleta.get(i).split(" ");
			String estacionActual = valoresEstacion[0];
			String estacionSiguiente = getSiguienteEstacion(i+1);
			double cotaEstacionActual = cotasCompensadas.get(i);
			double cotaEstacionSiguiente = cotasCompensadas.get(i+1);
			
			// Recuperar datos observaciones estacion actual
			String[] observacionDirecta = getObservaciones(estacionActual, estacionSiguiente).split(" ");
			double verticalDirecto = Double.parseDouble(observacionDirecta[1]);
			double distanciaDirecto = Double.parseDouble(observacionDirecta[2]);
				//Distancia UTM
			double varHdirecto = getIncrementoH(distanciaDirecto, verticalDirecto);
			double dCdirecto = getDistanciaCuerda(distanciaDirecto, varHdirecto, cotaEstacionActual, cotaEstacionSiguiente);
			double dUtmDirecta = getDistanciaUtm(dCdirecto, k);
			
			
			// Recuperar datos observaciones estacion siguiente
			String[] observacionReciproca = getObservaciones(estacionSiguiente, estacionActual).split(" ");
			double verticalReciproca = Double.parseDouble(observacionReciproca[1]);
			double distanciaReciproca = Double.parseDouble(observacionReciproca[2]);
				//Distancia UTM
			double varHreciproca = getIncrementoH(distanciaReciproca, verticalReciproca);
			double dCreciproca = getDistanciaCuerda(distanciaReciproca, varHreciproca, cotaEstacionSiguiente, cotaEstacionActual);
			double dUtmReciproca = getDistanciaUtm(dCreciproca, k);
		
			double distanciaPromediada = (dUtmDirecta + dUtmReciproca)/2;
			Log.i("distanciasUtmPrimediadas", "directa - reciproca - media: " + dUtmDirecta + " " + dUtmReciproca + " " + distanciaPromediada);
			distanciasPromediadas.add(distanciaPromediada);
		}
		
		return distanciasPromediadas;
	}
	
	public double getIncrementoH(double dGeometrica, double vertical)
	{
		double incremento = dGeometrica*Math.cos(vertical*(Math.PI/200));
		return incremento;
	}
	
	public double getDistanciaCuerda(double dGeometrica, double incrH, double cotaA, double cotaB)
	{
		double rT = 6378388;
		double distCuerda = Math.sqrt((Math.pow(dGeometrica, 2) - Math.pow(incrH, 2))/(1 + (cotaA/rT))*(1 + cotaB/rT));
		
		return distCuerda;
	}
	
	public double getDistanciaUtm(double distanciaCuerda, double k)
	{
		double distanciaUtm = distanciaCuerda*k;
		return distanciaUtm;
	}
	
	public ArrayList<Double> calcularAltimetria()
	{
		ArrayList<Double> cotasCalculadas = new ArrayList<Double>();
		ArrayList<Double> distanciaTramos = new ArrayList<Double>();
		ArrayList<Double> cotasCompensadas = new ArrayList<Double>();
		String[] valoresPrimeraBase = coordConocidasPrimeraBase.split(" ");
		double cotaPrimeraBase = Double.parseDouble(valoresPrimeraBase[4]);
		String[] valoresUltimaBase = coordConocidasUltimaBase.split(" ");
		double cotaUltimaBase = Double.parseDouble(valoresUltimaBase[4]);
		double distanciaAcumulada = 0;
		double desnivelAcumulado = 0;
		
		for(int i = 0; i < (listaBasesCompleta.size()-1); i++)
		{			
			String[] valoresEstacion = listaBasesCompleta.get(i).split(" ");
			String estacionActual = valoresEstacion[0];
		
			// Recuperar datos observaciones estacion actual -> estacion siguiente
			String estacionSiguiente = getSiguienteEstacion(i+1);
			String[] observacionDirecta = getObservaciones(estacionActual, estacionSiguiente).split(" ");
			String verticalDirecto = observacionDirecta[1];
			String distanciaDirecto = observacionDirecta[2];
			String miraDirecto = observacionDirecta[3];
			String intrumentoDirecto = observacionDirecta[4];
			
			double desnivelDirecto = calcularDesnivel(distanciaDirecto, verticalDirecto, miraDirecto, intrumentoDirecto);
			
			// Recuperar datos observaciones estacion siguiente -> estacion actual
			String[] observacionReciproca = getObservaciones(estacionSiguiente, estacionActual).split(" ");
			String verticalReciproca = observacionReciproca[1];
			String distanciaReciproca = observacionReciproca[2];
			String miraReciproca = observacionReciproca[3];
			String intrumentoReciproca = observacionReciproca[4];
			
			double desnivelReciproco = calcularDesnivel(distanciaReciproca, verticalReciproca, miraReciproca, intrumentoReciproca);
			
			// Calcular desnivel medio
			double desnivelMedio = (desnivelDirecto - desnivelReciproco)/2;
			desnivelAcumulado = desnivelAcumulado + desnivelMedio;
			double cotaEstacion = cotaPrimeraBase + desnivelAcumulado;
			cotasCalculadas.add(cotaEstacion);
			
			// Calculo de distancia media para ponderacion
			double distanciaMedia = (Double.parseDouble(distanciaDirecto) + Double.parseDouble(distanciaReciproca))/2;
			distanciaAcumulada = distanciaAcumulada + distanciaMedia;
			distanciaTramos.add(distanciaMedia);
		}
		
		double cotaObservadaUltimaBase = cotaPrimeraBase + desnivelAcumulado;
		double cierreAltimetrico = cotaObservadaUltimaBase - cotaUltimaBase;

		cotasCompensadas.add(cotaPrimeraBase); //-> así devuelvo todas las cotas ordenadas
		double compensacionAcumulada = 0;
		
		for(int j = 0; j < (listaBasesCompleta.size()-1); j++)
		{
			double compensacionTramo = -(cierreAltimetrico/distanciaAcumulada)*distanciaTramos.get(j);
			compensacionAcumulada = compensacionAcumulada + compensacionTramo;
			cotasCompensadas.add(cotasCalculadas.get(j) + compensacionAcumulada);
		}
		
		Log.i("calcular altimetria", "cotas compensadas: " + cotasCompensadas);
		return cotasCompensadas;
	}
	
	public String getObservaciones(String estacion, String visado)
	{
		String observaciones = "";
		
		BaseDatosObservaciones bdObservaciones =  new BaseDatosObservaciones(this, "Observaciones", null, 1);
		SQLiteDatabase dbObservaciones = bdObservaciones.getWritableDatabase();
		String[] columnasObservaciones = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
		String[] argumentosBusqueda = new String[]{trabajoSeleccionado, estacion, visado};
		Cursor c =  dbObservaciones.query("Observaciones", columnasObservaciones, "nombreTrabajo=? AND nombreEstacion=? AND nombreVisado=?", argumentosBusqueda, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String horizontal = c.getString(3);
					String vertical = c.getString(4);
					String geometrica = c.getString(5);
					String mira = c.getString(6);
					String instrumento = c.getString(7);
					
					observaciones = horizontal + " " + vertical + " " + geometrica + " " + mira + " " + instrumento;
				}
				while(c.moveToNext());
			}
		}
		
		Log.i("getObservaciones", "observaciones: " + observaciones);
		return observaciones;
	}
	
	public double calcularDesnivel(String distancia, String vertical, String mira, String instrumento)
	{
		double dis = Double.parseDouble(distancia);
		double verticalRadianes = Double.parseDouble(vertical)*Math.PI/200;
		double m = Double.parseDouble(mira);
		double i = Double.parseDouble(instrumento);
		
		double desnivel = dis*Math.cos(verticalRadianes) + i - m;
		Log.i("calcularDesnivel", "desnivel: " + desnivel);
		return desnivel;
	}
	
	public double corridaAcimutes(double desorientacionInicial) //Se rellenan los arrays de desorientaciones y acimut a estacion visada. 
	//Se devuelve el error de cierre angular
	{
		double cierreAngular = 0;
		double acimutEstacionAnterior = 0;
		double acimutEstacionSiguiente = 0;
		Formulas formulas = new Formulas();
		
		for(int i = 0; i < listaBasesCompleta.size(); i++)
		{			
			String[] valoresEstacion = listaBasesCompleta.get(i).split(" ");
			String estacionActual = valoresEstacion[0];
			
			Log.i("bucle", "estacion actual: " + estacionActual);
			
			if(i == 0)
			{
				Log.i("calculo poligonal", "entra en primera base");
				desorientacionEstacion.add(Double.toString(desorientacionInicial));
				String siguienteEstacion = getSiguienteEstacion(i+1);
				Log.i("calculo poligonal", "estacion actual/siguiente: " + estacionActual + "/" + siguienteEstacion);
				
				double horizontalSiguiente = getHorizontalSiguienteEstacion(estacionActual, siguienteEstacion);
				acimutEstacionSiguiente = desorientacionInicial + horizontalSiguiente;
				
				if(acimutEstacionSiguiente >= 400)
				{
					acimutEstacionSiguiente = acimutEstacionSiguiente - 400;
				}
				
				acimutEstacionVisada.add(Double.toString(acimutEstacionSiguiente));
				Log.i("calculo poligonal", "primer acimut: " + acimutEstacionSiguiente);
			}
			else
			{
				if(i == (listaBasesCompleta.size()-1)) 	// - Desorientacion y acimut en ultima estacion
				{
					Log.i("calculo poligonal", "entra en ultima base");
					acimutEstacionAnterior = acimutEstacionSiguiente + 200;
					
					if(acimutEstacionAnterior > 400)
					{
						acimutEstacionAnterior = acimutEstacionAnterior - 400;
					}
					
					//Lectura estacion anterior
					String anteriorEstacion = getSiguienteEstacion(i-1); // --> El -1 para que pille la anterior, dado que doy una posicion
					Log.i("calculo poligonal", "estacion actual/anterior: " + estacionActual + "/" + anteriorEstacion);

					//Desorientacion
					double horizontalAnterior = getHorizontalSiguienteEstacion(estacionActual, anteriorEstacion);
					double desorientacion = acimutEstacionAnterior - horizontalAnterior;
					
					if(desorientacion > 400)
					{
						desorientacion = desorientacion - 400;
					}
					
					if(desorientacion < 0)
					{
						desorientacion = desorientacion + 400;
					}
					
					desorientacionEstacion.add(Double.toString(desorientacion));
					Log.i("calculo poligonal", "desorientacion: " + desorientacion);
					
					//----- ERROR DE CIERRE ----------------
					double xEstacion = Double.parseDouble(valoresEstacion[2]);
					double yEstacion = Double.parseDouble(valoresEstacion[3]);
					ArrayList<String> referenciasObservadas = getReferenciasObservadas(estacionActual);
					double diferenciasAcimutes = 0;
					for(int j = 0; j < referenciasObservadas.size(); j++)
					{
						String[] valoresReferencia = referenciasObservadas.get(j).split(" ");
						String nombreReferencia = valoresReferencia[0];
						
						//Acimutes observados
						double horizontalReferencia = getHorizontalSiguienteEstacion(estacionActual, nombreReferencia);
						double acimutObservado = desorientacion + horizontalReferencia;
						if(acimutObservado >= 400)
						{
							acimutObservado = acimutObservado - 400;
						}
						acimutEstacionVisada.add(Double.toString(acimutObservado));
						
						//Acimutes calculados
						String[] coordenadasReferencia = getCoordenadasReferencia(nombreReferencia).split(" ");
						double xReferencia = Double.parseDouble(coordenadasReferencia[0]);
						double yReferencia = Double.parseDouble(coordenadasReferencia[1]);
						double acimutCalculado = formulas.acimut(xEstacion, yEstacion, xReferencia, yReferencia, 200);
						
						//Cierre
						double diferenciaAcimutes = acimutObservado - acimutCalculado;
						diferenciasAcimutes = diferenciasAcimutes + diferenciaAcimutes;
						Log.i("corrida acimutes", "observado / calculado: " + acimutObservado + " " + acimutCalculado);
						Log.i("corrida acimutes", "errores cierre: " + diferenciaAcimutes);
					}
					
					cierreAngular = diferenciasAcimutes/referenciasObservadas.size();
				}
				else
				{
					Log.i("calculo poligonal", "entra en base intermedia");
					acimutEstacionAnterior = acimutEstacionSiguiente + 200;
					
					if(acimutEstacionAnterior > 400)
					{
						acimutEstacionAnterior = acimutEstacionAnterior - 400;
					}
					Log.i("calculo poligonal", "acimut a base anterior: " + acimutEstacionAnterior);
					
					//Lectura estacion anterior
					Log.i("calculo poligonal", "estacion actual: " + estacionActual + " " + i);
					String anteriorEstacion = getSiguienteEstacion(i-1); // --> El -1 para que pille la anterior, dado que doy una posicion
					Log.i("calculo poligonal", "estacion anterior: " + anteriorEstacion);

					//Desorientacion
					double horizontalAnterior = getHorizontalSiguienteEstacion(estacionActual, anteriorEstacion);
					double desorientacion = acimutEstacionAnterior - horizontalAnterior;
					
					if(desorientacion > 400)
					{
						desorientacion = desorientacion - 400;
					}
					
					if(desorientacion < 0)
					{
						desorientacion = desorientacion + 400;
					}
					
					desorientacionEstacion.add(Double.toString(desorientacion));
					Log.i("calculo poligonal", "desorientacion: " + desorientacion);
					
					//Acimut estacion siguiente
					String siguienteEstacion = getSiguienteEstacion(i+1);
					double horizontalSiguiente = getHorizontalSiguienteEstacion(estacionActual, siguienteEstacion);
					acimutEstacionSiguiente = desorientacion + horizontalSiguiente;
					if(acimutEstacionSiguiente >= 400)
					{
						acimutEstacionSiguiente = acimutEstacionSiguiente - 400;
					}
					acimutEstacionVisada.add(Double.toString(acimutEstacionSiguiente));
				}
			}
		}
		
		return cierreAngular;
	}
	
	public double getHorizontalSiguienteEstacion(String estacionActual, String siguienteEstacion)
	{
		double lecturaHorizontal = 0;
		
		BaseDatosObservaciones bdObservaciones =  new BaseDatosObservaciones(this, "Observaciones", null, 1);
		SQLiteDatabase dbObservaciones = bdObservaciones.getWritableDatabase();
		String[] columnasObservaciones = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
		String[] argumentosBusqueda = new String[]{trabajoSeleccionado, estacionActual, siguienteEstacion};
		Cursor c =  dbObservaciones.query("Observaciones", columnasObservaciones, "nombreTrabajo=? AND nombreEstacion=? AND nombreVisado=?", argumentosBusqueda, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					lecturaHorizontal = Double.parseDouble(c.getString(3));
				}
				while(c.moveToNext());
			}
		}
		Log.i("horizontal siguiente estacion", "horizontal: " + lecturaHorizontal);
		return lecturaHorizontal;
	}
	
	public String getSiguienteEstacion(int posicion)
	{
		String[] valoresEstacion = listaBasesCompleta.get(posicion).split(" ");
		String nombreEstacion = valoresEstacion[0];
		Log.i("nombre siguiente estacion", "nombre: " + nombreEstacion);
		return nombreEstacion;
	}
	
	public ArrayList<String> getReferenciasObservadas(String nombreEstacion)
	{
		ArrayList<String> referenciasObservadas= new ArrayList<String>();
		
		String[] camposObservaciones = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
		String[] argsObservaciones = new String[] {trabajoSeleccionado, nombreEstacion, "referencia"};
		Cursor c = dbObservaciones.query("Observaciones", camposObservaciones, "nombreTrabajo=? AND nombreEstacion=? AND tipo=?", argsObservaciones, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String nombreReferencia = c.getString(2);
					String ah = c.getString(3);
					String av = c.getString(4);
					String dg = c.getString(5);
					String m = c.getString(6);
					String i = c.getString(7);
	
					referenciasObservadas.add(nombreReferencia + " " + ah + " " + av + " " + dg + " " + m + " " + i);
				}
				while(c.moveToNext());
			}
		}
		
		return referenciasObservadas;
	}
	
	public String getCoordenadasReferencia(String nombreEstacion)
	{
		String coordenadasReferencia = "";
		String[] camposEstaciones = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
		String[] argsEstaciones = new String[] {trabajoSeleccionado, nombreEstacion};
		
		Cursor cE = dbEstaciones.query("Estaciones", camposEstaciones, "nombreTrabajo=? AND nombreEstacion=?", argsEstaciones, null, null, null);
		
		if(cE.getCount() > 0)
		{
			if(cE.moveToFirst())
			{
				do
				{
					String x = cE.getString(3);
					String y = cE.getString(4);
					String z = cE.getString(5);

					coordenadasReferencia = x + " " + y + " " + z;
				}
				while(cE.moveToNext());
			}
		}

		return coordenadasReferencia;
	}
		
	public double desorientacionMedia()
	{
			// Corrida de Acimutes
			//Calculo de acimut a referencias conocidas 
				//- Cojo la primera base, y recupero las referencias a las que se ha leído
		String[] valoresPrimeraBase = coordConocidasPrimeraBase.split(" ");
		String nombrePrimeraBase = valoresPrimeraBase[0];
		String xPrimeraBase = valoresPrimeraBase[2];
		String yPrimeraBase = valoresPrimeraBase[3];
		
		ArrayList<String> primeraBaseReferenciasObservaciones = new ArrayList<String>();
		ArrayList<String> primeraBaseReferenciasCoordenadas = new ArrayList<String>();
		 
					// Primero recojo las referencias visadas y las lecturas 
		String[] camposObservaciones = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
		String[] argsObservaciones = new String[] {trabajoSeleccionado, nombrePrimeraBase, "referencia"};
		Cursor c = dbObservaciones.query("Observaciones", camposObservaciones, "nombreTrabajo=? AND nombreEstacion=? AND tipo=?", argsObservaciones, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String nombreReferencia = c.getString(2);
					String ah = c.getString(3);
					String av = c.getString(4);
					String dg = c.getString(5);
					String m = c.getString(6);
					String i = c.getString(7);
	
					primeraBaseReferenciasObservaciones.add(nombreReferencia + " " + ah + " " + av + " " + dg + " " + m + " " + i);
				}
				while(c.moveToNext());
			}
		}
		
					// Ahora recojo las coordenadas de las referencias recogidas anteriormente
		String[] camposEstaciones = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
		
		for(int i = 0; i < primeraBaseReferenciasObservaciones.size(); i++)
		{
			String[] valoresEstacionComparar = primeraBaseReferenciasObservaciones.get(i).split(" ");
			String nombreEstacionComparar = valoresEstacionComparar[0];
			String[] argsEstaciones = new String[] {trabajoSeleccionado, nombreEstacionComparar};
			
			Cursor cE = dbEstaciones.query("Estaciones", camposEstaciones, "nombreTrabajo=? AND nombreEstacion=?", argsEstaciones, null, null, null);
			
			if(cE.getCount() > 0)
			{
				if(cE.moveToFirst())
				{
					do
					{
						String nombreEstacion = cE.getString(1);
						String x = cE.getString(3);
						String y = cE.getString(4);
						String z = cE.getString(5);
	
						primeraBaseReferenciasCoordenadas.add(nombreEstacion + " " + x + " " + y + " " + z);
					}
					while(cE.moveToNext());
				}
			}
		}
		
				// Procedo a calcular la desolrientación media directamente
		Formulas formulas = new Formulas();
		double xEstacion = Double.parseDouble(xPrimeraBase);
		double yEstacion = Double.parseDouble(yPrimeraBase);
		double desorientacionAcumulada = 0;
		double desorientacionMedia = 0;
		
		for(int i = 0; i < primeraBaseReferenciasCoordenadas.size(); i++)
		{
			String[] valoresReferencia = primeraBaseReferenciasCoordenadas.get(i).toString().split(" ");
			String xR = valoresReferencia[1];
			String yR = valoresReferencia[2];
			
			String[] valoresLecturaReferencia = primeraBaseReferenciasObservaciones.get(i).toString().split(" ");
			String horizontalRef = valoresLecturaReferencia[1];
	
			double xReferencia = Double.parseDouble(xR);
			double yReferencia = Double.parseDouble(yR);
			double acimutReferencia = formulas.acimut(xEstacion, yEstacion, xReferencia, yReferencia, 200);
			double lhReferencia = Double.parseDouble(horizontalRef);
			double desReferencia = acimutReferencia - lhReferencia;
			
			if(desReferencia < 0)
			{
				desReferencia = 400 + desReferencia;
			}
			
			desorientacionAcumulada = desorientacionAcumulada + desReferencia;
		}
		
		desorientacionMedia = desorientacionAcumulada/primeraBaseReferenciasCoordenadas.size();
		return desorientacionMedia;
	}
	

	public void refrescarArrayEstaciones()
	{
		estaciones.clear();
		String[] campos = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
		String[] args = new String[] {trabajoSeleccionado};
		Cursor c = dbEstaciones.query("Estaciones", campos, "nombreTrabajo=?", args, null, null, null);
		
		if(c.getCount() > 0)
		{
			if(c.moveToFirst())
			{
				do
				{
					String estacionRecogida = c.getString(1).replace(" ", "_");
					String tipoEstacion = c.getString(2);
					String x = c.getString(3);
					String y = c.getString(4);
					String z = c.getString(5);
					
					estaciones.add(estacionRecogida + " " + tipoEstacion + " " + x + " " + y + " " + z);
				}
				while(c.moveToNext());
			}
		}
	}
	
	public void refrescarListadoEstaciones(ArrayList<String> lista, ListView listView)
	{
		ArrayList<String> listaReferencias = new ArrayList<String>();
		ArrayList<String> listaBases = new ArrayList<String>();
		ArrayList<String> listaEstacionesCompleta = new ArrayList<String>();
		
		for(int i = 0; i < lista.size(); i++)
		{
			String[] valoresEstacion = lista.get(i).split(" ");
			String tipoEstacion = valoresEstacion[1];
			
			if(tipoEstacion.contentEquals("base"))
			{
				listaBases.add(lista.get(i).toString());
			}
			else
			{
				listaReferencias.add(lista.get(i).toString());
			}
		}
		
		if(listaBases.size() > 0 && listaReferencias.size() > 0)
		{
			listaEstacionesCompleta.addAll(listaReferencias);
			listaEstacionesCompleta.add(listaBases.get(0));
			listaEstacionesCompleta.add(listaBases.get(listaBases.size() - 1));
			
			coordConocidasPrimeraBase = listaBases.get(0);
			coordConocidasUltimaBase = listaBases.get(listaBases.size() - 1);
			listaBasesCompleta = listaBases;
			
			filaEstaciones = new BeanCalculosEstaciones[listaEstacionesCompleta.size()];
			
			for(int j = 0; j < listaEstacionesCompleta.size(); j++)
			{
				String[] valoresEstacion = listaEstacionesCompleta.get(j).split(" ");
				String nombreEstacion = valoresEstacion[0];
				String coordenadasEstacion = valoresEstacion[2] + " " + valoresEstacion[3] + " " +  valoresEstacion[4];
				
				filaEstaciones[j] = new BeanCalculosEstaciones(nombreEstacion, coordenadasEstacion);
			}
			
			AdaptadorListadoEstaciones adaptador = new AdaptadorListadoEstaciones(this);
			listView.setAdapter(adaptador);
		}
	}
	
	class AdaptadorListadoEstaciones extends ArrayAdapter<Object>
	{
		Activity context;  // estas variables globales son las que luego pide en el paréntesis del this
		// para hacerlas internas de la clase, hay de declararlas dentro del constructor
		DecimalFormat metros = new DecimalFormat("0.000");
		
		public AdaptadorListadoEstaciones(Activity context)
		{
			super(context, R.layout.row_personalizado, filaEstaciones);
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.row_personalizado, null);
			
			TextView lblNombreEstacion = (TextView) item.findViewById(R.id.textView1);
			TextView lblCoordenadasEstacion = (TextView) item.findViewById(R.id.textView2);
			
			String estacion = filaEstaciones[position].getNombreEstacion();
			String[] coordenadas = filaEstaciones[position].getCoordenadasEstacion().split(" ");
			double x = Double.parseDouble(coordenadas[0]);
			double y = Double.parseDouble(coordenadas[1]);
			double z = Double.parseDouble(coordenadas[2]);
			
			lblNombreEstacion.setText(estacion);
			lblCoordenadasEstacion.setText(metros.format(x) + "m " + metros.format(y) + "m " + metros.format(z) + "m");
			
			return item;
		}
	}
	
	class AdaptadorListadoBases extends ArrayAdapter<Object>
	{
		Activity context;  // estas variables globales son las que luego pide en el paréntesis del this
		// para hacerlas internas de la clase, hay de declararlas dentro del constructor
		DecimalFormat metros = new DecimalFormat("0.000");
		
		public AdaptadorListadoBases(Activity context)
		{
			super(context, R.layout.row_personalizado, filaBases);
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = context.getLayoutInflater();
			View item = inflater.inflate(R.layout.row_personalizado, null);
			
			TextView lblNombreEstacion = (TextView) item.findViewById(R.id.textView1);
			TextView lblCoordenadasEstacion = (TextView) item.findViewById(R.id.textView2);
			
			String estacion = filaBases[position].getNombreEstacion();
			String[] coordenadas = filaBases[position].getCoordenadasEstacion().split(" ");
			double x = Double.parseDouble(coordenadas[0]);
			double y = Double.parseDouble(coordenadas[1]);
			double z = Double.parseDouble(coordenadas[2]);
			
			lblNombreEstacion.setText(estacion);
			lblCoordenadasEstacion.setText(metros.format(x) + "m " + metros.format(y) + "m " + metros.format(z) + "m");
			
			return item;
		}
	}
	
	
	
	//------------------- DIALOGOS ------------------------------------------------------------------------
    protected Dialog onCreateDialog (int id)
    {
    	Dialog ventanaDialogo = null;

    	switch (id)
    	{
    		case DIALOGO_ACTUALIZAR_ESTACION:
    			ventanaDialogo = crearDialogoActualizarEstacion();
    		break;
    		
    		case DIALOGO_COMPRAR_BASEDATOS:
    			ventanaDialogo = crearDialogoComprarBD();
    		break;
    		
    		case DIALOGO_IMPRIMIR_INFORME:
    			ventanaDialogo = dialogoImprimirInforme();
    		break;
    		
    		case DIALOGO_FICHERO_OBSERVACIONES:
    			ventanaDialogo = dialogoInformeRegistros();
    		break;
    		
    		case DIALOGO_FICHERO_POLIGONAL:
    			ventanaDialogo = dialogoInformePoligonal();
    		break;
    	}
    	
		return ventanaDialogo;
    }
    
    private Dialog dialogoImprimirInforme()
    {
    	String titulo = getResources().getString(R.string.imprimir_informe);
    	String mensaje = getResources().getString(R.string.msg_imprimir_informe);
    	String registros = getResources().getString(R.string.observaciones_registradas);
    	String poligonal = getResources().getString(R.string.poligonal_medicion);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	
    	builder.setPositiveButton(poligonal, new OnClickListener()
    	{
			public void onClick(DialogInterface dialog, int which) 
			{
    			if(lstBasesPoligonal.getCount()>0)
    			{
    				showDialog(DIALOGO_FICHERO_POLIGONAL);
    			}
    			else
    			{
    				Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_toast_imprimir_poligonal), Toast.LENGTH_SHORT).show();
    			}
			}
		});
    	
    	builder.setNegativeButton(registros, new OnClickListener()
    	{
			public void onClick(DialogInterface dialog, int which) 
			{
				showDialog(DIALOGO_FICHERO_OBSERVACIONES);
			}
		});
    	
    	return builder.create();
    }
    
	private Dialog dialogoInformeRegistros()
    {
    	String titulo = getResources().getString(R.string.imprimir_observaciones_registradas);
    	String mensaje = getResources().getString(R.string.msg_imprimir_observaciones);
    	String aceptar = getResources().getString(R.string.aceptar);
    	String cancelar = getResources().getString(R.string.cancelar);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	final EditText campoDialogo = new EditText(getApplicationContext());
    	campoDialogo.setTextColor(Color.RED);
    	builder.setView(campoDialogo);
    	
    	builder.setPositiveButton(aceptar, new OnClickListener ()
    	{
    		public void onClick(DialogInterface dialog, int which)
    		{
    			String nombreFichero = campoDialogo.getText().toString();
    			imprimirRegistrosTrabajo(nombreFichero);
    		}
    	});
    	
    	builder.setNegativeButton(cancelar, new OnClickListener()
    	{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
    	
    	return builder.create();
    }
	
	private Dialog dialogoInformePoligonal()
    {
    	String titulo = getResources().getString(R.string.imprimir_poligonal);
    	String mensaje = getResources().getString(R.string.msg_imprimir_poligonal);
    	String aceptar = getResources().getString(R.string.aceptar);
    	String cancelar = getResources().getString(R.string.cancelar);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	final EditText campoDialogo = new EditText(getApplicationContext());
    	campoDialogo.setTextColor(Color.RED);
    	builder.setView(campoDialogo);
    	
    	builder.setPositiveButton(aceptar, new OnClickListener ()
    	{
    		public void onClick(DialogInterface dialog, int which)
    		{
    			String nombreFichero = campoDialogo.getText().toString();
    			imprimirPoligonalCoordenadas(nombreFichero);
    		}
    	});
    	
    	builder.setNegativeButton(cancelar, new OnClickListener()
    	{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
		});
    	
    	return builder.create();
    }
    
    private Dialog crearDialogoActualizarEstacion()
    {
    	String titulo = getResources().getString(R.string.actualizar_coordenadas);
    	String mensaje = getResources().getString(R.string.msg_actualizar_coordenadas);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	LayoutInflater inflater = this.getLayoutInflater();
    	View layout = inflater.inflate(R.layout.dialog_actualizar_coordenadas, null);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	builder.setView(layout);
    	
		Uri camposUriTopografica = Uri.parse("content://com.victor.basededatos/topografia");
		Uri camposUriGeodesica = Uri.parse("content://com.victor.geodesia/geodesia");
		
		ContentResolver crTopografica = getContentResolver();
		ContentResolver crGeodesica = getContentResolver();
    	
    	final EditText txtX = (EditText) layout.findViewById(R.id.editText1);
    	final EditText txtY = (EditText) layout.findViewById(R.id.editText2);
    	final EditText txtZ = (EditText) layout.findViewById(R.id.editText3);
    	final EditText txtConsultarPunto = (EditText) layout.findViewById(R.id.editText4);

		final Button btnAceptar = (Button) layout.findViewById(R.id.button2);
		final Button btnCancelar = (Button) layout.findViewById(R.id.button1);
		
		final ListView lstEstacionesBd = (ListView) layout.findViewById(R.id.listView1);
		
		try
		{
			String[] camposTopografica = new String[]{"proyecto", "nombrePunto", "x", "y", "z"};
			Cursor cTopografica = crTopografica.query(camposUriTopografica, camposTopografica, null, null, null);
			
			String[] camposGeodesica = new String[] {"proyecto", "nombrePunto", "phi", "landa", "x", "y", "ortometrica", "elipsoidal"};
			Cursor cGeodesica = crGeodesica.query(camposUriGeodesica, camposGeodesica, null, null, null);
			
			pintarPuntos(cTopografica, cGeodesica, lstEstacionesBd, txtConsultarPunto);
		}
		catch(Exception e)
		{
			showDialog(DIALOGO_COMPRAR_BASEDATOS); 
		}
		
		lstEstacionesBd.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				String[] valores = lstEstacionesBd.getItemAtPosition(position).toString().split(" ");
				String x = valores[2];
				String y = valores[3];
				String z = valores[4];
				
				txtX.setText(x);
				txtY.setText(y);
				txtZ.setText(z);
			}
		});
    	
		btnAceptar.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v) 
			{
				String x = txtX.getText().toString();
				String y = txtY.getText().toString();
				String z = txtZ.getText().toString();
				
				if(x.contentEquals("") || y.contentEquals("") || z.contentEquals(""))
				{
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_campos_vacios), Toast.LENGTH_SHORT).show();
				}
				else
				{
					ContentValues valuesActualizar = new ContentValues();
					valuesActualizar.put("x", x);
					valuesActualizar.put("y", y);
					valuesActualizar.put("z", z);
					
					String[] args = new String[]{trabajoSeleccionado, estacionSeleccionada};
					Log.i("dialogo actualizar estacion", "args: " + args[0] + " " + args[1]);
					dbEstaciones.update("Estaciones", valuesActualizar, "nombreTrabajo=? AND nombreEstacion=?", args);
					
					String[] campos = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
					String[] args2 = new String[] {trabajoSeleccionado};
					Cursor c = dbEstaciones.query("Estaciones", campos, "nombreTrabajo=?", args2, null, null, null);
					
					if(c.getCount() > 0)
					{
						if(c.moveToFirst())
						{
							do
							{
								String trabajo = c.getString(0);
								String estacionRecogida = c.getString(1).replace(" ", "_");
								String tipoEstacion = c.getString(2);
								String xE = c.getString(3);
								String yE = c.getString(4);
								String zE = c.getString(5);
								
								Log.i("actializar ", "registros " +trabajo + " " + estacionRecogida + " " + tipoEstacion + " " + xE + " " + yE + " " + zE);
							}
							while(c.moveToNext());
						}
					}
					
					refrescarArrayEstaciones();
					refrescarListadoEstaciones(estaciones, lstEstaciones);
					dismissDialog(DIALOGO_ACTUALIZAR_ESTACION);
				}
			}
		});
		
		btnCancelar.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				dismissDialog(DIALOGO_ACTUALIZAR_ESTACION);
			}
		});
    	
    	return builder.create();
    }
    
    private Dialog crearDialogoComprarBD()
    {
    	String titulo = getResources().getString(R.string.atencion);
    	String mensaje = getResources().getString(R.string.no_base_datos);
    	String aceptar = getResources().getString(R.string.aceptar);
    	String comprar = getResources().getString(R.string.comprar);
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle(titulo);
    	builder.setMessage(mensaje);
    	
    	builder.setPositiveButton(aceptar, new OnClickListener()
    	{
    		public void onClick(DialogInterface dialog, int which)
    		{
    			dialog.cancel();
    		}
    	});
    	
    	builder.setNegativeButton(comprar, new OnClickListener()
    	{
			public void onClick(DialogInterface dialog, int which) 
			{
		    	Intent i = new Intent(Intent.ACTION_VIEW);
		    	String ruta = "https://play.google.com/store/apps/details?id=com.victor.basededatos";
		    	i.setData(Uri.parse(ruta));
		    	startActivity(i);
			}
		});
    	
    	return builder.create();
    }
    
	public void pintarPuntos(Cursor cTopografica, Cursor cGeodesica, ListView listView1, EditText cajaTexto1)
	{
		ArrayList<String> registrosLeidos = new ArrayList<String>();
		refrescarListadosBD(registrosLeidos, listView1, cajaTexto1);
		
		if(cTopografica.moveToFirst())
		{
			do
			{
				String proyectoLeido = cTopografica.getString(0);
				String puntoLeido = cTopografica.getString(1);
				String xLeida= cTopografica.getString(2);
				String yLeida= cTopografica.getString(3);
				String zLeida= cTopografica.getString(4);
				
				registrosLeidos.add(proyectoLeido + " " + puntoLeido + " " + xLeida + " " + yLeida + " " + zLeida);
			}
			while(cTopografica.moveToNext());
		}
		
		if(cGeodesica.moveToFirst())
		{
			do
			{
				String proyectoLeido = cGeodesica.getString(0);
				String puntoLeido = cGeodesica.getString(1);
				String xLeida= cGeodesica.getString(4);
				String yLeida= cGeodesica.getString(5);
				String zLeida= cGeodesica.getString(6);
				
				registrosLeidos.add(proyectoLeido + " " + puntoLeido + " " + xLeida + " " + yLeida + " " + zLeida);
			}
			while(cGeodesica.moveToNext());
		}
		
		if(registrosLeidos.size() > 0)
		{
			refrescarListadosBD(registrosLeidos, listView1, cajaTexto1);
		}
		else
		{
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_no_registros), Toast.LENGTH_SHORT).show();
		}
	}
    
	public void refrescarListadosBD(ArrayList<String> lista, ListView listView, EditText cajaTexto1)
	{
		int lineas = lista.size();
		String[] datos = new String [lineas];
		
		for (int i= 0; i < lineas; i++)
		{
			datos[i] = (String) lista.get(i);
		}
		
        adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.row_negro, datos);
        listView.setAdapter(adaptador);
        
        cajaTexto1.addTextChangedListener(new TextWatcher()
        {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				CalculosTrabajo.this.adaptador.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void imprimirPoligonalCoordenadas(String nombreFichero)
	{
		String informe = "";
		String titulo = trabajoSeleccionado + "\n";
		String subTitulo = getResources().getString(R.string.informe_imprimir_poligonal) + "\n" + "\n";
		informe = titulo + subTitulo + coordenadasInforme;
		grabar(nombreFichero, informe);
	}
	
    public void imprimirRegistrosTrabajo(String nombreFichero)
    {
    	//Se llama a la base de datos Estaciones y se recogen todas las bases almacenadas para el trabajo seleccionado
    	String informe = getResources().getString(R.string.observaciones_almacenadas) + " " + trabajoSeleccionado + "\n" + "\n";
    	ArrayList<String> estacionesGuardadas = new ArrayList<String>();
    	
		String[] columnasEstaciones = new String[] {"nombreTrabajo", "nombreEstacion", "tipo", "x", "y", "z"};
		String[] args = new String[] {trabajoSeleccionado};
    	Cursor c = dbEstaciones.query("Estaciones", columnasEstaciones, "nombreTrabajo=?", args, null, null, null);
    	
    	if(c.getCount() > 0)
    	{
    		if(c.moveToFirst())
    		{
    			do
    			{
    				estacionesGuardadas.add(c.getString(1));
    			}
    			while(c.moveToNext());
    		}
    	}
    	
    	// Por cada estaci�n guardada, se llama a la base de datos Observaciones, y se recogen todas las observaciones de cada estaci�n
    	// Acumulo en un String, al final del bucle, toda la informaci�n recogida
    	for(int i = 0; i < estacionesGuardadas.size(); i++)
    	{
        	String[] columnasObservaciones = new String[] {"nombreTrabajo", "nombreEstacion", "nombreVisado", "ah", "av", "dg", "m", "i", "tipo"};
        	String[] argsObs = new String[] {trabajoSeleccionado, estacionesGuardadas.get(i).toString()};
        	Cursor cObs = dbObservaciones.query("Observaciones", columnasObservaciones, "nombreTrabajo=? AND nombreEstacion=?", argsObs, null, null, null);
        	informe = informe + estacionesGuardadas.get(i) + "\n";
        	
        	if(cObs.getCount() > 0)
        	{
        		Log.i("informes", "columnas observaciones: " + cObs.getColumnCount());
        		if(cObs.moveToFirst())
        		{
        			do
        			{
        				String visado = cObs.getString(2);
        				String ah = cObs.getString(3);
        				String av = cObs.getString(4);
        				String dg = cObs.getString(5);
        				String m = cObs.getString(6);
        				String iE = cObs.getString(7);
        				String observacion = visado + " " + ah + " " + av + " " + dg + " " + m + " " + iE;
        				
        				informe = informe + observacion + "\n";
        			}
        			while(cObs.moveToNext());
        		}
        	}
        	
        	informe = informe + "\n";
    	}
    	
    	grabar(nombreFichero, informe);
    }
    
    public void grabar (String nombre, String linea)
	{
    	String fichero = getResources().getString(R.string.fichero_generado);
		try
		{
			File tarjeta = Environment.getExternalStorageDirectory();
			File file = new File (tarjeta.getAbsolutePath(), nombre + ".txt");
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
			osw.write(linea + "\n");
			osw.flush();
			osw.close();
			Log.i("GRABAR", "archivo generado: " + file.toString());
			Toast.makeText(getApplicationContext(), fichero + ": " + file.getPath().toString(), Toast.LENGTH_LONG).show();
		}
		catch (Exception e) 
		{
			Toast.makeText(getApplicationContext(), "ERROR: " + e, Toast.LENGTH_LONG).show();
			Log.i("GRABAR", "peta: " + e);
		}
	}
}
