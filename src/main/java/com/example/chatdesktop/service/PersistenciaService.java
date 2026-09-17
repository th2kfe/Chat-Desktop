package com.example.chatdesktop.service;

import com.example.chatdesktop.model.Conversation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço responsável por salvar e carregar dados do app em disco,
 * para que contas cadastradas e histórico de conversas sobrevivam
 * a fechar e abrir o programa novamente.
 *
 * Os arquivos ficam guardados na pasta pessoal do usuário do
 * sistema operacional, em uma pasta oculta chamada ".chatdesktop":
 *
 *   Windows: C:\Users\SeuUsuario\.chatdesktop\
 *   Linux/Mac: ~/.chatdesktop/
 *
 * OBS DE SEGURANÇA: as senhas são salvas em texto puro (sem hash)
 * dentro de usuarios.dat. Isso é aceitável para um projeto de
 * estudo/demonstração rodando localmente, mas NÃO deve ser usado
 * assim em um sistema real com usuários de verdade.
 */
public class PersistenciaService {

    private final Path pastaDados;

    private final Path arquivoUsuarios;

    private final Path arquivoConversas;

    public PersistenciaService() {

        pastaDados = Paths.get(
                System.getProperty("user.home"),
                ".chatdesktop"
        );

        arquivoUsuarios = pastaDados.resolve("usuarios.dat");

        arquivoConversas = pastaDados.resolve("conversas.dat");

        try {

            if (!Files.exists(pastaDados)) {

                Files.createDirectories(pastaDados);
            }

        } catch (IOException erro) {

            throw new RuntimeException(
                    "Não foi possível criar a pasta de dados do app em "
                            + pastaDados,
                    erro
            );
        }
    }

    // ================================================================
    // USUÁRIOS (e-mail -> senha)
    // ================================================================

    @SuppressWarnings("unchecked")
    public Map<String, String> carregarUsuarios()
            throws IOException, ClassNotFoundException {

        if (!Files.exists(arquivoUsuarios)) {

            return new HashMap<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(
                Files.newInputStream(arquivoUsuarios)
        )) {

            Object objeto = entrada.readObject();

            if (objeto instanceof Map) {

                return (Map<String, String>) objeto;
            }

            return new HashMap<>();
        }
    }

    public void salvarUsuarios(Map<String, String> usuarios)
            throws IOException {

        try (ObjectOutputStream saida = new ObjectOutputStream(
                Files.newOutputStream(arquivoUsuarios)
        )) {

            saida.writeObject(new HashMap<>(usuarios));
        }
    }

    // ================================================================
    // CONVERSAS
    // ================================================================

    @SuppressWarnings("unchecked")
    public List<Conversation> carregarConversas()
            throws IOException, ClassNotFoundException {

        if (!Files.exists(arquivoConversas)) {

            return new ArrayList<>();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(
                Files.newInputStream(arquivoConversas)
        )) {

            Object objeto = entrada.readObject();

            if (objeto instanceof List) {

                return new ArrayList<>((List<Conversation>) objeto);
            }

            return new ArrayList<>();
        }
    }

    public void salvarConversas(List<Conversation> conversas)
            throws IOException {

        try (ObjectOutputStream saida = new ObjectOutputStream(
                Files.newOutputStream(arquivoConversas)
        )) {

            saida.writeObject(new ArrayList<>(conversas));
        }
    }
}