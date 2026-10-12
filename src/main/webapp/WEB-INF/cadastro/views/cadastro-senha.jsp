<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Definir senha</title>
</head>
<body>

<h1>Defina a senha da instituição</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<% if (erro != null && !erro.isBlank()) { %>
    <p style="color: red;"><%= erro %></p>
<% } %>

<form action="${pageContext.request.contextPath}/cadastro" method="post">

    <input type="hidden" name="action" value="definirSenha">

    <label for="senha">Senha:</label>
    <input type="password" id="senha" name="senha" required>
    <br><br>

    <label for="confirmarSenha">Confirmar senha:</label>
    <input type="password" id="confirmarSenha" name="confirmarSenha" required>
    <br><br>

    <button type="submit">Continuar</button>

</form>

</body>
</html>