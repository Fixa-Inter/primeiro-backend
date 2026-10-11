<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Pagamento</title>
</head>

<body>

<h1>Cadastrar Pagamento</h1>

<form
        action="${pageContext.request.contextPath}/pagamentos"
        method="post"
>

    <input
            type="hidden"
            name="action"
            value="create"
    >

    <label for="valor">Valor:</label>
    <input
            type="number"
            id="nome"
            name="nome"
            required
    >

    <br><br>

    <label for="data_pagamento">Data pagamento:</label>
    <input
            type="LocalDateTime"
            id="data_pagamento"
            name="data_pagamento"
            required
    >

    <br><br>

    <label for="fk_contrato">Fk contrato:</label>
    <input
            type="number"
            id="fk_contrato"
            name="fk_contrato"
            required
    >

    <br><br>

    <label for="metodo_pagamento">Metodo pagamento:</label>
    <input
            type="text"
            id="metodo_pagamento"
            name="metodo_pagamento"
    >

    <br><br>

    <button type="submit">
        Cadastrar
    </button>

</form>

</body>
</html>