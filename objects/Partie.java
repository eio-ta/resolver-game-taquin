package objects;

import java.io.Serializable;

/**
 * <b>Partie</b> est la classe qui représente une partie de jeu.
 */
public class Partie implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public Plateau partieInitiale;
	public Plateau partieEnCours;
	public int nbMouvs;

	/**
	 * CONSTRUCTEUR
	 */
	public Partie( Plateau p) {
		this.partieEnCours = p;
		this.partieInitiale = partieEnCours.copyPlateau();
	}
	
	
	// FONCTIONS
	
	/**
	 * Verifie si la configuration du jeu en cours est la meme que la finale
	 */
	public boolean win() {
		return partieEnCours.win();
	}
	
	/**
	 * Affiche une partie en cours
	 */
	public void afficher() {
		if(partieEnCours != null) {
			partieEnCours.afficher();
		} else {
			System.out.println("pas de partie");
		}
	}
	
}
