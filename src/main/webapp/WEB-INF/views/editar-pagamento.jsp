<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.model.Pagamento" %>

<%
    Pagamento pagamento =
            (Pagamento) request.getAttribute("pagamento");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Editar Pagamento</title>
</head>

<body>

<h1>Editar Pagamento</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<form method="post"
      action="${pageContext.request.contextPath}/pagamentos">
            <% if (erro != null && !erro.isBlank()) { %>
            <p>
                <%= erro %>
            </p>
            <% } %>


    <input type="hidden" name="action" value="update">

    <input
            type="hidden"
            name="id"
            value="<%= pagamento.getId() %>"
    >


    <div>

        <label>ID:</label>

        <span>
            <%= pagamento.getId() %>
        </span>

    </div>

    <br>


    <div>

        <label>Data do pagamento:</label>

        <span>
            <%= pagamento.getDataPagamento() %>
        </span>

    </div>

    <br>


    <div>

        <label for="valor">
            Valor:
        </label>

        <input
                type="number"
                id="valor"
                name="valor"
                step="0.01"
                min="0"
                value="<%= pagamento.getValor() %>"
                required
        >

    </div>

    <br>


    <div>

        <label for="fkContrato">
            ID do contrato:
        </label>

        <input
                type="number"
                id="fkContrato"
                name="fkContrato"
                min="1"
                step="1"
                value="<%= pagamento.getFkContrato() %>"
                required
        >

    </div>

    <br>


    <div>

        <label for="metodoPagamento">
            Método de pagamento:
        </label>

        <select
                id="metodoPagamento"
                name="metodoPagamento"
                required
        >

            <option value="">
                Selecione
            </option>

            <!--
                Ajustar para os valores exatos
                do seu enum MetodoPagamento.
            -->

            <option value="PIX"
                    <%= "PIX".equals(
                            pagamento.getMetodoPagamento().name()
                    ) ? "selected" : "" %>>
                PIX
            </option>

            <option value="CARTAO"
                    <%= "CARTAO".equals(
                            pagamento.getMetodoPagamento().name()
                    ) ? "selected" : "" %>>
                Cartão
            </option>

            <option value="BOLETO"
                    <%= "BOLETO".equals(
                            pagamento.getMetodoPagamento().name()
                    ) ? "selected" : "" %>>
                Boleto
            </option>

        </select>

    </div>

    <br>


    <div>

        <label for="foiRealizado">
            Pagamento realizado:
        </label>

        <select
                id="foiRealizado"
                name="foiRealizado"
                required
        >

            <option
                    value="true"
                    <%= pagamento.getFoiRealizado()
                            ? "selected"
                            : "" %>
            >
                Sim
            </option>

            <option
                    value="false"
                    <%= !pagamento.getFoiRealizado()
                            ? "selected"
                            : "" %>
            >
                Não
            </option>

        </select>

    </div>

    <br>


    <button type="submit">
        Salvar alterações
    </button>

    <a href="${pageContext.request.contextPath}/pagamentos">
        Cancelar
    </a>

</form>

</body>

</html>