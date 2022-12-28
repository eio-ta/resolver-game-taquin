package launcher;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TreeMap;

import javax.swing.*;

import algorithmes.*;
import objects.*;

/**
 * <b>VueGUI</b> représente l'interface graphique.
 */
public class VueGUI extends JFrame implements ComponentListener {
	
	
	// VARIABLES
	
	private static final long serialVersionUID = 1L;
	private Modele m = new Modele();
	CardLayout cardLayout = new CardLayout();
	private JPanel mainPanelBold = new JPanel(cardLayout);
	int lar;
	int lon;
	private int charIndex = 0;
	private String nomPanelCourant;
	boolean robot = false;
	String algo = "astarM";
	String[] resultAlgo = null;
	
	
	// CONSTRUCTEUR
	
	/**
	 * CONSTRUCTEUR
	 * @param lar
	 * @param lon
	 */
	public VueGUI(int lar, int lon) {
		this.lar = lar;
		this.lon = lon - 50;
		this.setTitle("Jeu du TAQUIN");
		this.setAlwaysOnTop(true);
		this.getContentPane().add(mainPanelBold);
		
		repaint("accueil", jPanelAccueil());
		addComponentListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	////////////////////////////////////////////////////////////////////////
	
	// FONCTIONS QUI SONT TOUT LE TEMPS UTILISEES
	
	/**
	 * Affiche le bon JPanel sur la fenêtre Swing
	 * @param title
	 * @param p
	 */
	void repaint(String title, JPanel p) {
		this.mainPanelBold.add(title, p);
		cardLayout.show(mainPanelBold, title);
		nomPanelCourant = title;
	}
	
	/**
	 * Retourne une image sous forme de JLabel
	 * @param pathIcon
	 * @return
	 */
	private JLabel addAnImage(String pathIcon) {
		JLabel res = new JLabel("", 0);
		res.setIcon(new ImageIcon(getClass().getResource(pathIcon)));
		res.setForeground(Color.white);
		res.setFont(new Font("SansSerif", Font.BOLD, 20));
		res.setVerticalTextPosition(SwingConstants.CENTER);
		res.setHorizontalTextPosition(SwingConstants.CENTER);
		return res;
	}
	
	/**
	 * Ajoute un titre et un sous titre au JPanel
	 * @param mainPanel
	 * @param s1
	 * @param s2
	 */
	private void addTitleText(JPanel mainPanel, String s1, String s2) {
		JPanel title = new JPanel(new GridLayout(2,1));
		title.setBounds(lar/2-lar/4,lon/10,lar/2, lon/4);
		title.setOpaque(false);
		mainPanel.add(title);

		JLabel t1 = new JLabel(s1, 0);
		t1.setFont(new Font("SansSerif", Font.PLAIN, 30));
		t1.setForeground(Color.white);
		title.add(t1);
		
		JLabel t2 = new JLabel(s2, 0);
		t2.setFont(new Font("SansSerif", Font.BOLD, 60));
		t2.setForeground(Color.white);
		title.add(t2);
	}
	
	/**
	 * Ajoute trois boutons au JPanel
	 * @param contentB
	 * @param s1
	 * @param s2
	 * @param s3
	 * @param page
	 */
	private void addThreeButtons(JPanel contentB, String s1, String s2, String s3, String page) {
		String[] sButtons = {s1, s2, s3};
		for(int i=0; i<sButtons.length; i++) {
			if(! sButtons[i].equals("Null")) {
				int x = i;
				JButton b = new JButton(sButtons[i]);
				if(page.equals("ac") && i == 0) b.setBackground(new Color(122,169,92));
				else b.setBackground(new Color(84,84,84));
				b.setFont(new Font("SansSerif", Font.PLAIN, 15));
				b.setBorder(BorderFactory.createLineBorder(new Color(73,73,73), 8));
				b.setForeground(Color.white);
				if(page.equals("ac") && i == 1 && m.partieSave == null) b.setEnabled(false);
				else if(page.equals("partie") && i == 0 && (m.taille >= 5 || (m.taille == 4 && (m.difficulte == 3 || m.difficulte == 0)))) b.setEnabled(false);
				else if(page.equals("partie") && i == 0 && algo.equals("dj") && m.taille >= 4) b.setEnabled(false);
				else if(page.equals("partie") && i == 0 && algo.equals("dj") && m.taille >= 4) b.setEnabled(false);
				b.addActionListener((event) -> eventButtons(page, x));
				contentB.add(b);
			}
		}
	}
	
	/**
	 * Ajoute des évènements aux boutons de toutes les pages
	 * @param page
	 * @param ID
	 */
	private void eventButtons(String page, int ID) {
		switch(page) {
			case "ac" :
				if(ID == 0) repaint("param", jpanelModeJeu());
				else if(ID == 1) {
					m.partie = m.partieSave;
					repaint("partie", jPanelPartie());
				} else repaint("man", jPanelMan());
				break;
			case "partie" :
				if(ID == 0) {
					if(!robot) {
					charIndex = 0;
					robot = true;
					repaint("partie", jPanelPartie());
					}
				} else if(ID == 2) {
					m.recommencerPartie();
					robot = false;
					repaint("partie", jPanelPartie());
				} else repaint("param", jpanelModeJeu());
				break;
			case "win" :
				if(ID == 0) {
					m.partie = new Partie(new Plateau(m.taille, m.difficulte));
					robot = false;
					repaint("partie", jPanelPartie());
				} else if(ID == 1) repaint("param", jpanelModeJeu());
				else {
					m.partie = null;
					repaint("accueil", jPanelAccueil());
				}
				break;
			default : break;
		}
	}
	
	/**
	 * Retourne un radio bouton
	 * @param name
	 * @param b
	 * @return
	 */
	private JRadioButton makeRadioButton(String name, ButtonGroup b) {
		JRadioButton res = new JRadioButton(name);
		res.setFont(new Font("SansSerif", Font.PLAIN, 13));
		res.setForeground(Color.white);
		res.setOpaque(false);
		res.setEnabled(false);
		res.setHorizontalAlignment(0);
		b.add(res);
		return res;
	}
	
	
	////////////////////////////////////////////////////////////////////////
	
	// FONCTIONS QUI RENVOIENT DES JPANEL
	
	/**
	 * Retourne le JPanel de la page d'accueil
	 * @return
	 */
	private JPanel jPanelAccueil() {
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(73,73,73));
		addTitleText(mainPanel, "JEU DU", "TAQUIN");
		JPanel contentB = new JPanel(new GridLayout(3,1));
		contentB.setBounds(lar/2-lar/4,2*lon/5,lar/2, lon/2);
		contentB.setOpaque(false);
		mainPanel.add(contentB);
		
		addThreeButtons(contentB, "Choisir le mode de jeu", "Restaurer la partie en cours", "Options du jeu", "ac");
		
		return mainPanel;
	}
	
