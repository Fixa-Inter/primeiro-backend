<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
String erro = (String) request.getAttribute("erro");
String razaoSocial = (String) request.getAttribute("razaoSocial");
// so mostra o formulario de endereco depois que o CNPJ foi consultado (ou apos erro ao finalizar)
boolean dadosCarregados = razaoSocial != null
|| request.getAttribute("rua") != null
|| request.getAttribute("cidade") != null
|| request.getAttribute("cep") != null;
%>
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
    <script src="${pageContext.request.contextPath}/assets/js/cadastro-cnpj.js" defer>
    </script>
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
      <%-- Etapa 1: consulta o CNPJ e preenche os dados do endereço --%>
      <form action="${pageContext.request.contextPath}/cadastro?action=buscarCnpj" method="post">
        <div class="campo-cadastro">
          <label for="cnpjBusca">
            CNPJ *
          </label>
          <input type="text" id="cnpjBusca" name="cnpj" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("cnpj") == null || "".equals(request.getAttribute("cnpj")) ? request.getParameter("cnpj") : request.getAttribute("cnpj")) %>" inputmode="numeric" maxlength="18" placeholder="00.000.000/0000-00" required>
        </div>
        <button class="botao-principal" type="submit">
          Buscar CNPJ
        </button>
      </form>
      <% if (razaoSocial != null && !razaoSocial.isBlank()) { %>
      <p class="descricao-cadastro">
        Razão social:
        <strong>
          <%= com.utils.ListaWeb.escapar(razaoSocial) %>
        </strong>
      </p>
      <% } %>
      <%-- Etapa 2: confere/edita os dados preenchidos e conclui o cadastro --%>
      <% if (dadosCarregados) { %>
      <form action="${pageContext.request.contextPath}/cadastro?action=finalizar" method="post">
        <input type="hidden" name="cnpj" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("cnpj")) %>">
        <div class="campo-cadastro">
          <label for="nome">
            Nome do endereço *
          </label>
          <input type="text" id="nome" name="nomeEndereco" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("nomeEndereco")) %>" maxlength="50" placeholder="Ex: Sede Campinas" required>
        </div>
        <div class="linha-campos">
          <div class="campo-cadastro">
            <label for="cep">
              CEP *
            </label>
            <input type="text" id="cep" name="cep" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("cep")) %>" inputmode="numeric" maxlength="9" autocomplete="postal-code" placeholder="00000-000" required>
          </div>
        </div>
        <div class="campo-cadastro">
          <label for="rua">
            Logradouro
          </label>
          <input type="text" id="rua" name="rua" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("rua")) %>" maxlength="40" autocomplete="address-line1" placeholder="Ex: Rua das Flores">
        </div>
        <div class="linha-campos">
          <div class="campo-cadastro">
            <label for="bairro">
              Bairro
            </label>
            <input type="text" id="bairro" name="bairro" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("bairro")) %>" maxlength="40" autocomplete="address-level3" placeholder="Ex: Centro">
          </div>
          <div class="campo-cadastro">
            <label for="cidade">
              Cidade
            </label>
            <input type="text" id="cidade" name="cidade" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("cidade")) %>" maxlength="40" autocomplete="address-level2" placeholder="Ex: São Paulo">
          </div>
        </div>
        <div class="linha-campos">
          <div class="campo-cadastro">
            <label for="estado">
              Estado
            </label>
            <input type="text" id="estado" name="estado" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("estado")) %>" maxlength="2" autocomplete="address-level1" placeholder="UF">
          </div>
          <div class="campo-cadastro">
            <label for="numero">
              Número *
            </label>
            <input type="text" id="numero" name="numero" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("numero")) %>" maxlength="10" placeholder="Ex: 120" required>
          </div>
          <div class="campo-cadastro">
            <label for="complemento">
              Complemento
            </label>
            <input type="text" id="complemento" name="complemento" value="<%= com.utils.ListaWeb.escapar(request.getAttribute("complemento")) %>" maxlength="50" autocomplete="address-line2" placeholder="Opcional">
          </div>
        </div>
        <button class="botao-principal" type="submit">
          Concluir cadastro
        </button>
      </form>
      <% } %>
      <a class="voltar-cadastro" href="${pageContext.request.contextPath}/cadastro?action=enderecoCnpj">
        Voltar
      </a>
    </main>
    <script src="${pageContext.request.contextPath}/assets/js/cadastro-cnpj.js" defer>
    </script>
    <script src="${pageContext.request.contextPath}/assets/js/mascara.js" defer>
    </script>
  </body>
</html>
