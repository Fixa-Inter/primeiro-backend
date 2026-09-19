<%@ page import="java.util.List" %>
<%@ page import="com.model.Plano" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<Plano> planos = (List<Plano>) request.getAttribute("planos");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Lista de Planos</title>
</head>

<body>

<h1>Planos cadastrados</h1>

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
        <th>Valor</th>
        <th>Descrição</th>
    </tr>

    <%
        for (Plano plano : planos) {
    %>

    <tr>
        <td><%= plano.getId() %></td>
        <td><%= plano.getNome() %></td>
        <td><%= plano.getValorMensal() %></td>
        <td><%= plano.getDescricao() %></td>
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