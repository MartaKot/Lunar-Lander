import java.io.*;
import java.net.Socket;

/**
 * Klasa która odpowiada za komunikacje z serwerem. Są w niej metody służące do
 * nawiązania polaczenia z serwerem i wysylania do niego żądań
 */
public class Client {
    static Socket socket;
    /** informacja czy serwer jest nieosiągalny - true, osiągalny - true */
    static boolean isOffline = true;
    /**
     * Wysłanie do serwera żądania o dane konfiguracyjne, wywołanie metody connect z zapytaniem
     * @return linia tekstu bedaca odpowiedzia od serwera
     */
    static String getConfig() throws IOException {
        String respond = connect("getConfig");
        socket.close();
        return respond;
    }

    /**
     * Wysłanie do serwera żądania o dane konfiguracyjne poziomu o konkretnym indeksie, wywołanie metody connect z zapytaniem
     * @param index numer poziomu ktory chcemmy uzyskac
     * @return linia tekstu będąca odpowiedzia od serwera
     */
    static String getLevel(int index) throws IOException {
        String respond = connect("getLevel" + "-" + index);
        socket.close();
        return respond;
    }
    /**
     * Sprawdza czy program moze połaczyc sie z serwerem
     * @return boolean, jesli true - nie mozna ustanowic polaczenia, jesli false - udalo się ustanowic
     * polaczenie
     */
    static boolean checkIfOffline(){
        try {
            isOffline = false;
            socket = new Socket(FileParser.ipAddress, FileParser.port);
        } catch (IOException e) {
            System.out.println("Server is offline");
            isOffline = true;
        }
        return isOffline;
    }

    /**
     * Ustanawia polaczenie z serwerem. Tworzy obiekt klasy socket i wysyla zadanie do serwera
     * @param command Tresc zadania
     * @return linia tekstu będaca odpowiedzia serwera
     */
    private static String connect(String command) throws IOException {
        try {
            socket = new Socket(FileParser.ipAddress, FileParser.port);
        } catch (Exception e){
            System.out.println("Server is offline");
        }
        socket = new Socket(FileParser.ipAddress, FileParser.port);
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(command);
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine();
    }

}
