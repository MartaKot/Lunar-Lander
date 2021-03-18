import java.io.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;


/**
 * Klasa odpowiedzialna za wczytywanie z plików konfiguracyjnych.
 * Z pliku Maps.txt odczytywane są wielkosci wedlug ktorych generowana jest mapa.
 * reprezentują wspolrzedne(xpoints, ypoints) kolejnych wierzchołków wielokata z ktorych generowana jest nierownomierna powierzchnia planety,
 * landing sklada sie z czterech wartosci ktore okreslaja wspołrzedne oraz wymiary ladowiska.
 * Z pliku Config.txt wczytywane sa elementy związane z grą.
 */
public class FileParser {

    /**
     * Okresla startowa szerokosc okna
     */
    static int xSize;
    /**
     * Okresla startowa wysokosc okna
     */
    static int ySize;
    /**
     * Predkosc pozioma statku
     */
    static int vx;
    /**
     * Predkosc pionowa statku
     */
    static int vy;
    /**
     * Okresla odpowiednią predkość w płaszczyznie X
     */
    static int properVx;
    /**
     * Określa odpowiednią prędkość w płaszczyznie Y
     */
    static int properVy;
    /**
     * Okresla maksymalna pionowa predkosc
     */
    static int maxVy;
    /**
     * Okresla ilość paliwa
     */
    static int fuelAmount;
    /**
     * Okresla ilosc poziomów
     */
    static int numberOfLevels;
    /**
     * Okresla ilosc statkow
     */
    static int numberOfRockets;
    /**
     * Okresla ilosc punktow przyznawanych za pozostale statki
     */
    static int bonusPerRocket;
    /**
     * Okersla punkt startowy w ktorym pojawia sie gracz
     */
    static int startPoint;
    /**
     * tablica przechowujaca x'owe wspolrzedne wierzcholkow wielokata bedacych powierzchnia planety
     */
    static int[] xPoints;
    /**
     * tabblica przechowujaca y'owe wspolrzedne wierzcholkow wielokata bedacych powierzchnia planety
     */
    static int[] yPoints;
    /**
     * tablica przechowujaca x'owe wspolrzedne wierzcholkow wielokata bedacego ladowiskiem
     */
    static int[] xLandingField;
    /**
     * tablica przechowujaca y'owe wspolrzedne wierzcholkow wielokata bedacego ladowiskiem
     */
    static int[] yLandingField;
    /**
     * tablica przechowująca zasady gry
     */
    static String[] rules, scores;
    /**
     * Adres ip serwera
     */
    static String ipAddress;
    /**
     * Port na ktorym dziala serwer
     */
    static int port;



    /**
     * Wczytuje dane z plikow konfiguracyjnych i zapisujaca je do odpowiednich pol w klasie
     */
    static void loadConfig() throws IOException {
        InputStream configFile = new FileInputStream("doc/client/Config.txt");
        Properties properties = new Properties();
        properties.load(configFile);
        xSize = Integer.parseInt(properties.getProperty("xSize"));
        ySize = Integer.parseInt(properties.getProperty("ySize"));
        numberOfLevels = Integer.parseInt(properties.getProperty("numberOfLevels"));
        vx = Integer.parseInt(properties.getProperty("Vx"));
        vy = Integer.parseInt(properties.getProperty("Vy"));
        properVx = Integer.parseInt(properties.getProperty("properVx"));
        properVy = Integer.parseInt(properties.getProperty("properVy"));
        maxVy = Integer.parseInt(properties.getProperty("maxVy"));
        fuelAmount = Integer.parseInt(properties.getProperty("fuelAmount"));
        numberOfRockets = Integer.parseInt(properties.getProperty("numberOfRockets"));
        bonusPerRocket = Integer.parseInt(properties.getProperty("bonusPerRocket"));

        configFile.close();
    }

