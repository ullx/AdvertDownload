package test;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public class Utils {

	public static boolean retryingFind(WebDriver driver, By by) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 10) {
            try {
                driver.findElement(by);
                result = true;
                break;
            } catch(StaleElementReferenceException e) {
            } catch(WebDriverException e) {
            }
            attempts++;
        }
        return result;
}
	
}
