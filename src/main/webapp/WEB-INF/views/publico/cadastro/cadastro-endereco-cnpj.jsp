<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String erro = (String) request.getAttribute("erro"); %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Adicione um endereço | FIXA
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
      <h1>
        Adicione um endereço
      </h1>
      <p class="descricao-cadastro">
        Informe o endereço da instituição onde a manutenção será gerenciada.
      </p>
      <% if (erro != null && !erro.isBlank()) { %>
      <p class="mensagem-erro" role="alert">
        <%= com.utils.ListaWeb.escapar(erro) %>
      </p>
      <% } %>
      <form action="${pageContext.request.contextPath}/cadastro?action=buscarCnpj" method="post">
        <div class="campo-cadastro">
          <label for="cnpj">
            CNPJ *
          </label>
          <input type="text" id="cnpj" name="cnpj" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("cnpj") == null || "".equals(request.getAttribute("cnpj")) ? request.getParameter("cnpj") : request.getAttribute("cnpj")) %>" required inputmode="numeric" title="Informe os 14 números do CNPJ, com ou sem pontuação." placeholder="00.000.000/0000-00">
        </div>
        <button class="botao-principal" type="submit">
          Buscar CNPJ
        </button>
      </form>
      <a class="voltar-cadastro" href="${pageContext.request.contextPath}/cadastro?action=enderecoInfo">
        Voltar
      </a>
    </main>
  </body>
</html>
