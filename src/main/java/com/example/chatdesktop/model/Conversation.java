package com.example.chatdesktop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    private String titulo;

    private final List<ChatMessage> mensagens;

    public Conversation(String titulo) {

        this.titulo = titulo;

        this.mensagens = new ArrayList<>();
    }

    public String getTitulo() {

        return titulo;
    }

    public void setTitulo(String titulo) {

        this.titulo = titulo;
    }

    public List<ChatMessage> getMensagens() {

        return mensagens;
    }

    public void adicionarMensagem(ChatMessage mensagem) {

        mensagens.add(mensagem);
    }

    @Override
    public String toString() {

        return titulo;
    }
}