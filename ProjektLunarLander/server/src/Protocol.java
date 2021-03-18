import java.io.IOException;
/**
 * Klasa w której ustalone są odpowiedzi serwera na żądanie klienta
 */
public class Protocol {
    /**
     * Konkretne żądanie klienta pozwala wybrać odpowiednia odpowiedź
     * @param command treść żądania klienta
     * @return odpowiedź serwera
     */
    public static String serverAction(String command) throws IOException {
        String serverMessage;
        String[] commands = command.split("-");
        switch (commands[0]) {
            case "getConfig":
                serverMessage = FileParser.loadConfig();
                break;
            case "getLevel":
                serverMessage = FileParser.loadLevelConfigs(Integer.parseInt(commands[1]));
                break;
            default:
                serverMessage = "Invalid command";
        }
        return serverMessage;
    }
}