<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Pagamento</title>
</head>

<body>

<h1>Cadastrar Pagamento</h1>

<form method="post"
      action="${pageContext.request.contextPath}/pagamentos">

    <input type="hidden" name="action" value="create">

    <div>
        <label for="valor">Valor:</label>

        <input
                type="number"
                id="valor"
                name="valor"
                step="0.01"
                min="0"
                required
        >
    </div>

    <br>

    <div>
        <label for="fkContrato">ID do contrato:</label>

        <input
                type="number"
                id="fkContrato"
                name="fkContrato"
                min="1"
                step="1"
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
                Coloque aqui os nomes EXATOS do seu
                enum MetodoPagamento.
            -->

            <option value="PIX">PIX</option>
            <option value="CARTAO">Cartão</option>
            <option value="BOLETO">Boleto</option>

        </select>
    </div>

    <br>

    <button type="submit">
        Cadastrar
    </button>

    <a href="${pageContext.request.contextPath}/pagamentos">
        Cancelar
    </a>

</form>

</body>

</html>