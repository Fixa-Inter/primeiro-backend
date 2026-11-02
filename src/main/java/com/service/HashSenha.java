package com.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

// Gera e confere o hash das senhas
public class HashSenha {

    // Gera o hash da senha em texto
    public static String gerarHash(String senha){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));

            // Converte os bytes para texto em Base64
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            System.out.print("Algoritmo SHA-256 não encontrado: " + e.getMessage());
            return null;
        }
    }

    // Confere se a senha digitada bate com o hash salvo
    public static boolean conferir(String senha, String hash){
        String hashDaSenha = gerarHash(senha);
        return hashDaSenha.equals(hash);
    }
}