<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String erro = (String) request.getAttribute("erro");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
    <main>
        <h1>Entrar</h1>
        <form method="post" action="${pageContext.request.contextPath}/login-handler?action=login">
            <div>
                <label for="email">E-mail</label>
                <input type="email" id="email" name="email" autocomplete="email" placeholder="nome@email.com" required>
            </div>
            <div>
                <label for="senha">Senha</label>
                <input type="password" id="senha" name="senha" autocomplete="off" placeholder="senha123" required>
            </div>
            <button action="" type="submit">Entrar</button>
        </form>
        <a href="${pageContext.request.contextPath}/index.html">Voltar ao início</a>
        <% if (erro != null) { %>
        <p>
            <%= erro %>
        </p>
        <% } %>
    </main>
</body>
</html>
