package algorithmes;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import objects.*;

/**
 * <b>Parcours En Largeur</b> est un algorithme de parcours.
 * @see Algorithme
 */
public class ParcoursEnLargeur extends Algorithme {
	
	HashSet<Sommet> ensemble = new HashSet<Sommet>();
	Queue<Sommet> file = new LinkedList<Sommet>();
	
	/**
	 * CONSTRUCTEUR
	 * @param p
	 */
	public ParcoursEnLargeur(Plateau p) {
		super(p);
	}
	
	
	@Override
	public LinkedList<Plateau> algorithme() {
		ensemble.add(super.s);
		file.add(super.s);
		nbElemDansEnsemble++;
		nbElemDansFile++;
		while(!file.isEmpty()) {
			Sommet x = file.poll();
			if(x.getP().win()) return super.recupRes(x);
			LinkedList<Plateau> adjaX = x.getP().listefils();
			for(int i=0; i<adjaX.size(); i++) {
				Plateau pi = adjaX.get(i);
				Sommet y = new Sommet(pi, x, x.getMouv()+1);
				if(!ensemble.contains(y)) {
					ensemble.add(y);
					file.add(y);
					nbElemDansEnsemble++;
					nbElemDansFile++;
				}
			}
		}
		return null;
	}
}
