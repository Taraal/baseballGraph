package baseballGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



public class Baseball {
	
	private ArrayList<Team> teams = new ArrayList();
	
	
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
	

	private static final class Team {
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
		Baseball b = new Baseball("/home/keason/Bureau/baseballGraph/teams.txt");
		b.printTeams();
	}

}
