package com.victor.geodesia;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.util.Log;

public class Funciones 
{
    //----- Variables de paso
    private static String calculoProblemaDirectoX = "";
    private static String calculoProblemaDirectoY = "";
    private static ArrayList<String> iteracionProblemaInversoPhi = new ArrayList<String>();
    private static String calculoProblemaInversoPhi = "";
    private static String calculoProblemaInversoLanda = "";
    private static String calculoAnamorfosisGeodesicas = "";
    private static String calculoAnamorfosisUtm = "";
    private static String calculoConvergenciaGeodesicas = "";
    private static String calculoConvergenciaUtm = "";
    private static String calculoReducidaUtm = "";
    private static String calculoUtmReducida = "";
    
    
	public static String getCalculoProblemaDirectoX() {
		return calculoProblemaDirectoX;
	}

	public static void setCalculoProblemaDirectoX(
			String calculoProblemaDirectoX) {
		Funciones.calculoProblemaDirectoX = calculoProblemaDirectoX;
	}
	
	public static String getCalculoProblemaDirectoY() {
		return calculoProblemaDirectoY;
	}

	public static void setCalculoProblemaDirectoY(String calculoProblemaDirectoY) {
		Funciones.calculoProblemaDirectoY = calculoProblemaDirectoY;
	}

	public static ArrayList<String> getIteracionProblemaInversoPhi() {
		return iteracionProblemaInversoPhi;
	}

	public static void setIteracionProblemaInversoPhi(
			ArrayList<String> iteracionProblemaInversoPhi) {
		Funciones.iteracionProblemaInversoPhi = iteracionProblemaInversoPhi;
	}

	public static String getCalculoProblemaInversoPhi() {
		return calculoProblemaInversoPhi;
	}

	public static void setCalculoProblemaInversoPhi(
			String calculoProblemaInversoPhi) {
		Funciones.calculoProblemaInversoPhi = calculoProblemaInversoPhi;
	}

	public static String getCalculoProblemaInversoLanda() {
		return calculoProblemaInversoLanda;
	}

	public static void setCalculoProblemaInversoLanda(
			String calculoProblemaInversoLanda) {
		Funciones.calculoProblemaInversoLanda = calculoProblemaInversoLanda;
	}	

	public static String getCalculoAnamorfosisGeodesicas() {
		return calculoAnamorfosisGeodesicas;
	}

	public static void setCalculoAnamorfosisGeodesicas(
			String calculoAnamorfosisGeodesicas) {
		Funciones.calculoAnamorfosisGeodesicas = calculoAnamorfosisGeodesicas;
	}

	public static String getCalculoAnamorfosisUtm() {
		return calculoAnamorfosisUtm;
	}

	public static void setCalculoAnamorfosisUtm(String calculoAnamorfosisUtm) {
		Funciones.calculoAnamorfosisUtm = calculoAnamorfosisUtm;
	}	
	
	public static String getCalculoConvergenciaGeodesicas() {
		return calculoConvergenciaGeodesicas;
	}

	public static void setCalculoConvergenciaGeodesicas(
			String calculoConvergenciaGeodesicas) {
		Funciones.calculoConvergenciaGeodesicas = calculoConvergenciaGeodesicas;
	}
	
	public static String getCalculoConvergenciaUtm() {
		return calculoConvergenciaUtm;
	}

	public static void setCalculoConvergenciaUtm(String calculoConvergenciaUtm) {
		Funciones.calculoConvergenciaUtm = calculoConvergenciaUtm;
	}	
	
	public static String getCalculoReducidaUtm() {
		return calculoReducidaUtm;
	}

	public static void setCalculoReducidaUtm(String calculoReducidaUtm) {
		Funciones.calculoReducidaUtm = calculoReducidaUtm;
	}
	
	public static String getCalculoUtmReducida() {
		return calculoUtmReducida;
	}

	public static void setCalculoUtmReducida(String calculoUtmReducida) {
		Funciones.calculoUtmReducida = calculoUtmReducida;
	}
	
	
	
	//----- FUNCIONES Y SUBRRUTINAS

