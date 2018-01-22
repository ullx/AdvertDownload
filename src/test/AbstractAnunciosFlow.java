package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

abstract class AbstractAnunciosFlow {
	
	String chromeDriverPath = "chromedriver.exe";
	protected WebDriver driver;
	protected Properties config;
	protected BusquedaTipo inmuebleTipo;
	String outputFileDir = "../resultados";
	
	public WebDriver setupDriver() {
//		File file = new File("C:\\Users\\Usuario\\Documents\\Tools\\Drivers\\chromedriver_win32\\chromedriver.exe");
		File file = new File(chromeDriverPath);
		System.out.println("searching for driver " + file.getAbsolutePath());
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		
		//DesiredCapabilities.chrome()
		//Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
		driver = new ChromeDriver();
		driver.get(getURL());
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		return driver;
	}
	
	public AbstractAnunciosFlow(Properties config) {
		this.config = config;
		String transaccionConfig = config.getProperty("transaccion");
		String inmuebleConfig = config.getProperty("inmueble");
		String ubicacionConfig = config.getProperty("ubicacion") != null? config.getProperty("ubicacion") : "";
		String precioMinConfig = config.getProperty("precioMin");
		String precioMaxConfig = config.getProperty("precioMax");
		
		System.out.println("Parametros de búsqueda: ");
		System.out.println("Transacción: ---" + transaccionConfig);
		System.out.println("Inmuebles  : ---" + inmuebleConfig);
		System.out.println("Ubicación  : ---" + ubicacionConfig);
		System.out.println("Precio Min : ---" + precioMinConfig);
		System.out.println("Precio Max : ---" + precioMaxConfig);
		
		
		this.inmuebleTipo = BusquedaTipo.getValueOf(inmuebleConfig);
		if(inmuebleTipo == null) {
			System.err.println("No se configuró un tipo de inmueble válido " + inmuebleConfig);
			throw new RuntimeException();
		}
		
		//NO CAMBIAR ESTO SE TIENEN QUE EVALUAR LAS DOS CONDICIONES
		if(!transaccionConfig.equalsIgnoreCase("renta") & !transaccionConfig.equalsIgnoreCase("venta")) {
			System.err.println("No se configuró un tipo de transaccion válido " + transaccionConfig);
			throw new RuntimeException();
		}
		
	}
	
	abstract String getURL();
	
	public void runFlow() throws Exception {
		//Leer configuracions para saber el tipo de busqueda y estado busqueda
		
		hacerConsulta();
		
		//TODO: Regresar los datos de los anuncios para despues aqui mismo
		//mandar llamar al metodo para guardar los datos que se le pasen?
		
		List<Anuncio> anuncios =  extraerGuardarDatos();
		exportToCSV(anuncios);
	}
	
	/**
	 * This method is the one that should hold the loop to go throughout all and every
	 * advertisement to get the information
	 * 
	 * @param driver
	 * @throws Exception
	 */
	abstract List<Anuncio> extraerGuardarDatos() throws Exception;

	abstract void hacerConsulta();
	

	public void exportToCSV(List<Anuncio> anuncios) {
		String fileName = config.getProperty("inmueble") + " " + config.getProperty("transaccion") + ".csv";

		File outputFilePath = new File(outputFileDir, fileName);
		Writer writer = null;
		
		for (Anuncio a : anuncios) {
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "utf-8"));
				writer.write(" ," + a.getDescripcion() + a.getTelefono() + " numeroDeSucursal");
				((BufferedWriter) writer).newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {writer.close();} catch (Exception e) {}
			}
		}
		System.out.println("Archivo output " + outputFilePath.getAbsolutePath());
	}
	
}
