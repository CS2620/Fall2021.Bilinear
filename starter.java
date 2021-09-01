
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class starter {

    public static void main(String[] args) {
        try {
            BufferedImage bufferedImage = ImageIO.read(new File("corn.jpg"));

            // var cropped = Crop(bufferedImage, 10, 10, 1000, 1000);
            // var translated = TranslateNearestNeighbor(bufferedImage, 50.5, 50.5, false);
            var translated = TranslateLinear(bufferedImage, 50.5, 50.5, false);

            ImageIO.write(translated, "PNG", new File("out.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static BufferedImage TranslateNearestNeighbor(BufferedImage bufferedImage, double i, double j, boolean b) {
        BufferedImage toReturn = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (var y = 0; y < toReturn.getHeight(); y++) {
            for (var x = 0; x < toReturn.getWidth(); x++) {
                int originalX = (int) (x - i + .5);
                int originalY = (int) (y - j + .5);

                if (originalX < 0 || originalX >= bufferedImage.getWidth() || originalY < 0
                        || originalY >= bufferedImage.getHeight())
                    continue;

                var pixelInt = bufferedImage.getRGB(originalX, originalY);
                toReturn.setRGB(x, y, pixelInt);
            }
        }

        return toReturn;
    }

    private static BufferedImage TranslateLinear(BufferedImage bufferedImage, double i, double j, boolean b) {
        BufferedImage toReturn = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        for (var y = 0; y < toReturn.getHeight(); y++) {
            for (var x = 0; x < toReturn.getWidth()-1; x++) {
                int originalX = (int) (x - i + .5);
                int leftPixel = (originalX);
                int rightPixel = (originalX + 1);
                int originalY = (int) (y - j + .5);

                if (originalX < 0 || originalX >= bufferedImage.getWidth()-1 || originalY < 0
                        || originalY >= bufferedImage.getHeight()-1)
                    continue;

                Color pixelLeft = new Color(bufferedImage.getRGB(leftPixel, originalY));
                Color pixelRight = new Color(bufferedImage.getRGB(rightPixel, originalY));

                double percent = i - (int) i;
                Color leftPixelContibution = new Color((int) (pixelLeft.getRed() * (1 - percent)),
                        (int) (pixelLeft.getGreen() * (1 - percent)), (int) (pixelLeft.getBlue() * (1 - percent)));
                Color rightPixelContibution = new Color((int) (pixelRight.getRed() * (percent)),
                (int) (pixelRight.getGreen() * ( percent)), (int) (pixelRight.getBlue() * (percent)));

                int finalRed = leftPixelContibution.getRed() + rightPixelContibution.getRed();
                int finalGreen = leftPixelContibution.getGreen() + rightPixelContibution.getGreen();
                int finalBlue = leftPixelContibution.getBlue() + rightPixelContibution.getBlue();
                Color finalColor = new Color(finalRed, finalGreen, finalBlue);

                

                toReturn.setRGB(x, y,   finalColor.getRGB());
            }
        }

        return toReturn;
    }

    private static BufferedImage Crop(BufferedImage bufferedImage, int ulx, int uly, int lrx, int lry) {
        var width = lrx - ulx;
        var height = lry - uly;
        var toReturn = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (var h = 0; h < height; h++) {
            for (var w = 0; w < width; w++) {
                var pixelInt = bufferedImage.getRGB(w + ulx, h + uly);
                var pixelColor = new Color(pixelInt);
                toReturn.setRGB(w, h, pixelColor.getRGB());
            }
        }

        return toReturn;

    }
}
