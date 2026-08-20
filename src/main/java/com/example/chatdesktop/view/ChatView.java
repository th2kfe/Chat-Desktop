package com.example.chatdesktop.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChatView {

    private final BorderPane root;

    private final TextArea areaChat;

    private final TextField campoMensagem;

    private final Button botaoEnviar;

    private final Button botaoNovaConversa;

    private final Label status;

    public ChatView() {


        root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: #f4f6f8;"
        );



        Label titulo =
                new Label(
                        "Groq Chat"
                );

        titulo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );



        botaoNovaConversa =
                new Button(
                        "＋ Nova conversa"
                );

        botaoNovaConversa.setPrefHeight(
                38
        );

        botaoNovaConversa.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-text-fill: #333333;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );



        HBox cabecalho =
                new HBox(
                        15,
                        titulo,
                        botaoNovaConversa
                );

        cabecalho.setAlignment(
                Pos.CENTER_LEFT
        );

        cabecalho.setPadding(
                new Insets(15)
        );

        cabecalho.setStyle(
                "-fx-background-color: #5b2ecc;"
        );

        HBox.setHgrow(
                titulo,
                Priority.ALWAYS
        );



        areaChat =
                new TextArea();

        areaChat.setEditable(
                false
        );

        areaChat.setWrapText(
                true
        );

        areaChat.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-control-inner-background: white;" +
                        "-fx-background-color: white;" +
                        "-fx-border-color: #dddddd;"
        );

        areaChat.setText(
                "IA: Olá! Eu sou seu assistente. "
                        + "Digite uma mensagem para começar.\n\n"
        );


        status =
                new Label();

        status.setStyle(
                "-fx-text-fill: #666666;" +
                        "-fx-font-size: 12px;"
        );



        campoMensagem =
                new TextField();

        campoMensagem.setPromptText(
                "Digite sua mensagem..."
        );

        campoMensagem.setPrefHeight(
                42
        );



        botaoEnviar =
                new Button(
                        "Enviar"
                );

        botaoEnviar.setPrefHeight(
                42
        );

        botaoEnviar.setPrefWidth(
                90
        );

        botaoEnviar.setStyle(
                "-fx-background-color: #5b2ecc;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        /*
         * ========================================================
         * ENTRADA
         * ========================================================
         */

        HBox entrada =
                new HBox(
                        10,
                        campoMensagem,
                        botaoEnviar
                );

        entrada.setAlignment(
                Pos.CENTER
        );

        HBox.setHgrow(
                campoMensagem,
                Priority.ALWAYS
        );

        /*
         * ========================================================
         * RODAPÉ
         * ========================================================
         */

        VBox rodape =
                new VBox(
                        6,
                        status,
                        entrada
                );

        rodape.setPadding(
                new Insets(
                        10,
                        0,
                        0,
                        0
                )
        );

        /*
         * ========================================================
         * CONTEÚDO
         * ========================================================
         */

        VBox conteudo =
                new VBox(
                        10,
                        areaChat,
                        rodape
                );

        conteudo.setPadding(
                new Insets(15)
        );

        VBox.setVgrow(
                areaChat,
                Priority.ALWAYS
        );

        /*
         * ========================================================
         * ROOT
         * ========================================================
         */

        root.setTop(
                cabecalho
        );

        root.setCenter(
                conteudo
        );
    }

    /*
     * ============================================================
     * GETTERS
     * ============================================================
     */

    public BorderPane getRoot() {

        return root;
    }

    public TextArea getAreaChat() {

        return areaChat;
    }

    public TextField getCampoMensagem() {

        return campoMensagem;
    }

    public Button getBotaoEnviar() {

        return botaoEnviar;
    }

    public Button getBotaoNovaConversa() {

        return botaoNovaConversa;
    }

    /*
     * ============================================================
     * MENSAGEM DO USUÁRIO
     * ============================================================
     */

    public void adicionarMensagemUsuario(
            String mensagem
    ) {

        areaChat.appendText(
                "Você:\n"
                        + mensagem
                        + "\n\n"
        );

        rolarParaBaixo();
    }

    /*
     * ============================================================
     * MENSAGEM DA IA
     * ============================================================
     */

    public void adicionarMensagemIA(
            String mensagem
    ) {

        areaChat.appendText(
                "IA:\n"
                        + mensagem
                        + "\n\n"
        );

        rolarParaBaixo();
    }

    /*
     * ============================================================
     * ERRO
     * ============================================================
     */

    public void adicionarErro(
            String mensagem
    ) {

        areaChat.appendText(
                "Erro:\n"
                        + mensagem
                        + "\n\n"
        );

        rolarParaBaixo();
    }

    /*
     * ============================================================
     * NOVA CONVERSA
     * ============================================================
     */

    public void limparConversa() {

        areaChat.clear();
    }

    /*
     * ============================================================
     * CARREGAMENTO
     * ============================================================
     */

    public void setCarregando(
            boolean carregando
    ) {

        botaoEnviar.setDisable(
                carregando
        );

        campoMensagem.setDisable(
                carregando
        );

        botaoNovaConversa.setDisable(
                false
        );

        if (carregando) {

            status.setText(
                    "A IA está pensando..."
            );

        } else {

            status.setText("");
        }
    }

    /*
     * ============================================================
     * SCROLL
     * ============================================================
     */

    private void rolarParaBaixo() {

        areaChat.positionCaret(
                areaChat.getLength()
        );
    }
}