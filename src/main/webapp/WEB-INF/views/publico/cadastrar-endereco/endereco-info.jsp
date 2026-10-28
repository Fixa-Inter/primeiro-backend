<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Os dados ficam no endereço | FIXA</title>
  <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-claro.svg">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
  <script src="${pageContext.request.contextPath}/assets/js/cadastro.js" defer></script>
</head>
<body>
  <main class="cadastro">
    <a class="sair-cadastro" href="${pageContext.request.contextPath}/index.html" aria-label="Voltar ao site">×</a>

    <div class="etapas-cadastro" aria-label="Etapas: instituição, código, senha e endereço">
      <span class="etapa concluida">1</span>
      <span class="linha-etapa concluida" aria-hidden="true"></span>
      <span class="etapa concluida">2</span>
      <span class="linha-etapa concluida" aria-hidden="true"></span>
      <span class="etapa concluida">3</span>
      <span class="linha-etapa concluida" aria-hidden="true"></span>
      <span class="etapa atual" aria-current="step">4</span>
    </div>

    <img class="mascote-cadastro" src="${pageContext.request.contextPath}/assets/img/cadastro/manu-cadastro.svg" alt="Manu com uma prancheta">

    <h1>Os dados ficam no endereço</h1>
    <p class="descricao-cadastro">O app e o site trabalham com o endereço da instituição, e não com a instituição em si. Os dados são separados por endereço: nenhuma instituição "controla" todos os dados da aplicação.</p>

    <a class="botao-principal" href="${pageContext.request.contextPath}/cadastrar-endereco?action=adicionar">Adicionar endereço</a>
  </main>
</body>
</html>
