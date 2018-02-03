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
		
		try {
			URL url = ClassLoader.getSystemResource("censor-beep-2.wav");
			Clip clip = AudioSystem.getClip();
			// getAudioInputStream() also accepts a File or InputStream
			AudioInputStream ais = AudioSystem.getAudioInputStream(url);
			clip.open(ais);
			clip.loop(10);
			
		} catch (Exception e) {
			log.info("Error al lanzar el mensaje para resolver captcha");
		}
		
		
		
		
		testClass testClass = new testClass();
		testClass.testSimple();
	}
}
