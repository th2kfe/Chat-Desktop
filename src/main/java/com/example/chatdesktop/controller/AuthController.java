package com.example.chatdesktop.controller;

import com.example.chatdesktop.view.AuthView;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador da tela de login/registro.
 *
 * OBS: aqui não existe um banco de dados real — as contas
 * ficam guardadas em memória (Map) só para fins de demonstração.
 * Elas são perdidas a cada vez que você fecha e abre o app.
 */
public class AuthController {

    private final AuthView view;

    private final Runnable aoAutenticar;

    private final Map<String, String> usuarios = new HashMap<>();

    public AuthController(AuthView view, Runnable aoAutenticar) {

        this.view = view;

        this.aoAutenticar = aoAutenticar;

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

        System.out.println("BOTAO ENTRAR CLICADO");

        view.limparErroLogin();

        String email = view.getCampoLoginEmail().getText().trim();

        String senha = view.getCampoLoginSenha().getTexto();

        System.out.println("Email digitado: [" + email + "]");

        System.out.println("Senha digitada: [" + senha + "]");

        System.out.println("Senha salva para esse email: [" + usuarios.get(email.toLowerCase()) + "]");

        if (email.isBlank() || senha.isBlank()) {

            view.mostrarErroLogin("Preencha e-mail e senha.");

            return;
        }

        String senhaSalva = usuarios.get(email.toLowerCase());

        if (senhaSalva == null || !senhaSalva.equals(senha)) {

            view.mostrarErroLogin("E-mail ou senha inválidos.");

            return;
        }

        System.out.println("LOGIN VALIDADO -> chamando aoAutenticar.run()");

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

        System.out.println("CONTA CRIADA: " + email);

        // Conta criada -> volta para a aba de Login em branco
        view.voltarParaLoginAposRegistro();
    }
}