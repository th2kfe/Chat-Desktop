package com.example.chatdesktop.view;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

public class AuthView {

    private final BorderPane root;

    private VBox painelLogin;
    private VBox painelRegistro;

    private Button tabLogin;
    private Button tabRegistro;
    private Button botaoTema;

    private boolean temaEscuro = true;

    // LOGIN
    private TextField campoLoginEmail;
    private CampoSenha campoLoginSenha;
    private CheckBox checkLembrar;
    private Hyperlink linkEsqueciSenha;
    private Label erroLogin;
    private Label sucessoLogin;
    private Button botaoLogin;
    private Button linkParaRegistro;

    // REGISTRO
    private TextField campoRegNome;
    private TextField campoRegEmail;
    private CampoSenha campoRegSenha;
    private CampoSenha campoRegConfirmar;
    private CheckBox checkTermos;
    private Label erroRegistro;
    private Button botaoRegistrar;
    private Button linkParaLogin;

    private StackPane cardContainer;

    public AuthView() {

        root = new BorderPane();

        root.getStyleClass().addAll(
                "app-root",
                "dark",
                "auth-root"
        );

        var css = getClass().getResource(
                "/com/example/chatdesktop/css/chat.css"
        );

        if (css != null) {
            root.getStylesheets().add(css.toExternalForm());
        }

        StackPane fundo = criarFundo();

        BorderPane conteudo = new BorderPane();

        conteudo.setTop(criarHeader());

        StackPane centro = new StackPane();

        HBox layout = new HBox(65);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(
                new Insets(30, 70, 45, 70)
        );

        VBox apresentacao = criarApresentacao();

        cardContainer = criarCardAutenticacao();

        HBox.setHgrow(apresentacao, Priority.ALWAYS);

        layout.getChildren().addAll(
                apresentacao,
                cardContainer
        );

        centro.getChildren().add(layout);

        conteudo.setCenter(centro);

        fundo.getChildren().add(conteudo);

        root.setCenter(fundo);

        animarEntrada(apresentacao, cardContainer);
    }

    // =========================================================
    // FUNDO
    // =========================================================

    private StackPane criarFundo() {

        StackPane fundo = new StackPane();

        fundo.getStyleClass().add("auth-space");

        Pane efeitos = new Pane();
        efeitos.setMouseTransparent(true);

        Circle glow1 = criarGlow("#168CFF", 400, 0.23);
        Circle glow2 = criarGlow("#604CFF", 350, 0.17);
        Circle glow3 = criarGlow("#24D9FF", 250, 0.12);

        glow1.layoutXProperty().bind(
                efeitos.widthProperty().multiply(0.15)
        );

        glow1.layoutYProperty().bind(
                efeitos.heightProperty().multiply(0.25)
        );

        glow2.layoutXProperty().bind(
                efeitos.widthProperty().multiply(0.90)
        );

        glow2.layoutYProperty().bind(
                efeitos.heightProperty().multiply(0.85)
        );

        glow3.layoutXProperty().bind(
                efeitos.widthProperty().multiply(0.75)
        );

        glow3.layoutYProperty().bind(
                efeitos.heightProperty().multiply(0.10)
        );

        efeitos.getChildren().addAll(
                glow1,
                glow2,
                glow3
        );

        criarEstrelas(efeitos);

        Rectangle vinheta = new Rectangle();

        vinheta.widthProperty().bind(
                fundo.widthProperty()
        );

        vinheta.heightProperty().bind(
                fundo.heightProperty()
        );

        vinheta.setFill(
                new RadialGradient(
                        0,
                        0,
                        0.5,
                        0.5,
                        0.75,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.TRANSPARENT),
                        new Stop(
                                1,
                                Color.web("#00020B", 0.68)
                        )
                )
        );

        vinheta.setMouseTransparent(true);

        fundo.getChildren().addAll(
                efeitos,
                vinheta
        );

