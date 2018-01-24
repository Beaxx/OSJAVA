package Gruppe7.Data;

public class Saal {
    //Attribute
    private int plaetzeLoge;
    private int plaetzeParkett;
    private boolean threeD;
    private int saalNummer;

    //Constructor
    public Saal(int in_PlaetzeLoge, int in_PlaetzeParkett, boolean in_3D, int in_getSaalNummer) {

        plaetzeLoge = in_PlaetzeLoge;
        plaetzeParkett = in_PlaetzeParkett;
        threeD = in_3D;
        saalNummer = in_getSaalNummer;

//        System.out.println("  Klasse Saal: Saal Instanz erzeugt!");
//        System.out.println("  Klasse Saal Nr: "+saalNummer);
//        System.out.println("  Klasse Saal Platze Parkett: "+plaetzeParkett);
//        System.out.println("  Klasse Saal Plätze Loge: "+plaetzeLoge);
//        System.out.println("  Klasse Saal 3D Fähig: "+ threeD);
//
//        System.out.println("--------------------------------------");
    }

            //Getter & Setter

    public int getPlaetzeLoge() {
        return plaetzeLoge;
    }

    public int getPlaetzeParkett() {
        return plaetzeParkett;
    }

    public boolean getThreeD() { // TODO: wurde packacge exclusive gemacht, exception?
        return threeD;
    }

    public int getSaalNummer() {
        return saalNummer;
    }
}
