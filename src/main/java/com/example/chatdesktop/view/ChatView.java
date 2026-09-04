package com.example.chatdesktop.view;

import com.example.chatdesktop.model.Conversation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;

import java.util.Objects;
import java.util.function.Consumer;

public class ChatView {

    private final BorderPane root;

    private final TextArea areaChat;

    private final TextField campoMensagem;

    private final Button botaoEnviar;

    private final Button botaoNovaConversa;

    private final Button botaoCopiar;

    private final Button botaoRegenerar;

    private final Button botaoRenomear;

    private final Button botaoExcluir;

    private final Button botaoTema;

    private final Label status;

    private final Label origemResposta;

    private final Label fonteResposta;

    private final ListView<Conversation> listaConversas;

    private String ultimaRespostaIA = "";

    private boolean temaEscuro = false;

    private Consumer<Conversation> aoSelecionarConversa;

    private Runnable aoNovaConversa;

    private Runnable aoRegenerar;

    private Runnable aoRenomear;

    private Runnable aoExcluir;

    public ChatView() {

        root = new BorderPane();

        root.getStyleClass().add("app-root");

        root.getStylesheets().add(
                Objects.requireNonNull(
                        getClass().getResource(
                                "/com/example/chatdesktop/css/chat.css"
                        )
                ).toExternalForm()
        );

        Label tituloHistorico = new Label("Conversas");

        tituloHistorico.getStyleClass().add("sidebar-title");

        tituloHistorico.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        botaoNovaConversa = new Button("＋ Nova conversa");

        botaoNovaConversa.getStyleClass().add("btn-light");

        botaoNovaConversa.setMaxWidth(Double.MAX_VALUE);

        botaoNovaConversa.setPrefHeight(40);

        botaoNovaConversa.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        listaConversas = new ListView<>();

        listaConversas.getStyleClass().add("conversation-list");

        listaConversas.setCellFactory(list -> new ConversationCell());

        listaConversas.setStyle("-fx-background-color: transparent;");

        listaConversas.setOnMouseClicked(evento -> {

            Conversation conversa =
                    listaConversas.getSelectionModel().getSelectedItem();

            if (conversa != null && aoSelecionarConversa != null) {
                aoSelecionarConversa.accept(conversa);
            }
        });

        VBox barraLateral = new VBox(
                15, tituloHistorico, botaoNovaConversa, listaConversas
        );

        barraLateral.getStyleClass().add("sidebar");

        barraLateral.setPadding(new Insets(15));

        barraLateral.setPrefWidth(230);

        VBox.setVgrow(listaConversas, Priority.ALWAYS);

        Label titulo = new Label("Groq Chat");

        titulo.getStyleClass().add("header-title");

        titulo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        botaoTema = new Button("🌙 Escuro");

        botaoTema.getStyleClass().add("btn-light");

        botaoTema.setStyle("-fx-background-radius: 8;");

        botaoTema.setOnAction(evento -> alternarTema());

        botaoRenomear = new Button("✏ Renomear");

        botaoRenomear.getStyleClass().add("btn-light");

        botaoRenomear.setStyle("-fx-background-radius: 8;");

        botaoExcluir = new Button("🗑 Excluir");

        botaoExcluir.getStyleClass().add("btn-danger");

        botaoExcluir.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        HBox cabecalho = new HBox(
                10, titulo, botaoTema, botaoRenomear, botaoExcluir
        );

        cabecalho.getStyleClass().add("header");

        cabecalho.setAlignment(Pos.CENTER_LEFT);

        cabecalho.setPadding(new Insets(15));

        HBox.setHgrow(titulo, Priority.ALWAYS);

        areaChat = new TextArea();

        areaChat.getStyleClass().add("chat-area");

        areaChat.setEditable(false);

        areaChat.setWrapText(true);

        areaChat.setStyle("-fx-font-size: 15px;");

        areaChat.setText(
                "IA: Olá! Eu sou seu assistente. " +
                        "Digite uma mensagem para começar.\n\n"
        );

        origemResposta = new Label("Origem: -");

        origemResposta.getStyleClass().add("origem-label");

        origemResposta.setStyle(
                "-fx-font-size: 12px;" +
                        "-fx-font-weight: bold;"
        );

        fonteResposta = new Label("Fonte: -");

        fonteResposta.getStyleClass().add("fonte-label");

        fonteResposta.setStyle("-fx-font-size: 12px;");

        HBox informacoesResposta = new HBox(15, origemResposta, fonteResposta);

        status = new Label();

        status.getStyleClass().add("status-label");

        status.setStyle("-fx-font-size: 12px;");

        campoMensagem = new TextField();

        campoMensagem.setPromptText("Digite sua mensagem...");

        campoMensagem.setPrefHeight(42);

        botaoCopiar = new Button("📋 Copiar");

        botaoCopiar.getStyleClass().add("btn-secondary");

        botaoCopiar.setPrefHeight(42);

        botaoCopiar.setPrefWidth(100);

        botaoCopiar.setDisable(true);

        botaoCopiar.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        botaoCopiar.setOnAction(evento -> copiarRespostaIA());

        botaoRegenerar = new Button("🔄 Regenerar");

        botaoRegenerar.getStyleClass().add("btn-secondary");

        botaoRegenerar.setPrefHeight(42);

        botaoRegenerar.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        botaoRegenerar.setDisable(true);

        botaoRegenerar.setOnAction(evento -> {
            if (aoRegenerar != null) {
                aoRegenerar.run();
            }
        });

        botaoEnviar = new Button("Enviar");

        botaoEnviar.getStyleClass().add("btn-primary");

        botaoEnviar.setPrefHeight(42);

        botaoEnviar.setPrefWidth(90);

        botaoEnviar.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        HBox entrada = new HBox(
                10, campoMensagem, botaoRegenerar, botaoCopiar, botaoEnviar
        );

        entrada.setAlignment(Pos.CENTER);

        HBox.setHgrow(campoMensagem, Priority.ALWAYS);

        VBox rodape = new VBox(6, informacoesResposta, status, entrada);

        rodape.setPadding(new Insets(10, 0, 0, 0));

        VBox conteudo = new VBox(10, areaChat, rodape);

        conteudo.setPadding(new Insets(15));

        VBox.setVgrow(areaChat, Priority.ALWAYS);

        root.setLeft(barraLateral);

        root.setTop(cabecalho);

        root.setCenter(conteudo);

        botaoNovaConversa.setOnAction(evento -> {
            if (aoNovaConversa != null) {
                aoNovaConversa.run();
            }
        });

        botaoRenomear.setOnAction(evento -> {
            if (aoRenomear != null) {
                aoRenomear.run();
            }
        });

        botaoExcluir.setOnAction(evento -> {
            if (aoExcluir != null) {
                aoExcluir.run();
            }
        });
    }

