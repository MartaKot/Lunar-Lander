import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.Timer;



/**
 * Klasa odpowiedzialna za tworzenie okna i pobieranie nicku od gracza
 */
public class GameWindow extends JFrame implements ActionListener {

    /** przechowuje aktualną liczbę statków */
    private static int numberOfRockets;
    /** Przechowuje numer obecnego poziomu */
    private int currentLevel;
    /** pole do wpisania nicku gracza*/
    private JTextField playerNameTextField;
    /** przycisk rozpoczynający grę*/
    private JButton startButton;
    /** przycisk cofający do okna startowego*/
    private JButton backButton;
    /** przycisk do wyświetlania tabelki z wynikami */
    private JButton bestScoresButton;
    /** przycisk do zatrzymywania gry */
    private JButton pauseButton;
    /** panel obrazu głównego */
    private SpringLayout springLayout = new SpringLayout();
    /** Przechowuje nick gracza */
    private String playerName;
    /** Przechowuje wynik gracza w trakcie rozgrywki*/
    private int score;
    /** Przechowuje informacje o zatrzymaniu gry*/
    public boolean gamePaused = false;
    /**Licznik odpowiedzialny za animacje*/
    private Timer timer;
    /** zmienna tworząca przestrzeń gry */
    private JFrame gameArea;
    /** panel obrazu głównego */
    private ImagePanel imagePanel;


    /**
     * Konstruktor
     * <p>
     * Tworzy okno do wprowadzenia nazwy gracza i odpowiada za rozpoczęcie gry
     */
    GameWindow() throws IOException {
        super("Lunar Lander");
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentLevel=1;
        FileParser.loadConfig();
        numberOfRockets = FileParser.numberOfRockets;
        score = 0;
        Container pane = getContentPane();
        setLayout(springLayout);
        imagePanel = new ImagePanel("img/nickname.png");

        playerNameTextField = new JTextField();
        playerNameTextField.setMinimumSize(new Dimension(180, 50));
        playerNameTextField.setPreferredSize(new Dimension(180, 50));
        playerNameTextField.setBackground(Color.white);
        playerNameTextField.setFont(new Font("Arial", Font.PLAIN, 24));

        startButton = new JButton("  START  ");
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorder(new LineBorder(Color.white,2,true));
        startButton.setFont(new Font("Impact", Font.PLAIN, 25));
        startButton.setForeground(Color.white);

        backButton = new JButton("  BACK  ");
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorder(new LineBorder(Color.white,2,true));
        backButton.setFont(new Font("Impact", Font.PLAIN, 25));
        backButton.setForeground(Color.white);

        //koordynaty startButton
        springLayout.putConstraint(SpringLayout.SOUTH, startButton, -30, springLayout.SOUTH, pane);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, startButton, -50, SpringLayout.HORIZONTAL_CENTER, pane);

