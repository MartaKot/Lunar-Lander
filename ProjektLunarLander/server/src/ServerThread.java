import java.io.*;
import java.net.Socket;

/**
 * Klasa odpowiedzialna za wątek w którym obsługiwane są żądania klientów przez serwer
 */
public class ServerThread implements Runnable {

    private Socket socket;
    public ServerThread(Socket socket){
        this.socket = socket;
    }

    /**
     * Odczytuje zadanie klienta i wywołuję metode ktora ma za zadnie na nie odpowiedziec
     */
    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(10);
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                String fromClient = bufferedReader.readLine();
                if (fromClient != null) {
                    System.out.println("Client: " + fromClient);
                    String serverRespond = Protocol.serverAction(fromClient);
                    printWriter.println(serverRespond);
                    printWriter.flush();
                    System.out.println("Server: " + serverRespond);
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Your connection was lost");
        }
    }
}
