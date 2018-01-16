package test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PageFlow {

	public void seleccionarBienesRaices(WebDriver driver) {
		WebElement bienesRaices = driver.findElement(By.className("type-house"));
		bienesRaices.click();
	}

	public void opcionRenta(WebDriver driver) {

		WebElement DropdownRenta = driver.findElement(By.id("drop_tran"));
		WebElement div1 = DropdownRenta.findElement(By.className("dropdown"));
		div1.click();

		try {
			WebElement div2 = div1.findElement(By.tagName("div"));
			List<WebElement> liListaRenta = div2.findElements(By.tagName("li"));
			WebElement A = liListaRenta.get(1);
			Thread.sleep(1000);

			for (WebElement liRenta : liListaRenta) {
				if (liRenta.getText().equals("Renta")) {
					System.out.println("Opción Seleccionada: " + A);
					liRenta.click();
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void opcionBodega(WebDriver driver) {

		WebElement DropdownBodega = driver.findElement(By.id("drop_inmu"));
		WebElement div3 = DropdownBodega.findElement(By.className("dropdown"));
		div3.click();

		try {
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement div4 = div3.findElement(By.tagName("div"));
			List<WebElement> liListaBodega = div4.findElements(By.tagName("li"));
			WebElement B = liListaBodega.get(6);
			Thread.sleep(1000);

			for (WebElement liBodega : liListaBodega) {
				if (liBodega.getText().equals("Bodegas")) {
					System.out.println("Opción Seleccionada: " + B);
					liBodega.click();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void hacerConsulta(WebDriver driver) {

		 WebElement zonaMetropolitana = driver.findElement(By.id("quick-search"));
		 zonaMetropolitana.click();

//		WebElement zapopan = driver.findElement(By.id("quick-searchZap"));
//		zapopan.click();

//		 WebElement tlaquepaque = driver.findElement(By.id("quick-searchTlaq"));
//		 tlaquepaque.click();

	}

	public void extraerGuardarDatos(WebDriver driver) throws Exception {
		WebElement siguiente = null;
		List<Anuncio> anuncios = new ArrayList<Anuncio>();

		for (;;) {

			WebElement municipio = driver.findElement(By.id("items"));
			List<WebElement> listaResultados = municipio.findElements(By.tagName("li"));
			int results = listaResultados.size();
			System.out.println("Número de resultados: " + results);

			for (int idx = 0; idx < results; idx++) {

				listaResultados.get(idx).click();
				Anuncio data = getDataFromLink(driver);

				System.out.println("Contacto: " + idx + " " + data.getNombreAutor());
				System.out.println("Descripción: " + idx + " " + data.getDescripcion());
				System.out.println("Teléfono: " + idx + " " + data.getTel());
				anuncios.add(data);

				try {
					WebElement regresar = driver.findElement(By.linkText("< Regresar"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontro link regresar");
					break;
				}

				municipio = driver.findElement(By.id("items"));
				listaResultados = municipio.findElements(By.tagName("li"));

			}
			try {
				siguiente = driver.findElement(By.linkText("Siguiente"));
				if (siguiente.isEnabled()) {
					siguiente.click();
				}

			} catch (WebDriverException e) {
				System.out.println("Se llego al final de las paginas");
				break;
			}

			System.out.println("Terminando proceso");
		}
		exportToExcel(anuncios);
		System.out.println("Finished ");
	}

	private Anuncio getDataFromLink(WebDriver driver) {
		Anuncio anuncio = new Anuncio();
		try {
			WebElement descripccionBodega = driver.findElement(By.className("detail-description"));
			anuncio.setDescripcion(descripccionBodega.getText());
		} catch (NoSuchElementException e) {
			anuncio.setDescripcion("No se encontro descripcion");
		}

		try {
			WebElement nombreAutor = driver.findElement(By.className("author-name"));
			anuncio.setNombreAutor(nombreAutor.getText());

		} catch (NoSuchElementException e) {
			anuncio.setNombreAutor("No se encontro nombre");
		}

		try {
			WebElement telefono = driver.findElement(By.cssSelector("a[href*='tel:']"));
			anuncio.setTel(telefono.getText());

		} catch (NoSuchElementException e) {
			anuncio.setTel("No se encontro nombre");
		}

		return anuncio;
	}

	private void exportToExcel(List<Anuncio> data) throws IOException {

		FileInputStream fis = new FileInputStream(
				"C:\\Users\\Usuario\\Documents\\Automation Projects\\eclipse-workspace\\WarehouseProject\\src\\testData\\TestData.xlsx");
		FileOutputStream fos = null;
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheet("Data");
		Row row = null;
		// XSSFCell cell = null;

		for (int i = 1; i < data.size(); i++) {
			Anuncio anuncio = data.get(i);

			row = sheet.createRow(i);
			row.createCell(0).setCellValue(anuncio.getNombreAutor());
			row.createCell(1).setCellValue(anuncio.getTel());
			row.createCell(2).setCellValue(anuncio.getDescripcion());
		}

		fos = new FileOutputStream(
				"C:\\Users\\Usuario\\Documents\\Automation Projects\\eclipse-workspace\\WarehouseProject\\src\\testData\\testData.xlsx");
		workbook.write(fos);
		fos.close();
	}
}
