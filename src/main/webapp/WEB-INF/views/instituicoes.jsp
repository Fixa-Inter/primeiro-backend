<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.Instituicao" %>
<%@ page import="com.model.Filtro" %>

<%
    List<Instituicao> instituicoes =
            (List<Instituicao>) request.getAttribute("instituicoes");

    List<Filtro> filtros =
            (List<Filtro>) request.getAttribute("filtros");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Instituições</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
        }

        h1 {
            margin-bottom: 20px;
        }

        .acoes {
            margin-bottom: 20px;
        }

        .acoes a,
        button {
            padding: 8px 12px;
            margin-right: 5px;
            text-decoration: none;
            cursor: pointer;
        }

        .filtro {
            border: 1px solid #ccc;
            padding: 15px;
            margin-bottom: 20px;
        }

        .filtro form {
            display: flex;
            gap: 10px;
            align-items: center;
            flex-wrap: wrap;
        }

        select,
        input {
            padding: 7px;
        }

        table {
            border-collapse: collapse;
            width: 100%;
        }

        th,
        td {
            border: 1px solid #ccc;
            padding: 10px;
            text-align: left;
        }

        th {
            background-color: #eee;
        }

        .filtros-ativos {
            margin-bottom: 20px;
        }

        .filtro-ativo {
            display: inline-block;
            border: 1px solid #aaa;
            padding: 7px;
            margin: 3px;
        }

        .filtro-ativo form {
            display: inline;
        }

        .ordenacao {
            margin-bottom: 20px;
        }
    </style>
</head>

<body>

<h1>Instituições</h1>

<div class="acoes">
    <a href="${pageContext.request.contextPath}/instituicoes?action=create">
        Cadastrar instituição
    </a>

    <a href="${pageContext.request.contextPath}/index.jsp">
        Voltar
    </a>
</div>

<form method="get" action="${pageContext.request.contextPath}/instituicoes">
    <input type="hidden" name="action" value="read">
    <input type="search" name="pesquisa" value="${param.pesquisa}" placeholder="Pesquisar...">
    <% if (filtros != null) for (Filtro filtro : filtros) { %>
    <input type="hidden" name="campoFiltro" value="<%= filtro.getCampoFiltravel() %>">
    <input type="hidden" name="valorFiltro" value="<%= filtro.getValor() %>">
    <input type="hidden" name="operacaoFiltro" value="<%= filtro.getOperacaoFiltro().name() %>">
    <% } %>
    <% if (request.getParameter("ordenacao") != null) { %>
    <input type="hidden" name="ordenacao" value="<%= request.getParameter("ordenacao") %>">
    <% } %>
    <button type="submit">Pesquisar</button>
</form>

<!-- ========================= -->
<!-- FILTRO -->
<!-- ========================= -->

<div class="filtro">

    <h3>Adicionar filtro</h3>

    <form method="get"
          action="${pageContext.request.contextPath}/instituicoes">

        <input type="hidden" name="action" value="read">

        <select name="campoFiltro" required>

            <option value="">Campo</option>

            <option value="ID">ID</option>
            <option value="NOME">Nome</option>
            <option value="EMAIL_CORPORATIVO">
                Email corporativo
            </option>
            <option value="DATA_CADASTRO">
                Data de cadastro
            </option>
            <option value="DOMINIO_EMAIL">
                Domínio do email
            </option>
            <option value="TIPO_INSTITUICAO">
                Tipo de instituição
            </option>

        </select>


        <select name="operacaoFiltro" required>

            <option value="">Operação</option>

            <option value="IGUAL">Igual</option>
            <option value="MAIOR_QUE">Maior que</option>
            <option value="MAIOR_OU_IGUAL">
                Maior ou igual
            </option>
            <option value="MENOR_QUE">
                Menor que
            </option>
            <option value="MENOR_OU_IGUAL">
                Menor ou igual
            </option>
            <option value="CONTEM">
                Contêm
            </option>

        </select>


        <input
                type="text"
                name="valorFiltro"
                placeholder="Valor"
                required
        >


        <button type="submit">
            Adicionar filtro
        </button>

    </form>

</div>


<!-- ========================= -->
<!-- FILTROS ATIVOS -->
<!-- ========================= -->

