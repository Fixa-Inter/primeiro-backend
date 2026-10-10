<%@ page import="com.model.Contrato" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Contrato contrato =
            (Contrato) request.getAttribute("contrato");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Editar Contrato</title>
</head>

<body>

<h1>Editar contrato</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<form method="post"
      action="<%= request.getContextPath() %>
            <% if (erro != null && !erro.isBlank()) { %>
            <p>
                <%= erro %>
            </p>
            <% } %>
/contratos">

    <input type="hidden"
           name="action"
           value="update">

    <label>ID:</label>

    <input type="number"
           name="ID"
           value="<%= contrato.getId() %>"
           readonly>

    <br><br>

    <p>
        Data de criação:
        <%= contrato.getDataInicio() %>
    </p>

    <br><br>

    <label>Data de vencimento:</label>

    <input type="date"
           name="DATA_VENCIMENTO"
           value="<%= contrato.getDataVencimento() %>"
           required>

    <br><br>

    <label>ID do endereço:</label>

    <input type="number"
           name="FK_ENDERECO_ID"
           value="<%= contrato.getFkEndereco() %>"
           required>

    <br><br>

    <label>ID do plano:</label>

    <input type="number"
           name="FK_PLANO_ID"
           value="<%= contrato.getFkPlano() %>"
           required>

    <br><br>

    <label>Status do contrato:</label>

    <select name="STATUS_CONTRATO" required>

        <option value="1"
                <%= contrato.getStatusContrato().getCodigo() == 1 ? "selected" : "" %>>
            Ativo
        </option>

        <option value="2"
                <%= contrato.getStatusContrato().getCodigo() == 2 ? "selected" : "" %>>
            Inativo
        </option>

        <option value="3"
                <%= contrato.getStatusContrato().getCodigo() == 3 ? "selected" : "" %>>
            Cancelado
        </option>

    </select>

    <br><br>

    <button type="submit">
        Salvar alterações
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/contratos">
    Voltar
</a>

</body>
</html>