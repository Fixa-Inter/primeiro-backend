<%@ page import="java.util.List" %>
<%@ page import="com.model.SuperAdministrador" %>
<%@ page import="com.model.Filtro" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<SuperAdministrador> superAdmins =
            (List<SuperAdministrador>) request.getAttribute("superAdmins");

    List<Filtro> filtros =
            (List<Filtro>) request.getAttribute("filtros");

    String ordenacaoAtual =
            request.getParameter("ordenacao");

    String contexto = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Super Administradores</title>
</head>

<body>

<h1>Super Administradores</h1>

<a href="<%= contexto %>/superAdmin?action=create">
    Cadastrar Super Administrador
</a>

<hr>

<form method="get" action="<%= contexto %>/superAdmin">
    <input type="hidden" name="action" value="read">
    <input type="search" name="pesquisa" value="${param.pesquisa}" placeholder="Pesquisar...">
    <% if (filtros != null) for (Filtro filtro : filtros) { %>
    <input type="hidden" name="campoFiltro" value="<%= filtro.getCampoFiltravel() %>">
    <input type="hidden" name="valorFiltro" value="<%= filtro.getValor() %>">
    <input type="hidden" name="operacaoFiltro" value="<%= filtro.getOperacaoFiltro().name() %>">
    <% } %>
    <% if (ordenacaoAtual != null && !ordenacaoAtual.isBlank()) { %>
    <input type="hidden" name="ordenacao" value="<%= ordenacaoAtual %>">
    <% } %>
    <button type="submit">Pesquisar</button>
</form>

<h2>Filtros aplicados</h2>

<%
    if (filtros == null || filtros.isEmpty()) {
%>

<p>Nenhum filtro aplicado.</p>

<%
} else {

    for (int i = 0; i < filtros.size(); i++) {

        Filtro filtro = filtros.get(i);
%>

<div>

    <strong>
        <%= filtro.getCampoFiltravel() %>
    </strong>

    <%= filtro.getOperacaoFiltro().getNome() %>

    <%= filtro.getValor() %>

    <form
            action="<%= contexto %>/superAdmin"
            method="get"
            style="display:inline"
    >

        <input
                type="hidden"
                name="action"
                value="read"
        >

        <input
                type="hidden"
                name="removerFiltro"
                value="<%= i %>"
        >

        <% if (ordenacaoAtual != null && !ordenacaoAtual.isBlank()) { %>

        <input
                type="hidden"
                name="ordenacao"
                value="<%= ordenacaoAtual %>"
        >

        <% } %>

        <%
            for (Filtro filtroAtual : filtros) {
        %>

        <input
                type="hidden"
                name="campoFiltro"
                value="<%= filtroAtual.getCampoFiltravel() %>"
        >

        <input
                type="hidden"
                name="operacaoFiltro"
                value="<%= filtroAtual.getOperacaoFiltro().name() %>"
        >

        <input
                type="hidden"
                name="valorFiltro"
                value="<%= filtroAtual.getValor() %>"
        >

        <%
            }
        %>

        <button type="submit">
            Excluir filtro
        </button>

    </form>

</div>

<%
        }
    }
%>

<hr>

<h2>Adicionar filtro</h2>

<form
        action="<%= contexto %>/superAdmin"
        method="get"
>

    <input
            type="hidden"
            name="action"
            value="read"
    >

    <% if (ordenacaoAtual != null && !ordenacaoAtual.isBlank()) { %>

    <input
            type="hidden"
            name="ordenacao"
            value="<%= ordenacaoAtual %>"
    >

    <% } %>

    <%
        if (filtros != null) {

            for (Filtro filtro : filtros) {
    %>

    <input
            type="hidden"
            name="campoFiltro"
            value="<%= filtro.getCampoFiltravel() %>"
    >

    <input
            type="hidden"
            name="operacaoFiltro"
            value="<%= filtro.getOperacaoFiltro().name() %>"
    >

    <input
            type="hidden"
            name="valorFiltro"
            value="<%= filtro.getValor() %>"
    >

    <%
            }
        }
    %>

    <label for="campoFiltro">
        Campo:
    </label>

    <select
            id="campoFiltro"
            name="campoFiltro"
    >

        <option value="ID">
            ID
        </option>

        <option value="NOME">
            Nome
        </option>

        <option value="EMAIL">
            Email
        </option>

    </select>

    <label for="operacaoFiltro">
        Operação:
    </label>

    <select
            id="operacaoFiltro"
            name="operacaoFiltro"
    >

        <option value="IGUAL">
            Igual
        </option>

        <option value="CONTEM">
            Contém
        </option>

    </select>

    <label for="valorFiltro">
        Valor:
    </label>

    <input
            type="text"
            id="valorFiltro"
            name="valorFiltro"
            required
    >

    <button type="submit">
        Filtrar
    </button>

