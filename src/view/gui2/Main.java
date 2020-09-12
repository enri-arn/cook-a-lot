package view.gui2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("CookALot");
        primaryStage.setScene(new Scene(root, 1200, 800));
        primaryStage.getIcons().add(new Image("view/image/cook.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
