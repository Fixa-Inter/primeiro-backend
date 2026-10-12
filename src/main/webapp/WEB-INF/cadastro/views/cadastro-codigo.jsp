<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Código de verificação</title>
</head>
<body>

<h1>Digite o código de verificação</h1>

<p>Enviamos um código de 6 dígitos para o seu e-mail.</p>

<form action="${pageContext.request.contextPath}/cadastro" method="post">

    <input type="hidden" name="action" value="verificarCodigo">

    <label for="codigo">Código:</label>
    <input
            type="text"
            id="codigo"
            name="codigo"
            maxlength="6"
            pattern="\d{6}"
            inputmode="numeric"
            required
    >
    <br><br>

    <button type="submit">Verificar</button>

</form>

</body>
</html>