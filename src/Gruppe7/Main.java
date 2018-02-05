package Gruppe7;

import Gruppe7.Data.FilmVerwaltung;
import Gruppe7.Data.SaalVerwaltung;
import Gruppe7.Data.WerbefilmVerwaltung;
import Gruppe7.Exporter.ExportFinanzplan;
import Gruppe7.Exporter.ExportKinoprogramm;
import Gruppe7.Exporter.ExportRaumplanung;
import Gruppe7.Importer.KinofilmImporter;
import Gruppe7.Importer.SaalImporter;
import Gruppe7.Importer.WerbefilmImporter;
import Gruppe7.Logic.Planer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;


/**
 * Zentraler Programeinstieg
 *
 * 1. Benutzerinteraktion, Pfad festlegen, Anzahl der Durchläufe angeben
 * 2. Import der Dateien
 * 3. Durchführung der Optimierung
 * 4. Ausgabe
 *
 * @author Lennart Völler, Nicole Diestler, Fabian Ueberle
 */

public class Main {

    public static void main(String[] args) throws IOException {

        //region User Interaktion Pfadangabe
        System.out.println("Bitte geben Sie den gesamten Verzeichnis-Pfad ihres Datensatzes an.\n" +
                "Die Importdateien müssen wiefolgt benannt sein:\n\n" +
                "Filme:         filme.csv\n" +
                "Säle:          saele.csv\n" +
                "Werbespots:    werbespots.csv\n\n" +
                "Das Standardverzeichnis ist: C:/import/...\n" +
                "Wenn Ihr Datensatz dort liegt drücken Sie bitte 'ENTER'\n" +
                "Ansonsten geben Sie bitte den Pfad an. (Ohne '/' am Ende)\n\n");

        // Input Stream für Userinput öffnen
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Inputvalidierung Input-Pfad

        String path = "C:" + File.separator + "import";
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
                System.out.print("Dieser Pfad existiert nicht, haben Sie sich verschrieben?\n\n");
            }
        } while (!validInput);
        //endregion

        //region User Interaktion Mindestbeliebtheit
        System.out.println("Bitte geben Sie die Mindestbeliebtheit für die zu Importierenden Filme an.\n" +
                "Dieser Wert muss zwischen 0 und 100 liegen.\n\n" +
                "Sie sollten den Wert möglichst hoch wählen.\n" +
                "Wird der Wert jedoch zu hoch gewählt, kann der Spielplan\n" +
                "bestimmte Restriktionen nicht mehr erfüllen und das Ausgabefensterbleibt leer. \n" +
                "Verwenden Sie in diesem Fall bitte einen niedrigeren Schwellenwert.\n" +
                "Empfohlen wird der Wert 96.\n\n");

        validInput = false;
        int checkedInput = 96;
        do {
            try {
                String input = reader.readLine();
                checkedInput = Integer.parseInt(input);

                if (checkedInput < 0 || checkedInput > 100) {
                    throw new Exception();
                }

                validInput = true;
            } catch (Exception e) {
                System.out.print("Bitte geben Sie eine Zahl zwischen 0 und 100 ein.");
            }
        } while (!validInput);
        int mindestBeliebtheit = checkedInput;
        //endregion

        //region User Interaktion Durchlauf Anzahl
        System.out.println("Wie viele Optimierungsdurchläufe möchten Sie machen? \nMit steigender Anzahl der Optimierungen" +
                " steigt die Qualität der generierten Spielpläne.\nWählen Sie eine Zahl zwischen 10.000 und 1.000.000\n\n" +
                "Laufzeiten (ca.)\n" +
                "10.000 Durchläufe:     4   Sekunden\n" +
                "100.000 Durchläufe:    48  Sekunden\n" +
                "250.000 Durchläufe:    2   Minuten\n" +
                "1.000.000 Durchläufe:  8   Minuten\n" +
                "Die Ausgabe erfolgt im Ordner 'export' in Ihrem Datensatz-Verzeichnis.");

        // Erstellung des Exportordners falls noch nicht vorhanden.
        new File(path + File.separator + "export").mkdirs();

        // Inputvalidierung Durchläufe
        validInput = false;
        checkedInput = 0;
        do {
            try {
                String input = reader.readLine();
                checkedInput = Integer.parseInt(input);

                if (checkedInput < 10000 || checkedInput > 1000000) {
                    throw new Exception();
                }

                validInput = true;
            } catch (Exception e) {
                System.out.print("Bitte geben Sie eine Zahl zwischen 10.000 und 1.000.000 ein.");
            }
        } while (!validInput);

        // Wartezeit ausgeben
        int dauer = Math.round(checkedInput / 2100 / 60);
        System.out.println(checkedInput + " Durchläufe werden durchgeführt. Bitte warten Sie ca. " + dauer + " Minuten");
        reader.close();
        //endregion

        //region Import
        // Datenimport
        new WerbefilmImporter(path + File.separator + "werbespots.csv");
        new SaalImporter(path + File.separator + "saele.csv");
        new KinofilmImporter(path + File.separator + "filme.csv", mindestBeliebtheit);

        // FilmArrays erstellen
        FilmVerwaltung.FilmArraysHelper();

        // Werbeplan sortieren und Standard 20 Minunten Block festlegen
        WerbefilmVerwaltung.WerbeplanSortieren();
        WerbefilmVerwaltung.StandardWerbeblock();
        WerbefilmVerwaltung.StandardWerbeblockUmsatzProZuschauer();

        // Saele sortieren
        SaalVerwaltung.SaalplanSortieren();
        SaalVerwaltung.PlaetzteInGroestemUndZweitgroesstemSaal();
        //endregion

        //region Algorithmus
        // Algorithmus
        Planer planer = new Planer();
        for (int i = 0; i < checkedInput; i++) {
            Planer tempPlaner = new Planer();

            if (tempPlaner.GetSpielplanGewinn() > planer.GetSpielplanGewinn()) {
                planer = tempPlaner;
                System.out.println(
                        "Ticketeinnahmen:   " + planer.GetSpielplanTicketeinnahmen() + "\n" +
                                "Werbeeinnahmen:    " + planer.GetSpielplanWerbeEinnahmen() + "\n" +
                                "Ausgaben:          " + planer.GetSpielplanAusgaben() + "\n" +
                                "Gewinn:            " + planer.GetSpielplanGewinn() + "\n" +
                                "---------------------------\n\n");
            }
        }

        //endregion
        System.out.println("Optimierung beendet. Die Ergebnisse befinden sich im Exportverzeichnis\n" +
                "Sie können das Fenster nun schließen...");

        //region Export
        new ExportRaumplanung(path + File.separator + "export" + File.separator + "raumplan.txt", planer);

        new ExportKinoprogramm(path + File.separator + "export" + File.separator + "kinoprogramm.csv", planer);

        new ExportFinanzplan(path + File.separator + "export" + File.separator + "finanzplan.csv", planer);
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
