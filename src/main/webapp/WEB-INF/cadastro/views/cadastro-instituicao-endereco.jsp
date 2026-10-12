<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Endereço da instituição</title>
</head>
<body>

<h1>Endereço da instituição</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<% if (erro != null && !erro.isBlank()) { %>
    <p style="color: red;"><%= erro %></p>
<% } %>

<!-- Form 1: busca o CNPJ e preenche os campos abaixo -->
<form action="${pageContext.request.contextPath}/cadastro" method="post">
    <input type="hidden" name="action" value="buscarCnpj">

    <label for="cnpjBusca">CNPJ:</label>
    <input type="text" id="cnpjBusca" name="cnpj" value="${cnpj}" maxlength="18" required>

    <button type="submit">Buscar</button>
</form>

<% String razaoSocial = (String) request.getAttribute("razaoSocial"); %>
<% if (razaoSocial != null && !razaoSocial.isBlank()) { %>
    <p><strong>Razão social:</strong> <%= razaoSocial %></p>
<% } %>

<hr>

<!-- Form 2: confirma/edita os dados e finaliza o cadastro -->
<form action="${pageContext.request.contextPath}/cadastro" method="post">
    <input type="hidden" name="action" value="finalizar">
    <input type="hidden" name="cnpj" value="${cnpj}">

    <label for="rua">Rua:</label>
    <input type="text" id="rua" name="rua" value="${rua}" maxlength="40" required>
    <br><br>

    <label for="numero">Número:</label>
    <input type="text" id="numero" name="numero" value="${numero}" maxlength="10" required>
    <br><br>

    <label for="complemento">Complemento:</label>
    <input type="text" id="complemento" name="complemento" value="${complemento}" maxlength="50">
    <br><br>

    <label for="bairro">Bairro:</label>
    <input type="text" id="bairro" name="bairro" value="${bairro}" maxlength="40" required>
    <br><br>

    <label for="cidade">Cidade:</label>
    <input type="text" id="cidade" name="cidade" value="${cidade}" maxlength="40" required>
    <br><br>

    <label for="estado">Estado (UF):</label>
    <input type="text" id="estado" name="estado" value="${estado}" maxlength="2" required>
    <br><br>

    <label for="cep">CEP:</label>
    <input type="text" id="cep" name="cep" value="${cep}" maxlength="9" required>
    <br><br>

    <button type="submit">Finalizar cadastro</button>
</form>

</body>
</html>