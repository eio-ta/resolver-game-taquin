package objects;

import java.util.Random;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * <b>Plateau</b> est une classe qui représente un plateau de jeu.
 */
public class Plateau implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public int taille;
	public int[] plateau;

	/**
	 * CONSTRUCTEUR
	 * @param taille
	 * @param difficulte
	 */
	public Plateau(int taille, int difficulte) {
		this.taille = taille; 
		generation_jeu(difficulte);
	}
	
	/**
	 * CONSTRUCTEUR
	 * @param tab
	 */
	public Plateau(int[] tab){
		this.taille = (int) Math.sqrt(tab.length);
		plateau = tab;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	// GENERATIONS DE CONFIGURATIONS
	
	/**
	 * Genere une configuration avec des permutations en fonction de la difficulté
	 * @param difficulte Niveau de difficulte
	 */
	private void generation_jeu(int difficulte) {
		int nbCoup = 0;
		if(difficulte == 1) nbCoup = 5;
		else if(difficulte == 2) nbCoup = 15;
		else if(difficulte == 3) nbCoup = 30;
		else {
			generateRandomSoluble();
			if(win()) generation_jeu(difficulte);
			return;
		}
		nbCoup *= taille;
		
		plateau = new int[taille*taille];
		for(int i=0; i<plateau.length-1; i++) {
			plateau[i] = i + 1;
		}
		plateau[plateau.length - 1] = 0;
		
		for(int i=0; i<nbCoup; i++) {
			HashMap<Integer, Integer> listA = adja(0);
			LinkedList<Integer> listA2 = new LinkedList<Integer>();
			for(var item : listA.entrySet()) listA2.add(item.getValue());
			Random rand = new Random();
			switch(rand.nextInt(listA2.size())) {
				case 0 :
					this.deplacement_dans_le_tableau(listA2.get(0));
					break;
				case 1 :
					this.deplacement_dans_le_tableau(listA2.get(1));
					break;
				case 2:
					this.deplacement_dans_le_tableau(listA2.get(2));
					break;
				case 3:
					this.deplacement_dans_le_tableau(listA2.get(3));
					break;
				default : break;
			}
		}
		if(win()) generation_jeu(difficulte);
	}
	
	/**
	 * Génére une configuration aléatoire jusqu'à ce qu'elle soit soluble
	 * Fonction pour nos tests
	 */
	public void generateRandomSoluble() {
		generateRandom();
		while(!this.isSolvable()) {
			generateRandom();
		}
	}
	
	/**
	 * Fonction auxilière de la génération d'une configuration aléatoire
	 */
    private void generateRandom() {
    	LinkedList<Integer> pioche = new LinkedList<Integer>();
    	plateau = new int[taille*taille];
    	for(int i=0; i<taille*taille; i++) {
    		pioche.add(i);
    	}
    	int c = 0;
    	while(pioche.size() != 0) {
    		Random rand = new Random();
    		int alea = rand.nextInt(pioche.size());
    		plateau[c] = pioche.get(alea);
    		pioche.remove(alea);
    		c++;
    	}
    }
    
    /**
     * Vérifie si le plateau est une permutation
     * si la taille est superieur a 2
     * @return
     */
    public boolean isAGoodPlateau() {
    	if(!this.isSolvable()) return false;
    	else {
    		if(plateau.length != taille * taille) return false;
    		for(int i=0; i<taille*taille; i++) {
    			boolean in = false;
    			for(int var : plateau) {
    				if(var == i) in = true;
    			}
    			if(!in) return false;
    		}
    		return true;
    	}
    }
    
    
    ////////////////////////////////////////////////////////////////////////
    
    // RECUPERER LES CONFIGURATIONS VOISINES
    
    /**
	 * Fais une liste de toutes les cases adjacentes du numero donne en argument
	 * @param num
	 */
	public HashMap<Integer, Integer> adja(int num) {
		HashMap<Integer, Integer> fils = new HashMap<Integer, Integer>();
		int ind = findNum(num);
		
		if(ind >= taille) {
			// CODE 1 POUR LA CASE DU HAUT
			fils.put(1, plateau[ind - taille]);
		}
		
		if(ind < taille*taille - taille) {
			// CODE 2 POUR LA CASE DU BAS
			fils.put(2, plateau[ind + taille]);
		}
		
		if(ind % taille != 0) {
			// CODE 3 POUR LA CASE DE GAUCHE
			fils.put(3, plateau[ind - 1]);
		}
		 
		if(ind % taille != taille - 1) {
			// CODE 4 POUR LA CASE DE DROITE
			fils.put(4, plateau[ind + 1]);
		}
		return fils;
	}

	/**
	 * Retourne les plateaux des deplacements possibles (fils)
	 * @return
	 */
	public LinkedList<Plateau> listefils(){
		LinkedList<Plateau> fils = new LinkedList<>();
		HashMap<Integer, Integer> adja = adja(0);
		for(var item : adja.entrySet()) {
			Plateau pi = this.copyPlateau();
			pi.deplacement_dans_le_tableau(item.getValue());
			fils.add(pi);
		}
		return fils;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	// GESTION DE JEU : DEPLACEMENTS, SAVOIR SI ON GAGNE
	
	/**
	 * Retrouve l'indice du numero entre en parametre
	 * @param num
	 * @return
	 */
	public int findNum(int num) {
		for(int i=0; i<plateau.length; i++) {
			if(plateau[i] == num) return i;
		}
		return -1;
	}
	
	
	/**
	 * Swap deux numeros
	 * @param num1
	 * @param num2
	 */
	private void swap(int num1, int num2) {
		int ind1 = findNum(num1);
		int ind2 = findNum(num2);
		int tmp = plateau[ind1];
		plateau[ind1] = plateau[ind2];
		plateau[ind2] = tmp;
	}
	
	
	/**
	 * Bouge le numero entre en parametre
	 * @param num
	 */
	public boolean deplacement_dans_le_tableau(int num) {
		// Si le trou est a cote du numero que le joueur veut bouger, alors le deplacement est faisable
		HashMap<Integer, Integer> listA = adja(0);
		if(listA.containsValue(num)) {
			swap(num, 0);
			return true;
		} else return false;
	}
	
	
	/**
	 * Verifie si la configuration du jeu en cours est la meme que la finale
	 */
	public boolean win() {
		for(int i=0; i<plateau.length-1; i++) {
			if(i != plateau[i]-1) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Affiche le plateau du jeu
	 */
	public void afficher() {
		int a = 0;
		for(int i=0; i<this.taille; i++) {
			for(int j=0; j<this.taille; j++) {
				System.out.print(plateau[a] + (plateau[a] < 10 ? "   " : "  "));
				a++;
			}
			System.out.println();
		}
	}
	
	/**
	 * Retourne le numero qui a bouge entre les 2 configurations
	 * @param e
	 * @return
	 */
	public int whatTheNumberMoved(Plateau e) {
		HashMap<Integer, Integer> list1 = this.adja(0);
		HashMap<Integer, Integer> list2 = e.adja(0);
		
		for(var number1 : list1.entrySet()) {
			for(var number2 : list2.entrySet()) {
				if(number2.getValue() == number1.getValue()) return number2.getValue();
			}
		}
		return -1;
	}
	
	
	////////////////////////////////////////////////////////////////////////
	
	// FONCTION SUPPLÉMENTAIRES ET UTILES
	
	/**
	 * Determine si le taquin est soluble ou pas
	 * @return
	 */
	public boolean isSolvable() {
		int permutations = 0;
		int ligne = 0;
		int ligne_vide = 0;
		
		for(int i = 0; i < plateau.length; i++) {
			if(i%taille == 0) ligne++;
			if(plateau[i]==0) {
				ligne_vide = ligne;
				continue;
			}
			for(int j=i+1; j<plateau.length; j++) {
				if(plateau[i] > plateau[j] && plateau[j] != 0) permutations++;
			}
		}
		
		if(taille % 2 == 0) {
			if(ligne_vide%2 == 0) return permutations % 2 == 0;
			else return permutations % 2 != 0;
		} else return permutations % 2 == 0;
	}
	
	/**
	 * Retourne une copie du plateau courant
	 * @return
	 */
	public Plateau copyPlateau() {
		Plateau copy = new Plateau(this.taille, 1);
		copy.plateau = new int[this.taille*this.taille];
		for(int i = 0; i < plateau.length; i++) {
			copy.plateau[i] = plateau[i];
		}
		return copy;
	}
	
	/**
	 * Compare deux tableaux et retourne true si ils ont les memes arguments
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if(o.getClass() != this.getClass()) return false;
		Plateau e = (Plateau) o;
		if(this.taille != e.taille) return false;
		for(int i=0; i<plateau.length; i++) {
			if(plateau[i] != e.plateau[i]) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		String st = "";
		for(int i=0; i<taille*taille; i++) {
			st += Integer.toString(plateau[i]) + "#";
		}
		return st.hashCode();
	}
	
	
	////////////////////////////////////////////////////////////////////////
	
	// DISTANCES POUR LES ALGORITHMES & HEURISTIQUES
	
	/**
	 * Retourne le nombre de cases mals placées
	 * @return
	 */
	public int distanceAvecFinale() {
		if(win()) return 0;
		int d = 0;
		for(int i=0; i< this.taille*this.taille-1; i++) {
			if(this.plateau[i] != i+1) d++;
		}
		if(plateau[this.taille*this.taille-1] != 0) d++;
		return d;
	}

	/**
	 * Retourne la distance de Manhattan
	 * @return
	 */
	public int distanceManhattan() {
		if(win()) return 0;
		int distance = 0;
		for(int i=0; i<this.taille*this.taille-1;i++) {
			int val = this.plateau[i];
			distance += Math.abs(val-i);
		}
		return distance;
	}
}
