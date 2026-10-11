<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Instituição</title>
</head>

<body>

<h1>Cadastrar Instituição</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<form
        action="${pageContext.request.contextPath}/instituicoes"
        method="post"
>
            <% if (erro != null && !erro.isBlank()) { %>
            <p>
                <%= erro %>
            </p>
            <% } %>


    <input
            type="hidden"
            name="action"
            value="create"
    >

    <label for="nome">
        Nome:
    </label>

    <input
            type="text"
            id="nome"
            name="nome"
            maxlength="50"
            required
    >

    <br><br>

    <label for="emailCorporativo">
        E-mail corporativo:
    </label>

    <input
            type="email"
            id="emailCorporativo"
            name="emailCorporativo"
            maxlength="50"
    >

    <br><br>

    <label for="dominioEmail">
        Domínio do e-mail:
    </label>

    <input
            type="text"
            id="dominioEmail"
            name="dominioEmail"
            maxlength="100"
            required
    >

    <br><br>

    <label for="tipoInstituicao">
        Tipo de instituição:
    </label>

    <select id="tipoInstituicao" name="tipoInstituicao" required>
        <option value="">Selecione o tipo</option>
        <option value="1">Escola</option>
        <option value="2">Faculdade</option>
        <option value="3">Empresa</option>
        <option value="4">Órgão público</option>
    </select>

    <br><br>

    <button type="submit">
        Cadastrar
    </button>

    <a href="${pageContext.request.contextPath}/instituicoes">
        Cancelar
    </a>

</form>

</body>

</html>
