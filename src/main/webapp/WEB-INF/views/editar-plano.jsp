<%@ page import="com.model.Plano" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Plano plano = (Plano) request.getAttribute("plano");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Editar Plano</title>
</head>

<body>

<h1>Editar Plano</h1>

<form
        action="${pageContext.request.contextPath}/planos"
        method="post"
>

    <input
            type="hidden"
            name="action"
            value="update"
    >

    <input
            type="hidden"
            name="id"
            value="<%= plano.getId() %>"
    >


    <label for="nome">
        Nome:
    </label>

    <input
            type="text"
            id="nome"
            name="nome"
            value="<%= plano.getNome() %>"
            required
    >

    <br><br>


    <label for="valorMensal">
        Valor mensal:
    </label>

    <input
            type="number"
            id="valorMensal"
            name="valorMensal"
            value="<%= plano.getValorMensal() %>"
            step="0.01"
            min="0"
            required
    >

    <br><br>


    <label for="duracaoMeses">
        Duração em meses:
    </label>

    <input
            type="number"
            id="duracaoMeses"
            name="duracaoMeses"
            value="<%= plano.getDuracaoMeses() %>"
            min="1"
            required
    >

    <br><br>


    <label for="descricao">
        Descrição:
    </label>

    <input
            type="text"
            id="descricao"
            name="descricao"
            value="<%= plano.getDescricao() %>"
    >

    <br><br>


    <button type="submit">
        Salvar alterações
    </button>

    <a href="${pageContext.request.contextPath}/planos">
        Cancelar
    </a>

</form>

</body>

</html>