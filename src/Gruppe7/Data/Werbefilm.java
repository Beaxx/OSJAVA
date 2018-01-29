package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 * Erbt von Film.
 *
 * Ein Werbefilm ist ein von der Vorstellung losgelöstes Objekt. Werbefilme sind austauschbare Einheiten, die unabhängig
 * vom Spielplan sind. Sie sind eine der Komponenten zur Erstellung von Vorstellungen.
 */
public class Werbefilm extends Film {
    private int umsatzProZuschauer;

    public Werbefilm(String in_Titel, int in_Laufzeit, int in_UmsatzProZuschauer) {
        super(in_Titel, in_Laufzeit);
        umsatzProZuschauer = in_UmsatzProZuschauer;
    }

    /**
     * Getmethode für den Umsatz pro Zuschauer eines Werbefilms
     *
     * @return der Umsatz pro Zuschauer eines Werbefilms.
     */
    public int GetUmsatzProZuschauer() {
        return umsatzProZuschauer;
    }

}
