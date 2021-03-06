package controller;

import model.ImageDouble;
import model.ImageX;
import model.Pixel;
import model.PixelDouble;
/**
 * COURS_GTI_410 : 
 * LABORATOIRE_3
 * 
 * EQUIPE : 
 * 			Idriss Aissou AISI01088901
 * 			Nicolas Hubert HUBN30099004
 */

/**
 *Classe qui permet de convertir notre image double en imageX
 *Elle permet aussi d'ajuster les contrastes de l image en appliquant un normalisation
 *sur l ensemble des pixels par l utilisation de la valeur absolue et le calcul du min et max des pixels
 *Calcul : (Math.abs(cone)+minMax[0])*(255/minMax[1]);
 *
 */
public class ImageClampAbsNormalizeTo255 extends ImageClampStrategy {

	double[] minMax;
	public ImageX convert(ImageDouble image) {
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		ImageX newImage = new ImageX(0, 0, imageWidth, imageHeight);
		PixelDouble curPixelDouble = null;
		minMax= getMinMax(image);
		
		newImage.beginPixelUpdate();
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				curPixelDouble = image.getPixel(x, y);
				double ROUGE = curPixelDouble.getRed();
				double VERT = curPixelDouble.getGreen();
				double BLEU = curPixelDouble.getBlue();
				double ALPHA = curPixelDouble.getAlpha();

				newImage.setPixel(
						x,
						y,
						new Pixel(
								(int) (normalisation(ROUGE)),
								(int) (normalisation(VERT)),
								(int) (normalisation(BLEU)),
								(int) (ALPHA)));
			}
		}

		newImage.endPixelUpdate();
		return newImage;

	}

	public double[] getMinMax(ImageDouble image) {
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		PixelDouble curPixelDouble = null;
		double MIN = 255;
		double MAX = 0;

		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				curPixelDouble = image.getPixel(x, y);

				// CALCUL DU MIN
				if (MIN > curPixelDouble.getRed()) {
					MIN = curPixelDouble.getRed();
				}
				if (MIN > curPixelDouble.getGreen()) {
					MIN = curPixelDouble.getGreen();
				}
				if (MIN > curPixelDouble.getBlue()) {
					MIN = curPixelDouble.getBlue();
				}
				// CALCUL DU MAX
				if (MAX < curPixelDouble.getRed()) {
					MAX = curPixelDouble.getRed();
				}
				if (MAX < curPixelDouble.getGreen()) {
					MAX = curPixelDouble.getGreen();
				}
				if (MAX < curPixelDouble.getBlue()) {
					MAX = curPixelDouble.getBlue();
				}
			}
		}
		double[] tab = new double[2];
		tab[0] = MIN;
		tab[1] = MAX;
		System.out.println("MIN :"+MIN +" "+"MAX :"+MAX);
		return tab;

	}

	double normalisation(double cone) {
		double calcul;
		calcul= (Math.abs(cone)+minMax[0])*(255/minMax[1]);
		return calcul;
	}

}
