package Gruppe7.Exporter;

public enum Wochentage {

    Montag(0),
    Dienstag(1),
    Mittwoch(2),
    Donnerstag(3),
    Freitag(4),
    Samstag(5),
    Sonntag(6);

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
