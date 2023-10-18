import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
    private PrintWriter out;
    private TextArea messageArea;
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) throws IOException{
        ChatController._primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(scene);
        primaryStage.show();
        ChatController cc = new ChatController();
        cc.listenMessages();
    }

    public void stop() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}