import startDialog.JaviFrame;

public class JaviMain
{
    private static void createAndShowGUI()
    {
        JaviFrame frame = new JaviFrame();
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(JaviMain::createAndShowGUI);
    }
}
