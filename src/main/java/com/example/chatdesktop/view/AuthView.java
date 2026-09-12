package com.example.chatdesktop.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AuthView {

    private final BorderPane root;

    private final VBox painelLogin;

    private final VBox painelRegistro;

    private final Button tabLogin;

    private final Button tabRegistro;

    private final Button botaoTema;

    private boolean temaEscuro = false;

    // ---- LOGIN ----
    private final TextField campoLoginEmail;

    private final CampoSenha campoLoginSenha;

    private final CheckBox checkLembrar;

    private final Hyperlink linkEsqueciSenha;

    private final Label erroLogin;

    private final Label sucessoLogin;

    private final Button botaoLogin;

    private final Button linkParaRegistro;

    // ---- REGISTRO ----
    private final TextField campoRegNome;

    private final TextField campoRegEmail;

    private final CampoSenha campoRegSenha;

    private final CampoSenha campoRegConfirmar;

    private final CheckBox checkTermos;

    private final Label erroRegistro;

    private final Button botaoRegistrar;

    private final Button linkParaLogin;

    public AuthView() {

        root = new BorderPane();

        root.getStyleClass().add("app-root");

        root.getStylesheets().add(
                getClass().getResource(
                        "/com/example/chatdesktop/css/chat.css"
                ).toExternalForm()
        );

        // ============================================================
        // BARRA SUPERIOR
        // ============================================================

        Label marca = new Label("AERO");

        marca.getStyleClass().add("brand-label");

        marca.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        botaoTema = new Button("Escuro");

        botaoTema.getStyleClass().add("btn-light");

        botaoTema.setStyle("-fx-background-radius: 8;");

        botaoTema.setOnAction(evento -> alternarTema());

        Region espacoTopo = new Region();

        HBox.setHgrow(espacoTopo, Priority.ALWAYS);

        HBox barraTopo = new HBox(12, marca, espacoTopo, botaoTema);

        barraTopo.setAlignment(Pos.CENTER_LEFT);

        barraTopo.setPadding(new Insets(20, 24, 20, 24));

        root.setTop(barraTopo);

        // ============================================================
        // ABAS
        // ============================================================

        tabLogin = new Button("Entrar");

        tabRegistro = new Button("Criar conta");

        tabLogin.getStyleClass().addAll("tab-btn", "active");

        tabRegistro.getStyleClass().add("tab-btn");

        HBox.setHgrow(tabLogin, Priority.ALWAYS);

        HBox.setHgrow(tabRegistro, Priority.ALWAYS);

        tabLogin.setMaxWidth(Double.MAX_VALUE);

        tabRegistro.setMaxWidth(Double.MAX_VALUE);

        HBox abas = new HBox(4, tabLogin, tabRegistro);

        abas.getStyleClass().add("tabs-bar");

        tabLogin.setOnAction(evento -> mudarParaAba("login"));

        tabRegistro.setOnAction(evento -> mudarParaAba("registro"));

        // ============================================================
        // PAINEL LOGIN
        // ============================================================

        Label tituloLogin = new Label("Bem-vindo de volta");

        tituloLogin.getStyleClass().add("form-title");

        Label subtituloLogin = new Label("Entre para continuar sua jornada.");

        subtituloLogin.getStyleClass().add("form-subtitle");

        campoLoginEmail = new TextField();

        campoLoginEmail.setPromptText("voce@email.com");

        campoLoginEmail.getStyleClass().add("text-input");

        campoLoginSenha = new CampoSenha("Senha");

        checkLembrar = new CheckBox("Lembrar-me");

        checkLembrar.getStyleClass().add("field-label");

        linkEsqueciSenha = new Hyperlink("Esqueci minha senha");

        linkEsqueciSenha.getStyleClass().add("link-muted");

        Region espacoLogin = new Region();

        HBox.setHgrow(espacoLogin, Priority.ALWAYS);

        HBox linhaExtrasLogin = new HBox(
                checkLembrar, espacoLogin, linkEsqueciSenha
        );

        linhaExtrasLogin.setAlignment(Pos.CENTER_LEFT);

        sucessoLogin = new Label();

        sucessoLogin.getStyleClass().add("success-label");

        sucessoLogin.setVisible(false);

        sucessoLogin.setManaged(false);

        erroLogin = new Label();

        erroLogin.getStyleClass().add("error-label");

        erroLogin.setVisible(false);

        erroLogin.setManaged(false);

        botaoLogin = new Button("Entrar");

        botaoLogin.getStyleClass().add("btn-primary");

        botaoLogin.setMaxWidth(Double.MAX_VALUE);

        botaoLogin.setStyle("-fx-font-weight: bold; -fx-background-radius: 999;");

        linkParaRegistro = new Button("Criar conta");

        linkParaRegistro.getStyleClass().add("link-muted");

        linkParaRegistro.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        linkParaRegistro.setOnAction(evento -> mudarParaAba("registro"));

        HBox rodapeLogin = new HBox(
                4,
                new Label("Não tem uma conta?"),
                linkParaRegistro
        );

        rodapeLogin.setAlignment(Pos.CENTER);

        painelLogin = new VBox(
                12,
                tituloLogin,
                subtituloLogin,
                campoComLabel("E-mail", campoLoginEmail),
                campoComLabel("Senha", campoLoginSenha),
                linhaExtrasLogin,
                sucessoLogin,
                erroLogin,
                botaoLogin,
                rodapeLogin
        );

        // ============================================================
        // PAINEL REGISTRO
        // ============================================================

        Label tituloRegistro = new Label("Criar sua conta");

        tituloRegistro.getStyleClass().add("form-title");

        Label subtituloRegistro = new Label("Comece sua jornada além da Terra.");

        subtituloRegistro.getStyleClass().add("form-subtitle");

        campoRegNome = new TextField();

        campoRegNome.setPromptText("Seu nome");

        campoRegNome.getStyleClass().add("text-input");

        campoRegEmail = new TextField();

        campoRegEmail.setPromptText("voce@email.com");

        campoRegEmail.getStyleClass().add("text-input");

        campoRegSenha = new CampoSenha("Mínimo 8 caracteres");

        campoRegConfirmar = new CampoSenha("Repita a senha");

        checkTermos = new CheckBox("Aceito os termos de uso");

        checkTermos.getStyleClass().add("field-label");

        erroRegistro = new Label();

        erroRegistro.getStyleClass().add("error-label");

        erroRegistro.setVisible(false);

        erroRegistro.setManaged(false);

        botaoRegistrar = new Button("Criar conta");

        botaoRegistrar.getStyleClass().add("btn-primary");

        botaoRegistrar.setMaxWidth(Double.MAX_VALUE);

        botaoRegistrar.setStyle("-fx-font-weight: bold; -fx-background-radius: 999;");

        linkParaLogin = new Button("Entrar");

        linkParaLogin.getStyleClass().add("link-muted");

        linkParaLogin.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

        linkParaLogin.setOnAction(evento -> mudarParaAba("login"));

        HBox rodapeRegistro = new HBox(
                4,
                new Label("Já tem uma conta?"),
                linkParaLogin
        );

        rodapeRegistro.setAlignment(Pos.CENTER);

        painelRegistro = new VBox(
                12,
                tituloRegistro,
                subtituloRegistro,
                campoComLabel("Nome completo", campoRegNome),
                campoComLabel("E-mail", campoRegEmail),
                campoComLabel("Senha", campoRegSenha),
                campoComLabel("Confirmar senha", campoRegConfirmar),
                checkTermos,
                erroRegistro,
                botaoRegistrar,
                rodapeRegistro
        );

        painelRegistro.setVisible(false);

        painelRegistro.setManaged(false);

        // ============================================================
        // CARTÃO
        // ============================================================

        VBox card = new VBox(20, abas, painelLogin, painelRegistro);

        card.getStyleClass().add("auth-card");

        card.setMaxWidth(380);

        card.setPadding(new Insets(28));

        VBox centroWrapper = new VBox(card);

        centroWrapper.setAlignment(Pos.CENTER);

        root.setCenter(centroWrapper);
    }

    // ================================================================
    // AJUDANTES DE LAYOUT
    // ================================================================

    private VBox campoComLabel(String texto, Region campo) {

        Label label = new Label(texto);

        label.getStyleClass().add("field-label");

        VBox caixa = new VBox(6, label, campo);

        return caixa;
    }

    // ================================================================
    // TROCA DE ABA
    // ================================================================

    public void mudarParaAba(String aba) {

        boolean ehLogin = "login".equals(aba);

        painelLogin.setVisible(ehLogin);

        painelLogin.setManaged(ehLogin);

        painelRegistro.setVisible(!ehLogin);

        painelRegistro.setManaged(!ehLogin);

        tabLogin.getStyleClass().remove("active");

        tabRegistro.getStyleClass().remove("active");

        if (ehLogin) {
            tabLogin.getStyleClass().add("active");
        } else {
            tabRegistro.getStyleClass().add("active");
        }

        limparErroLogin();

        limparErroRegistro();
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

        mostrarSucessoLogin("Conta criada! Faça login para continuar.");
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
    // ERROS / SUCESSO
    // ================================================================

    public void mostrarErroLogin(String mensagem) {

        limparSucessoLogin();

        erroLogin.setText(mensagem);

        erroLogin.setVisible(true);

        erroLogin.setManaged(true);
    }

    public void limparErroLogin() {

        erroLogin.setVisible(false);

        erroLogin.setManaged(false);
    }

    public void mostrarSucessoLogin(String mensagem) {

        sucessoLogin.setText(mensagem);

        sucessoLogin.setVisible(true);

        sucessoLogin.setManaged(true);
    }

    public void limparSucessoLogin() {

        sucessoLogin.setVisible(false);

        sucessoLogin.setManaged(false);
    }

    public void mostrarErroRegistro(String mensagem) {

        erroRegistro.setText(mensagem);

        erroRegistro.setVisible(true);

        erroRegistro.setManaged(true);
    }

    public void limparErroRegistro() {

        erroRegistro.setVisible(false);

        erroRegistro.setManaged(false);
    }

    // ================================================================
    // GETTERS
    // ================================================================

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

    // ================================================================
    // CAMPO DE SENHA COM BOTÃO DE MOSTRAR/OCULTAR
    // ================================================================

    public static class CampoSenha extends HBox {

        private final PasswordField oculto = new PasswordField();

        private final TextField visivel = new TextField();

        private final Button alternar = new Button("Mostrar");

        private boolean mostrando = false;

        public CampoSenha(String prompt) {

            super(8);

            oculto.setPromptText(prompt);

            visivel.setPromptText(prompt);

            oculto.getStyleClass().add("text-input");

            visivel.getStyleClass().add("text-input");

            visivel.setManaged(false);

            visivel.setVisible(false);

            HBox.setHgrow(oculto, Priority.ALWAYS);

            HBox.setHgrow(visivel, Priority.ALWAYS);

            StackPane pilha = new StackPane(oculto, visivel);

            HBox.setHgrow(pilha, Priority.ALWAYS);

            alternar.getStyleClass().add("btn-toggle-senha");

            alternar.setOnAction(evento -> alternarVisibilidade());

            getChildren().addAll(pilha, alternar);

            setAlignment(Pos.CENTER_LEFT);
        }

        private void alternarVisibilidade() {

            if (mostrando) {

                oculto.setText(visivel.getText());

                oculto.setManaged(true);

                oculto.setVisible(true);

                visivel.setManaged(false);

                visivel.setVisible(false);

                alternar.setText("Mostrar");

            } else {

                visivel.setText(oculto.getText());

                visivel.setManaged(true);

                visivel.setVisible(true);

                oculto.setManaged(false);

                oculto.setVisible(false);

                alternar.setText("Ocultar");
            }

            mostrando = !mostrando;
        }

        public String getTexto() {
            return mostrando ? visivel.getText() : oculto.getText();
        }

        public void limpar() {
            oculto.clear();
            visivel.clear();
        }
    }
}