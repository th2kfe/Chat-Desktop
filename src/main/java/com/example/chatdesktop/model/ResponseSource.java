package com.example.chatdesktop.model;

public class ResponseSource {

    private final String origem;

    private final String fonte;

    public ResponseSource(
            String origem,
            String fonte
    ) {

        this.origem = origem;
        this.fonte = fonte;
    }

    public String getOrigem() {

        return origem;
    }

    public String getFonte() {

        return fonte;
    }

    public static ResponseSource rag(
            String arquivo
    ) {

        return new ResponseSource(
                "RAG",
                arquivo
        );
    }

    public static ResponseSource internet(
            String fonte
    ) {

        return new ResponseSource(
                "Internet",
                fonte
        );
    }

    public static ResponseSource fallback() {

        return new ResponseSource(
                "Fallback local",
                null
        );
    }
}