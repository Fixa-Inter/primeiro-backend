<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Defina sua senha | FIXA
    </title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-claro.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/senha.css">
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
          <span class="etapa atual" aria-current="step">
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
        Defina sua senha
      </h1>
      <p class="descricao-cadastro">
        Escolha a senha definitiva de acesso ao Fixa.
      </p>
      <% String erro = (String) request.getAttribute("erro"); %>
      <% if (erro != null && !erro.isBlank()) { %>
      <p class="mensagem-erro" role="alert">
        <%= erro %>
      </p>
      <% } %>
      <form action="${pageContext.request.contextPath}/cadastro?action=definirSenha" method="post">
        <div class="campo-cadastro">
          <label for="senha">
            Nova senha *
          </label>
          <div class="campo-senha">
            <input type="password" id="senha" name="senha" required title="Use pelo menos 8 caracteres, uma letra e um número." autocomplete="new-password" aria-describedby="regras-senha" placeholder="Digite sua nova senha">
            <button class="mostrar-senha" type="button" data-campo="senha" aria-controls="senha confirmarSenha" aria-label="Mostrar senhas" aria-pressed="false" title="Mostrar senha">
              <svg viewBox="0 0 24 24" aria-hidden="true" focusable="false">
                <path d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12Z">
                </path>
                <circle cx="12" cy="12" r="3">
                </circle>
                <path class="risco-senha" d="m3 3 18 18">
                </path>
              </svg>
            </button>
          </div>
        </div>
        <ul class="regras-senha" id="regras-senha">
          <li>
            Mínimo de 8 caracteres
          </li>
          <li>
            Pelo menos uma letra
          </li>
          <li>
            Pelo menos um número
          </li>
        </ul>
        <div class="campo-cadastro">
          <label for="confirmarSenha">
            Confirmar nova senha *
          </label>
          <div class="campo-senha">
            <input type="password" id="confirmarSenha" name="confirmarSenha" required autocomplete="new-password" placeholder="Repita a nova senha">
          </div>
        </div>
        <button class="botao-principal" type="submit">
          Continuar
        </button>
      </form>
      <a class="voltar-cadastro" href="${pageContext.request.contextPath}/cadastro?action=iniciar">
        Voltar
      </a>
    </main>
    <script src="${pageContext.request.contextPath}/assets/js/senha-cadastro.js" defer>
    </script>
  </body>
</html>
