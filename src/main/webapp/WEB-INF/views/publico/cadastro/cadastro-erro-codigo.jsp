<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Confirme seu e-mail | FIXA</title>
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
      <span class="etapa atual" aria-current="step">2</span>
      <span class="linha-etapa" aria-hidden="true"></span>
      <span class="etapa">3</span>
      <span class="linha-etapa" aria-hidden="true"></span>
      <span class="etapa">4</span>
    </div>

    <h1>Confirme seu e-mail</h1>
    <p class="descricao-cadastro">Digite o código de 6 dígitos enviado para o e-mail corporativo informado. Ele vale por 5 minutos.</p>

    <p class="mensagem-erro" id="erro-codigo" role="alert">Código inválido ou expirado. Confira os números ou solicite um novo código.</p>

    <form action="${pageContext.request.contextPath}/cadastro?action=verificarCodigo" method="post">
      <fieldset class="codigo-area">
      <legend>Código de verificação *</legend>
      <div class="codigo-inputs">
        <input type="text" id="codigo1" name="codigo1" inputmode="numeric" pattern="[0-9]" maxlength="1" aria-label="Dígito 1 do código" autocomplete="one-time-code" aria-invalid="true" aria-describedby="erro-codigo" required>
        <input type="text" id="codigo2" name="codigo2" inputmode="numeric" pattern="[0-9]" maxlength="1" aria-label="Dígito 2 do código" autocomplete="off" aria-invalid="true" aria-describedby="erro-codigo" required>
        <input type="text" id="codigo3" name="codigo3" inputmode="numeric" pattern="[0-9]" maxlength="1" aria-label="Dígito 3 do código" autocomplete="off" aria-invalid="true" aria-describedby="erro-codigo" required>
        <input type="text" id="codigo4" name="codigo4" inputmode="numeric" pattern="[0-9]" maxlength="1" aria-label="Dígito 4 do código" autocomplete="off" aria-invalid="true" aria-describedby="erro-codigo" required>
        <input type="text" id="codigo5" name="codigo5" inputmode="numeric" pattern="[0-9]" maxlength="1" aria-label="Dígito 5 do código" autocomplete="off" aria-invalid="true" aria-describedby="erro-codigo" required>
        <input type="text" id="codigo6" name="codigo6" inputmode="numeric" pattern="[0-9]" maxlength="1" aria-label="Dígito 6 do código" autocomplete="off" aria-invalid="true" aria-describedby="erro-codigo" required>
      </div>
    </fieldset>
      <button class="botao-principal" type="submit">Verificar código</button>
    </form>

    <a class="voltar-cadastro" href="${pageContext.request.contextPath}/cadastro?action=iniciar">Voltar</a>
  </main>
</body>
</html>
