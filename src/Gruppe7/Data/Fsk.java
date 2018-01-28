package Gruppe7.Data;

/**
 * @author Fabian Ueberle
 */
public enum Fsk {

    FSK_0("0"),
    FSK_6("6"),
    FSK_12("12"),
    FSK_16("16"),
    FSK_18("18");

    private String fskName;

    /**
     *
     * @param fskName die Bezeichnung eines FSK-Siegels
     */
    Fsk(String fskName){
        this.fskName = fskName;
    }

    /**
     * Die Gethmethode für die Bezeichnung eines FSK-Siegels
     * @return die Bezeichnung des FSK-Siegels.
     */
    public String getFskName(){
        return this.fskName;
    }
}