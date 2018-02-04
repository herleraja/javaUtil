package com.herle.java.utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import com.herle.java.model.CountryModel;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class IPUtil {

	private static final File cityDatabase = new File("./src/main/resources/GeoLite2-City.mmdb");

	public static void main(String[] args) throws IOException, GeoIp2Exception {

		System.out.println(getCountryDetails("131.246.229.174"));

	}

	public static CountryModel getCountryDetails(String ipAddress) throws IOException, GeoIp2Exception {

		DatabaseReader dbReader = new DatabaseReader.Builder(cityDatabase).build();

		InetAddress inetAddress = InetAddress.getByName(ipAddress);
		CityResponse cityResponse = dbReader.city(inetAddress);

		String countryName = cityResponse.getCountry().getName();
		String continent = cityResponse.getContinent().getName();
		String cityName = cityResponse.getCity().getName();
		Double latitude = cityResponse.getLocation().getLatitude();
		Double longitude = cityResponse.getLocation().getLongitude();

		CountryModel countryModel = new CountryModel(countryName, continent, cityName, latitude, longitude);

		return countryModel;

	}

}
