import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
    private PrintWriter out;
    private TextArea messageArea;
    private TextField inputField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        messageArea = new TextArea();
        messageArea.setEditable(false);
        root.setCenter(messageArea);

        inputField = new TextField();
        inputField.setOnAction(event -> sendMessage());
        root.setBottom(inputField);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Chat Client");
        primaryStage.show();

        try {
            Socket socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while ((message = in.readLine()) != null) {
                showMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        out.println(message);
        showMessage("You: " + message);
        inputField.clear();
    }

    private void showMessage(String message) {
        messageArea.appendText(message + "\n");
    }

    public void stop() {
    }
}