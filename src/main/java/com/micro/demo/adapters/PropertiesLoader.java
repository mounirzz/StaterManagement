package com.micro.demo.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertiesLoader {
	private static Logger logger = LoggerFactory
			.getLogger(PropertiesLoader.class);

	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	private final Properties properties;

	public PropertiesLoader(String... resourcesPaths) {
		properties = loadProperties(resourcesPaths);
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * Supprimer la propriété, mais la priorité de la propriété du système,
	 *  ne peut pas obtenir de retourner la chaîne vide.
	 */
	private String getValue(String key) {
		String systemProperty = System.getProperty(key);
		if (systemProperty != null) {
			return systemProperty;
		}
		if (properties.containsKey(key)) {
			return properties.getProperty(key);
		}
		return "";
	}

	/**
	 * Supprimez le type de chaîne de propriété, mais la priorité de propriété du système,
	 *  si tous sont Null, lance une exception.
	 */
	public String getProperty(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return value;
	}

    /**
     *Pour déterminer s'il faut définir la clé du développement de la propriété
     * @param key
     * @return
     */
    public Boolean havaProperty(String key){
        return !getProperty(key).isEmpty();
    }

	/**
	 * Supprimez le type de chaîne de propriété, mais la priorité de propriété du système. 
	 * Si les deux sont Null pour retourner à la valeur par défaut.
	 */
	public String getProperty(String key, String defaultValue) {
		String value = getValue(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * Supprimez le type de propriété Integer, mais la priorité de la propriété System. 
	 * Si les deux sont Null ou que l'erreur de contenu est levée, exception.
	 */
	public Integer getInteger(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Integer.valueOf(value);
	}

	/**
	 * Supprimez le type de propriété Integer, mais la priorité Propriété du système 
	 * Si les deux sont Null pour retourner à la valeur par défaut, si le contenu est incorrect,
	 */
	public Integer getInteger(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Integer.valueOf(value) : defaultValue;
	}

	/**
	 * Prend la propriété du type Double, mais prend d'abord la propriété System et lance une exception
	 *  si les deux sont Null ou une erreur de contenu.
	 */
	public Double getDouble(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Double.valueOf(value);
	}

	/**
	 * Supprimez la propriété du type Double, mais accordez la priorité à la propriété System. 
	 * Si les deux sont Null, renvoyez la valeur par défaut et lancez une exception si le contenu est incorrect
	 */
	public Double getDouble(String key, Integer defaultValue) {
		String value = getValue(key);
		return value != null ? Double.valueOf(value) : defaultValue;
	}

	/**
	 * Supprimer le type booléen de la propriété, mais la priorité de la propriété du système 
	 * Si Null jette une exception, si le contenu n'est pas vrai 
	 * false renvoie false.
	 */
	public Boolean getBoolean(String key) {
		String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException();
		}
		return Boolean.valueOf(value);
	}

	/**
	 * Supprimez le type booléen de la propriété, mais la priorité Propriété du système.
	 *  Si les deux sont Null pour retourner à la valeur par défaut, si le contenu n'est pas vrai /
	 * false renvoie false.
	 */
	public Boolean getBoolean(String key, boolean defaultValue) {
		String value = getValue(key);
		return value != null ? Boolean.valueOf(value) : defaultValue;
	}

	/**
	 * Chargez plusieurs fichiers, chemin du fichier en utilisant le format Spring Resource.
	 */
	private Properties loadProperties(String... resourcesPaths) {
		Properties props = new Properties();

		for (String location : resourcesPaths) {

			// logger.debug("Loading properties file from:" + location);

			InputStream is = null;
			try {
				Resource resource = resourceLoader.getResource(location);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException ex) {
				logger.info("Could not load properties from path:" + location
						+ ", " + ex.getMessage());
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return props;
	}
}
