<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Confirme os dados da instituição | FIXA</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-claro.svg">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
  <script src="${pageContext.request.contextPath}/assets/js/cadastro.js" defer></script>
</head>
<body>
  <main class="cadastro">
    <a class="sair-cadastro" href="${pageContext.request.contextPath}/index.html" aria-label="Voltar ao site">×</a>

    <h1>Confirme seus dados de instituição</h1>
    <p class="descricao-cadastro">Informe o e-mail e a senha da instituição para adicionar um novo endereço.</p>

    <% String erro = (String) request.getAttribute("erro"); %>
    <% if (erro != null && !erro.isBlank()) { %>
      <p class="mensagem-erro" role="alert"><%= erro %></p>
    <% } %>

    <form action="${pageContext.request.contextPath}/cadastrar-endereco?action=login" method="post">
      <div class="campo-cadastro">
        <label for="email_corporativo">E-mail da instituição *</label>
        <input type="email" id="email_corporativo" name="email" maxlength="50" autocomplete="username" placeholder="Ex: contato@dominio.com.br" required>
      </div>

      <div class="campo-cadastro">
        <label for="senha">Senha *</label>
        <input type="password" id="senha" name="senha" autocomplete="current-password" placeholder="Digite a senha da instituição" required>
      </div>

      <button class="botao-principal" type="submit">Continuar</button>
    </form>

    <a class="voltar-cadastro" href="${pageContext.request.contextPath}/index.html">Voltar</a>
  </main>
</body>
</html>
