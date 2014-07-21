package com.victor.levantamientostopograficos;

public class Formulas 
{
	// ang puede ser 180, 200 o Math.PI
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
	
	
}
