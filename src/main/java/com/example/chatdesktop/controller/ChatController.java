package com.example.chatdesktop.controller;

import com.example.chatdesktop.model.ChatMessage;
import com.example.chatdesktop.model.Conversation;
import com.example.chatdesktop.service.GroqService;
import com.example.chatdesktop.service.RagService;
import com.example.chatdesktop.service.WebSearchService;
import com.example.chatdesktop.view.ChatView;

import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatController {

    private final ChatView view;

    private final GroqService groqService;

    private final RagService ragService;

    private final WebSearchService webSearchService;

    private final List<Conversation> conversas;

    private Conversation conversaAtual;

    private String ultimaPergunta = "";

    private record ContextoResultado(
            String contexto,
            String origem,
            String fonte
    ) {
    }

    public ChatController(ChatView view) {

        this.view = view;

        this.groqService = new GroqService();

        this.ragService = new RagService();

        this.webSearchService = new WebSearchService();

        this.conversas = new ArrayList<>();

        criarPrimeiraConversa();

        configurarEventos();
    }

    private void configurarEventos() {

        view.getBotaoEnviar().setOnAction(
                evento -> enviarMensagem()
        );

        view.getCampoMensagem().setOnAction(
                evento -> enviarMensagem()
        );

        view.setAoNovaConversa(
                this::novaConversa
        );

        view.setAoRegenerar(
                this::regenerarResposta
        );

        view.setAoRenomear(
                this::renomearConversa
        );

        view.setAoExcluir(
                this::excluirConversa
        );

        view.setAoSelecionarConversa(
                this::selecionarConversa
        );
    }

    private void criarPrimeiraConversa() {

        conversaAtual =
                new Conversation(
                        "Nova conversa"
                );

        conversas.add(
                conversaAtual
        );

        atualizarLista();

        view.limparConversa();

        view.adicionarMensagemIA(
                "Olá! Eu sou seu assistente. "
                        + "Digite uma mensagem para começar."
        );
    }

    private void novaConversa() {

        conversaAtual =
                new Conversation(
                        "Nova conversa"
                );

        conversas.add(
                conversaAtual
        );

        ultimaPergunta = "";

        atualizarLista();

        view.limparConversa();

        view.adicionarMensagemIA(
                "Olá! Eu sou seu assistente. "
                        + "Digite uma mensagem para começar."
        );

        view.getCampoMensagem()
                .requestFocus();
    }

    private void enviarMensagem() {

        if (conversaAtual == null) {
            return;
        }

        String mensagem =
                view.getCampoMensagem()
                        .getText()
                        .trim();

        if (mensagem.isEmpty()) {
            return;
        }

        ultimaPergunta = mensagem;

        enviarPergunta(
                mensagem
        );
    }

    private void enviarPergunta(
            String mensagem
    ) {

        if (conversaAtual == null) {
            return;
        }

        view.adicionarMensagemUsuario(
                mensagem
        );

        view.getCampoMensagem()
                .clear();

        ChatMessage mensagemUsuario =
                ChatMessage.user(
                        mensagem
                );

        conversaAtual.adicionarMensagem(
                mensagemUsuario
        );

        view.setCarregando(true);

        final ContextoResultado contextoResultado =
                obterContexto(mensagem);

        final List<ChatMessage> historico =
                new ArrayList<>(
                        conversaAtual.getMensagens()
                );

        final Conversation conversaDaRequisicao =
                conversaAtual;

        Thread thread =
                new Thread(() -> {

                    try {

                        String resposta =
                                groqService.enviarMensagem(
                                        historico,
                                        contextoResultado.contexto()
                                );

                        Platform.runLater(() -> {

                            ChatMessage mensagemIA =
                                    ChatMessage.assistant(
                                            resposta
                                    );

                            conversaDaRequisicao
                                    .adicionarMensagem(
                                            mensagemIA
                                    );

                            view.adicionarMensagemIA(
                                    resposta,
                                    contextoResultado.origem(),
                                    contextoResultado.fonte()
                            );

                            view.setCarregando(
                                    false
                            );

                            view.getCampoMensagem()
                                    .requestFocus();

                            atualizarTitulo();

                            atualizarLista();
                        });

                    } catch (Throwable erro) {

                        Platform.runLater(() -> {

                            view.adicionarErro(
                                    obterMensagemErro(
                                            erro
                                    )
                            );

                            view.setCarregando(
                                    false
                            );

                            view.getCampoMensagem()
                                    .requestFocus();
                        });
                    }
                });

        thread.setDaemon(true);

        thread.start();
    }

    private void regenerarResposta() {

        if (conversaAtual == null) {
            return;
        }

        if (
                ultimaPergunta == null
                        ||
                        ultimaPergunta.isBlank()
        ) {
            return;
        }

        List<ChatMessage> mensagens =
                conversaAtual.getMensagens();

        if (!mensagens.isEmpty()) {

            int ultimoIndice =
                    mensagens.size() - 1;

            ChatMessage ultimaMensagem =
                    mensagens.get(
                            ultimoIndice
                    );

            if (
                    "assistant".equals(
                            ultimaMensagem.getRole()
                    )
            ) {

                mensagens.remove(
                        ultimoIndice
                );
            }
        }

        gerarNovamente();
    }

    private void gerarNovamente() {

        if (conversaAtual == null) {
            return;
        }

        final String pergunta =
                ultimaPergunta;

        view.setCarregando(true);

        final ContextoResultado contextoResultado =
                obterContexto(pergunta);

        final List<ChatMessage> historico =
                new ArrayList<>(
                        conversaAtual.getMensagens()
                );

        final Conversation conversaDaRequisicao =
                conversaAtual;

        Thread thread =
                new Thread(() -> {

                    try {

                        String resposta =
                                groqService.enviarMensagem(
                                        historico,
                                        contextoResultado.contexto()
                                );

                        Platform.runLater(() -> {

                            conversaDaRequisicao
                                    .adicionarMensagem(
                                            ChatMessage.assistant(
                                                    resposta
                                            )
                                    );

                            view.adicionarMensagemIA(
                                    resposta,
                                    contextoResultado.origem(),
                                    contextoResultado.fonte()
                            );

                            view.setCarregando(
                                    false
                            );

                            view.getCampoMensagem()
                                    .requestFocus();

                            atualizarLista();
                        });

                    } catch (Throwable erro) {

                        Platform.runLater(() -> {

                            view.adicionarErro(
                                    obterMensagemErro(
                                            erro
                                    )
                            );

                            view.setCarregando(
                                    false
                            );
                        });
                    }
                });

        thread.setDaemon(true);

        thread.start();
    }

    private ContextoResultado obterContexto(String pergunta) {

        String contextoInterno = obterContextoInterno(pergunta);

        if (contextoInterno != null && !contextoInterno.isBlank()) {

            return new ContextoResultado(
                    contextoInterno,
                    "RAG Interna",
                    "Base de conhecimento local"
            );
        }

        String contextoExterno = obterContextoExterno(pergunta);

        if (contextoExterno != null && !contextoExterno.isBlank()) {

            return new ContextoResultado(
                    contextoExterno,
                    "RAG Externa (Web)",
                    "Resultados de busca na web"
            );
        }

        return new ContextoResultado(
                "",
                "Groq",
                null
        );
    }

    private String obterContextoInterno(String pergunta) {

        try {

            return ragService.buscarContexto(
                    pergunta
            );

        } catch (Exception erro) {

            System.err.println(
                    "Erro no RAG interno: "
                            + erro.getMessage()
            );

            return "";
        }
    }

    private String obterContextoExterno(String pergunta) {

        try {

            return webSearchService.buscarContextoExterno(
                    pergunta
            );

        } catch (Exception erro) {

            System.err.println(
                    "Erro no RAG externo (web): "
                            + erro.getMessage()
            );

            return "";
        }
    }

    private void renomearConversa() {

        if (conversaAtual == null) {
            return;
        }

        TextInputDialog dialog =
                new TextInputDialog(
                        conversaAtual.getTitulo()
                );

        dialog.setTitle(
                "Renomear conversa"
        );

        dialog.setHeaderText(
                "Digite o novo nome da conversa:"
        );

        dialog.setContentText(
                "Nome:"
        );

        Optional<String> resultado =
                dialog.showAndWait();

        resultado.ifPresent(
                novoNome -> {

                    if (
                            novoNome != null
                                    &&
                                    !novoNome.isBlank()
                    ) {

                        conversaAtual.setTitulo(
                                novoNome.trim()
                        );

                        atualizarLista();
                    }
                }
        );
    }

    private void excluirConversa() {

        if (conversaAtual == null) {
            return;
        }

        boolean confirmou =
                view.confirmarExclusao(
                        conversaAtual.getTitulo()
                );

        if (!confirmou) {
            return;
        }

        conversas.remove(
                conversaAtual
        );

        if (conversas.isEmpty()) {

            criarPrimeiraConversa();

            return;
        }

        conversaAtual =
                conversas.get(
                        conversas.size() - 1
                );

        ultimaPergunta =
                encontrarUltimaPergunta(
                        conversaAtual
                );

        atualizarLista();

        view.carregarConversa(
                conversaAtual
        );
    }

    private void selecionarConversa(
            Conversation conversa
    ) {

        if (conversa == null) {
            return;
        }

        conversaAtual =
                conversa;

        ultimaPergunta =
                encontrarUltimaPergunta(
                        conversa
                );

        view.carregarConversa(
                conversa
        );
    }

    private String encontrarUltimaPergunta(
            Conversation conversa
    ) {

        if (conversa == null) {
            return "";
        }

        List<ChatMessage> mensagens =
                conversa.getMensagens();

        for (
                int i = mensagens.size() - 1;
                i >= 0;
                i--
        ) {

            ChatMessage mensagem =
                    mensagens.get(i);

            if (
                    "user".equals(
                            mensagem.getRole()
                    )
            ) {

                return mensagem.getContent();
            }
        }

        return "";
    }

    private void atualizarTitulo() {

        if (conversaAtual == null) {
            return;
        }

        if (
                !"Nova conversa".equals(
                        conversaAtual.getTitulo()
                )
        ) {
            return;
        }

        if (
                ultimaPergunta == null
                        ||
                        ultimaPergunta.isBlank()
        ) {
            return;
        }

        String titulo =
                ultimaPergunta.trim();

        if (titulo.length() > 25) {

            titulo =
                    titulo.substring(
                            0,
                            25
                    ) + "...";
        }

        conversaAtual.setTitulo(
                titulo
        );
    }

    private void atualizarLista() {

        view.getListaConversas()
                .getItems()
                .setAll(
                        conversas
                );

        if (conversaAtual != null) {

            view.getListaConversas()
                    .getSelectionModel()
                    .select(
                            conversaAtual
                    );
        }
    }

    private String obterMensagemErro(
            Throwable erro
    ) {

        if (erro == null) {
            return "Erro desconhecido.";
        }

        String mensagem =
                erro.getMessage();

        if (
                mensagem == null
                        ||
                        mensagem.isBlank()
        ) {

            return erro
                    .getClass()
                    .getSimpleName();
        }

        return mensagem;
    }
}