	/**
	 * Retourne le JPanel des paramètres
	 * @return
	 */
	private JPanel jpanelModeJeu() {
		JPanel main = new JPanel(null);
		main.setBackground(new Color(73,73,73));
		
		
		////////////////////////
		
		
		JLabel txt1 = new JLabel("MODE DE JEU", 0);
		txt1.setBounds(lar/2-lar/3,lon/22,2*lar/3, lon/12);
		txt1.setFont(new Font("SansSerif", Font.PLAIN, 17));
		txt1.setForeground(Color.white);
		txt1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		main.add(txt1);
		
		JPanel rep1 = new JPanel(new GridLayout(2,2));
		rep1.setBounds(lar/2-lar/3,3*lon/22,2*lar/3, lon/9);
		rep1.setOpaque(false);
		main.add(rep1);
		
		ButtonGroup play = new ButtonGroup();
		
		JRadioButton genere = makeRadioButton("Generer un jeu", play);
		genere.setEnabled(true);
		rep1.add(genere);
		
		JRadioButton importe =  makeRadioButton("Importer un jeu", play);
		importe.setEnabled(true);
		rep1.add(importe);
		
		JLabel path = new JLabel("", 0);
		path.setBackground(new Color(230,230,230));
		path.setFont(new Font("SansSerif", Font.PLAIN, 10));
		path.setOpaque(true);
		rep1.add(path);
		
		JButton importe3 = new JButton("Ouvrir un fichier");
		importe3.setBackground(new Color(84,84,84));
		importe3.setFont(new Font("SansSerif", Font.PLAIN, 13));
		importe3.setForeground(Color.white);
		importe3.setBorderPainted(false);
		importe3.setEnabled(false);
		rep1.add(importe3);
		
		
		////////////////////////
		
		
		JLabel txt2 = new JLabel("CHOISIR LA DIFFICULTE", 0);
		txt2.setBounds(lar/2-lar/3,6*lon/22,2*lar/3, lon/12);
		txt2.setFont(new Font("SansSerif", Font.PLAIN, 17));
		txt2.setForeground(Color.white);
		txt2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		main.add(txt2);
		
		JPanel opt2 = paramAndOptions();
		opt2.setBounds(lar/2-lar/3, 8*lon/22, 2*lar/3, lon/8);
		opt2.setOpaque(false);
		main.add(opt2);
		
		JLabel opt2bis = new JLabel("Cette fonctionnalite n'est pas disponible pour cette option.", 0);
		opt2bis.setBounds(lar/2-lar/3, 8*lon/22, 2*lar/3, lon/8);
		opt2bis.setForeground(Color.white);
		opt2bis.setFont(new Font("SansSerif", Font.PLAIN, 13));
		opt2bis.setOpaque(false);
		opt2bis.setVisible(false);
		main.add(opt2bis);
		
		
		////////////////////////
		
		
		JLabel txt3 = new JLabel("CHOISIR L'ALGORITHME", 0);
		txt3.setBounds(lar/2-lar/3,11*lon/22,2*lar/3, lon/12);
		txt3.setFont(new Font("SansSerif", Font.PLAIN, 17));
		txt3.setForeground(Color.white);
		txt3.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		main.add(txt3);
		
		JPanel rep3 = new JPanel(new GridLayout(1,3));
		rep3.setBounds(lar/2-lar/3,13*lon/22,2*lar/3, lon/12);
		rep3.setOpaque(false);
		main.add(rep3);
		
		ButtonGroup play2 = new ButtonGroup();
		
		JRadioButton op1 = makeRadioButton("Parcours en largeur", play2);
		op1.setEnabled(false);
		rep3.add(op1);
		
		JPanel rep5 = new JPanel(new GridLayout(1,2));
		rep5.setOpaque(false);
		rep3.add(rep5);
		
		JRadioButton op2 =  makeRadioButton("Dijsktra", play2);
		op2.setEnabled(false);
		rep5.add(op2);
		
		JRadioButton op3 =  makeRadioButton("A*", play2);
		op3.setEnabled(false);
		rep5.add(op3);
		
		JRadioButton op4 =  makeRadioButton("A* Manhanttan", play2);
		op4.setEnabled(false);
		rep3.add(op4);
		
		////////////////////////
		
		JLabel txt4 = new JLabel("VALIDER SON CHOIX", 0);
		txt4.setBounds(lar/2-lar/3,15*lon/22,2*lar/3, lon/12);
		txt4.setFont(new Font("SansSerif", Font.PLAIN, 17));
		txt4.setForeground(Color.white);
		txt4.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		main.add(txt4);
		
		GridLayout g2 = new GridLayout(1,3);
		g2.setHgap(lon/25);
		JPanel rep4 = new JPanel(g2);
		rep4.setBounds(lar/2-lar/3,11*lon/14,2*lar/3, lon/11);
		rep4.setOpaque(false);
		main.add(rep4);
		
		JButton b4_1 = new JButton("Annuler");
		b4_1.setBackground(new Color(84,84,84));
		b4_1.setFont(new Font("SansSerif", Font.PLAIN, 13));
		b4_1.setBorder(BorderFactory.createLineBorder(new Color(73,73,73), 8));
		b4_1.setForeground(Color.white);
		b4_1.addActionListener((event) -> {
			if(m.partie != null) {
				robot = false;
			repaint("partie", jPanelPartie());
			} else repaint("accueil", jPanelAccueil());
		});
		rep4.add(b4_1);
		
		JButton b4_2 = new JButton("Jouer");
		b4_2.setEnabled(false);
		b4_2.setBackground(new Color(84,84,84));
		b4_2.setFont(new Font("SansSerif", Font.PLAIN, 13));
		b4_2.setBorder(BorderFactory.createLineBorder(new Color(73,73,73), 8));
		b4_2.setForeground(Color.white);
		rep4.add(b4_2);
	
		JButton b4_3 = new JButton("Tester l'algo");
		b4_3.setEnabled(false);
		b4_3.setBackground(new Color(84,84,84));
		b4_3.setFont(new Font("SansSerif", Font.PLAIN, 13));
		b4_3.setBorder(BorderFactory.createLineBorder(new Color(73,73,73), 8));
		b4_3.setForeground(Color.white);
		rep4.add(b4_3);
		////////////////////////
			
			
		genere.addActionListener((event) -> {
			importe3.setEnabled(false);
			opt2.setVisible(true);
			opt2bis.setVisible(false);
			op1.setEnabled(true);
			op2.setEnabled(true);
			op3.setEnabled(true);
			op4.setEnabled(true);
			path.setText("");
		});
		
		importe.addActionListener((event) -> {
			importe3.setEnabled(true);
			opt2.setVisible(false);
			opt2bis.setVisible(true);
			op1.setEnabled(false);
			op2.setEnabled(false);
			op3.setEnabled(false);
			op4.setEnabled(false);
		});
		
		importe3.addActionListener((event) -> {
			JFileChooser c = new JFileChooser();
			c.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int rval = c.showOpenDialog(this);
			if(rval == JFileChooser.APPROVE_OPTION) {
				if(!c.getSelectedFile().getAbsolutePath().endsWith(".txt")) {
					path.setText("Fichier incompatible");
				} else {
					path.setText(c.getSelectedFile().getAbsolutePath());
					op1.setEnabled(true);
					op2.setEnabled(true);
					op3.setEnabled(true);
					op4.setEnabled(true);

				}
			}
		});
		
		op1.addActionListener((event) -> {
			b4_2.setEnabled(true);
			b4_3.setEnabled(true);
			this.algo = "pl";
		});
		
		op2.addActionListener((event) -> {
			b4_2.setEnabled(true);
			b4_3.setEnabled(true);
			this.algo = "dj";
		});
		
		op3.addActionListener((event) -> {
			b4_2.setEnabled(true);
			b4_3.setEnabled(true);
			this.algo = "astar";
		});
		
		op4.addActionListener((event) -> {
			b4_2.setEnabled(true);
			b4_3.setEnabled(true);
			this.algo = "astarM";
		});
		
		
		b4_2.addActionListener((event) -> {
			if(genere.isSelected()) {
				m.partie = new Partie(new Plateau(m.taille, m.difficulte));
				robot = false;
				repaint("partie", jPanelPartie());
			} else {
				Plateau p = m.donneUnPlateauDunFichierTxt(path.getText());
				if(p == null) {
					path.setText("Fichier non reconnu");
					b4_2.setEnabled(false);
					b4_3.setEnabled(false);
				} else {
					Partie p1 = new Partie(p);
					m.partie = p1;
					robot = false;
					repaint("partie", jPanelPartie());
				}
			}
			
		});
		
		b4_3.addActionListener((event) -> {
			if(genere.isSelected()) {
				m.partie = new Partie(new Plateau(m.taille, m.difficulte));
				repaint("algo", jPanelAlgo());
			} else {
				Plateau p = m.donneUnPlateauDunFichierTxt(path.getText());
				if(p == null) {
					path.setText("Fichier non reconnu");
					b4_2.setEnabled(false);
					b4_3.setEnabled(false);
				} else {
					Partie p1 = new Partie(p);
					m.partie = p1;
					robot = false;
					repaint("algo", jPanelAlgo());
				}
			}
			
		});
	
		
		////////////////////////
		return main;
	}
	
