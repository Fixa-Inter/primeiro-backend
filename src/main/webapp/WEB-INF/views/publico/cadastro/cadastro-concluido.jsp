<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Cadastro concluído | FIXA
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
          <span class="etapa concluida">
            4
          </span>
          <span class="nome-etapa">
            Endereço
          </span>
        </div>
      </div>
      <div class="manu-fala-cadastro">
        <img class="mascote-cadastro" src="${pageContext.request.contextPath}/assets/img/cadastro/manu-cadastro.svg" alt="Manu comemorando o cadastro">
        <div class="balao-cadastro">
          <p class="rotulo-cadastro">
            MANU · ASSISTENTE
          </p>
          <p>
            Pronto! Agora é só começar a registrar suas solicitações. Qualquer dúvida, é só me chamar.
          </p>
        </div>
      </div>
      <h1>
        Cadastro concluído!
      </h1>
      <p class="descricao-cadastro">
        Seu cadastro foi concluído com sucesso. Tudo pronto para começar a usar o Fixa.
      </p>
      <a class="botao-principal" href="${pageContext.request.contextPath}/index.html">
        Voltar ao site
      </a>
    </main>
  </body>
</html>
