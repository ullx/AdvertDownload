package test;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageLamudi extends AbstractAnunciosFlow {

	String baseUrl = "https://www.lamudi.com.mx/";
	String estado = null;
	
	@Override
	String getURL() {
		return baseUrl;
	}

	@Override
	void hacerConsulta(BusquedaTipo busquedaTipo, String estado) {
		this.estado = estado;
		
		//Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36
		
		
		driver.findElement(By.xpath("//*[@id=\"search-widget-tabs\"]/div[2]")).click();
		
		switch(busquedaTipo) {
		case BODEGAS: driver.findElement(By.id("searchbar")).sendKeys("bodega");
			break;
		case CASAS: driver.findElement(By.id("searchbar")).sendKeys(""); 
			break;
		case OFICINAS: driver.findElement(By.id("searchbar")).sendKeys("");
			break;
		case TERRENOS: driver.findElement(By.id("searchbar")).sendKeys("");
			break;
		default: driver.findElement(By.id("searchbar")).sendKeys("bodega");
			break;
		}
		
		// para bodegas se mete texto en el buscador
		if (busquedaTipo != BusquedaTipo.BODEGAS) {
			driver.findElement(By.id("lnkSelectKit-display")).click();
			WebElement dropDownCategories = driver.findElement(By.className("selectkit-choices"));
			List<WebElement> options = dropDownCategories.findElements(By.className("selectkit-choice"));
			
			
			for (WebElement opt : options) {
				System.out.println(opt.getText());
				if (opt.getText().contains(busquedaTipo.toString().toLowerCase())) {
					opt.click();
				}
			}
		}

		WebElement button = driver.findElement(By.id("btnSubmitSearch"));    //.click();
		System.out.println(button.getText());
		 
		 
		 
		 try {
			Thread.sleep(10000);
		} catch (Exception e) {
		}
		 
		 
		 throw new RuntimeException();
	}
	
	@Override
	void extraerGuardarDatos() throws Exception {
		
		List<Anuncio> anuncios = new ArrayList<Anuncio>();
		int countPagina = 1;
		
		do {
			List<WebElement> anunciosTitles = driver.findElement(By.id("pnlPropertiesListPanel")).findElements(By.className("highlight-box"));
			int results = anunciosTitles.size();
			
			System.out.println("Pagina: " + countPagina + " Número de resultados" + results);
			
			for (int idx = results; idx < results; idx++) {

				WebElement anuncioContainer = anunciosTitles.get(idx);
						
				JavascriptExecutor jse = (JavascriptExecutor) driver;
				jse.executeScript("arguments[0].scrollIntoView()", anuncioContainer); 
				System.out.println("clicking " + anuncioContainer.getText());
				
				
				anuncioContainer.findElement(By.className("listing-info")).findElement(By.tagName("a")).click();
				
				Anuncio data = getDataFromLink(driver);

				System.out.println("Descripción: " + idx + " " + data.getDescripcion());
				anuncios.add(data);

				try {
					WebElement regresar = driver.findElement(By.className("header-back"));
					regresar.click();
				} catch (NoSuchElementException e) {
					System.out.println("No se encontro link regresar");
					break;
				}

				anunciosTitles = driver.findElement(By.id("pnlPropertiesListPanel")).findElements(By.className("highlight-box"));
			}
			countPagina++;
		}while (morePagesExists());
		
	}
	
	private boolean morePagesExists() {
		
		try {
			WebElement pagination = driver.findElement(By.className("Pagination-item--arrow"));
			pagination.click();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private Anuncio getDataFromLink(WebDriver driver) {
		Anuncio a = new Anuncio();
		
		//show more link
		driver.findElement(By.className("ViewMore-trigger-text")).click();;
		a.setDescripcion(driver.findElement(By.className("Description")).getText());
		//Reveal phone number
		driver.findElement(By.partialLinkText("Mostrar el tel")).click();
		driver.findElement(By.id("request_phone_phone_input")).sendKeys("33 33 33 33 33");
		driver.findElement(By.id("js-requestPhoneBtn")).click();
		
		
		WebElement telefono = driver.findElement(By.className("AgentBoxSuccess-agent-phone"));
		
		WebDriverWait wait = new WebDriverWait(driver, 5000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("AgentBoxSuccess-agent-phone")));
		a.setTelefono(telefono.getText());
		
		driver.findElement(By.className("js-viewerContainerRequestPhoneCloseBtn\"")).click();
		
		return a;
	}

}
