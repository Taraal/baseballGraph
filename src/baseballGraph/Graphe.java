package baseballGraph;

import java.util.ArrayList;

public class Graphe {

    int taille;

    ArrayList<Sommet> sommets;
    ArrayList<Arete> aretes;

    boolean pousser(int u){
        for(int i = 0; i < aretes.size(); i++){
            if (aretes.get(i).debut == u){
                if (aretes.get(i).flot == aretes.get(i).capacite){
                    continue;
                }

                if(sommets.get(u).hauteur > sommets.get(aretes.get(i).fin).hauteur){

                    int flow = Math.min(aretes.get(i).capacite - aretes.get(i).flot,
                            sommets.get(u).excedent);

                    sommets.get(u).excedent -= flow;

                    sommets.get(aretes.get(i).fin).excedent += flow;

                    aretes.get(i).flot += flow;

                    updateReversedFlow(i, flow);

                    return true;
                }
            }
        }
        return false;
    }

    void elever(int u){
        int hauteur_max = Integer.MAX_VALUE;

        for (int i = 0; i < aretes.size(); i++){
            if(aretes.get(i).debut == u){

                if(aretes.get(i).flot == aretes.get(i).capacite){
                    continue;
                }

                if(sommets.get(aretes.get(i).fin).hauteur < hauteur_max){
                    hauteur_max = sommets.get(aretes.get(i).fin).hauteur;

                    sommets.get(u).hauteur = hauteur_max + 1;
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

    int sommetDebordant(ArrayList<Sommet> sommets){
        for(int i = 1; i<sommets.size(); i++){
            if (sommets.get(i).excedent > 0){
                return i;
            }
        }
        return -1;
    }

    void updateReversedFlow(int i, int flow){
        int u = aretes.get(i).fin;
        int v = aretes.get(i).debut;

        for (int j = 0; j < aretes.size(); j++){
            if (aretes.get(j).fin == v && aretes.get(j).debut == u){
                aretes.get(j).flot -= flow;
                return;
            }
        }

        Arete e = new Arete(0, flow, u, v);
        aretes.add(e);
    }

    public Graphe(int taille){
        this.taille = taille;
        for (int i = 0; i < taille; i++){
            sommets.add(new Sommet(0,0));
        }
    }

    public void addArete(int u, int v, int capacity){
        aretes.add(new Arete(0, capacity, u , v));
    }

    int flotMaximal(int s, int t){
        preflot(s);
        while(sommetDebordant(sommets) != -1){
            int u = sommetDebordant(sommets);
            if (!pousser(u)){
                elever(u);
            }
        }
        return  sommets.get(sommets.size() - 1).excedent;
    }

}
