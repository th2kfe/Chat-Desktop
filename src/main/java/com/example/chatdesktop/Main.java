package com.example.chatdesktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Main extends Application {

    // Coloque uma NOVA chave da Groq aqui.
    private static final String GROQ_API_KEY =
            "\n".trim();

    private static final String GROQ_MODEL =
            "openai/gpt-oss-20b";

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final HttpClient httpClient =
            HttpClient.newHttpClient();

    private final TextArea chatArea =
            new TextArea();

    private final TextField campoMensagem =
            new TextField();

    private final Button botaoEnviar =
            new Button("Enviar");

    private final Label status =
            new Label();

    @Override
    public void start(Stage stage) {

        Label titulo = new Label("Groq Chat");

        titulo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        chatArea.setEditable(false);
        chatArea.setWrapText(true);

        chatArea.setStyle(
                "-fx-font-size: 15px;"
        );

        chatArea.setText(
                "IA: Olá! Eu sou seu assistente. " +
                        "Digite uma mensagem para começar.\n\n"
        );

        campoMensagem.setPromptText(
                "Digite sua mensagem..."
        );

        campoMensagem.setPrefHeight(42);

        botaoEnviar.setPrefHeight(42);
        botaoEnviar.setPrefWidth(90);

        status.setText("");

        status.setStyle(
                "-fx-text-fill: #666666;"
        );

        HBox entrada = new HBox(
                10,
                campoMensagem,
                botaoEnviar
        );

        entrada.setAlignment(Pos.CENTER);

        HBox.setHgrow(
                campoMensagem,
                Priority.ALWAYS
        );

        VBox rodape = new VBox(
                5,
                status,
                entrada
        );

        VBox conteudo = new VBox(
                15,
                titulo,
                chatArea,
                rodape
        );

        conteudo.setPadding(
                new Insets(20)
        );

        VBox.setVgrow(
                chatArea,
                Priority.ALWAYS
        );

        BorderPane root = new BorderPane();

        root.setCenter(conteudo);

        botaoEnviar.setOnAction(
                event -> enviarMensagem()
        );

        campoMensagem.setOnAction(
                event -> enviarMensagem()
        );

        Scene scene = new Scene(
                root,
                750,
                550
        );

        stage.setTitle("Groq Chat - JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    private void enviarMensagem() {

        String mensagem =
                campoMensagem.getText().trim();

        if (mensagem.isEmpty()) {
            return;
        }

        if (GROQ_API_KEY.isBlank()
                || GROQ_API_KEY.equals(
                "COLE_SUA_NOVA_CHAVE_AQUI"
        )) {

            chatArea.appendText(
                    "ERRO: Configure sua chave da Groq.\n\n"
            );

            return;
        }

        chatArea.appendText(
                "Você: " + mensagem + "\n\n"
        );

        campoMensagem.clear();

        botaoEnviar.setDisable(true);
        campoMensagem.setDisable(true);

        status.setText(
                "A IA está pensando..."
        );

        Thread thread = new Thread(() -> {

            try {

                String resposta =
                        chamarGroq(mensagem);

                Platform.runLater(() -> {

                    chatArea.appendText(
                            "IA: " + resposta + "\n\n"
                    );

                    status.setText("");

                    botaoEnviar.setDisable(false);
                    campoMensagem.setDisable(false);

                    campoMensagem.requestFocus();
                });

            } catch (Exception erro) {

                Platform.runLater(() -> {

                    chatArea.appendText(
                            "ERRO: "
                                    + obterMensagemErro(erro)
                                    + "\n\n"
                    );

                    status.setText("");

                    botaoEnviar.setDisable(false);
                    campoMensagem.setDisable(false);

                    campoMensagem.requestFocus();
                });
            }

        });

        thread.setDaemon(true);
        thread.start();
    }

    private String chamarGroq(
            String mensagem
    ) throws Exception {

        String json =
                """
                {
                    "model": "%s",
                    "messages": [
                        {
                            "role": "system",
                            "content": "Você é um assistente útil, educado e objetivo. Responda sempre em português do Brasil."
                        },
                        {
                            "role": "user",
                            "content": "%s"
                        }
                    ],
                    "temperature": 0.7,
                    "max_completion_tokens": 1024
                }
                """.formatted(
                        GROQ_MODEL,
                        escaparJson(mensagem)
                );

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(GROQ_URL))
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .header(
                                "Authorization",
                                "Bearer " + GROQ_API_KEY.trim()
                        )
                        .POST(
                                HttpRequest.BodyPublishers.ofString(
                                        json,
                                        StandardCharsets.UTF_8
                                )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString(
                                StandardCharsets.UTF_8
                        )
                );

        if (response.statusCode() < 200
                || response.statusCode() >= 300) {

            throw new RuntimeException(
                    "Erro da API: "
                            + response.statusCode()
                            + "\n"
                            + response.body()
            );
        }

        return extrairConteudo(response.body());
    }

    private String extrairConteudo(String json) {

        String chave = "\"content\":\"";

        int inicio = json.indexOf(chave);

        if (inicio == -1) {
            return "Não foi possível encontrar a resposta da IA.";
        }

        inicio += chave.length();

        StringBuilder resultado =
                new StringBuilder();

        boolean escapado = false;

        for (int i = inicio; i < json.length(); i++) {

            char caractere = json.charAt(i);

            if (escapado) {

                switch (caractere) {

                    case 'n':
                        resultado.append('\n');
                        break;

                    case 'r':
                        resultado.append('\r');
                        break;

                    case 't':
                        resultado.append('\t');
                        break;

                    case '"':
                        resultado.append('"');
                        break;

                    case '\\':
                        resultado.append('\\');
                        break;

                    default:
                        resultado.append(caractere);
                        break;
                }

                escapado = false;

            } else if (caractere == '\\') {

                escapado = true;

            } else if (caractere == '"') {

                break;

            } else {

                resultado.append(caractere);
            }
        }

        return resultado.toString().trim();
    }

    private String escaparJson(String texto) {

        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private String obterMensagemErro(Exception erro) {

        String mensagem = erro.getMessage();

        if (mensagem == null || mensagem.isBlank()) {
            return erro.getClass().getSimpleName();
        }

        return mensagem;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
