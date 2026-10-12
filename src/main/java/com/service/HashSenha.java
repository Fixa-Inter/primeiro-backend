package com.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashSenha {

    // gera o hash a partir da senha gerada em texto
    public static String gerarHash(String senha){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));

            // converter os bytes
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            System.out.print("Algoritmo SHA-256 não encontrado: " + e.getMessage());
            return null;
        }
    }

    // confere no login, se a senha digitada bate com o hash salvo (transforma a nova senha em hash e depois compara)
    public static boolean conferir(String senha, String hash){
        String hashDaSenha = gerarHash(senha);
        return hashDaSenha.equals(hash);
    }
}