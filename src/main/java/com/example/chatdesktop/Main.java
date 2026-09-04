package com.example.chatdesktop;

import com.example.chatdesktop.controller.ChatController;
import com.example.chatdesktop.view.ChatView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        ChatView view = new ChatView();

        new ChatController(view);

        Scene scene = new Scene(
                view.getRoot(),
                1000,
                650
        );

        stage.setTitle("Groq Chat - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}