	public String pasar_a_sexa (double angulo)
	{	
		DecimalFormat Decimal = new DecimalFormat("0.000");
		DecimalFormat NoDecimal = new DecimalFormat("0");
		String angulo_str = Double.toString(angulo);
		String grados= quita_puntos(angulo_str);
		double angulo_entero = Double.parseDouble(grados);
		//Decimales
		double parte_decimal = angulo - angulo_entero;
		double minutos = 0;
		minutos = parte_decimal * 60; //Minutos
		Log.i("PASAR A SEXA", "minutos: " + minutos);
		String minutos_str = Double.toString(minutos);
		String minutos_enteros = quita_puntos(minutos_str);
		double minutos_ent = Double.parseDouble(minutos_enteros);
		
		double parte_decimal_segundos = minutos - minutos_ent;
		double segundos = 0;
		segundos = parte_decimal_segundos * 60;
		
		String resultado = grados + "º " + NoDecimal.format(Math.abs(minutos_ent))+ "' " + Decimal.format(Math.abs(segundos))+"''";
		return resultado;
	}
	
	public String quita_puntos(String numero)
	{
		String angulo_str = numero;
		String lector = "";
		String entero = "";
		int caracteres = angulo_str.length();
		for (int i = 0; i < caracteres; i++)
		{
			lector = angulo_str.substring(i, i+1);
			if (lector.contentEquals(".") || lector.contentEquals(","))
			{
				i = caracteres;
			}
			else
			{
				entero = entero + lector;
			}
		}
		return entero;
	}
	public static String parte_decimal(String numero)
	{
		String str_numero = numero;
		String lector = "";
		String decimal = "";
		String str_entero = "NO";
		int caracteres = numero.length();
		for(int i = 0; i<caracteres; i++)
		{
			lector = str_numero.substring(i, i+1);
			if(lector.contentEquals("."))
			{
				str_entero = "SI";
			}
			if (str_entero.contentEquals("SI"))
			{
				decimal = decimal + lector;
			}
		}
		
		return decimal;
	}
	public double acimut(double xe, double ye, double xv, double yv, double ang)
	{
		double dx = xv - xe;
		double dy = yv - ye;
		double az = 0;
		
		//Primero situamos cuadrantes
		//Direcci�n norte 
		if (dx == 0 && dy > 0)
		{
			az = 0.0000;
		}
		//Primer cuadrante
		if (dx > 0 && dy > 0)
		{
			az = Math.atan((dx/dy))*ang/Math.PI;
		}
		//Direcci�n este
		if (dx > 0 && dy == 0)
		{
			az = ang/2;
		}
		//Segundo cuadrante
		if (dx > 0 && dy < 0)
		{
			az = Math.atan((dx/dy))*ang/Math.PI + ang;
		}
		//Direcci�n sur 
		if (dx == 0 && dy < 0)
		{
			az = ang;
		}
		//Tercer cuadrante
		if (dx < 0 && dy < 0)
		{
			az = Math.atan((dx/dy))*ang/Math.PI + ang;
		}
		//Direcci�n oeste 
		if (dx < 0 && dy == 0)
		{
			az = ang*1.5;
		}
		//Cuarto cuadrante
		if (dx < 0 && dy > 0)
		{
			az = Math.atan((dx/dy))*ang/Math.PI + ang*2;
		}
		//Mismo punto
		//Tercer cuadrante
		if (dx == 0 && dy == 0)
		{
			az = 0.0000;
		}
		return az;
	}
	
	public double distancia (double xe, double ye, double xv, double yv)
	{
		double d = 0;
		double dx = xv - xe;
		double dy = yv - ye;
		
		d = Math.pow(((dx*dx)+(dy*dy)), 0.5);
		
		return d;
	}
	
	public double recoje_puntos(String grados, String minutos, String segundos) 
	{   
       //Recogo valores de puntos en cadena de caractreres
       //y los voy pasando a double
       double grados_decimales = Double.parseDouble(grados);
       double minutos_decimales = Double.parseDouble(minutos);
       double segundos_decimales = Double.parseDouble(segundos);
       
       //Hago el cálculo para pasar los grados decimales a radianes,
       //así a pelo, porque es con radianes con lo que se realizará
       //todo el proceso de cálculo
       
       //Primero recojo los grados y los pongo en grado y parte decimal de grado:
       double grados_decimal = grados_decimales + minutos_decimales/60 +segundos_decimales/3600;
       if (grados_decimales < 0)
       {
          grados_decimal = grados_decimales - minutos_decimales/60 - segundos_decimales/3600;
       }
       
       //Ahora paso los grados a radianes
       double grados_radianes = grados_decimal*(Math.PI)/180;
       //Y como paso final, pongo en el return el resultado de la operación
       return grados_radianes;    
    }
	    
