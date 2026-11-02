<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Cadastre sua instituição | FIXA
    </title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-claro.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
  </head>
  <body>
    <main class="cadastro cadastro-inicial">
      <a class="sair-cadastro" href="${pageContext.request.contextPath}/index.html" aria-label="Voltar ao site">
        ×
      </a>
      <div class="etapas-cadastro" aria-label="Etapas: Instituição, E-mail, Senha e Endereço">
        <div class="passo-cadastro">
          <span class="etapa atual" aria-current="step">
            1
          </span>
          <span class="nome-etapa">
            Instituição
          </span>
        </div>
        <span class="linha-etapa" aria-hidden="true">
        </span>
        <div class="passo-cadastro">
          <span class="etapa">
            2
          </span>
          <span class="nome-etapa">
            E-mail
          </span>
        </div>
        <span class="linha-etapa" aria-hidden="true">
        </span>
        <div class="passo-cadastro">
          <span class="etapa">
            3
          </span>
          <span class="nome-etapa">
            Senha
          </span>
        </div>
        <span class="linha-etapa" aria-hidden="true">
        </span>
        <div class="passo-cadastro">
          <span class="etapa">
            4
          </span>
          <span class="nome-etapa">
            Endereço
          </span>
        </div>
      </div>
      <h1>
        Cadastre sua instituição
      </h1>
      <p class="descricao-cadastro">
        Leva menos de 2 minutos. Depois enviaremos um código de verificação para o e-mail corporativo informado.
      </p>
      <% String erro = (String) request.getAttribute("erro"); %>
      <% if (erro != null && !erro.isBlank()) { %>
      <p class="mensagem-erro" role="alert">
        <%= erro %>
      </p>
      <% } %>
      <form action="${pageContext.request.contextPath}/cadastro?action=enviarCodigo" method="post">
        <div class="campo-cadastro">
          <label for="nome">
            Nome da instituição *
          </label>
          <input type="text" id="nome" name="nome" maxlength="50" autocomplete="organization" placeholder="Ex: Instituto J&amp;F - Escola de Tecnologia" required>
        </div>
        <div class="campo-cadastro">
          <label for="tipo_instituicao">
            Tipo da instituição *
          </label>
          <select id="tipo_instituicao" name="tipoInstituicao" required>
            <option value="">
              Selecione o tipo
            </option>
            <option value="1">
              Escola
            </option>
            <option value="2">
              Faculdade
            </option>
            <option value="3">
              Empresa
            </option>
            <option value="4">
              Órgão público
            </option>
          </select>
        </div>
        <div class="campo-cadastro">
          <label for="email_corporativo">
            E-mail corporativo *
          </label>
          <input type="email" id="email_corporativo" name="emailCorporativo" maxlength="50" autocomplete="email" placeholder="Ex: contato@suainstituicao.com.br" required>
        </div>
        <button class="botao-principal" type="submit">
          Continuar
        </button>
      </form>
      <p class="link-cadastro">
        <a href="${pageContext.request.contextPath}/login">
          Já é cadastrado? Entrar
        </a>
      </p>
      <a class="voltar-cadastro" href="${pageContext.request.contextPath}/cadastro?action=escolher">
        Voltar
      </a>
    </main>
  </body>
</html>
