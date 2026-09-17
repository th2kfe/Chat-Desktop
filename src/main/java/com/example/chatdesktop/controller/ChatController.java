package com.example.chatdesktop.controller;

import com.example.chatdesktop.model.ChatMessage;
import com.example.chatdesktop.model.Conversation;
import com.example.chatdesktop.service.GroqService;
import com.example.chatdesktop.service.PersistenciaService;
import com.example.chatdesktop.service.RagService;
import com.example.chatdesktop.service.WebSearchService;
import com.example.chatdesktop.view.ChatView;

import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controlador principal do ChatDesktop.
 *
 * Responsável por:
 * - Enviar mensagens para a Groq
 * - Utilizar o RAG interna (base local) e, se necessário,
 *   RAG externa (busca na web) para montar contexto
 * - Criar novas conversas
 * - Selecionar conversas
 * - Renomear conversas
 * - Excluir conversas
 * - Regenerar respostas
 * - Atualizar a interface
 * - Persistir o histórico de conversas em disco, para que ele
 *   continue existindo mesmo depois de fechar e abrir o app
 */
public class ChatController {

    private final ChatView view;

    private final GroqService groqService;

    private final RagService ragService;

    private final WebSearchService webSearchService;

    private final PersistenciaService persistenciaService;

    private final List<Conversation> conversas;

    private Conversation conversaAtual;

    private String ultimaPergunta = "";

    /**
     * Pequeno "pacote" com o resultado da busca de contexto:
     * o texto encontrado, a origem (para exibir na tela) e
     * a fonte (detalhe da origem).
     */
    private record ContextoResultado(
            String contexto,
            String origem,
            String fonte
    ) {
    }

    /**
     * Construtor.
     */
    public ChatController(ChatView view) {

        this.view = view;

        this.groqService = new GroqService();

        this.ragService = new RagService();

        this.webSearchService = new WebSearchService();

        this.persistenciaService = new PersistenciaService();

        this.conversas = carregarConversasComTratamento();

        configurarEventos();

        if (conversas.isEmpty()) {

            criarPrimeiraConversa();

        } else {

            conversaAtual = conversas.get(conversas.size() - 1);

            ultimaPergunta = encontrarUltimaPergunta(conversaAtual);

            view.carregarConversa(conversaAtual);

            atualizarLista();
        }
    }

    // ================================================================
    // CONFIGURAÇÃO DOS EVENTOS
    // ================================================================

    private void configurarEventos() {

        // Botão Enviar
        view.getBotaoEnviar().setOnAction(
                evento -> enviarMensagem()
        );

        // Pressionar ENTER no campo de mensagem
        view.getCampoMensagem().setOnAction(
                evento -> enviarMensagem()
        );

        // Nova conversa
        view.setAoNovaConversa(
                this::novaConversa
        );

        // Regenerar resposta
        view.setAoRegenerar(
                this::regenerarResposta
        );

        // Renomear conversa
        view.setAoRenomear(
                this::renomearConversa
        );

        // Excluir conversa
        view.setAoExcluir(
                this::excluirConversa
        );

        // Selecionar conversa
        view.setAoSelecionarConversa(
                this::selecionarConversa
        );
    }

    // ================================================================
    // PRIMEIRA CONVERSA (só é usada quando não há histórico salvo)
    // ================================================================

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

    // ================================================================
    // NOVA CONVERSA
    // ================================================================

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

    // ================================================================
    // ENVIAR MENSAGEM
    // ================================================================

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

    // ================================================================
    // ENVIO PARA GROQ + RAG (INTERNA E EXTERNA)
    // ================================================================

    private void enviarPergunta(
            String mensagem
    ) {

        if (conversaAtual == null) {
            return;
        }

        // Mostra a mensagem do usuário na tela
        view.adicionarMensagemUsuario(
                mensagem
        );

        // Limpa o campo
        view.getCampoMensagem()
                .clear();

        // Adiciona a mensagem ao histórico
        ChatMessage mensagemUsuario =
                ChatMessage.user(
                        mensagem
                );

        conversaAtual.adicionarMensagem(
                mensagemUsuario
        );

        // Salva já com a pergunta do usuário, mesmo antes da
        // resposta da IA chegar (evita perder a pergunta se algo falhar)
        persistirConversas();

        // Mostra carregamento
        view.setCarregando(true);

        // ============================================================
        // BUSCA DE CONTEXTO (RAG interna, com fallback para externa)
        // ============================================================

        final ContextoResultado contextoResultado =
                obterContexto(mensagem);

        // ============================================================
        // HISTÓRICO
        // ============================================================

        final List<ChatMessage> historico =
                new ArrayList<>(
                        conversaAtual.getMensagens()
                );

        /*
         * Guardamos a conversa atual em uma variável final.
         *
         * Isso evita problemas caso o usuário crie outra
         * conversa enquanto a IA estiver processando.
         */
        final Conversation conversaDaRequisicao =
                conversaAtual;

        // ============================================================
        // THREAD
        // ============================================================

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

    // ================================================================
    // REGENERAR RESPOSTA
    // ================================================================

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

        /*
         * Se a última mensagem for da IA,
         * removemos antes de gerar uma nova.
         */
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

                persistirConversas();
            }
        }

        /*
         * Regera a resposta usando a última pergunta.
         *
         * Não adicionamos novamente a pergunta ao histórico
         * aqui porque ela já está presente.
         */
        gerarNovamente();
    }

    // ================================================================
    // GERAR NOVAMENTE
    // ================================================================

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

    // ================================================================
    // BUSCA DE CONTEXTO: RAG INTERNA PRIMEIRO, EXTERNA COMO FALLBACK
    // ================================================================

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

    // ================================================================
    // RENOMEAR CONVERSA
    // ================================================================

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

    // ================================================================
    // EXCLUIR CONVERSA
    // ================================================================

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

        /*
         * Se não houver mais conversas,
         * criamos uma nova automaticamente.
         */
        if (conversas.isEmpty()) {

            criarPrimeiraConversa();

            return;
        }

        /*
         * Seleciona a última conversa restante.
         */
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

    // ================================================================
    // SELECIONAR CONVERSA
    // ================================================================

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

    // ================================================================
    // ENCONTRAR ÚLTIMA PERGUNTA
    // ================================================================

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

    // ================================================================
    // TÍTULO AUTOMÁTICO
    // ================================================================

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

    // ================================================================
    // ATUALIZAR LISTA DE CONVERSAS (e persistir em disco)
    // ================================================================

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

        persistirConversas();
    }

    // ================================================================
    // PERSISTÊNCIA
    // ================================================================

    private List<Conversation> carregarConversasComTratamento() {

        try {

            return persistenciaService.carregarConversas();

        } catch (Exception erro) {

            System.err.println(
                    "Erro ao carregar conversas salvas: "
                            + erro.getMessage()
            );

            return new ArrayList<>();
        }
    }

    private void persistirConversas() {

        try {

            persistenciaService.salvarConversas(conversas);

        } catch (Exception erro) {

            System.err.println(
                    "Erro ao salvar conversas: " + erro.getMessage()
            );
        }
    }

    // ================================================================
    // TRATAMENTO DE ERRO
    // ================================================================

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