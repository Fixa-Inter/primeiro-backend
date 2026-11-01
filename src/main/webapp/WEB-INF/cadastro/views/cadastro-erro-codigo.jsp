<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Código inválido</title>
</head>
<body>

<h1>Código inválido ou expirado</h1>

<p>O código digitado está incorreto ou já expirou. Refaça o cadastro para receber um novo código.</p>

<a href="${pageContext.request.contextPath}/cadastro?action=iniciar">
    <button type="button">Recomeçar</button>
</a>

</body>
</html>