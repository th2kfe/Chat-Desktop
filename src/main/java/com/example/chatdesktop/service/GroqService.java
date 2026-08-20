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

        this.httpClient = HttpClient.newHttpClient();
    }

    public String enviarMensagem(
            List<ChatMessage> mensagens
    ) throws IOException, InterruptedException {

        String json = criarJson(mensagens);

        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(
                                URI.create(
                                        GroqConfig.getApiUrl()
                                )
                        )
                        .header(
                                "Content-Type",
                                "application/json"
                        )
                        .header(
                                "Authorization",
                                "Bearer "
                                        + GroqConfig
                                        .getApiKey()
                        )
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(
                                                json,
                                                StandardCharsets.UTF_8
                                        )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers
                                .ofString(
                                        StandardCharsets.UTF_8
                                )
                );

        if (
                response.statusCode() < 200
                        ||
                        response.statusCode() >= 300
        ) {

            throw new RuntimeException(
                    "Erro da API Groq: HTTP "
                            + response.statusCode()
                            + "\n"
                            + extrairErro(
                            response.body()
                    )
            );
        }

        return extrairResposta(
                response.body()
        );
    }

    private String criarJson(
            List<ChatMessage> mensagens
    ) {

        StringBuilder json =
                new StringBuilder();

        json.append("{");

        json.append("\"model\":\"")
                .append(
                        escaparJson(
                                GroqConfig.getModel()
                        )
                )
                .append("\",");

        json.append("\"messages\":[");

        /*
         * Mensagem de sistema.
         */
        json.append("{");

        json.append("\"role\":\"system\",");
        json.append("\"content\":\"");

        json.append(
                escaparJson(
                        "Você é um assistente inteligente, "
                                + "educado e útil. "
                                + "Responda sempre em português do Brasil. "
                                + "Seja claro e objetivo."
                )
        );

        json.append("\"}");

        /*
         * Histórico da conversa.
         */
        for (ChatMessage mensagem : mensagens) {

            json.append(",");

            json.append("{");

            json.append("\"role\":\"")
                    .append(
                            escaparJson(
                                    mensagem.getRole()
                            )
                    )
                    .append("\",");

            json.append("\"content\":\"")
                    .append(
                            escaparJson(
                                    mensagem.getContent()
                            )
                    )
                    .append("\"");

            json.append("}");
        }

        json.append("]");

        json.append(",");

        json.append("\"temperature\":0.7");

        json.append(",");

        json.append("\"max_completion_tokens\":1024");

        json.append("}");

        return json.toString();
    }

    private String extrairResposta(
            String json
    ) {

        String marcador =
                "\"content\":\"";

        int inicio =
                json.indexOf(marcador);

        if (inicio == -1) {

            throw new RuntimeException(
                    "A API respondeu, mas não foi possível "
                            + "encontrar o conteúdo da resposta."
            );
        }

        inicio += marcador.length();

        StringBuilder resposta =
                new StringBuilder();

        boolean escapado = false;

        for (
                int i = inicio;
                i < json.length();
                i++
        ) {

            char caractere =
                    json.charAt(i);

            if (escapado) {

                switch (caractere) {

                    case 'n':
                        resposta.append('\n');
                        break;

                    case 'r':
                        resposta.append('\r');
                        break;

                    case 't':
                        resposta.append('\t');
                        break;

                    case '"':
                        resposta.append('"');
                        break;

                    case '\\':
                        resposta.append('\\');
                        break;

                    case '/':
                        resposta.append('/');
                        break;

                    default:
                        resposta.append(
                                caractere
                        );
                        break;
                }

                escapado = false;

            } else if (caractere == '\\') {

                escapado = true;

            } else if (caractere == '"') {

                break;

            } else {

                resposta.append(
                        caractere
                );
            }
        }

        return resposta
                .toString()
                .trim();
    }

    private String extrairErro(
            String json
    ) {

        String marcador =
                "\"message\":\"";

        int inicio =
                json.indexOf(marcador);

        if (inicio == -1) {

            return json;
        }

        inicio += marcador.length();

        StringBuilder erro =
                new StringBuilder();

        boolean escapado = false;

        for (
                int i = inicio;
                i < json.length();
                i++
        ) {

            char caractere =
                    json.charAt(i);

            if (escapado) {

                if (caractere == 'n') {

                    erro.append('\n');

                } else {

                    erro.append(caractere);
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

        return erro.toString();
    }

    private String escaparJson(
            String texto
    ) {

        return texto
                .replace(
                        "\\",
                        "\\\\"
                )
                .replace(
                        "\"",
                        "\\\""
                )
                .replace(
                        "\n",
                        "\\n"
                )
                .replace(
                        "\r",
                        "\\r"
                )
                .replace(
                        "\t",
                        "\\t"
                );
    }
}