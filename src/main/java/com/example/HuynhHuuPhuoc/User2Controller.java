package com.example.HuynhHuuPhuoc;

import com.example.HuynhHuuPhuoc.Model.Message;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.net.URL;
import java.util.ResourceBundle;


public class User2Controller implements Initializable{
    @FXML
    private TextArea txtContent;

    @FXML
    private TextField txtMessage;
    @FXML
    private TableView<Message> tbvShowMessage;
    @FXML
    private TableColumn<Message, String> idCollumn;

    @FXML
    private TableColumn<Message, String> messageCollumn;
    @FXML
    private Button btnShow;

    @FXML
    private Button txtSend;
    private JedisPubSub jedisPubSub;
    private String channel = "phuoc";
    private Jedis jedisP;
    private Jedis jedisS;
    private static ObservableList<Message> messages;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        jedisP = new Jedis("localhost", 6379);
        jedisS = new Jedis("localhost", 6379);
        Thread consumerThread = new Thread(this::consumeMessage);
        consumerThread.setDaemon(true);
        consumerThread.start();


    }
    public void sendMessage() {
        jedisP.publish(channel, "User 2: "+txtMessage.getText());
        txtMessage.setText("");
        txtMessage.requestFocus();

    }
    @FXML
    public void consumeMessage() {
        while(true){
            jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    Platform.runLater(()->
                    txtContent.appendText(message+"\n")
                            );
                }
            };
            jedisS.subscribe(jedisPubSub, channel);
        }
    }

}