    private void alternarTema() {

        temaEscuro = !temaEscuro;

        if (temaEscuro) {

            root.getStyleClass().add("dark");

            botaoTema.setText("☀ Claro");

        } else {

            root.getStyleClass().remove("dark");

            botaoTema.setText("🌙 Escuro");
        }
    }

    public boolean isTemaEscuro() {
        return temaEscuro;
    }

    public void setAoSelecionarConversa(Consumer<Conversation> callback) {
        aoSelecionarConversa = callback;
    }

    public void setAoNovaConversa(Runnable callback) {
        aoNovaConversa = callback;
    }

    public void setAoRegenerar(Runnable callback) {
        aoRegenerar = callback;
    }

    public void setAoRenomear(Runnable callback) {
        aoRenomear = callback;
    }

    public void setAoExcluir(Runnable callback) {
        aoExcluir = callback;
    }

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

    public Button getBotaoCopiar() {
        return botaoCopiar;
    }

    public Button getBotaoRegenerar() {
        return botaoRegenerar;
    }

    public ListView<Conversation> getListaConversas() {
        return listaConversas;
    }

    public void adicionarMensagemUsuario(String mensagem) {

        areaChat.appendText("Você:\n" + mensagem + "\n\n");

        rolarParaBaixo();
    }

    public void adicionarMensagemIA(String mensagem) {

        adicionarMensagemIA(mensagem, "Fallback local", null);
    }

