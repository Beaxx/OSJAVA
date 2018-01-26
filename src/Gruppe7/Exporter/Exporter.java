package Gruppe7.Exporter;

import Gruppe7.Importer.Datei;

public class Exporter extends Datei
{

    String exportString;

    public Exporter(String in_name, String in_exportString)
    {
        super(in_name);

        exportString = in_exportString;
        Datei export = new Datei(in_name);

        export.openOutFile_FS();

        //String readLine = export.readLine_FS();

        //System.out.println(readLine);

        export.writeLine_FS(exportString);
        export.closeOutFile_FS();
        export.eof();

        test1();


    }

    public void test1() {
        System.out.println("Methode Test 1");

        for (int i=0;i<10;i++){


        }
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
