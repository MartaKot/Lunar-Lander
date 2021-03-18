import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;


public class StartWindow extends JFrame implements ActionListener {

    /**
     *  przycisk włączający grę w trybie online
     */
    private JButton onlineGameButton;
    /**
     * przycisk włączający grę w trybie offline
     */
    private JButton offlineGameButton;
    /**
     * przycisk pomocy
     */
    private JButton helpButton;
    /**
     * panel obrazu głównego
     */
    private SpringLayout springLayout = new SpringLayout();
    /**
     * Panel obrazu głównego
     */
    private ImagePanel imagePanel;
    /**
     * Ikona pomocy
     */
    private Icon helpButtonIcon;
    private JFrame onlineGameFrame;
    private JLabel ipLabel, portLabel, connectionLabel;
    private TextField ipField, portField;
    private JButton okButton;


    /**
     * Konstruktor
     * <p>
     * Tworzone i inicjalizowane jest całe okno startowe
     */
    public StartWindow() {
        super("Lunar Lander");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(300, 200));
        setSize(new Dimension(680, 600));
        imagePanel = new ImagePanel("img/tlo.png");
        setLayout(springLayout);
        Container pane = getContentPane();
        pane.setBackground(new Color(255, 164, 150, 209));

        onlineGameButton = new JButton("  PLAY ONLINE  ");
        onlineGameButton.setOpaque(false);
        onlineGameButton.setContentAreaFilled(false);
        onlineGameButton.setBorder(new LineBorder(Color.white,2,true));
        onlineGameButton.setFont(new Font("Impact", Font.PLAIN, 35));
        onlineGameButton.setForeground(Color.white);

        offlineGameButton = new JButton("  PLAY OFFLINE  ");
        offlineGameButton.setOpaque(false);
        offlineGameButton.setContentAreaFilled(false);
        offlineGameButton.setBorder(new LineBorder(Color.white,2,true));
        offlineGameButton.setFont(new Font("Impact", Font.PLAIN, 35));
        offlineGameButton.setForeground(Color.white);

        helpButtonIcon = new ImageIcon("img/help.png");
        helpButton = new JButton(helpButtonIcon);
        helpButton.setOpaque(false);
        helpButton.setContentAreaFilled(false);
        helpButton.setToolTipText("HELP");
        helpButton.setBorderPainted(false);

        //koordynaty helpButton
        springLayout.putConstraint(SpringLayout.SOUTH, helpButton, -10, springLayout.SOUTH, pane);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, helpButton, -270, SpringLayout.HORIZONTAL_CENTER, pane);

        //koordynaty offlineGameButton
        springLayout.putConstraint(SpringLayout.SOUTH, offlineGameButton, -150, SpringLayout.SOUTH, pane);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, offlineGameButton, 0, SpringLayout.HORIZONTAL_CENTER, pane);

        //koordynaty onlineGameButton
        springLayout.putConstraint(SpringLayout.SOUTH, onlineGameButton, -90, SpringLayout.SOUTH, pane);
        springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, onlineGameButton, 0, SpringLayout.HORIZONTAL_CENTER, pane);

        // wyswietlenie przycisków na ekranie
        pane.add(helpButton);
        pane.add(offlineGameButton);
        pane.add(onlineGameButton);

        add(imagePanel);

        helpButton.addActionListener(this);
        offlineGameButton.addActionListener(this);
        onlineGameButton.addActionListener(this);

        //Gdy okno zmienia swój rozmiar tworzone jest zdarzenie i wtedy aktualizowany jest rozmiar obrazu będącego jego tłem
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


        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onlineGame() throws IOException{
        onlineGameFrame = new JFrame("Lunar Lander");
        onlineGameFrame.setLayout(new GridLayout(3,2,15,15));
        onlineGameFrame.setSize(550,250);
        onlineGameFrame.getContentPane().setBackground(new Color(0x344977));
        onlineGameFrame.setLocationRelativeTo(null);
        onlineGameFrame.setVisible(true);
        onlineGameFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ipLabel = new JLabel("Server ip address: ");
        ipLabel.setFont(new Font("Impact",Font.PLAIN, 20));
        ipLabel.setForeground(Color.white);
        onlineGameFrame.add(ipLabel);

        ipField = new TextField("127.0.0.1");
        ipField.setFont(new Font("Arial", Font.PLAIN, 20));
        onlineGameFrame.add(ipField);

        portLabel = new JLabel("Port: ");
        portLabel.setFont(new Font("Impact",Font.PLAIN, 20));
        portLabel.setForeground(Color.white);
        onlineGameFrame.add(portLabel);

        portField = new TextField("8080");
        portField.setMinimumSize(new Dimension(180, 30));
        portField.setPreferredSize(new Dimension(180, 30));
        portField.setFont(new Font("Arial", Font.PLAIN, 24));
        onlineGameFrame.add(portField);

        connectionLabel = new JLabel();
        connectionLabel.setFont(new Font("Impact", Font.PLAIN, 18));
        connectionLabel.setForeground(Color.white);
        onlineGameFrame.add(connectionLabel);

        okButton = new JButton("OK");
        okButton.setOpaque(true);
        okButton.setBackground(Color.decode("#4E85CA"));
        okButton.setBorder(new LineBorder(new Color(0x344977),5,false));
        okButton.setFont(new Font("Impact", Font.PLAIN, 25));
        okButton.setForeground(Color.white);
        onlineGameFrame.add(okButton);
        okButton.addActionListener(event -> {
            try {
                new GameWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
            onlineGameFrame.dispose();
        });
        removeAll();
        revalidate();
    }


    /**
     * Obsługa zdarzeń
     * @param event
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        Object source = event.getSource();

        if (source == helpButton) {

            try {
                new HelpWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (source == onlineGameButton) {
            try {
                dispose();
                onlineGame();
                FileParser.savePortAndIP(Integer.parseInt(portField.getText()),ipField.getText());
                FileParser.loadServerConfigs();

            } catch (IOException e) {
                System.out.println("Connection not established");
                connectionLabel.setText("Connection not established");
                this.revalidate();
            }
            if(!Client.checkIfOffline()) {
                System.out.println("Connection established");
            }

        }
        if (source == offlineGameButton) {

            try {
                new GameWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dispose();

        }

    }

}