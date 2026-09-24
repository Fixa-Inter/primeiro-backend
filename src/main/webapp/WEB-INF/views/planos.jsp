<%@ page import="java.util.ArrayList" %>
<%@ page import="com.model.Plano" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Plano> planos =
            (ArrayList<Plano>) request.getAttribute("planos");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Planos</title>
</head>

<body>

<h1>Planos cadastrados</h1>

<a href="${pageContext.request.contextPath}/planos?action=create">
    Cadastrar novo plano
</a>

<br><br>

<form action="${pageContext.request.contextPath}/planos" method="get">

    <input
            type="hidden"
            name="action"
            value="read"
    >

    <!-- BARRA DE PESQUISA -->
    <label for="pesquisa">
        Pesquisar:
    </label>

    <input
            type="text"
            id="pesquisa"
            name="pesquisa"
            value="${param.pesquisa}"
            placeholder="Digite o nome do plano"
    >

    <br><br>


    <label for="campoFiltro">
        Filtrar por:
    </label>

    <select
            name="campoFiltro"
            id="campoFiltro"
    >

        <option value="">
            Sem filtro
        </option>

        <option
                value="NOME"
        ${param.campoFiltro == 'NOME' ? 'selected' : ''}
        >
            Nome
        </option>

        <option
                value="VALOR_MENSAL"
        ${param.campoFiltro == 'VALOR_MENSAL' ? 'selected' : ''}
        >
            Valor mensal
        </option>

        <option
                value="DURACAO_MESES"
        ${param.campoFiltro == 'DURACAO_MESES' ? 'selected' : ''}
        >
            Duração
        </option>

        <option
                value="DESCRICAO"
        ${param.campoFiltro == 'DESCRICAO' ? 'selected' : ''}
        >
            Descrição
        </option>

    </select>


    <label for="valorFiltro">
        Valor:
    </label>

    <input
            type="text"
            id="valorFiltro"
            name="valorFiltro"
            value="${param.valorFiltro}"
            placeholder="Digite o valor"
    >

    <br><br>


    <label for="ordenacao">
        Ordenar por:
    </label>

    <select
            name="ordenacao"
            id="ordenacao"
    >

        <option value="">
            Padrão
        </option>

        <option
                value="NOME-ASC"
        ${param.ordenacao == 'NOME-ASC' ? 'selected' : ''}
        >
            Nome crescente
        </option>

        <option
                value="NOME-DESC"
        ${param.ordenacao == 'NOME-DESC' ? 'selected' : ''}
        >
            Nome decrescente
        </option>

        <option
                value="VALOR_MENSAL-ASC"
        ${param.ordenacao == 'VALOR_MENSAL-ASC' ? 'selected' : ''}
        >
            Valor mensal crescente
        </option>

        <option
                value="VALOR_MENSAL-DESC"
        ${param.ordenacao == 'VALOR_MENSAL-DESC' ? 'selected' : ''}
        >
            Valor mensal decrescente
        </option>

        <option
                value="DURACAO_MESES-ASC"
        ${param.ordenacao == 'DURACAO_MESES-ASC' ? 'selected' : ''}
        >
            Duração crescente
        </option>

        <option
                value="DURACAO_MESES-DESC"
        ${param.ordenacao == 'DURACAO_MESES-DESC' ? 'selected' : ''}
        >
            Duração decrescente
        </option>

    </select>

    <br><br>

    <button type="submit">
        Aplicar
    </button>

    <a href="${pageContext.request.contextPath}/planos">
        Limpar
    </a>

</form>

<br><br>

<%
    if (planos == null) {
%>

<p>A lista de planos não foi enviada pelo Servlet.</p>

<%
} else if (planos.isEmpty()) {
%>

<p>Nenhum plano encontrado.</p>

<%
} else {
%>

<table border="1">

    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>Valor mensal</th>
        <th>Duração</th>
        <th>Descrição</th>
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
            <%= plano.getDuracaoMeses() %> meses
        </td>

        <td>
            <%= plano.getDescricao() %>
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