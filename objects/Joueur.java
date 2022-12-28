package objects;

import java.io.Serializable;

/**
 * <b>Joueur</b> est une classe qui représente un joueur.
 */
public class Joueur implements Serializable {
	
	// VARIABLES

	private static final long serialVersionUID = 1L;
	private int minTaille2 = -1;
	private int minTaille3 = -1;
	private int minTaille4 = -1;
	private int minTaille5 = -1;
	
	
	// FONCTIONS
	
	/**
	 * Retourne le meilleur score d'une partie avec une taille particulière
	 * @param t
	 * @return
	 */
	public int quelTableau(int t) {
		if(t == 2) return minTaille2;
		else if(t == 3) return minTaille3;
		else if(t == 4) return minTaille4;
		else return minTaille5;
	}
	
	
	// FONCTIONS
	
	/**
	 * Retourne "true" si le score actuel est meilleure que le record
	 * @param t
	 * @param n
	 * @return
	 */
	boolean meilleurePartie(int t, int n) {
		if(quelTableau(t) == -1) return true;
		return n < quelTableau(t);
	}
	
	/**
	 * Remplace le score actuel par le nouveau score
	 * @param t
	 * @param n
	 * @return
	 */
	public boolean changerMin(int t, int n) {
		if(meilleurePartie(t, n)) {
			if(t == 2) minTaille2 = n;
			else if(t == 3) minTaille3 = n;
			else if(t == 4) minTaille4 = n;
			else minTaille5 = n;
			return true;
		}
		return false;
	}
	
}
