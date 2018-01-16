package test;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class testClass extends TestCase {
	private WebDriver driver;
	String baseURL = "http://aviso.informador.com.mx/";

	@Before
	public void setUp() throws Exception {
	  File file = new File("C:\\Users\\Usuario\\Documents\\Tools\\Drivers\\chromedriver_win32\\chromedriver.exe");
	  System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());		 
      driver = new ChromeDriver();
	  driver.get(baseURL);	
	  driver.manage().window().maximize();
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void testSimple() throws Exception {
		
		System.out.println("Starting test");
		
		PageFlow PF= new PageFlow();
		PF.seleccionarBienesRaices(driver);
		PF.opcionRenta(driver);
		
		
		PF.opcionBodega(driver);
		PF.hacerConsulta(driver);
		
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		PF.extraerGuardarDatos(driver);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}