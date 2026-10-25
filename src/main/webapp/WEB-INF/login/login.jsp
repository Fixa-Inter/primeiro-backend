<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entrar - Fixa</title>
</head>
<body>
    <h1>Fixa - Entrar</h1>

    <c:if test="${not empty erro}">
        <p style="color: red;">${erro}</p>
    </c:if>

    <form method="POST" action="/login-handler?action=login">
        <div>
            <input type="radio" id="instituicao" name="tipoLogin" value="instituicao" checked>
            <label for="instituicao">Instituição</label>
        </div>
        <div>
            <input type="radio" id="admin" name="tipoLogin" value="admin">
            <label for="admin">Admin</label>
        </div>

        <div>
            <label for="email">E-mail</label>
            <input type="email" id="email" name="email" required>
        </div>

        <div>
            <label for="senha">Senha</label>
            <input type="password" id="senha" name="senha" required>
        </div>

        <button type="submit">Entrar</button>
    </form>

    <p>
        Não tem conta? <a href="/cadastro">Cadastre-se</a> |
        <a href="/">Voltar</a>
    </p>
</body>
</html>