    //Funcion que resuelve la coordenada X del problema directo
    public double problema_directo_x (double phi, double landa, double landa_mc, double a, double f, double ko) 
    {
        double incr_landa = landa-landa_mc;
        double b = (a*f-a)/f;
    // e²
        double e = (Math.pow(a,2) - Math.pow(b,2))/Math.pow(a,2);
    // e²'
        double e_prima = (a*a-b*b)/(b*b);
        Log.i("PROBLEMA DIRECTO X", "valor ko: " + ko);
    //N
        double N = a/Math.pow((1-e*Math.pow(Math.sin(phi),2)),0.5);
    //n²
        double n =e_prima*Math.pow(Math.cos(phi),2);
    
        double x= 500000 + ko*(incr_landa*N*Math.cos(phi)+(Math.pow(incr_landa,3)/6)*N*Math.pow(Math.cos(phi),3)*(1-Math.pow(Math.tan(phi),2)+n)+(Math.pow(incr_landa,5)/120)*N*Math.pow(Math.cos(phi),5)*(5-18*Math.pow(Math.tan(phi),2)+Math.pow(Math.tan(phi),4)+14*Math.pow(n,2)-58*Math.pow(n,2)*Math.pow(Math.tan(phi),2)));

        setCalculoProblemaDirectoX(incr_landa + " " + b + " " + e + " " + e_prima + " " + N + " " + n + " " + x);
        return x;
    }
	    
    //Funcion que resuelve la coordenada Y del problema directo
    public double problema_directo_y (double phi, double landa, double landa_mc, double a, double f, double ko) 
    {
    //Para mayor comodidad, voy a meter el incremento de landa en una variable, y ya opero con la misma
        double incr_landa = landa-landa_mc;
    //Ahora vamos a por el radio pequeño
        double b = (a*f-a)/f;
    //Ahora vamos a por e²
        double e = (Math.pow(a,2) - Math.pow(b,2))/(Math.pow(a,2));
    //Ahora vamos a por e²'
        double e_prima = (a*a-b*b)/(b*b);
    //N
        double N = a/Math.pow((1-e*Math.pow(Math.sin(phi),2)),0.5);
    //n²
        double n =e_prima*Math.pow(Math.cos(phi),2);
        
    //Aquí hay que utilizar el arco de meridiano, el cual se calculará a continuación
        double Sm = lam(a,phi,e);
    
    //Y por útlimo, vamos con lo más cojonudo de todo, la y, como es un rollo de fórmula,
    //la dividiré en 5 trozos:
        double ta = (Math.pow(incr_landa,2)/2)*N*Math.tan(phi)*Math.pow(Math.cos(phi),2);
        double tb = (Math.pow(incr_landa,4)/24)*N*Math.tan(phi)*Math.pow(Math.cos(phi),4);
        double tc = 5 - Math.pow(Math.tan(phi),2) + 9*n + 4*Math.pow(n,2);
        double td = (Math.pow(incr_landa,6)/720)*N*Math.tan(phi)*Math.pow(Math.cos(phi),6);
        double te = 61 - 58*Math.pow(Math.tan(phi),2) + Math.pow(Math.tan(phi),4) + 270*n - 330*n*Math.pow(Math.tan(phi),2);
        
        double y = ko*(Sm + ta + tb*tc + td*te);
        
        if(y < 0)
        {
        	y = y + 10000000;
        }
        
        setCalculoProblemaDirectoY(incr_landa + " " + b + " " + e + " " + e_prima + " " + N + " " + n + " " + Sm + " " + y);
        
        return y;
    }
    
