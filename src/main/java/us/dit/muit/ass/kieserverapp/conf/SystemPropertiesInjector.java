package us.dit.muit.ass.kieserverapp.conf;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Permite configurar las variables de entorno de la máquina virtual java con
 * valores que se establezcan en el fichero de configuración de la aplicación.
 * Se configurarán las variables del espacio de nombres system Es decir, si en
 * el fichero application.properties aparece una variable system.aaa.bbb se
 * configurará al arrancar la aplicación
 */
@Component
@ConfigurationProperties(prefix = "system")
@Lazy(value = false)
public class SystemPropertiesInjector {

	private static final Logger logger = LogManager.getLogger();

	Map<String, String> properties;

	@PostConstruct
	void init() {
		if (properties != null) {

			for (Entry<String, String> property : properties.entrySet()) {
				logger.info("Injecting Property with Name: {} and Value: {} into System Properties", property.getKey(),
						property.getValue());
				System.setProperty(property.getKey(), property.getValue());
			}
		}
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}
