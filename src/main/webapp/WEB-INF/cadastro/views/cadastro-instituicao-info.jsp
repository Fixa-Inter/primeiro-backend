<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Instituição</title>
</head>
<body>

<h1>Informações da Instituição</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<% if (erro != null && !erro.isBlank()) { %>
    <p style="color: red;"><%= erro %></p>
<% } %>

<form action="${pageContext.request.contextPath}/cadastro" method="post">

    <input type="hidden" name="action" value="enviarCodigo">

    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" maxlength="50" required>
    <br><br>

    <label for="tipoInstituicao">Tipo de instituição:</label>
    <select id="tipoInstituicao" name="tipoInstituicao" required>
        <option value="">Selecione...</option>
        <option value="Escola">Escola</option>
        <option value="Faculdade">Faculdade</option>
        <option value="Empresa">Empresa</option>
        <option value="Órgão público">Órgão público</option>
    </select>
    <br><br>

    <label for="emailCorporativo">E-mail corporativo:</label>
    <input type="email" id="emailCorporativo" name="emailCorporativo" maxlength="50" required>
    <br><br>

    <button type="submit">Enviar código</button>

    <a href="${pageContext.request.contextPath}/cadastro?action=escolher">Cancelar</a>

</form>

</body>
</html>