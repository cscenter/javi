import startDialog.JaviFrame;

import view.JaviBlockSchemePictureTest;

public class JaviMain {
    private static void createAndShowGUI() {
        JaviFrame frame = new JaviFrame();
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
    	System.out.println("params:" + args.length);
        if (args.length == 1 && args[0].startsWith("-")) {
            String arg = args[0];
            if (arg.equals("-png")) {
                JaviBlockSchemePictureTest.saveAll();
            }
        }
        else if (args.length == 2) {
            JaviBlockSchemePictureTest.saveToPng(args);
        }
     	else {	
        	javax.swing.SwingUtilities.invokeLater(JaviMain::createAndShowGUI);
        }
    }
}