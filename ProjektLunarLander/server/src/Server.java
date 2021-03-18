import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * Klasa odpowiadająca za obsluge żądań klientow i tworzenia nowych wątków w momencie otrzymania żądania.
 */
public class Server {
    int port;
    /**
     * Konstruktor w ktorym przypisywany jest numer portu z pliku konfiguracyjnego.
     */
    public Server() throws IOException {
        FileParser.loadPort();
        port = FileParser.port;
    }
    /**
     * Serwer oczekuje na żądania od klientow. W momencie gdy pojawi sie klient nastepuje utworzenie nowego watku
     * w ktorym klient jest obslugiwany a serwer dalej oczekuje na kolejne żądania
     */
    public void runServer() throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Waiting for connections.");
        while (true){
            Thread.sleep(10);
            Socket socket = serverSocket.accept();
            new Thread(new ServerThread(socket)).start();
        }
    }
}
