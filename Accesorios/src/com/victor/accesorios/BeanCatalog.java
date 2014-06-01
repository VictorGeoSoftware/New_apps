package com.victor.accesorios;

public class BeanCatalog 
{
	private String aplication;
	private String description;
	private int image;
	
	public BeanCatalog(String app, String des, int img)
	{
		this.aplication = app;
		this.description = des;
		this.image = img;
	}
	
	public String getAplication() {
		return aplication;
	}

	public void setAplication(String aplication) {
		this.aplication = aplication;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}
}
