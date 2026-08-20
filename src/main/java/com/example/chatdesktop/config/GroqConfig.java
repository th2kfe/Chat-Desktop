package com.example.chatdesktop.config;



    public final class GroqConfig {

        private GroqConfig() {
        }

        /*
         * A chave será lida da variável de ambiente:
         *
         * GROQ_API_KEY
         *
         * Não coloque sua chave diretamente no código.
         */

        public static String getApiKey() {

            String apiKey = System.getenv("GROQ_API_KEY");

            if (apiKey == null || apiKey.isBlank()) {

                throw new IllegalStateException(
                        "A variável de ambiente GROQ_API_KEY não foi configurada."
                );
            }

            return apiKey.trim();
        }

        /*
         * Modelo utilizado pelo chat.
         */
        public static String getModel() {

            return "openai/gpt-oss-20b";
        }

        /*
         * URL da API Groq.
         */
        public static String getApiUrl() {

            return "https://api.groq.com/openai/v1/chat/completions";
        }
    }

