package test;
import org.apache.logging.log4j.*;

public class Ejecutable {

	public static Logger log = LogManager.getLogger(Ejecutable.class);
	
	public static void main(String[] args) throws Exception {
		
		log.info("some test");
		
		if(false) {
		testClass testClass = new testClass();
		testClass.testSimple();
		}
	}
}
