package com.victor.levantamientostopograficos;

public class BeanObservaciones 
{
	private String punto;
	private String tipoPunto;
	
	public BeanObservaciones (String p, String tp)
	{
		punto = p;
		tipoPunto = tp;
	}
	
	public String getPunto()
	{
		return punto;
	}
	
	public String getTipoPunto()
	{
		return tipoPunto;
	}
}
