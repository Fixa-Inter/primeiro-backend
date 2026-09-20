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

<%
    if (planos == null) {
%>

<p>A lista de planos não foi enviada pelo Servlet.</p>

<%
} else if (planos.isEmpty()) {
%>

<p>Nenhum plano cadastrado.</p>

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

            <!-- EDITAR -->
            <a href="${pageContext.request.contextPath}/planos?action=update&id=<%= plano.getId() %>">
                Editar
            </a>

            <!-- EXCLUIR -->
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