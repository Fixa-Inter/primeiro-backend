<%@ page import="java.util.List" %>
<%@ page import="com.DTO.UsuarioDTO" %>
<%@ page import="com.model.Filtro" %>
<%@ page import="com.model.enums.OperacaoFiltro" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    List<UsuarioDTO> usuarios = (List<UsuarioDTO>) request.getAttribute("usuarios");
    List<Filtro> filtros = (List<Filtro>) request.getAttribute("filtros");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Usuários</title>
</head>

<body>

<h1>Usuários</h1>

<a href="${pageContext.request.contextPath}/usuarios?action=create">
    Cadastrar usuário
</a>

<form action="${pageContext.request.contextPath}/usuarios" method="get">
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

<hr>

<h2>Filtros</h2>

<% if (filtros != null && !filtros.isEmpty()) { %>

<%
    for (int i = 0; i < filtros.size(); i++) {
        Filtro filtro = filtros.get(i);
%>

<p>
    <strong>Campo:</strong>
        <%= filtro.getCampoFiltravel() %>

    &nbsp;

    <strong>Operação:</strong>
        <%= filtro.getOperacaoFiltro().getNome() %>

    &nbsp;

    <strong>Valor:</strong>
        <%= filtro.getValor() %>

<form action="${pageContext.request.contextPath}/usuarios"
      method="get"
      style="display:inline;">

    <input type="hidden" name="action" value="read">

    <%
        for (int j = 0; j < filtros.size(); j++) {
            if (j == i) {
                continue;
            }

            Filtro outroFiltro = filtros.get(j);
    %>

    <input type="hidden"
           name="campoFiltro"
           value="<%= outroFiltro.getCampoFiltravel() %>">

    <input type="hidden"
           name="valorFiltro"
           value="<%= outroFiltro.getValor() %>">

    <input type="hidden"
           name="operacaoFiltro"
           value="<%= outroFiltro.getOperacaoFiltro().name() %>">

    <%
        }
    %>

    <button type="submit">Remover</button>
</form>
</p>

<%
    }
%>

<form action="${pageContext.request.contextPath}/usuarios"
      method="get">

    <input type="hidden" name="action" value="read">

    <button type="submit">
        Limpar filtros
    </button>

</form>

<% } else { %>

<p>Nenhum filtro aplicado.</p>

<% } %>

<hr>

<h2>Adicionar filtro</h2>

<form action="${pageContext.request.contextPath}/usuarios"
      method="get">

    <input type="hidden" name="action" value="read">

    <label for="campoFiltro">Campo:</label>

    <select name="campoFiltro" id="campoFiltro">

        <option value="ID">ID</option>
        <option value="NOME">Nome</option>
        <option value="ESTA_ATIVO">Está ativo</option>
        <option value="EMAIL">Email</option>
        <option value="CARGO">Cargo</option>
        <option value="TIPO_ACESSO">Tipo de acesso</option>
        <option value="DATA_NASCIMENTO">Data de nascimento</option>
        <option value="PRIMEIRO_ACESSO">Primeiro acesso</option>

    </select>

    <label for="operacaoFiltro">Operação:</label>

    <select name="operacaoFiltro" id="operacaoFiltro">

        <option value="IGUAL">Igual</option>
        <option value="CONTEM">Contém</option>
        <option value="MAIOR_QUE">Maior que</option>
        <option value="MAIOR_OU_IGUAL">Maior ou igual</option>
        <option value="MENOR_QUE">Menor que</option>
        <option value="MENOR_OU_IGUAL">Menor ou igual</option>

    </select>

    <label for="valorFiltro">Valor:</label>

    <input type="text"
           name="valorFiltro"
           id="valorFiltro">

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

    <button type="submit">
        Adicionar filtro
    </button>

</form>

<hr>

<h2>Ordenação</h2>

<form action="${pageContext.request.contextPath}/usuarios"
      method="get">

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

    <label for="ordenacao">Ordenar por:</label>

    <select name="ordenacao" id="ordenacao">

        <option value="ID-ASC">ID - Crescente</option>
        <option value="ID-DESC">ID - Decrescente</option>

        <option value="NOME-ASC">Nome - Crescente</option>
        <option value="NOME-DESC">Nome - Decrescente</option>

        <option value="ESTA_ATIVO-ASC">Está ativo - Crescente</option>
        <option value="ESTA_ATIVO-DESC">Está ativo - Decrescente</option>

        <option value="EMAIL-ASC">Email - Crescente</option>
        <option value="EMAIL-DESC">Email - Decrescente</option>

        <option value="CARGO-ASC">Cargo - Crescente</option>
        <option value="CARGO-DESC">Cargo - Decrescente</option>

        <option value="TIPO_ACESSO-ASC">Tipo de acesso - Crescente</option>
        <option value="TIPO_ACESSO-DESC">Tipo de acesso - Decrescente</option>

        <option value="DATA_NASCIMENTO-ASC">Data de nascimento - Crescente</option>
        <option value="DATA_NASCIMENTO-DESC">Data de nascimento - Decrescente</option>

        <option value="PRIMEIRO_ACESSO-ASC">Primeiro acesso - Crescente</option>
        <option value="PRIMEIRO_ACESSO-DESC">Primeiro acesso - Decrescente</option>

    </select>

    <button type="submit">
        Ordenar
    </button>

</form>

<hr>

<h2>Lista de usuários</h2>

<% if (usuarios != null && !usuarios.isEmpty()) { %>

<table border="1">

    <thead>
    <tr>
        <th>ID</th>
        <th>Nome</th>
        <th>Está ativo</th>
        <th>Email</th>
        <th>Cargo</th>
        <th>Tipo de acesso</th>
        <th>Data de nascimento</th>
        <th>Primeiro acesso</th>
        <th>Estado</th>
        <th>Instituição</th>
        <th>Ações</th>
    </tr>
    </thead>

    <tbody>

    <% for (UsuarioDTO usuario : usuarios) { %>

    <tr>

        <td>
            <%= usuario.getId() %>
        </td>

        <td>
            <%= usuario.getNome() %>
        </td>

        <td>
            <%= usuario.getEstaAtivo() %>
        </td>

        <td>
            <%= usuario.getEmail() %>
        </td>

        <td>
            <%= usuario.getCargo() %>
        </td>

        <td>
            <%= usuario.getTipoAcesso() %>
        </td>

        <td>
            <%= usuario.getDataNascimento() %>
        </td>

        <td>
            <%= usuario.getPrimeiroAcesso() %>
        </td>

        <td>
            <%= usuario.getEstado() %>
        </td>

        <td>
            <%= usuario.getInstituicaoNome() %>
        </td>

        <td>

            <a href="${pageContext.request.contextPath}/usuarios?action=update&id=<%= usuario.getId() %>">
                Editar
            </a>

            <form action="${pageContext.request.contextPath}/usuarios"
                  method="post"
                  style="display:inline;">

                <input type="hidden"
                       name="action"
                       value="delete">

                <input type="hidden"
                       name="id"
                       value="<%= usuario.getId() %>">

                <button type="submit">
                    Excluir
                </button>

            </form>

        </td>

    </tr>

    <% } %>

    </tbody>

</table>

<% } %>

</body>
</html>
