import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.io.*;
import java.util.*;

/**Klasa odpowiadajaca za liste rekordow*/
public class BestScoresList {
    /**Lista przechowujaca informacje na temat wynikow*/
    static ArrayList<String> scores;

    /**
     * Metoda odpowiedzialna za zapisywanie wyniku gracza do listy. Automatycznie sortuje liste, ustala i sprawdza jej wielkosc, usuwajac nadmiarowy wynik.
     * @param score wynik gracza
     * @param playerName nazwa gracza
     */
    static void gameScore(String playerName, int score) throws IOException {
        loadScores();
        scores.add(playerName +  " : " + score);
        scores.sort(new MyComparator());
        if(scores.size() > 5)
            scores.remove(scores.size()-1);
        save();
    }

    /**Zapisywanie wynikow do pliku*/
    public static void save() throws IOException {
        InputStream file = new FileInputStream("doc/client/scores.txt");
        Properties prop = new Properties();
        prop.load(file);
        for(int i = 0; i< scores.size(); i++) {
            prop.setProperty("nick" + (i+1), scores.get(i).split(  " : ")[0]);
            prop.setProperty("score" + (i+1), scores.get(i).split( " : ")[1]);
        }
        prop.store(new FileOutputStream("doc/client/scores.txt"), null);
        file.close();
    }

    /**Odczytywanie wynikow z pliku*/
    public static void loadScores() throws IOException {
        InputStream file = new FileInputStream("doc/client/scores.txt");
        Properties scoresProperties = new Properties();
        scoresProperties.load(file);
        scores = new ArrayList<>();
        for(int i=1 ; i<11; i++){
            if(scoresProperties.containsKey("nick"+i))
                scores.add(scoresProperties.getProperty("nick"+i) + " : " + scoresProperties.getProperty("score"+i));
        }
        file.close();
        scores.sort(new MyComparator());
    }

    /**Implementacja klasy porownujacej z biblioteki standardowej. Uzywana do sortowania wynikow*/
    static class MyComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2){
            Integer a = Integer.parseInt(o1.split( " : ")[1]);
            Integer b = Integer.parseInt(o2.split( " : ")[1]);
            return -a.compareTo(b);
        }
    }
}

