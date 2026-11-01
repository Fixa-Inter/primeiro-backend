<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cadastro | FIXA</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-claro.svg">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
  <script src="${pageContext.request.contextPath}/assets/js/cadastro.js" defer></script>
</head>
<body>
  <main class="cadastro">
    <a class="sair-cadastro" href="${pageContext.request.contextPath}/index.html" aria-label="Voltar ao site">×</a>

    <h1>O que você quer cadastrar?</h1>
    <p class="descricao-cadastro">Escolha por onde quer começar.</p>

    <a class="opcao-cadastro" href="${pageContext.request.contextPath}/cadastro?action=iniciar">
      <img src="${pageContext.request.contextPath}/assets/img/cadastro/instituicao.svg" alt="">
      <div>
        <h2>Cadastrar instituição</h2>
        <p>Primeiro acesso: crie a conta da sua escola, faculdade ou empresa e o primeiro endereço.</p>
      </div>
      <span class="seta-opcao" aria-hidden="true">›</span>
    </a>

    <a class="opcao-cadastro" href="${pageContext.request.contextPath}/cadastrar-endereco">
      <img src="${pageContext.request.contextPath}/assets/img/cadastro/endereco.svg" alt="">
      <div>
        <h2>Adicionar endereço</h2>
        <p>Sua instituição já tem conta? Cadastre outra unidade com o e-mail e a senha dela.</p>
      </div>
      <span class="seta-opcao" aria-hidden="true">›</span>
    </a>

    <p class="link-cadastro">Já é cadastrado? <a>Entrar</a></p>
  </main>
</body>
</html>
