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
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application {

    /*
     * ============================================================
     * PALETA
     * ============================================================
     *
     * #2078CF  azul claro
     * #0E4EB2  azul vivo
     * #011F65  azul profundo
     * #020C47  azul marinho
     * #000521  azul quase preto
     *
     */

    private final Random random = new Random();

    @Override
    public void start(Stage stage) {

        // ============================================================
        // TELAS PRINCIPAIS
        // ============================================================

        AuthView authView = new AuthView();

        ChatView chatView = new ChatView();

        new ChatController(chatView);

        new AuthController(
                authView,
                () -> {

                    Scene scene = stage.getScene();

                    animarEntradaChat(
                            scene,
                            chatView.getRoot()
                    );
                }
        );

        // ============================================================
        // SPLASH
        // ============================================================

        StackPane splashRoot =
                criarSplash();

        Scene scene =
                new Scene(
                        splashRoot,
                        1000,
                        650
                );

        stage.setTitle(
                "FAITH IN GOD"
        );

        stage.setMinWidth(850);
        stage.setMinHeight(550);

        stage.setScene(scene);

        stage.show();

        // ============================================================
        // INICIA A SUPER ANIMAÇÃO
        // ============================================================

        executarAnimacaoSplash(
                splashRoot,
                scene,
                authView.getRoot()
        );
    }

    // ================================================================
    // CRIAÇÃO DO SPLASH
    // ================================================================

    private StackPane criarSplash() {

        StackPane root =
                new StackPane();

        root.setStyle(
                "-fx-background-color: #000521;"
        );

        // ============================================================
        // CAMADA DE FUNDO
        // ============================================================

        Pane fundo = new Pane();

        fundo.setMouseTransparent(true);

        fundo.prefWidthProperty()
                .bind(root.widthProperty());

        fundo.prefHeightProperty()
                .bind(root.heightProperty());

        // ============================================================
        // BRILHO CENTRAL GIGANTE
        // ============================================================

        Circle brilhoCentral =
                new Circle(220);

        brilhoCentral.setFill(
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
                                Color.web(
                                        "#2078CF",
                                        0.42
                                )
                        ),

                        new Stop(
                                0.35,
                                Color.web(
                                        "#0E4EB2",
                                        0.25
                                )
                        ),

                        new Stop(
                                0.70,
                                Color.web(
                                        "#011F65",
                                        0.12
                                )
                        ),

                        new Stop(
                                1,
                                Color.TRANSPARENT
                        )
                )
        );

        brilhoCentral.setEffect(
                new GaussianBlur(55)
        );

        brilhoCentral.setOpacity(0);

        // ============================================================
        // SEGUNDO BRILHO
        // ============================================================

        Circle brilhoInterno =
                new Circle(85);

        brilhoInterno.setFill(
                Color.web(
                        "#2078CF",
                        0.20
                )
        );

        brilhoInterno.setEffect(
                new GaussianBlur(35)
        );

        brilhoInterno.setOpacity(0);

        // ============================================================
        // ANÉIS
        // ============================================================

        Circle anelExterno =
                criarAnel(
                        115,
                        "#2078CF",
                        2
                );

        Circle anelMedio =
                criarAnel(
                        88,
                        "#0E4EB2",
                        1.5
                );

        Circle anelInterno =
                criarAnel(
                        61,
                        "#2078CF",
                        1
                );

        anelExterno.setOpacity(0);
        anelMedio.setOpacity(0);
        anelInterno.setOpacity(0);

        // ============================================================
        // NÚCLEO
        // ============================================================

        Circle nucleo =
                new Circle(7);

        nucleo.setFill(
                Color.web("#FFFFFF")
        );

        DropShadow glowNucleo =
                new DropShadow();

        glowNucleo.setColor(
                Color.web("#2078CF")
        );

        glowNucleo.setRadius(30);
        glowNucleo.setSpread(0.65);

        nucleo.setEffect(glowNucleo);

        nucleo.setOpacity(0);
        nucleo.setScaleX(0);
        nucleo.setScaleY(0);

        // ============================================================
        // LINHA HORIZONTAL
        // ============================================================

        Line linhaLuz =
                new Line();

        linhaLuz.setStroke(
                Color.web("#2078CF")
        );

        linhaLuz.setStrokeWidth(2);

        linhaLuz.setOpacity(0);

        linhaLuz.setStartX(-170);
        linhaLuz.setEndX(170);

        DropShadow brilhoLinha =
                new DropShadow();

        brilhoLinha.setColor(
                Color.web("#2078CF")
        );

        brilhoLinha.setRadius(18);
        brilhoLinha.setSpread(0.4);

        linhaLuz.setEffect(
                brilhoLinha
        );

        // ============================================================
        // TÍTULO
        // ============================================================

        Label titulo =
                new Label(
                        "FAITH IN GOD"
                );

        titulo.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 42px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-letter-spacing: 4px;"
        );

        DropShadow tituloGlow =
                new DropShadow();

        tituloGlow.setColor(
                Color.web(
                        "#2078CF",
                        0.75
                )
        );

        tituloGlow.setRadius(22);
        tituloGlow.setSpread(0.15);

        titulo.setEffect(
                tituloGlow
        );

        titulo.setOpacity(0);

        titulo.setScaleX(0.82);
        titulo.setScaleY(0.82);

        titulo.setTranslateY(10);

        // ============================================================
        // SUBTÍTULO
        // ============================================================

        Label subtitulo =
                new Label(
                        "INTELLIGENCE • KNOWLEDGE • FAITH"
                );

        subtitulo.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #8FC9FF;"
        );

        subtitulo.setOpacity(0);
        subtitulo.setTranslateY(55);

        // ============================================================
        // CONTAINER DO LOGO
        // ============================================================

        StackPane logo =
                new StackPane(
                        brilhoCentral,
                        brilhoInterno,
                        anelExterno,
                        anelMedio,
                        anelInterno,
                        nucleo,
                        linhaLuz,
                        titulo,
                        subtitulo
                );

        logo.setAlignment(Pos.CENTER);

        // Guardamos referências para animação.
        logo.getProperties().put(
                "brilhoCentral",
                brilhoCentral
        );

        logo.getProperties().put(
                "brilhoInterno",
                brilhoInterno
        );

        logo.getProperties().put(
                "anelExterno",
                anelExterno
        );

        logo.getProperties().put(
                "anelMedio",
                anelMedio
        );

        logo.getProperties().put(
                "anelInterno",
                anelInterno
        );

        logo.getProperties().put(
                "nucleo",
                nucleo
        );

        logo.getProperties().put(
                "linha",
                linhaLuz
        );

        logo.getProperties().put(
                "titulo",
                titulo
        );

        logo.getProperties().put(
                "subtitulo",
                subtitulo
        );

        // ============================================================
        // PARTÍCULAS
        // ============================================================

        criarParticulas(
                fundo,
                root
        );

        // ============================================================
        // VINHETA
        // ============================================================

        Rectangle vinheta =
                new Rectangle();

        vinheta.widthProperty()
                .bind(root.widthProperty());

        vinheta.heightProperty()
                .bind(root.heightProperty());

        vinheta.setFill(
                new RadialGradient(
                        0,
                        0,
                        0.5,
                        0.5,
                        0.7,
                        true,
                        CycleMethod.NO_CYCLE,

                        new Stop(
                                0,
                                Color.TRANSPARENT
                        ),

                        new Stop(
                                0.65,
                                Color.web(
                                        "#000521",
                                        0.10
                                )
                        ),

                        new Stop(
                                1,
                                Color.web(
                                        "#000000",
                                        0.55
                                )
                        )
                )
        );

        vinheta.setMouseTransparent(true);

        // ============================================================
        // MONTAGEM
        // ============================================================

        root.getChildren().addAll(
                fundo,
                logo,
                vinheta
        );

        root.getProperties().put(
                "logo",
                logo
        );

        return root;
    }

    // ================================================================
    // ANEL
    // ================================================================

    private Circle criarAnel(
            double raio,
            String cor,
            double largura
    ) {

        Circle circle =
                new Circle(raio);

        circle.setFill(
                Color.TRANSPARENT
        );

        circle.setStroke(
                Color.web(
                        cor,
                        0.70
                )
        );

        circle.setStrokeWidth(
                largura
        );

        DropShadow glow =
                new DropShadow();

        glow.setColor(
                Color.web(
                        cor,
                        0.65
                )
        );

        glow.setRadius(14);

        circle.setEffect(glow);

        return circle;
    }

    // ================================================================
    // PARTÍCULAS
    // ================================================================

    private void criarParticulas(
            Pane fundo,
            StackPane root
    ) {

        /*
         * Criamos pequenos pontos azuis.
         *
         * Eles começam invisíveis e se movem lentamente,
         * dando sensação de profundidade.
         */

        for (int i = 0; i < 42; i++) {

            double tamanho =
                    0.7
                            + random.nextDouble()
                            * 2.0;

            Circle particula =
                    new Circle(tamanho);

            if (i % 3 == 0) {

                particula.setFill(
                        Color.web("#2078CF")
                );

            } else if (i % 3 == 1) {

                particula.setFill(
                        Color.web("#0E4EB2")
                );

            } else {

                particula.setFill(
                        Color.web("#FFFFFF")
                );
            }

            particula.setOpacity(
                    0.10
                            + random.nextDouble()
                            * 0.40
            );

            /*
             * Como o tamanho da Scene só fica disponível
             * depois, usamos posições relativas aproximadas.
             */

            double x =
                    40
                            + random.nextDouble()
                            * 920;

            double y =
                    30
                            + random.nextDouble()
                            * 590;

            particula.setLayoutX(x);
            particula.setLayoutY(y);

            fundo.getChildren().add(
                    particula
            );

            // Movimento vertical
            TranslateTransition movimento =
                    new TranslateTransition(
                            Duration.seconds(
                                    3
                                            + random.nextDouble()
                                            * 4
                            ),
                            particula
                    );

            movimento.setFromY(
                    10
            );

            movimento.setToY(
                    -20
                            - random.nextDouble()
                            * 30
            );

            movimento.setAutoReverse(true);

            movimento.setCycleCount(
                    Animation.INDEFINITE
            );

            movimento.setInterpolator(
                    Interpolator.EASE_BOTH
            );

            // Pulsação
            FadeTransition piscar =
                    new FadeTransition(
                            Duration.seconds(
                                    1.5
                                            + random.nextDouble()
                                            * 2
                            ),
                            particula
                    );

            piscar.setFromValue(
                    0.08
            );

            piscar.setToValue(
                    0.60
            );

            piscar.setAutoReverse(true);

            piscar.setCycleCount(
                    Animation.INDEFINITE
            );

            movimento.play();
            piscar.play();
        }
    }

    // ================================================================
    // SUPER ANIMAÇÃO
    // ================================================================

    private void executarAnimacaoSplash(
            StackPane splash,
            Scene scene,
            Parent telaLogin
    ) {

        StackPane logo =
                (StackPane)
                        splash
                                .getProperties()
                                .get("logo");

        Circle brilhoCentral =
                (Circle)
                        logo
                                .getProperties()
                                .get("brilhoCentral");

        Circle brilhoInterno =
                (Circle)
                        logo
                                .getProperties()
                                .get("brilhoInterno");

        Circle anelExterno =
                (Circle)
                        logo
                                .getProperties()
                                .get("anelExterno");

        Circle anelMedio =
                (Circle)
                        logo
                                .getProperties()
                                .get("anelMedio");

        Circle anelInterno =
                (Circle)
                        logo
                                .getProperties()
                                .get("anelInterno");

        Circle nucleo =
                (Circle)
                        logo
                                .getProperties()
                                .get("nucleo");

        Line linha =
                (Line)
                        logo
                                .getProperties()
                                .get("linha");

        Label titulo =
                (Label)
                        logo
                                .getProperties()
                                .get("titulo");

        Label subtitulo =
                (Label)
                        logo
                                .getProperties()
                                .get("subtitulo");

        // ============================================================
        // 1 - FUNDO GANHA VIDA
        // ============================================================

        FadeTransition aparecerBrilho =
                new FadeTransition(
                        Duration.millis(850),
                        brilhoCentral
                );

        aparecerBrilho.setFromValue(0);
        aparecerBrilho.setToValue(1);

        ScaleTransition expandirBrilho =
                new ScaleTransition(
                        Duration.millis(1100),
                        brilhoCentral
                );

        expandirBrilho.setFromX(0.25);
        expandirBrilho.setFromY(0.25);

        expandirBrilho.setToX(1);
        expandirBrilho.setToY(1);

        expandirBrilho.setInterpolator(
                Interpolator.EASE_OUT
        );

        // ============================================================
        // 2 - NÚCLEO
        // ============================================================

        FadeTransition nucleoFade =
                new FadeTransition(
                        Duration.millis(350),
                        nucleo
                );

        nucleoFade.setFromValue(0);
        nucleoFade.setToValue(1);

        ScaleTransition nucleoScale =
                new ScaleTransition(
                        Duration.millis(550),
                        nucleo
                );

        nucleoScale.setFromX(0);
        nucleoScale.setFromY(0);

        nucleoScale.setToX(1);
        nucleoScale.setToY(1);

        nucleoScale.setInterpolator(
                Interpolator.EASE_OUT
        );

        // ============================================================
        // 3 - ANÉIS
        // ============================================================

        ParallelTransition aneis =
                new ParallelTransition(

                        criarEntradaAnel(
                                anelInterno,
                                0.4,
                                1
                        ),

                        criarEntradaAnel(
                                anelMedio,
                                0.25,
                                1
                        ),

                        criarEntradaAnel(
                                anelExterno,
                                0.15,
                                1
                        )
                );

        // Rotação contínua sutil
        RotateTransition girarExterno =
                new RotateTransition(
                        Duration.seconds(7),
                        anelExterno
                );

        girarExterno.setByAngle(360);

        girarExterno.setCycleCount(
                Animation.INDEFINITE
        );

        girarExterno.setInterpolator(
                Interpolator.LINEAR
        );

        RotateTransition girarMedio =
                new RotateTransition(
                        Duration.seconds(5),
                        anelMedio
                );

        girarMedio.setByAngle(-360);

        girarMedio.setCycleCount(
                Animation.INDEFINITE
        );

        girarMedio.setInterpolator(
                Interpolator.LINEAR
        );

        // ============================================================
        // 4 - FLASH INTERNO
        // ============================================================

        FadeTransition flashInterno =
                new FadeTransition(
                        Duration.millis(400),
                        brilhoInterno
                );

        flashInterno.setFromValue(0);
        flashInterno.setToValue(1);

        flashInterno.setAutoReverse(true);
        flashInterno.setCycleCount(2);

        // ============================================================
        // 5 - LINHA DE LUZ
        // ============================================================

        FadeTransition linhaFade =
                new FadeTransition(
                        Duration.millis(300),
                        linha
                );

        linhaFade.setFromValue(0);
        linhaFade.setToValue(0.9);

        ScaleTransition linhaScale =
                new ScaleTransition(
                        Duration.millis(650),
                        linha
                );

        linhaScale.setFromX(0);
        linhaScale.setToX(1);

        linhaScale.setInterpolator(
                Interpolator.EASE_OUT
        );

        ParallelTransition entradaLinha =
                new ParallelTransition(
                        linhaFade,
                        linhaScale
                );

        // ============================================================
        // 6 - TÍTULO
        // ============================================================

        FadeTransition tituloFade =
                new FadeTransition(
                        Duration.millis(650),
                        titulo
                );

        tituloFade.setFromValue(0);
        tituloFade.setToValue(1);

        ScaleTransition tituloScale =
                new ScaleTransition(
                        Duration.millis(800),
                        titulo
                );

        tituloScale.setFromX(0.82);
        tituloScale.setFromY(0.82);

        tituloScale.setToX(1);
        tituloScale.setToY(1);

        tituloScale.setInterpolator(
                Interpolator.EASE_OUT
        );

        TranslateTransition tituloMove =
                new TranslateTransition(
                        Duration.millis(800),
                        titulo
                );

        tituloMove.setFromY(10);
        tituloMove.setToY(0);

        tituloMove.setInterpolator(
                Interpolator.EASE_OUT
        );

        ParallelTransition entradaTitulo =
                new ParallelTransition(
                        tituloFade,
                        tituloScale,
                        tituloMove
                );

        // ============================================================
        // 7 - SUBTÍTULO
        // ============================================================

        FadeTransition subFade =
                new FadeTransition(
                        Duration.millis(600),
                        subtitulo
                );

        subFade.setFromValue(0);
        subFade.setToValue(1);

        TranslateTransition subMove =
                new TranslateTransition(
                        Duration.millis(700),
                        subtitulo
                );

        subMove.setFromY(65);
        subMove.setToY(55);

        subMove.setInterpolator(
                Interpolator.EASE_OUT
        );

        ParallelTransition entradaSub =
                new ParallelTransition(
                        subFade,
                        subMove
                );

        // ============================================================
        // 8 - PULSO FINAL
        // ============================================================

        ScaleTransition pulsoLogo =
                new ScaleTransition(
                        Duration.millis(550),
                        logo
                );

        pulsoLogo.setFromX(1);
        pulsoLogo.setFromY(1);

        pulsoLogo.setToX(1.025);
        pulsoLogo.setToY(1.025);

        pulsoLogo.setAutoReverse(true);
        pulsoLogo.setCycleCount(2);

        pulsoLogo.setInterpolator(
                Interpolator.EASE_BOTH
        );

        // ============================================================
        // SEQUÊNCIA COMPLETA
        // ============================================================

        ParallelTransition primeiraFase =
                new ParallelTransition(
                        aparecerBrilho,
                        expandirBrilho
                );

        ParallelTransition segundaFase =
                new ParallelTransition(
                        nucleoFade,
                        nucleoScale,
                        flashInterno
                );

        PauseTransition pausaCurta1 =
                new PauseTransition(
                        Duration.millis(120)
                );

        PauseTransition pausaCurta2 =
                new PauseTransition(
                        Duration.millis(100)
                );

        PauseTransition mostrarLogo =
                new PauseTransition(
                        Duration.millis(900)
                );

        SequentialTransition sequencia =
                new SequentialTransition(

                        primeiraFase,

                        pausaCurta1,

                        segundaFase,

                        aneis,

                        pausaCurta2,

                        entradaLinha,

                        entradaTitulo,

                        entradaSub,

                        pulsoLogo,

                        mostrarLogo
                );

        // Começa rotações quando anéis aparecem
        aneis.setOnFinished(evento -> {

            girarExterno.play();
            girarMedio.play();
        });

        // ============================================================
        // FINAL CINEMATOGRÁFICO
        // ============================================================

        sequencia.setOnFinished(
                evento -> {

                    executarSaidaSplash(
                            splash,
                            logo,
                            brilhoCentral,
                            scene,
                            telaLogin
                    );
                }
        );

        sequencia.play();
    }

    // ================================================================
    // ENTRADA DOS ANÉIS
    // ================================================================

    private ParallelTransition criarEntradaAnel(
            Circle anel,
            double escalaInicial,
            double escalaFinal
    ) {

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(500),
                        anel
                );

        fade.setFromValue(0);
        fade.setToValue(0.75);

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(750),
                        anel
                );

        scale.setFromX(
                escalaInicial
        );

        scale.setFromY(
                escalaInicial
        );

        scale.setToX(
                escalaFinal
        );

        scale.setToY(
                escalaFinal
        );

        scale.setInterpolator(
                Interpolator.EASE_OUT
        );

        return new ParallelTransition(
                fade,
                scale
        );
    }

    // ================================================================
    // SAÍDA DO SPLASH
    // ================================================================

    private void executarSaidaSplash(
            StackPane splash,
            StackPane logo,
            Circle brilho,
            Scene scene,
            Parent telaLogin
    ) {

        /*
         * O logo primeiro cresce rapidamente.
         *
         * O brilho aumenta junto.
         *
         * Depois toda a tela desaparece.
         */

        ScaleTransition zoomLogo =
                new ScaleTransition(
                        Duration.millis(650),
                        logo
                );

        zoomLogo.setFromX(1);
        zoomLogo.setFromY(1);

        zoomLogo.setToX(1.35);
        zoomLogo.setToY(1.35);

        zoomLogo.setInterpolator(
                Interpolator.EASE_IN
        );

        ScaleTransition explosaoLuz =
                new ScaleTransition(
                        Duration.millis(650),
                        brilho
                );

        explosaoLuz.setToX(2.7);
        explosaoLuz.setToY(2.7);

        explosaoLuz.setInterpolator(
                Interpolator.EASE_IN
        );

        FadeTransition sumirLogo =
                new FadeTransition(
                        Duration.millis(550),
                        logo
                );

        sumirLogo.setFromValue(1);
        sumirLogo.setToValue(0);

        FadeTransition sumirSplash =
                new FadeTransition(
                        Duration.millis(700),
                        splash
                );

        sumirSplash.setFromValue(1);
        sumirSplash.setToValue(0);

        ParallelTransition saida =
                new ParallelTransition(
                        zoomLogo,
                        explosaoLuz,
                        sumirLogo,
                        sumirSplash
                );

        saida.setOnFinished(
                evento -> {

                    mostrarTelaLogin(
                            scene,
                            telaLogin
                    );
                }
        );

        saida.play();
    }

    // ================================================================
    // REVELAÇÃO DO LOGIN
    // ================================================================

    private void mostrarTelaLogin(
            Scene scene,
            Parent telaLogin
    ) {

        telaLogin.setOpacity(0);

        telaLogin.setScaleX(0.97);
        telaLogin.setScaleY(0.97);

        telaLogin.setTranslateY(12);

        scene.setRoot(
                telaLogin
        );

        // Fade
        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(700),
                        telaLogin
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        // Zoom suave
        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(750),
                        telaLogin
                );

        scale.setFromX(0.97);
        scale.setFromY(0.97);

        scale.setToX(1);
        scale.setToY(1);

        scale.setInterpolator(
                Interpolator.EASE_OUT
        );

        // Movimento para cima
        TranslateTransition mover =
                new TranslateTransition(
                        Duration.millis(750),
                        telaLogin
                );

        mover.setFromY(12);
        mover.setToY(0);

        mover.setInterpolator(
                Interpolator.EASE_OUT
        );

        ParallelTransition entrada =
                new ParallelTransition(
                        fade,
                        scale,
                        mover
                );

        entrada.play();
    }

    // ================================================================
    // TRANSIÇÃO LOGIN -> CHAT
    // ================================================================

    private void animarEntradaChat(
            Scene scene,
            Parent chat
    ) {

        Parent atual =
                scene.getRoot();

        FadeTransition saidaLogin =
                new FadeTransition(
                        Duration.millis(300),
                        atual
                );

        saidaLogin.setFromValue(1);
        saidaLogin.setToValue(0);

        saidaLogin.setOnFinished(
                evento -> {

                    chat.setOpacity(0);

                    chat.setScaleX(0.985);
                    chat.setScaleY(0.985);

                    scene.setRoot(chat);

                    FadeTransition fade =
                            new FadeTransition(
                                    Duration.millis(450),
                                    chat
                            );

                    fade.setFromValue(0);
                    fade.setToValue(1);

                    ScaleTransition scale =
                            new ScaleTransition(
                                    Duration.millis(500),
                                    chat
                            );

                    scale.setFromX(0.985);
                    scale.setFromY(0.985);

                    scale.setToX(1);
                    scale.setToY(1);

                    scale.setInterpolator(
                            Interpolator.EASE_OUT
                    );

                    new ParallelTransition(
                            fade,
                            scale
                    ).play();
                }
        );

        saidaLogin.play();
    }

    // ================================================================
    // MAIN
    // ================================================================

    public static void main(String[] args) {

        launch(args);
    }
}