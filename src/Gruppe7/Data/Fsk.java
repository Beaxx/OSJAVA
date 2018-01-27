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
    Fsk(String fskName){
        this.fskName = fskName;
    }

    public String getFskName(){
        return this.fskName;
    }
}