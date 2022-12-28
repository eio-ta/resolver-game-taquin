package algorithmes;

import java.util.ArrayList;
import java.util.HashMap;

import objects.Plateau;

/**
 * <b>File de Priorité</b> est une classe qui représente une file de priorité.
 * On l'utilise dans les algorithmes de A* et de Dijkstra.
 * @see AEtoile
 * @see AEtoileManhattan
 * @see Dijkstra
 */
public class FileDePriorite {
	
	/**
	 * <b> Paire </b> 
	 * est une classe interne a file de priorité.
	 * on l'utilise pour lier un sommet a une classe. 
	 */
	private class Paire {
		Sommet val;
		int cle;
		Paire() { val = null; cle = -1; }
		Paire(Sommet s, int c) { val = s; cle = c; }
	}
	
	private ArrayList<Paire> T = new ArrayList<Paire>();
	private int nb = 0;
	private HashMap<Sommet, Integer> hmap = new HashMap<Sommet, Integer>();
	
	/**
	 * CONSTRUCTEUR
	 */
	FileDePriorite() {
		T.add(new Paire());
	}
	
	/**
	 * Retroune "true" si la file est vide
	 * @return
	 */
	boolean estVide() {
		return nb == 0;
	}
	
	/**
	 * Insère un sommet dans la file
	 * @param s
	 */
	void insertion(Sommet s, int c) {
		Paire P = new Paire(s,c);
		T.add(P);
		nb = nb+1;
		int i = nb;
		while(i/2 >= 1 && T.get(i/2).cle > c) {
			T.set(i,T.get(i/2));
			hmap.replace(T.get(i).val,i);
			i = i/2;
		}
		T.set(i,P);
		hmap.put(s,i);
	}
	
	/**
	 * Retire le premier sommet avec la clé la plus petite
	 * @return
	 */
	Sommet extraireMin() {
		assert(nb>0);
		Sommet res = T.get(1).val;
		T.set(1,T.get(nb));
		T.set(nb,null);
		hmap.remove(res);
		nb = nb-1;
		if(nb>0) {
			hmap.replace(T.get(1).val,1);
			remiseEnTas(1);
		}
		return res;
	}
	
	/**
	 * Met à jour la file de priorité
	 * @param i
	 */
	private void remiseEnTas(int i) {
		int g = 2*i, d = 2*i+1, win = i;
		if(g <= nb && T.get(g).cle < T.get(win).cle) win = g;
		if(d <= nb && T.get(d).cle < T.get(win).cle) win = d;
		if(win != i) {
			Paire p = T.get(win);
			T.set(win,T.get(i));
			T.set(i,p);
			hmap.replace(T.get(win).val,win);
			hmap.replace(T.get(i).val,i);
			remiseEnTas(win);
		}
	}
	
	/**
	 * Met à jour la clé du sommet
	 * @param c
	 * @param s
	 */
	void MaJ(Sommet s, int c) {
		int pos = this.indiceDansFile(s);
		assert(pos != -1);
		int excle = T.get(pos).cle;
		assert(excle >= c);
		T.get(pos).cle = c;
		Paire P = T.get(pos);
		int i = pos;
		while(i/2 >= 1 && T.get(i/2).cle > c) {
			T.set(i,T.get(i/2));
			hmap.replace(T.get(i).val,i);
			i = i/2;
		}    
		T.set(i,P);
		hmap.put(s,i);
	}
	
	/**
	 * Retourne l'indice du sommet dans la file avec un sommet
	 * @param s
	 * @return
	 */
	int indiceDansFile(Sommet s) {
		for(var elem : hmap.entrySet()) {
			if(elem.getKey().equals(s)) return elem.getValue();
		}
		return -1;
	}
	
	
	/**
	 * Retourne l'indice du sommet dans la file avec un plateau
	 * @param p
	 * @return
	 */
	int indiceDansFile(Plateau p) {
		Sommet s = new Sommet(p, null,0);
		return indiceDansFile(s);
	}
	
	/**
	 * Retourne un sommet dans la file de priorité
	 * @param indice
	 * @return
	 */
	Sommet getSommet(int indice) {
		return T.get(indice).val;
	}
	
	/**
	 * Affiche la file
	 */
	@Override
	public String toString() {
		System.out.print(hmap);
		String res = "";
		for(int i=1; i<=nb; i++) {
			res += String.valueOf(T.get(i).cle) + " ";
		}
		return res;
	}
}