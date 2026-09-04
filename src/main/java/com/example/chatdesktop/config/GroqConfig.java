package com.example.chatdesktop.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class GroqConfig {

    private static final Properties propriedades = new Properties();

    static {
        try (InputStream input = GroqConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "Arquivo config.properties não encontrado em resources. "
                                + "Copie config.properties.example, renomeie para "
                                + "config.properties e preencha sua chave da Groq."
                );
            }

            propriedades.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar config.properties", e);
        }
    }

    private GroqConfig() {
    }

    public static String getApiKey() {

        String apiKey = propriedades.getProperty("groq.api.key");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "A propriedade groq.api.key não foi configurada em config.properties."
            );
        }

        return apiKey.trim();
    }

    public static String getModel() {
        return "openai/gpt-oss-20b";
    }

    public static String getApiUrl() {
        return "https://api.groq.com/openai/v1/chat/completions";
    }
}