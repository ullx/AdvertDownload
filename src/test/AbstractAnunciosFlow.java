package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

abstract class AbstractAnunciosFlow {
	
	public static Logger log = LogManager.getLogger(AbstractAnunciosFlow.class);
	String chromeDriverPath = "chromedriver.exe";
	protected WebDriver driver;
	protected Properties config;
	protected BusquedaTipo inmuebleTipo;
	String outputFileDir = "resultados";
	String transaccionConfig = null;
	
	public WebDriver setupDriver() {
		File file = new File(chromeDriverPath);
		log.info("searching for driver " + file.getAbsolutePath());
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
		 transaccionConfig = config.getProperty("transaccion");
		String inmuebleConfig = config.getProperty("inmueble");
		String ubicacionConfig = config.getProperty("ubicacion") != null? config.getProperty("ubicacion") : "";
		String precioMinConfig = config.getProperty("precioMin");
		String precioMaxConfig = config.getProperty("precioMax");
		
		log.info("Parametros de búsqueda: ");
		log.info("Transacción: ---" + transaccionConfig);
		log.info("Inmuebles  : ---" + inmuebleConfig);
		log.info("Ubicación  : ---" + ubicacionConfig);
		log.info("Precio Min : ---" + precioMinConfig);
		log.info("Precio Max : ---" + precioMaxConfig);
		
		
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
		
		hacerConsulta();
		
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
	
	public void exportToCSV(List<Anuncio> anuncios) throws UnsupportedEncodingException, FileNotFoundException {
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
		String date = df.format(cal.getTime());
		log.info("Se escribiran " + anuncios.size());
		String pagina = config.getProperty("pagina");
		String fileName = String.format("%s %s %s %s %s.csv", pagina, config.getProperty("inmueble"), config.getProperty("transaccion"), config.getProperty("ubicacion"), date);
		
		File dir = new File(outputFileDir);
		if(dir.exists() == false) {
			if(dir.mkdir() == false) {
				log.info("No se pudo crear el directorio " + dir.getAbsolutePath());
				dir = new File("C:/");
			}
		}
		File outputFilePath = new File(dir, fileName);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath), "utf-8"));
		try {

			for (Anuncio a : anuncios) {
				String desc = Utils.cleanToWriteCSV(a.getDescripcion());
				String titulo = Utils.cleanToWriteCSV(a.getTitulo());
				String precio = Utils.cleanToWriteCSV(a.getPrecio());
				String tel = Utils.cleanToWriteCSV(a.getTelefono());
				writer.write(String.format(",%s %s %s %s %s, numeroSucursal", titulo, desc, precio, tel,transaccionConfig ));
				writer.newLine();

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
		log.info("Archivo output " + outputFilePath.getAbsolutePath());
	}
	
}
