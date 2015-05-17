package controller;

import model.Pixel;

public class ConversionRGBversCMYK {
	
	public static int getC(Pixel pixel){
		return (1-pixel.getRed()-getK(pixel))/1-getK(pixel);
	}
	public static int getM(Pixel pixel){
		return (1-pixel.getGreen()-getK(pixel))/1-getK(pixel);
	}
	public static int getY(Pixel pixel){
		return (1-pixel.getBlue()-getK(pixel))/1-getK(pixel);
	}
	public static int getK(Pixel pixel){
		return 1-Math.max(pixel.getRed(), Math.max(pixel.getBlue(), pixel.getGreen()));
	}
	
}

