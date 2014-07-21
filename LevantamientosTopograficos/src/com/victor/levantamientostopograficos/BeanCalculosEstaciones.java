package com.victor.levantamientostopograficos;

public class BeanCalculosEstaciones 
{
	String nombreEstacion = "";
	String coordenadasEstacion = "";
	
	public BeanCalculosEstaciones(String nE, String cE)
	{
		nombreEstacion = nE;
		coordenadasEstacion = cE;
	}
	
	public String getNombreEstacion()
	{
		return nombreEstacion;
	}
	
	public String getCoordenadasEstacion()
	{
		return coordenadasEstacion;
	}
}
