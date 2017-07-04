package deltadak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;

/**
 * main class
 */
public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws Exception{
        System.out.println(getClass().getResource
                ("interface.fxml"));
        URL interfaceURL = new File("src/deltadak/interface.fxml")
                .toURL();
        System.out.println(interfaceURL);
        FXMLLoader loader = new FXMLLoader(interfaceURL);
        Parent root = loader.load();
        
        //used to invoke a setup method in controller which needs the stage
        Controller controller = loader.getController();
        controller.setDayChangeListener(primaryStage);
        
        primaryStage.setTitle("Plep");
        primaryStage.setScene(new Scene(root, 0, 0));
    
        //set a size relative to screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth()/2);
        primaryStage.setHeight(primaryScreenBounds.getHeight());
    
        URL listviewURL = new File("src/deltadak/listview.css").toURL();
    
        String listViewCSS = listviewURL.toExternalForm();
        primaryStage.getScene().getStylesheets().addAll(listViewCSS);
    
        // check if running in debug mode
        // to display the default java icon so we can distinguish between
        // the program we are testing and the one we are actually using
        // (the latter has the plep logo)
        boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().
                getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
        if(isDebug) {
            System.out.println("debug");
        } else {
            System.out.println("no debug");
            primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("icon.png")));
        }
    
        primaryStage.show();

    }
    
    /**
     *  main method
     * @param args args
     */
    public static void main(final String[] args) {
        launch(args);
    }
}