    /** Wczytywanie zasad gry z pliku*/
    public static void loadRules() throws IOException {
        File file = new File("doc/client/rules.txt");

        Scanner in = new Scanner(file);
        rules = new String[50];
        if (file.exists()) {
            int i = 0;
            while (in.hasNext()) {
                rules[i] = in.nextLine();
                i++;
            }
        }
    }

    /** Odpowiada za przeskalowywanie punktów na mapie gry*/
    private static void transformPoints()
    {
        yPoints = Arrays.stream(yPoints).map(ypoints -> (int)(1.25*FileParser.ySize*0.01*ypoints)).toArray();
        xPoints = Arrays.stream(xPoints).map(xpoints -> (int)(1.25*FileParser.xSize*0.01*xpoints)).toArray();
        xLandingField = Arrays.stream(xLandingField).map(xlanding -> (int)(1.25*FileParser.xSize*0.01*xlanding)).toArray();
        yLandingField = Arrays.stream(yLandingField).map(ylanding -> (int)(1.25*FileParser.ySize*0.01*ylanding)).toArray();
        startPoint = (int)(1.25*startPoint * 0.01 * FileParser.xSize);
    }


    /**
     * Wczytuje wspolrzedne z pliku konfiguracyjnego i zapisuje je do odpowednich tablic
     * @param levelIndex oznacza numer poziomu ktory chcemy wczytac z pliku konfiguracyjnego serwera
     */

    static void loadLevelConfigs(int levelIndex) throws IOException {
        InputStream propertiesFile2 = new FileInputStream("doc/client/Maps.txt");
        Properties mapProperties = new Properties();
        mapProperties.load(propertiesFile2);
        xPoints = Arrays.stream(mapProperties.getProperty("xpoints" + levelIndex).split("-")).mapToInt(Integer::parseInt).toArray();
        yPoints = Arrays.stream(mapProperties.getProperty("ypoints" + levelIndex).split("-")).mapToInt(Integer::parseInt).toArray();
        xLandingField = Arrays.stream(mapProperties.getProperty("xlanding" + levelIndex).split("-")).mapToInt(Integer::parseInt).toArray();
        yLandingField = Arrays.stream(mapProperties.getProperty("ylanding" + levelIndex).split("-")).mapToInt(Integer::parseInt).toArray();
        startPoint = Integer.parseInt(mapProperties.getProperty("startPoint" + levelIndex));
        transformPoints();
    }

    /** zapis portu i adresu IP */
    static void savePortAndIP(int port,String ipAddress){
        FileParser.port = port;
        FileParser.ipAddress = ipAddress;
    }

    /**
     *Wczytuje dane z plikow konfiguracyjnych serwera i zapisuje je do odpowiednich pol w klasie
     */
    static void loadServerConfigs() throws IOException {
        String response = Client.getConfig();
        int[] config;
        config = Arrays.stream(response.split("-")).mapToInt(Integer::parseInt).toArray();
        xSize = config[0];
        ySize = config[1];
        properVx = config[2];
        properVy = config[3];
        fuelAmount = config[4];
        numberOfLevels = config[5];
        numberOfRockets = config[6];
        bonusPerRocket = config[7];
        maxVy = config[8];
    }

    /**
     * Wczytuje wspolrzedne z pliku konfiguracyjnego serwera i zapisuje je do odpowednich tablic
     * @param levelIndex numer poziomu ktory chcemy wczytac z pliku konfiguracyjnego serwera
     */
    static void loadConfigsFromServer(int levelIndex) throws IOException {
        String response = Client.getLevel(levelIndex);
        String[] configs = response.split("-");
        xPoints = Arrays.stream(configs[0].split(" ")).mapToInt(Integer::parseInt).toArray();
        yPoints = Arrays.stream(configs[1].split(" ")).mapToInt(Integer::parseInt).toArray();
        xLandingField = Arrays.stream(configs[2].split(" ")).mapToInt(Integer::parseInt).toArray();
        yLandingField = Arrays.stream(configs[3].split(" ")).mapToInt(Integer::parseInt).toArray();
        startPoint = Integer.parseInt(configs[4]);
        transformPoints();
    }
}