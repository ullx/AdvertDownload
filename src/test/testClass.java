package test;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class testClass extends TestCase {

	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
//	  File file = new File("C:\\Users\\Patty\\eclipse-workspace\\chromedriver_win32\\chromedriver.exe");
//	  System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());		 
//      driver = new ChromeDriver();
//	  driver.get(baseURL);	
//	  driver.manage().window().maximize();
//      driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
	}

	@Test
	public void testSimple() throws Exception {
		//Cargar configuracion
		//Definir que se quiere hacer Venta o Renta
		//Sacar el Inmueble
		
		Properties pro = loadProperties();

		
//		PageInmuebles24 inmuebles24 = new PageInmuebles24(pro);
//		driver = inmuebles24.setupDriver();
//		inmuebles24.runFlow(null, null);
		
	
//		PageInformador flowInformador = new PageInformador(pro);
//		driver = flowInformador.setupDriver();
//		flowInformador.runFlow(null, null);
		
		
//		AbstractAnunciosFlow flowViva = new PageVivanuncios();
//		driver = flowViva.setupDriver();
//		flowViva.runFlow(null, null);
		
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		Anuncio a = new Anuncio("transaccion", "inmueble", "descripcion", "telefono");
		anuncios.add(a);

		exportToCSV(anuncios);
		
//		AbstractAnunciosFlow flowLamudi = new PageLamudi();
//		driver = flowLamudi.setupDriver();
//		flowLamudi.runFlow(BusquedaTipo.BODEGAS, "mexico");
		
	}
	
	public void exportToCSV(List<Anuncio> anuncios) {
		
		File outputFilePath = new File("testFile.csv");
		
		for(Anuncio a : anuncios ) {
			try(Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFilePath), "utf-8"))){
				writer.write(a.getDescripcion() + a.getTelefono());
				
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Archivo output " + outputFilePath.getAbsolutePath());
	}


public Properties loadProperties() {
		Hashtable<String, String> HT = new Hashtable<String, String>();

		Properties pro = null;
		
		try {
			File source = new File(
			"C:\\Users\\Usuario\\Documents\\Automation Projects\\git\\AdvertDownload\\config.properties");

			FileInputStream input = new FileInputStream(source);
			
			pro = new Properties();
			pro.load(input);

			System.out.println("Parametros de búsqueda: ");
			System.out.println("Transacción: ---" + pro.getProperty("transaccion"));
			System.out.println("Inmuebles  : ---" + pro.getProperty("inmueble"));
			System.out.println("Ubicación  : ---" + pro.getProperty("ubicacion"));
			System.out.println("Precio Min : ---" + pro.getProperty("precioMin"));
			System.out.println("Precio Max : ---" + pro.getProperty("precioMax"));

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