    //Función que calcula la Longitud del Arco de Meridiano (1º método)  FUNCIONAAA!!!
    public double lam (double a, double phi, double e)
    {
        //Se calculan primero los diferentes valores de g:
        double g1 = phi;
        double g2 = (3.0/2)*e*(-(1.0/2)*Math.sin(phi)*Math.cos(phi)+(1.0/2)*phi);
        double g3 = (15.0/8)*Math.pow(e,2)*(-(1.0/4)*Math.pow(Math.sin(phi),3)*Math.cos(phi)-(3.0/8)*Math.sin(phi)*Math.cos(phi)+(3.0/8)*phi);
        double g4 = (35.0/16)*Math.pow(e,3)*(-(1.0/6)*Math.pow(Math.sin(phi),5)*Math.cos(phi)-(5.0/24)*Math.pow(Math.sin(phi),3)*Math.cos(phi)-(5.0/16)*Math.sin(phi)*Math.cos(phi)+(5.0/16)*phi);
        double g5 = (315.0/128)*Math.pow(e,4)*((-1.0/8)*Math.pow(Math.sin(phi),7)*Math.cos(phi)-(7.0/48)*Math.pow(Math.sin(phi),5)*Math.cos(phi)-(35.0/192)*Math.pow(Math.sin(phi),3)*Math.cos(phi)-(35.0/128)*Math.sin(phi)*Math.cos(phi)+(35.0/128)*phi);
        double g6 = (693.0/256)*Math.pow(e,5)*(-0.1*Math.pow(Math.sin(phi),9)*Math.cos(phi)-(9.0/80)*Math.pow(Math.sin(phi),7)*Math.cos(phi)-(21.0/160)*Math.pow(Math.sin(phi),5)*Math.cos(phi)-(21.0/128)*Math.pow(Math.sin(phi),3)*Math.cos(phi)-(63.0/256)*Math.sin(phi)*Math.cos(phi)+(63.0/256)*phi);
        //Una vez calculadas todas las partes, se procede a hayar el valor del arco de meridiano
        double arco_meridiano = a*(1-e)*(g1+g2+g3+g4+g5+g6);
        return arco_meridiano;
    }
    
    //Función que calcula la longitud de un arco de meridiano (2º método) FUNCIONAAAA
    //A ver si esta quiere...
    public double lam_2 (double a, double phi, double e)
    {
        double A = 1 + (3.0/4)*e + (45.0/64)*Math.pow(e,2) + (175.0/256)*Math.pow(e,3);
        double B = (3.0/4)*e + (15.0/16)*Math.pow(e,2) + (525.0/512)*Math.pow(e,3);
        double C = (15.0/64)*Math.pow(e,2) + (105.0/256)*Math.pow(e,3);
        double D = (35.0/512)*Math.pow(e,3);
        
        double m = A*a*(1-e);
        double n = (B/2)*a*(1-e);
        double p = (C/4)*a*(1-e);
        double q = (D/6)*a*(1-e);
        
        //Calculamos longitud de arco de meridiano
        double arco_meridiano = m*phi - n*Math.sin(2*phi) + p*Math.sin(4*phi) - q*Math.sin(6*phi);
        return arco_meridiano;
    }
    
    public double ConvertirObjectToDouble(Object Obj)
    {
        String Str = Obj.toString();
        double NumDouble = Double.valueOf(Str).doubleValue();
        if (Str =="")
        {
            NumDouble = 0;
        }
        return NumDouble;
    }
	    
