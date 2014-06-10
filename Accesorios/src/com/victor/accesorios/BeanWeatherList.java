package com.victor.accesorios;

public class BeanWeatherList 
{
	private String data;
	private String hour;
	private String temperature;
	private String description;
	private String pressure;
	private String humidity;
	private String icon;
	
	public BeanWeatherList(String data, String hour, String temperature, String description, String pressure, String humidity, String icon)
	{
		this.data = data;
		this.hour = hour;
		this.temperature = temperature;
		this.description = description;
		this.pressure = pressure;
		this.humidity = humidity;
		this.icon = icon;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String weather) {
		this.temperature = weather;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}
}