	/**
	 * Retourne le jpanel pour tester les algorithmes
	 * @return
	 */
	private JPanel jPanelAlgo() {
		JPanel main = new JPanel(null);
		main.setBackground(new Color(73,73,73));
		
		partieA(main);
	
		// TRAIT DE SEPARATION
		JLabel rep = new JLabel(" ");
		rep.setBounds(lar/2 + lar/8,lon/3 - lon /20,lar/3, lon/12);
		rep.setFont(new Font("SansSerif", Font.PLAIN, 17));
		rep.setForeground(Color.white);
		rep.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		main.add(rep);
		
		// RESULTAT
		if(resultAlgo == null) resultAlgo = Modele.afficherResultat(algo, m.partie.partieEnCours, false);
		String r = "<html>";
		// On ajoute les balises HTML pour bien afficher les resultats
		for(String s : resultAlgo) {
			boolean alinea = false;
			for(int i=1; i<s.length()+1; i++) {
				if(i%33 == 0) alinea = true;
				if(alinea && s.charAt(i-1) == ' ') {
					r += "<br>";
					alinea = false;
				}
				r += s.charAt(i-1);
			}
			r += "<br><br>";
		}
		r += "</html>";
		
		JLabel chemin = new JLabel(r);
		chemin.setBounds(lar/2 + lar/8, lon/2-lon/20, lar/3, lon/3+lon/8);
		chemin.setFont(new Font("SansSerif", Font.PLAIN, 15));
		chemin.setForeground(Color.white);
		main.add(chemin);
		
		return main;
	}
	
