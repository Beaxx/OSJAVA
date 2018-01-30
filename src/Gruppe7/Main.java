package Gruppe7;

import java.io.*;

import Gruppe7.Data.*;
import Gruppe7.Exporter.ExportFinanzplan;
import Gruppe7.Exporter.ExportKinoprogramm;
import Gruppe7.Exporter.ExportRaumplanung;
import Gruppe7.Logic.*;
import Gruppe7.Importer.*;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;


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

        //region User Interaktion
        System.out.println("Bitte geben Sie den gesamten Verzeichnis-Pfad ihres Datensatzes an.\n" +
                "Die Importdateien müssen wiefolgt benannt sein:\n\n" +
                "Filme:         filme.csv\n" +
                "Säle:          saele.csv\n" +
                "Werbespots:    werbespots.csv\n\n" +
                "Das Standardverzeichnis ist: C:/import/Datensatz/...\n" +
                "Wenn Ihr Datensatz dort liegt drücken Sie bitte 'ENTER'\n" +
                "Ansonsten geben Sie bitte den Pfad an. (Ohne '/' am Ende)");

        // Input Stream für Userinput öffnen
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //Inputvalidierung Input-Pfad
        String path = "C:/import/Datensatz";
        boolean validInput = false;
        do {
            try {
                String input = reader.readLine();
                if (isValidPath(input) && !input.isEmpty()) {
                    path = input;
                    validInput = true;
                } else if (isValidPath(input) && input.isEmpty()) {
                    validInput = true;
                } else {
                    throw new IOException();
                }
            } catch (IOException ex) {
                System.out.print("Dieser Pfad existiert nicht, haben Sie sich verschrieben?");
            }
        } while (!validInput);

        System.out.println("Wie viele Optimierungsdurchläufe möchten Sie machen? \nMit steigender Anzahl der Optimierungen" +
                " steigt die Qualität der generierten Spielpläne.\nWählen Sie eine Zahl zwischen 10.000 und 10.000.000\n\n" +
                "Laufzeiten (ca.)\n" +
                "10.000 Durchläufe:     2   Sekunden\n" +
                "100.000 Durchläufe:    20  Sekunden\n" +
                "1.000.000 Durchläufe:  2   Minuten\n" +
                "10.000.000 Durchläufe: 37  Minuten\n\n" +
                "Die Ausgabe erfolgt im Ordner 'export' in Ihrem Datensatz-Verzeichnis.");

        // Erstellung des Exportordners falls noch nicht vorhanden.
        new File(path + "/export").mkdirs();

        // Inputvalidierung Durchläufe
        validInput = false;
        int checkedInput = 0;
        do {
            try {
                String input = reader.readLine();
                checkedInput = Integer.parseInt(input);

                if (checkedInput < 10000 || checkedInput > 10000000) {
                    throw new Exception();
                }

                validInput = true;
            } catch (Exception e) {
                System.out.print("Bitte geben Sie eine Zahl zwischen 10.000 und 10.000.000 ein.");
            }
        } while (!validInput);

        // Wartezeit ausgeben
        int dauer = Math.round(checkedInput / 4000 / 60);
        System.out.println(checkedInput + " Durchläufe werden durchgeführt. Bitte warten Sie ca. " + dauer + "Minuten");
        reader.close();
        //endregion

        //region Start-Up
        // Datenimport
        new WerbefilmImporter(path + "/werbespots.csv");
        new SaalImporter(path + "/saele.csv");
        int mindestBeliebtheit = 93;
        new KinofilmImporter(path + "/filme.csv", mindestBeliebtheit);

        // FilmArrays erstellen
        FilmVerwaltung.FilmArraysHelper();

        // Werbeplan sortieren und Standard 20 Minunten Block festlegen
        WerbefilmVerwaltung.WerbeplanSortieren();
        WerbefilmVerwaltung.StandardWerbeblock();
        WerbefilmVerwaltung.StandardWerbeblockUmsatzProZuschauer();

        // Saele sortieren
        SaalVerwaltung.SaalplanSortieren();
        SaalVerwaltung.PlaetzteInGroestemUndZweitgroestemSaal();
        //endregion

        //region Algorithmus
        // Performance Wrapper start
        long startTime = System.currentTimeMillis();

        // Algorithmus
        Planer planer = new Planer();
        for (int i = 0; i < checkedInput; i++)

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
        long totalTimeS = totalTime / 1000;
        //endregion

        // Ausgabe Laufdauer und Geschwindigkeit
        System.out.println(totalTimeS + " Sekunden für " + checkedInput + " Durchläufe" + "\n" +
                (double) checkedInput / totalTimeS + " pro Sekunde");

        //region Export
        new ExportRaumplanung(path + "/export/raumplan.txt", planer);

        new ExportKinoprogramm(path + "/export/kinoprogramm.csv", planer);

        new ExportFinanzplan(path + "/export/finanzplan.csv", planer);
        //endregion
    }

    private static boolean isValidPath(String input) throws IOException {

        try {
            if (!Files.exists(Paths.get(input))) {
                throw new IOException();
            }

        } catch (IOException | InvalidPathException ex) {
            return false;
        }
        return true;
    }
}
