<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar</title>
</head>
<body>

<h1>O que você quer cadastrar?</h1>

<a href="${pageContext.request.contextPath}/cadastro?action=iniciar">
    <button type="button">Instituição</button>
</a>

<a href="${pageContext.request.contextPath}/login-endereco">
    <button type="button">Endereço</button>
</a>

</body>
</html>