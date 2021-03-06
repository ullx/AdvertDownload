package test;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class Utils {
	
	public static Logger log = LogManager.getLogger(AbstractAnunciosFlow.class);

	public static boolean retryingFind(WebDriver driver, By by) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 10) {
			try {
				driver.findElement(by);
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
			} catch (WebDriverException e) {
			}
			attempts++;
		}
		return result;
	}

	/**
	 * Reemplaza letras con acento por la misma letra sin acento
	 * 
	 * @param estado
	 * @return
	 */
	public static String clearAcentos(String estado) {
		String clean = estado;

		clean = clean.replaceAll("�", "a");
		clean = clean.replaceAll("�", "e");
		clean = clean.replaceAll("�", "i");
		clean = clean.replaceAll("�", "o");
		clean = clean.replaceAll("�", "u");
		clean = clean.replaceAll("�", "u");

		return clean;

	}

	public static boolean retryingFindClick(WebDriver driver, By by) {
		boolean result = false;
		int attempts = 0;
		while (attempts < 2) {
			try {
				driver.findElement(by).click();
				result = true;
				break;
			} catch (StaleElementReferenceException e) {
				log.debug(e);
			} catch (WebDriverException e) {
				log.debug(e);
			}
			attempts++;
		}
		return result;
	}

	public static String cleanToWriteCSV(String strToClean) {
		String tmp = strToClean.replaceAll("\r", " ").replaceAll("\n", " ");
		tmp = tmp.replaceAll(",", "");
		return tmp;
	}
	
	public static void openLinkInSamePage(WebDriver driver, String linkToOpen) {
		System.out.println("driver.getCurrentUrl() " + driver.getCurrentUrl());
		String currentUrl = driver.getCurrentUrl();
		String host = driver.getCurrentUrl().substring(0, currentUrl.indexOf("/", 8));
		
		if(! linkToOpen.startsWith("/")) {
			linkToOpen = "/".concat(linkToOpen);
		}
		
		System.out.println("new host " + host);
		System.out.println("new url " + host.concat(linkToOpen));
		driver.get(host.concat(linkToOpen));
	}
}
