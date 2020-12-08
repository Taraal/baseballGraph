package baseballGraph;

import java.util.ArrayList;

public class Graphe{

    int CAP_MAX = Integer.MAX_VALUE;
    ArrayList<SommetEquipe> sommetsEquipe;
    ArrayList<SommetPaire> sommetsPaire;
    ArrayList<Arete> aretes;
    Sommet source;
    Sommet puits;
    Baseball.Team equipeCourante;
    Graphe residuel;

    // Méthode de test
    public void printNodes(){
        for (SommetPaire i : this.sommetsPaire){
            System.out.println("NODE :");
            System.out.println("Equipe 1 : " + i.equipe1.name);
            System.out.println("Equipe 2 : " + i.equipe2.name);
        }
    }





    private void generationSommets(ArrayList<Baseball.Team> equipes){

        for (Baseball.Team i: equipes){
            System.out.println(i);
            this.sommetsEquipe.add(new SommetEquipe(i));
        }
        for (Baseball.Team i : equipes){
            for (Baseball.Team j : equipes){
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
            sp.aretes.add(a);
            this.aretes.add(a);
        }

        // Aretes sommetsEquipe => puits
        for (SommetEquipe se : sommetsEquipe){
            int capacite = equipeCourante.wins + equipeCourante.matchsToPlay - se.equipe.wins;
            Arete a = new Arete(se, puits, capacite);
            this.aretes.add(a);
            se.aretes.add(a);
        }

        // Aretes sommetsPaire => sommetsEquipe
        for (SommetPaire sp: sommetsPaire){
            for (SommetEquipe se: sommetsEquipe){
                if (sp.equipe2.id == se.equipe.id || sp.equipe1.id == se.equipe.id){
                    Arete a = new Arete(sp, se, CAP_MAX);
                    this.aretes.add(a);
                    sp.aretes.add(a);
                    se.aretes.add(a);
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
    public Graphe(ArrayList<Baseball.Team> equipes, Baseball.Team k){
        equipeCourante = k;
        equipes.remove(k);
        this.sommetsPaire = new ArrayList<SommetPaire>();
        this.sommetsEquipe = new ArrayList<SommetEquipe>();
        this.aretes = new ArrayList<Arete>();

        generationSommets(equipes);

        generationAretes();
    }

    public void InitialiserPreflot(){

        source.hauteur = sommetsPaire.size() + sommetsEquipe.size();
        puits.hauteur = 0;
        for (Sommet s: sommetsEquipe){
            s.hauteur = 0;
        }
        for (Sommet s: sommetsPaire){
            s.hauteur = 0;
        }
        residuel = this;
        residuel.aretes = new ArrayList<Arete>();

        for (Arete a: aretes){
            if (a.debut == source) {
                a.flot = a.capacite;
                Arete a_residuel = new Arete();
                a_residuel.capacite = a.flot;

            }else{
                a.flot = 0;
            }
        }
    }

}