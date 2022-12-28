package launcher;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import algorithmes.AEtoile;
import algorithmes.AEtoileManhattan;
import algorithmes.Algorithme;
import algorithmes.Dijkstra;
import algorithmes.ParcoursEnLargeur;

import objects.*;

/**
 * <b>Modele</b> est la classe qui enregistre les données d'une partie.
 * Cette classe peut enregistrer des parties déjà commencées, supprimer des fichiers en cours et récupérer des parties en cours.
 */
public class Modele {

	// VARIABLES
	
	int taille = 2;
	int difficulte = 1;
	Partie partie;
	Partie partieSave;
	Joueur joueur;
	

	/**
	 * CONSTRUCTEUR
	 */
	Modele() {
		Partie p = deserializePartie();
		partieSave = p;
		Joueur j = deserializeJoueur();
		if(j == null) {
			joueur = new Joueur();
			serializeJoueur(joueur);
		} else joueur = j;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	// FONCTIONS

	/**
	 * Augmente/Diminie la taille ET/OU la difficulté
	 * @param condition
	 * @param augmenteOuPas
	 */
	void augmenteOuDiminue(boolean condition, boolean augmenteOuPas) {
		if(condition) {
			if(augmenteOuPas && taille < 5) taille++;
			if(!(augmenteOuPas) && taille > 2) taille--;
		} else {
			if(augmenteOuPas && difficulte < 4) difficulte++;
			if(!(augmenteOuPas) && difficulte > 1) difficulte--;
		}
	}
	
	/**
	 * Recommence la partie en cours
	 */
	void recommencerPartie() {
		partie.partieEnCours = partie.partieInitiale.copyPlateau();
		partie.nbMouvs = 0;
		serializePartie(partie);
		partieSave = partie;
	}
	
	/**
	 * Déplace une case dans la partie en cours ET dans le plateau
	 * @param num
	 */
	void deplacementDansLaPartie(int num, boolean test) {
		partie.partieEnCours.deplacement_dans_le_tableau(num);
		partie.nbMouvs++;
		if(!test) serializePartie(partie);
		partieSave = partie;
	}
	
	/**
	 * Enregistre les données d'une partie
	 * @param p
	 */
	void serializePartie(Partie p) {
		try(FileOutputStream fos = new FileOutputStream("./launcher/medias/partieSave.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(p);
			oos.close();
		} catch(IOException e) {
		}
	}
	
	/**
	 * Retire les données d'une partie
	 * @return
	 */
	Partie deserializePartie() {
		Partie p = null;
		try {
			FileInputStream fis = new FileInputStream("./launcher/medias/partieSave.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			p = (Partie) ois.readObject();
			ois.close();
		} catch(IOException e) {
		} catch (ClassNotFoundException e) {
		}
		if(p != null) {
			this.taille = p.partieEnCours.taille;
			this.difficulte = 4;
		}
		return p;
	}
	
	/**
	 * Enregistre les données d'un joueur
	 * @param j
	 */
	void serializeJoueur(Joueur j) {
		try(FileOutputStream fos = new FileOutputStream("./launcher/medias/joueurSave.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(j);
			oos.close();
		} catch(IOException e) {
		}
	}
	
	/**
	 * Retire les données d'un joueur
	 * @return
	 */
	Joueur deserializeJoueur() {
		Joueur p = null;
		try {
			FileInputStream fis = new FileInputStream("./launcher/medias/joueurSave.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			p = (Joueur) ois.readObject();
			ois.close();
		} catch(IOException e) {
		} catch (ClassNotFoundException e) {
		}
		return p;
	}
	
	/**
	 * Supprime toutes les données
	 */
	void supprimerSave() {
		partieSave = null;
		Path path = Paths.get("./launcher/medias/partieSave.txt");
		try {
			Files.delete(path);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Retourne un plateau importé par le joueur
	 * @param path
	 * @return
	 */
	Plateau donneUnPlateauDunFichierTxt(String path) {
		try {
			File file = new File(path);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			LinkedList<String> res = new LinkedList<String>();
			String line;
			while((line = br.readLine()) != null) {
				res.add(line);
			}
			fr.close();
			if(res.size() != 2) {
				System.out.println("WARNING : Le tableau que vous avez ecrit n'est pas une permutation.");
				return null;
			} else {
				taille = Integer.parseInt(res.get(0));
				difficulte = 4;
				String[] sInt = res.get(1).split(" ");
				if(sInt.length != taille * taille) {
					System.out.println("WARNING : Le tableau que vous avez ecrit n'est pas une permutation.");
					return null;
				} else {
					int[] plateau = new int[taille*taille];
					for(int i = 0; i<taille*taille; i++) {
						plateau[i] = Integer.parseInt(sInt[i]);
					}
					Plateau p = new Plateau(plateau);
					if(!p.isAGoodPlateau()) {
						System.out.println("WARNING : Le tableau que vous avez ecrit n'est pas une permutation.");
						return null;
					} else return p;
				}
				
			}
		} catch(IOException e) {
			e.printStackTrace();
		} catch(NumberFormatException e) {
			return null;
		}
		return null;
	}
	
	/**
	 * Retourne si le PDF des stats a été ouvert ou pas
	 */
	public static boolean stats(String p1) throws IOException {
		if(p1.equals("pl") || p1.equals("dj") || p1.equals("astar") || p1.equals("astarM") || p1.equals("all")) {
			File file = null;
			if(p1.equals("pl") ) file = new File("./launcher/medias/STATSPL.pdf");
			else if(p1.equals("dj")) file = new File("./launcher/medias/STATSDJ.pdf");
			else if(p1.equals("astar")) file = new File("./launcher/medias/STATSASTAR.pdf");
			else if(p1.equals("astarManhattan")) file = new File("./launcher/medias/STATSASTARM.pdf");
			if(file!=null) Desktop.getDesktop().open(file);
			System.out.println();
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * Retourne les résultats d'un algorithme sur un plateau
	 * @param algo
	 * @param p
	 * @param result
	 * @return
	 */
	public static String[] afficherResultat(String algo, Plateau p, boolean result) {
		Algorithme am;
		String[] s = new String[6];
		
		if(algo.equals("pl")) {
			am = new ParcoursEnLargeur(p);
			s[0] = "Parcours En Parcours :";
		} else if(algo.equals("dj")) {
			am = new Dijkstra(p);
			s[0]= "Dijkstra :";
		} else if(algo.equals("astar")) {
			am = new AEtoile(p);
			s[0] = "A* (avec les cases mal placees) :";
		} else if(algo.equals("astarM")){
			am = new AEtoileManhattan(p);
			s[0] = "A* (avec la distance de Manhattan) :";
		} else {
			System.out.println("Vous avez selectionne un algo qui n'existe pas.");
			return null;
		}
		
		long debut = System.currentTimeMillis();
		LinkedList<Plateau> res = am.algorithme();
		
		s[1] ="Nbr d'elements dans la file: " + am.nbElemDansFile;
		s[2] = "Nbr d'elements dans l'ensemble: " + am.nbElemDansEnsemble;
		s[3] ="Nbr de mouvements: " + (res.size()-1);
		s[4] ="Temps en millisecondes: " + (System.currentTimeMillis()-debut);
		
		s[5] = "Chemin: ";
        for(int i =1; i<res.size(); i++) {
            int num = res.get(i-1).whatTheNumberMoved(res.get(i));
            s[5] += Integer.toString(num) + " ";
        }
		
		if(result) am.afficherRes(res);
		return s;
	}

}
