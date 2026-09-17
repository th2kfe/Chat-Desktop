package com.example.chatdesktop.view;

import com.example.chatdesktop.model.Conversation;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ChatView {

    private final BorderPane root;

    // ---- SIDEBAR ----
    private final TextField campoBusca;

    private final ListView<Conversation> listaConversas;

    private List<Conversation> ultimaListaCompleta = new ArrayList<>();

    private boolean aplicandoFiltro = false;

    private final Button botaoNovaConversa;

    // ---- HEADER ----
    private final Button botaoTema;

    private final Button botaoRenomear;

    private final Button botaoExcluir;

    private boolean temaEscuro = false;

    // ---- ÁREA DE MENSAGENS ----
    private final VBox mensagensBox;

    private final ScrollPane scrollMensagens;

    private final VBox estadoVazio;

    // ---- CAPTION / STATUS ----
    private final Label origemResposta;

    private final Label fonteResposta;

    private final Label status;

    // ---- BARRA DE ENTRADA ----
    private final TextField campoMensagem;

    private final Button botaoCopiar;

    private final Button botaoRegenerar;

    private final Button botaoEnviar;

    // ---- COMPATIBILIDADE DE API (não exibido na tela) ----
    private final TextArea areaChatOculta = new TextArea();

    private String ultimaRespostaIA = "";

    private Consumer<Conversation> aoSelecionarConversa;

    private Runnable aoNovaConversa;

    private Runnable aoRegenerar;

    private Runnable aoRenomear;

    private Runnable aoExcluir;

    public ChatView() {

        root = new BorderPane();

        root.getStyleClass().add("app-root");

        root.getStylesheets().add(
                getClass().getResource(
                        "/com/example/chatdesktop/css/chat.css"
                ).toExternalForm()
        );

        // ============================================================
        // SIDEBAR
        // ============================================================

        Label marca = new Label("FAITH IN GOD");

        marca.getStyleClass().add("brand-label");

        marca.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;"
        );

        campoBusca = new TextField();

        campoBusca.setPromptText("Buscar conversas...");

        campoBusca.getStyleClass().add("search-input");

        campoBusca.textProperty().addListener(
                (obs, valorAntigo, valorNovo) -> aplicarFiltroBusca(valorNovo)
        );

        botaoNovaConversa = new Button("+ Nova conversa");

        botaoNovaConversa.getStyleClass().add("btn-primary");

        botaoNovaConversa.setMaxWidth(Double.MAX_VALUE);

        botaoNovaConversa.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;"
        );

        listaConversas = new ListView<>();

        listaConversas.getStyleClass().add("conversation-list");

        listaConversas.setCellFactory(
                lista -> new ConversationCell()
        );

        listaConversas.setStyle(
                "-fx-background-color: transparent;"
        );

        listaConversas.getItems().addListener(
                (ListChangeListener<Conversation>) mudanca -> {

                    if (!aplicandoFiltro) {

                        ultimaListaCompleta =
                                new ArrayList<>(
                                        listaConversas.getItems()
                                );
                    }
                }
        );

        listaConversas.setOnMouseClicked(
                evento -> {

                    Conversation conversa =
                            listaConversas
                                    .getSelectionModel()
                                    .getSelectedItem();

                    if (conversa != null && aoSelecionarConversa != null) {

                        aoSelecionarConversa.accept(conversa);
                    }
                }
        );

        VBox.setVgrow(listaConversas, Priority.ALWAYS);

        VBox sidebar = new VBox(
                14, marca, campoBusca, botaoNovaConversa, listaConversas
        );

        sidebar.getStyleClass().add("sidebar");

        sidebar.setPadding(new Insets(18));

        sidebar.setPrefWidth(240);

        root.setLeft(sidebar);

        // ============================================================
        // HEADER (barra superior minimalista)
        // ============================================================

        botaoTema = new Button("Escuro");

        botaoTema.getStyleClass().add("btn-light");

        botaoTema.setStyle("-fx-background-radius: 8;");

        botaoTema.setOnAction(evento -> alternarTema());

        botaoRenomear = new Button("Renomear");

        botaoRenomear.getStyleClass().add("btn-light");

        botaoRenomear.setStyle("-fx-background-radius: 8;");

        botaoExcluir = new Button("Excluir");

        botaoExcluir.getStyleClass().add("btn-danger");

        botaoExcluir.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;"
        );

        Region espacoHeader = new Region();

        HBox.setHgrow(espacoHeader, Priority.ALWAYS);

        HBox header = new HBox(
                10, espacoHeader, botaoTema, botaoRenomear, botaoExcluir
        );

        header.getStyleClass().add("header-flat");

        header.setAlignment(Pos.CENTER_RIGHT);

        header.setPadding(new Insets(16, 20, 0, 20));

        root.setTop(header);

        // ============================================================
        // ÁREA DE MENSAGENS (bolhas)
        // ============================================================

        mensagensBox = new VBox(14);

        mensagensBox.setPadding(new Insets(10, 30, 10, 30));

        mensagensBox.getChildren().addListener(
                (ListChangeListener<Node>) mudanca -> atualizarEstadoVazio()
        );

        scrollMensagens = new ScrollPane(mensagensBox);

        scrollMensagens.setFitToWidth(true);

        scrollMensagens.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        // ============================================================
        // CAPTION DE ORIGEM / STATUS
        // ============================================================

        origemResposta = new Label("");

        origemResposta.getStyleClass().add("origem-label");

        origemResposta.setStyle(
                "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;"
        );

        fonteResposta = new Label("");

        fonteResposta.getStyleClass().add("fonte-label");

        fonteResposta.setStyle("-fx-font-size: 11px;");

        status = new Label();

        status.getStyleClass().add("status-label");

        status.setStyle("-fx-font-size: 11px;");

        HBox linhaCaption = new HBox(
                14, origemResposta, fonteResposta, status
        );

        linhaCaption.setPadding(new Insets(0, 30, 4, 30));

        // ============================================================
        // BARRA DE ENTRADA
        // ============================================================

        campoMensagem = new TextField();

        campoMensagem.setPromptText("Digite sua mensagem...");

        campoMensagem.getStyleClass().add("text-input");

        campoMensagem.setPrefHeight(46);

        HBox.setHgrow(campoMensagem, Priority.ALWAYS);

        botaoCopiar = new Button("Copiar");

        botaoCopiar.getStyleClass().add("btn-secondary");

        botaoCopiar.setPrefHeight(46);

        botaoCopiar.setDisable(true);

        botaoCopiar.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;"
        );

        botaoCopiar.setOnAction(evento -> copiarRespostaIA());

        botaoRegenerar = new Button("Regenerar");

        botaoRegenerar.getStyleClass().add("btn-secondary");

        botaoRegenerar.setPrefHeight(46);

        botaoRegenerar.setDisable(true);

        botaoRegenerar.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;"
        );

        botaoRegenerar.setOnAction(
                evento -> {

                    if (aoRegenerar != null) {

                        aoRegenerar.run();
                    }
                }
        );

        botaoEnviar = new Button("Enviar");

        botaoEnviar.getStyleClass().add("btn-primary");

        botaoEnviar.setPrefHeight(46);

        botaoEnviar.setPrefWidth(100);

        botaoEnviar.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-background-radius: 10;"
        );

        HBox barraEntrada = new HBox(
                10, campoMensagem, botaoRegenerar, botaoCopiar, botaoEnviar
        );

        barraEntrada.setAlignment(Pos.CENTER);

        barraEntrada.getStyleClass().add("input-bar");

        barraEntrada.setPadding(new Insets(14, 20, 20, 20));

        VBox rodape = new VBox(6, linhaCaption, barraEntrada);

        // ============================================================
        // ESTADO VAZIO (tela de boas-vindas)
        // ============================================================

        estadoVazio = criarEstadoVazio();

        StackPane areaConteudo = new StackPane(scrollMensagens, estadoVazio);

        VBox centro = new VBox(areaConteudo, rodape);

        VBox.setVgrow(areaConteudo, Priority.ALWAYS);

        root.setCenter(centro);

        // ============================================================
        // EVENTOS DE BOTÕES
        // ============================================================

        botaoNovaConversa.setOnAction(
                evento -> {

                    if (aoNovaConversa != null) {

                        aoNovaConversa.run();
                    }
                }
        );

        botaoRenomear.setOnAction(
                evento -> {

                    if (aoRenomear != null) {

                        aoRenomear.run();
                    }
                }
        );

        botaoExcluir.setOnAction(
                evento -> {

                    if (aoExcluir != null) {

                        aoExcluir.run();
                    }
                }
        );

        atualizarEstadoVazio();
    }

    // ================================================================
    // TELA DE BOAS-VINDAS (orbe + sugestões)
    // ================================================================

    private VBox criarEstadoVazio() {

        Circle orbe = new Circle(46);

        RadialGradient gradiente = new RadialGradient(
                0, 0, 0.5, 0.5, 0.6, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#DAF1DE")),
                new Stop(0.5, Color.web("#8EB69B")),
                new Stop(1, Color.web("#163832"))
        );

        orbe.setFill(gradiente);

        Label titulo = new Label("Como posso te ajudar hoje?");

        titulo.getStyleClass().add("empty-title");

        titulo.setStyle(
                "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitulo = new Label(
                "Pergunte algo, ou escolha uma sugestão abaixo."
        );

        subtitulo.getStyleClass().add("empty-subtitle");

        subtitulo.setStyle("-fx-font-size: 13px;");

        HBox cartoes = new HBox(
                14,
                criarCartaoSugestao(
                        "Resumir conhecimento",
                        "Peça um resumo dos documentos da base local.",
                        "Resuma o conteúdo mais importante da minha base de conhecimento."
                ),
                criarCartaoSugestao(
                        "Pesquisar na web",
                        "A IA busca informações atuais automaticamente.",
                        "Pesquise informações atuais sobre "
                ),
                criarCartaoSugestao(
                        "Só conversar",
                        "Comece um bate-papo qualquer.",
                        "Como você está hoje?"
                )
        );

        cartoes.setAlignment(Pos.CENTER);

        VBox caixa = new VBox(18, orbe, titulo, subtitulo, cartoes);

        caixa.setAlignment(Pos.CENTER);

        caixa.setMaxWidth(680);

        return caixa;
    }

    private VBox criarCartaoSugestao(
            String titulo,
            String descricao,
            String promptSugerido
    ) {

        Label tituloLabel = new Label(titulo);

        tituloLabel.getStyleClass().add("suggestion-card-title");

        tituloLabel.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;"
        );

        tituloLabel.setWrapText(true);

        Label descLabel = new Label(descricao);

        descLabel.getStyleClass().add("suggestion-card-desc");

        descLabel.setStyle("-fx-font-size: 11px;");

        descLabel.setWrapText(true);

        VBox cartao = new VBox(6, tituloLabel, descLabel);

        cartao.getStyleClass().add("suggestion-card");

        cartao.setPadding(new Insets(14));

        cartao.setPrefWidth(190);

        cartao.setMaxWidth(190);

        cartao.setStyle("-fx-cursor: hand;");

        cartao.setOnMouseClicked(
                evento -> {

                    campoMensagem.setText(promptSugerido);

                    campoMensagem.requestFocus();

                    campoMensagem.positionCaret(
                            promptSugerido.length()
                    );
                }
        );

        return cartao;
    }

    // ================================================================
    // BOLHAS DE MENSAGEM
    // ================================================================

    private void adicionarBolha(String texto, boolean doUsuario) {

        Label textoLabel = new Label(texto);

        textoLabel.setWrapText(true);

        textoLabel.setMaxWidth(480);

        textoLabel.getStyleClass().add(
                doUsuario ? "bubble-user-text" : "bubble-ai-text"
        );

        HBox linha = new HBox(textoLabel);

        linha.setAlignment(
                doUsuario ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT
        );

        mensagensBox.getChildren().add(linha);

        rolarParaBaixo();
    }

    private void adicionarBolhaErro(String texto) {

        Label textoLabel = new Label(texto);

        textoLabel.setWrapText(true);

        textoLabel.setMaxWidth(480);

        textoLabel.getStyleClass().add("bubble-error");

        HBox linha = new HBox(textoLabel);

        linha.setAlignment(Pos.CENTER_LEFT);

        mensagensBox.getChildren().add(linha);

        rolarParaBaixo();
    }

    private void rolarParaBaixo() {

        scrollMensagens.layout();

        scrollMensagens.setVvalue(1.0);
    }

    // ================================================================
    // ESTADO VAZIO x LISTA DE MENSAGENS
    // ================================================================

    private void atualizarEstadoVazio() {

        boolean vazio = mensagensBox.getChildren().size() <= 1;

        estadoVazio.setVisible(vazio);

        estadoVazio.setManaged(vazio);

        scrollMensagens.setVisible(!vazio);

        scrollMensagens.setManaged(!vazio);
    }

    // ================================================================
    // BUSCA
    // ================================================================

    private void aplicarFiltroBusca(String termo) {

        aplicandoFiltro = true;

        String valor = termo == null ? "" : termo.trim().toLowerCase();

        if (valor.isEmpty()) {

            listaConversas.getItems().setAll(ultimaListaCompleta);

        } else {

            List<Conversation> filtradas = ultimaListaCompleta.stream()
                    .filter(
                            c -> c.getTitulo() != null
                                    && c.getTitulo().toLowerCase().contains(valor)
                    )
                    .collect(Collectors.toList());

            listaConversas.getItems().setAll(filtradas);
        }

        aplicandoFiltro = false;
    }

    // ================================================================
    // TEMA CLARO / ESCURO
    // ================================================================

    private void alternarTema() {

        temaEscuro = !temaEscuro;

        if (temaEscuro) {

            root.getStyleClass().add("dark");

            botaoTema.setText("Claro");

        } else {

            root.getStyleClass().remove("dark");

            botaoTema.setText("Escuro");
        }
    }

    public boolean isTemaEscuro() {
        return temaEscuro;
    }

    // ================================================================
    // CALLBACKS
    // ================================================================

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

    // ================================================================
    // GETTERS
    // ================================================================

    public BorderPane getRoot() {
        return root;
    }

    public TextArea getAreaChat() {
        return areaChatOculta;
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

    // ================================================================
    // MENSAGENS (API usada pelo ChatController)
    // ================================================================

    public void adicionarMensagemUsuario(String mensagem) {

        adicionarBolha(mensagem, true);
    }

    public void adicionarMensagemIA(String mensagem) {

        adicionarMensagemIA(mensagem, "Fallback local", null);
    }

    public void adicionarMensagemIA(
            String mensagem,
            String origem,
            String fonte
    ) {

        ultimaRespostaIA = mensagem;

        botaoCopiar.setDisable(false);

        botaoRegenerar.setDisable(false);

        adicionarBolha(mensagem, false);

        origemResposta.setText(
                origem == null || origem.isBlank() ? "" : "Origem: " + origem
        );

        if (fonte == null || fonte.isBlank()) {

            fonteResposta.setText("");

        } else {

            fonteResposta.setText("Fonte: " + fonte);
        }
    }

    public void adicionarErro(String mensagem) {

        adicionarBolhaErro(mensagem);

        status.setText("Ocorreu um erro.");
    }

    public void limparConversa() {

        mensagensBox.getChildren().clear();

        ultimaRespostaIA = "";

        botaoCopiar.setDisable(true);

        botaoRegenerar.setDisable(true);

        origemResposta.setText("");

        fonteResposta.setText("");

        status.setText("");
    }

    public void carregarConversa(Conversation conversa) {

        mensagensBox.getChildren().clear();

        ultimaRespostaIA = "";

        botaoCopiar.setDisable(true);

        botaoRegenerar.setDisable(true);

        origemResposta.setText("");

        fonteResposta.setText("");

        for (var mensagem : conversa.getMensagens()) {

            if ("user".equals(mensagem.getRole())) {

                adicionarBolha(mensagem.getContent(), true);

            } else if ("assistant".equals(mensagem.getRole())) {

                adicionarBolha(mensagem.getContent(), false);

                ultimaRespostaIA = mensagem.getContent();

                botaoCopiar.setDisable(false);

                botaoRegenerar.setDisable(false);
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

        status.setText(carregando ? "A IA está pensando..." : "");
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

    // ================================================================
    // CÉLULA DO HISTÓRICO
    // ================================================================

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

            excluir.setOnAction(
                    evento -> {

                        Conversation conversa = getItem();

                        if (conversa != null && aoExcluir != null) {

                            getListView().getSelectionModel().select(conversa);

                            aoExcluir.run();
                        }
                    }
            );

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
}