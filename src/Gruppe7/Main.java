package Gruppe7;

import java.io.IOException;

import Gruppe7.Data.*;
import Gruppe7.Logic.*;
import Gruppe7.Importer.*;

/**
 * Zentraler Programeinstieg
 *
 * SETTINGS:
 * /plaeneZuErstellen/
 * Wie viele gültige Spielpläne sollen erstellt werden? Diese Zahl bestimmt über die Dauer des Programmablaufs.
 * Die Zahl wirkt sich maßgeblich auf dei Dauer des Programmablaufs aus. Wird die Optimierung der Pläne
 * ausgeschaltet (OptimierungSwitch = false) sind Geschwindigkeiten von über 4000 Plänen / Sekunde möglich.
 *
 * /OptimierungsSwitch/
 * Der Konstruktor der Planer-Klassen enthält einen If-Wrapper, der über den Zustand der OptimierungsSwitch
 * Variable geschaltet wird. Diese sorgt dafür, dass der Optimierungsalgorithmus für alle Vorstellungen den
 * optimalen Preis im Intervall von [mindestPreisVorstellung, maximalPreisVortellung] ermittelt. Hierdurch
 * ergeben sich bessseren Pläne, jedoch starke Performanceeinbußen.
 *
 * /mindestPreisVorstellung/
 * Der niedrigste Preis, den eine Vorstellung haben darf. Wird für die Optimierung verwendet. Ist die
 * Optimierung abgeschaltet hat diese Variable keinen Effekt.
 *
 * /maximalPreisVorstellung/
 * Der höchste Preis, den eine Vorstellung haben darf. Wird für die Optimierung verwendet. Ist die
 * Optimierung abgeschaltet hat diese Variable keinen Effekt. Sollte nicht größer als 26 gesetzt werden, da der Andrang
 * für eine Vorstellung bei Eintrittspreis >26€ null/negativ wird.
 *
 * WICHTIG: Die Differenz zwischen Mindest- und Max-Eintrittspreis entscheidet über Geschwindigkeit des programs
 * da jede Wert von MAx -> Min ausprobiert werden muss. Hier ein paar Beispielwerte für die Performance:
 *
 * Zufällige Erstellung ohne Optimierung mit festem Eintrittspreis: 2000 Pläne / Sekunde
 * Delta = 0: 9 Pläne / Sekunde (z. B. Min: 17, Max: 17) outdated
 * Delta = 1: 4 Pläne / Sekunde (z. B. Min: 17, Max: 18) outdated
 * Delta = 2: 3.2 Pläne / Sekunde (z. B. Min: 16, Max: 18) outdated
 * Delta = 3: 2.2 Pläne / Sekunde (z. B. Min: 16, Max: 19) outdated
 *
 * /mindestBeliebtheit/
 * Setzt den Schwellenwert für die Beliebtheit eines Films. Filme mit niedrigerer Beliebtheit werden nicht importiert.
 * Hat keinen Einfluss auf die Performance, aber auf das Endergebnis. Je niedriger die Mindestbeliebtheit ist, desto
 * größer ist die Auswahl an Filmen für einen Spielplan. Es sind dementsprechend mehr Optimierungsläufe durchzufüren,
 * bis ein Plan in der Region der wirtschaftlich optimalen Pläne gefunden wurde.
 *
 * @author Lennart Völler, Nicole Diestler, Fabian Ueberle
 */

public class Main {

    public static boolean OptimierungSwitch;

