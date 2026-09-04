package com.example.chatdesktop.service;

import java.util.*;
import java.util.stream.Collectors;

public class RagService {

    private final DocumentService documentService;

    public RagService() {
        documentService = new DocumentService();
    }

    public String buscarContexto(String pergunta) {

        List<String> documentos = documentService.carregarDocumentos();

        if (documentos.isEmpty()) {
            return "";
        }

        String perguntaNormalizada = normalizar(pergunta);

        Set<String> palavrasPergunta =
                Arrays.stream(perguntaNormalizada.split("\\s+"))
                        .filter(palavra -> palavra.length() >= 3)
                        .collect(Collectors.toSet());

        List<ResultadoBusca> resultados = new ArrayList<>();

        for (String documento : documentos) {

            String textoNormalizado = normalizar(documento);

            int pontuacao = 0;

            for (String palavra : palavrasPergunta) {
                if (textoNormalizado.contains(palavra)) {
                    pontuacao++;
                }
            }

            if (pontuacao > 0) {
                resultados.add(new ResultadoBusca(documento, pontuacao));
            }
        }

        resultados.sort(
                Comparator.comparingInt(ResultadoBusca::pontuacao).reversed()
        );

        StringBuilder contexto = new StringBuilder();

        int limite = Math.min(3, resultados.size());

        for (int i = 0; i < limite; i++) {

            contexto.append("\n--- DOCUMENTO ---\n")
                    .append(resultados.get(i).documento())
                    .append("\n");
        }

        return contexto.toString();
    }

    private String normalizar(String texto) {

        return texto
                .toLowerCase(Locale.ROOT)
                .replaceAll("[áàãâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòõôö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[ç]", "c");
    }

    private record ResultadoBusca(String documento, int pontuacao) {
    }
}