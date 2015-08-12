package project;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

/**
 * Created by troyr on 8/2/2015.
 */
public class ImageMining {

    private static int width;
    private static int height;
    private static BufferedImage input;
    private static BufferedImage image;
    private static WritableRaster r;
    private static WritableRaster w;

    private ImageMining() {}

    public static BufferedImage start(String imageFile, int m, double thresholdValue) {
        input= null;
        try {
            input = ImageIO.read(new File("assets/" + imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        width = input.getWidth();
        height = input.getHeight();

        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        r = input.getRaster();
        w = image.getRaster();

        int pixelGrid[] = new int[m*m];
        int magnitude[] = new int[1];
        int offset = (m + 1) /2;

        for (int x = m; x < width-m; x++) {
            for (int y = m; y < height-m; y++) {
                magnitude[0] = 0;

                // Get the 3x3 pixels around the current pixel and put them into this array.
                r.getPixels(x-1, y-1, m, m, pixelGrid);

                for (int a = 0; a < m; a++) {
                    for (int b = 0; b < m; b++) {
                        int index = a * m + b;
                        magnitude[0] += pixelGrid[index] * (((double) 1) / (m * m));
                    }
                }

                // Thresholding
                if (magnitude[0] > thresholdValue)
                    magnitude[0] = 255;
                else
                    magnitude[0] = 0;

                // Set the pixel value in the new image array.
                w.setPixel(x, y, magnitude);
            }
        }

        return image;
    }

}