<%
    if (filtros != null && !filtros.isEmpty()) {
%>

<div class="filtros-ativos">

    <h3>Filtros ativos</h3>

    <%
        for (int i = 0; i < filtros.size(); i++) {

            Filtro filtro = filtros.get(i);
    %>

    <div class="filtro-ativo">

        <strong>
            <%= filtro.getCampoFiltravel() %>
        </strong>

        <%= filtro.getOperacaoFiltro().getNome() %>

        <%= filtro.getValor() %>


        <form method="get"
              action="${pageContext.request.contextPath}/instituicoes">

            <input
                    type="hidden"
                    name="removerFiltro"
                    value="<%= i %>"
            >

            <%
                for (Filtro f : filtros) {
            %>

            <input
                    type="hidden"
                    name="campoFiltro"
                    value="<%= f.getCampoFiltravel() %>"
            >

            <input
                    type="hidden"
                    name="valorFiltro"
                    value="<%= f.getValor() %>"
            >

            <input
                    type="hidden"
                    name="operacaoFiltro"
                    value="<%= f.getOperacaoFiltro().name() %>"
            >

            <%
                }
            %>

            <button type="submit">
                Remover
            </button>

        </form>

    </div>

    <%
        }
    %>

</div>

<%
    }
%>


<!-- ========================= -->
<!-- ORDENAÇÃO -->
<!-- ========================= -->

<div class="ordenacao">

    <h3>Ordenação</h3>

    <form method="get"
          action="${pageContext.request.contextPath}/instituicoes">

        <select name="ordenacao">

            <option value="">
                Padrão: ID ASC
            </option>

            <option value="ID-ASC">
                ID ↑
            </option>

            <option value="ID-DESC">
                ID ↓
            </option>

            <option value="NOME-ASC">
                Nome ↑
            </option>

            <option value="NOME-DESC">
                Nome ↓
            </option>

            <option value="EMAIL_CORPORATIVO-ASC">
                Email corporativo ↑
            </option>

            <option value="EMAIL_CORPORATIVO-DESC">
                Email corporativo ↓
            </option>

            <option value="DATA_CADASTRO-ASC">
                Data de cadastro ↑
            </option>

            <option value="DATA_CADASTRO-DESC">
                Data de cadastro ↓
            </option>

            <option value="DOMINIO_EMAIL-ASC">
                Domínio do email ↑
            </option>

            <option value="DOMINIO_EMAIL-DESC">
                Domínio do email ↓
            </option>

            <option value="TIPO_INSTITUICAO-ASC">
                Tipo de instituição ↑
            </option>

            <option value="TIPO_INSTITUICAO-DESC">
                Tipo de instituição ↓
            </option>

        </select>


        <%
            /*
             * Mantém os filtros existentes quando ordenar.
             */
            if (filtros != null) {

                for (Filtro f : filtros) {
        %>

        <input
                type="hidden"
                name="campoFiltro"
                value="<%= f.getCampoFiltravel() %>"
        >

        <input
                type="hidden"
                name="valorFiltro"
                value="<%= f.getValor() %>"
        >

        <input
                type="hidden"
                name="operacaoFiltro"
                value="<%= f.getOperacaoFiltro().name() %>"
        >

        <%
                }
            }
        %>


        <button type="submit">
            Ordenar
        </button>

    </form>

</div>


<!-- ========================= -->
<!-- TABELA -->
<!-- ========================= -->

<table>

    <thead>

    <tr>

        <th>ID</th>
        <th>Nome</th>
        <th>Email corporativo</th>
        <th>Data de cadastro</th>
        <th>Domínio do email</th>
        <th>Tipo de instituição</th>
        <th>Ações</th>

    </tr>

    </thead>


    <tbody>

    <%
        if (instituicoes != null && !instituicoes.isEmpty()) {

            for (Instituicao instituicao : instituicoes) {
    %>

    <tr>

        <td>
            <%= instituicao.getId() %>
        </td>

        <td>
            <%= instituicao.getNome() %>
        </td>

        <td>
            <%= instituicao.getEmailCorporativo() %>
        </td>

        <td>
            <%= instituicao.getDataCadastro() %>
        </td>

        <td>
            <%= instituicao.getDominioEmail() %>
        </td>

        <td>
            <%= instituicao.getTipoInstituicao() %>
        </td>

        <td>

            <a href="${pageContext.request.contextPath}/instituicoes?action=update&id=<%= instituicao.getId() %>">
                Editar
            </a>


            <form
                    method="post"
                    action="${pageContext.request.contextPath}/instituicoes"
                    style="display:inline;"
            >

                <input
                        type="hidden"
                        name="action"
                        value="delete"
                >

                <input
                        type="hidden"
                        name="id"
                        value="<%= instituicao.getId() %>"
                >

                <button type="submit">
                    Excluir
                </button>

            </form>

        </td>

    </tr>

    <%
        }

    } else {
    %>

    <tr>

        <td colspan="7">
            Nenhuma instituição encontrada.
        </td>

    </tr>

    <%
        }
    %>

    </tbody>

</table>

</body>
</html>