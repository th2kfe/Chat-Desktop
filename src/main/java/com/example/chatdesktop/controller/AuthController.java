package com.example.chatdesktop.controller;

import com.example.chatdesktop.service.PersistenciaService;
import com.example.chatdesktop.view.AuthView;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador da tela de login/registro.
 *
 * As contas cadastradas agora são salvas em disco (via
 * PersistenciaService), então elas continuam existindo mesmo
 * depois de fechar e abrir o app de novo — não precisa mais
 * se registrar toda vez.
 */
public class AuthController {

    private final AuthView view;

    private final Runnable aoAutenticar;

    private final PersistenciaService persistenciaService;

    private final Map<String, String> usuarios;

    public AuthController(AuthView view, Runnable aoAutenticar) {

        this.view = view;

        this.aoAutenticar = aoAutenticar;

        this.persistenciaService = new PersistenciaService();

        this.usuarios = carregarUsuariosComTratamento();

        configurarEventos();
    }

    private void configurarEventos() {

        view.getBotaoLogin().setOnAction(evento -> tentarLogin());

        view.getBotaoRegistrar().setOnAction(evento -> tentarRegistro());
    }

    // ================================================================
    // LOGIN
    // ================================================================

    private void tentarLogin() {

        view.limparErroLogin();

        String email = view.getCampoLoginEmail().getText().trim();

        String senha = view.getCampoLoginSenha().getTexto();

        if (email.isBlank() || senha.isBlank()) {

            view.mostrarErroLogin("Preencha e-mail e senha.");

            return;
        }

        String senhaSalva = usuarios.get(email.toLowerCase());

        if (senhaSalva == null || !senhaSalva.equals(senha)) {

            view.mostrarErroLogin("E-mail ou senha inválidos.");

            return;
        }

        // Login OK -> abre a tela do chat (IA)
        aoAutenticar.run();
    }

    // ================================================================
    // REGISTRO
    // ================================================================

    private void tentarRegistro() {

        view.limparErroRegistro();

        String nome = view.getCampoRegNome().getText().trim();

        String email = view.getCampoRegEmail().getText().trim().toLowerCase();

        String senha = view.getCampoRegSenha().getTexto();

        String confirmar = view.getCampoRegConfirmar().getTexto();

        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {

            view.mostrarErroRegistro("Preencha todos os campos.");

            return;
        }

        if (senha.length() < 8) {

            view.mostrarErroRegistro("A senha precisa ter no mínimo 8 caracteres.");

            return;
        }

        if (!senha.equals(confirmar)) {

            view.mostrarErroRegistro("As senhas não coincidem.");

            return;
        }

        if (!view.getCheckTermos().isSelected()) {

            view.mostrarErroRegistro("Você precisa aceitar os termos de uso.");

            return;
        }

        if (usuarios.containsKey(email)) {

            view.mostrarErroRegistro("Já existe uma conta com esse e-mail.");

            return;
        }

        usuarios.put(email, senha);

        salvarUsuariosComTratamento();

        // Conta criada -> volta para a aba de Login em branco
        view.voltarParaLoginAposRegistro();
    }

    // ================================================================
    // PERSISTÊNCIA
    // ================================================================

    private Map<String, String> carregarUsuariosComTratamento() {

        try {

            return persistenciaService.carregarUsuarios();

        } catch (Exception erro) {

            System.err.println(
                    "Erro ao carregar usuários salvos: " + erro.getMessage()
            );

            return new HashMap<>();
        }
    }

    private void salvarUsuariosComTratamento() {

        try {

            persistenciaService.salvarUsuarios(usuarios);

        } catch (Exception erro) {

            System.err.println(
                    "Erro ao salvar usuários: " + erro.getMessage()
            );
        }
    }
}