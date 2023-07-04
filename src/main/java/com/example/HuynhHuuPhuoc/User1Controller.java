package com.example.HuynhHuuPhuoc;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


public class User1Controller {
    @FXML
    private TextArea txtContent;

    @FXML
    private TextField txtMessage;

    @FXML
    private Button txtSend;
    private JedisPubSub jedisPubSub;
    private String channel = "phuoc";
    private Jedis jedisP;
    private Jedis jedisS;

    @FXML
    private void initialize() {
        jedisP = new Jedis("localhost", 6379);
        jedisS = new Jedis("localhost", 6379);
        Thread consumerThread = new Thread(this::consumeMessage);
        consumerThread.setDaemon(true);
        consumerThread.start();
    }
    @FXML
    public void sendMessage() {
        jedisP.publish(channel,"User 1: "+ txtMessage.getText());
        txtMessage.setText("");
        txtMessage.requestFocus();
    }

    @FXML
    public void consumeMessage() {
        while (true){
            jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    Platform.runLater(()->
                            txtContent.appendText(message+"\n")
                            );}
            };
            jedisS.subscribe(jedisPubSub, channel);
        }

    }
}