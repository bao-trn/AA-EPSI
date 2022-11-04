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
	private final DessinPanel2 panelDesign;
	private final ColorComponent couleur;

	public DessinFrame(String titre) {
		setTitle(titre);
		
		
		// PANNEAU DE DESSIN
		panelDesign = new DessinPanel2();
		panelDesign.setCouleur(Color.BLACK);
		panelDesign.setBackground(Color.WHITE);
		this.add(panelDesign, BorderLayout.CENTER);
		this.add(new JLabel("Clic et drag pour dessiner"), BorderLayout.SOUTH);

		// construction du menu
		JMenuBar fileMenu = new JMenuBar();
		setJMenuBar(fileMenu);
		JMenu fichierMenu = new JMenu("Fichier");
		JMenu selectionMenu = new JMenu("Selection");
		fileMenu.add(fichierMenu);
		fileMenu.add(selectionMenu);
		
		addFileMenuListener(fichierMenu, "Open", "ctrl O", true);
		addFileMenuListener(fichierMenu, "Save", "ctrl S", false);
		
		fichierMenu.addSeparator();

		addClearFileMenuListener(fichierMenu);
		
		MenuState selectAllKey = new SelectAllMenuItem();
		MenuState noneKey = new NoneMenuItem();
		MenuState colorKey = new ColorMenuItem();
		MenuState deleteKey = new DeleteMenuItem();
		selectionMenu.add(selectAllKey.setupKey(panelDesign));
		selectionMenu.add(noneKey.setupKey(panelDesign));
		selectionMenu.add(colorKey.setupKey(panelDesign));
		selectionMenu.add(deleteKey.setupKey(panelDesign));

		JPanel laBarreOutils = new JPanel();

		String imgPath = System.getProperty("user.dir") + File.separator
				+ "images" + File.separator;
		// *BOUTON RADIO
		JRadioButton ellipse = new JRadioButton(new ImageIcon(imgPath
				+ "ellipse.png"));
		ellipse.setSelectedIcon(new ImageIcon(imgPath + "ellipseSelect.png"));

		JRadioButton rectangle = new JRadioButton(new ImageIcon(imgPath
				+ "rectangle.png"));
		rectangle.setSelectedIcon(new ImageIcon(imgPath
				+ "rectangleSelect.png"));

		laBarreOutils.add(rectangle);
		laBarreOutils.add(ellipse);
		couleur = new ColorComponent(Color.BLACK);
		couleur.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				Color defeaultColor = getBackground();
				Color selected = JColorChooser.showDialog(DessinFrame.this,
						"Palette de Couleur", defeaultColor);

				if (selected != null) {
					couleur.setColor(selected);
					panelDesign.setCouleur(selected);
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

	public JMenuItem setupKey(String itemName, String keyToPress) {
		JMenuItem menuItem = new JMenuItem(itemName);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(keyToPress));
		return  menuItem;
	}
	
	public void addClearFileMenuListener(JMenu jMenu) {
		JMenuItem item = setupKey("Clear", "ctrl C");
		item.addActionListener(a -> panelDesign.clearAll());
		jMenu.add(item);
	}
	public void addFileMenuListener(JMenu jMenu, String itemName, String keyToPress, boolean input) {
		JMenuItem item = setupKey(itemName, keyToPress);
		item.addActionListener(a -> {
			JFileChooser fileChooser = new JFileChooser();
			try {
				File file = fileChooser.getSelectedFile();
				if (input && fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
					FileUtils.readInfo(objectInputStream, panelDesign.getFormesGeo());
				} else if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
					FileUtils.save(objectOutputStream, panelDesign.getFormesGeo());
				}

			} catch (IOException | ClassNotFoundException e) {
				System.exit(1);
			}

		});
		jMenu.add(item);
	}

	private class RadioButtonHandler implements ItemListener {
		private String name;

		public RadioButtonHandler(String s) {
			name = s;
		}

		public void itemStateChanged(ItemEvent e) {
			if (name.equals("Rectangle"))
				panelDesign.setTypeDessin(GeoFormType.RECT);
			if (name.equals("Ellipse"))
				panelDesign.setTypeDessin(GeoFormType.ELLIPSE);

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
