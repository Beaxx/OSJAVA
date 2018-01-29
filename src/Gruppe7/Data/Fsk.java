package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 * Enum zur Verwaltung von FSK-Einstufungen.
 */
public enum Fsk {

    FSK_0("0"),
    FSK_6("6"),
    FSK_12("12"),
    FSK_16("16"),
    FSK_18("18");

    /**
     * fskName wird nicht genutzt, muss für enum-Definition jedoch verwendet werden.
     *
     * @param in_fskName Die Bezeichnung der FSK Einstufung.
     */
    Fsk(String in_fskName) {
        String fskName = in_fskName;
    }
}