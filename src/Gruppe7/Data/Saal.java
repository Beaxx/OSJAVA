package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 */
public class Saal {
    private int plaetzeLoge;
    private int plaetzeParkett;
    private boolean threeD;
    private int saalNummer;

    /**
     * Saalkonstruktor erstellt Saalobjekt
     * @param in_PlaetzeLoge Anzahl der Plätze in der Loge des Saals
     * @param in_PlaetzeParkett Anzahl der Plätze im Parkett des Saals
     * @param in_3D Fähigkeit des Saals 3D-Filme abspielen zu können
     * @param in_getSaalNummer Die Nummer des Saals (gleichzeitig seine Bezeichnung)
     */
    public Saal(int in_PlaetzeLoge, int in_PlaetzeParkett, boolean in_3D, int in_getSaalNummer) {

        plaetzeLoge = in_PlaetzeLoge;
        plaetzeParkett = in_PlaetzeParkett;
        threeD = in_3D;
        saalNummer = in_getSaalNummer;
    }

    //Getter

    /**
     *
     * @return die Anzahl der Plätze im Parkett des Saals
     */
    public int getPlaetzeLoge() {
        return plaetzeLoge;
    }

    /**
     *
     * @return die Anzahl der Plätze in der Loge des Saals
     */
    public int getPlaetzeParkett() {
        return plaetzeParkett;
    }

    /**
     *
     * @return die Fähigkeit des Saals 3D-Filme abzuspielen
     */
    boolean getThreeD() {
        return threeD;
    }

    /**
     *
     * @return die Nummer des Saals
     */
    int getSaalNummer() {
        return saalNummer;
    }
}
