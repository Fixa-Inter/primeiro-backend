<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Contrato</title>
</head>

<body>

<h1>Cadastrar contrato</h1>

<form
        method="post"
        action="<%= request.getContextPath() %>/contratos"
>

    <input
            type="hidden"
            name="action"
            value="create"
    >

    <label for="dataVencimento">
        Data de vencimento:
    </label>

    <input
            type="date"
            id="dataVencimento"
            name="DATA_VENCIMENTO"
            required
    >

    <br><br>

    <label for="fkEndereco">
        ID do endereço:
    </label>

    <input
            type="number"
            id="fkEndereco"
            name="FK_ENDERECO_ID"
            required
    >

    <br><br>

    <label for="fkPlano">
        ID do plano:
    </label>

    <input
            type="number"
            id="fkPlano"
            name="FK_PLANO_ID"
            required
    >

    <br><br>

    <label for="statusContrato">
        Status do contrato:
    </label>

    <select
            id="statusContrato"
            name="STATUS_CONTRATO"
            required
    >

        <option value="">
            Selecione
        </option>

        <option value="1">
            Ativo
        </option>

        <option value="2">
            Inativo
        </option>

        <option value="3">
            Cancelado
        </option>

    </select>

    <br><br>

    <button type="submit">
        Cadastrar
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/contratos">
    Voltar
</a>

</body>

</html>