package test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

abstract class AbstractAnunciosFlow {
	
	protected WebDriver driver;
	
	public WebDriver setupDriver() {
		File file = new File("C:\\Users\\Patty\\eclipse-workspace\\chromedriver_win32\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		
		//DesiredCapabilities.chrome()
		
		driver = new ChromeDriver();
		driver.get(getURL());
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		return driver;
	}
	
	abstract String getURL();
	
	public void runFlow(BusquedaTipo busquedaTipo, String estado) throws Exception {
		//Leer configuracions para saber el tipo de busqueda y estado busqueda
		
		hacerConsulta(busquedaTipo, "estado");
		
		//TODO: Regresar los datos de los anuncios para despues aqui mismo
		//mandar llamar al metodo para guardar los datos que se le pasen?
		extraerGuardarDatos();
	}
	
	/**
	 * This method is the one that should hold the loop to go throughout all and every
	 * advertisement to get the information
	 * 
	 * @param driver
	 * @throws Exception
	 */
	abstract void extraerGuardarDatos() throws Exception;

	abstract void hacerConsulta(BusquedaTipo busqueda, String estado);
	
}