    public double problema_inverso_phi(double a, double e, double f, double X, double Y, double precision_requerida, double ko, String hemisferio)
    {
		double factorHemiSur = 0;
        if(hemisferio.contentEquals("S")) factorHemiSur = 10000000;
        iteracionProblemaInversoPhi.clear();
        double y = (Y - factorHemiSur)/ko;
        
        double phi_0 = phi_p(y, a, e);
        double phi_recibido = 0;
        double phi_devuelto = 0;
        double precision = 10;
        double diferencial = 0;
        int cnt_pasadas = 0;
        
        while (precision_requerida < precision)
        {
        	if (cnt_pasadas == 0)
        	{
        		phi_recibido = phi_0;
        	}
        	else
        	{
        		phi_recibido = phi_devuelto;
        	}
        	
        	double phi_recibido_sexa = phi_recibido*180/Math.PI;
        	String str_sexa_decimales = parte_decimal(Double.toString(phi_recibido_sexa));
        	double sexa_decimales = Double.parseDouble(str_sexa_decimales);
        	double sexa_segundos = 3600/sexa_decimales;
        	
        	System.out.println("Angulo: "+ phi_recibido_sexa);
        	System.out.println("Segundos recibido: " + sexa_segundos);
        	
	        double g2 = (3.0/2)*e*(-(1.0/2)*Math.sin(phi_recibido)*Math.cos(phi_recibido)+(1.0/2)*phi_recibido);
	        double g3 = (15.0/8)*Math.pow(e,2)*(-(1.0/4)*Math.pow(Math.sin(phi_recibido),3)*Math.cos(phi_recibido)-(3.0/8)*Math.sin(phi_recibido)*Math.cos(phi_recibido)+(3.0/8)*phi_recibido);
	        double g4 = (35.0/16)*Math.pow(e,3)*(-(1.0/6)*Math.pow(Math.sin(phi_recibido),5)*Math.cos(phi_recibido)-(5.0/24)*Math.pow(Math.sin(phi_recibido),3)*Math.cos(phi_recibido)-(5.0/16)*Math.sin(phi_recibido)*Math.cos(phi_recibido)+(5.0/16)*phi_recibido);
	        double g5 = (315.0/128)*Math.pow(e,4)*((-1.0/8)*Math.pow(Math.sin(phi_recibido),7)*Math.cos(phi_recibido)-(7.0/48)*Math.pow(Math.sin(phi_recibido),5)*Math.cos(phi_recibido)-(35.0/192)*Math.pow(Math.sin(phi_recibido),3)*Math.cos(phi_recibido)-(35.0/128)*Math.sin(phi_recibido)*Math.cos(phi_recibido)+(35.0/128)*phi_recibido);
	        double g6 = (693.0/256)*Math.pow(e,5)*(-0.1*Math.pow(Math.sin(phi_recibido),9)*Math.cos(phi_recibido)-(9.0/80)*Math.pow(Math.sin(phi_recibido),7)*Math.cos(phi_recibido)-(21.0/160)*Math.pow(Math.sin(phi_recibido),5)*Math.cos(phi_recibido)-(21.0/128)*Math.pow(Math.sin(phi_recibido),3)*Math.cos(phi_recibido)-(63.0/256)*Math.sin(phi_recibido)*Math.cos(phi_recibido)+(63.0/256)*phi_recibido);
	        
	        diferencial = g2 + g3 + g4 + g5 + g6;
	        phi_devuelto = phi_0 - diferencial;
	        
	        double phi_devuelto_sexa = phi_devuelto*180/Math.PI;
        	String str_devuelto_decimales = Funciones.parte_decimal(Double.toString(phi_devuelto_sexa));
        	double devuelto_decimales = Double.parseDouble(str_devuelto_decimales);
        	double devuelto_segundos = 3600/devuelto_decimales;
        	
	        precision = Math.abs(sexa_segundos - devuelto_segundos);
	        System.out.println("Segundos devuelto / precision: " + devuelto_segundos + " / " + precision);
	        
	        iteracionProblemaInversoPhi.add(g2 + " " + g3 + " " + g4 + " " + g5 + " " + g6 + " " + diferencial + phi_devuelto_sexa);
	       
	        cnt_pasadas = cnt_pasadas + 1;
        }
        
        return phi_devuelto;
    }
    
    public double problema_inverso_landa (double a, double e, double f, double X, double phi_prima, double ko)
    {
        //N
        double N_prima = a/Math.pow((1-e*Math.pow(Math.sin(phi_prima),2)),0.5);
        System.out.println("N_prima_landa: " +N_prima);
        //Ahora vamos a por el radio pequeño
        double b = (a*f-a)/f;
        //Ahora vamos a por e²'
        double e_prima = (a*a-b*b)/(b*b);
        //n²
        double n_prima_cuadrado =e_prima*Math.pow(Math.cos(phi_prima),2);
        double n_prima = Math.pow(n_prima_cuadrado, .5);
        System.out.println("n_prima_metodo_landa: " +n_prima_cuadrado);
        //x
        double x= (X-500000)/ko;        
        double landa = (x/(N_prima*Math.cos(phi_prima)))-(Math.pow(x, 3))/(6*Math.pow(N_prima, 3)*Math.cos(phi_prima))*(1+2*Math.pow(Math.tan(phi_prima), 2)+Math.pow(n_prima, 2))+(Math.pow(x, 5)/(120*Math.pow(N_prima, 5)*Math.cos(phi_prima)))*(5+28*Math.pow(Math.tan(phi_prima), 2)+24*Math.pow(Math.tan(phi_prima), 4)+6*n_prima*n_prima+8*n_prima*n_prima*Math.pow(Math.tan(phi_prima), 2));

        setCalculoProblemaInversoLanda(N_prima + " " + b + " " + e_prima + " " + n_prima_cuadrado + " " + n_prima + " " + x + " " + landa);
        return landa;
    }
    