    public void adicionarMensagemIA(String mensagem, String origem, String fonte) {

        ultimaRespostaIA = mensagem;

        botaoCopiar.setDisable(false);

        botaoRegenerar.setDisable(false);

        areaChat.appendText("IA:\n" + mensagem + "\n\n");

        origemResposta.setText("Origem: " + origem);

        if (fonte == null || fonte.isBlank()) {
            fonteResposta.setText("Fonte: -");
        } else {
            fonteResposta.setText("Fonte: " + fonte);
        }

        rolarParaBaixo();
    }

    private void copiarRespostaIA() {

        if (ultimaRespostaIA == null || ultimaRespostaIA.isBlank()) {
            return;
        }

        Clipboard clipboard = Clipboard.getSystemClipboard();

        ClipboardContent content = new ClipboardContent();

        content.putString(ultimaRespostaIA);

        clipboard.setContent(content);

        status.setText("Resposta copiada!");
    }

    public void carregarConversa(Conversation conversa) {

        areaChat.clear();

        ultimaRespostaIA = "";

        botaoCopiar.setDisable(true);

        botaoRegenerar.setDisable(true);

        origemResposta.setText("Origem: -");

        fonteResposta.setText("Fonte: -");

        for (var mensagem : conversa.getMensagens()) {

            if (mensagem.getRole().equals("user")) {

                areaChat.appendText("Você:\n" + mensagem.getContent() + "\n\n");

            } else if (mensagem.getRole().equals("assistant")) {

                areaChat.appendText("IA:\n" + mensagem.getContent() + "\n\n");

                ultimaRespostaIA = mensagem.getContent();

                botaoCopiar.setDisable(false);

                botaoRegenerar.setDisable(false);
            }
        }

        rolarParaBaixo();
    }

    public void limparConversa() {

        areaChat.clear();

        ultimaRespostaIA = "";

        botaoCopiar.setDisable(true);

        botaoRegenerar.setDisable(true);

        origemResposta.setText("Origem: -");

        fonteResposta.setText("Fonte: -");

        status.setText("");
    }

    public boolean confirmarExclusao(String titulo) {

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

        alerta.setTitle("Excluir conversa");

        alerta.setHeaderText("Excluir esta conversa?");

        alerta.setContentText(
                "A conversa \"" + titulo + "\" será removida do histórico."
        );

        return alerta.showAndWait()
                .filter(resposta -> resposta == ButtonType.OK)
                .isPresent();
    }

    private void rolarParaBaixo() {

        areaChat.positionCaret(areaChat.getLength());
    }

    private class ConversationCell extends ListCell<Conversation> {

        private final Label titulo;

        private final Button excluir;

        private final HBox caixa;

        public ConversationCell() {

            titulo = new Label();

            titulo.setMaxWidth(Double.MAX_VALUE);

            titulo.setStyle("-fx-text-fill: white;");

            excluir = new Button("×");

            excluir.setStyle(
                    "-fx-background-color: transparent;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 16px;"
            );

            excluir.setOnAction(evento -> {

                Conversation conversa = getItem();

                if (conversa != null && aoExcluir != null) {

                    getListView().getSelectionModel().select(conversa);

                    aoExcluir.run();
                }
            });

            caixa = new HBox(5, titulo, excluir);

            caixa.setAlignment(Pos.CENTER_LEFT);

            HBox.setHgrow(titulo, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(Conversation item, boolean empty) {

            super.updateItem(item, empty);

            if (empty || item == null) {

                setGraphic(null);

            } else {

                titulo.setText(item.getTitulo());

                setGraphic(caixa);
            }
        }
    }

    public void setCarregando(boolean carregando) {

        botaoEnviar.setDisable(carregando);

        campoMensagem.setDisable(carregando);

        botaoNovaConversa.setDisable(false);

        botaoCopiar.setDisable(
                carregando || ultimaRespostaIA == null || ultimaRespostaIA.isBlank()
        );

        botaoRegenerar.setDisable(
                carregando || ultimaRespostaIA == null || ultimaRespostaIA.isBlank()
        );

        if (carregando) {
            status.setText("A IA está pensando...");
        } else {
            status.setText("");
        }
    }

    public void adicionarErro(String mensagem) {

        areaChat.appendText("Erro:\n" + mensagem + "\n\n");

        status.setText("Ocorreu um erro.");

        rolarParaBaixo();
    }
}