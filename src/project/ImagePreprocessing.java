package project;


import ij.ImagePlus;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static ij.IJ.openImage;

/**
 * Created by troyr on 8/1/2015.
 */
public final class ImagePreprocessing {

    // Edge detection mask - Horizontal
    private static float sobelx[][] = {
            {-1, 0, 1},
            {-2, 0, 2},
            {-1, 0, 1}
    };

    // Edge detection mask - Vertical
    private static float sobely[][] = {
            {-1, -2, -1},
            {0,  0,  0},
            {1,  2,  1}
    };

    // Edge detection mask - General
    private static float laplacian[][] = {
            {0, -1, 0},
            {-1, 4, -1},
            {0, -1, 0}
    };

    // Sharpening mask
    private static float enhance[][] = {
            {0, -1, 0},
            {-1, 5, -1},
            {0, -1, 0}
    };

    private static int width;
    private static int height;
    private static BufferedImage input;
    private static BufferedImage image;
    private static WritableRaster r;
    private static WritableRaster w;

    private ImagePreprocessing() {}

    private static void generalFilter(String imageFile) {
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

        int[] pixels = new int[width*height];
        r.getPixels(0,0,width,height,pixels);
        w.setPixels(0,0,width,height,pixels);
    }

    private static int convolution(int[] pixelGrid, float[][] filter) {
        int filterSize = filter[0].length;

        int magnitude = 0;
        for (int a = 0; a < filterSize; a++) {
            for (int b = 0; b < filterSize; b++) {
                int index = a * 3 + b;
                magnitude += pixelGrid[index] * filter[a][b];
            }
        }
        return magnitude;
    }

    public static BufferedImage sobelEdges(String imageFile) {

        generalFilter(imageFile);

        int magX = 0;
        int magY = 0;
        int pixelGrid[] = new int[9];
        int magnitude[] = new int[1];

        for(int x=1; x<width-2; x++){
            for(int y=1; y<height-2; y++){
                // Get the 3x3 pixels around the current pixel and put them into this array.
                r.getPixels(x-1, y-1, 3, 3, pixelGrid);

                // Sobel X filter
                magX = convolution(pixelGrid, sobelx);
                // Sobel Y filter
                magY = convolution(pixelGrid, sobely);

                // Magnitude Equation
                magnitude[0] = (int) Math.abs(Math.sqrt((magX * magX) + (magY*magY)));

                // Bound the integer to one byte.
                if (magnitude[0] > 255)
                    magnitude[0] = 255;
                else if (magnitude[0] < 0)
                    magnitude[0] = 0;

                // Set the pixel value in the new image array.
                w.setPixel(x, y, magnitude);
            }
        }

        return image;
    }

    public static BufferedImage laplacianFilter(String imageFile) {

        generalFilter(imageFile);

        int pixelGrid[] = new int[9];
        int magnitude[] = new int[1];

        for (int x = 1; x < width-2; x++) {
            for (int y = 1; y < height-2; y++) {

                // Get the 3x3 pixels around the current pixel and put them into this array.
                r.getPixels(x-1, y-1, 3, 3, pixelGrid);

                magnitude[0] = convolution(pixelGrid, laplacian);

                // Bound the integer to one byte.
                if (magnitude[0] > 255)
                    magnitude[0] = 255;
                else if (magnitude[0] < 0)
                    magnitude[0] = 0;

                // Set the pixel value in the new image array.
                w.setPixel(x, y, magnitude);
            }
        }

        return image;
    }

    // – Mean Filters
    // – Median Filters
    // – Enhancement Filters
    public static BufferedImage noiseCancellation(String imageFile, int filter, int maskSize) {

        generalFilter(imageFile);

        switch (filter) {
            case 1:
                image = meanFilter(maskSize);
                break;
            case 2:
                image = medianFilter(maskSize);
                break;
            default:
                break;
        }

        return image;
    }

    private static BufferedImage meanFilter(int m) {

        int pixelGrid[] = new int[m*m];
        int magnitude[] = new int[1];
        int offset = (m + 1) /2;

        for (int x = offset; x < width-offset; x++) {
            for (int y = offset; y < height-offset; y++) {
                magnitude[0] = 0;

                // Get the 3x3 pixels around the current pixel and put them into this array.
                r.getPixels(x-1, y-1, m, m, pixelGrid);

                for (int a = 0; a < m; a++) {
                    for (int b = 0; b < m; b++) {
                        int index = a * m + b;
                        magnitude[0] += pixelGrid[index] * (((double) 1) / (m * m));
                    }
                }

                // Bound the integer to one byte.
                if (magnitude[0] > 255)
                    magnitude[0] = 255;
                else if (magnitude[0] < 0)
                    magnitude[0] = 0;

                // Set the pixel value in the new image array.
                w.setPixel(x, y, magnitude);
            }
        }

        return image;
    }

    private static BufferedImage medianFilter(int m) {

        ArrayList<Integer> magnitudeList = new ArrayList<Integer>();
        int pixelGrid[] = new int[m*m];
        int magnitude[] = new int[1];
        int offset = (m + 1) /2;

        for (int x = offset; x < width-offset; x++) {
            for (int y = offset; y < height-offset; y++) {
                magnitudeList.clear();

                // Get the 3x3 pixels around the current pixel and put them into this array.
                r.getPixels(x - 1, y - 1, m, m, pixelGrid);

                for (int a = 0; a < m; a++) {
                    for (int b = 0; b < m; b++) {
                        int index = a * m + b;
                        magnitudeList.add(pixelGrid[index]);
                    }
                }

                Collections.sort(magnitudeList);
                if (magnitudeList.size() % 2 == 0)
                    magnitude[0] = (magnitudeList .get(magnitudeList.size() / 2) + magnitudeList.get(magnitudeList.size() / 2 - 1)) / 2;
                else
                    magnitude[0] = magnitudeList.get(magnitudeList.size() / 2);

                // Bound the integer to one byte.
                if (magnitude[0] > 255)
                    magnitude[0] = 255;
                else if (magnitude[0] < 0)
                    magnitude[0] = 0;

                // Set the pixel value in the new image array.
                w.setPixel(x, y, magnitude);
            }
        }

        return image;
    }

    public static BufferedImage imageEnhancement(String imageFile) {

        generalFilter(imageFile);
        int pixelGrid[] = new int[9];
        int magnitude[] = new int[1];

        for (int x = 1; x < width-1; x++) {
            for (int y = 1; y < height-1; y++) {

                r.getPixels(x - 1, y - 1, 3, 3, pixelGrid);

                magnitude[0] = convolution(pixelGrid, enhance);

                // Bound the integer to one byte.
                if (magnitude[0] > 255)
                    magnitude[0] = 255;
                else if (magnitude[0] < 0)
                    magnitude[0] = 0;

                // Set the pixel value in the new image array.
                w.setPixel(x, y, magnitude);
            }
        }

        return image;
    }
}
