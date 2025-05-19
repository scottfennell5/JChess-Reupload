//Created: by Jake Sutton
//Finished: in Spring of 2020
//Description: Class for easy image loading with specific dimension settings
package com.chess.UI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CustomImage extends ImageIcon {
    private BufferedImage bufferedImage;
    private BufferedImage resizedImage;
    private final InputStream is;
    private final int tooWidth;
    private final int tooHeight;

    public CustomImage(String imagePath, int tooWidth, int tooHeight) //Constructor for a CustomImage
    {
        this.tooWidth = tooWidth;
        this.tooHeight = tooHeight;
        System.out.printf("DEBUG: Attempting to load image %s%n", imagePath);
        is = getClass().getResourceAsStream(imagePath);
        if (is == null) {
            System.out.printf("ERROR: Image %s couldn't be found!", imagePath);
        }
        loadImage();
        scaleImage();
        setImage(resizedImage);
    }

    private void loadImage() //Loads image at imagePath
    {
        try {
            bufferedImage = ImageIO.read(is);
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void scaleImage() //Scales image to specified dimensions using the Graphics2D class
    {
        resizedImage = new BufferedImage(tooWidth, tooHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        //Suggestions or "hints" to java 2D on how to render the graphical options
        g2.drawImage(bufferedImage, 0, 0, tooWidth, tooHeight, null);
        g2.dispose();
    }

    public Color getColorAtPoint(int x, int y) throws ArrayIndexOutOfBoundsException { //Gets the Color of a specific
        // pixel, if pixel is out of rang it throws a ArrayIndexOutOfBoundsException
        if (resizedImage != null && x < tooWidth && 0 < x && y < tooHeight)
            return new Color(resizedImage.getRGB(x, y), true);
        else if (x < tooWidth && 0 < x && y < tooHeight)
            return new Color(bufferedImage.getRGB(x, y), true);
        throw new ArrayIndexOutOfBoundsException();
    }
}
