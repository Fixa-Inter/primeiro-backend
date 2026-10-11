<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Fixa Backend</title>
</head>

<body>

<h1>Fixa Backend</h1>

<form method="post" action="${pageContext.request.contextPath}/login-handler">
    <input type="hidden" name="action" value="logout">
    <button type="submit">Sair</button>
</form>

<h2>CRUDs</h2>

<ul>

    <li>
        <a href="${pageContext.request.contextPath}/instituicoes">
            Instituições
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/planos">
            Planos
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/superAdmin">
            Super Administradores
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/enderecos">
            Endereços
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/contratos">
            Contratos
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/usuarios">
            Usuários
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/pagamentos">
            Pagamentos
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/fotoUsuario">
            Fotos dos Usuários
        </a>
    </li>

</ul>

</body>

</html>
