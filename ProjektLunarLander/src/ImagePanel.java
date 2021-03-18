import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Klasa dziedziczaca po JPanel, dodawanie tla do okien z pliku
 */


class ImagePanel extends JPanel {

    /**
     * Kontener obrazu
     */
    private Image originalImage;

    /**
     * przeskalowany obraz
     */
    private Image scaledImage;

    /**
     * Zmienna informująca czy obrazek został załadowany
     */
    private boolean loaded = false;

    /**
     * Konstruktor
     * @param imgFileName nazwa pliku z obrazem
     */
    public ImagePanel(String imgFileName) {
        loadImage(imgFileName);
    }

    /**
     * Malowanie panelu
     *
     * @param g konstekst graficzny
     */
    public void paintComponent(Graphics g) {
        if (originalImage != null && loaded){
            scaledImage = originalImage.getScaledInstance(this.getWidth(), -1, Image.SCALE_AREA_AVERAGING);
            g.drawImage(scaledImage, 0, 0, null);

        } else
            g.drawString("No picture", 10, getHeight() - 10);
    }

    /**
     * Metoda ładująca obraz z pliku
     *
     * @param imgFileName nazwa pliku z obrazem
     */

    private void loadImage(String imgFileName) {
        try {
            originalImage = ImageIO.read(new File(imgFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(originalImage, 1);
        try {
            mt.waitForID(1);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        int w = originalImage.getWidth(this); // szerokość obrazka
        int h = originalImage.getHeight(this); // wysokość obrazka
        if (w != -1 && w != 0 && h != -1 && h != 0) {
            loaded = true;
            setPreferredSize(new Dimension(w, h));
        } else
            setPreferredSize(new Dimension(200, 200));
    }

}