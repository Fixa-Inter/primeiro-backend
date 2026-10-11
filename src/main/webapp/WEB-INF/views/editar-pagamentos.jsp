<%@ page import="com.model.Pagamento" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Pagamento pagamento = (Pagamento) request.getAttribute("pagamento");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Editar Pagamento</title>
</head>

<body>

<h1>Editar Pagamento</h1>

<form
        action="${pageContext.request.contextPath}/pagamentos"
        method="post"
>

    <input
            type="hidden"
            name="action"
            value="update"
    >

    <input
            type="hidden"
            name="id"
            value="<%= pagamento.getId() %>"
    >


    <label for="valor">
        Valor:
    </label>

    <input
            type="number"
            id="valor"
            name="valor"
            value="<%= pagamento.getValor() %>"
            required
    >

    <br><br>


    <label for="data_pagamento">
        Data pagamento:
    </label>

    <input
            type="LocalDateTime"
            id="data_pagamento"
            name="data_pagamento"
            value="<%= pagamento.getDataPagamento() %>"
            required
    >

    <br><br>


    <label for="fk_contrato">
        Fk contrato:
    </label>

    <input
            type="number"
            id="duracaoMeses"
            name="duracaoMeses"
            value="<%= pagamento.getDuracaoMeses() %>"
            min="1"
            required
    >

    <br><br>


    <label for="metodo_pagamento">
        Metodo Pagamento:
    </label>

    <input
            type="text"
            id="metodo_pagamento"
            name="metodo_pagamento"
            value="<%= pagamento.getDescricao() %>"
    >

    <br><br>


    <button type="submit">
        Salvar alterações
    </button>

    <a href="${pageContext.request.contextPath}/pagamentos">
        Cancelar
    </a>

</form>

</body>

</html>