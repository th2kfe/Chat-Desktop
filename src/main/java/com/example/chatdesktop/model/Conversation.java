package com.example.chatdesktop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String titulo;

    private final List<ChatMessage> mensagens;

    /*
     * Não usamos final aqui para manter compatibilidade
     * com conversas antigas já serializadas.
     */
    private LocalDateTime dataCriacao;
    private LocalDateTime ultimaAtualizacao;

    public Conversation(String titulo) {

        this.titulo = titulo;
        this.mensagens = new ArrayList<>();

        this.dataCriacao = LocalDateTime.now();
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {

        this.titulo = titulo;
        atualizarData();
    }

    public List<ChatMessage> getMensagens() {
        return mensagens;
    }

    public void adicionarMensagem(ChatMessage mensagem) {

        mensagens.add(mensagem);
        atualizarData();
    }

    public LocalDateTime getDataCriacao() {

        /*
         * Conversas antigas salvas antes dessa propriedade
         * podem carregar null.
         */
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }

        return dataCriacao;
    }

    public LocalDateTime getUltimaAtualizacao() {

        if (ultimaAtualizacao == null) {
            ultimaAtualizacao = getDataCriacao();
        }

        return ultimaAtualizacao;
    }

    public void atualizarData() {
        ultimaAtualizacao = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return titulo;
    }
}