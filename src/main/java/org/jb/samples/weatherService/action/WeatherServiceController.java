package org.jb.samples.weatherService.action;

import java.util.ArrayList;
import java.util.List;

import org.jb.samples.weatherService.business.WeatherServiceDelegate;
import org.jb.samples.weatherService.model.WeatherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WeatherServiceController {
	
	@Autowired
	private WeatherServiceDelegate weatherService;
	private List<Integer> cityList = new ArrayList<Integer>();;
	
	public WeatherServiceController() {
		//IDs from http://bulk.openweathermap.org/sample/city.list.json.gz
		cityList.add(2643743); // London
		cityList.add(3067696); // Prague
		cityList.add(5391959); // San Francisco
	}
	
	/**
	 * API #1 - Displays list of weather information from London, Prague and San Francisco.
	 * 
	 * @return
	 */
	@GetMapping("/getWeatherInfo")
    @ResponseBody
    public List<WeatherInfo> getWeatherInfo() {
		return weatherService.getWeatherInfo(cityList);
    }
	
	/**
	 * API #2 - API that store last five unique responses of API #1
	 * 
	 * @return
	 */
	@GetMapping("/storeWeatherInfo")
    @ResponseBody
    public String storeWeatherInfo() {
		weatherService.storeWeatherLog(cityList);
		return "Storing weather log success!";
    }

}