	/**
	 * Retourne les titres des paramètres
	 * @return
	 */
	private JPanel paramAndOptions() {
		JPanel o = new JPanel(new GridLayout(2, 2));
		o.setBackground(Color.white);
		o.setOpaque(false);
		
		JLabel p1 = new JLabel("Taille du jeu");
		p1.setForeground(Color.white);
		p1.setFont(new Font("SansSerif", Font.PLAIN, 13));
		p1.setHorizontalAlignment(0);
		o.add(p1);
		paramAndCursors(o, true);
		
		JLabel p2 = new JLabel("Difficulte");
		p2.setForeground(Color.white);
		p2.setFont(new Font("SansSerif", Font.PLAIN, 13));
		p2.setHorizontalAlignment(0);
		o.add(p2);
		paramAndCursors(o, false);
		
		return o;
	}
	
	/**
	 * Ajoute les curseurs des "+" et des "-" dans le JPanel des paramètres
	 * Si condition est false, alors c'est la difficulte sinon c'est la taille du niveau
	 * @param content
	 * @param condition
	 */
	private void paramAndCursors(JPanel content, boolean condition) {
        JPanel c = new JPanel(new GridLayout(1, 3));
        c.setBackground(new Color(50,50,50));
        String[] plusOuMoins = {(condition ? Integer.toString(m.taille) : Integer.toString(m.difficulte)),"-", "+"};
        JButton[] buttons = new JButton[3];
        
        if(m.taille > 5) m.taille = 5;
        if(condition && m.taille>=5) plusOuMoins[0] = "5";
        if(!(condition) && m.difficulte == 4) plusOuMoins[0] = "alea";
        
        for(int i=0; i<plusOuMoins.length; i++) {
            int x = i;
            buttons[i] = new JButton(plusOuMoins[i]);
            buttons[i].setForeground(Color.white);
            buttons[i].setFont(new Font("SansSerif", Font.PLAIN, 13));
            buttons[i].setHorizontalTextPosition(0);
            buttons[i].setBackground(new Color(50,50,50));
            buttons[i].setBorder(BorderFactory.createLineBorder(new Color(73,73,73), 5));
            if(i == 0) buttons[i].setEnabled(false);
            buttons[i].addActionListener((event) -> {
                if(x == 2) m.augmenteOuDiminue(condition, true);
                else m.augmenteOuDiminue(condition, false);
                buttons[0].setText(condition ? Integer.toString(m.taille) : Integer.toString(m.difficulte));
                if(!condition && m.difficulte == 4) buttons[0].setText("alea");
                else buttons[0].setText(condition ? Integer.toString(m.taille) : Integer.toString(m.difficulte));
            });
            c.add(buttons[i]);
        }
        content.add(c);
    }
	
