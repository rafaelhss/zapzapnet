package zap.bussiness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzipper {



    public String unzipFilesInFolder(String zippedFiles) {
        String UNZIPPEDFILES = zippedFiles + "\\unzip";

        for (File arq : new File(zippedFiles).listFiles()) {
            System.out.println("### unzipping: " + arq.getAbsoluteFile().toString());
            unZipIt(arq.getAbsoluteFile().toString(), UNZIPPEDFILES);
        }
        return UNZIPPEDFILES;
    }

    /**
     * Unzip it
     * @param zipFile input zip file
     * @param outputFolder zip file output folder
     */
    public String unZipIt(String zipFile, String outputFolder){

        System.out.println("outputFolder:" + outputFolder);
        String lastFileName = "error";

        byte[] buffer = new byte[1024];

        try{

            //create output directory is not exists
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);

                System.out.println("file unzip : "+ newFile.getAbsoluteFile());

                lastFileName = newFile.getAbsoluteFile().toString();

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

            return lastFileName;
        }catch(IOException ex){
            ex.printStackTrace();
            return ex.getMessage();
        }


    }
}