<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.model.Instituicao" %>

<%
    Instituicao instituicao =
            (Instituicao) request.getAttribute("instituicao");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Editar Instituição</title>
</head>

<body>

<h1>Editar Instituição</h1>

<%
    if (instituicao == null) {
%>

<p>Instituição não encontrada.</p>

<a href="${pageContext.request.contextPath}/instituicoes">
    Voltar
</a>

<%
} else {
%>

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
            value="update"
    >

    <input
            type="hidden"
            name="id"
            value="<%= instituicao.getId() %>"
    >

    <p>
        <strong>ID:</strong>
        <%= instituicao.getId() %>
    </p>

    <label for="nome">
        Nome:
    </label>

    <input
            type="text"
            id="nome"
            name="nome"
            value="<%= instituicao.getNome() %>"
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
            value="<%= instituicao.getEmailCorporativo() %>"
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
            value="<%= instituicao.getDominioEmail() %>"
            maxlength="100"
            required
    >

    <br><br>

    <label for="tipoInstituicao">
        Tipo de instituição:
    </label>

    <select id="tipoInstituicao" name="tipoInstituicao" required>
        <option value="1" <%= instituicao.getTipoInstituicao().getCodigo() == 1 ? "selected" : "" %>>Escola</option>
        <option value="2" <%= instituicao.getTipoInstituicao().getCodigo() == 2 ? "selected" : "" %>>Faculdade</option>
        <option value="3" <%= instituicao.getTipoInstituicao().getCodigo() == 3 ? "selected" : "" %>>Empresa</option>
        <option value="4" <%= instituicao.getTipoInstituicao().getCodigo() == 4 ? "selected" : "" %>>Órgão público</option>
    </select>

    <br><br>

    <p>
        <strong>Data de cadastro:</strong>
        <%= instituicao.getDataCadastro() %>
    </p>

    <button type="submit">
        Salvar alterações
    </button>

    <a href="${pageContext.request.contextPath}/instituicoes">
        Cancelar
    </a>

</form>

<%
    }
%>

</body>

</html>
