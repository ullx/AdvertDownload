package test;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
//import org.openqa.selenium.*;
//import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;
import java.io.File;

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
		

		AbstractAnunciosFlow flowLamudi = new PageLamudi();
		driver = flowLamudi.setupDriver();
		flowLamudi.runFlow(BusquedaTipo.BODEGAS, "mexico");
		
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

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}