    public static double phi_p (double y, double a, double e)
    {
        double phi_prima = y/(a*(1-e));
        return phi_prima;
    }
    public double problema_inverso_phi_absoluta(double a, double e, double f, double X, double Y, double phi_iterada, double ko)
    {
        double phi_prima = phi_iterada;
        //N
        double N_prima = a/Math.pow((1-e*Math.pow(Math.sin(phi_prima),2)),0.5);
        //Ahora vamos a por el radio peque�o
        double b = (a*f-a)/f;
        //Ahora vamos a por e�'
        double e_prima = (a*a-b*b)/(b*b);
        //n�
        double n_prima_cuadrado =e_prima*Math.pow(Math.cos(phi_prima),2);
        double n_prima = Math.pow(n_prima_cuadrado, .5);
        //x
        double x= (X-500000)/ko;

        double i = -Math.pow(x, 2)*Math.tan(phi_prima)*(1+Math.pow(n_prima, 2))/(2*Math.pow(N_prima, 2)) + (Math.pow(x, 4)/(24*Math.pow(N_prima, 4))*Math.tan(phi_prima)*(5+3*Math.pow(Math.tan(phi_prima), 2) + 6*Math.pow(n_prima, 2) - 6*Math.pow(n_prima, 2)*Math.pow(Math.tan(phi_prima),2) - 3*Math.pow(n_prima, 4) - 9*Math.pow(n_prima, 4)*Math.pow(Math.tan(phi_prima), 2)) - (Math.pow(x, 6)/(720*Math.pow(N_prima, 6))*Math.tan(phi_prima)*(61+90*Math.pow(Math.tan(phi_prima), 2) + 45*Math.pow(Math.tan(phi_prima), 4) + 107*Math.pow(n_prima, 2)-162*Math.pow(n_prima, 2)*Math.pow(Math.tan(phi_prima), 2)-45*Math.pow(n_prima, 2)*Math.pow(Math.tan(phi_prima), 4))));
        double phi =phi_prima + i;
        
        setCalculoProblemaInversoPhi(phi_prima + " " + N_prima + " " + b + " " + e_prima + " " + n_prima_cuadrado + " " + 
        								n_prima + " " + x + " " + i + " " + phi_prima);
        return phi;
    }
    
    public double convergenciaMeridianosGeodesicas(double a, double f, double phi, double incrementoLanda)
    {
    	double convergencia = 0;
    	double landa = incrementoLanda;
        double b = (a*f-a)/f;
        
        double e_prima_cuadrado = (a*a-b*b)/(b*b);

        
        double n_cuadrado = e_prima_cuadrado*Math.pow(Math.cos(phi),2);
        
        double terminoConvergencia1 = landa*Math.sin(phi);
        double terminoConvergencia2 = (Math.pow(landa, 3)/3)*Math.sin(phi)*Math.pow(Math.cos(phi), 2)*(1 + 3*n_cuadrado + 2*Math.pow(n_cuadrado, 2));
        double terminoConvergencia3 = (Math.pow(landa, 5)/15)*Math.sin(phi)*Math.pow(Math.cos(phi), 4)*(2 - Math.pow(Math.tan(phi), 2));
        convergencia = terminoConvergencia1 + terminoConvergencia2 + terminoConvergencia3;
    	double convergenciaRad = convergencia*180/Math.PI;
    	
    	setCalculoConvergenciaGeodesicas(landa + " " + b + " " + e_prima_cuadrado + " " + n_cuadrado + " " + convergencia + " " + convergenciaRad);
    	return convergenciaRad;
    }
    
    public double convergenciameridianosUtm(double a, double f, double e, double X, double Y, double pecisionRequerida, double ko, String hemisferio)
    {
    	double convergencia = 0;
        
    	double phiIterada = problema_inverso_phi(a, e, f, X, Y, pecisionRequerida, ko, hemisferio);
    	double b = (a*f-a)/f;
    	double e_cuadrado = (Math.pow(a,2) - Math.pow(b,2))/Math.pow(a,2);
    	double e_prima_cuadrado = (a*a-b*b)/(b*b);
    	
    	double x = (X - 500000)/ko;
    	double N_prima = a/Math.pow((1-e_cuadrado*Math.pow(Math.sin(phiIterada),2)),0.5);
    	double n_prima_cuadrado = e_prima_cuadrado*Math.pow(Math.cos(phiIterada),2);
    	
    	
    	double termino1 = (x*Math.tan(phiIterada))/N_prima;
    	double termino2 = (Math.pow(x, 3)/(3*Math.pow(N_prima, 3)))*Math.tan(phiIterada)*(1 + Math.pow(Math.tan(phiIterada), 2) + n_prima_cuadrado - 2*Math.pow(n_prima_cuadrado, 2));
    	double termino3 = (Math.pow(x, 5)/(15*Math.pow(N_prima, 5)))*Math.tan(phiIterada)*(2 + 5*Math.pow(Math.tan(phiIterada), 2) + 3*Math.pow(Math.tan(phiIterada), 4));
    	convergencia = termino1 - termino2 + termino3;
    	double convergenciaRad = convergencia*180/Math.PI;
    	
    	setCalculoConvergenciaUtm(phiIterada + " " + b + " " + e_cuadrado + " " + e_prima_cuadrado + " " + x + " " + N_prima + " " +
    								n_prima_cuadrado + " " + convergencia + " " + convergenciaRad);
    	return convergenciaRad;
    }
    
