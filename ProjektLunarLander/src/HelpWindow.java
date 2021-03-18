import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Klasa wyswietlajaca okno ze wskazówkami do gry
 */
public class HelpWindow extends JFrame implements ActionListener {

    /**
     * Przycisk back - powrót do menu
     */
    private JButton backButton;

    /**
     * Konstruktor okna
     */
    public HelpWindow() throws IOException {
        super("Lunar Lander");
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Container pane = getContentPane();
        pane.setBackground(new Color(0x344977));

        backButton = new JButton("BACK");
        backButton.setOpaque(true);
        backButton.setBackground(Color.decode("#4E85CA"));
        backButton.setBorder(new LineBorder(new Color(0x344977),20,false));
        backButton.setFont(new Font("Impact", Font.PLAIN, 30));
        backButton.setForeground(Color.white);
        backButton.addActionListener(this);
        add(backButton, BorderLayout.SOUTH);

        JLabel textLabel = new JLabel("Game rules");
        textLabel.setFont(new Font("Impact", Font.PLAIN, 40));
        textLabel.setForeground(Color.white);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        add(textLabel, BorderLayout.NORTH);

        FileParser.loadRules();
        JLabel[] label= new JLabel[20];
        for(int i=0; i<19; i++) {
            label[i] = new JLabel(FileParser.rules[i]);
            label[i].setBounds(20,-200+20*i,500,500);
            label[i].setFont(new Font("Impact", Font.PLAIN, 20));
            label[i].setForeground(Color.white);
            add(label[i]);
        }


        setVisible(true);
    }


    /**
     * Obsluga zdarzenia przycisku
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == backButton) {

            dispose();

        }
    }


}
