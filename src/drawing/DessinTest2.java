package drawing; /****************************************************************************
 * @author Josaphat Mayuba Ndele	et Andres Garcia Cotton					*					*
 * Les programmes permet a faire de dessin de forme rectanglulaire et 		*
 * les ellipses	on peut l'enregistre et ouvrir le meme fichier.				*											*
 * 																			*
 ****************************************************************************/

import enums.GeoFormType;
import states.ColorMenuItem;
import states.DeleteMenuItem;
import states.NoneMenuItem;
import states.SelectAllMenuItem;
import states.interfaces.MenuState;
import utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.*;

public class DessinTest2 {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			DessinFrame application = new DessinFrame("paint drawing");
			application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			application.setSize(800, 600);
			application.setVisible(true);

		});
	}
}

class DessinFrame extends JFrame {
	private final DessinPanel2 PanelDesign;
	private final ColorComponent couleur;

	public DessinFrame(String titre) {
		setTitle(titre);
		
		
		// PANNEAU DE DESSIN
		PanelDesign = new DessinPanel2();
		PanelDesign.setCouleur(Color.BLACK);
		PanelDesign.setBackground(Color.WHITE);
		this.add(PanelDesign, BorderLayout.CENTER);
		this.add(new JLabel("Clic et drag pour dessiner"), BorderLayout.SOUTH);

		// construction du menu
		JMenuBar fileMenu = new JMenuBar();
		setJMenuBar(fileMenu);
		JMenu fichierMenu = new JMenu("Fichier");
		fileMenu.add(fichierMenu);
		JMenuItem openItem = new JMenuItem("Ouvrir");
		JMenuItem sauvegarderItem = new JMenuItem("Enregistrer");
		
		
		// TOUCHE RELIER AU MENU
		openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		sauvegarderItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		
		
		// LES ACTION POUR SOUS MENU OUVRIR ET SAUVERGARDER
		openItem.addActionListener(x -> {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					File file2 = fileChooser.getSelectedFile();
					FileInputStream ouvre = new FileInputStream(file2);
					ObjectInputStream ouvrir = new ObjectInputStream(ouvre);
					FileUtils.readInfo(ouvrir, PanelDesign.getFormesGeo());

				} catch (IOException | ClassNotFoundException e) {
					System.exit(1);
				}
			}
		});
		sauvegarderItem.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fileChooser.getSelectedFile();
					FileOutputStream enr = new FileOutputStream(file);
					ObjectOutputStream enregistre = new ObjectOutputStream(enr);

					FileUtils.save(enregistre, PanelDesign.getFormesGeo());

				} catch (IOException io) {
					System.exit(1);
				}
			}
		});

		// affche menu ouvrir
		fichierMenu.add(openItem);
		fichierMenu.add(sauvegarderItem);
		
		
		// SEPARATEUR
		fichierMenu.addSeparator();

		// AUTRE SOUS MENU,LEURS TOUCHES ET LEURS ACTIONS
		JMenuItem effacerItem = new JMenuItem("Effacer");
		fichierMenu.add(effacerItem);
		effacerItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
		effacerItem.addActionListener(e -> PanelDesign.clearAll());

		JMenu selectionMenu = new JMenu("Selection");
		fileMenu.add(selectionMenu);
		
		MenuState selectAllKey = new SelectAllMenuItem();
		MenuState noneKey = new NoneMenuItem();
		MenuState colorKey = new ColorMenuItem();
		MenuState deleteKey = new DeleteMenuItem();
		selectionMenu.add(selectAllKey.setupKey(PanelDesign));
		selectionMenu.add(noneKey.setupKey(PanelDesign));
		selectionMenu.add(colorKey.setupKey(PanelDesign));
		selectionMenu.add(deleteKey.setupKey(PanelDesign));

		// PANNEAU DE BOUTON RADIO
		/**
		 * @param laBarreOutils
		 *            laBarreOutils contient des boutons qui permet a
		 *            l'utilisation ou la selecion de couleur et des different
		 *            forme a dessiner
		 * 
		 */
		JPanel laBarreOutils = new JPanel();

		String chemin_img = System.getProperty("user.dir") + File.separator
				+ "images" + File.separator;
		// *BOUTON RADIO
		JRadioButton ellipse = new JRadioButton(new ImageIcon(chemin_img
				+ "ellipse.png"));
		ellipse.setSelectedIcon(new ImageIcon(chemin_img + "ellipseSelect.png"));

		JRadioButton rectangle = new JRadioButton(new ImageIcon(chemin_img
				+ "rectangle.png"));
		rectangle.setSelectedIcon(new ImageIcon(chemin_img
				+ "rectangleSelect.png"));

		laBarreOutils.add(rectangle);
		laBarreOutils.add(ellipse);
		couleur = new ColorComponent(Color.BLACK);
		couleur.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				Color defeaultColor = getBackground();
				Color selected = JColorChooser.showDialog(DessinFrame.this,
						"Palette de Couleur", defeaultColor);

				if (selected != null) {
					couleur.setColor(selected);
					PanelDesign.setCouleur(selected);
				}
			}
		});

		// on doit cr�er un groupe de boutons pour pouvoir cocher seulment un
		// seul bouton et non les deux boutonr

		ButtonGroup group = new ButtonGroup();
		group.add(ellipse);
		group.add(rectangle);

		rectangle.setSelected(true);
		rectangle.addItemListener(new RadioButtonHandler("Rectangle"));
		ellipse.addItemListener(new RadioButtonHandler("Ellipse"));

		laBarreOutils.add(couleur);
		laBarreOutils.setPreferredSize(new Dimension(54, 70));

		laBarreOutils.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));

		this.add(laBarreOutils, BorderLayout.NORTH);

	}

	private class RadioButtonHandler implements ItemListener {
		private String name;

		public RadioButtonHandler(String s) {
			name = s;
		}

		public void itemStateChanged(ItemEvent e) {
			if (name.equals("Rectangle"))
				PanelDesign.setTypeDessin(GeoFormType.RECT);
			if (name.equals("Ellipse"))
				PanelDesign.setTypeDessin(GeoFormType.ELLIPSE);

		}

	}

	// TAILLE AFFICHAGE
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 400;

}

class ColorComponent extends JComponent {
	private Color c;
	private final Rectangle2D rect;

	public ColorComponent(Color c) {
		super();
		this.c = c;
		this.rect = new Rectangle2D.Double(5, 5, 15, 15);
		this.setPreferredSize(new Dimension(20, 20));
		this.setMaximumSize(new Dimension(25, 25));
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setPaint(c);
		g2.fill(rect);

	}

	public Color getColor() {
		return c;
	}

	public void setColor(Color c) {
		this.c = c;
		repaint();
	}
}
