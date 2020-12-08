package baseballGraph;

import baseballGraph.Baseball;

public class SommetPaire extends Sommet {
    public Baseball.Team equipe1;
    public Baseball.Team equipe2;


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

    public SommetPaire(Baseball.Team equipe1, Baseball.Team equipe2){
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
    }

}