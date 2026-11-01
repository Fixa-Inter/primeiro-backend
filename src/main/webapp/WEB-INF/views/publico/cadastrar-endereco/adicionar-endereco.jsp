<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String erro = (String) request.getAttribute("erro");
  String razaoSocial = (String) request.getAttribute("razaoSocial");

  // o servlet manda etapa=confirmar depois de consultar o CNPJ (ou apos erro ao finalizar)
  boolean dadosCarregados = "confirmar".equals(request.getAttribute("etapa"));
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Adicione um endereço | FIXA</title>
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

    <h1>Adicione um endereço</h1>
    <p class="descricao-cadastro">Adicione um endereço dessa instituição. Os dados no app são separados por endereço, não por instituição.</p>

    <% if (erro != null && !erro.isBlank()) { %>
      <p class="mensagem-erro" role="alert"><%= erro %></p>
    <% } %>

    <%-- Etapa 1: consulta o CNPJ e preenche os dados do endereco --%>
    <form action="${pageContext.request.contextPath}/cadastrar-endereco?action=buscarCnpj" method="post">
      <div class="campo-cadastro">
        <label for="cnpjBusca">CNPJ *</label>
        <input type="text" id="cnpjBusca" name="cnpj" value="${empty cnpj ? param.cnpj : cnpj}" inputmode="numeric" maxlength="18" placeholder="00.000.000/0000-00" required>
      </div>

      <button class="botao-principal" type="submit">Buscar CNPJ</button>
    </form>

    <% if (razaoSocial != null && !razaoSocial.isBlank()) { %>
      <p class="descricao-cadastro">Razão social: <strong><%= razaoSocial %></strong></p>
    <% } %>

    <%-- Etapa 2: confere/edita os dados preenchidos e salva o endereco --%>
    <% if (dadosCarregados) { %>
    <form action="${pageContext.request.contextPath}/cadastrar-endereco?action=finalizar" method="post">
      <input type="hidden" name="cnpj" value="${cnpj}">

      <div class="campo-cadastro">
        <label for="nome">Nome do endereço *</label>
        <input type="text" id="nome" name="nomeEndereco" value="${nomeEndereco}" maxlength="50" placeholder="Ex: Sede Campinas" required>
      </div>

      <div class="linha-campos">
        <div class="campo-cadastro">
          <label for="cep">CEP *</label>
          <input type="text" id="cep" name="cep" value="${cep}" inputmode="numeric" maxlength="9" autocomplete="postal-code" placeholder="00000-000" required>
        </div>
      </div>

      <div class="campo-cadastro">
        <label for="rua">Logradouro *</label>
        <input type="text" id="rua" name="rua" value="${rua}" maxlength="40" autocomplete="address-line1" placeholder="Ex: Rua das Flores" required>
      </div>

      <div class="linha-campos">
        <div class="campo-cadastro">
          <label for="bairro">Bairro *</label>
          <input type="text" id="bairro" name="bairro" value="${bairro}" maxlength="40" autocomplete="address-level3" placeholder="Ex: Centro" required>
        </div>
        <div class="campo-cadastro">
          <label for="cidade">Cidade *</label>
          <input type="text" id="cidade" name="cidade" value="${cidade}" maxlength="40" autocomplete="address-level2" placeholder="Ex: São Paulo" required>
        </div>
      </div>

      <div class="linha-campos">
        <div class="campo-cadastro">
          <label for="estado">Estado *</label>
          <input type="text" id="estado" name="estado" value="${estado}" maxlength="2" autocomplete="address-level1" placeholder="UF" required>
        </div>
        <div class="campo-cadastro">
          <label for="numero">Número *</label>
          <input type="text" id="numero" name="numero" value="${numero}" maxlength="10" placeholder="Ex: 120" required>
        </div>
        <div class="campo-cadastro">
          <label for="complemento">Complemento</label>
          <input type="text" id="complemento" name="complemento" value="${complemento}" maxlength="50" autocomplete="address-line2" placeholder="Opcional">
        </div>
      </div>

      <button class="botao-principal" type="submit">Concluir cadastro</button>
    </form>
    <% } %>

    <a class="voltar-cadastro" href="${pageContext.request.contextPath}/index.html">Voltar</a>
  </main>
</body>
</html>
