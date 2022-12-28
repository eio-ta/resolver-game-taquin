package algorithmes;

import objects.Plateau;

/**
 * <b>Sommet</b> représente un sommet du graphe.
 * Un sommet représente une configuration particulière d'un plateau : son père, son nombre de mouvements.
 */
public class Sommet {
	
	
	// VARIABLES
	
	private Plateau p;
	private Sommet pere;
	private int mouv;
	
	/**
	 * CONSTRUCTEUR
	 * @param p
	 * @param pere
	 * @param m
	 */
	public Sommet(Plateau p, Sommet pere, int m) {
		this.p = p;
		this.pere = pere;
		this.mouv = m;
	}
	
	/**
	 * CONSTRUCTEUR
	 * @param p
	 * @param pere
	 */
	public Sommet(Plateau p, Sommet pere) {
		this.p = p;
		this.pere = pere;
		this.mouv = 0;
	}
	
	
	// ACCESSEURS & GETTEURS
	
	/**
	 * GETTEUR
	 * Retourne un plateau
	 * @return
	 */
	Plateau getP() { return p; }
	
	/**
	 * SETTEUR
	 * Change le plateau
	 * @param p
	 */
	void setP(Plateau p) { this.p = p; }
	
	/**
	 * GETTEUR
	 * Retourne le pere du sommet
	 * @return
	 */
	Sommet getPere() { return pere; }
	
	/**
	 * SETTEUR
	 * Change le pere du sommet
	 * @param pere
	 */
	void setPere(Sommet pere) { this.pere = pere; }
	
	/**
	 * GETTEUR
	 * Renvoie le nombre de mouvements
	 * @return
	 */
	int getMouv() { return mouv; }
	
	/**
	 * SETTEUR
	 * Change le nombre de mouvements
	 * @param distance
	 */
	void setMouv(int distance) { this.mouv = distance; }
	
	
	// FONCTIONS
	
	@Override
	public boolean equals(Object o) {
		if(o.getClass() != this.getClass() && o.getClass() != this.p.getClass()) return false;
		
		Sommet s;
		
		if(o.getClass() == this.getClass()) s = (Sommet) o;
		else s = new Sommet((Plateau) o, null, 0);
		
		return this.p.equals(s.p);
	}
	
	@Override
	public int hashCode() {
		return p.hashCode();
	}
}
