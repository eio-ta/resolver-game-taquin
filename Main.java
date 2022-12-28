import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import launcher.Modele;
import launcher.VueGUI;
import launcher.VueTerminal;
import objects.Plateau;

public class Main {

	/**
	 * Fonction principale / Fonction MAIN
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		if(args.length == 0) afficherAide();
		else {
			if(args.length == 1) {
				if(args[0].equals("GUI")) launcher(true);
				else if(args[0].equals("terminal")) launcher(false);
				else afficherAide();
			} else if (args.length == 3) {
				if(args[0].startsWith("--algo=") && args[1].startsWith("--taille=") && args[2].startsWith("--niveau=")) {
					// ON RÉCUPÈRE LA TAILLE DU JEU
					int taille = quelleTaille(args);
					// ON RECUPERE LA DIFFICULTE
					int niveau = quelNiveau(args);
					// ON RECUPERE L'ALGO
					String[] p1 = args[0].split("=");
					if(stats(p1[1])) {
						application(taille, niveau, p1[1]);
					}
				} else {
					afficherAide();
				}
			} else {
				afficherAide();
			}
		}
	}
	
	/**
	 * Lance la partie sur les différentes interfaces
	 * Si l'argument est "true" alors il lance l'interface graphique.
	 * Si l'argument est "false" alors il lance l'interface du terminal.
	 * @param inter
	 */
	public static void launcher(boolean inter) {
		if(inter) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			 int lar = screenSize.width/3 + screenSize.width/5;
			int lon = screenSize.height/2 + 30;
			VueGUI frame = new VueGUI(lar, lon);
			frame.setSize(lar, lon);
			frame.setVisible(true);
			frame.setMinimumSize(new Dimension(lar, lon));
			frame.setAlwaysOnTop(true);
			frame.setLocation((screenSize.width-frame.getWidth())/2,(screenSize.height-frame.getHeight())/2);
		} else new VueTerminal(); 
	}
	
	/**
	 * Demande la taille de jeu au joueur
	 * @param args
	 * @return
	 */
	private static int quelleTaille(String[] args) {
		String[] p1 = args[1].split("=");
		try {
			int t = Integer.parseInt(p1[1]);
			if(t <= 1) {
				System.out.println("Vous avez choisi une taille trop petite.\n Voici la taille minimum d'un jeu normal.");
				return 2;
			}
			else return t;
		} catch (NumberFormatException e) {
			afficherAide();
			return -1;
		}
	}
	
	/**
	 * Demande le niveau du jeu au joueur
	 * @param args
	 * @return
	 */
	private static int quelNiveau(String[] args) {
		String[] p1 = args[2].split("=");
		try {
			int t = Integer.parseInt(p1[1]);
			if(t > 4) return 4;
			else if(t < 1) return 1;
			else return t;
		} catch (NumberFormatException e) {
			afficherAide();
			return -1;
		}
	}
	
	/**
	 * Fait un exemple d'application d'un jeu avec une taille et un niveau donné
	 * @param taille
	 * @param difficulte
	 * @param algo
	 * @return
	 */
	private static boolean application(int taille, int difficulte, String algo) {
		System.out.println("EXEMPLE D'APPLICATION : \n");
		Plateau p = new Plateau(taille, difficulte);
		
		
		if(algo.equals("all")) {
			afficherResultat("pl", p, true);
			System.out.println("\n\n STATISTIQUES : \n");
			String[] algos = {"pl", "dj", "astar", "astarM"};
			for(int i=0; i<algos.length; i++) {
				afficherResultat(algos[i], p, false);
			}
			return true;
		} else {
			System.out.println("Si rien ne s'affiche, c'est sans doute que le programme prend du temps pour executer ce que vous voulez faire.\n");
			return afficherResultat(algo, new Plateau(taille, difficulte), true);
		}
	}
	
	/**
	 * Affiche les résultats d'un algo
	 * @param algo
	 * @param p
	 * @param result
	 * @return
	 */
	private static boolean afficherResultat(String algo, Plateau p, boolean result) {
		String[] a = Modele.afficherResultat(algo, p, result);
		System.out.println();
		for(int i=0; i<a.length; i++) {
			System.out.println(a[i]);
		}
		return true;
	}	
	
	/**
	 * Ouvre un PDF avec toutes les stats
	 * @param p1
	 * @return
	 * @throws IOException
	 */
	private static boolean stats(String p1) throws IOException {
		if(p1.equals("pl") || p1.equals("dj") || p1.equals("astar") || p1.equals("astarM")) {
			System.out.println("Voici les statistiques : ");
		}
		return Modele.stats(p1);
	}
	
	/**
	 * Affiche les aides des commandes
	 */
	public static void afficherAide() {
		System.out.println("Bonjour. Vous avez saisi sans doute oublie/mal écrit un parametre :");
		
		System.out.println("Si vous voulez jouer avec l'interface graphique, alors ecrivez : java Main GUI");
		System.out.println("Si vous voulez jouer sur le terminal, alors ecrivez : java Main terminal\n");
		
		System.out.println("Voici un petit rappel pour les tests !");
		System.out.println("Vous devez ecrire, pour utiliser cette classe :");
		System.out.println("\"--algo=[...]\" pour choisir l'algorithme que vous voulez tester. (Choix possibles : \"pl\", \"dj\", \"astar\", \"astarManhattan\" et \"all\")");
		System.out.println("\"--taille=[...] pour choisir la taille de votre jeu");
		System.out.println("\"--niveau=[...] pour choisir la difficulte de votre jeu (attention, le niveau 4 est definie par le niveau aleatoire).");
		System.out.println("Exemple de commande :\"java Main --algo=pl --taille=4 --niveau=2\"");
		
	}
}