	/**
	 * Retourne le JPanel de la partie en cours
	 * @return
	 */
	@SuppressWarnings("serial")
	JPanel jPanelPartie() {
		JPanel main = new JPanel(null);
		main.setBackground(new Color(73,73,73));
		
		VisuelPlateau vp = partieB(main);
		JPanel panelNotify = new JPanel(new BorderLayout()) {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D)g;
				GradientPaint grad = new GradientPaint(20, 50, new Color(73,73,73), 0, 0, Color.WHITE);
				g2.setPaint(grad);
				g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panelNotify.setBounds(0,lon/25,2*lar/3,lon/18);
		main.add(panelNotify);
		
		if(robot) {
			Algorithme res;
			if(this.algo.equals("asm")) res = new AEtoileManhattan(m.partie.partieEnCours);
			else if(this.algo.equals("dj")) res = new Dijkstra(m.partie.partieEnCours);
			else if(this.algo.equals("astar")) res = new AEtoile(m.partie.partieEnCours);
			else res = new ParcoursEnLargeur(m.partie.partieEnCours);
			LinkedList<Plateau> list = res.algorithme();
			if(list.get(1).win()) return main;
			int num = list.get(0).whatTheNumberMoved(list.get(1));
			String notify = "Le robot a choisi de bouger la case " + num + ".";
			JLabel pl = new JLabel("      ");
			pl.setForeground(Color.black);
			panelNotify.add(pl);
			this.revalidate();
			Timer t = new Timer(50, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String labelText = pl.getText();
					labelText += notify.charAt(charIndex);
					if(notify.charAt(charIndex) == '.') robot = false;
					pl.setText(labelText);
					charIndex++;
					if(charIndex >= notify.length()) ((Timer) e.getSource()).stop();
				}
			});
			t.start();
			this.repaint();
			vp.donneUnCoup(num);
		}
		return main;
	}
	
	/**
	 * Retourne le jPanel du plateau de jeu
	 * @param main
	 * @return
	 */
	private VisuelPlateau partieB(JPanel main) {
		JPanel rTop = new JPanel(new GridLayout(1, 2));
		rTop.setBounds(2*lar/3, lon/14, lar/4, lon/3);
		rTop.setOpaque(false);
		rTop.add(addAnImage("/launcher/medias/deplacements.png"));
		
		JLabel nbM = new JLabel(Integer.toString(m.partie.nbMouvs), 0);
		nbM.setFont(new Font("SansSerif", Font.PLAIN, 40));
		nbM.setForeground(Color.white);
		rTop.add(nbM);
		main.add(rTop);
		
		VisuelPlateau vp = new VisuelPlateau(this, m, nbM, false);
		vp.setBounds(lar/20, (lon-3*lon/4)/2, 3*lon/4, 3*lon/4);
		main.add(vp);
		
		JPanel rBottom = new JPanel(new GridLayout(3,1));
		rBottom.setBounds(lar-(2*lar/5), 5*lon/14, lar/3, lon/2-2);
		rBottom.setOpaque(false);
		main.add(rBottom);
		
		addThreeButtons(rBottom, "Appel au robot", "Changer la difficulte", "Recommencer", "partie");
		
		return vp;
	}
	
	/**
	 * Retourne le Jpanel du plateau pour les tests des algorithmes 
	 * @param main
	 * @return
	 */
	private VisuelPlateau partieA(JPanel main) {
		JPanel rTop = new JPanel(new GridLayout(1, 2));
		rTop.setBounds(2*lar/3, lon/20, lar/4, lon/3);
		rTop.setOpaque(false);
		rTop.add(addAnImage("/launcher/medias/deplacements.png"));
		
		JLabel nbM = new JLabel(Integer.toString(m.partie.nbMouvs), 0);
		nbM.setFont(new Font("SansSerif", Font.PLAIN, 40));
		nbM.setForeground(Color.white);
		rTop.add(nbM);
		main.add(rTop);
		
		VisuelPlateau vp = new VisuelPlateau(this, m, nbM, true);
		vp.setBounds(lar/20, (lon-3*lon/4)/2, 3*lon/4, 3*lon/4);
		main.add(vp);
		
		JPanel rBottom = new JPanel(new GridLayout(3,1));
		rBottom.setBounds(0, 0, lar/3, lon/2-2);
		rBottom.setOpaque(false);
		main.add(rBottom);
		
		return vp;
	}
	
	/**
	 * Retourne le jPanel lorsque le joueur a gagné
	 * @param nbMouv
	 * @return
	 */
	JPanel jPanelWin(int nbMouv) {
		m.supprimerSave();
		resultAlgo = null;
		
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(73, 73, 73));
		
		addTitleText(mainPanel, "MOUVEMENTS", Integer.toString(nbMouv));

		JPanel contentB = new JPanel(new GridLayout(3,1));
		contentB.setBounds(lar/4,2*lon/5,lar/2, lon/2);
		contentB.setOpaque(false);
		mainPanel.add(contentB);
		
		addThreeButtons(contentB, "Null", "Nouvelle partie", "Revenir au menu", "win");
		
		return mainPanel;
	}

	/**
	 * Retourne le JPanel de la page des options supplémentaires
	 * @return
	 */
	private JPanel jPanelMan() {
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(73, 73, 73));	
		
		JLabel txt1 = new JLabel("JEU DU TAQUIN", 0);
		txt1.setBounds(lar/2-lar/3,lon/12,2*lar/3, lon/9);
		txt1.setFont(new Font("SansSerif", Font.PLAIN, 22));
		txt1.setForeground(Color.white);
		txt1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		mainPanel.add(txt1);
		
		JLabel txt2 = new JLabel("<html><p align=justify>Le but du Taquin est de ranger toutes les cases dans le bon ordre. En cliquant sur une case, on peut le bouger s'il y a l'emplacement vide a cote. Les blocs sont tout d'abord melanges, et la partie est gagnee quand la disposition initiale est atteinte.</p></html>", 0);
		txt2.setBounds(lar/2-lar/3,3*lon/12,2*lar/3, lon/6);
		txt2.setFont(new Font("SansSerif", Font.PLAIN, 15));
		txt2.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		txt2.setForeground(Color.white);
		mainPanel.add(txt2);
		
		JLabel txt3 = new JLabel("MEILLEURES PARTIES", 0);
		txt3.setBounds(lar/2-lar/3,5*lon/12,2*lar/3, lon/9);
		txt3.setFont(new Font("SansSerif", Font.PLAIN, 22));
		txt3.setForeground(Color.white);
		txt3.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		mainPanel.add(txt3);
		
		JPanel down = new JPanel(new GridLayout(2,2));
		down.setBounds(lar/2-lar/3, 7*lon/12,2*lar/3, lon/9);
		down.setOpaque(false);
		mainPanel.add(down);
		
		for(int i=2; i<=5; i++) {
			JLabel min = new JLabel("Taille " + i + " : " + (m.joueur.quelTableau(i) == -1 ? "-" : Integer.toString(m.joueur.quelTableau(i))), 0);
			min.setForeground(Color.white);
			min.setFont(new Font("SansSerif", Font.PLAIN, 15));
			down.add(min);
		}
		
		JButton menu = new JButton("Menu");
		menu.setBackground(new Color(84,84,84));
		menu.setFont(new Font("SansSerif", Font.PLAIN, 15));
		menu.setForeground(Color.white);
		menu.setBounds(lar/2-lar/3, 9*lon/12, 2*lar/3, lon/9);
		menu.setBorderPainted(false);
		mainPanel.add(menu);
		menu.addActionListener ((event) -> {
			repaint("accueil", jPanelAccueil());
		});
		
		JButton stat = new JButton(">");
		stat.setBackground(new Color(84,84,84));
		stat.setFont(new Font("SansSerif", Font.PLAIN, 13));
		stat.setForeground(Color.white);
		stat.setBounds(9*lar/10, lon/2-lon/24, lon/12, lon/12);
		stat.setBorderPainted(false);
		mainPanel.add(stat);
		stat.addActionListener((event) -> {
			repaint("stat", jPanelStat());
		});
		
		return mainPanel;
	}

	/**
	 * Retourne le JPanel des statistiques
	 * @return
	 */
	private JPanel jPanelStat() {
		JPanel mainPanel = new JPanel(null);
		mainPanel.setBackground(new Color(73, 73, 73));
		
		JLabel txt1 = new JLabel("ALGORITHMES ET STATISTIQUES", 0);
		txt1.setBounds(lar/2-lar/3,lon/12,2*lar/3, lon/9);
		txt1.setFont(new Font("SansSerif", Font.PLAIN, 22));
		txt1.setForeground(Color.white);
		txt1.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.white));
		mainPanel.add(txt1);
		
		JPanel algos = new JPanel(new GridLayout(4, 1));
		algos.setBounds(lar/2-lar/3,3*lon/16, 2*lar/3, 9*lon/12);
		algos.setOpaque(false);
		mainPanel.add(algos);
		
		TreeMap<String, String> titleAlgos = new TreeMap<String, String>();
		titleAlgos.put("Parcours en largeur", "pl");
		titleAlgos.put("Dijkstra", "dj");
		titleAlgos.put("A*", "astar");
		titleAlgos.put("A* (Manhattan)", "astarManhattan");
		
		TreeMap<String, String> titleText = new TreeMap<String, String>();
		titleText.put("Parcours en largeur", "Un parcours en largeur debute a partir d'un nœud source. Puis il liste tous les voisins de la source, puis ensuite il les explore un par un.");
		titleText.put("Dijkstra", "Dijkstra sert a resoudre le probleme du plus court chemin, mais ici, il n'est pas optimise puisque la distance entre tous les noeuds est toujours de 1.");
		titleText.put("A*", "A* est un algorithme de recherche dans un graphe entre deux noeuds. Il utilise une heuristique sur chaque nœud pour estimer le meilleur chemin. Ici, nous utilisons le nombre de cases mals placees.");
		titleText.put("A* (Manhattan)", "Cet algorithme est le meme que le precedent. Il y a juste l'heuristique qui change : ici, nous utilisons la distance de Manhattan.");
		
		for(var item : titleAlgos.entrySet()) {
			JPanel t = new JPanel(null);
			t.setOpaque(false);
			
			JLabel t2 = new JLabel(item.getKey());
			t2.setBounds(0, 10, 3* lar/5, lon/20);
			t2.setFont(new Font("SansSerif", Font.PLAIN, 15));
			t2.setForeground(Color.white);
			t.add(t2);
			
			JLabel t3 = new JLabel("<html><p align=justify>" + titleText.get(item.getKey()) + "</p></html>");
			t3.setBounds(0, 40, 3*lar/5-20, lon/12);
			t3.setFont(new Font("SansSerif", Font.PLAIN, 14));
			t3.setForeground(Color.white);
			t.add(t3);
			
			JButton stats = new JButton("+");
			stats.setBackground(new Color(84,84,84));
			stats.setFont(new Font("SansSerif", Font.PLAIN, 13));
			stats.setForeground(Color.white);
			stats.setBorderPainted(false);
			stats.setBounds(3*lar/5, 40, lar/18, lon/18);
			t.add(stats);
			stats.addActionListener((event) -> {
				try {
					Modele.stats(item.getValue());
				} catch (IOException e) {
				}
			});
			
			algos.add(t);
		}
		
		JButton man = new JButton("<");
		man.setBackground(new Color(84,84,84));
		man.setFont(new Font("SansSerif", Font.PLAIN, 13));
		man.setForeground(Color.white);
		man.setBounds(lar/10-lon/12, lon/2-lon/24, lon/12, lon/12);
		man.setBorderPainted(false);
		mainPanel.add(man);
		man.addActionListener((event) -> {
			repaint("man", jPanelMan());
		});
		
		return mainPanel;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	// REECRITURE DES FONCTIONS
	
	@Override
	public void componentHidden(ComponentEvent arg0) {}

	@Override
	public void componentMoved(ComponentEvent arg0) {}
	
	@Override
	public void componentResized(ComponentEvent arg0) {
		Dimension dim = this.getSize();
		lar = (int) dim.getWidth();
		lon = (int) dim.getHeight();
		switch(nomPanelCourant) {
			case "accueil" : repaint("accueil", jPanelAccueil()); break;
			case "param" : repaint("param", jpanelModeJeu()); break;
			case "partie" : repaint("partie", jPanelPartie()); break;
			case "man" : repaint("man", jPanelMan()); break;
			case "win" : repaint("win", jPanelWin(m.partie.nbMouvs)); break;
			case "stat" : repaint("stat", jPanelStat()); break;
			case "algo" : repaint("algo", jPanelAlgo()); break;
			default : break;
		}
	}
	
	@Override
	public void componentShown(ComponentEvent arg0) {}

}

