import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mosaic{

    private BufferedImage inputImage;
    private HashMap<String,BufferedImage> cache;
    private final int tileSize;


    public Mosaic(String inputImagePath, int width ,  String dbFilePath, int tileSize) throws IOException {
        this.tileSize = tileSize;
        try {
            inputImage = ImageIO.read(new File(inputImagePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(inputImage == null) {
            throw new IllegalArgumentException("Not a valid Input image: "+inputImagePath);
        }
        inputImage = Utility.getResizedImage(inputImage,width);
        String fileBaseName = new File(inputImagePath).getName();
        String scaledImagePath = "Scale_Image_"+ fileBaseName;
        ImageIO.write(inputImage,"jpg",new File(scaledImagePath));

        ArrayList<SourceImage> sourceImages = Utility.loadSourceImages(dbFilePath);
        cache = new HashMap<>();
        assemble(sourceImages);

        String outImageFilePath = "OUTPUT_Image_" + fileBaseName;
        ImageIO.write(inputImage, "jpg", new File(outImageFilePath));
        Utility.displayImage(outImageFilePath);


    }

    private void assemble(ArrayList<SourceImage> sourceImages) {
        for (int i = 0 ; i < inputImage.getHeight(); i += tileSize){
            for (int j = 0 ; j < inputImage.getWidth(); j += tileSize){
                BufferedImage patch = inputImage.getSubimage(j,i,tileSize,tileSize);
                String closestMatch = getClosest(patch,sourceImages);


                BufferedImage closestMatchImage = null;
                if (!cache.containsKey(closestMatch)) {
                    try {
                        closestMatchImage = ImageIO.read(new File(closestMatch));
                        cache.put(closestMatch,closestMatchImage);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                else {
                    closestMatchImage = cache.get(closestMatch);
                }
                fitTile(closestMatchImage,j,i);
            }
        }
    }

    private void fitTile(BufferedImage closestMatchImage,int x, int y) {
        int[] RGBs = closestMatchImage.getRGB(0,0,closestMatchImage.getWidth(),closestMatchImage.getHeight(),
                null,0,closestMatchImage.getWidth());
        inputImage.setRGB(x,y,tileSize,tileSize,RGBs,0,tileSize);
    }

    private String getClosest(BufferedImage patch, ArrayList<SourceImage> sourceImages) {
        double minDistance = Double.POSITIVE_INFINITY;
        SourceImage closestSourceImage = null;
        for (SourceImage si : sourceImages){
            double currentDistance = calculateEuclideanDistance(Utility.getAvgColor(patch),si.getAvgColors());
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestSourceImage = si;
            }
        }
        assert closestSourceImage != null;
        return closestSourceImage.getImagePath();
    }

    private double calculateEuclideanDistance(double[] patchColor, double[] tileColor) {
        double distance = 0 ;
        for (int i = 0 ; i < patchColor.length; i++){
            distance += Math.pow(patchColor[i]-tileColor[i],2);
        }
        return Math.sqrt(distance);
    }


}