<%@ page import="java.util.List" %>
<%@ page import="com.model.Plano" %>
<%@ page import="com.model.Filtro" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Plano> planos =
            (List<Plano>) request.getAttribute("planos");

    List<Filtro> filtros =
            (List<Filtro>) request.getAttribute("filtros");

    String ordenacaoAtual =
            request.getParameter("ordenacao");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Planos</title>
</head>

<body>

<h1>Planos</h1>


<a href="${pageContext.request.contextPath}/planos?action=create">
    Cadastrar novo plano
</a>


<hr>


<form method="get" action="${pageContext.request.contextPath}/planos">
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
            action="${pageContext.request.contextPath}/planos"
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


        <%
            if (ordenacaoAtual != null
                    && !ordenacaoAtual.isBlank()) {
        %>

        <input
                type="hidden"
                name="ordenacao"
                value="<%= ordenacaoAtual %>"
        >

        <%
            }
        %>


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


<br>


<a href="${pageContext.request.contextPath}/planos?action=read">
    Limpar filtros
</a>


<hr>


<h2>Adicionar filtro</h2>

<form
        action="${pageContext.request.contextPath}/planos"
        method="get"
>

    <input
            type="hidden"
            name="action"
            value="read"
    >


    <%
        if (ordenacaoAtual != null
                && !ordenacaoAtual.isBlank()) {
    %>

    <input
            type="hidden"
            name="ordenacao"
            value="<%= ordenacaoAtual %>"
    >

    <%
        }
    %>


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

        <option value="VALOR_MENSAL">
            Valor Mensal
        </option>

        <option value="DURACAO_MESES">
            Duração meses
        </option>

        <option value="DESCRICAO">
            Descrição
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

        <option value="MAIOR_QUE">
            Maior que
        </option>

        <option value="MAIOR_OU_IGUAL">
            Maior ou igual
        </option>

        <option value="MENOR_QUE">
            Menor que
        </option>

        <option value="MENOR_OU_IGUAL">
            Menor ou igual
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
        Adicionar filtro
    </button>

</form>


<hr>


<h2>Ordenação</h2>

<form
        action="${pageContext.request.contextPath}/planos"
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


    <label for="ordenacao">
        Ordenar por:
    </label>

    <select
            id="ordenacao"
            name="ordenacao"
    >

        <option value="">
            Padrão
        </option>


        <option
                value="ID-ASC"
                <%= "ID-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            ID - Crescente
        </option>

        <option
                value="ID-DESC"
                <%= "ID-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            ID - Decrescente
        </option>


        <option
                value="NOME-ASC"
                <%= "NOME-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Nome - Crescente
        </option>

        <option
                value="NOME-DESC"
                <%= "NOME-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Nome - Decrescente
        </option>


        <option
                value="VALOR_MENSAL-ASC"
                <%= "VALOR_MENSAL-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Valor Mensal - Crescente
        </option>

        <option
                value="VALOR_MENSAL-DESC"
                <%= "VALOR_MENSAL-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Valor Mensal - Decrescente
        </option>


        <option
                value="DURACAO_MESES-ASC"
                <%= "DURACAO_MESES-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Duração meses - Crescente
        </option>

        <option
                value="DURACAO_MESES-DESC"
                <%= "DURACAO_MESES-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Duração meses - Decrescente
        </option>


        <option
                value="DESCRICAO-ASC"
                <%= "DESCRICAO-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Descrição - Crescente
        </option>

        <option
                value="DESCRICAO-DESC"
                <%= "DESCRICAO-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Descrição - Decrescente
        </option>

    </select>


    <button type="submit">
        Ordenar
    </button>

</form>


<hr>


<h2>Planos encontrados</h2>


<%
    if (planos == null || planos.isEmpty()) {
%>

<p>Nenhum plano cadastrado.</p>

<%
} else {
%>


<table border="1">

    <tr>

        <th>ID</th>

        <th>Nome</th>

        <th>Valor Mensal</th>

        <th>Duração</th>

        <th>Descrição</th>

        <th>Data de criação</th>

        <th>Ações</th>

    </tr>


    <%
        for (Plano plano : planos) {
    %>

    <tr>

        <td>
            <%= plano.getId() %>
        </td>

        <td>
            <%= plano.getNome() %>
        </td>

        <td>
            <%= plano.getValorMensal() %>
        </td>

        <td>
            <%= plano.getDuracaoMeses() %>
        </td>

        <td>
            <%= plano.getDescricao() %>
        </td>

        <td>
            <%= plano.getDataCriacao() %>
        </td>

        <td>

            <a href="${pageContext.request.contextPath}/planos?action=update&id=<%= plano.getId() %>">
                Editar
            </a>


            <form
                    action="${pageContext.request.contextPath}/planos"
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
                        value="<%= plano.getId() %>"
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

</table>


<%
    }
%>

</body>

</html>