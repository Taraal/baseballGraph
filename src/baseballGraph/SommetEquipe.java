package baseballGraph;

public class SommetEquipe  extends Sommet{
    public Baseball.Team equipe;

    public SommetEquipe(Baseball.Team equipe, int hauteur, int excedent){
        this.equipe = equipe;
        this.hauteur = hauteur;
        this.excedent = excedent;
    }

    public SommetEquipe(){}
}
