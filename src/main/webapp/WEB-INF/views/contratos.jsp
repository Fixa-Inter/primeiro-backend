<%@ page import="java.util.List" %>
<%@ page import="com.model.Contrato" %>
<%@ page import="com.model.Filtro" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Contrato> contratos =
            (List<Contrato>) request.getAttribute("contratos");

    List<Filtro> filtros =
            (List<Filtro>) request.getAttribute("filtros");

    String ordenacaoAtual =
            request.getParameter("ordenacao");

    String contexto = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Contratos</title>
</head>

<body>

<h1>Contratos</h1>

<a href="<%= contexto %>/contratos?action=create">
    Cadastrar contrato
</a>

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
            action="${pageContext.request.contextPath}/contratos"
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


<hr>

<h2>Filtros</h2>

<form method="get" action="<%= contexto %>/contratos">

    <input type="hidden" name="action" value="read">

    <%
        if (filtros != null) {
            for (Filtro filtro : filtros) {
    %>

    <input type="hidden"
           name="campoFiltro"
           value="<%= filtro.getCampoFiltravel() %>">

    <input type="hidden"
           name="operacaoFiltro"
           value="<%= filtro.getOperacaoFiltro().name() %>">

    <input type="hidden"
           name="valorFiltro"
           value="<%= filtro.getValor() %>">

    <%
            }
        }
    %>


    <label>Campo:</label>

    <select name="campoFiltro">

        <option value="ID">
            ID
        </option>

        <option value="DATA_INICIO">
            Data de início
        </option>

        <option value="DATA_VENCIMENTO">
            Data de vencimento
        </option>

        <option value="FK_ENDERECO_ID">
            ID do endereço
        </option>

        <option value="FK_PLANO_ID">
            ID do plano
        </option>

        <option value="STATUS_CONTRATO">
            Status do contrato
        </option>

    </select>


    <label>Operação:</label>

    <select name="operacaoFiltro">

        <option value="IGUAL">
            Igual
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


    <label>Valor:</label>

    <input type="text"
           name="valorFiltro">


    <button type="submit">
        Filtrar
    </button>

</form>

<hr>

<h2>Ordenação</h2>

<form method="get" action="<%= contexto %>/contratos">

    <input type="hidden" name="action" value="read">

    <%
        if (filtros != null) {
            for (Filtro filtro : filtros) {
    %>

    <input type="hidden"
           name="campoFiltro"
           value="<%= filtro.getCampoFiltravel() %>">

    <input type="hidden"
           name="valorFiltro"
           value="<%= filtro.getValor() %>">

    <input type="hidden"
           name="operacaoFiltro"
           value="<%= filtro.getOperacaoFiltro().name() %>">

    <%
            }
        }
    %>

    <select name="ordenacao">

        <option value="">Padrão</option>

        <option value="DATA_INICIO-ASC">
            Data de início crescente
        </option>

        <option value="DATA_INICIO-DESC">
            Data de início decrescente
        </option>

        <option value="DATA_VENCIMENTO-ASC">
            Data de vencimento crescente
        </option>

        <option value="DATA_VENCIMENTO-DESC">
            Data de vencimento decrescente
        </option>

        <option value="FK_PLANO_ID-ASC">
            Plano crescente
        </option>

        <option value="FK_PLANO_ID-DESC">
            Plano decrescente
        </option>

    </select>

    <button type="submit">
        Ordenar
    </button>

</form>

<hr>

<h2>Lista de contratos</h2>

<%
    if (contratos != null && !contratos.isEmpty()) {
%>

<table border="1">

    <thead>
    <tr>
        <th>ID</th>
        <th>Data de início</th>
        <th>Data de vencimento</th>
        <th>ID do plano</th>
        <th>ID do endereço</th>
        <th>Status</th>
        <th>Ações</th>
    </tr>
    </thead>

    <tbody>

    <%
        for (Contrato contrato : contratos) {
    %>

    <tr>

        <td>
            <%= contrato.getId() %>
        </td>

        <td>
            <%= contrato.getDataInicio() %>
        </td>

        <td>
            <%= contrato.getDataVencimento() %>
        </td>

        <td>
            <%= contrato.getFkPlano() %>
        </td>

        <td>
            <%= contrato.getFkEndereco() %>
        </td>

        <td>
            <%= contrato.getStatusContrato() %>
        </td>

        <td>

            <a href="<%= contexto %>/contratos?action=update&id=<%= contrato.getId() %>">
                Editar
            </a>

            <form method="post"
                  action="<%= contexto %>/contratos"
                  style="display:inline;">

                <input type="hidden"
                       name="action"
                       value="delete">

                <input type="hidden"
                       name="id"
                       value="<%= contrato.getId() %>">

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

<p>Nenhum contrato encontrado.</p>

<%
    }
%>

</body>
</html>