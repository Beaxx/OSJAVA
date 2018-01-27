package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 */
public class Saal {
    private int plaetzeLoge;
    private int plaetzeParkett;
    private boolean threeD;
    private int saalNummer;

    public Saal(int in_PlaetzeLoge, int in_PlaetzeParkett, boolean in_3D, int in_getSaalNummer) {

        plaetzeLoge = in_PlaetzeLoge;
        plaetzeParkett = in_PlaetzeParkett;
        threeD = in_3D;
        saalNummer = in_getSaalNummer;
    }

    //Getter
    public int getPlaetzeLoge() {
        return plaetzeLoge;
    }

    public int getPlaetzeParkett() {
        return plaetzeParkett;
    }

    boolean getThreeD() {
        return threeD;
    }

    int getSaalNummer() {
        return saalNummer;
    }
}
