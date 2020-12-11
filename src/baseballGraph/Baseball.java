package baseballGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Baseball {

	public ArrayList<Team> teams = new ArrayList();
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

			Collections.sort(this.teams, new Comparator<Team>() {
				@Override
				public int compare(Team a, Team b) {
					return (a.wins + a.matchsToPlay) > (b.wins + b.matchsToPlay) ? - 1
							: (a.wins + a.matchsToPlay) < (b.wins + b.matchsToPlay) ? 1
							: 0;
				}
			});

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

	public final class Team {
		public int nbOfTeams;
		
		public int id;
		public String name;
		public int wins;
		public int matchsToPlay;
		public boolean eliminee = false;
		
		public ArrayList<Integer> matchToPlayAgainst = new ArrayList<>();
		
		public  Team(int nbOfTeams_,int id_, String name_, int wins_, int matchsToPlay_, ArrayList<Integer> matchToPlayAgainst_) {
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

	public void testEliminationEquipe(Team k){

		ArrayList<Team> equipes = this.getTeams();
		this.graphe = new Graphe(equipes, k);
		System.out.println("Flot Maximal pour " + k.name + " : " + graphe.flotMax);

		k.eliminee = this.graphe.equipeEliminee();
	}

	public void testEliminationToutesEquipes(){

		for (Team t: this.getTeams()){
			testEliminationEquipe(t);
		}

	}

	public void testDeuxEliminationToutesEquipes(){

		for(int i = 0; i < this.getTeams().size(); i++){
			// On se doit de tester au minimum une équipe
			if (i == 0){
				testEliminationEquipe(getTeams().get(i));
			}
			// Si l'équipe précédente a été éliminée, on peut déduire que l'équipe actuelle le sera aussi
			else if (this.getTeams().get(i-1).eliminee == true){
				System.out.println(getTeams().get(i-1).name + " a été éliminée, donc : ");
				getTeams().get(i).eliminee = true;
			}
			// Sinon, on teste son élimination
			else{
				testEliminationEquipe(getTeams().get(i));
			}

			System.out.println(getTeams().get(i).name + " éliminée : " + getTeams().get(i).eliminee);

			System.out.println(" ");
		}

	}

	public static void main(String[] args) {
		System.out.println("Entrez le nom du fichier à scanner");
		Scanner input = new Scanner(System.in);
		String name = input.nextLine();
		Baseball b = new Baseball("tests/" + name +".txt");

		//b.testEliminationEquipe(b.getTeams().get(3));
		b.testDeuxEliminationToutesEquipes();



	}

}
