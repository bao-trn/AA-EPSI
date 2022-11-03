package geometry; /****************************************************************************
 * @author Josaphat Mayuba Ndele	et Andres Garcia Cotton					*					*
 * Les programmes permet a faire de dessin de forme rectanglulaire et 		*
 * les ellipses	on peut l'enregistre et ouvrir le meme fichier.				*											*
 * 																			*
 ****************************************************************************/
import enums.GeoFormType;
import states.NotSelectedState;
import states.interfaces.ShapeState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.Serializable;

public class FormGeo implements Serializable {

	private ShapeState selected = new NotSelectedState();
	private static Color currentColor = Color.BLACK;
	private Color color;
	private transient RectangularShape rectangularShape;


	public FormGeo(GeoFormType td, double x, double y, double w, double h) {
		if (td == GeoFormType.RECT) {
			this.rectangularShape = new Rectangle2D.Double(x, y, w, h);
		} else if (td == GeoFormType.ELLIPSE) {
			this.rectangularShape = new Ellipse2D.Double(x, y, w, h);
		}

	}
	public FormGeo(GeoFormType td) {
		this(td, 0, 0, 0, 0);
	}

	public static void setCurrentColor(Color c) {
		currentColor = c;
	}

	public static Color getCurrentColor() {
		return currentColor;
	}

	public void dessine(Graphics2D g) {
		g.setPaint(color);
		g.fill(rectangularShape);

	}

	public void moveBy(double dx, double dy) {
		double x = this.rectangularShape.getX();
		double y = this.rectangularShape.getY();
		double w = this.rectangularShape.getWidth();
		double h = this.rectangularShape.getHeight();
		this.rectangularShape.setFrame(x + dx, y + dy, w, h);
	}

	public void setFrameFromDiagonal(Point2D lastPointPress, Point2D p) {
		this.rectangularShape.setFrameFromDiagonal(lastPointPress, p);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ShapeState isSelected() {
		return selected;
	}

	public void setSelected(ShapeState state) {
		this.selected = state;
	}

	public RectangularShape getRectangularShape() {
		return rectangularShape;
	}

	public void setRectangularShape(RectangularShape rectangularShape) {
		this.rectangularShape = rectangularShape;
	}

}