package com.victor.cartelera;

public class BeanFindFilms
{
	private int idFilm;
	private String imagePath;
	private String tittle;
	private String subTittle;
	private String premiereDay;

	public BeanFindFilms (int id, String im, String t, String st, String pd)
	{
		idFilm = id;
		imagePath = im;
		tittle = t;
		subTittle = st;
		premiereDay = pd;
	}
	
	
	public String getTittle() {
		return tittle;
	}
	
	public void setTittle(String tittle) {
		this.tittle = tittle;
	}
	
	public String getSubTittle() {
		return subTittle;
	}
	
	public void setSubTittle(String subTittle) {
		this.subTittle = subTittle;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
	public String getPremiereDay() {
		return premiereDay;
	}
	
	
	public void setPremiereDay(String premiereDay) {
		this.premiereDay = premiereDay;
	}
	
	
	public int getIdFilm() {
		return idFilm;
	}
	
	
	public void setIdFilm(int idFilm) {
		this.idFilm = idFilm;
	}
}