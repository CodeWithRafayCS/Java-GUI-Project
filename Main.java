import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import gui.LoginFrame;
import utils.DataPersistence;

public class Main {
    
    public static void main(String[] args) {
        // Set System Look and Feel
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        // Initialize data directory on startup
        DataPersistence.initializeDataDirectory();
        
        // Launch the application on Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            }
        }
    );
  }
}