        return fundo;
    }

    private Circle criarGlow(
            String cor,
            double raio,
            double opacity
    ) {

        Circle circle = new Circle(raio);

        circle.setFill(
                new RadialGradient(
                        0,
                        0,
                        0.5,
                        0.5,
                        0.5,
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

        circle.setEffect(
                new GaussianBlur(70)
        );

        return circle;
    }

    private void criarEstrelas(Pane pane) {

        Random random = new Random(42);

        for (int i = 0; i < 75; i++) {

            Circle estrela = new Circle(
                    0.4 + random.nextDouble() * 1.1
            );

            estrela.setFill(
                    Color.web(
                            "#BFEAFF",
                            0.15 + random.nextDouble() * 0.55
                    )
            );

            double x = random.nextDouble();
            double y = random.nextDouble();

            estrela.layoutXProperty().bind(
                    pane.widthProperty().multiply(x)
            );

            estrela.layoutYProperty().bind(
                    pane.heightProperty().multiply(y)
            );

            pane.getChildren().add(estrela);

            if (i % 8 == 0) {

                FadeTransition fade =
                        new FadeTransition(
                                Duration.seconds(
                                        1.4 + random.nextDouble() * 2
                                ),
                                estrela
                        );

                fade.setFromValue(0.15);
                fade.setToValue(1);
                fade.setAutoReverse(true);
                fade.setCycleCount(
                        Animation.INDEFINITE
                );

                fade.play();
            }
        }
    }

    // =========================================================
    // HEADER
    // =========================================================

    private HBox criarHeader() {

        StackPane logo = criarLogo(18);

        Label marca = new Label("FAITH IN GOD");
        marca.getStyleClass().add("auth-brand-title");

        Label mini = new Label(
                "INTELLIGENCE • KNOWLEDGE • FAITH"
        );

        mini.getStyleClass().add(
                "auth-brand-subtitle"
        );

        VBox textos = new VBox(
                1,
                marca,
                mini
        );

        HBox marcaBox = new HBox(
                11,
                logo,
                textos
        );

        marcaBox.setAlignment(Pos.CENTER_LEFT);

        Region espaco = new Region();

        HBox.setHgrow(
                espaco,
                Priority.ALWAYS
        );

        Label seguro = new Label(
                "●  SISTEMA SEGURO"
        );

        seguro.getStyleClass().add(
                "auth-secure"
        );

        botaoTema = new Button("☾");

        botaoTema.getStyleClass().add(
                "auth-theme-button"
        );

        botaoTema.setOnAction(
                e -> alternarTema()
        );

        HBox header = new HBox(
                15,
                marcaBox,
                espaco,
                seguro,
                botaoTema
        );

        header.setAlignment(Pos.CENTER_LEFT);

        header.setPadding(
                new Insets(22, 32, 20, 32)
        );

        header.getStyleClass().add(
                "auth-header"
        );

        return header;
    }

    // =========================================================
    // APRESENTAÇÃO
    // =========================================================

    private VBox criarApresentacao() {

        Label badge = new Label(
                "✦  FAITH INTELLIGENCE"
        );

        badge.getStyleClass().add(
                "auth-hero-badge"
        );

        Label titulo = new Label(
                "Inteligência que\nvai além."
        );

        titulo.getStyleClass().add(
                "auth-hero-title"
        );

        Label descricao = new Label(
                "Uma experiência criada para transformar\n" +
                        "perguntas em conhecimento, ideias em projetos\n" +
                        "e curiosidade em possibilidades."
        );

        descricao.getStyleClass().add(
                "auth-hero-description"
        );

        HBox feature1 = criarFeature(
                "✦",
                "Inteligência avançada",
                "Converse e explore qualquer assunto."
        );

        HBox feature2 = criarFeature(
                "◈",
                "Conhecimento conectado",
                "IA, RAG local e pesquisa trabalhando juntas."
        );

        HBox feature3 = criarFeature(
                "</>",
                "Feito para criar",
                "Estude, programe e desenvolva novas ideias."
        );

        VBox features = new VBox(
                13,
                feature1,
                feature2,
                feature3
        );

        VBox box = new VBox(
                20,
                badge,
                titulo,
                descricao,
                features
        );

        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(570);

        return box;
    }

    private HBox criarFeature(
            String icone,
            String titulo,
            String descricao
    ) {

        Label icon = new Label(icone);

        icon.getStyleClass().add(
                "auth-feature-icon"
        );

        StackPane iconBox =
                new StackPane(icon);

        iconBox.getStyleClass().add(
                "auth-feature-icon-box"
        );

        iconBox.setMinSize(42, 42);
        iconBox.setPrefSize(42, 42);

        Label title = new Label(titulo);

        title.getStyleClass().add(
                "auth-feature-title"
        );

        Label desc = new Label(descricao);

        desc.getStyleClass().add(
                "auth-feature-description"
        );

        VBox textos = new VBox(
                2,
                title,
                desc
        );

        HBox feature = new HBox(
                12,
                iconBox,
                textos
        );

        feature.setAlignment(Pos.CENTER_LEFT);

        return feature;
    }

    // =========================================================
    // CARD
    // =========================================================

    private StackPane criarCardAutenticacao() {

        tabLogin = new Button("Entrar");
        tabRegistro = new Button("Criar conta");

        tabLogin.getStyleClass().addAll(
                "auth-tab",
                "active"
        );

        tabRegistro.getStyleClass().add(
                "auth-tab"
        );

        tabLogin.setMaxWidth(Double.MAX_VALUE);
        tabRegistro.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(tabLogin, Priority.ALWAYS);
        HBox.setHgrow(tabRegistro, Priority.ALWAYS);

        HBox tabs = new HBox(
                5,
                tabLogin,
                tabRegistro
        );

        tabs.getStyleClass().add(
                "auth-tabs"
        );

        tabLogin.setOnAction(
                e -> mudarParaAba("login")
        );

        tabRegistro.setOnAction(
                e -> mudarParaAba("registro")
        );

        criarPainelLogin();
        criarPainelRegistro();

        StackPane paginas = new StackPane(
                painelLogin,
                painelRegistro
        );

        VBox card = new VBox(
                22,
                tabs,
                paginas
        );

        card.setPadding(
                new Insets(26)
        );

        card.setPrefWidth(410);
        card.setMaxWidth(410);

        card.getStyleClass().add(
                "auth-premium-card"
        );

        StackPane wrapper =
                new StackPane(card);

        wrapper.setMaxWidth(450);

        return wrapper;
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void criarPainelLogin() {

        Label pequeno = new Label(
                "BEM-VINDO DE VOLTA"
        );

        pequeno.getStyleClass().add(
                "auth-form-eyebrow"
        );

        Label titulo = new Label(
                "Entre na sua conta"
        );

        titulo.getStyleClass().add(
                "auth-form-title"
        );

        Label subtitulo = new Label(
                "Continue de onde você parou."
        );

        subtitulo.getStyleClass().add(
                "auth-form-subtitle"
        );

        campoLoginEmail = new TextField();

        campoLoginEmail.setPromptText(
                "seu@email.com"
        );

        campoLoginEmail.getStyleClass().add(
                "auth-input"
        );

        campoLoginSenha =
                new CampoSenha("Sua senha");

        checkLembrar =
                new CheckBox("Lembrar de mim");

        checkLembrar.getStyleClass().add(
                "auth-checkbox"
        );

        linkEsqueciSenha =
                new Hyperlink("Esqueci minha senha");

        linkEsqueciSenha.getStyleClass().add(
                "auth-link"
        );

        Region space = new Region();

        HBox.setHgrow(
                space,
                Priority.ALWAYS
        );

        HBox extras = new HBox(
                checkLembrar,
                space,
                linkEsqueciSenha
        );

        extras.setAlignment(Pos.CENTER_LEFT);

        sucessoLogin = new Label();

        sucessoLogin.getStyleClass().add(
                "auth-success"
        );

        sucessoLogin.setVisible(false);
        sucessoLogin.setManaged(false);

        erroLogin = new Label();

        erroLogin.getStyleClass().add(
                "auth-error"
        );

        erroLogin.setVisible(false);
        erroLogin.setManaged(false);

        botaoLogin =
                new Button("Entrar  →");

        botaoLogin.setMaxWidth(
                Double.MAX_VALUE
        );

        botaoLogin.getStyleClass().add(
                "auth-primary-button"
        );

        Label pergunta =
                new Label("Ainda não possui uma conta?");

        pergunta.getStyleClass().add(
                "auth-footer-text"
        );

        linkParaRegistro =
                new Button("Criar conta");

        linkParaRegistro.getStyleClass().add(
                "auth-text-button"
        );

        linkParaRegistro.setOnAction(
                e -> mudarParaAba("registro")
        );

        HBox rodape = new HBox(
                5,
                pergunta,
                linkParaRegistro
        );

        rodape.setAlignment(Pos.CENTER);

        painelLogin = new VBox(
                13,
                pequeno,
                titulo,
                subtitulo,
                campoComLabel(
                        "E-MAIL",
                        campoLoginEmail
                ),
                campoComLabel(
                        "SENHA",
                        campoLoginSenha
                ),
                extras,
                sucessoLogin,
                erroLogin,
                botaoLogin,
                rodape
        );

        painelLogin.getStyleClass().add(
                "auth-form"
        );
    }

    // =========================================================
    // REGISTRO
    // =========================================================

    private void criarPainelRegistro() {

        Label pequeno = new Label(
                "NOVA CONTA"
        );

        pequeno.getStyleClass().add(
                "auth-form-eyebrow"
        );

        Label titulo = new Label(
                "Comece sua jornada"
        );

        titulo.getStyleClass().add(
                "auth-form-title"
        );

        Label subtitulo = new Label(
                "Crie sua conta FAITH."
        );

        subtitulo.getStyleClass().add(
                "auth-form-subtitle"
        );

        campoRegNome = new TextField();
        campoRegNome.setPromptText("Seu nome");
        campoRegNome.getStyleClass().add(
                "auth-input"
        );

        campoRegEmail = new TextField();
        campoRegEmail.setPromptText(
                "seu@email.com"
        );

        campoRegEmail.getStyleClass().add(
                "auth-input"
        );

        campoRegSenha =
                new CampoSenha(
                        "Mínimo 8 caracteres"
                );

        campoRegConfirmar =
                new CampoSenha(
                        "Repita sua senha"
                );

        checkTermos =
                new CheckBox(
                        "Aceito os termos de uso"
                );

        checkTermos.getStyleClass().add(
                "auth-checkbox"
        );

        erroRegistro = new Label();

        erroRegistro.getStyleClass().add(
                "auth-error"
        );

        erroRegistro.setVisible(false);
        erroRegistro.setManaged(false);

        botaoRegistrar =
                new Button("Criar minha conta  →");

        botaoRegistrar.setMaxWidth(
                Double.MAX_VALUE
        );

        botaoRegistrar.getStyleClass().add(
                "auth-primary-button"
        );

        Label pergunta =
                new Label("Já possui uma conta?");

        pergunta.getStyleClass().add(
                "auth-footer-text"
        );

        linkParaLogin =
                new Button("Entrar");

        linkParaLogin.getStyleClass().add(
                "auth-text-button"
        );

        linkParaLogin.setOnAction(
                e -> mudarParaAba("login")
        );

        HBox rodape = new HBox(
                5,
                pergunta,
                linkParaLogin
        );

        rodape.setAlignment(Pos.CENTER);

        painelRegistro = new VBox(
                11,
                pequeno,
                titulo,
                subtitulo,
                campoComLabel(
                        "NOME",
                        campoRegNome
                ),
                campoComLabel(
                        "E-MAIL",
                        campoRegEmail
                ),
                campoComLabel(
                        "SENHA",
                        campoRegSenha
                ),
                campoComLabel(
                        "CONFIRMAR SENHA",
                        campoRegConfirmar
                ),
                checkTermos,
                erroRegistro,
                botaoRegistrar,
                rodape
        );

        painelRegistro.getStyleClass().add(
                "auth-form"
        );

        painelRegistro.setVisible(false);
        painelRegistro.setManaged(false);
    }

    private VBox campoComLabel(
            String texto,
            Region campo
    ) {

        Label label = new Label(texto);

        label.getStyleClass().add(
                "auth-field-label"
        );

        return new VBox(
                6,
                label,
                campo
        );
    }

    // =========================================================
    // SENHA
    // =========================================================

    public static class CampoSenha extends HBox {

        private final PasswordField passwordField;
        private final TextField textField;
        private final Button visualizar;

        private boolean visivel = false;

        public CampoSenha(String prompt) {

            passwordField =
                    new PasswordField();

            textField =
                    new TextField();

            passwordField.setPromptText(prompt);
            textField.setPromptText(prompt);

            passwordField.getStyleClass().add(
                    "auth-password-field"
            );

            textField.getStyleClass().add(
                    "auth-password-field"
            );

            textField.textProperty()
                    .bindBidirectional(
                            passwordField.textProperty()
                    );

            textField.setVisible(false);
            textField.setManaged(false);

            visualizar = new Button("◉");

            visualizar.getStyleClass().add(
                    "auth-eye"
            );

            visualizar.setOnAction(e ->
                    alternarVisibilidade()
            );

            StackPane campos =
                    new StackPane(
                            passwordField,
                            textField
                    );

            HBox.setHgrow(
                    campos,
                    Priority.ALWAYS
            );

            getChildren().addAll(
                    campos,
                    visualizar
            );

            setAlignment(Pos.CENTER);

            getStyleClass().add(
                    "auth-password-box"
            );
        }

        private void alternarVisibilidade() {

            visivel = !visivel;

            passwordField.setVisible(!visivel);
            passwordField.setManaged(!visivel);

            textField.setVisible(visivel);
            textField.setManaged(visivel);

            visualizar.setText(
                    visivel ? "○" : "◉"
            );

            if (visivel) {
                textField.requestFocus();
                textField.positionCaret(
                        textField.getText().length()
                );
            } else {
                passwordField.requestFocus();
                passwordField.positionCaret(
                        passwordField.getText().length()
                );
            }
        }

        public String getTexto() {
            return passwordField.getText();
        }

        public void limpar() {
            passwordField.clear();
        }
    }

    // =========================================================
    // LOGO
    // =========================================================

    private StackPane criarLogo(double raio) {

        Circle glow =
                new Circle(raio + 8);

        glow.setFill(
                Color.web("#178FFF", 0.17)
        );

        glow.setEffect(
                new GaussianBlur(10)
        );

        Circle circle =
                new Circle(raio);

        circle.setFill(
                new RadialGradient(
                        0,
                        0,
                        0.45,
                        0.35,
                        0.8,
                        true,
                        CycleMethod.NO_CYCLE,
                        new Stop(
                                0,
                                Color.web("#72F1FF")
                        ),
                        new Stop(
                                0.35,
                                Color.web("#168CFF")
                        ),
                        new Stop(
                                1,
                                Color.web("#03215E")
                        )
                )
        );

        circle.setStroke(
                Color.web("#77ECFF", 0.65)
        );

        Label estrela =
                new Label("✦");

        estrela.getStyleClass().add(
                "auth-logo-star"
        );

        return new StackPane(
                glow,
                circle,
                estrela
        );
    }

    // =========================================================
    // ABAS
    // =========================================================

    public void mudarParaAba(String aba) {

        boolean login =
                "login".equals(aba);

        VBox mostrar =
                login
                        ? painelLogin
                        : painelRegistro;

        VBox esconder =
                login
                        ? painelRegistro
                        : painelLogin;

        esconder.setVisible(false);
        esconder.setManaged(false);

        mostrar.setVisible(true);
        mostrar.setManaged(true);

        tabLogin.getStyleClass()
                .remove("active");

        tabRegistro.getStyleClass()
                .remove("active");

        if (login) {
            tabLogin.getStyleClass()
                    .add("active");
        } else {
            tabRegistro.getStyleClass()
                    .add("active");
        }

        mostrar.setOpacity(0);
        mostrar.setTranslateX(
                login ? -18 : 18
        );

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(280),
                        mostrar
                );

        fade.setToValue(1);

        TranslateTransition move =
                new TranslateTransition(
                        Duration.millis(330),
                        mostrar
                );

        move.setToX(0);
        move.setInterpolator(
                Interpolator.EASE_OUT
        );

        new ParallelTransition(
                fade,
                move
        ).play();

        limparErroLogin();
        limparErroRegistro();
    }

    // =========================================================
    // ANIMAÇÃO
    // =========================================================

    private void animarEntrada(
            Node esquerda,
            Node direita
    ) {

        esquerda.setOpacity(0);
        esquerda.setTranslateX(-35);

        direita.setOpacity(0);
        direita.setTranslateX(35);
        direita.setScaleX(0.96);
        direita.setScaleY(0.96);

        FadeTransition fade1 =
                new FadeTransition(
                        Duration.millis(800),
                        esquerda
                );

        fade1.setToValue(1);

        TranslateTransition move1 =
                new TranslateTransition(
                        Duration.millis(850),
                        esquerda
                );

        move1.setToX(0);
        move1.setInterpolator(
                Interpolator.EASE_OUT
        );

        FadeTransition fade2 =
                new FadeTransition(
                        Duration.millis(850),
                        direita
                );

        fade2.setToValue(1);

        TranslateTransition move2 =
                new TranslateTransition(
                        Duration.millis(900),
                        direita
                );

        move2.setToX(0);

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(900),
                        direita
                );

        scale.setToX(1);
        scale.setToY(1);

        SequentialTransition sequencia =
                new SequentialTransition(
                        new PauseTransition(
                                Duration.millis(100)
                        ),
                        new ParallelTransition(
                                fade1,
                                move1,
                                fade2,
                                move2,
                                scale
                        )
                );

        sequencia.play();
    }

    // =========================================================
    // TEMA
    // =========================================================

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

    public boolean isTemaEscuro() {
        return temaEscuro;
    }

    // =========================================================
    // MENSAGENS
    // =========================================================

    public void mostrarErroLogin(
            String mensagem
    ) {

        limparSucessoLogin();

        erroLogin.setText(mensagem);
        erroLogin.setVisible(true);
        erroLogin.setManaged(true);
    }

    public void limparErroLogin() {

        erroLogin.setVisible(false);
        erroLogin.setManaged(false);
    }

    public void mostrarSucessoLogin(
            String mensagem
    ) {

        sucessoLogin.setText(mensagem);
        sucessoLogin.setVisible(true);
        sucessoLogin.setManaged(true);
    }

    public void limparSucessoLogin() {

        sucessoLogin.setVisible(false);
        sucessoLogin.setManaged(false);
    }

    public void mostrarErroRegistro(
            String mensagem
    ) {

        erroRegistro.setText(mensagem);
        erroRegistro.setVisible(true);
        erroRegistro.setManaged(true);
    }

    public void limparErroRegistro() {

        erroRegistro.setVisible(false);
        erroRegistro.setManaged(false);
    }

    public void voltarParaLoginAposRegistro() {

        campoRegNome.clear();
        campoRegEmail.clear();

        campoRegSenha.limpar();
        campoRegConfirmar.limpar();

        checkTermos.setSelected(false);

        campoLoginEmail.clear();
        campoLoginSenha.limpar();

        mudarParaAba("login");

        mostrarSucessoLogin(
                "✓ Conta criada! Faça login para continuar."
        );
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public BorderPane getRoot() {
        return root;
    }

    public TextField getCampoLoginEmail() {
        return campoLoginEmail;
    }

    public CampoSenha getCampoLoginSenha() {
        return campoLoginSenha;
    }

    public CheckBox getCheckLembrar() {
        return checkLembrar;
    }

    public Hyperlink getLinkEsqueciSenha() {
        return linkEsqueciSenha;
    }

    public Button getBotaoLogin() {
        return botaoLogin;
    }

    public TextField getCampoRegNome() {
        return campoRegNome;
    }

    public TextField getCampoRegEmail() {
        return campoRegEmail;
    }

    public CampoSenha getCampoRegSenha() {
        return campoRegSenha;
    }

    public CampoSenha getCampoRegConfirmar() {
        return campoRegConfirmar;
    }

    public CheckBox getCheckTermos() {
        return checkTermos;
    }

    public Button getBotaoRegistrar() {
        return botaoRegistrar;
    }

    public Button getBotaoTema() {
        return botaoTema;
    }
}