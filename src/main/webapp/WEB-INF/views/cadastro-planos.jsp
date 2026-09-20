<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Plano</title>
</head>

<body>

<h1>Cadastrar Plano</h1>

<form
        action="${pageContext.request.contextPath}/planos"
        method="post"
>

    <input
            type="hidden"
            name="action"
            value="create"
    >

    <label for="nome">Nome:</label>
    <input
            type="text"
            id="nome"
            name="nome"
            required
    >

    <br><br>

    <label for="valor">Valor mensal:</label>
    <input
            type="number"
            id="valor"
            name="valor"
            step="0.01"
            required
    >

    <br><br>

    <label for="duracao">Duração em meses:</label>
    <input
            type="number"
            id="duracao"
            name="duracao"
            required
    >

    <br><br>

    <label for="descricao">Descrição:</label>
    <input
            type="text"
            id="descricao"
            name="descricao"
    >

    <br><br>

    <button type="submit">
        Cadastrar
    </button>

</form>

</body>
</html>