<%@ page import="java.util.ArrayList" %>
<%@ page import="com.model.Pagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    ArrayList<Pagamento> pagamentos =
            (ArrayList<Pagamento>) request.getAttribute("pagamentos");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Pagamentos</title>
</head>

<body>

<h1>Pagamentos cadastrados</h1>

<a href="${pageContext.request.contextPath}/pagamentos?action=create">
    Cadastrar novo pagamento
</a>

<br><br>

<form action="${pageContext.request.contextPath}/pagamentos" method="get">

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
            placeholder="Digite o nome do pagamentos"
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
                value="VALOR"
        ${param.campoFiltro == 'VALOR' ? 'selected' : ''}
        >
            Valor
        </option>

        <option
                value="DATA_PAGAMENTO"
        ${param.campoFiltro == 'DATA_PAGAMENTO' ? 'selected' : ''}
        >
            Data Pagamento
        </option>

        <option
                value="FOI_REALIZADO"
        ${param.campoFiltro == 'FOI_REALIZADO' ? 'selected' : ''}
        >
            Foi Realizado
        </option>

        <option
                value="FK_CONTRATO"
        ${param.campoFiltro == 'FK_CONTRATO' ? 'selected' : ''}
        >
            Fk Contrato
        </option>

        <option
                value="METODO_PAGAMENTO"
                ${param.campoFiltro == 'METODO_PAGAMENTO' ? 'selected' : ''}
                >
                    Metodo Pagamento
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
                value="VALOR-ASC"
        ${param.ordenacao == 'VALOR-ASC' ? 'selected' : ''}
        >
            Valor crescente
        </option>

        <option
                value="VALOR-DESC"
        ${param.ordenacao == 'VALOR-DESC' ? 'selected' : ''}
        >
            Valor decrescente
        </option>

    </select>

    <br><br>

    <button type="submit">
        Aplicar
    </button>

    <a href="${pageContext.request.contextPath}/pagamentos">
        Limpar
    </a>

</form>

<br><br>

<%
    if (pagamentos == null) {
%>

<p>A lista de pagamentos não foi enviada pelo Servlet.</p>

<%
} else if (pagamentos.isEmpty()) {
%>

<p>Nenhum pagamento encontrado.</p>

<%
} else {
%>

<table border="1">

    <tr>
        <th>ID</th>
        <th>Valor</th>
        <th>Data_Pagamento</th>
        <th>Foi_Realizado</th>
        <th>Fk_Contrato</th>
        <th>Metodo_Pagamento</th>
    </tr>

    <%
        for (Pagamento pagamento : pagamentos) {
    %>

    <tr>

        <td>
            <%= pagamento.getId() %>
        </td>

        <td>
            <%= pagamento.getValor() %>
        </td>

        <td>
            <%= pagamento.getDataPagamento() %>
        </td>

        <td>
            <%= pagamento.getFoiRealizado() %> meses
        </td>

        <td>
            <%= pagamento.getFkContrato() %>
        </td>

        <td>
            <%= pagamento.getMetodoPagamento() %>
         </td>

        <td>

            <a href="${pageContext.request.contextPath}/pagamentos?action=update&id=<%= pagamento.getId() %>">
                Editar
            </a>

            <form
                    action="${pageContext.request.contextPath}/pagamentos"
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
                        value="<%= pagamento.getId() %>"
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