    public double anamorfosisLinealGeodesicas(double a, double f, double phi, double incrementoLanda, double ko)
    {
    	double anamorfosis = 0;
    	double landa = incrementoLanda;
        double b = (a*f-a)/f;
        
        double e_prima_cuadrado = (a*a-b*b)/(b*b);
        double n_cuadrado = e_prima_cuadrado*Math.pow(Math.cos(phi),2);
        
        double termino1 = ko*(1+0.5*Math.pow(landa, 2)*Math.pow(Math.cos(phi), 2)*(1 + n_cuadrado));
        double termino2 = (1/24)*Math.pow(landa, 4)*Math.pow(Math.cos(phi), 4);
        double termino3 = 5-4*Math.pow(Math.tan(phi), 4) + 14*n_cuadrado - 28*n_cuadrado*Math.pow(Math.tan(phi), 2) + 
        					13*Math.pow(n_cuadrado, 2) - 48*Math.pow(n_cuadrado, 2)*Math.pow(Math.tan(phi), 2) +
        					4*Math.pow(n_cuadrado, 3) - 24*Math.pow(n_cuadrado, 3)*Math.pow(Math.tan(phi), 2);
        
        anamorfosis = termino1 + termino2*termino3;
        
        setCalculoAnamorfosisGeodesicas(landa + " " + b + " " + e_prima_cuadrado + " " + n_cuadrado + " " + anamorfosis);
    	return anamorfosis;
    }
    
    public double anamorfosisLinealUtm(double a, double f, double e, double X, double Y, double pecisionRequerida, double ko, String hemisferio)
    {
    	double anamorfosis = 0;
    	
    	double phiPrima = problema_inverso_phi(a, e, f, X, Y, pecisionRequerida, ko, hemisferio);
    	double phi = problema_inverso_phi_absoluta(a, e, f, X, Y, phiPrima, ko);
    	
        double b = (a*f-a)/f;
        double e_prima_cuadrado = (a*a-b*b)/(b*b);
    	double x = (X - 500000)/ko;
    	double N_prima = a/Math.pow((1-e*Math.pow(Math.sin(phiPrima),2)),0.5);
    	double n_prima_cuadrado =e_prima_cuadrado*Math.pow(Math.cos(phiPrima),2);
    	double p = (a*(1 - e))/Math.pow((1 - e*Math.pow(Math.sin(phi), 2)), 3/2);
    	
    	anamorfosis = ko*(1 + (Math.pow(x, 2)/(2*p*N_prima)) + (Math.pow(x, 4)/(24*Math.pow(N_prima, 4))*(1 + 6*n_prima_cuadrado)));
    	
    	setCalculoAnamorfosisUtm(phiPrima + " " + phi + " " + b + " " + " " + e_prima_cuadrado + " " + x + " " + N_prima + " " + n_prima_cuadrado + " " + p + " " + anamorfosis);
    	return anamorfosis;
    }
    
    public double reducidaUtm(double a, double f, double e, double phi, double Dr, double H, double incrementoLanda, double ko)
    {
    	double distanciaUtm = 0;
    	
    	//Cálculo de elementos, paso a paso
    	double ro = (a*(1 - e))/(Math.pow((1 - e*Math.pow(Math.sin(phi), 2)), 3/2));
    	double N = a/Math.pow((1-e*Math.pow(Math.sin(phi),2)),0.5);
    	double radioEsferaLocal = Math.sqrt(ro*N);
    	double R = 1 - H/radioEsferaLocal - (Math.pow(H, 2))/(Math.pow(radioEsferaLocal, 2));
    	double D1 = Dr*R;
    	double K1 = anamorfosisLinealGeodesicas(a, f, phi, incrementoLanda, ko);
    	double D2 = D1 + (Math.pow(D1, 3)/(24*Math.pow(radioEsferaLocal, 2)));
    	distanciaUtm = D2*K1;
    	
    	setCalculoReducidaUtm(ro + " " + N + " " + radioEsferaLocal + " " + R + " " + D1 + " " + K1 + " " + D2 + " " + distanciaUtm);
    	return distanciaUtm;
    }
    
