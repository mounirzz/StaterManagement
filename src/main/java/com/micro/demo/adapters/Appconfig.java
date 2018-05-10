package com.micro.demo.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class AppConfig {


	// Fichier d'attributs pour charger l'objet
	
	private static PropertiesLoader propertiesLoader;

	@Value("${Spring-stater.cookie.name}")
	public String USER_COOKIE_NAME;

	@Value("${Spring-stater.cookie.age}")
	public int USER_COOKIE_AGE;

//Obtenir la configuration
	
	public static String getConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("application.properties");
		}
		return propertiesLoader.getProperty(key);
	}
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
}

