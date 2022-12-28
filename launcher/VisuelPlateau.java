package launcher;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import objects.Partie;

/**
 * <b>VisuelPlateau</b> gère le visuel pour le plateau de jeu.
 * On l'utilise toujours dans la classe <b>VueGUI</b> pour afficher le plateau.
 * @see VueGUI
 */
public class VisuelPlateau extends JPanel implements MouseListener {
	
	// VARIABLES

	private static final long serialVersionUID = 1L;
	private VueGUI vue;
	private JLabel[] tabP;
	private JLabel nbMouv;
	private Modele m;
	private Partie p;
	
	private int largeur;
	private final int C;
	private boolean test;
	

	/**
	 * CONSTRUCTEUR
	 * @param v
	 * @param m
	 * @param nbMouv
	 */
	VisuelPlateau(VueGUI v, Modele m, JLabel nbMouv, boolean test) {
		this.m = m;
		this.p = m.partie;
		this.nbMouv = nbMouv;
		this.vue = v;
		this.test = test;
		largeur = 3*vue.lon/4;
		C = largeur/m.taille;
		tabP = new JLabel[m.taille*m.taille];
		
		this.addMouseListener(this);
		this.setLayout(null);
        this.setBackground(new Color(40,40,40));
        this.setPreferredSize(new Dimension(largeur, largeur));
        
        int a = 0;
		for(int i=0; i<m.taille; i++) {
			for(int j=0; j<m.taille; j++) {
				if(p.partieEnCours.plateau[a] != 0) {
					JLabel txt = new JLabel((p.partieEnCours.plateau[a] != 0 ? Integer.toString(p.partieEnCours.plateau[a]) : ""), SwingConstants.CENTER);
					txt.setSize(C-1, C-1);
					txt.setOpaque(true);
					txt.setFont(new Font("SansSerif", Font.PLAIN, C/5));
					if(p.partieEnCours.plateau[a] != 0) txt.setBackground(Color.white);
					
					int y = i*C;
					int x = j*C;
					txt.setLocation(x, y);
					tabP[a] = txt;
					
					this.add(txt);
				}
				a++;
			}
		}
	}

	// FONCTIONS
	
	/**
	 * Anime la case qui bouge avec le numéro donné en paramètre
	 * @param num
	 */
	void donneUnCoup(int num) {
		HashMap<Integer, Integer> fils = p.partieEnCours.adja(0);
		
		if(fils.containsValue(num)) {
			int code = 0;
			for(var item : fils.entrySet()) {
				if(item.getValue() == num) code = item.getKey();
			}
			
			int indice = m.partie.partieEnCours.findNum(num);
			int X = tabP[indice].getX();
			int Y = tabP[indice].getY();
			
			switch(code) {
				case 1 : Y += C; break;
				case 2 : Y -= C; break;
				case 3 : X += C; break;
				case 4 : X -= C; break;
				default : break;
			}
			
			tabP[indice].setLocation(X, Y);
			
			for(int i=0; i<m.taille*m.taille; i++) {
				if(tabP[i] == null) tabP[i] = tabP[indice];
			}
			tabP[indice] = null;
			m.deplacementDansLaPartie(num, test);
			nbMouv.setText(Integer.toString(Integer.parseInt(nbMouv.getText())+1));
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getY()/C;
		int y = e.getX()/C;
		int indice = x*m.taille+y;
		
		donneUnCoup(p.partieEnCours.plateau[indice]);
		
		if(m.partie.win()) {
			if(!test) {
				m.joueur.changerMin(m.taille, m.partie.nbMouvs);
				m.serializeJoueur(m.joueur);
			}
			vue.cardLayout.removeLayoutComponent(this);
			vue.repaint("win", vue.jPanelWin(m.partie.nbMouvs));
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
