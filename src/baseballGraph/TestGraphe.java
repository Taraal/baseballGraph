package baseballGraph;

import java.util.ArrayList;

public class TestGraphe {
    int flotMax;


    ArrayList<Sommet> sommets = new ArrayList<Sommet>();
    ArrayList<Arete> aretes = new ArrayList<Arete>();

    public TestGraphe(){

        // Création de la source
        sommets.add(new Sommet(0, 0));

        for(int i = 0; i < 4; i++){
            sommets.add(new Sommet(0, 0));
        }

        // Création du puits
        sommets.add(new Sommet(0,0));

        generationAretes();

        this.flotMax = flotMaximal(0, sommets.size() - 1);

    }

    public void generationAretes(){
        int s = 0;
        int t = sommets.size() - 1;
        this.addArete(s, 1, 16);
        this.addArete(s,2, 13);
        this.addArete(1,2, 10);
        this.addArete(1,3, 12);
        this.addArete(3,2, 9);
        this.addArete(2,4, 14);
        this.addArete(4, 3, 7);
        this.addArete(3, t, 20);
        this.addArete(4,t, 4);

    }

    boolean pousser(int u){
        // Pour chaque arete
        for(int i = 0; i < aretes.size(); i++){

            // Si l'arete commence bien par le sommet désiré
            if (aretes.get(i).debut == u){
                // Si son flot est déjà égal à sa capacité, on passe à l'arete suivante
                if (aretes.get(i).flot == aretes.get(i).capacite){
                    continue;
                }

                // Si le sommet de début est plus élevé que le sommet de fin
                if(sommets.get(u).hauteur > sommets.get(aretes.get(i).fin).hauteur){

                    // On prend le minimum entre l'excédent de U et le flot restant de l'arete
                    int flot = Math.min(aretes.get(i).capacite - aretes.get(i).flot,
                            sommets.get(u).excedent);

                    // Le sommet U vide son excedent
                    sommets.get(u).excedent -= flot;

                    // Le sommet V se remplit
                    sommets.get(aretes.get(i).fin).excedent += flot;

                    // L'arc se remplit
                    aretes.get(i).flot += flot;

                    // On met à jour le graphe résiduel
                    grapheResiduel(i, flot);

                    // Tant que l'on peut pousser, on retourne Vrai
                    return true;
                }
            }
        }
        return false;
    }

    void elever(int u){
        int hauteur_min = Integer.MAX_VALUE;

        // On cherche la hauteur minimum des voisins
        for (int i = 0; i < aretes.size(); i++){


            if(aretes.get(i).debut == u){

                // Si elle est déjà à sa capacité max, on l'ignore
                if(aretes.get(i).flot == aretes.get(i).capacite){
                    continue;
                }

                // On met à jour la hauteur minimum
                if(sommets.get(aretes.get(i).fin).hauteur < hauteur_min){
                    hauteur_min = sommets.get(aretes.get(i).fin).hauteur;

                    // On élève le sommet u
                    sommets.get(u).hauteur = hauteur_min + 1;
                }
            }
        }
    }

    void preflot(int s){
        sommets.get(s).hauteur = sommets.size();

        for (int i = 0; i< aretes.size(); i++){
            Arete e = aretes.get(i);
            if(e.debut == s){
                e.flot = e.capacite;
                sommets.get(e.fin).excedent += e.flot;
                aretes.add(new Arete(-e.flot, 0, e.fin, s));
            }
        }
    }

    int sommetDebordant(ArrayList<Sommet> sommets, int s, int t){
        for(int i = 1; i<sommets.size() - 1; i++){
            if (i != s && i != t && sommets.get(i).excedent > 0){
                return i;
            }
        }
        return -1;
    }

    void grapheResiduel(int i, int flot){
        // On crée une arete dans le graphe résiduel quand un flot a été ajouté à l'arete i

        int u = aretes.get(i).fin;
        int v = aretes.get(i).debut;

        for (int j = 0; j < aretes.size(); j++){
            if (aretes.get(j).fin == v && aretes.get(j).debut == u){
                aretes.get(j).flot -= flot;
                return;
            }
        }

        Arete e = new Arete(-flot, 0, u, v);
        aretes.add(e);
    }

    public void addArete(int u, int v, int capacity){
        aretes.add(new Arete(0, capacity, u , v));
    }

    int flotMaximal(int s, int t){
        preflot(s);
        while(sommetDebordant(sommets, s, t ) != -1){
            int u = sommetDebordant(sommets, s, t);
            if (!pousser(u)){
                elever(u);
            }
        }
        return  sommets.get(t).excedent;
    }

    public boolean equipeEliminee(){
        boolean eliminee = false;

        for(Arete a: aretes){
            if(a.debut == 0){
                if (a.capacite != a.flot){
                    eliminee = true;
                }
            }
        }
        return eliminee;
    }


}
