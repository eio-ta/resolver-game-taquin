package algorithmes;

import java.util.LinkedList;
import objects.*;
import java.util.HashSet;

/**
 * <b>Dijkstra</b> est un algorithme de parcours.
 * @see Algorithme
 */
public class Dijkstra extends Algorithme {
	
	private FileDePriorite file = new FileDePriorite();
	private HashSet<Sommet> ensemble = new HashSet<Sommet>();
	
	
	/**
	 * CONSTRUCTEUR
	 * @param p
	 */
	public Dijkstra(Plateau p) {
		super(p);
	}
	
	
	@Override
	public LinkedList<Plateau> algorithme() {
		ensemble.add(s);
		file.insertion(s, 0);
		nbElemDansEnsemble++;
		nbElemDansFile++;
		while(!file.estVide()) {
			Sommet u = file.extraireMin();
			if(u.getP().win()) return super.recupRes(u);
			LinkedList<Plateau> pla = u.getP().listefils();
			for(Plateau v : pla) {
				if(file.indiceDansFile(v) != -1) {
					Sommet vs = file.getSommet(file.indiceDansFile(v));
					if(u.getMouv() + 1 < vs.getMouv()) {
						vs.setMouv(u.getMouv() + 1);
						u.setPere(u);
						file.MaJ(vs, vs.getMouv());
					}
				} else {
					Sommet tmp = new Sommet(v, u, u.getMouv()+1);
					if(!ensemble.contains(tmp)) {
						file.insertion(tmp, tmp.getMouv());
						ensemble.add(tmp);
						nbElemDansEnsemble++;
						nbElemDansFile++;
					}
				}
			}
		}
		return null;
	}
}
