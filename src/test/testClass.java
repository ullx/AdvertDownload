package test;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class testClass extends TestCase {

	private WebDriver driver;
	String configFilePath = "../config.properties";

	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSimple() throws Exception {

		Properties config = loadProperties();
		

		AbstractAnunciosFlow runFlow = new PageVivanuncios(config);
		driver = runFlow.setupDriver();
		runFlow.runFlow();
		
//		List<Anuncio> anuncios = new ArrayList<Anuncio>();
//		Anuncio a = new Anuncio("transaccion", "inmueble", "descripcion", "telefono");
//		anuncios.add(a);

//		exportToCSV(anuncios);
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