        //koordynaty backButton
        springLayout.putConstraint(SpringLayout.SOUTH, backButton, -30, springLayout.SOUTH, pane);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, backButton, 50, SpringLayout.HORIZONTAL_CENTER, pane);

        //koordynaty playerNameTextField
        springLayout.putConstraint(SpringLayout.SOUTH, playerNameTextField, -80, SpringLayout.SOUTH, pane);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, playerNameTextField, 0, SpringLayout.HORIZONTAL_CENTER, pane);

        pane.add(playerNameTextField);
        pane.add(startButton);
        pane.add(backButton);

        backButton.addActionListener(this);
        startButton.addActionListener(this);
        playerNameTextField.addActionListener(this);

        setVisible(true);

        add(imagePanel);

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                imagePanel.setSize(getWidth(), getHeight());
                imagePanel.repaint();
                repaint();
            }

            @Override
            public void componentMoved(ComponentEvent e) {}

            @Override
            public void componentShown(ComponentEvent e) {}

            @Override
            public void componentHidden(ComponentEvent e) {}
        });

    }


    private void gameView(String playerName, int levelIndex) throws IOException {
        gameArea = new JFrame("Lunar Lander");
        gameArea.setSize(800, 700);
        gameArea.setMinimumSize(new Dimension(600, 400));
        gameArea.setVisible(true);
        gameArea.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameArea.setBackground(new Color(17, 17, 47));
        gameArea.setLocationRelativeTo(null);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        GameBoard gameBoard = new GameBoard(levelIndex);
        this.timer = new Timer();
        timer.scheduleAtFixedRate(gameBoard.new AnimationTimerTask(),100, 30);
        this.playerName = playerName;

        //wlasciwosci pauseButton
        ImageIcon pauseImageIcon = null;
        try {
            pauseImageIcon = new ImageIcon("img/pause.png");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        pauseButton = new JButton(pauseImageIcon);
        pauseButton.setOpaque(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setToolTipText("Pause");
        pauseButton.setBorderPainted(false);
        infoPanel.add(pauseButton);

        //wlasciwosci Label
        Label levelLabel = new Label();
        levelLabel.setForeground(Color.white);
        levelLabel.setFont(new Font("Impact", Font.PLAIN, 14));
        infoPanel.add(levelLabel);

        //wlasciwosci rocketsLabel
        Label rocketsLabel = new Label();
        rocketsLabel.setForeground(Color.white);
        rocketsLabel.setFont(new Font("Impact", Font.PLAIN, 14));
        infoPanel.add(rocketsLabel);

        //wlasciwosci scoreLabel
        Label scoreLabel = new Label();
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(new Font("Impact", Font.PLAIN, 14));
        infoPanel.add(scoreLabel);

        //wlasciwosci playerNameLabel
        Label playerNameLabel= new Label();
        playerNameLabel.setForeground(new Color(0xFF00F1));
        playerNameLabel.setFont(new Font("Impact", Font.PLAIN, 16));
        playerNameLabel.setText(this.playerName);
        infoPanel.add(playerNameLabel);

        //wlasciwosci fuelLabel
        JProgressBar fuelBar = new JProgressBar();
        fuelBar.setBackground(Color.RED);
        fuelBar.setFont(new Font("Impact", Font.PLAIN, 20));
        fuelBar.setMaximum(100);
        fuelBar.setStringPainted(true);
        infoPanel.add(fuelBar);

        //wlasciwosci bestScoresButton
        ImageIcon bestResultsImageIcon = null;
        try {
            bestResultsImageIcon = new ImageIcon("img/trophy.png");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        bestScoresButton = new JButton(bestResultsImageIcon);
        bestScoresButton.setOpaque(false);
        bestScoresButton.setContentAreaFilled(false);
        bestScoresButton.setBorderPainted(false);
        bestScoresButton.setToolTipText("Best results");
        bestScoresButton.setContentAreaFilled(false);
        infoPanel.add(bestScoresButton);
        bestScoresButton.addActionListener(event-> {
            try {
                showScores();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        pauseButton.addActionListener(event->pauseGame(gameBoard));
        gameArea.add(gameBoard);
        KeyListeners keyListeners = new KeyListeners(gameBoard);
        gameArea.addKeyListener(keyListeners);
        gameArea.requestFocus();

        new Thread(() -> {
            while (!gameBoard.wasSuccessful && !gameBoard.wasCrashed){
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rocketsLabel.setText("Rockets: " + numberOfRockets);
                levelLabel.setText("Level: "+this.currentLevel);
                scoreLabel.setText("Score: "+this.score);
                fuelBar.setValue((int) ((int) 2*gameBoard.fuel));
                if(gameBoard.fuel <= 0 ) {
                    gameArea.removeKeyListener(keyListeners);
                }

            }
            try {
                resetTimer();
                if(gameBoard.wasCrashed && numberOfRockets <= 1) {
                    endGameWindow(playerName, score);
                    gameArea.dispose();
                }
                if(gameBoard.wasCrashed && numberOfRockets > 1) {
                    endLevelWindow(false);
                    gameArea.dispose();
                }
                if(gameBoard.wasSuccessful) {
                    endLevelWindow(true);
                    score = score + numberOfRockets*10 + gameBoard.score;
                    gameArea.dispose();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        MatteBorder matteBorder = new MatteBorder(0, 0, 0, 0, (new Color(0x000022)));
        infoPanel.setBackground(Color.darkGray);
        infoPanel.setBorder(matteBorder);
        gameArea.add(infoPanel, BorderLayout.NORTH);
        gameArea.add(gameBoard);

    }


    /**
     * Wyświetla okno pokazujace sie po zakończeniu gry. Umożliwia sprawdzenie najlepszych wyników i powrót do okna startowego
     */
    private void endGameWindow(String playerName, int score) throws IOException {
        JFrame endGameFrame = new JFrame("Lunar Lander");
        JLabel endGameLabel = new JLabel("You've finished the game! Your score: " + this.score);
        endGameLabel.setFont(new Font("Impact", Font.PLAIN, 25));
        endGameLabel.setForeground(Color.white);
        endGameLabel.setHorizontalAlignment(JLabel.CENTER);
        JButton okButton = new JButton("OK");
        okButton.setOpaque(true);
        okButton.setBackground(Color.decode("#4E85CA"));
        okButton.setBorder(new LineBorder(new Color(0x344977),20,false));
        okButton.setFont(new Font("Impact", Font.PLAIN, 25));
        okButton.setForeground(Color.white);

        BestScoresList.gameScore(playerName, score);

        ImageIcon bestResultsImageIcon = null;
        try {
            bestResultsImageIcon = new ImageIcon("img/trophy.png");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        JButton bestScoresButton = new JButton(bestResultsImageIcon);
        bestScoresButton.setOpaque(false);
        bestScoresButton.setContentAreaFilled(false);
        bestScoresButton.setBorderPainted(false);
        bestScoresButton.setToolTipText("Best results");
        bestScoresButton.addActionListener(event-> {
            try {
                showScores();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        endGameFrame.setLayout(new BorderLayout());
        endGameFrame.add(bestScoresButton, BorderLayout.NORTH);
        endGameFrame.add(endGameLabel, BorderLayout.CENTER);
        endGameFrame.add(okButton, BorderLayout.SOUTH);

        endGameFrame.setSize(550,250);
        endGameFrame.getContentPane().setBackground(new Color(0x344977));
        endGameFrame.setLocationRelativeTo(null);
        endGameFrame.setVisible(true);
        endGameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        okButton.addActionListener(event -> {
            new StartWindow();
            endGameFrame.dispose();
        });
        removeAll();
        revalidate();
    }


    /**
     * Metoda wczytująca kolejny poziom lub wyświetlanie okna informującego o ukończeniu gry
     */
    private void loadNextLevel() throws IOException {
        resetTimer();
        if (currentLevel < FileParser.numberOfLevels) {
            currentLevel++;
            removeAll();
            gameView(playerName, currentLevel);
        }
        else {
            endGameWindow(playerName, score);
        }
    }


    /**
     * Wyświetlane okna z informacją, czy lądowanie gracza było pomyślne
     * @param wasSuccessful true - informuje o pomyslnym lądowaniu, false - informuje o niepomyslnym lądowaniu
     */
    private void endLevelWindow(boolean wasSuccessful) {
        JFrame endLevelFrame = new JFrame("Lunar Lander");
        endLevelFrame.setLayout(new BorderLayout());

        endLevelFrame.setSize(300,170);
        endLevelFrame.getContentPane().setBackground(new Color(0x344977));
        endLevelFrame.setLocationRelativeTo(null);
        endLevelFrame.setVisible(true);
        endLevelFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel textLabel = new JLabel();
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setFont(new Font("Impact", Font.PLAIN, 34));
        textLabel.setForeground(Color.white);

        JButton okButton = new JButton("OK");
        okButton.setOpaque(true);
        okButton.setBackground(Color.decode("#4E85CA"));
        okButton.setBorder(new LineBorder(new Color(0x344977),20,false));
        okButton.setFont(new Font("Impact", Font.PLAIN, 25));
        okButton.setForeground(Color.white);


        if(wasSuccessful) {
            textLabel.setText("Level completed!");

            endLevelFrame.add(textLabel, BorderLayout.CENTER);
            endLevelFrame.add(okButton, BorderLayout.SOUTH);

            okButton.addActionListener(event -> {
                endLevelFrame.dispose();
                try{
                    loadNextLevel();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        else {
            textLabel.setText("You've failed!");

            endLevelFrame.add(textLabel, BorderLayout.CENTER);
            endLevelFrame.add(okButton, BorderLayout.SOUTH);

            okButton.addActionListener(event -> {
                try {
                    gameView(playerName, currentLevel);
                    numberOfRockets--;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                endLevelFrame.dispose();
            });
        }
    }


    /**Metoda służąca do pauzowania gry, wywoluje metody klasy GameBoard.
     * @param gameBoard informuje ktory poziom zostaje zatrzymany
     */
    private void pauseGame(GameBoard gameBoard) {
        if(gamePaused) {
            gameBoard.gameResumed();
            gamePaused = false;
            gameArea.requestFocus();

        } else {
            gameBoard.gamePaused();
            gamePaused = true;
        }
    }


    /**
     * Resetuje licznik
     */
    private void resetTimer() {
        timer.cancel();
        timer.purge();
    }


    /**
     * Obsługa zdarzeń
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        Object source = event.getSource();

        if (source == backButton) {

            new StartWindow();
            dispose();
        }
        if (source == startButton) {

            String playerName = playerNameTextField.getText();

            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter your nickname", "Lunar Lander", JOptionPane.PLAIN_MESSAGE);
            }
            else if(playerNameTextField.getText().length()>9) {
                JOptionPane.showMessageDialog(this, "Nickname is too long", "Lunar Lander", JOptionPane.PLAIN_MESSAGE);
            }
            else {
                try {
                    gameView(playerName, currentLevel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                numberOfRockets = FileParser.numberOfRockets;
                score = 0;

                dispose();
            }
            if (source == bestScoresButton ){
                try {
                    showScores();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void showScores() throws IOException {
        new BestScoreWindow();
    }


}