<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String base = request.getContextPath() + "/login-endereco";
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Endereço adicionado</title>
</head>
<body>
    <main>
        <h1>Endereço adicionado!</h1>
        <p>O novo endereço foi vinculado à sua instituição.</p>

        <a href="<%= base %>?action=adicionar">Adicionar outro endereço</a>
        <br>
        <a href="<%= base %>?action=logout">Sair</a>
    </main>
</body>
</html>
