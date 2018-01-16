package test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageVivanuncios extends AbstractAnunciosFlow {

	//This link with code is bodegas and jalisco
	String baseURL = "https://www.vivanuncios.com.mx/s-bodegas/jalisco/v1c33l1013p1";

	@Override
	String getURL() {
		return baseURL;
	}

	@Override
	void hacerConsulta() {
		// TODO Auto-generated method stub

	}

	@Override
	void extraerGuardarDatos() {

		WebElement siguiente = null;
		List<Anuncio> anuncios = new ArrayList<Anuncio>();

		for (;;) {

			List<WebElement> anunciosTitles = driver.findElement(By.className("results")).findElements(By.className("title"));
			
			int results = anunciosTitles.size();
			System.out.println("Número de resultados: " + results);
			
			for (int idx = 0; idx < results; idx++) {

//				System.out.println(anunciosTitles.get(idx).getText());
				WebElement el = anunciosTitles.get(idx).findElement(By.tagName("a"));
				JavascriptExecutor jse = (JavascriptExecutor)driver;

				jse.executeScript("arguments[0].scrollIntoView()", el); 
				System.out.println("clicking " + el.getText());
				el.click();
				
				Anuncio data = getDataFromLink(driver);

//				System.out.println("Contacto: " + idx + " " + data.getNombreAutor());
				System.out.println("Descripción: " + idx + " " + data.getDescripcion());
//				System.out.println("Teléfono: " + idx + " " + data.getTel());
				anuncios.add(data);

				try {
					WebElement regresar = driver.findElement(By.className("header-back"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontro link regresar");
					break;
				}

				anunciosTitles = driver.findElement(By.className("results")).findElements(By.className("title"));
			}
		}
	}
	
	private Anuncio getDataFromLink(WebDriver driver) {
		Anuncio a = new Anuncio();
		
		WebElement desc = driver.findElement(By.className("ad-description"));
		a.setDescripcion(desc.getText());
		
		return a;
	}

}
