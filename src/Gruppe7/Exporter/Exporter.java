package Gruppe7.Exporter;

import Gruppe7.Data.Vorstellung;
import Gruppe7.Importer.Datei;

public class Exporter extends Datei
{

    Datei export;
    String exportString;


    public Exporter(String in_name, String in_exportString) {
        super(in_name);
        Datei export = new Datei(in_name);
        exportString = in_exportString;



        //export.openOutFile_FS();

        //String readLine = export.readLine_FS();

        //System.out.println(readLine);

        /*export.writeLine_FS(exportString);
        export.closeOutFile_FS();
        export.eof();*/

        //test1();
        export.openOutFile_FS();

        for (int i = 0; i < 10; i++) {
            export.writeLine_FS(exportString);
            export.writeLine_FS("\r");


        }

        export.closeOutFile_FS();
        export.eof();


    }


    public void ExportKinoProgramm()
    {
        //Code
    }

    public void ExportFinanzplan()
    {
        //Code
    }

    public void ExportSaalplan()
    {
        //Code
    }
}
