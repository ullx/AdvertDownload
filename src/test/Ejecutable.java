package test;

import org.apache.logging.log4j.*;

//import com.sun.istack.internal.logging.Logger;

public class Ejecutable {

	public static Logger log = LogManager.getLogger(Ejecutable.class);
	
	public static void main(String[] args) throws Exception {
		
		testClass testClass = new testClass();
		testClass.testSimple();
	}
}
