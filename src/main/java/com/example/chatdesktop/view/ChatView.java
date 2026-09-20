package com.example.chatdesktop.view;

import com.example.chatdesktop.model.Conversation;

import javafx.animation.*;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ChatView {

    private final BorderPane root;

    private TextField campoBusca;
    private ListView<Conversation> listaConversas;

    private List<Conversation> listaCompleta =
            new ArrayList<>();

    private boolean filtrando;

    private Button botaoNovaConversa;
    private Button botaoRenomear;
    private Button botaoExcluir;
    private Button botaoTema;

    private final VBox mensagensBox;
    private final ScrollPane scrollMensagens;
    private final StackPane areaPrincipal;
    private final StackPane telaInicial;

    private TextField campoMensagem;
    private Button botaoEnviar;
    private Button botaoCopiar;
    private Button botaoRegenerar;

    private Label origemResposta;
    private Label fonteResposta;
    private Label status;

    private final TextArea areaChatOculta =
            new TextArea();

    private String ultimaRespostaIA = "";

    private boolean temaEscuro = true;

    private Consumer<Conversation>
            aoSelecionarConversa;

    private Runnable aoNovaConversa;
    private Runnable aoRegenerar;
    private Runnable aoRenomear;
    private Runnable aoExcluir;

    public ChatView() {

        root = new BorderPane();

        root.getStyleClass().addAll(
                "app-root",
                "dark",
                "chat-root"
        );

        var css = getClass().getResource(
                "/com/example/chatdesktop/css/chat.css"
        );

        if (css != null) {
            root.getStylesheets().add(
                    css.toExternalForm()
            );
        }

        StackPane fundo = criarFundo();

        BorderPane janela = new BorderPane();

        janela.getStyleClass().add(
                "ai-window"
        );

        janela.setMaxWidth(1500);
        janela.setMaxHeight(930);

        StackPane.setMargin(
                janela,
                new Insets(17)
        );

        janela.setLeft(criarSidebar());

        BorderPane centro =
                new BorderPane();

        centro.getStyleClass().add(
                "ai-main"
        );

        centro.setTop(criarHeader());

        mensagensBox =
                new VBox(22);

        mensagensBox.getStyleClass().add(
                "ai-messages"
        );

        mensagensBox.setPadding(
                new Insets(35, 65, 45, 65)
        );

        scrollMensagens =
                new ScrollPane(
                        mensagensBox
                );

        scrollMensagens.setFitToWidth(true);

        scrollMensagens.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollMensagens.getStyleClass().add(
                "ai-scroll"
        );

        telaInicial =
                criarTelaInicial();

        areaPrincipal =
                new StackPane(
                        criarEstrelas(),
                        scrollMensagens,
                        telaInicial
                );

        areaPrincipal.getStyleClass().add(
                "ai-content"
        );

        centro.setCenter(areaPrincipal);
        centro.setBottom(criarComposer());

        janela.setCenter(centro);

        fundo.getChildren().add(janela);

        root.setCenter(fundo);

        mensagensBox
                .getChildren()
                .addListener(
                        (ListChangeListener<Node>) c ->
                                atualizarTelaInicial()
                );

        configurarEventos();
        atualizarTelaInicial();
    }

    // =========================================================
    // FUNDO
    // =========================================================

    private StackPane criarFundo() {

        StackPane fundo =
                new StackPane();

        fundo.getStyleClass().add(
                "ai-outer"
        );

        Pane glows = new Pane();
        glows.setMouseTransparent(true);

        Circle azul =
                glow("#168CFF", 380, .20);

        Circle roxo =
                glow("#704DFF", 360, .15);

        azul.layoutXProperty().bind(
                glows.widthProperty()
                        .multiply(.10)
        );

        azul.layoutYProperty().bind(
                glows.heightProperty()
                        .multiply(.20)
        );

        roxo.layoutXProperty().bind(
                glows.widthProperty()
                        .multiply(.90)
        );

        roxo.layoutYProperty().bind(
                glows.heightProperty()
                        .multiply(.85)
        );

        glows.getChildren().addAll(
                azul,
                roxo
        );

        fundo.getChildren().add(glows);

        return fundo;
    }

    private Circle glow(
            String cor,
            double raio,
            double opacity
    ) {

        Circle c = new Circle(raio);

        c.setFill(
                new RadialGradient(
                        0, 0,
                        .5, .5,
                        .5,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(
                                0,
                                Color.web(cor, opacity)
                        ),
                        new Stop(
                                1,
                                Color.TRANSPARENT
                        )
                )
        );

        c.setEffect(
                new GaussianBlur(70)
        );

        return c;
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private VBox criarSidebar() {

        StackPane logo = criarLogo(19);

        Label nome =
                new Label("FAITH IN GOD");

        nome.getStyleClass().add(
                "side-brand"
        );

        Label descricao =
                new Label("YOUR AI COMPANION");

        descricao.getStyleClass().add(
                "side-brand-mini"
        );

        VBox marcaText =
                new VBox(
                        1,
                        nome,
                        descricao
                );

        HBox marca =
                new HBox(
                        10,
                        logo,
                        marcaText
                );

        marca.setAlignment(
                Pos.CENTER_LEFT
        );

        marca.getStyleClass().add(
                "side-logo"
        );

        // NOVA CONVERSA

        botaoNovaConversa =
                new Button(
                        "＋   Nova conversa"
                );

        botaoNovaConversa.setMaxWidth(
                Double.MAX_VALUE
        );

        botaoNovaConversa.getStyleClass().add(
                "side-new-chat"
        );

        // BUSCA

        Label buscaIcon =
                new Label("⌕");

        buscaIcon.getStyleClass().add(
                "side-search-icon"
        );

        campoBusca =
                new TextField();

        campoBusca.setPromptText(
                "Pesquisar conversas..."
        );

        campoBusca.getStyleClass().add(
                "side-search-input"
        );

        HBox.setHgrow(
                campoBusca,
                Priority.ALWAYS
        );

        HBox busca =
                new HBox(
                        8,
                        buscaIcon,
                        campoBusca
                );

        busca.setAlignment(
                Pos.CENTER_LEFT
        );

        busca.getStyleClass().add(
                "side-search"
        );

        // HISTÓRICO

        Label historico =
                new Label("CONVERSAS");

        historico.getStyleClass().add(
                "side-section-title"
        );

        listaConversas =
                new ListView<>();

        listaConversas.getStyleClass().add(
                "side-history"
        );

        listaConversas.setCellFactory(
                lista ->
                        new ConversationCell()
        );

        VBox.setVgrow(
                listaConversas,
                Priority.ALWAYS
        );

        // FOOTER

        Circle avatarCircle =
                new Circle(18);

        avatarCircle.setFill(
                new LinearGradient(
                        0, 0,
                        1, 1,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(
                                0,
                                Color.web("#55E8FF")
                        ),
                        new Stop(
                                1,
                                Color.web("#0E4EB2")
                        )
                )
        );

        Label avatarText =
                new Label("U");

        avatarText.getStyleClass().add(
                "side-avatar-text"
        );

        StackPane avatar =
                new StackPane(
                        avatarCircle,
                        avatarText
                );

        Label usuario =
                new Label("Usuário");

        usuario.getStyleClass().add(
                "side-user-name"
        );

        Label plano =
                new Label("FAITH ACCOUNT");

        plano.getStyleClass().add(
                "side-user-plan"
        );

        VBox userInfo =
                new VBox(
                        1,
                        usuario,
                        plano
                );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        botaoTema =
                new Button("☾");

        botaoTema.getStyleClass().add(
                "side-theme"
        );

        HBox perfil =
                new HBox(
                        9,
                        avatar,
                        userInfo,
                        spacer,
                        botaoTema
                );

        perfil.setAlignment(
                Pos.CENTER_LEFT
        );

        perfil.getStyleClass().add(
                "side-profile"
        );

        VBox sidebar =
                new VBox(
                        17,
                        marca,
                        botaoNovaConversa,
                        busca,
                        historico,
                        listaConversas,
                        perfil
                );

        sidebar.setPrefWidth(270);
        sidebar.setMinWidth(245);
        sidebar.setMaxWidth(290);

        sidebar.setPadding(
                new Insets(
                        22,
                        16,
                        16,
                        16
                )
        );

        sidebar.getStyleClass().add(
                "ai-sidebar"
        );

        configurarBusca();

        return sidebar;
    }

    // =========================================================
    // HEADER
    // =========================================================

    private HBox criarHeader() {

        StackPane logo =
                criarLogo(14);

        Label titulo =
                new Label(
                        "FAITH AI"
                );

        titulo.getStyleClass().add(
                "ai-header-title"
        );

        Label statusOnline =
                new Label(
                        "●  ONLINE"
                );

        statusOnline.getStyleClass().add(
                "ai-online"
        );

        HBox esquerda =
                new HBox(
                        10,
                        logo,
                        titulo,
                        statusOnline
                );

        esquerda.setAlignment(
                Pos.CENTER_LEFT
        );

        Region space =
                new Region();

        HBox.setHgrow(
                space,
                Priority.ALWAYS
        );

        botaoRenomear =
                new Button(
                        "✎  Renomear"
                );

        botaoExcluir =
                new Button(
                        "⌫  Excluir"
                );

        botaoRenomear.getStyleClass().add(
                "ai-header-button"
        );

        botaoExcluir.getStyleClass().add(
                "ai-header-button"
        );

        HBox header =
                new HBox(
                        9,
                        esquerda,
                        space,
                        botaoRenomear,
                        botaoExcluir
                );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setPadding(
                new Insets(
                        15,
                        22,
                        15,
                        22
                )
        );

        header.getStyleClass().add(
                "ai-header"
        );

        return header;
    }

    // =========================================================
    // TELA INICIAL
    // =========================================================

    private StackPane criarTelaInicial() {

        StackPane logo =
                criarLogo(38);

        Label badge =
                new Label(
                        "✦  FAITH INTELLIGENCE"
                );

        badge.getStyleClass().add(
                "ai-welcome-badge"
        );

        Label titulo =
                new Label(
                        "Como posso ajudar?"
                );

        titulo.getStyleClass().add(
                "ai-welcome-title"
        );

        Label subtitulo =
                new Label(
                        "Pergunte, explore, aprenda e transforme suas ideias."
                );

        subtitulo.getStyleClass().add(
                "ai-welcome-subtitle"
        );

        HBox sugestoes =
                new HBox(
                        10,
                        criarSugestao(
                                "✦",
                                "Criar uma ideia",
                                "Me dê uma ideia incrível para um projeto"
                        ),
                        criarSugestao(
                                "</>",
                                "Programar",
                                "Me ajude com meu código Java"
                        ),
                        criarSugestao(
                                "◈",
                                "Aprender",
                                "Me ensine algo novo hoje"
                        )
                );

        sugestoes.setAlignment(
                Pos.CENTER
        );

        VBox conteudo =
                new VBox(
                        16,
                        logo,
                        badge,
                        titulo,
                        subtitulo,
                        sugestoes
                );

        conteudo.setAlignment(
                Pos.CENTER
        );

        StackPane wrapper =
                new StackPane(conteudo);

        animarWelcome(conteudo);

        return wrapper;
    }

    private VBox criarSugestao(
            String icon,
            String titulo,
            String prompt
    ) {

        Label i =
                new Label(icon);

        i.getStyleClass().add(
                "ai-suggestion-icon"
        );

        Label t =
                new Label(titulo);

        t.getStyleClass().add(
                "ai-suggestion-title"
        );

        Label p =
                new Label(prompt);

        p.setWrapText(true);

        p.getStyleClass().add(
                "ai-suggestion-text"
        );

        VBox card =
                new VBox(
                        7,
                        i,
                        t,
                        p
                );

        card.setPrefWidth(185);
        card.setPrefHeight(105);

        card.getStyleClass().add(
                "ai-suggestion"
        );

        card.setOnMouseClicked(e -> {

            campoMensagem.setText(prompt);
            campoMensagem.requestFocus();

            animarClique(card);
        });

        return card;
    }

    // =========================================================
    // ESTRELAS
    // =========================================================

    private Pane criarEstrelas() {

        Pane pane = new Pane();

        pane.setMouseTransparent(true);

        Random random =
                new Random(93);

        for (int i = 0; i < 65; i++) {

            Circle c =
                    new Circle(
                            .35
                                    + random.nextDouble()
                                    * .8
                    );

            c.setFill(
                    Color.web(
                            "#BCE8FF",
                            .10
                                    + random.nextDouble()
                                    * .40
                    )
            );

            double x =
                    random.nextDouble();

            double y =
                    random.nextDouble();

            c.layoutXProperty().bind(
                    pane.widthProperty()
                            .multiply(x)
            );

            c.layoutYProperty().bind(
                    pane.heightProperty()
                            .multiply(y)
            );

            pane.getChildren().add(c);
        }

        return pane;
    }

    // =========================================================
    // COMPOSER
    // =========================================================

    private VBox criarComposer() {

        origemResposta =
                new Label();

        fonteResposta =
                new Label();

        status =
                new Label();

        origemResposta.getStyleClass().add(
                "ai-source"
        );

        fonteResposta.getStyleClass().add(
                "ai-source-detail"
        );

        status.getStyleClass().add(
                "ai-status"
        );

        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        HBox info =
                new HBox(
                        8,
                        origemResposta,
                        fonteResposta,
                        spacer,
                        status
                );

        info.setAlignment(
                Pos.CENTER_LEFT
        );

        botaoCopiar =
                new Button("▣  Copiar");

        botaoRegenerar =
                new Button("↻  Regenerar");

        botaoCopiar.getStyleClass().add(
                "ai-small-action"
        );

        botaoRegenerar.getStyleClass().add(
                "ai-small-action"
        );

        botaoCopiar.setDisable(true);
        botaoRegenerar.setDisable(true);

        HBox actions =
                new HBox(
                        7,
                        botaoCopiar,
                        botaoRegenerar
                );

        campoMensagem =
                new TextField();

        campoMensagem.setPromptText(
                "Envie uma mensagem para FAITH AI..."
        );

        campoMensagem.getStyleClass().add(
                "ai-input"
        );

        HBox.setHgrow(
                campoMensagem,
                Priority.ALWAYS
        );

        Label shortcut =
                new Label("ENTER");

        shortcut.getStyleClass().add(
                "ai-shortcut"
        );

        botaoEnviar =
                new Button("➤");

        botaoEnviar.getStyleClass().add(
                "ai-send"
        );

        botaoEnviar.setMinSize(52, 52);
        botaoEnviar.setPrefSize(52, 52);

        HBox composer =
                new HBox(
                        10,
                        campoMensagem,
                        shortcut,
                        botaoEnviar
                );

        composer.setAlignment(
                Pos.CENTER
        );

        composer.getStyleClass().add(
                "ai-composer"
        );

        VBox footer =
                new VBox(
                        7,
                        info,
                        actions,
                        composer
                );

        footer.setPadding(
                new Insets(
                        8,
                        30,
                        18,
                        30
                )
        );

        footer.getStyleClass().add(
                "ai-footer"
        );

        return footer;
    }

    // =========================================================
    // BUSCA
    // =========================================================

    private void configurarBusca() {

        listaConversas
                .getItems()
                .addListener(
                        (ListChangeListener<Conversation>) c -> {

                            if (!filtrando) {
                                listaCompleta =
                                        new ArrayList<>(
                                                listaConversas
                                                        .getItems()
                                        );
                            }
                        }
                );

        campoBusca
                .textProperty()
                .addListener(
                        (obs, antigo, novo) ->
                                filtrar(novo)
                );
    }

    private void filtrar(String texto) {

        filtrando = true;

        String busca =
                texto == null
                        ? ""
                        : texto
                        .trim()
                        .toLowerCase();

        if (busca.isBlank()) {

            listaConversas
                    .getItems()
                    .setAll(
                            listaCompleta
                    );

        } else {

            List<Conversation> resultado =
                    listaCompleta
                            .stream()
                            .filter(c ->
                                    c.getTitulo() != null
                                            &&
                                            c.getTitulo()
                                                    .toLowerCase()
                                                    .contains(busca)
                            )
                            .collect(
                                    Collectors.toList()
                            );

            listaConversas
                    .getItems()
                    .setAll(resultado);
        }

        filtrando = false;
    }

    // =========================================================
    // HISTÓRICO PREMIUM
    // =========================================================

    private class ConversationCell
            extends ListCell<Conversation> {

        private final Label categoria =
                new Label();

        private final Label icon =
                new Label("◇");

        private final Label titulo =
                new Label();

        private final Label horario =
                new Label();

        private final VBox container =
                new VBox(4);

        ConversationCell() {

            categoria.getStyleClass().add(
                    "history-category"
            );

            icon.getStyleClass().add(
                    "history-icon"
            );

            titulo.getStyleClass().add(
                    "history-title-text"
            );

            horario.getStyleClass().add(
                    "history-time"
            );

            titulo.setTextOverrun(
                    OverrunStyle.ELLIPSIS
            );

            HBox.setHgrow(
                    titulo,
                    Priority.ALWAYS
            );

            HBox row =
                    new HBox(
                            8,
                            icon,
                            titulo,
                            horario
                    );

            row.setAlignment(
                    Pos.CENTER_LEFT
            );

            row.getStyleClass().add(
                    "history-row"
            );

            container.getChildren().addAll(
                    categoria,
                    row
            );
        }

        @Override
        protected void updateItem(
                Conversation conversa,
                boolean empty
        ) {

            super.updateItem(
                    conversa,
                    empty
            );

            if (empty || conversa == null) {

                setGraphic(null);
                setText(null);
                return;
            }

            titulo.setText(
                    conversa.getTitulo() == null
                            ? "Nova conversa"
                            : conversa.getTitulo()
            );

            LocalDateTime data =
                    conversa.getUltimaAtualizacao();

            horario.setText(
                    data.format(
                            DateTimeFormatter.ofPattern(
                                    "HH:mm"
                            )
                    )
            );

            categoria.setText(
                    obterCategoria(data)
            );

            /*
             * Só mostra o cabeçalho de categoria
             * quando ele muda em relação ao item anterior.
             */
            int indice = getIndex();

            boolean mostrarCategoria = true;

            if (indice > 0
                    && indice <
                    getListView()
                            .getItems()
                            .size()) {

                Conversation anterior =
                        getListView()
                                .getItems()
                                .get(indice - 1);

                String categoriaAnterior =
                        obterCategoria(
                                anterior
                                        .getUltimaAtualizacao()
                        );

                mostrarCategoria =
                        !categoriaAnterior.equals(
                                categoria.getText()
                        );
            }

            categoria.setVisible(
                    mostrarCategoria
            );

            categoria.setManaged(
                    mostrarCategoria
            );

            setText(null);
            setGraphic(container);
        }
    }

    private String obterCategoria(
            LocalDateTime data
    ) {

        LocalDate hoje =
                LocalDate.now();

        LocalDate d =
                data.toLocalDate();

        if (d.equals(hoje)) {
            return "HOJE";
        }

        if (d.equals(
                hoje.minusDays(1)
        )) {
            return "ONTEM";
        }

        if (!d.isBefore(
                hoje.minusDays(7)
        )) {
            return "ÚLTIMOS 7 DIAS";
        }

        return "ANTERIORES";
    }

    // =========================================================
    // EVENTOS
    // =========================================================

    private void configurarEventos() {

        botaoNovaConversa.setOnAction(e -> {

            animarClique(
                    botaoNovaConversa
            );

            if (aoNovaConversa != null) {
                aoNovaConversa.run();
            }
        });

        botaoRenomear.setOnAction(e -> {

            if (aoRenomear != null) {
                aoRenomear.run();
            }
        });

        botaoExcluir.setOnAction(e -> {

            if (aoExcluir != null) {
                aoExcluir.run();
            }
        });

        botaoRegenerar.setOnAction(e -> {

            if (aoRegenerar != null) {
                aoRegenerar.run();
            }
        });

        botaoCopiar.setOnAction(
                e -> copiarResposta()
        );

        botaoTema.setOnAction(
                e -> alternarTema()
        );

        listaConversas.setOnMouseClicked(e -> {

            Conversation conversa =
                    listaConversas
                            .getSelectionModel()
                            .getSelectedItem();

            if (conversa != null
                    && aoSelecionarConversa != null) {

                aoSelecionarConversa.accept(
                        conversa
                );
            }
        });
    }

    // =========================================================
    // MENSAGENS
    // =========================================================

    private void criarMensagemUsuario(
            String texto
    ) {

        Label mensagem =
                new Label(texto);

        mensagem.setWrapText(true);
        mensagem.setMaxWidth(580);

        mensagem.getStyleClass().add(
                "message-user"
        );

        Label avatar =
                new Label("U");

        avatar.getStyleClass().add(
                "message-user-avatar"
        );

        HBox row =
                new HBox(
                        10,
                        mensagem,
                        avatar
                );

        row.setAlignment(
                Pos.TOP_RIGHT
        );

        row.setPadding(
                new Insets(
                        2,
                        5,
                        2,
                        150
                )
        );

        mensagensBox
                .getChildren()
                .add(row);

        animarMensagem(row, true);
        scrollFinal();
    }

    private void criarMensagemIA(
            String texto
    ) {

        StackPane avatar =
                criarLogo(17);

        Label nome =
                new Label("FAITH AI");

        nome.getStyleClass().add(
                "message-ai-name"
        );

        VBox resposta =
                formatarResposta(texto);

        VBox content =
                new VBox(
                        5,
                        nome,
                        resposta
                );

        HBox row =
                new HBox(
                        11,
                        avatar,
                        content
                );

        row.setAlignment(
                Pos.TOP_LEFT
        );

        row.setPadding(
                new Insets(
                        2,
                        130,
                        2,
                        5
                )
        );

        mensagensBox
                .getChildren()
                .add(row);

        animarMensagem(row, false);
        scrollFinal();
    }

    private VBox formatarResposta(
            String texto
    ) {

        VBox card =
                new VBox(9);

        card.setMaxWidth(760);

        card.getStyleClass().add(
                "message-ai-card"
        );

        if (texto == null
                || texto.isBlank()) {
            return card;
        }

        for (String original :
                texto.split("\\R")) {

            String linha =
                    original.trim();

            if (linha.isBlank()) {
                continue;
            }

            if (linha.matches(
                    "^#{1,6}\\s+.*"
            )) {

                Label heading =
                        new Label(
                                limparMarkdown(
                                        linha.replaceFirst(
                                                "^#{1,6}\\s+",
                                                ""
                                        )
                                )
                        );

                heading.setWrapText(true);

                heading.getStyleClass().add(
                        "message-ai-heading"
                );

                card.getChildren().add(
                        heading
                );

                continue;
            }

            if (linha.matches(
                    "^[-*•]\\s+.*"
            )) {

                Label bullet =
                        new Label("•");

                bullet.getStyleClass().add(
                        "message-ai-bullet"
                );

                Label body =
                        criarBody(
                                linha.replaceFirst(
                                        "^[-*•]\\s+",
                                        ""
                                )
                        );

                HBox row =
                        new HBox(
                                8,
                                bullet,
                                body
                        );

                row.setAlignment(
                        Pos.TOP_LEFT
                );

                card.getChildren().add(row);

                continue;
            }

            if (linha.matches(
                    "^\\d+[.)]\\s+.*"
            )) {

                String numero =
                        linha.replaceFirst(
                                "^(\\d+)[.)].*",
                                "$1"
                        );

                Label num =
                        new Label(numero);

                num.getStyleClass().add(
                        "message-ai-number"
                );

                Label body =
                        criarBody(
                                linha.replaceFirst(
                                        "^\\d+[.)]\\s+",
                                        ""
                                )
                        );

                HBox row =
                        new HBox(
                                9,
                                num,
                                body
                        );

                row.setAlignment(
                        Pos.TOP_LEFT
                );

                card.getChildren().add(row);

                continue;
            }

            card.getChildren().add(
                    criarBody(linha)
            );
        }

        return card;
    }

    private Label criarBody(
            String texto
    ) {

        Label label =
                new Label(
                        limparMarkdown(texto)
                );

        label.setWrapText(true);

        label.getStyleClass().add(
                "message-ai-body"
        );

        return label;
    }

    private String limparMarkdown(
            String texto
    ) {

        return texto
                .replace("**", "")
                .replace("__", "")
                .replace("`", "")
                .trim();
    }

    // =========================================================
    // LOGO
    // =========================================================

    private StackPane criarLogo(
            double raio
    ) {

        Circle glow =
                new Circle(raio + 6);

        glow.setFill(
                Color.web(
                        "#168CFF",
                        .15
                )
        );

        glow.setEffect(
                new GaussianBlur(8)
        );

        Circle circle =
                new Circle(raio);

        circle.setFill(
                new RadialGradient(
                        0, 0,
                        .4, .3,
                        .8,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(
                                0,
                                Color.web("#73F2FF")
                        ),
                        new Stop(
                                .35,
                                Color.web("#168CFF")
                        ),
                        new Stop(
                                1,
                                Color.web("#03215E")
                        )
                )
        );

        circle.setStroke(
                Color.web(
                        "#7AEFFF",
                        .65
                )
        );

        Label star =
                new Label("✦");

        star.getStyleClass().add(
                "ai-logo-star"
        );

        return new StackPane(
                glow,
                circle,
                star
        );
    }

    // =========================================================
    // ANIMAÇÕES
    // =========================================================

    private void animarWelcome(
            Node node
    ) {

        node.setOpacity(0);
        node.setTranslateY(20);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(800),
                        node
                );

        fade.setToValue(1);

        TranslateTransition move =
                new TranslateTransition(
                        Duration.millis(850),
                        node
                );

        move.setToY(0);

        move.setInterpolator(
                Interpolator.EASE_OUT
        );

        new ParallelTransition(
                fade,
                move
        ).play();
    }

    private void animarMensagem(
            Node node,
            boolean usuario
    ) {

        node.setOpacity(0);

        node.setTranslateX(
                usuario ? 18 : -18
        );

        node.setTranslateY(10);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(320),
                        node
                );

        fade.setToValue(1);

        TranslateTransition move =
                new TranslateTransition(
                        Duration.millis(390),
                        node
                );

        move.setToX(0);
        move.setToY(0);

        move.setInterpolator(
                Interpolator.EASE_OUT
        );

        new ParallelTransition(
                fade,
                move
        ).play();
    }

    private void animarClique(
            Node node
    ) {

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(95),
                        node
                );

        scale.setToX(.97);
        scale.setToY(.97);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);

        scale.play();
    }

    // =========================================================
    // OUTROS
    // =========================================================

    private void atualizarTelaInicial() {

        boolean vazio =
                mensagensBox
                        .getChildren()
                        .isEmpty();

        telaInicial.setVisible(vazio);
        telaInicial.setManaged(vazio);

        scrollMensagens.setVisible(!vazio);
        scrollMensagens.setManaged(!vazio);
    }

    private void scrollFinal() {

        scrollMensagens.applyCss();
        scrollMensagens.layout();
        scrollMensagens.setVvalue(1);
    }

    private void copiarResposta() {

        if (ultimaRespostaIA == null
                || ultimaRespostaIA.isBlank()) {
            return;
        }

        ClipboardContent content =
                new ClipboardContent();

        content.putString(
                ultimaRespostaIA
        );

        Clipboard
                .getSystemClipboard()
                .setContent(content);

        status.setText(
                "✓ Copiado"
        );
    }

    private void alternarTema() {

        temaEscuro = !temaEscuro;

        if (temaEscuro) {

            if (!root.getStyleClass()
                    .contains("dark")) {

                root.getStyleClass()
                        .add("dark");
            }

            botaoTema.setText("☾");

        } else {

            root.getStyleClass()
                    .remove("dark");

            botaoTema.setText("☀");
        }
    }

    // =========================================================
    // CALLBACKS
    // =========================================================

    public void setAoSelecionarConversa(
            Consumer<Conversation> callback
    ) {
        aoSelecionarConversa = callback;
    }

    public void setAoNovaConversa(
            Runnable callback
    ) {
        aoNovaConversa = callback;
    }

    public void setAoRegenerar(
            Runnable callback
    ) {
        aoRegenerar = callback;
    }

    public void setAoRenomear(
            Runnable callback
    ) {
        aoRenomear = callback;
    }

    public void setAoExcluir(
            Runnable callback
    ) {
        aoExcluir = callback;
    }

    // =========================================================
    // GETTERS
    // =========================================================

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

    public ListView<Conversation>
    getListaConversas() {
        return listaConversas;
    }

    // =========================================================
    // CONTROLLER API
    // =========================================================

    public void adicionarMensagemUsuario(
            String mensagem
    ) {

        criarMensagemUsuario(mensagem);
    }

    public void adicionarMensagemIA(
            String mensagem
    ) {

        adicionarMensagemIA(
                mensagem,
                "FAITH AI",
                null
        );
    }

    public void adicionarMensagemIA(
            String mensagem,
            String origem,
            String fonte
    ) {

        ultimaRespostaIA = mensagem;

        botaoCopiar.setDisable(false);
        botaoRegenerar.setDisable(false);

        criarMensagemIA(mensagem);

        origemResposta.setText(
                origem == null
                        || origem.isBlank()
                        ? ""
                        : "● " + origem
        );

        fonteResposta.setText(
                fonte == null
                        || fonte.isBlank()
                        ? ""
                        : fonte
        );
    }

    public void adicionarErro(
            String mensagem
    ) {

        Label erro =
                new Label(
                        "⚠  " + mensagem
                );

        erro.setWrapText(true);

        erro.getStyleClass().add(
                "message-error"
        );

        mensagensBox
                .getChildren()
                .add(erro);

        animarMensagem(
                erro,
                false
        );

        scrollFinal();
    }

    public void limparConversa() {

        mensagensBox
                .getChildren()
                .clear();

        ultimaRespostaIA = "";

        botaoCopiar.setDisable(true);
        botaoRegenerar.setDisable(true);

        origemResposta.setText("");
        fonteResposta.setText("");
        status.setText("");

        atualizarTelaInicial();
    }

    public void carregarConversa(
            Conversation conversa
    ) {

        mensagensBox
                .getChildren()
                .clear();

        ultimaRespostaIA = "";

        if (conversa == null
                || conversa.getMensagens() == null) {

            atualizarTelaInicial();
            return;
        }

        for (var mensagem :
                conversa.getMensagens()) {

            if ("user".equals(
                    mensagem.getRole()
            )) {

                criarMensagemUsuario(
                        mensagem.getContent()
                );

            } else if (
                    "assistant".equals(
                            mensagem.getRole()
                    )
            ) {

                criarMensagemIA(
                        mensagem.getContent()
                );

                ultimaRespostaIA =
                        mensagem.getContent();
            }
        }

        boolean possuiResposta =
                !ultimaRespostaIA.isBlank();

        botaoCopiar.setDisable(
                !possuiResposta
        );

        botaoRegenerar.setDisable(
                !possuiResposta
        );

        atualizarTelaInicial();
    }

    public void setCarregando(
            boolean carregando
    ) {

        botaoEnviar.setDisable(
                carregando
        );

        campoMensagem.setDisable(
                carregando
        );

        botaoCopiar.setDisable(
                carregando
                        || ultimaRespostaIA.isBlank()
        );

        botaoRegenerar.setDisable(
                carregando
                        || ultimaRespostaIA.isBlank()
        );

        status.setText(
                carregando
                        ? "✦ Pensando..."
                        : ""
        );
    }

    public boolean confirmarExclusao(
            String titulo
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        alert.setTitle(
                "Excluir conversa"
        );

        alert.setHeaderText(
                "Excluir esta conversa?"
        );

        alert.setContentText(
                "\"" + titulo +
                        "\" será removida do histórico."
        );

        return alert
                .showAndWait()
                .filter(
                        b ->
                                b == ButtonType.OK
                )
                .isPresent();
    }
}