package algorithmes;
import objects.*;
import java.util.LinkedList;
import java.util.HashMap;

/**
 * <b>AEtoileManhattan</b> représente l'algoritheme A* avec une heuristique différente.
 * CLÉ : DISTANCE DE MANHATTAN + NOMBRE DE MOUVEMENTS
 * DISTANCE : NOMBRE DE MOUVEMENTS
 */
public class AEtoileManhattan extends Algorithme {
	
	private FileDePriorite file = new FileDePriorite();
	private HashMap<Sommet,Integer> vu = new HashMap<Sommet,Integer>();
	
	/**
	 * CONSTRUCTEUR
	 * @param p
	 */
	public AEtoileManhattan(Plateau p) {
		super(p);
	}
	
	
	@Override
	public LinkedList<Plateau> algorithme() {
		file.insertion(s, s.getP().distanceManhattan());
		nbElemDansFile++;
		while(!file.estVide()) {
			Sommet u = file.extraireMin();
			vu.put(u, u.getMouv());
			nbElemDansEnsemble++;
			if(u.getP().win()) return super.recupRes(u);
			
			LinkedList<Plateau> fils = u.getP().listefils();
			for(Plateau v: fils) {
				Sommet tmp = new Sommet(v, u, u.getMouv()+1);
				if(file.indiceDansFile(tmp) != -1) {
					tmp = file.getSommet(file.indiceDansFile(v));
					if(tmp.getMouv() > u.getMouv()+1) {
						tmp.setMouv(u.getMouv()+1);
						tmp.setPere(u);
						file.MaJ(tmp, tmp.getP().distanceManhattan() + tmp.getMouv());
					}
				}else if(vu.containsKey(tmp)) {
					int dist = vu.get(tmp);
					if(dist > u.getMouv()+1) {
						vu.remove(tmp);
						file.insertion(tmp, v.distanceManhattan() + tmp.getMouv());
						nbElemDansFile++;
					}
				}			
				else {
					tmp = new Sommet(v, u, u.getMouv()+1);
					file.insertion(tmp, v.distanceManhattan() + tmp.getMouv());
					nbElemDansFile++;
				}
			}
		}
		return null;
	}
}