<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Os dados ficam no endereço | FIXA
    </title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-claro.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
  </head>
  <body>
    <main class="cadastro">
      <a class="sair-cadastro" href="${pageContext.request.contextPath}/index.html" aria-label="Voltar ao site">
        ×
      </a>
      <div class="etapas-cadastro" aria-label="Etapas: Instituição, E-mail, Senha e Endereço">
        <div class="passo-cadastro">
          <span class="etapa concluida">
            1
          </span>
          <span class="nome-etapa">
            Instituição
          </span>
        </div>
        <span class="linha-etapa concluida" aria-hidden="true">
        </span>
        <div class="passo-cadastro">
          <span class="etapa concluida">
            2
          </span>
          <span class="nome-etapa">
            E-mail
          </span>
        </div>
        <span class="linha-etapa concluida" aria-hidden="true">
        </span>
        <div class="passo-cadastro">
          <span class="etapa concluida">
            3
          </span>
          <span class="nome-etapa">
            Senha
          </span>
        </div>
        <span class="linha-etapa concluida" aria-hidden="true">
        </span>
        <div class="passo-cadastro">
          <span class="etapa atual" aria-current="step">
            4
          </span>
          <span class="nome-etapa">
            Endereço
          </span>
        </div>
      </div>
      <img class="mascote-cadastro" src="${pageContext.request.contextPath}/assets/img/cadastro/manu-cadastro.svg" alt="Manu com uma prancheta">
      <h1>
        Os dados ficam no endereço
      </h1>
      <p class="descricao-cadastro">
        Cada endereço tem seus próprios dados de manutenção. Adicione um endereço para começar.
      </p>
      <a class="botao-principal" href="${pageContext.request.contextPath}/cadastro?action=enderecoCnpj">
        Adicionar endereço
      </a>
      <a href="${pageContext.request.contextPath}/cadastro?action=iniciar" class="voltar-cadastro">
        Voltar
      </a>
    </main>
  </body>
</html>
