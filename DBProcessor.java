import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;

public class DBProcessor {
    private final int width;
    private String inDirPath;
    private String outDirPath;
    private final File inDir;

    private PrintWriter csvWriter;
    public DBProcessor(String inDirPath, String outDirPath, int width) {
        this.width = width;
        this.inDirPath = inDirPath;
        this.outDirPath = outDirPath;

        inDir = new File(inDirPath);
        if (!inDir.exists()) {
            throw new IllegalArgumentException(inDirPath + " doesn't exist.");
        }

        new File(outDirPath).mkdir();
        createDBFile();
        File[] inputImages = listInputImages(inDir);
        processImages(inputImages);
        csvWriter.close();
    }

    private void processImages(File[] inputImages) {
        for (File f : inputImages){
            String inputFileBaseName = f.getName().substring(0,f.getName().lastIndexOf("."));
            String outFileName = outDirPath + "/" + inputFileBaseName + ".jpg";
            if (new File(outFileName).exists()){
                System.err.println( outFileName + " DB file already exists");
                continue;
            }
            BufferedImage image = null;
            try{
                image = ImageIO.read(f);

            } catch (Exception e) {
                System.err.println("Unable to read " + f.getName());
                e.printStackTrace();
                continue;
            }
            if (image == null) {
                System.err.println("Skipping non-image file "+f.getName());
                continue;
            }
            BufferedImage dbBufferedImage = Utility.getResizedImage(image,width);
            try {
                writeDBImageToFile(dbBufferedImage,outFileName);
            } catch (Exception e) {
                System.err.println("Error Writing file "+outFileName);
                e.printStackTrace();
                continue;
            }

            double[] avgColor = Utility.getAvgColor(dbBufferedImage);
            writeToDBFile(outFileName,avgColor);
        }
    }

    private void writeToDBFile(String outFileName, double[] avgColor) {
        StringBuilder sb = new StringBuilder();
        sb.append(outFileName).append(",");
        for (int i = 0 ; i < avgColor.length ; i++){
            sb.append(avgColor[i]);
            if(i != avgColor.length-1){
                sb.append(",");
            }
        }
        csvWriter.println(sb.toString());
        csvWriter.flush();
    }

    private void writeDBImageToFile(BufferedImage dbBufferedImage, String outFileName) {

        try {
            ImageIO.write(dbBufferedImage,"jpg",new File(outFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("[INFO] successfully created "+ outFileName);

    }



    private File[] listInputImages(File inDir) {
        File[] inputImages;
        if(!inDir.isDirectory()){
            inputImages = new File[]{inDir};
        } else {
            inputImages = inDir.listFiles();
        }
        return inputImages;
    }

    private void createDBFile() {
        String fileName = outDirPath + "/Processed" + ".csv";
        try {
            if(inDir.exists() && !inDir.isDirectory()){
               csvWriter = new PrintWriter(new FileOutputStream(new File(fileName),true));
            }
            else {
                csvWriter = new PrintWriter(new File(fileName));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}