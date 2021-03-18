import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Klasa wyswietlająca okno najlepszymi wynikami (odczytanymi z pliku)
 **/
public class BestScoreWindow extends JFrame implements ActionListener {

    /**
     * przycisk powrotu do menu
     */
    private JButton backButton;


    /**
     * Konstruktor okna
     */
    BestScoreWindow() throws IOException {
        super("Lunar Lander");
        setMinimumSize(new Dimension(250, 270));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Container pane = getContentPane();
        pane.setBackground(new Color(0x344977));

        backButton = new JButton("OK");
        backButton.setOpaque(true);
        backButton.setBackground(Color.decode("#4E85CA"));
        backButton.setBorder(new LineBorder(new Color(0x344977),20,false));
        backButton.setFont(new Font("Impact", Font.PLAIN, 20));
        backButton.setForeground(Color.white);
        backButton.addActionListener(this);
        add(backButton, BorderLayout.SOUTH);

        JLabel textLabel = new JLabel("Best scores: ");
        textLabel.setFont(new Font("Impact", Font.PLAIN, 25));
        textLabel.setForeground(Color.white);
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        add(textLabel, BorderLayout.NORTH);

        BestScoresList.loadScores();
        JLabel[] label = new JLabel[BestScoresList.scores.size()+1];
        for(int i = 0; i< BestScoresList.scores.size(); i++) {
            label[i] = new JLabel((i+1) + ". " + BestScoresList.scores.get(i));
            label[i].setBounds(50,50 + 20*i,300,30);
            label[i].setFont(new Font("Impact", Font.PLAIN, 20));
            label[i].setForeground(Color.white);
            add(label[i]);
        }
        label[BestScoresList.scores.size()] = new JLabel("   ");
        add(label[BestScoresList.scores.size()]);

        setVisible(true);
    }

    /**
     * Obsługa zdarzeń
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == backButton) {

            dispose();

        }
    }


}
