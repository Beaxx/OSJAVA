package Gruppe7.Data;

/**
 * Erbt von Film.
 * @author Fabian Ueberle
 */
public class Werbefilm extends Film {
    private int umsatzProZuschauer;

    public Werbefilm(String in_titel, int in_laufzeit, int in_umsatzProZuschauer) {
        super(in_titel, in_laufzeit);
        umsatzProZuschauer = in_umsatzProZuschauer;
    }

    /**
     * Getmethode für den Umsatz pro Zuschauer eines Werbefilms
     * @return der Umsatz pro Zuschauer eines Werbefilms.
     */
    public int getUmsatzProZuschauer() {
        return umsatzProZuschauer;
    }
}
