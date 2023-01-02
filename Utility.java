import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utility{

    public static double[] getAvgColor(BufferedImage dbBufferedImage) {
        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        int numPixels = 0;
        for (int x = 0; x < dbBufferedImage.getWidth(); x++) {
            for (int y = 0; y < dbBufferedImage.getHeight(); y++) {
                int pixel = dbBufferedImage.getRGB(x, y);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                sumR += r;
                sumG += g;
                sumB += b;
                numPixels++;
            }
        }
        int avgR = sumR / numPixels;
        int avgG = sumG / numPixels;
        int avgB = sumB / numPixels;
        return new double[]{avgR,avgG,avgB};
    }
    public static BufferedImage getResizedImage(BufferedImage image, int width) {
        Image resized = image.getScaledInstance(width,width,Image.SCALE_SMOOTH);
        BufferedImage bufferedImage = new BufferedImage(width,width,BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(resized,0,0,null);
        return bufferedImage;
    }

    public static ArrayList<SourceImage> loadSourceImages(String dbFilePath) {
        File file = new File(dbFilePath);
        ArrayList<SourceImage> images = new ArrayList<>();
        if (!file.exists()){
            throw  new IllegalArgumentException("Couldn't find db file "+ dbFilePath);
        }
        Scanner scan = null;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String[] data = scan.nextLine().split(",");
                double[] avgColor = new double[]{
                        Double.parseDouble(data[1]),
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3])
                };
                images.add(new SourceImage(data[0],avgColor));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return images;
    }
}