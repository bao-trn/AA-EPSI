package drawing; /****************************************************************************
 * @author Josaphat Mayuba Ndele	et Andres Garcia Cotton					*					*
 * Les programmes permet a faire de dessin de forme rectanglulaire et 		*
 * les ellipses	on peut l'enregistre et ouvrir le meme fichier.				*											*
 * 																			*
 ****************************************************************************/
import geometry.FormGeo;
import constants.GeoConstants;
import enums.GeoFormType;
import states.NotSelectedState;
import states.SelectedState;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.RectangularShape;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JPanel;

public class DessinPanel2 extends JPanel {
	public List<FormGeo> getFormesGeo() {
		return formesGeo;
	}

	private final ArrayList<FormGeo> formesGeo;
	private final ArrayList<FormGeo> selectedFormesGeo;
	private FormGeo geoForm;
	private transient Point2D lastPointPress;
	private FormGeo lastFormGeo = null;

	private int allSelected
			;

	private Color color;

	private GeoFormType geoFormType = GeoFormType.RECT;

	private int touche;

	public DessinPanel2() {
		formesGeo = new ArrayList<>();
		selectedFormesGeo = new ArrayList<>();
		geoForm = null;
		addMouseListener(new MouseHandler());
		addMouseMotionListener(new MouseMotionHandler());
	}

	// EFFACE TOUT
	public void clearAll() {
		clearSelected();
		formesGeo.clear();
		paintComponent(getGraphics());
	}

	// EFFACE LA FIGURE SELECTIONNER
	public void clearSelected() {
		selectedFormesGeo.forEach(shape -> shape.setSelected(new NotSelectedState()));
		selectedFormesGeo.clear();
		repaint();
	}

	// EFFACE LA FIGURE CONTENU DANS LE TABLEAU
	/** selectedFormesGeo */
	public void deleteSelected() {
		selectedFormesGeo.forEach(formesGeo::remove);
		selectedFormesGeo.clear();
		repaint();
	}

	public void selectAll() {
		allSelected = 1;
		selectedFormesGeo.clear();
		selectedFormesGeo.addAll(formesGeo);
		selectedFormesGeo.forEach(shape -> shape.setSelected(new SelectedState()));
		repaint();
	}

	public void coloreSelected() {
		selectedFormesGeo.forEach(shape -> shape.setColor(FormGeo.getCurrentColor()));
		paintComponent(getGraphics());
	}

	public void setCouleur(Color c) {
		FormGeo.setCurrentColor(c);
	}

	public void setTypeDessin(GeoFormType type) {
		geoFormType = type;
	}

	public FormGeo find(Point2D p) {
		for (FormGeo f : formesGeo) {
			if (f.getRectangularShape().contains(p)) {
				return f;
			}
		}
		return null;
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (FormGeo f : formesGeo) {
			f.dessine(g2);
			if (geoForm != null) {
				lightSquares(g2, geoForm.getRectangularShape());
				repaint();
			}
			if (allSelected == 1) {// TOUTSELEC EST UTILISER POUR
				for (FormGeo selec : selectedFormesGeo) {
					lightSquares(g2, selec.getRectangularShape());
					repaint();
				}
			}

		}
	}
	public void lightSquares(Graphics2D g2D, RectangularShape rectangularShape) {
		double x = rectangularShape.getX();
		double y = rectangularShape.getY();
		double w = rectangularShape.getWidth();
		double h = rectangularShape.getHeight();
		g2D.setColor(Color.BLACK);

		g2D.fill(new Rectangle.Double(x + w * 0.5 - 3.0, y - 3.0, 6.0, 6.0));

		g2D.fill(new Rectangle.Double(x - 3.0, y + h * 0.5 - 3.0, 6.0, 6.0));
		g2D.fill(new Rectangle.Double(x + w - 3.0, y + h * 0.5 - 3.0, 6.0, 6.0));

		g2D.fill(new Rectangle.Double(x + w * 0.5 - 3.0, y + h - 3.0, 6.0, 6.0));

	}


	// LORS LA SOURIS EST PRESSER

	private class MouseHandler extends MouseAdapter {
		public void mousePressed(MouseEvent event) {

			Point p = event.getPoint();
			geoForm = find(p);
			lastPointPress = p;
			lastFormGeo = null;

		}

		// LORS DE CLIQUE DE LA SOURIS
		public void mouseClicked(MouseEvent event) {
			allSelected = 0;
			Point p = event.getPoint();
			double x = p.getX();
			double y = p.getY();
			if (geoForm == null && lastFormGeo == null) {
				FormGeo f = new FormGeo(geoFormType, x - GeoConstants.SQUARE_SIZE / 2, y
						- GeoConstants.SQUARE_SIZE / 2, GeoConstants.SQUARE_SIZE, GeoConstants.SQUARE_SIZE);
				f.setColor(FormGeo.getCurrentColor());
				formesGeo.add(f);
			} else {
				if (!selectedFormesGeo.contains(geoForm)) {
					geoForm.setSelected(new SelectedState());
					selectedFormesGeo.add(geoForm);
				}
			}
			repaint();
		}
	}

	// Lorsque on appui sur Maj(shift) enfonce

	private class MouseMotionHandler implements MouseMotionListener {
		// MOUVEMENT DE LA SOURIS
		public void mouseMoved(MouseEvent event) {
			if (find(event.getPoint()) == null)
				setCursor(Cursor.getDefaultCursor());
			else
				setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

		}

		// DEPLACEMENT DE L'ELEMENT AVEC LA SOURIS
		public void mouseDragged(MouseEvent event) {

			Point p = event.getPoint();
			if (geoForm == null) {
				if (lastFormGeo == null) {
					lastFormGeo = new FormGeo(geoFormType);
					lastFormGeo.setColor(FormGeo.getCurrentColor());
					formesGeo.add(lastFormGeo);
				}
				lastFormGeo.setFrameFromDiagonal(lastPointPress, p);
			} else {
				double dx = p.getX() - lastPointPress.getX();
				double dy = p.getY() - lastPointPress.getY();

				if (!selectedFormesGeo.contains(geoForm)) {
					geoForm.moveBy(dx, dy);

				}

				// attrape l'erreur en cas ou deux object entre dans le tableau
				// DONC "si apres une selection on selectionne lautre figure"
				try {

					for (FormGeo f : selectedFormesGeo) {
						f.moveBy(dx, dy);
						if (allSelected != 1) {
							// Pour arreter la selection de plusieur fichier
							// et le deplacer en meme temp
							// en cas ou on clique appui pas la touche
							// majuscule enfoncer
							selectedFormesGeo.clear();// retour rien
															// dans le
															// tableau

						}

					}

				} catch (ConcurrentModificationException e) {
					allSelected = 0;
				}
				lastPointPress = p;
			}
			repaint();
		}
	}


}