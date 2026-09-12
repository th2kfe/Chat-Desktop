package com.example.chatdesktop;

import com.example.chatdesktop.controller.AuthController;
import com.example.chatdesktop.controller.ChatController;
import com.example.chatdesktop.view.AuthView;
import com.example.chatdesktop.view.ChatView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        AuthView authView = new AuthView();

        ChatView chatView = new ChatView();

        new ChatController(chatView);

        Scene scene = new Scene(
                authView.getRoot(),
                1000,
                650
        );

        new AuthController(
                authView,
                () -> {

                    System.out.println("AUTENTICOU - tentando trocar para o chat...");

                    try {

                        scene.setRoot(chatView.getRoot());

                        System.out.println("TROCA DE TELA OK!");

                    } catch (Exception erro) {

                        System.out.println("ERRO AO TROCAR DE TELA: " + erro);

                        erro.printStackTrace();
                    }
                }
        );

        stage.setTitle("Groq Chat - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}