package com.example.chatdesktop.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WebSearchService {

    private final HttpClient httpClient;

    public WebSearchService() {
        httpClient = HttpClient.newHttpClient();
    }

    public String buscarContextoExterno(String pergunta)
            throws IOException, InterruptedException {

        String urlBusca =
                "https://api.duckduckgo.com/?q="
                        + URLEncoder.encode(pergunta, StandardCharsets.UTF_8)
                        + "&format=json&no_html=1&skip_disambig=1&no_redirect=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBusca))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            System.err.println("Erro na busca externa: HTTP " + response.statusCode());
            return "";
        }

        return montarContexto(response.body());
    }

    private String montarContexto(String json) {

        if (json == null || json.isBlank()) {
            return "";
        }

        String heading = extrairValorUnico(json, "Heading");
        String abstractText = extrairValorUnico(json, "AbstractText");
        String abstractUrl = extrairValorUnico(json, "AbstractURL");
        List<String> relacionados = extrairTodosValores(json, "Text");

        StringBuilder contexto = new StringBuilder();

        if (!abstractText.isBlank()) {

            contexto.append("\n--- RESULTADO DA WEB (DuckDuckGo) ---\n");

            if (!heading.isBlank()) {
                contexto.append("Título: ").append(heading).append("\n");
            }

            if (!abstractUrl.isBlank()) {
                contexto.append("Fonte: ").append(abstractUrl).append("\n");
            }

            contexto.append(abstractText).append("\n");
        }

        int adicionados = 0;

        for (String texto : relacionados) {

            if (adicionados >= 2) {
                break;
            }

            if (texto.isBlank() || texto.equals(abstractText)) {
                continue;
            }

            contexto.append("\n--- TÓPICO RELACIONADO (DuckDuckGo) ---\n")
                    .append(texto)
                    .append("\n");

            adicionados++;
        }

        return contexto.toString();
    }

    private String extrairValorUnico(String json, String campo) {

        List<String> valores = extrairTodosValores(json, campo);

        return valores.isEmpty() ? "" : valores.get(0);
    }

    private List<String> extrairTodosValores(String json, String campo) {

        List<String> valores = new ArrayList<>();

        String marcador = "\"" + campo + "\":\"";

        int posicao = 0;

        while (true) {

            int inicio = json.indexOf(marcador, posicao);

            if (inicio == -1) {
                break;
            }

            inicio += marcador.length();

            StringBuilder valor = new StringBuilder();

            boolean escapado = false;

            int i = inicio;

            for (; i < json.length(); i++) {

                char c = json.charAt(i);

                if (escapado) {

                    switch (c) {
                        case 'n' -> valor.append('\n');
                        case 'r' -> valor.append('\r');
                        case 't' -> valor.append('\t');
                        case '"' -> valor.append('"');
                        case '\\' -> valor.append('\\');
                        case '/' -> valor.append('/');
                        default -> valor.append(c);
                    }

                    escapado = false;

                } else if (c == '\\') {

                    escapado = true;

                } else if (c == '"') {

                    break;

                } else {

                    valor.append(c);
                }
            }

            valores.add(valor.toString().trim());

            posicao = i + 1;
        }

        return valores;
    }
}