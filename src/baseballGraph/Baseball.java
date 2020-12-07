package baseballGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;



public class Baseball {

	private final class Graphe{

		int CAP_MAX = Integer.MAX_VALUE;
		ArrayList<SommetEquipe> sommetsEquipe;
		ArrayList<SommetPaire> sommetsPaire;
		ArrayList<Arete> aretes;
		Sommet source;
		Sommet puits;
		Team equipeCourante;

		public class Arete{
			public Sommet debut;
			public Sommet fin;
			public int valeur;
			public int capacite;

			public Arete(Sommet debut, Sommet fin, int capacite){
				this.debut = debut;
				this.fin = fin;
				this.capacite = capacite;
			}

			public Arete(){}
		}

		public class Sommet{
			public Sommet(){};
		}


		// Méthode de test
		public void printNodes(){
			for (SommetPaire i : this.sommetsPaire){
				System.out.println("NODE :");
				System.out.println("Equipe 1 : " + i.equipe1.name);
				System.out.println("Equipe 2 : " + i.equipe2.name);
			}
		}

		private final class SommetEquipe extends Sommet{
			public Team equipe;

			public SommetEquipe(Team equipe){
				this.equipe = equipe;
			}

		}

		public class SommetPaire extends Sommet{
			public Team equipe1;
			public Team equipe2;


			// Besoin de comparer les sommets pour sommetsPaire.contains()
			@Override public boolean equals(Object o){
				if ( !(o instanceof SommetPaire)){return false;}
				SommetPaire sp = (SommetPaire)o;
				return (
						(this.equipe1.id == sp.equipe1.id && this.equipe2.id == sp.equipe2.id)
						|| (this.equipe2.id == sp.equipe1.id && this.equipe1.id == sp.equipe2.id)
				);
			}
			// Nécessaire pour equals()
			@Override public int hashCode(){
				return 7 * equipe1.hashCode() + 13 * equipe2.hashCode();
			}

			public SommetPaire(Team equipe1, Team equipe2){
				this.equipe1 = equipe1;
				this.equipe2 = equipe2;
			}

		}

		private void generationSommets(ArrayList<Team> equipes){

			for (Team i: equipes){
				System.out.println(i);
				this.sommetsEquipe.add(new SommetEquipe(i));
			}
			for (Team i : equipes){
				for (Team j : equipes){
					SommetPaire sp = new SommetPaire(i, j);
					if (i != j && !sommetsPaire.contains(sp)) {
						this.sommetsPaire.add(new SommetPaire(i, j));
					}
				}
			}

		}

		private void generationAretes(){

			// Aretes source => sommetsPaire
			for (SommetPaire sp: sommetsPaire){
				int capacite = sp.equipe1.matchToPlayAgainst.get(sp.equipe2.id - 1);

				Arete a = new Arete(source, sp, capacite);
				this.aretes.add(a);
			}

			// Aretes sommetsEquipe => puits
			for (SommetEquipe se : sommetsEquipe){
				int capacite = equipeCourante.wins + equipeCourante.matchsToPlay - se.equipe.wins;
				Arete a = new Arete(se, puits, capacite);
				this.aretes.add(a);
			}

			// Aretes sommetsPaire => sommetsEquipe
			for (SommetPaire sp: sommetsPaire){
				for (SommetEquipe se: sommetsEquipe){
					if (sp.equipe2.id == se.equipe.id || sp.equipe1.id == se.equipe.id){
						Arete a = new Arete(sp, se, CAP_MAX);
						this.aretes.add(a);
					}
				}
			}
		}


		public void printAretes(){
			for (Arete a : aretes){
				System.out.println("Debut : " + a.debut);
				System.out.println("Fin : " + a.fin);
				System.out.println("Cap : " + a.capacite);
				System.out.println(" ");

			}
		}
		public Graphe(ArrayList<Team> equipes, Team k){
			equipeCourante = k;
			equipes.remove(k);
			this.sommetsPaire = new ArrayList<SommetPaire>();
			this.sommetsEquipe = new ArrayList<SommetEquipe>();
			this.aretes = new ArrayList<Arete>();

			generationSommets(equipes);

			generationAretes();
		}
	}

	private ArrayList<Team> teams = new ArrayList();
	Graphe graphe;
	
	public Baseball(String path) {
		try {
			File f = new File(path);
			Scanner sc = new Scanner(f);

			int nbEquipes = Integer.parseInt(sc.nextLine());
			
			
			while (sc.hasNextLine()) {
				String data = sc.nextLine();
				String[] splited= data.split(" ", 0);
				
				int id = Integer.parseInt(splited[0]);
				String name = splited[1];
				int wins = Integer.parseInt(splited[2]);
				int matchsToPlay = Integer.parseInt(splited[3]);
				
				
				ArrayList<Integer> matchToPlayAgainst = new ArrayList<>();
				
				for(int i=4;i<4+nbEquipes;i++) {
					matchToPlayAgainst.add(Integer.parseInt(splited[i]));
				}
				
				Team t = new Team(nbEquipes,id,name,wins,matchsToPlay,matchToPlayAgainst);
				this.teams.add(t);
				
			}
			sc.close();

			this.graphe = new Graphe(this.getTeams(), this.getTeams().get(0));

		} 
		catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		
	}
	
	
	public ArrayList<Team> getTeams() {
		return this.teams;
	}
	
	public void printTeams() {
		for (int i=0;i<teams.size();i++) {
			getTeams().get(i).printTeam();
		}
	}

	private final class Team {
		private int nbOfTeams;
		
		private int id;
		private String name;
		private int wins;
		private int matchsToPlay;
		
		private ArrayList<Integer> matchToPlayAgainst = new ArrayList<>();
		
		private  Team(int nbOfTeams_,int id_, String name_, int wins_, int matchsToPlay_, ArrayList<Integer> matchToPlayAgainst_) {
			this.nbOfTeams = nbOfTeams_;
			this.id = id_;
			this.name = name_;
			this.wins = wins_;
			this.matchsToPlay = matchsToPlay_;
			this.matchToPlayAgainst = (ArrayList<Integer>) matchToPlayAgainst_.clone();
		}
		
		public void printTeam() {
			System.out.println("Il y a " + this.nbOfTeams + " Equipes");
			System.out.println("Equipe numéro :  " + this.id + " Nom : " + this.name + " Nombre de victoires : " + this.wins + " Matchs restants à jouer : " + this.matchsToPlay);
			
			for (int i=0;i<this.matchToPlayAgainst.size();i++) {
				System.out.println("Nombre de matchs à jouer contre l'équipe " + (i+1) + " : " + this.matchToPlayAgainst.get(i));
			}
			System.out.println(" ");
		}
		
		
	}
	
	public static void main(String[] args) {
		Baseball b = new Baseball("/home/sylouan/Downloads/teams.txt");
		b.printTeams();
		b.graphe.printNodes();
		b.graphe.printAretes();
	}

}
