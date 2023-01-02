import java.io.File;
import java.io.IOException;

public class Driver{
    public static void main(String[] args) {
        String inputImagePath = args[0];
        String sourceImagesDirectoryPath = args[1];
        String processedSourceImagesDirectoryPath = args[2];
        int downscaleSizeOfSourceImages = 32;
        int downScaleSizeOfInputImage = 512;
        int tileSize = 8;

        if (args.length == 4){
            downscaleSizeOfSourceImages = Integer.parseInt(args[3]);
        } else if (args.length == 6) {
            downscaleSizeOfSourceImages = Integer.parseInt(args[3]);
            downScaleSizeOfInputImage = Integer.parseInt(args[4]);
            tileSize = Integer.parseInt(args[5]);
            if (downScaleSizeOfInputImage % tileSize != 0) {
                System.err.println("tileSize should be a proper divisor of downScaleSizeOfInputImage");
                System.exit(1);
            }
        } else {
            System.err.println("Invalid Arguments...");
            System.err.println("Usage: java Driver <inputImagePath> <sourceImagePath> <processedImagePath>");
            System.err.println("OR");
            System.err.println("Usage: java Driver <inputImagePath> <sourceImagePath> <processedImagePath>" +
                    "<downscaleSizeOfSourceImages (Default value is 32)>");
            System.err.println("OR");
            System.err.println("Usage: java Driver <inputImagePath> <sourceImagePath> <processedImagePath>" +
                    "<downscaleSizeOfSourceImages (Default value is 32)>"+" <downscaleSizeOfInputImage (Default value is 512)>" +
                    "<tileSize (Should be a proper divisor of downscaleSizeOfInputImage)(Default value is 8)>");
            System.exit(1);
        }
        String dbFilePath = processedSourceImagesDirectoryPath + "/Processed.csv";
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            new DBProcessor(sourceImagesDirectoryPath,processedSourceImagesDirectoryPath,downscaleSizeOfSourceImages);
        }
        try {
            new Mosaic(inputImagePath,downScaleSizeOfInputImage,dbFilePath,tileSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}