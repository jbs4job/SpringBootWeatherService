DROP DATABASE IF EXISTS weather_info;
CREATE DATABASE weather_info;

USE weather_info;

DROP DATABASE IF EXISTS WeatherLog;
create table WeatherLog (
    Id bigint(20) not null auto_increment,
    responseId varchar(50),
    location varchar(100),
    actualWeather varchar(100),
    temperature varchar(50),
    dtimeInserted timestamp,
    primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;