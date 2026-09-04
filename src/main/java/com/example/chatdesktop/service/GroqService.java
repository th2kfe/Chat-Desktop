package com.example.chatdesktop.service;

import com.example.chatdesktop.config.GroqConfig;
import com.example.chatdesktop.model.ChatMessage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GroqService {

    private final HttpClient httpClient;

    public GroqService() {
        httpClient = HttpClient.newHttpClient();
    }

    public String enviarMensagem(List<ChatMessage> mensagens, String contexto)
            throws IOException, InterruptedException {

        String json = criarJson(mensagens, contexto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GroqConfig.getApiUrl()))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + GroqConfig.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                    "Erro da API Groq: HTTP " + response.statusCode()
                            + "\n" + extrairErro(response.body())
            );
        }

        return extrairResposta(response.body());
    }

    private String criarJson(List<ChatMessage> mensagens, String contexto) {

        StringBuilder json = new StringBuilder();

        json.append("{");
        json.append("\"model\":\"").append(escaparJson(GroqConfig.getModel())).append("\",");
        json.append("\"messages\":[");

        String instrucaoSistema =
                "Você é um assistente inteligente, "
                        + "educado e útil. "
                        + "Responda sempre em português do Brasil.\n\n"
                        + "Quando houver contexto recuperado "
                        + "pela base de conhecimento local ou por busca "
                        + "na web, utilize essas informações para responder.\n\n"
                        + "Não invente informações que não "
                        + "estejam disponíveis no contexto.\n\n"
                        + "Se a informação solicitada não estiver "
                        + "disponível no contexto, informe "
                        + "claramente que ela não foi encontrada.\n\n"
                        + "CONTEXTO RECUPERADO:\n"
                        + (contexto == null ? "" : contexto);

        json.append("{");
        json.append("\"role\":\"system\",");
        json.append("\"content\":\"");
        json.append(escaparJson(instrucaoSistema));
        json.append("\"}");

        if (mensagens != null) {

            for (ChatMessage mensagem : mensagens) {

                if (mensagem == null) {
                    continue;
                }

                json.append(",");
                json.append("{");
                json.append("\"role\":\"").append(escaparJson(mensagem.getRole())).append("\",");
                json.append("\"content\":\"").append(escaparJson(mensagem.getContent())).append("\"");
                json.append("}");
            }
        }

        json.append("]");
        json.append(",");
        json.append("\"temperature\":0.7");
        json.append(",");
        json.append("\"max_completion_tokens\":1024");
        json.append("}");

        return json.toString();
    }

    private String extrairResposta(String json) {

        if (json == null || json.isBlank()) {
            throw new RuntimeException("A API Groq retornou uma resposta vazia.");
        }

        String marcador = "\"content\":\"";

        int inicio = json.indexOf(marcador);

        if (inicio == -1) {
            throw new RuntimeException("Não foi possível encontrar a resposta da IA.");
        }

        inicio += marcador.length();

        StringBuilder resposta = new StringBuilder();

        boolean escapado = false;

        for (int i = inicio; i < json.length(); i++) {

            char caractere = json.charAt(i);

            if (escapado) {

                switch (caractere) {
                    case 'n' -> resposta.append('\n');
                    case 'r' -> resposta.append('\r');
                    case 't' -> resposta.append('\t');
                    case '"' -> resposta.append('"');
                    case '\\' -> resposta.append('\\');
                    case '/' -> resposta.append('/');
                    default -> resposta.append(caractere);
                }

                escapado = false;

            } else if (caractere == '\\') {

                escapado = true;

            } else if (caractere == '"') {

                break;

            } else {

                resposta.append(caractere);
            }
        }

        return resposta.toString().trim();
    }

    private String extrairErro(String json) {

        if (json == null || json.isBlank()) {
            return "A API não informou o motivo do erro.";
        }

        String marcador = "\"message\":\"";

        int inicio = json.indexOf(marcador);

        if (inicio == -1) {
            return json;
        }

        inicio += marcador.length();

        StringBuilder erro = new StringBuilder();

        boolean escapado = false;

        for (int i = inicio; i < json.length(); i++) {

            char caractere = json.charAt(i);

            if (escapado) {

                switch (caractere) {
                    case 'n' -> erro.append('\n');
                    case 'r' -> erro.append('\r');
                    case 't' -> erro.append('\t');
                    case '"' -> erro.append('"');
                    case '\\' -> erro.append('\\');
                    default -> erro.append(caractere);
                }

                escapado = false;

            } else if (caractere == '\\') {

                escapado = true;

            } else if (caractere == '"') {

                break;

            } else {

                erro.append(caractere);
            }
        }

        return erro.toString().trim();
    }

    private String escaparJson(String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}