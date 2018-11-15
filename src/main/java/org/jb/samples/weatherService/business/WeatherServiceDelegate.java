package org.jb.samples.weatherService.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jb.samples.weatherService.dao.WeatherLogRepository;
import org.jb.samples.weatherService.model.WeatherInfo;
import org.jb.samples.weatherService.model.WeatherLog;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceDelegate {
	
	@Autowired
	private WeatherLogRepository weatherLogRepository;
	
	/**
	 * Get weather info from OpenWeatherMapAPI by City ID list
	 * 
	 * @return
	 */
	public List<WeatherInfo> getWeatherInfo(final List<Integer> cityIdList) {
		if(cityIdList == null || cityIdList.size() <= 0) {
			return null;
		}
		
		List<WeatherInfo> weatherInfoList = null;
		
		final String apiKey = "959ca1b935041dc961649d033de6d021";
		final String idParams = this.generateIdParameter(cityIdList);
		
		final RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000).setSocketTimeout(5000).build();
		final CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		String weatherUrl = "http://api.openweathermap.org/data/2.5/group?id=" + idParams
				+ "&units=metric" // Get temperature in Celsius
				+ "&appid=" + apiKey;
		
		final HttpGet request = new HttpGet(weatherUrl);
		try {
			
			final HttpResponse response = httpClient.execute(request);
			weatherInfoList = this.parseWeatherInfo(this.convertResponseToString(response));
			
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			request.releaseConnection();
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		return weatherInfoList;
	}
	
	/**
	 * Generate ID parameter depending on the number of IDs passed. Do not include a comma if the number of IDs passed is 1 or if the last ID is being appended
	 * 
	 * @param cityIdList
	 * @return
	 */
	private String generateIdParameter(final List<Integer> cityIdList) {
		final StringBuilder idParam = new StringBuilder();
		for(int x = 0; x < cityIdList.size(); x++) {
			idParam.append(cityIdList.get(x));
			if(cityIdList.size() == 1) {
				break;
			} else if(x < (cityIdList.size() - 1)) {
				idParam.append(",");
			}
		}
		return idParam.toString();
	}
	
	/**
	 * Convert OpenWeatherMapAPI response to plain Java String object 
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private String convertResponseToString(final HttpResponse response) throws IOException {
		Writer writer = null;
		final BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		if (br != null) {
			writer = new StringWriter();
			final char[] buffer = new char[1024];
			final Reader reader = br;
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
			try {
				br.close();
			} catch (final Exception fe) {}
		}
		return writer.toString();
	}
	
	/**
	 * Create a list of WeatherInfo by parsing the JSON response returned by OpenWeatherMapAPI
	 * 
	 * @param jsonResponse
	 * @return
	 */
	private List<WeatherInfo> parseWeatherInfo(final String jsonResponse) {
		final List<WeatherInfo> weatherInfoList = new ArrayList<WeatherInfo>();
		
		final JSONObject weatherInfoObj = new JSONObject(jsonResponse);
		final JSONArray locationArray = (JSONArray) weatherInfoObj.get("list");
		
		for(int x = 0; x < locationArray.length(); x++) {
			final JSONObject locationInfo = locationArray.getJSONObject(x);
			
			final WeatherInfo weatherInfo = new WeatherInfo();
			weatherInfo.setId(locationInfo.getLong("id"));
			weatherInfo.setLocation(locationInfo.getString("name"));
			
			final JSONArray weatherArr = locationInfo.getJSONArray("weather");
			final JSONObject weathObj = weatherArr.getJSONObject(0);
			weatherInfo.setWeather(weathObj.getString("main"));
			
			final JSONObject tempObj = locationInfo.getJSONObject("main");
			weatherInfo.setTemperature(tempObj.getDouble("temp") + " C");
			
			weatherInfoList.add(weatherInfo);
			
			weatherInfo.setTimeOfCalculation(locationInfo.getLong("dt"));
		}
		
		return weatherInfoList;
	}
	
	/**
	 * Stores the last 5 unique weather info 
	 * 
	 * @param cityIdList
	 */
	public void storeWeatherLog(final List<Integer> cityIdList) {
		final List<WeatherInfo> weatherInfoList = this.getWeatherInfo(cityIdList); // retrieve latest weather info
		final List<WeatherLog> newLogs = new ArrayList<WeatherLog>();
		for(final WeatherInfo weatherInfo: weatherInfoList) {
			newLogs.add(new WeatherLog(weatherInfo)); // Converts WeatherInfo object to WeatherLog
		}
		
		// Retrieve all WeatherLog, sorted by dateTimeInserted
		final List<WeatherLog> savedLogs = weatherLogRepository.findAllByOrderByDateTimeInsertedAsc();
				
		for(final WeatherLog newLog: newLogs) {
			// Checks whether the latest weather info is already existing in the DB. If not, add them to the list. 
			// Unique responses are determined by combining City ID and dt returned by OpenWeatherMap API 
			if(!savedLogs.contains(newLog)) {
				savedLogs.add(newLog);
			}
		}
		
		weatherLogRepository.deleteAll(); // Clear the weatherlog table
		
		Collections.reverse(savedLogs); // Reverse the list in order to get the latest unique responses first
		
		// Save the last 5 WeatherLog. If it's less than 5, then save whatever number they are
		int noOfLogsToSave = savedLogs.size() <= 5 ? savedLogs.size() : 5;
		for(int x = 0; x < noOfLogsToSave; x++) {
			weatherLogRepository.save(savedLogs.get(x));
		}
	}

}