    public static void main(String[] args) throws IOException {

        /* SETTINGS */
        int plaeneZuErstellen = 100000;
        OptimierungSwitch = false;
        int mindestPreisVorstellung = 14;
        int maximalPreisVorstellung = 15;
        int mindestBeliebtheit = 96;
        /* /SETTINGS */

        // Datenimport
        new WerbefilmImporter("C:/import/werbespots.csv");
        new SaalImporter("C:/import/saele.csv");
        new KinofilmImporter("C:/import/filme.csv", mindestBeliebtheit);

        // FilmArrays erstellen
        FilmVerwaltung.FilmArraysHelper();

        // Werbeplan sortieren und Standard 20 Minunten Block festlegen
        WerbefilmVerwaltung.werbeplanSortieren();
        WerbefilmVerwaltung.standardWerbeblock();

        // Saele sortieren
        SaalVerwaltung.saalplanSortieren();
        SaalVerwaltung.plaetzteInGroestemUndZweitgroestemSaal();

        // Performance Wrapper start
        long startTime = System.currentTimeMillis();

            // Algorithmus
            Planer planer = new Planer(mindestPreisVorstellung, maximalPreisVorstellung);
            for (int i = 0; i < plaeneZuErstellen; i++)

            {
                Planer tempPlaner = new Planer(mindestPreisVorstellung, maximalPreisVorstellung);

                if (tempPlaner.getSpielplanGewinn() > planer.getSpielplanGewinn()) {
                    planer = tempPlaner;
                    System.out.println("Ticket Einnahmen: " + planer.getSpielplanEinnahmenAusKartenverkaeufen() + "\n" +
                            "Werbungs Einnahmen: " + planer.getSpielplanWerbungsEinnahmen() + "\n" +
                            "Ausgaben: " + planer.getSpielplanAusgaben() + "\n" +
                            "Gewinn:" + planer.getSpielplanGewinn() + "\n" +
                            "--------------------------------");
                }
            }

        // Performance Wrapper ende
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long totalTimeS = totalTime / 1000;

        // Ausgabe Laufdauer und Geschwindigkeit
        System.out.println(totalTimeS + " Sekunden für " + plaeneZuErstellen + " Durchläufe" + "\n" +
                (double) plaeneZuErstellen / totalTimeS + " pro Sekunde");


        Vorstellung[][][][] spielPlanObj;
        spielPlanObj = planer.getSpielplan();

        // TODO: Broken @ fabian / nicole
//        System.out.println("Spielplan: " + spielPlanObj);
//        final Stream<Vorstellung[][][]> stream = Arrays.stream(spielPlanObj);
//        System.out.println(stream.toArray());

//        //region Ausgabe
//
//        //[WOCHE][TAG][SAAL][SPIELZEIT]
//        System.out.println(spielPlanObj.length); //Wochen 0-2
//        System.out.println(spielPlanObj[0].length); //Tage 0-6
//        System.out.println(spielPlanObj[0][0].length); //Kinosaal
//        System.out.println(spielPlanObj[0][0][0].length); //Spielzeit
//        //System.out.println("Get: "+spielPlanObj[0][0][0][0].GetSaal().getSaalNummer()); //Spielzeit
//
//        //Raumplan
//        System.out.println("Raumplan");
//        System.out.println("Saal: "+spielPlanObj[0][0][0][0].GetSaal().GetSaalNummer());
//        System.out.println("Tag 1");
//        System.out.println("Spielzeit: "+spielPlanObj[0][0][0][0].GetSpielzeiten());
//        System.out.println("Film: "+spielPlanObj[0][0][0][0].GetKinofilm().GetTitel());
//
//        System.out.println("--- --- --- ---");
//
//        System.out.println("Raumplan");
//        System.out.println("Saal: "+spielPlanObj[0][0][0][0].GetSaal().GetSaalNummer());
//        System.out.println("Tag 1");
//        System.out.println("Spielzeit: "+spielPlanObj[0][0][0][1].GetSpielzeiten());
//        System.out.println("Film: "+spielPlanObj[0][0][0][1].GetKinofilm().GetTitel());
//
//        System.out.println("--- --- --- ---");
//
//        System.out.println("Raumplan");
//        System.out.println("Saal: "+spielPlanObj[0][0][0][0].GetSaal().GetSaalNummer());
//        System.out.println("Tag 1");
//        System.out.println("Spielzeit: "+spielPlanObj[0][0][0][2].GetSpielzeiten());
//        System.out.println("Film: "+spielPlanObj[0][0][0][2].GetKinofilm().GetTitel());
//
//        System.out.println("--- --- --- ---");
//
//        System.out.println("Raumplan");
//        System.out.println("Saal: "+spielPlanObj[0][0][0][0].GetSaal().GetSaalNummer());
//        System.out.println("Tag 1");
//        System.out.println("Spielzeit: "+spielPlanObj[0][0][0][3].GetSpielzeiten());
//        System.out.println("Film: "+spielPlanObj[0][0][0][3].GetKinofilm().GetTitel());
//
//        System.out.println("--- --- --- ---");
//
//        System.out.println("Raumplan");
//        System.out.println("Saal: "+spielPlanObj[0][1][0][0].GetSaal().GetSaalNummer());
//        System.out.println("Tag 2");
//        System.out.println("Spielzeit: "+spielPlanObj[0][1][0][0].GetSpielzeiten());
//        System.out.println("Film: "+spielPlanObj[0][1][0][0].GetKinofilm().GetTitel());
//
//        //endregion
    }
}