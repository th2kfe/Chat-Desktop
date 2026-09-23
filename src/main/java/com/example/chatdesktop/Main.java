package com.example.chatdesktop;

import com.example.chatdesktop.controller.AuthController;
import com.example.chatdesktop.controller.ChatController;
import com.example.chatdesktop.view.AuthView;
import com.example.chatdesktop.view.ChatView;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    // =========================================================
    // PALETA FAITH
    // =========================================================

    private static final String AZUL_CLARO = "#58DFFF";
    private static final String AZUL = "#2078CF";
    private static final String AZUL_VIVO = "#0E4EB2";
    private static final String AZUL_PROFUNDO = "#011F65";
    private static final String MARINHO = "#020C47";
    private static final String ESCURO = "#00030D";

    private final Random random = new Random();

    // =========================================================
    // START
    // =========================================================

    @Override
    public void start(Stage stage) {

        AuthView authView = new AuthView();
        ChatView chatView = new ChatView();

        new ChatController(chatView);

        new AuthController(
                authView,
                () -> animarEntradaChat(
                        stage.getScene(),
                        chatView.getRoot()
                )
        );

        StackPane splash = criarSplash();

        Scene scene = new Scene(
                splash,
                1100,
                700
        );

        stage.setTitle("FAITH IN GOD");

        stage.setMinWidth(900);
        stage.setMinHeight(580);

        stage.setScene(scene);
        stage.show();

        executarSuperSplash(
                splash,
                scene,
                authView.getRoot()
        );
    }

    // =========================================================
    // SPLASH
    // =========================================================

    private StackPane criarSplash() {

        StackPane root = new StackPane();

        root.setStyle(
                "-fx-background-color: " + ESCURO + ";"
        );

        // Fundo
        Pane fundo = new Pane();

        fundo.prefWidthProperty()
                .bind(root.widthProperty());

        fundo.prefHeightProperty()
                .bind(root.heightProperty());

        fundo.setMouseTransparent(true);

        // Estrelas
        criarCampoEstelar(
                fundo,
                root,
                95
        );

        // Glow gigante
        Circle glowGigante = new Circle(330);

        glowGigante.setFill(
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
                                        AZUL,
                                        0.30
                                )
                        ),

                        new Stop(
                                0.25,
                                Color.web(
                                        AZUL_VIVO,
                                        0.17
                                )
                        ),

                        new Stop(
                                0.55,
                                Color.web(
                                        AZUL_PROFUNDO,
                                        0.10
                                )
                        ),

                        new Stop(
                                1,
                                Color.TRANSPARENT
                        )
                )
        );

        glowGigante.setEffect(
                new GaussianBlur(80)
        );

        glowGigante.setOpacity(0);
        glowGigante.setScaleX(0.15);
        glowGigante.setScaleY(0.15);

        // =====================================================
        // FRASE INICIAL
        // =====================================================

        Label fraseInicial = new Label(
                "ALGO MAIOR ESTÁ POR VIR..."
        );

        fraseInicial.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 11px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #A8BED6;"
        );

        fraseInicial.setOpacity(0);

        // =====================================================
        // CAMADA DAS PARTÍCULAS DE ENERGIA
        // =====================================================

        Pane particulasEnergia = new Pane();

        particulasEnergia.setMouseTransparent(true);

        particulasEnergia.prefWidthProperty()
                .bind(root.widthProperty());

        particulasEnergia.prefHeightProperty()
                .bind(root.heightProperty());

        List<Circle> particulas =
                criarParticulasEnergia(
                        particulasEnergia,
                        root
                );

        // =====================================================
        // SISTEMA CENTRAL
        // =====================================================

        StackPane sistema =
                new StackPane();

        sistema.setAlignment(Pos.CENTER);

        // Glow interno
        Circle glowInterno =
                new Circle(130);

        glowInterno.setFill(
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
                                        "#FFFFFF",
                                        0.40
                                )
                        ),

                        new Stop(
                                0.10,
                                Color.web(
                                        AZUL_CLARO,
                                        0.38
                                )
                        ),

                        new Stop(
                                0.35,
                                Color.web(
                                        AZUL,
                                        0.20
                                )
                        ),

                        new Stop(
                                1,
                                Color.TRANSPARENT
                        )
                )
        );

        glowInterno.setEffect(
                new GaussianBlur(35)
        );

        glowInterno.setOpacity(0);

        // =====================================================
        // ANÉIS
        // =====================================================

        Circle anel1 =
                criarAnel(
                        72,
                        AZUL_CLARO,
                        1.4
                );

        Circle anel2 =
                criarAnel(
                        104,
                        AZUL,
                        1.2
                );

        Circle anel3 =
                criarAnel(
                        138,
                        "#45BFFF",
                        1
                );

        Circle anel4 =
                criarAnel(
                        175,
                        AZUL_VIVO,
                        0.8
                );

        Circle anel5 =
                criarAnel(
                        210,
                        "#167FD5",
                        0.65
                );

        Circle[] aneis = {
                anel1,
                anel2,
                anel3,
                anel4,
                anel5
        };

        for (Circle anel : aneis) {

            anel.setOpacity(0);

            anel.setScaleX(0.15);
            anel.setScaleY(0.15);
        }

        // =====================================================
        // ARCOS HOLOGRÁFICOS
        // =====================================================

        Arc arco1 =
                criarArco(
                        120,
                        120,
                        15,
                        120,
                        AZUL_CLARO,
                        2
                );

        Arc arco2 =
                criarArco(
                        155,
                        155,
                        190,
                        95,
                        AZUL,
                        1.7
                );

        Arc arco3 =
                criarArco(
                        195,
                        195,
                        70,
                        70,
                        "#4DDCFF",
                        1.4
                );

        arco1.setOpacity(0);
        arco2.setOpacity(0);
        arco3.setOpacity(0);

        // =====================================================
        // LINHAS DE ENERGIA
        // =====================================================

        Line vertical = new Line(
                0,
                -260,
                0,
                260
        );

        vertical.setStroke(
                Color.web(
                        AZUL_CLARO,
                        0.80
                )
        );

        vertical.setStrokeWidth(1);

        vertical.setOpacity(0);

        vertical.setEffect(
                criarGlow(
                        AZUL_CLARO,
                        16,
                        0.6
                )
        );

        Line horizontal = new Line(
                -280,
                0,
                280,
                0
        );

        horizontal.setStroke(
                Color.web(
                        AZUL,
                        0.70
                )
        );

        horizontal.setStrokeWidth(1);

        horizontal.setOpacity(0);

        horizontal.setEffect(
                criarGlow(
                        AZUL,
                        18,
                        0.6
                )
        );

        // =====================================================
        // NÚCLEO
        // =====================================================

        Circle nucleoGlow =
                new Circle(32);

        nucleoGlow.setFill(
                Color.web(
                        AZUL_CLARO,
                        0.18
                )
        );

        nucleoGlow.setEffect(
                new GaussianBlur(18)
        );

        nucleoGlow.setOpacity(0);

        Circle nucleo =
                new Circle(6);

        nucleo.setFill(Color.WHITE);

        nucleo.setEffect(
                criarGlow(
                        AZUL_CLARO,
                        35,
                        0.9
                )
        );

        nucleo.setOpacity(0);
        nucleo.setScaleX(0);
        nucleo.setScaleY(0);

        // =====================================================
        // ONDA DE CHOQUE
        // =====================================================

        Circle onda =
                criarAnel(
                        38,
                        "#A7F5FF",
                        2
                );

        onda.setOpacity(0);
        onda.setScaleX(0.3);
        onda.setScaleY(0.3);

        // =====================================================
        // SÍMBOLO
        // =====================================================

        Label simbolo =
                new Label("✦");

        simbolo.setStyle(
                "-fx-font-family: 'Segoe UI Symbol';" +
                        "-fx-font-size: 112px;" +
                        "-fx-text-fill: white;"
        );

        simbolo.setEffect(
                criarGlow(
                        AZUL_CLARO,
                        38,
                        0.85
                )
        );

        simbolo.setOpacity(0);
        simbolo.setScaleX(0.15);
        simbolo.setScaleY(0.15);

        // =====================================================
        // TEXTO FINAL
        // =====================================================

        Label titulo =
                new Label(
                        "FAITH IN GOD"
                );

        titulo.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 44px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;"
        );

        titulo.setEffect(
                criarGlow(
                        AZUL,
                        25,
                        0.65
                )
        );

        titulo.setOpacity(0);
        titulo.setTranslateY(112);
        titulo.setScaleX(0.88);
        titulo.setScaleY(0.88);

        Label subtitulo =
                new Label(
                        "INTELLIGENCE  •  KNOWLEDGE  •  FAITH"
                );

        subtitulo.setStyle(
                "-fx-font-family: 'Segoe UI';" +
                        "-fx-font-size: 10px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #7FDFFF;"
        );

        subtitulo.setOpacity(0);
        subtitulo.setTranslateY(153);

        // Linha brilhante que atravessa título
        Rectangle scanner =
                new Rectangle(
                        3,
                        90
                );

        scanner.setFill(Color.WHITE);

        scanner.setEffect(
                criarGlow(
                        "#7DEBFF",
                        28,
                        0.95
                )
        );

        scanner.setOpacity(0);
        scanner.setTranslateY(118);
        scanner.setTranslateX(-250);

        sistema.getChildren().addAll(
                glowGigante,
                glowInterno,

                anel5,
                anel4,
                anel3,
                anel2,
                anel1,

                arco1,
                arco2,
                arco3,

                vertical,
                horizontal,

                onda,
                nucleoGlow,
                nucleo,

                simbolo,
                titulo,
                subtitulo,
                scanner
        );

        // =====================================================
        // VINHETA
        // =====================================================

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
                        0.78,
                        true,
                        CycleMethod.NO_CYCLE,

                        new Stop(
                                0,
                                Color.TRANSPARENT
                        ),

                        new Stop(
                                0.58,
                                Color.web(
                                        "#00030D",
                                        0.08
                                )
                        ),

                        new Stop(
                                1,
                                Color.web(
                                        "#000000",
                                        0.82
                                )
                        )
                )
        );

        vinheta.setMouseTransparent(true);

        root.getChildren().addAll(
                fundo,
                particulasEnergia,
                sistema,
                fraseInicial,
                vinheta
        );

        // =====================================================
        // REFERÊNCIAS
        // =====================================================

        root.getProperties().put(
                "frase",
                fraseInicial
        );

        root.getProperties().put(
                "sistema",
                sistema
        );

        root.getProperties().put(
                "particulas",
                particulas
        );

        root.getProperties().put(
                "glowGigante",
                glowGigante
        );

        root.getProperties().put(
                "glowInterno",
                glowInterno
        );

        root.getProperties().put(
                "aneis",
                aneis
        );

        root.getProperties().put(
                "arco1",
                arco1
        );

        root.getProperties().put(
                "arco2",
                arco2
        );

        root.getProperties().put(
                "arco3",
                arco3
        );

        root.getProperties().put(
                "vertical",
                vertical
        );

        root.getProperties().put(
                "horizontal",
                horizontal
        );

        root.getProperties().put(
                "nucleo",
                nucleo
        );

        root.getProperties().put(
                "nucleoGlow",
                nucleoGlow
        );

        root.getProperties().put(
                "onda",
                onda
        );

        root.getProperties().put(
                "simbolo",
                simbolo
        );

        root.getProperties().put(
                "titulo",
                titulo
        );

        root.getProperties().put(
                "subtitulo",
                subtitulo
        );

        root.getProperties().put(
                "scanner",
                scanner
        );

        return root;
    }

    // =========================================================
    // CAMPO ESTELAR
    // =========================================================

    private void criarCampoEstelar(
            Pane pane,
            StackPane root,
            int quantidade
    ) {

        for (int i = 0; i < quantidade; i++) {

            double tamanho =
                    0.35
                            + random.nextDouble()
                            * 1.5;

            Circle estrela =
                    new Circle(tamanho);

            if (i % 7 == 0) {

                estrela.setFill(
                        Color.web("#5BDFFF")
                );

            } else if (i % 4 == 0) {

                estrela.setFill(
                        Color.web("#2078CF")
                );

            } else {

                estrela.setFill(
                        Color.WHITE
                );
            }

            estrela.setOpacity(
                    0.06
                            + random.nextDouble()
                            * 0.35
            );

            double x =
                    random.nextDouble();

            double y =
                    random.nextDouble();

            estrela.layoutXProperty().bind(
                    root.widthProperty()
                            .multiply(x)
            );

            estrela.layoutYProperty().bind(
                    root.heightProperty()
                            .multiply(y)
            );

            pane.getChildren().add(
                    estrela
            );

            FadeTransition piscar =
                    new FadeTransition(
                            Duration.seconds(
                                    1.2
                                            + random.nextDouble()
                                            * 3
                            ),
                            estrela
                    );

            piscar.setFromValue(0.05);
            piscar.setToValue(
                    0.30
                            + random.nextDouble()
                            * 0.50
            );

            piscar.setAutoReverse(true);

            piscar.setCycleCount(
                    Animation.INDEFINITE
            );

            piscar.play();
        }
    }

    // =========================================================
    // PARTÍCULAS DE ENERGIA
    // =========================================================

    private List<Circle> criarParticulasEnergia(
            Pane pane,
            StackPane root
    ) {

        List<Circle> lista =
                new ArrayList<>();

        for (int i = 0; i < 72; i++) {

            Circle particula =
                    new Circle(
                            0.7
                                    + random.nextDouble()
                                    * 1.8
                    );

            if (i % 4 == 0) {

                particula.setFill(
                        Color.WHITE
                );

            } else if (i % 2 == 0) {

                particula.setFill(
                        Color.web(
                                AZUL_CLARO
                        )
                );

            } else {

                particula.setFill(
                        Color.web(AZUL)
                );
            }

            particula.setOpacity(0);

            /*
             * Posição em volta do centro.
             */
            double angulo =
                    random.nextDouble()
                            * Math.PI
                            * 2;

            double distancia =
                    170
                            + random.nextDouble()
                            * 420;

            double x =
                    Math.cos(angulo)
                            * distancia;

            double y =
                    Math.sin(angulo)
                            * distancia;

            particula.setTranslateX(x);
            particula.setTranslateY(y);

            if (i % 8 == 0) {

                particula.setEffect(
                        criarGlow(
                                AZUL_CLARO,
                                10,
                                0.5
                        )
                );
            }

            pane.getChildren().add(
                    particula
            );

            /*
             * Pane ocupa a tela toda.
             * Mantemos o centro da partícula preso
             * ao centro da tela.
             */
            particula.layoutXProperty().bind(
                    root.widthProperty()
                            .divide(2)
            );

            particula.layoutYProperty().bind(
                    root.heightProperty()
                            .divide(2)
            );

            lista.add(particula);
        }

        return lista;
    }

    // =========================================================
    // ANEL
    // =========================================================

    private Circle criarAnel(
            double raio,
            String cor,
            double largura
    ) {

        Circle anel =
                new Circle(raio);

        anel.setFill(
                Color.TRANSPARENT
        );

        anel.setStroke(
                Color.web(
                        cor,
                        0.75
                )
        );

        anel.setStrokeWidth(
                largura
        );

        anel.setEffect(
                criarGlow(
                        cor,
                        14,
                        0.45
                )
        );

        /*
         * Tracejado deixa o anel
         * mais holográfico.
         */
        anel.getStrokeDashArray()
                .addAll(
                        14.0,
                        8.0,
                        3.0,
                        8.0
                );

        return anel;
    }

    // =========================================================
    // ARCO
    // =========================================================

    private Arc criarArco(
            double raioX,
            double raioY,
            double inicio,
            double comprimento,
            String cor,
            double largura
    ) {

        Arc arco =
                new Arc(
                        0,
                        0,
                        raioX,
                        raioY,
                        inicio,
                        comprimento
                );

        arco.setType(
                ArcType.OPEN
        );

        arco.setFill(
                Color.TRANSPARENT
        );

        arco.setStroke(
                Color.web(cor)
        );

        arco.setStrokeWidth(
                largura
        );

        arco.setEffect(
                criarGlow(
                        cor,
                        17,
                        0.6
                )
        );

        return arco;
    }

    // =========================================================
    // GLOW
    // =========================================================

    private DropShadow criarGlow(
            String cor,
            double raio,
            double spread
    ) {

        DropShadow glow =
                new DropShadow();

        glow.setColor(
                Color.web(cor)
        );

        glow.setRadius(raio);
        glow.setSpread(spread);

        return glow;
    }

    // =========================================================
    // SUPER SPLASH
    // =========================================================

    @SuppressWarnings("unchecked")
    private void executarSuperSplash(
            StackPane splash,
            Scene scene,
            Parent telaLogin
    ) {

        Label frase =
                (Label)
                        splash.getProperties()
                                .get("frase");

        StackPane sistema =
                (StackPane)
                        splash.getProperties()
                                .get("sistema");

        List<Circle> particulas =
                (List<Circle>)
                        splash.getProperties()
                                .get("particulas");

        Circle glowGigante =
                (Circle)
                        splash.getProperties()
                                .get("glowGigante");

        Circle glowInterno =
                (Circle)
                        splash.getProperties()
                                .get("glowInterno");

        Circle[] aneis =
                (Circle[])
                        splash.getProperties()
                                .get("aneis");

        Arc arco1 =
                (Arc)
                        splash.getProperties()
                                .get("arco1");

        Arc arco2 =
                (Arc)
                        splash.getProperties()
                                .get("arco2");

        Arc arco3 =
                (Arc)
                        splash.getProperties()
                                .get("arco3");

        Line vertical =
                (Line)
                        splash.getProperties()
                                .get("vertical");

        Line horizontal =
                (Line)
                        splash.getProperties()
                                .get("horizontal");

        Circle nucleo =
                (Circle)
                        splash.getProperties()
                                .get("nucleo");

        Circle nucleoGlow =
                (Circle)
                        splash.getProperties()
                                .get("nucleoGlow");

        Circle onda =
                (Circle)
                        splash.getProperties()
                                .get("onda");

        Label simbolo =
                (Label)
                        splash.getProperties()
                                .get("simbolo");

        Label titulo =
                (Label)
                        splash.getProperties()
                                .get("titulo");

        Label subtitulo =
                (Label)
                        splash.getProperties()
                                .get("subtitulo");

        Rectangle scanner =
                (Rectangle)
                        splash.getProperties()
                                .get("scanner");

        // =====================================================
        // FASE 0 - FRASE
        // =====================================================

        FadeTransition fraseEntrar =
                new FadeTransition(
                        Duration.millis(650),
                        frase
                );

        fraseEntrar.setFromValue(0);
        fraseEntrar.setToValue(0.85);

        FadeTransition fraseSair =
                new FadeTransition(
                        Duration.millis(350),
                        frase
                );

        fraseSair.setFromValue(0.85);
        fraseSair.setToValue(0);

        // =====================================================
        // FASE 1 - PARTÍCULAS
        // =====================================================

        ParallelTransition convergencia =
                new ParallelTransition();

        for (Circle p : particulas) {

            FadeTransition fade =
                    new FadeTransition(
                            Duration.millis(
                                    350
                                            + random.nextInt(450)
                            ),
                            p
                    );

            fade.setFromValue(0);
            fade.setToValue(
                    0.35
                            + random.nextDouble()
                            * 0.65
            );

            TranslateTransition mover =
                    new TranslateTransition(
                            Duration.millis(
                                    900
                                            + random.nextInt(550)
                            ),
                            p
                    );

            mover.setToX(
                    p.getTranslateX()
                            * 0.10
            );

            mover.setToY(
                    p.getTranslateY()
                            * 0.10
            );

            mover.setInterpolator(
                    Interpolator.EASE_IN
            );

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(1100),
                            p
                    );

            scale.setToX(1.7);
            scale.setToY(1.7);

            convergencia.getChildren()
                    .add(
                            new ParallelTransition(
                                    fade,
                                    mover,
                                    scale
                            )
                    );
        }

        // Glow surge junto
        FadeTransition glowFade =
                new FadeTransition(
                        Duration.millis(950),
                        glowGigante
                );

        glowFade.setToValue(1);

        ScaleTransition glowScale =
                new ScaleTransition(
                        Duration.millis(1100),
                        glowGigante
                );

        glowScale.setToX(1);
        glowScale.setToY(1);

        glowScale.setInterpolator(
                Interpolator.EASE_OUT
        );

        ParallelTransition energiaInicial =
                new ParallelTransition(
                        convergencia,
                        glowFade,
                        glowScale
                );

        // =====================================================
        // FASE 2 - NÚCLEO
        // =====================================================

        FadeTransition nucleoFade =
                new FadeTransition(
                        Duration.millis(280),
                        nucleo
                );

        nucleoFade.setToValue(1);

        ScaleTransition nucleoScale =
                new ScaleTransition(
                        Duration.millis(500),
                        nucleo
                );

        nucleoScale.setToX(1);
        nucleoScale.setToY(1);

        nucleoScale.setInterpolator(
                Interpolator.EASE_OUT
        );

        FadeTransition nucleoGlowFade =
                new FadeTransition(
                        Duration.millis(450),
                        nucleoGlow
                );

        nucleoGlowFade.setToValue(1);

        ScaleTransition nucleoGlowScale =
                new ScaleTransition(
                        Duration.millis(600),
                        nucleoGlow
                );

        nucleoGlowScale.setFromX(0.2);
        nucleoGlowScale.setFromY(0.2);

        nucleoGlowScale.setToX(2.2);
        nucleoGlowScale.setToY(2.2);

        ParallelTransition nascerNucleo =
                new ParallelTransition(
                        nucleoFade,
                        nucleoScale,
                        nucleoGlowFade,
                        nucleoGlowScale
                );

        // =====================================================
        // FASE 3 - ANÉIS
        // =====================================================

        ParallelTransition nascerAneis =
                new ParallelTransition();

        for (int i = 0; i < aneis.length; i++) {

            Circle anel =
                    aneis[i];

            FadeTransition fade =
                    new FadeTransition(
                            Duration.millis(
                                    350 + i * 60
                            ),
                            anel
                    );

            fade.setToValue(
                    0.30
                            + (aneis.length - i)
                            * 0.08
            );

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(
                                    650 + i * 80
                            ),
                            anel
                    );

            scale.setToX(1);
            scale.setToY(1);

            scale.setInterpolator(
                    Interpolator.EASE_OUT
            );

            nascerAneis
                    .getChildren()
                    .add(
                            new ParallelTransition(
                                    fade,
                                    scale
                            )
                    );
        }

        // Arcos
        FadeTransition arcoFade1 =
                fadePara(
                        arco1,
                        0.9,
                        500
                );

        FadeTransition arcoFade2 =
                fadePara(
                        arco2,
                        0.75,
                        550
                );

        FadeTransition arcoFade3 =
                fadePara(
                        arco3,
                        0.65,
                        600
                );

        nascerAneis.getChildren()
                .addAll(
                        arcoFade1,
                        arcoFade2,
                        arcoFade3
                );

        // =====================================================
        // ROTAÇÃO CONTÍNUA
        // =====================================================

        RotateTransition rot1 =
                rotacao(
                        aneis[0],
                        5.0,
                        360
                );

        RotateTransition rot2 =
                rotacao(
                        aneis[1],
                        7.0,
                        -360
                );

        RotateTransition rot3 =
                rotacao(
                        aneis[2],
                        9.0,
                        360
                );

        RotateTransition rot4 =
                rotacao(
                        aneis[3],
                        12.0,
                        -360
                );

        RotateTransition rot5 =
                rotacao(
                        aneis[4],
                        15.0,
                        360
                );

        RotateTransition arcoRot1 =
                rotacao(
                        arco1,
                        4.0,
                        360
                );

        RotateTransition arcoRot2 =
                rotacao(
                        arco2,
                        6.0,
                        -360
                );

        RotateTransition arcoRot3 =
                rotacao(
                        arco3,
                        8.0,
                        360
                );

        nascerAneis.setOnFinished(e -> {

            rot1.play();
            rot2.play();
            rot3.play();
            rot4.play();
            rot5.play();

            arcoRot1.play();
            arcoRot2.play();
            arcoRot3.play();
        });

        // =====================================================
        // FASE 4 - CRUZ DE ENERGIA
        // =====================================================

        vertical.setScaleY(0);
        horizontal.setScaleX(0);

        FadeTransition verticalFade =
                fadePara(
                        vertical,
                        0.85,
                        250
                );

        ScaleTransition verticalScale =
                new ScaleTransition(
                        Duration.millis(550),
                        vertical
                );

        verticalScale.setToY(1);

        FadeTransition horizontalFade =
                fadePara(
                        horizontal,
                        0.75,
                        250
                );

        ScaleTransition horizontalScale =
                new ScaleTransition(
                        Duration.millis(550),
                        horizontal
                );

        horizontalScale.setToX(1);

        ParallelTransition linhas =
                new ParallelTransition(
                        verticalFade,
                        verticalScale,
                        horizontalFade,
                        horizontalScale
                );

        // =====================================================
        // FASE 5 - EXPLOSÃO / ONDA
        // =====================================================

        FadeTransition ondaFade =
                new FadeTransition(
                        Duration.millis(600),
                        onda
                );

        ondaFade.setFromValue(0.95);
        ondaFade.setToValue(0);

        ScaleTransition ondaScale =
                new ScaleTransition(
                        Duration.millis(700),
                        onda
                );

        ondaScale.setFromX(0.3);
        ondaScale.setFromY(0.3);

        ondaScale.setToX(8.5);
        ondaScale.setToY(8.5);

        ondaScale.setInterpolator(
                Interpolator.EASE_OUT
        );

        ScaleTransition flashNucleo =
                new ScaleTransition(
                        Duration.millis(320),
                        nucleoGlow
                );

        flashNucleo.setToX(5);
        flashNucleo.setToY(5);

        flashNucleo.setAutoReverse(true);
        flashNucleo.setCycleCount(2);

        ParallelTransition explosao =
                new ParallelTransition(
                        ondaFade,
                        ondaScale,
                        flashNucleo
                );

        // =====================================================
        // FASE 6 - SÍMBOLO
        // =====================================================

        FadeTransition simboloFade =
                new FadeTransition(
                        Duration.millis(550),
                        simbolo
                );

        simboloFade.setToValue(1);

        ScaleTransition simboloScale =
                new ScaleTransition(
                        Duration.millis(700),
                        simbolo
                );

        simboloScale.setToX(1);
        simboloScale.setToY(1);

        simboloScale.setInterpolator(
                Interpolator.EASE_OUT
        );

        RotateTransition simboloRotate =
                new RotateTransition(
                        Duration.millis(750),
                        simbolo
                );

        simboloRotate.setFromAngle(-35);
        simboloRotate.setToAngle(0);

        ParallelTransition formarSimbolo =
                new ParallelTransition(
                        simboloFade,
                        simboloScale,
                        simboloRotate
                );

        // =====================================================
        // FASE 7 - TÍTULO
        // =====================================================

        FadeTransition tituloFade =
                new FadeTransition(
                        Duration.millis(650),
                        titulo
                );

        tituloFade.setToValue(1);

        ScaleTransition tituloScale =
                new ScaleTransition(
                        Duration.millis(750),
                        titulo
                );

        tituloScale.setToX(1);
        tituloScale.setToY(1);

        TranslateTransition tituloMove =
                new TranslateTransition(
                        Duration.millis(750),
                        titulo
                );

        tituloMove.setFromY(125);
        tituloMove.setToY(112);

        tituloMove.setInterpolator(
                Interpolator.EASE_OUT
        );

        ParallelTransition aparecerTitulo =
                new ParallelTransition(
                        tituloFade,
                        tituloScale,
                        tituloMove
                );

        // Subtítulo
        FadeTransition subFade =
                new FadeTransition(
                        Duration.millis(600),
                        subtitulo
                );

        subFade.setToValue(1);

        TranslateTransition subMove =
                new TranslateTransition(
                        Duration.millis(650),
                        subtitulo
                );

        subMove.setFromY(165);
        subMove.setToY(153);

        ParallelTransition aparecerSub =
                new ParallelTransition(
                        subFade,
                        subMove
                );

        // =====================================================
        // SCANNER
        // =====================================================

        FadeTransition scannerIn =
                new FadeTransition(
                        Duration.millis(120),
                        scanner
                );

        scannerIn.setToValue(0.85);

        TranslateTransition scannerMove =
                new TranslateTransition(
                        Duration.millis(650),
                        scanner
                );

        scannerMove.setFromX(-260);
        scannerMove.setToX(260);

        FadeTransition scannerOut =
                new FadeTransition(
                        Duration.millis(160),
                        scanner
                );

        scannerOut.setToValue(0);

        SequentialTransition scannerAnim =
                new SequentialTransition(
                        scannerIn,
                        scannerMove,
                        scannerOut
                );

        // =====================================================
        // SEQUÊNCIA PRINCIPAL
        // =====================================================

        SequentialTransition sequencia =
                new SequentialTransition(

                        // Tela preta
                        new PauseTransition(
                                Duration.millis(220)
                        ),

                        // Frase
                        fraseEntrar,

                        new PauseTransition(
                                Duration.millis(420)
                        ),

                        fraseSair,

                        // Partículas
                        energiaInicial,

                        // Núcleo
                        nascerNucleo,

                        // Anéis
                        nascerAneis,

                        // Linhas
                        linhas,

                        // Pulso
                        explosao,

                        // Símbolo
                        formarSimbolo,

                        // Título
                        aparecerTitulo,

                        aparecerSub,

                        // Reflexo
                        scannerAnim,

                        // Tempo para apreciar
                        new PauseTransition(
                                Duration.millis(750)
                        )
                );

        sequencia.setOnFinished(e ->
                executarWarpFinal(
                        splash,
                        sistema,
                        particulas,
                        glowGigante,
                        scene,
                        telaLogin
                )
        );

        sequencia.play();
    }

    // =========================================================
    // FADE AUXILIAR
    // =========================================================

    private FadeTransition fadePara(
            Node node,
            double opacity,
            double millis
    ) {

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(millis),
                        node
                );

        fade.setToValue(opacity);

        return fade;
    }

    // =========================================================
    // ROTAÇÃO AUXILIAR
    // =========================================================

    private RotateTransition rotacao(
            Node node,
            double segundos,
            double graus
    ) {

        RotateTransition rotate =
                new RotateTransition(
                        Duration.seconds(segundos),
                        node
                );

        rotate.setByAngle(graus);

        rotate.setCycleCount(
                Animation.INDEFINITE
        );

        rotate.setInterpolator(
                Interpolator.LINEAR
        );

        return rotate;
    }

    // =========================================================
    // WARP FINAL
    // =========================================================

    private void executarWarpFinal(
            StackPane splash,
            StackPane sistema,
            List<Circle> particulas,
            Circle glow,
            Scene scene,
            Parent login
    ) {

        /*
         * Nesta parte as partículas "voam"
         * em direção à câmera.
         */

        ParallelTransition warpParticulas =
                new ParallelTransition();

        for (Circle p : particulas) {

            double x =
                    p.getTranslateX();

            double y =
                    p.getTranslateY();

            /*
             * Como elas já estão próximas do centro,
             * usamos a direção original e jogamos
             * tudo para fora da tela.
             */
            if (Math.abs(x) < 3) {
                x = random.nextBoolean()
                        ? 5
                        : -5;
            }

            if (Math.abs(y) < 3) {
                y = random.nextBoolean()
                        ? 5
                        : -5;
            }

            TranslateTransition mover =
                    new TranslateTransition(
                            Duration.millis(
                                    480
                                            + random.nextInt(220)
                            ),
                            p
                    );

            mover.setToX(
                    x * 18
            );

            mover.setToY(
                    y * 18
            );

            mover.setInterpolator(
                    Interpolator.EASE_IN
            );

            ScaleTransition crescer =
                    new ScaleTransition(
                            Duration.millis(600),
                            p
                    );

            crescer.setToX(4.5);
            crescer.setToY(4.5);

            FadeTransition desaparecer =
                    new FadeTransition(
                            Duration.millis(620),
                            p
                    );

            desaparecer.setToValue(0);

            warpParticulas
                    .getChildren()
                    .add(
                            new ParallelTransition(
                                    mover,
                                    crescer,
                                    desaparecer
                            )
                    );
        }

        // Sistema central vem na direção da câmera
        ScaleTransition zoomSistema =
                new ScaleTransition(
                        Duration.millis(700),
                        sistema
                );

        zoomSistema.setFromX(1);
        zoomSistema.setFromY(1);

        zoomSistema.setToX(3.8);
        zoomSistema.setToY(3.8);

        zoomSistema.setInterpolator(
                Interpolator.EASE_IN
        );

        FadeTransition sistemaFade =
                new FadeTransition(
                        Duration.millis(650),
                        sistema
                );

        sistemaFade.setFromValue(1);
        sistemaFade.setToValue(0);

        // Flash final
        ScaleTransition flash =
                new ScaleTransition(
                        Duration.millis(650),
                        glow
                );

        flash.setToX(7);
        flash.setToY(7);

        FadeTransition splashFade =
                new FadeTransition(
                        Duration.millis(700),
                        splash
                );

        splashFade.setFromValue(1);
        splashFade.setToValue(0);

        ParallelTransition warp =
                new ParallelTransition(
                        warpParticulas,
                        zoomSistema,
                        sistemaFade,
                        flash,
                        splashFade
                );

        warp.setOnFinished(e ->
                revelarLogin(
                        scene,
                        login
                )
        );

        warp.play();
    }

    // =========================================================
    // LOGIN APARECE
    // =========================================================

    private void revelarLogin(
            Scene scene,
            Parent login
    ) {

        login.setOpacity(0);

        login.setScaleX(1.055);
        login.setScaleY(1.055);

        login.setTranslateY(8);

        /*
         * Um blur inicial dá sensação de
         * câmera entrando em foco.
         */
        GaussianBlur blur =
                new GaussianBlur(16);

        login.setEffect(blur);

        scene.setRoot(login);

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(850),
                        login
                );

        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale =
                new ScaleTransition(
                        Duration.millis(900),
                        login
                );

        scale.setFromX(1.055);
        scale.setFromY(1.055);

        scale.setToX(1);
        scale.setToY(1);

        scale.setInterpolator(
                Interpolator.EASE_OUT
        );

        TranslateTransition mover =
                new TranslateTransition(
                        Duration.millis(900),
                        login
                );

        mover.setFromY(8);
        mover.setToY(0);

        mover.setInterpolator(
                Interpolator.EASE_OUT
        );

        Timeline foco =
                new Timeline(

                        new KeyFrame(
                                Duration.ZERO,
                                new KeyValue(
                                        blur.radiusProperty(),
                                        16
                                )
                        ),

                        new KeyFrame(
                                Duration.millis(900),
                                new KeyValue(
                                        blur.radiusProperty(),
                                        0,
                                        Interpolator.EASE_OUT
                                )
                        )
                );

        ParallelTransition entrada =
                new ParallelTransition(
                        fade,
                        scale,
                        mover
                );

        entrada.setOnFinished(e ->
                login.setEffect(null)
        );

        foco.play();
        entrada.play();
    }

    // =========================================================
    // LOGIN -> CHAT
    // =========================================================

    private void animarEntradaChat(
            Scene scene,
            Parent chat
    ) {

        Parent login =
                scene.getRoot();

        // Login se afasta
        FadeTransition loginFade =
                new FadeTransition(
                        Duration.millis(320),
                        login
                );

        loginFade.setFromValue(1);
        loginFade.setToValue(0);

        ScaleTransition loginScale =
                new ScaleTransition(
                        Duration.millis(350),
                        login
                );

        loginScale.setFromX(1);
        loginScale.setFromY(1);

        loginScale.setToX(1.025);
        loginScale.setToY(1.025);

        ParallelTransition saida =
                new ParallelTransition(
                        loginFade,
                        loginScale
                );

        saida.setOnFinished(e -> {

            chat.setOpacity(0);

            chat.setScaleX(0.965);
            chat.setScaleY(0.965);

            chat.setTranslateY(14);

            GaussianBlur blur =
                    new GaussianBlur(10);

            chat.setEffect(blur);

            scene.setRoot(chat);

            FadeTransition fade =
                    new FadeTransition(
                            Duration.millis(650),
                            chat
                    );

            fade.setToValue(1);

            ScaleTransition scale =
                    new ScaleTransition(
                            Duration.millis(700),
                            chat
                    );

            scale.setToX(1);
            scale.setToY(1);

            scale.setInterpolator(
                    Interpolator.EASE_OUT
            );

            TranslateTransition mover =
                    new TranslateTransition(
                            Duration.millis(700),
                            chat
                    );

            mover.setToY(0);

            mover.setInterpolator(
                    Interpolator.EASE_OUT
            );

            Timeline removerBlur =
                    new Timeline(

                            new KeyFrame(
                                    Duration.ZERO,
                                    new KeyValue(
                                            blur.radiusProperty(),
                                            10
                                    )
                            ),

                            new KeyFrame(
                                    Duration.millis(650),
                                    new KeyValue(
                                            blur.radiusProperty(),
                                            0,
                                            Interpolator.EASE_OUT
                                    )
                            )
                    );

            ParallelTransition entrada =
                    new ParallelTransition(
                            fade,
                            scale,
                            mover
                    );

            entrada.setOnFinished(evento ->
                    chat.setEffect(null)
            );

            removerBlur.play();
            entrada.play();
        });

        saida.play();
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        launch(args);
    }
}