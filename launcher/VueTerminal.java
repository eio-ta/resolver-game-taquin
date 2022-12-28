package launcher;

import java.util.HashMap;
import java.util.Scanner;

import objects.Partie;
import objects.Plateau;

/**
 * <b>VueTerminal</b> représente l'interface sur le terminal.
 */
public class VueTerminal {
	
	private Partie p;
	
	/**
	 * CONSTRUCTEUR
	 */
	public VueTerminal() {
		Scanner sc = new Scanner(System.in);
		int t = demande_taille_du_jeu(sc);
		System.out.println("Vous avez saisi un plateau de jeu de cote " + t + ".\n");
		int d = demande_difficulte_du_jeu(sc);
		System.out.println("La difficulte du niveau est de " + d + ".\n");
		this.p = new Partie(new Plateau(t, d));
		deroulement_du_jeu();
	}
	
	/**
	 * Demande la taille du tableau de jeu au joueur
	 * @param sc
	 * @return
	 */
	private int demande_taille_du_jeu(Scanner sc) {
		System.out.println("Taille du cote du plateau de jeu (N >= 2) ?");
		try {
			int res = Integer.parseInt(sc.next());
			while (res < 2) {
				System.out.println("\nErreur de saisie : vous avez saisi un nombre inferieur a 2.");
				System.out.println("Taille du cote du plateau de jeu (N >= 2) ?");
				res = Integer.parseInt(sc.next());
			}
			return res;
		} catch (NumberFormatException e) {
			System.out.println("\nErreur de saisie, ce n'est pas un nombre. Recommencez.\n");
			return demande_taille_du_jeu(sc);
		}
	}
	
	/**
	 * Demande le niveau de difficulté du jeu au joueur
	 * @param sc
	 * @return
	 */
	private int demande_difficulte_du_jeu(Scanner sc) {
		System.out.println("Difficulte du jeu (1 <= M <= 4) ?");
		System.out.println("Le mode aleatoire est la difficulte 4.");
		try {
			int res = Integer.parseInt(sc.next());
			while (res > 4 || res < 1) {
				System.out.println("\nErreur de saisie : ce niveau de difficulte n'existe pas.");
				System.out.println("Difficulte du jeu (1 <= M <= 4) ?");
				System.out.println("Le mode aleatoire est la difficulte 4.");
				res = Integer.parseInt(sc.next());
			}
			return res;
		} catch (NumberFormatException e) {
			System.out.println("\nErreur de saisie, ce n'est pas un nombre. Recommencez.\n");
			return demande_difficulte_du_jeu(sc);
		}
	}
	
	/**
	 * Déroulement du jeu : le joueur fait bouger les pièces jusqu'à ce qu'il gagne
	 */
	private void deroulement_du_jeu() {
		p.partieEnCours.afficher();
		while(!p.win()) {
			int a = demander_choix();
			if(p.partieEnCours.deplacement_dans_le_tableau(a)) {
				p.partieEnCours.afficher();
			}
		}
		System.out.println("Feliciations, vous avez gagne !");
	}
	
	/**
	 * Demande au joueur le numéro de la case qu'il veut déplacer
	 * @return
	 */
	@SuppressWarnings("resource")
	private int demander_choix() {
		int res = 0;
		int max = p.partieEnCours.taille * p.partieEnCours.taille;
		System.out.println("\nQue voulez-vous deplacer ? (ecrivez le numero que vous voulez bouger)");
		Scanner sc = new Scanner(System.in);
		
		try {
			res = Integer.parseInt(sc.next());
			HashMap<Integer, Integer> adja = p.partieEnCours.adja(0);
			if(!adja.containsValue(res)) {
				System.out.println("\nErreur de saisie :.");
				System.out.println("- Soit vous avez saisi un numero qui ne peut pas bouger.");
				System.out.println("- Soit vous avez saisi un numero qui n est pas dans le tableau.");
				return demander_choix();
			}
			while (!(res >= 0 && res < max)) {
				System.out.println("\nErreur de saisie :.");
				System.out.println("- Soit vous avez saisi un numero qui ne peut pas bouger.");
				System.out.println("- Soit vous avez saisi un numero qui n est pas dans le tableau.\n");
				System.out.println("Que voulez-vous deplacer ? (ecrivez le numero que vous voulez bouger)");
				res = Integer.parseInt(sc.next());
				if(!adja.containsValue(res)) {
					System.out.println("\nErreur de saisie :.");
					System.out.println("- Soit vous avez saisi un numero qui ne peut pas bouger.");
					System.out.println("- Soit vous avez saisi un numero qui n est pas dans le tableau.");
					return demander_choix();
				}
			}
			return res;
		} catch (NumberFormatException e) {
			System.out.println("\nErreur de saisie, ce n'est pas un nombre. Recommencez.\n");
			return demander_choix();
		}
	}
}
