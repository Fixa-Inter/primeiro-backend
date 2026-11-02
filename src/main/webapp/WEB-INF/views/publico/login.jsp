<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String erro = (String) request.getAttribute("erro"); %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Área Restrita
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/senha.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/login/icon-claro.svg" media="(prefers-color-scheme: light)">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/login/icon-escuro.svg" type="image/svg+xml" media="(prefers-color-scheme: dark)">
  </head>
  <body>
    <a href="${pageContext.request.contextPath}/index.html" class="voltar">
      Voltar
    </a>
    <main class="login">
      <div class="login-card">
        <img src="${pageContext.request.contextPath}/assets/img/login/logo-fixa.svg" alt="Logo Fixa" class="logo">
        <h1>
          Acesso administrativo
        </h1>
        <p class="descricao">
          Entre com o e-mail e a senha do super administrador
          <br>
          para acessar o painel Fixa.
        </p>
        <% if (erro != null) { %>
        <p class="descricao">
          <%= com.utils.ListaWeb.escapar(erro) %>
        </p>
        <% } %>
        <form action="${pageContext.request.contextPath}/login-handler?action=login" method="post" novalidate>
          <div class="campo">
            <label for="email">
              E-mail
            </label>
            <input type="email" id="email" name="email" placeholder="seuemail@fixa.com.br">
          </div>
          <div class="campo">
            <label for="senha">
              Senha
            </label>
            <div class="campo-senha">
              <input type="password" id="senha" name="senha" placeholder="Digite sua senha">
              <button class="mostrar-senha" type="button" data-campo="senha" aria-controls="senha" aria-label="Mostrar senha" aria-pressed="false" title="Mostrar senha">
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
          <button type="submit">
            Entrar
          </button>
        </form>
      </div>
    </main>
    <script src="${pageContext.request.contextPath}/assets/js/senha.js" defer>
    </script>
  </body>
</html>
