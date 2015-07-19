/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package controller;

import java.awt.event.MouseEvent;
import java.util.List;

import view.Application;
import view.CurvesPanel;
import model.BezierCurveType;
import model.BsplineCurveType;
import model.ControlPoint;
import model.Curve;
import model.CurvesModel;
import model.DocObserver;
import model.Document;
import model.HermiteCurveType;
import model.PolylineCurveType;
import model.Shape;

/**
 * <p>Title: Curves</p>
 * <p>Description: (AbstractTransformer)</p>
 * <p>Copyright: Copyright (c) 2004 S�bastien Bois, Eric Paquette</p>
 * <p>Company: (�TS) - �cole de Technologie Sup�rieure</p>
 * @author unascribed
 * @version $Revision: 1.9 $
 */
public class Curves extends AbstractTransformer implements DocObserver {
		
	/**
	 * Default constructor
	 */
	public Curves() {
		Application.getInstance().getActiveDocument().addObserver(this);
	}	

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_CURVES; }
	
	public void activate() {
		firstPoint = true;
		Document doc = Application.getInstance().getActiveDocument();
		List selectedObjects = doc.getSelectedObjects();
		boolean selectionIsACurve = false; 
		if (selectedObjects.size() > 0){
			Shape s = (Shape)selectedObjects.get(0);
			if (s instanceof Curve){
				curve = (Curve)s;
				firstPoint = false;
				cp.setCurveType(curve.getCurveType());
				cp.setNumberOfSections(curve.getNumberOfSections());
			}
			else if (s instanceof ControlPoint){
				curve = (Curve)s.getContainer();
				firstPoint = false;
			}
		}
		
		if (firstPoint) {
			// First point means that we will have the first point of a new curve.
			// That new curve has to be constructed.
			curve = new Curve(100,100);
			setCurveType(cp.getCurveType());
			setNumberOfSections(cp.getNumberOfSections());
		}
	}
    
	/**
	 * 
	 */
	protected boolean mouseReleased(MouseEvent e){
		int mouseX = e.getX();
		int mouseY = e.getY();

		if (firstPoint) {
			firstPoint = false;
			Document doc = Application.getInstance().getActiveDocument();
			doc.addObject(curve);
		}
		ControlPoint cp = new ControlPoint(mouseX, mouseY);
		curve.addPoint(cp);
				
		return true;
	}

	/**
	 * @param string
	 */
	public void setCurveType(String string) {
		if (string == CurvesModel.BEZIER) {
			curve.setCurveType(new BezierCurveType(CurvesModel.BEZIER));
		} else if (string == CurvesModel.LINEAR) {
			curve.setCurveType(new PolylineCurveType(CurvesModel.LINEAR));
		} else if (string == CurvesModel.HERMITE) {
			curve.setCurveType(new HermiteCurveType(CurvesModel.HERMITE));
		} else if (string == CurvesModel.BSPLINE) {
			curve.setCurveType(new BsplineCurveType(CurvesModel.BSPLINE));
		}
		else {
			System.out.println("Curve type [" + string + "] is unknown.");
		}
	}
	
	public void alignControlPoint() {
		if (curve != null) {
			Document doc = Application.getInstance().getActiveDocument();
			List selectedObjects = doc.getSelectedObjects(); 
			if (selectedObjects.size() > 0){
				Shape s = (Shape)selectedObjects.get(0);
				if (curve.getShapes().contains(s)){
					int controlPointIndex = curve.getShapes().indexOf(s);
					System.out.println("Try to apply G1 continuity on control point [" + controlPointIndex + "]");
					
					//Recuperation des points pour le calcul
					
					ControlPoint pointReference = (ControlPoint) s;
					ControlPoint pointAvant = (ControlPoint) curve.getShapes().get(controlPointIndex-1);
					ControlPoint pointApres = (ControlPoint) curve.getShapes().get(controlPointIndex+1) ; 
									
					//Calcul d1 et d2
					//D1
					double x = (pointReference.getCenter().getX()-pointAvant.getCenter().getX());
					double y = (pointReference.getCenter().getY()-pointAvant.getCenter().getY());
					double d1 = Math.sqrt(x*x+y*y);
					//D2
					double x2 = (pointApres.getCenter().getX()-pointReference.getCenter().getX());
					double y2 = (pointApres.getCenter().getY()-pointReference.getCenter().getY());
					double d2 = Math.sqrt(x2*x2+y2*y2);
					
					//Calcul du nouveau Point
					ControlPoint nouveauPointApres = new  ControlPoint(
							((-(pointAvant.getCenter().getX()-pointReference.getCenter().getX())/d1)*d2)+(pointReference.getCenter().getX()),
							((-(pointAvant.getCenter().getY()-pointReference.getCenter().getY())/d1)*d2)+pointReference.getCenter().getY());
					
					pointApres.setCenter(nouveauPointApres.getCenter().getX(), nouveauPointApres.getCenter().getY());
					pointApres.notifyObservers();
				
					System.out.println("*********************************************");
					System.out.println(" D1 "+d1);
					System.out.println(" D2 " + d2);
					System.out.println("*********************************************");
					System.out.println(" x Point Avant "+pointAvant.getCenter().getX());
					System.out.println(" y Point AVant "+pointAvant.getCenter().getY());
					System.out.println("*********************************************");
					System.out.println(" x Point REF "+pointReference.getCenter().getX());
					System.out.println(" y Point REF "+pointReference.getCenter().getY());
					System.out.println("*********************************************");
					System.out.println(" x Point apres "+pointApres.getCenter().getX());
					System.out.println(" y Point apres "+pointApres.getCenter().getY());
					System.out.println("*********************************************");
					System.out.println(" NOUVEAU x Point apres "+nouveauPointApres.getCenter().getX());
					System.out.println(" NOUVEAU y Point apres "+nouveauPointApres.getCenter().getY());
					
				}
			}
			
		}
	}
	
	public void symetricControlPoint() {
		if (curve != null) {
			Document doc = Application.getInstance().getActiveDocument();
			List selectedObjects = doc.getSelectedObjects(); 
			if (selectedObjects.size() > 0){
				Shape s = (Shape)selectedObjects.get(0);
				if (curve.getShapes().contains(s)){
					int controlPointIndex = curve.getShapes().indexOf(s);
					System.out.println("Try to apply C1 continuity on control point [" + controlPointIndex + "]");
					
					ControlPoint pointReference = (ControlPoint) s;
					ControlPoint pointAvant = (ControlPoint) curve.getShapes().get(controlPointIndex-1);
					ControlPoint pointApres = (ControlPoint) curve.getShapes().get(controlPointIndex+1) ; 
					
					//Calcul du delta Avant
					double deltaAvantX = (pointAvant.getCenter().getX()-pointReference.getCenter().getX());
					double deltaAvantY = (pointAvant.getCenter().getY()-pointReference.getCenter().getY());
					
					System.out.println(" Delta X "+deltaAvantX);
					System.out.println(" Delta Y " + deltaAvantY);
					
					pointApres.setCenter(pointReference.getCenter().getX()-deltaAvantX,
							pointReference.getCenter().getY()-deltaAvantY);
					pointApres.notifyObservers();
				}
			}
			
		}
	}

	public void setNumberOfSections(int n) {
		curve.setNumberOfSections(n);
	}
	
	public int getNumberOfSections() {
		if (curve != null)
			return curve.getNumberOfSections();
		else
			return Curve.DEFAULT_NUMBER_OF_SECTIONS;
	}
	
	public void setCurvesPanel(CurvesPanel cp) {
		this.cp = cp;
	}
	
	/* (non-Javadoc)
	 * @see model.DocObserver#docChanged()
	 */
	public void docChanged() {
	}

	/* (non-Javadoc)
	 * @see model.DocObserver#docSelectionChanged()
	 */
	public void docSelectionChanged() {
		activate();
	}

	private boolean firstPoint = false;
	private Curve curve;
	private CurvesPanel cp;
}
