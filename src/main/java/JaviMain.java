import view.JaviFrame;

/**
 * Created by Anastasiia Kuzenkova on 10.03.15.
 */
public class JaviMain
{
    private static void createAndShowGUI()
    {
        JaviFrame frame = new JaviFrame();
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
