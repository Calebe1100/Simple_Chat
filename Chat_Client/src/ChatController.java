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
    public static boolean conected = false;

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

    @FXML
    void conect(ActionEvent event){
        this.conected = true;
        Runnable runnable = new MyRunnable();
        Thread thread = new Thread(runnable);
        thread.start();
        
    }
    

    private void sendMessage(){

        try(Socket socket = new Socket(ipServidor.getText(), Integer.parseInt(porta.getText()))){

            PrintWriter outBuffer = new PrintWriter(socket.getOutputStream(), true);
            String messageToSend = mensagemEnviar.getText()+";"+nomeUsuario.getText();
            outBuffer.println(messageToSend);
            outBuffer.close();
            socket.close();
            mensagemEnviar.setText("");
            if("bye".equalsIgnoreCase(mensagemEnviar.getText())){
                this.conected = false;
            }

        }catch(Exception e){

        }
    }

    public void listenMessages(){
       
        BufferedReader inBuffer;
        Socket clientCall;

        try{
            ServerSocket tomadaServidor = new ServerSocket(Integer.parseInt(porta.getText()));
            clientCall = tomadaServidor.accept();

            inBuffer = new BufferedReader(new InputStreamReader(clientCall.getInputStream()));
            String messageReceived = "";
            messageReceived = inBuffer.readLine();
            String[] receiverList = formatReceiverMessage(messageReceived);
            String messageToAdd = receiverList[1]+": "+receiverList[0];
            Text messageTextType = new Text(messageToAdd);
            caixaMensagens.getChildren().add(messageTextType);

        }catch(Exception e){

        }

    }
    
    public String[] formatReceiverMessage(String receiverMessage) {
        String[] receiverList = receiverMessage.split(";");
        return receiverList;
    }

    public class MyRunnable implements Runnable{

        @Override
        public void run() {
            listenMessages();
        }
        
    }
}
