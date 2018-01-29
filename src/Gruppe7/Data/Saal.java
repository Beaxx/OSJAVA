package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 *
 * Ein Saal ist ein von der Vorstellung losgelöstes Objekt. Säle sind austauschbare Einheiten, die unabhängig vom
 * Spielplan sind. Sie sind eine der Komponenten zur Erstellung von Vorstellungen.
 */
public class Saal {
    private int plaetzeLoge;
    private int plaetzeParkett;
    private boolean threeD;
    private int saalNummer;

    /**
     * Konstruktor
     *
     * @param in_PlaetzeLoge    Anzahl der Plätze in der Loge des Saals
     * @param in_PlaetzeParkett Anzahl der Plätze im Parkett des Saals
     * @param in_3D             Fähigkeit des Saals 3D-Filme abspielen zu können
     * @param in_GetSaalNummer  Die Nummer des Saals (gleichzeitig seine Bezeichnung)
     */
    public Saal(int in_PlaetzeLoge, int in_PlaetzeParkett, boolean in_3D, int in_GetSaalNummer) {

        plaetzeLoge = in_PlaetzeLoge;
        plaetzeParkett = in_PlaetzeParkett;
        threeD = in_3D;
        saalNummer = in_GetSaalNummer;
    }

    //Getter

    /**
     * @return die Anzahl der Plätze im Parkett des Saals
     */
    public int GetPlaetzeLoge() {
        return plaetzeLoge;
    }

    /**
     * @return die Anzahl der Plätze in der Loge des Saals
     */
    public int GetPlaetzeParkett() {
        return plaetzeParkett;
    }

    /**
     * @return die Fähigkeit des Saals 3D-Filme abzuspielen
     */
    public boolean GetThreeD() {
        return threeD;
    }

    /**
     * @return die Nummer des Saals
     */
    public int GetSaalNummer() {
        return saalNummer;
    }
}
