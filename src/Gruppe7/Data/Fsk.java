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
     * in_FskName wird nicht genutzt, muss für enum-Definition jedoch verwendet werden.
     *
     * @param in_FskName Die Bezeichnung der FSK Einstufung.
     */
    Fsk(String in_FskName) {
    }
}