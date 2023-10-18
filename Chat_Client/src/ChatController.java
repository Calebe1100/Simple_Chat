import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ChatController {

    public static Stage _primaryStage;

    @FXML
    private Button botaoEnviar;

    @FXML
    private TextFlow caixaMensagens;

    @FXML
    private TextField ipServidor;

    @FXML
    private TextField mensagemEnviar;

    @FXML
    private TextField nomeUsuario;

    @FXML
    private TextField porta;

    @FXML
    void onSend(ActionEvent event){
        sendMessage();
    }

    private void sendMessage(){

        try(Socket socket = new Socket(ipServidor.getText(), 5100)){

            PrintWriter outBuffer = new PrintWriter(socket.getOutputStream(), true);
            String messageToSend = mensagemEnviar.getText();
            outBuffer.println(messageToSend);

            outBuffer.close();
            socket.close();

        }catch(Exception e){

        }
    }

    public void listenMessages(){
        Socket clientCall;
        BufferedReader inBuffer;

        try(ServerSocket serverPort = new ServerSocket(5100)){
            System.out.println("Escutando mensagens");
            while(true){
                clientCall = serverPort.accept();
                inBuffer = new BufferedReader(new InputStreamReader(clientCall.getInputStream()));
                String messageReceived = inBuffer.readLine();
                Text textToAdd = new Text(messageReceived);
                caixaMensagens.getChildren().add(textToAdd);
                if("bye".equalsIgnoreCase(messageReceived)){
                    break;
                }
            }

            inBuffer.close();
            clientCall.close();
            serverPort.close();

        }catch(Exception e){

        }
    }

}
