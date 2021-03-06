
package controller;
/**
 * COURS_GTI_410 : 
 * LABORATOIRE_1
 * 
 * EQUIPE : 
 * 			Idriss Aissou AISI01088901
 * 			Nicolas Hubert HUBN30099004
 */

/**
 * Classe qui permet de convertir du format RGB au HSV
 *
 */
public class ConversionRGBversHSV {
	float H;
	float S;
	float V;
	float rouge;
	float vert;
	float bleu;
	float MAX;
	float MIN;
	
	/**
	 * Constructeur par defaut de la classe ConversionRGBversHSV
	 */
	public ConversionRGBversHSV(){}
	
	/**
	 * Methode permettant de convertir le RGB vers HSV
	 * Source de la m�thode de calcul
	 * http://www.rapidtables.com/convert/color/rgb-to-hsv.htm
	 * @param rougeR (Rouge)
	 * @param vertR	 (Vert)
	 * @param bleuR	 (Bleu)
	 * @return double [] tableau hsv
	 */
	public double[] RGBversHSV(int rougeR,int vertR,int bleuR){
		float H = 0;
		float S = 0;
		float V = 0;
		float MAX;
		float MIN;
		double[] TSV_TAB = new double[3];
				
		MAX = Math.max(rougeR, Math.max(vertR,bleuR));
		MIN = Math.min(rougeR, Math.min(vertR, bleuR));
		
		
		//HUE CALCUL (TEINTE)
		if(rougeR == MAX){
			H= (60 * ((vertR-bleuR)/(MAX-MIN)+360))%360;
		}
		
		if(vertR == MAX){
			H= 60 * ((bleuR-rougeR)/(MAX-MIN)) + 120;
		}
		if(bleuR == MAX){
			H= 60 * ((rougeR-vertR)/(MAX-MIN)) + 240;
		}
		if( H < 0 ){
			  H += 360;
		}
	      
		if(MAX == MIN){
			H= 0;
		}
		
		//SATURATION
		if(MAX==0){
			S=0;
		}else if(MAX!=0){
			S=1-(MIN/MAX);
		}
		
		//VALEUR
		V=Math.max(rougeR/255, Math.max(vertR/255,bleuR/255));
		TSV_TAB[0]=H;
		TSV_TAB[1]=S;
		TSV_TAB[2]=V;
		
			
		return TSV_TAB;
	}
	
	
}

