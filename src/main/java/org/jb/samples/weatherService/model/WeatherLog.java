package org.jb.samples.weatherService.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "weatherlog")
public class WeatherLog implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
    private Integer id;
	
	@Column(name="responseid")
	private String responseId;
	
	@Column(name="location")
	private String location;
	
	@Column(name="actualweather")
	private String actualWeather;
	
	@Column(name="temperature")
	private String temperature;
	
	@Column(name="dtimeinserted")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTimeInserted;
	
	public WeatherLog() {}
	
	public WeatherLog(final WeatherInfo weatherInfo) {
		this.responseId = weatherInfo.getId() + "-" + weatherInfo.getTimeOfCalculation();
		this.location = weatherInfo.getLocation();
		this.actualWeather = weatherInfo.getWeather();
		this.temperature = weatherInfo.getTemperature();
		this.dateTimeInserted = new Date();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getActualWeather() {
		return actualWeather;
	}

	public void setActualWeather(String actualWeather) {
		this.actualWeather = actualWeather;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public Date getDateTimeInserted() {
		return dateTimeInserted;
	}

	public void setDateTimeInserted(Date dateTimeInserted) {
		this.dateTimeInserted = dateTimeInserted;
	}
	
	@Override
	public String toString() {
		return "id : " + id + "\n"
				+ "responseId : " + responseId + "\n"
				+ "location : " + location + "\n"
				+ "actualWeather : " + actualWeather + "\n"
				+ "temperature : " + temperature + "\n"
				+ "dateTimeInserted : " + dateTimeInserted;
	}
	
	/**
	 * Unique responses are determined by combining City ID and dt returned by OpenWeatherMap API 
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		return (this.responseId.equals(((WeatherLog) obj).responseId));
	}

}
