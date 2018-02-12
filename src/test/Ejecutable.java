package test;

import java.awt.Toolkit;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.*;

public class Ejecutable {

	public static Logger log = LogManager.getLogger(Ejecutable.class);

	
	public static void main(String[] args) throws Exception {
		testClass testClass = new testClass();
		testClass.testSimple();
	}
}