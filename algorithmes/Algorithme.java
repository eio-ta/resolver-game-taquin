package algorithmes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import objects.Plateau;

/**
 * <b>Algorithme</b> est la classe mère de tous les algorithmes.
 * 
 */
public abstract class Algorithme {
	
	protected final Sommet s;
	public int nbElemDansEnsemble = 0;
	public int nbElemDansFile = 0;
	
	/**
	 * CONSTRUCTEUR
	 * @param p
	 */
	Algorithme(Plateau p) {
		this.s = new Sommet(p, null, 0);
	}
	
	
	/**
	 * Algortihme de résolution du jeu de Taquin
	 * @return
	 */
	public abstract LinkedList<Plateau> algorithme();
	
	/**
	 * Affiche les résultats d'un algorithme
	 * @param chemin
	 */
	public void afficherRes(List<Plateau> chemin) {
		if(chemin != null) {
			for(int i = 0; i<chemin.size()-1; i++) {
				System.out.println("Mouvement " + i + " :");
				chemin.get(i).afficher();
				int number = chemin.get(i).whatTheNumberMoved(chemin.get(i+1));
				System.out.println("Il faut bouger la case " + number + ".\n\n");
			}
			System.out.println("Mouvement " + (chemin.size()-1) + " :");
			chemin.get(chemin.size()-1).afficher();
		} else System.out.println("Il n'y a pas de solution.");
	}
	
	/**
	 * Récupère les résultats d'un algorithme
	 * @param u
	 * @return
	 */
	protected LinkedList<Plateau> recupRes(Sommet u) {
		LinkedList<Plateau> chemin = new LinkedList<Plateau>();
		while(u != null) {
			chemin.add(u.getP());
			u = u.getPere();
		}
		Collections.reverse(chemin);
		return chemin;
	}
	
}
