package org.jb.samples.weatherService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WeatherInfo {
	
	@JsonIgnore
	private long id;
	private String location;
	private String weather;
	private String temperature;
	
	@JsonIgnore
	private long timeOfCalculation;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getWeather() {
		return weather;
	}
	
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	public String getTemperature() {
		return temperature;
	}
	
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	
	public long getTimeOfCalculation() {
		return timeOfCalculation;
	}

	public void setTimeOfCalculation(long timeOfCalculation) {
		this.timeOfCalculation = timeOfCalculation;
	}

	@Override
	public String toString() {
		return "id : " + id + "\n"
				+ "location : " + location + "\n"
				+ "weather : " + weather + "\n"
				+ "temperature : " + temperature + "\n"
				+ "timeOfCalculation : " + timeOfCalculation;
	}

}