    public double utmReducida(double a, double f, double e, double X, double Y, double dUtm, double H, double ko, String hemisferio)
    {
    	double distanciaReducida = 0;
    	
    	double phiPrima = problema_inverso_phi(a, e, f, X, Y, 0.0000, ko, hemisferio);
    	double phi = problema_inverso_phi_absoluta(a, e, f, X, Y, phiPrima, ko);
    	
    	double ro = (a*(1 - e))/(Math.pow((1 - e*Math.pow(Math.sin(phi), 2)), 3/2));
    	double N = a/Math.pow((1-e*Math.pow(Math.sin(phi),2)),0.5);
    	double radioEsferaLocal = Math.sqrt(ro*N);
    	
    	//Calculo de D2
    	double K = anamorfosisLinealUtm(a, f, e, X, Y, 0.0000, ko, hemisferio);
    	double D2 = dUtm/K;
    	double R = 1 - H/radioEsferaLocal - (Math.pow(H, 2))/(Math.pow(radioEsferaLocal, 2));
    	distanciaReducida = D2/R;
    	
    	setCalculoUtmReducida(phiPrima + " " + phi + " " + ro + " " + N + " " + radioEsferaLocal + " " + K + " " + D2 + " " + R + " " + distanciaReducida);
    	return distanciaReducida;
    }
    
    public double reduccionDistanciaCuerda(double dCart, double azCartSexa, double X, double Y, double a, double e, double f, double ko, String hemisferio){
    	double dGeodesica = 0;
    	double azCart = azCartSexa*Math.PI/180;
    	
    	double phiPrima = problema_inverso_phi(a, e, f, X, Y, 0.0000, ko, hemisferio);
    	double phi = problema_inverso_phi_absoluta(a, e, f, X, Y, phiPrima, ko);
    	
    	double x = (X - 500000)/ko;
    	double ro = (a*(1 - e))/(Math.pow((1 - e*Math.pow(Math.sin(phi), 2)), 3/2));
    	double N = a/Math.pow((1-e*Math.pow(Math.sin(phi),2)),0.5);
    	double K = anamorfosisLinealUtm(a, f, e, X, Y, 0.0000, ko, hemisferio);
    	
    	dGeodesica = dCart + (1/24)*Math.pow(((x*Math.cos(azCart))/(ro*N*Math.pow(K, 2))), 2)*Math.pow(dCart, 3);
    	
    	return dGeodesica;
    }
    
    public double calculoAcimutGeodesico(double acimutCartografico, double xEst, double yEst, double xVis, double yVis, 
    										double a, double f, double e, double ko, String hemisferio){
    	double acimutGeodesico = 0;
    	
    	double phiPrima = problema_inverso_phi(a, e, f, xEst, yEst, 0.0000, ko, hemisferio);
    	double phi = problema_inverso_phi_absoluta(a, e, f, xEst, yEst, phiPrima, ko);
    	
    	double xE = (xEst - 500000)/ko;
    	double xV = (xVis - 500000)/ko;
    	double yE = 0;
    	double yV = 0;
    	if(hemisferio.contentEquals("S")){
    		yE = (yEst - 10000000)/ko;
    		yV = (yVis - 10000000)/ko;
    	} else{
        	yE = yEst/ko;
        	yV = yVis/ko;    		
    	}
    	
    	double b = (a*f-a)/f;
        double e_prima_cuadrado = (a*a-b*b)/(b*b);
        double n_cuadrado = e_prima_cuadrado*Math.pow(Math.cos(phi),2);
    	double N = a/Math.pow((1-e*Math.pow(Math.sin(phi),2)),0.5);
    	double ro = (a*(1 - e))/(Math.pow((1 - e*Math.pow(Math.sin(phi), 2)), 3/2));
    	
    	double dTab = ((yV - yE)*(2*xE + xV)*(1 + n_cuadrado))/(6*N*ro);
    	double azCg = acimutCartografico + dTab;
    	
    	double convergencia = convergenciameridianosUtm(a, f, e, xE, yE, 0.0000, ko, hemisferio);
    	acimutGeodesico = azCg + convergencia;
    	
    	return acimutGeodesico;
    }
}