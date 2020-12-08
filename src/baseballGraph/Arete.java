package baseballGraph;

public class Arete{
    public Sommet debut;
    public Sommet fin;
    public int capacite;
    public int flot;

    public Arete(Sommet debut, Sommet fin, int capacite){
        this.debut = debut;
        this.fin = fin;
        this.capacite = capacite;
    }

    public Arete(){}
}
