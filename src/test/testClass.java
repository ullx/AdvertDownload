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
import java.util.concurrent.TimeUnit;
import java.io.BufferedWriter;
import java.io.File;
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
//		AbstractAnunciosFlow flowInformador = new PageElInformador();
//		driver = flowInformador.setupDriver();
//		flowInformador.runFlow();
		
		
//		AbstractAnunciosFlow flowViva = new PageVivanuncios();
//		driver = flowViva.setupDriver();
//		flowViva.runFlow();
		
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		Anuncio a = new Anuncio("autor", "descripcion", "telefono");
		anuncios.add(a);

		exportToCSV(anuncios);
		
//		AbstractAnunciosFlow flowLamudi = new PageLamudi();
//		driver = flowLamudi.setupDriver();
//		flowLamudi.runFlow(BusquedaTipo.BODEGAS, "mexico");
		
//		System.out.println("Starting test");
//		
//		PageElInformador informadorFlow= new PageElInformador();
//		informadorFlow.seleccionarBienesRaices(driver);
//		informadorFlow.opcionRenta(driver);
//		
//		
//		informadorFlow.opcionBodega(driver);
//		informadorFlow.hacerConsulta(driver);
//		
//		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
//		informadorFlow.extraerGuardarDatos(driver);
		
	}
	
	public void exportToCSV(List<Anuncio> anuncios) {
		
		File outputFilePath = new File("testFile.csv");
		
		for(Anuncio a : anuncios ) {
			try(Writer writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(outputFilePath), "utf-8"))){
				writer.write(a.getDescripcion());
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Archivo output " + outputFilePath.getAbsolutePath());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}