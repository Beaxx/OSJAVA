package Gruppe7.Exporter;

/**
 * @author Lennart Völler
 * Enum zur Verwaltung von Wochentagen.
 */
public enum Wochentage {

    Montag(0),
    Dienstag(1),
    Mittwoch(2),
    Donnerstag(3),
    Freitag(4),
    Samstag(5),
    Sonntag(6);

    /**
     * in_TagIndex wird nicht genutzt, muss für enum-Definition jedoch verwendet werden.
     *
     * @param in_TagIndex der Wochentag.
     */
    Wochentage(int in_TagIndex) {
    }


    /**
     * ToString Override, um den Wochentag lesbar darzustellen.
     *
     * @return ein Wochentag
     */
    public String ToString() {
        switch (this){
            case Montag:
                return "Montag";
            case Dienstag:
                return "Dienstag";
            case Mittwoch:
                return "Mittwoch";
            case Donnerstag:
                return "Donnerstag";
            case Freitag:
                return "Freitag";
            case Samstag:
                return "Samstag";
            case Sonntag:
                return "Sonntag";
            default:
                return "------";
        }
    }
}
