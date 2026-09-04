package com.example.chatdesktop.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentService {

    private final Path pastaConhecimento;

    public DocumentService() {

        pastaConhecimento = Paths.get("knowledge");

        try {

            if (!Files.exists(pastaConhecimento)) {
                Files.createDirectories(pastaConhecimento);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Não foi possível criar a pasta knowledge.",
                    e
            );
        }
    }

    public List<String> carregarDocumentos() {

        List<String> documentos = new ArrayList<>();

        try {

            if (!Files.exists(pastaConhecimento)) {
                return documentos;
            }

            try (DirectoryStream<Path> arquivos =
                         Files.newDirectoryStream(pastaConhecimento, "*.txt")) {

                for (Path arquivo : arquivos) {

                    String conteudo = Files.readString(arquivo, StandardCharsets.UTF_8);

                    if (!conteudo.isBlank()) {

                        documentos.add(
                                "Arquivo: " + arquivo.getFileName() + "\n" + conteudo
                        );
                    }
                }
            }

        } catch (IOException e) {

            throw new RuntimeException("Erro ao carregar documentos.", e);
        }

        return documentos;
    }
}