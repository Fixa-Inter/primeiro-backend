<%@ page import="java.util.List" %>
<%@ page import="com.model.FotoUsuario" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<FotoUsuario> fotos =
            (List<FotoUsuario>) request.getAttribute("fotos");

    String contexto = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Fotos dos Usuários</title>
</head>

<body>

<h1>Fotos dos Usuários</h1>

<a href="<%= contexto %>/fotoUsuario?action=create">
    Cadastrar Foto
</a>

<form method="get" action="<%= contexto %>/fotoUsuario">
    <input type="hidden" name="action" value="read">
    <input type="search" name="pesquisa" value="${param.pesquisa}" placeholder="Pesquisar...">
    <button type="submit">Pesquisar</button>
</form>

<hr>

<table border="1">

    <thead>
    <tr>
        <th>ID</th>
        <th>Data de registro</th>
        <th>URL</th>
        <th>ID do usuário</th>
        <th>Ações</th>
    </tr>
    </thead>

    <tbody>

    <%
        if (fotos != null && !fotos.isEmpty()) {

            for (FotoUsuario foto : fotos) {
    %>

    <tr>

        <td>
            <%= foto.getId() %>
        </td>

        <td>
            <%= foto.getDataRegistro() %>
        </td>

        <td>
            <%= foto.getUrl() %>
        </td>

        <td>
            <%= foto.getFkUsuario() %>
        </td>

        <td>

            <a href="<%= contexto %>/fotoUsuario?action=update&id=<%= foto.getId() %>">
                Editar
            </a>

            <form
                    action="<%= contexto %>/fotoUsuario"
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
                        value="<%= foto.getId() %>"
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
        <td colspan="5">
            Nenhuma foto cadastrada.
        </td>
    </tr>

    <%
        }
    %>

    </tbody>

</table>

</body>

</html>