</form>

<hr>

<h2>Ordenação</h2>

<form
        action="<%= contexto %>/superAdmin"
        method="get"
>

    <input
            type="hidden"
            name="action"
            value="read"
    >

    <%
        if (filtros != null) {

            for (Filtro filtro : filtros) {
    %>

    <input
            type="hidden"
            name="campoFiltro"
            value="<%= filtro.getCampoFiltravel() %>"
    >

    <input
            type="hidden"
            name="operacaoFiltro"
            value="<%= filtro.getOperacaoFiltro().name() %>"
    >

    <input
            type="hidden"
            name="valorFiltro"
            value="<%= filtro.getValor() %>"
    >

    <%
            }
        }
    %>

    <select name="ordenacao">

        <option value="">
            Padrão
        </option>

        <option value="ID-ASC"
                <%= "ID-ASC".equals(ordenacaoAtual) ? "selected" : "" %>>
            ID crescente
        </option>

        <option value="ID-DESC"
                <%= "ID-DESC".equals(ordenacaoAtual) ? "selected" : "" %>>
            ID decrescente
        </option>

        <option value="NOME-ASC"
                <%= "NOME-ASC".equals(ordenacaoAtual) ? "selected" : "" %>>
            Nome crescente
        </option>

        <option value="NOME-DESC"
                <%= "NOME-DESC".equals(ordenacaoAtual) ? "selected" : "" %>>
            Nome decrescente
        </option>

        <option value="EMAIL-ASC"
                <%= "EMAIL-ASC".equals(ordenacaoAtual) ? "selected" : "" %>>
            Email crescente
        </option>

        <option value="EMAIL-DESC"
                <%= "EMAIL-DESC".equals(ordenacaoAtual) ? "selected" : "" %>>
            Email decrescente
        </option>

    </select>

    <button type="submit">
        Ordenar
    </button>

</form>

<form
        action="<%= contexto %>/superAdmin"
        method="get"
        style="display:inline"
>

    <input
            type="hidden"
            name="action"
            value="read"
    >

    <button type="submit">
        Limpar filtros
    </button>

</form>

<hr>

<h2>Lista de Super Administradores</h2>

<%
    if (superAdmins != null && !superAdmins.isEmpty()) {
%>

<table border="1">

    <thead>

    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>Email</th>
        <th>Ações</th>
    </tr>

    </thead>

    <tbody>

    <%
        for (SuperAdministrador adm : superAdmins) {
    %>

    <tr>

        <td>
            <%= adm.getId() %>
        </td>

        <td>
            <%= adm.getNome() %>
        </td>

        <td>
            <%= adm.getEmail() %>
        </td>

        <td>

            <a href="<%= contexto %>/superAdmin?action=update&id=<%= adm.getId() %>">
                Editar
            </a>

            <form
                    action="<%= contexto %>/superAdmin"
                    method="post"
                    style="display:inline"
            >

                <input
                        type="hidden"
                        name="action"
                        value="delete"
                >

                <input
                        type="hidden"
                        name="id"
                        value="<%= adm.getId() %>"
                >

                <button type="submit">
                    Excluir
                </button>

            </form>

        </td>

    </tr>

    <%
        }
    %>

    </tbody>

</table>

<%
} else {
%>

<p>Nenhum Super Administrador encontrado.</p>

<%
    }
%>

</body>

</html>