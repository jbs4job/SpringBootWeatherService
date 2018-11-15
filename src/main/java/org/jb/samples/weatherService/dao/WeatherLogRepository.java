package org.jb.samples.weatherService.dao;

import java.util.List;

import org.jb.samples.weatherService.model.WeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherLogRepository extends JpaRepository<WeatherLog, Integer> {
	
	/**
	 * Retrieves all WeatherLog and sort them in ascending order by dateTimeInserted
	 * 
	 * @return
	 */
	public List<WeatherLog> findAllByOrderByDateTimeInsertedAsc();
	
	/**
	 * Clears the WeatherLog table
	 */
	public void deleteAll();

}
