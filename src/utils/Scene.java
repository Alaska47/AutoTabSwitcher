package utils;

import java.awt.image.BufferedImage;

import org.opencv.core.Rect;

public class Scene {
	
	BufferedImage scene;
	Rect[] faces;
	
	public Scene(BufferedImage a, Rect[] s) {
		scene = a;
		faces = s;
	}
	
	public int getFaces() {
		return faces.length;
	}
	
	public boolean compareScene(Scene s) {
		return (faces.length == s.getFaces());
	}
	
}
