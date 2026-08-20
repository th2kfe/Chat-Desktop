package com.example.chatdesktop.model;



    public class ChatMessage {

        private final String role;
        private final String content;

        public ChatMessage(
                String role,
                String content
        ) {

            this.role = role;
            this.content = content;
        }

        public String getRole() {

            return role;
        }

        public String getContent() {

            return content;
        }

        public static ChatMessage user(
                String content
        ) {

            return new ChatMessage(
                    "user",
                    content
            );
        }

        public static ChatMessage assistant(
                String content
        ) {

            return new ChatMessage(
                    "assistant",
                    content
            );
        }

        public static ChatMessage system(
                String content
        ) {

            return new ChatMessage(
                    "system",
                    content
            );
        }
    }

