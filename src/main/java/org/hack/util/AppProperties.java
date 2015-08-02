package org.hack.util;

import java.util.Properties;

public class AppProperties {
	private static Properties prop = new Properties();
	static{
		try {
			prop.load(AppProperties.class.getClassLoader().getResourceAsStream("app.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getProp(String propName){
		return prop.getProperty(propName);
	}
}
