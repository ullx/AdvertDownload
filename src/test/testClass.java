package test;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

public class testClass extends TestCase {

	private WebDriver driver;
	String configFilePath = "config.properties";

	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSimple() throws Exception {
		Properties config = loadProperties();
		
		AbstractAnunciosFlow runFlow = null;
		String pagina = config.getProperty("pagina");
		
		switch(pagina) {
			case "vivanuncios" : runFlow = new PageVivanuncios(config);break;
			case "informador" : runFlow = new PageInformador(config); break;
			case "inmuebles24" : runFlow = new PageInmuebles24(config); break;
		}
		
		driver = runFlow.setupDriver();
		runFlow.runFlow();

		driver.quit();
	}

	
	private Properties loadProperties() {

		Properties pro = null;
		try {
			File configFile = new File(configFilePath);
			FileInputStream input = new FileInputStream(configFile);

			pro = new Properties();
			pro.load(input);

		} catch (Exception exp) {
			System.out.println("Exception is: ---" + exp.getMessage());
		}
		
		return pro;
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}