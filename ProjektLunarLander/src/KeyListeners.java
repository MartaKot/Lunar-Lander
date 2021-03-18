import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Klasa odpowiedzialna za obsluge zdarzen z klawiatury
 */
public class KeyListeners implements KeyListener {
    /** obiekt na który mają wpływ zdarzenia z klawiatury */
    private GameBoard gameBoard;

    KeyListeners(GameBoard gameBoard) { this.gameBoard = gameBoard; };

    @Override
    public void keyTyped(KeyEvent event) { }

    /**
     * Obsługa zdarzeń z klawiatury gdy przycisk jest przytrzymany i
     * spowodowanie tym zmniejszenia ilości paliwa oraz ruchu rakiety
     */
    @Override
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();

        if (key == KeyEvent.VK_RIGHT) gameBoard.vx +=4;
        if (key == KeyEvent.VK_LEFT) gameBoard.vx -=4;
        if (key == KeyEvent.VK_UP && gameBoard.vy > -10) gameBoard.vy -=4;
        gameBoard.fuel--;
    }

    /**
     * obsluga zdarzeń w przypadku gdy przycisk zostaje zwolniony
     */
    @Override
    public void keyReleased(KeyEvent event) {
        int key = event.getKeyCode();
        if (key == KeyEvent.VK_UP)  gameBoard.vy = gameBoard.vy-4;
    }
}
