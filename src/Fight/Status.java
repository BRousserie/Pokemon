package Fight;

import View.FightViewController;

public enum Status {
    BRULURE, POISON, PARALYSIE, SOMMEIL, GEL, KO, OK;

    public boolean pkmnCanAttack(String pkmnName, FightViewController view) {
        switch (this) {
            case SOMMEIL:
//                view.showMessage(pkmnName + " dort, il ne peut pas attaquer.");
                return false;
            case GEL:
//                view.showMessage(pkmnName + " est gelé, il ne peut pas attaquer.");
                return false;
            case PARALYSIE:
//                view.showMessage(pkmnName + " est paralysé, il ne peut pas attaquer.");
                return false;
            default:
                return true;
        }
    }

    public String getStatusMessage(String pkmnName) {
        String output = pkmnName + " souffre de " + this + " ! ";

        switch (this) {
            case SOMMEIL:
                return output + pkmnName + " dort, il ne peut pas attaquer.";
            case GEL:
                return output + pkmnName + " est gelé, il ne peut pas attaquer.";
            case PARALYSIE:
                return output + pkmnName + " est paralysé, il ne peut pas attaquer.";
            default:
                return output;
        }

    }

    public int getCaptureBonus() {
        switch (this) {
            case BRULURE:
            case POISON:
            case PARALYSIE:
                return 12;
            case SOMMEIL:
            case GEL:
                return 25;
            default:
                return 0;
        }
    }

}
