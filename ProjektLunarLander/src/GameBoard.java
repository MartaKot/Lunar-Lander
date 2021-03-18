import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.TimerTask;


/**
 * Klasa odpowiedzialna za tworzenie elementow gry
 */
public class GameBoard extends Canvas {

    /**Obiekt sluzacy do animacji*/
    private BufferStrategy bufferStrategy = null;
    /** Pole informujące o nieudanym lądowaniu */
    boolean wasCrashed = false;
    /** Pole informujące o udanym lądowaniu */
    boolean wasSuccessful = false;
    /**predkosc pionowa */
    int vy;
    /** predkosc pozioma */
    int vx;
    /** polozenie poziome */
    int x;
    /** polozenie pionowe */
    int y;
    /** Przechowuje informacje o zatrzymaniu gry*/
    boolean gamePaused ;
    /** Aktualny wynik */
    int score = 500;
    /** obecny poziom paliwa */
    float fuel;


    /**
     * Konstruktor klasy GameBoard. Wczytuje poziom.
     * @param levelIndex numer poziomu wczytywany z pliku i wyświetlany na plansze.
     */
    GameBoard(int levelIndex) throws IOException {
        setPreferredSize(new Dimension(800, 600));
        FileParser.loadLevelConfigs(levelIndex);
        if(!Client.isOffline) FileParser.loadConfigsFromServer(levelIndex);
        if(Client.isOffline) FileParser.loadLevelConfigs(levelIndex);
        fuel = FileParser.fuelAmount;
        x = FileParser.startPoint;
    }

    /**
     * Metoda odpowiedzialna za symulacje fizyki. Ustala lokalizacje rakiety.
     */
    private void rocketLocation() {
        y = y + vy;
        if(vy < FileParser.maxVy) vy++;
        if(vx > FileParser.maxVy) vx = FileParser.maxVy;
        if(vx < -FileParser.maxVy) vx = -FileParser.maxVy;
        x = x + vx;
        if(x > FileParser.xSize*0.97)
            x = (int) (FileParser.xSize*0.97);
        if(y < 0)
            y = 0;
        if(x < 0)
            x = 0;
        if(vx > 1) vx--;
        if(vx < -1) vx++;
    }

    public void addNotify() {
        super.addNotify();
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
    }

    /**
     *Rysuje wielokat bedacy powierzchnia planety, ladowisko oraz rakiete
     * @param g kontekst graficzny
     */
    public void paint(Graphics g) {
        super.paint(g);

        Shape planet = new Polygon(FileParser.xPoints,FileParser.yPoints,FileParser.xPoints.length);
        Shape landingField = new Polygon(FileParser.xLandingField,FileParser.yLandingField,FileParser.xLandingField.length);

        Graphics2D g2d = (Graphics2D) g;
        setBackground(new Color(17, 17, 47));
        g.clearRect(0, 0, getWidth(), getHeight());

        AffineTransform scaleTransform = g2d.getTransform();
        AffineTransform scaleMatrix = new AffineTransform();
        float xScale =(1f+(getSize().width-FileParser.xSize)/(float) FileParser.xSize);
        float yScale =(1f+(getSize().height-FileParser.ySize)/(float)FileParser.ySize);
        scaleMatrix.scale(xScale, yScale);
        g2d.setTransform(scaleMatrix);

        Image rocketIMG = new ImageIcon("img/rocket.png").getImage();
        Rectangle2D rocket = new Rectangle2D.Float(x, y, 50, 100);
        g.drawImage(rocketIMG, x, y, 50, 100, null);

        g2d.setColor(Color.lightGray);
        g2d.fill(landingField);

        g2d.setColor(Color.lightGray);
        g2d.fill(planet);

        g2d.setTransform(scaleTransform);
        isColliding(planet, landingField, rocket);
    }

    /**
     * Metoda odpowiedzialna za okreslanie czy ladowanie bylo pomyslne oraz wykrywa kolizje.
     * @param planet ksztalt planety
     * @param landingField ksztalt ladowiska
     * @param rocket rakieta, ktora porusza sie gracz
     */
    private void isColliding(Shape planet, Shape landingField, Rectangle2D rocket) {
        if((planet.intersects(rocket)) || (landingField.intersects(rocket)) && Math.abs(vy) > FileParser.properVy) {
            wasCrashed = true;
            wasSuccessful = false;
        }
        if(landingField.intersects(rocket) && Math.abs(vy) <= FileParser.properVy && Math.abs(vx) <= Math.abs(FileParser.properVx)) {
            wasSuccessful = true;
            wasCrashed = false;
        }
    }

    /** Metoda pauzujaca gre */
    public void gamePaused() {

        this.gamePaused = true;
    }

    /** Metoda wznawiajaca gre */
    public void gameResumed() {

        this.gamePaused = false;
    }

    /** Klasa odpowiadająca za animacje, zmienia lokalizacje rakiety, odpowiada za punkty (im dłuższa rozgrywka tym mniej punktów)  */
    public class AnimationTimerTask extends TimerTask {
        int i = 0;
        public void run() {
            if(!gamePaused) {
                if (i == 2) {
                    rocketLocation();
                    i = 0;
                }
                i++;
            }
            do {
                do {
                    Graphics graphics = bufferStrategy.getDrawGraphics();
                    paint(graphics);
                    graphics.dispose();
                    if(!gamePaused) score--;

                } while (bufferStrategy.contentsRestored());
                bufferStrategy.show();
            } while (bufferStrategy.contentsLost());
        }
    }
}