package com.example.chatdesktop;

import com.example.chatdesktop.controller.AuthController;
import com.example.chatdesktop.controller.ChatController;
import com.example.chatdesktop.view.AuthView;
import com.example.chatdesktop.view.ChatView;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private static final double LARGURA_BARRA = 260;

    @Override
    public void start(Stage stage) {

        AuthView authView = new AuthView();

        ChatView chatView = new ChatView();

        new ChatController(chatView);

        new AuthController(
                authView,
                () -> {
                    Scene scene = stage.getScene();
                    scene.setRoot(chatView.getRoot());
                }
        );

        // ============================================================
        // TELA DE CARREGAMENTO (SPLASH)
        // ============================================================

        StackPane splashRoot = criarSplash();

        Scene scene = new Scene(splashRoot, 1000, 650);

        stage.setTitle("FAITH IN GOD");

        stage.setScene(scene);

        stage.show();

        animarSplashParaLogin(splashRoot, scene, authView.getRoot());
    }

    // ================================================================
    // CONSTRUÇÃO DA TELA DE CARREGAMENTO
    // ================================================================

    private StackPane criarSplash() {

        StackPane splash = new StackPane();

        splash.setStyle("-fx-background-color: #051F20;");

        Label titulo = new Label("FAITH IN GOD");

        titulo.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #DAF1DE;"
        );

        Label subtitulo = new Label("Carregando...");

        subtitulo.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #8EB69B;"
        );

        // Trilha (fundo) da barra de progresso
        Region trilhaBarra = new Region();

        trilhaBarra.setPrefSize(LARGURA_BARRA, 4);

        trilhaBarra.setMaxSize(LARGURA_BARRA, 4);

        trilhaBarra.setStyle(
                "-fx-background-color: #163832;" +
                        "-fx-background-radius: 999;"
        );

        // Preenchimento animado da barra
        Region fillBarra = new Region();

        fillBarra.setPrefSize(0, 4);

        fillBarra.setMaxSize(0, 4);

        fillBarra.setStyle(
                "-fx-background-color: #DAF1DE;" +
                        "-fx-background-radius: 999;"
        );

        StackPane.setAlignment(fillBarra, Pos.CENTER_LEFT);

        StackPane barraContainer = new StackPane(trilhaBarra, fillBarra);

        barraContainer.setMaxSize(LARGURA_BARRA, 4);

        VBox conteudo = new VBox(16, titulo, subtitulo, barraContainer);

        conteudo.setAlignment(Pos.CENTER);

        conteudo.setOpacity(0);

        splash.getChildren().add(conteudo);

        // Fade-in inicial do conteúdo do splash
        FadeTransition entradaConteudo = new FadeTransition(
                Duration.millis(500), conteudo
        );

        entradaConteudo.setFromValue(0);

        entradaConteudo.setToValue(1);

        // Pulso suave no título enquanto carrega
        ScaleTransition pulso = new ScaleTransition(
                Duration.millis(900), titulo
        );

        pulso.setFromX(1);

        pulso.setFromY(1);

        pulso.setToX(1.04);

        pulso.setToY(1.04);

        pulso.setCycleCount(Animation.INDEFINITE);

        pulso.setAutoReverse(true);

        pulso.setInterpolator(Interpolator.EASE_BOTH);

        // Barra de progresso preenchendo
        Timeline preenchimento = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(fillBarra.prefWidthProperty(), 0),
                        new KeyValue(fillBarra.maxWidthProperty(), 0)
                ),
                new KeyFrame(
                        Duration.millis(1800),
                        new KeyValue(fillBarra.prefWidthProperty(), LARGURA_BARRA, Interpolator.EASE_OUT),
                        new KeyValue(fillBarra.maxWidthProperty(), LARGURA_BARRA, Interpolator.EASE_OUT)
                )
        );

        entradaConteudo.setOnFinished(evento -> {
            pulso.play();
            preenchimento.play();
        });

        entradaConteudo.play();

        return splash;
    }

    // ================================================================
    // TRANSIÇÃO DO SPLASH PARA A TELA DE LOGIN
    // ================================================================

    private void animarSplashParaLogin(
            StackPane splashRoot,
            Scene scene,
            Parent telaLogin
    ) {

        PauseTransition espera = new PauseTransition(
                Duration.millis(2200)
        );

        espera.setOnFinished(evento -> {

            FadeTransition saida = new FadeTransition(
                    Duration.millis(500), splashRoot
            );

            saida.setFromValue(1);

            saida.setToValue(0);

            saida.setOnFinished(fim -> {

                telaLogin.setOpacity(0);

                scene.setRoot(telaLogin);

                FadeTransition entrada = new FadeTransition(
                        Duration.millis(500), telaLogin
                );

                entrada.setFromValue(0);

                entrada.setToValue(1);

                entrada.play();
            });

            saida.play();
        });

        espera.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}