package Gruppe7;

import java.io.IOException;

import Gruppe7.Data.*;
//import Gruppe7.Exporter.ExportFinanzplan;
import Gruppe7.Exporter.ExportKinoprogramm;
import Gruppe7.Exporter.ExportRaumplanung;
import Gruppe7.Logic.*;
import Gruppe7.Importer.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;


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

    public static void main(String[] args) throws IOException {

        // User Interaktion
        System.out.println("Stellen Sie sicher, dass die Datensätze im Ordner dieser Java-Datei liegen.\n" +
                "Wenn Sie die Datein im Entsprechenden Ordner abgelegt haben drücken Sie bitte ENTER");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();

        System.out.println("Wie viele Optimierungsdurchläufe möchten Sie machen? \nMit steigender Anzahl der Optimierungen" +
                " steigt die Qualität der generierten Spielpläne.\nWählen Sie eine Zahl zwischen 1.000 und 10.000.000\n\n" +
                "Laufzeiten (ca.)\n" +
                "1.000 Durchläufe: < 1 Sekunde\n" +
                "10.000 Durchläufe: 2 Sekunden\n" +
                "100.000 Durchläufe: 20 Sekunden\n" +
                "1.000.000 Durchläufe: 2 Minuten\n" +
                "10.000.000 Durchläufe: 37 Minuten\n");

        int input = Integer.valueOf(reader.readLine());

        int dauer = Math.round(input/4000/60);
        System.out.println(input + " Durchläufe werden durchgeführt. Bitte warten Sie ca. " + dauer + "Minuten");
        reader.close();

        /* SETTINGS */
        int plaeneZuErstellen = input;
        int mindestBeliebtheit = 93;
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
        WerbefilmVerwaltung.standardWerbeblockUmsatzProZuschauer();

        // Saele sortieren
        SaalVerwaltung.saalplanSortieren();
        SaalVerwaltung.plaetzteInGroestemUndZweitgroestemSaal();

        // Performance Wrapper start
        long startTime = System.currentTimeMillis();

            // Algorithmus
            Planer planer = new Planer();
            for (int i = 0; i < plaeneZuErstellen; i++)

            {
                Planer tempPlaner = new Planer();

                if (tempPlaner.GetSpielplanGewinn() > planer.GetSpielplanGewinn()) {
                    planer = tempPlaner;
                    System.out.println("Tickets: " + planer.GetSpielplanTicketeinnahmen() + "\n" +
                                        "Werbung: " + planer.GetSpielplanWerbeEinnahmen() + "\n" +
                                        "Ausgaben: " + planer.GetSpielplanAusgaben() + "\n" +
                                        "Gewinn:" + planer.GetSpielplanGewinn() + "\n" +
                                        "--------------------------------");
                }
            }

        // Performance Wrapper ende
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        long totalTimeS = totalTime/1000;

        // Ausgabe Laufdauer und Geschwindigkeit
        System.out.println(totalTimeS + " Sekunden für " + plaeneZuErstellen + " Durchläufe" + "\n" +
                (double) plaeneZuErstellen / totalTimeS + " pro Sekunde");

        // Export
        Vorstellung[][][][] spielPlanObj;
        spielPlanObj = planer.GetSpielplan();

        new ExportRaumplanung("C:/import/export/raumplan.txt", planer);

        new ExportKinoprogramm("C:/import/export/kinoprogramm.csv", planer);

//        new ExportFinanzplan(spielPlanObj, "C:/import/export/finanzplan.csv", planer);
    }
}