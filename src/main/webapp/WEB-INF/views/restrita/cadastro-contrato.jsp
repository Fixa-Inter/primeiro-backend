<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, com.model.Endereco, com.model.Plano" %>
<% List<Endereco> enderecosDisponiveis = (List<Endereco>) request.getAttribute("enderecosDisponiveis"); List<Plano> planosDisponiveis = (List<Plano>) request.getAttribute("planosDisponiveis"); String erro = (String) request.getAttribute("erro"); %>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Cadastrar contrato | FIXA
    </title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/crud/simbolo-fixa.svg" type="image/svg+xml">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/crud.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feedback.css">
  </head>
  <body data-entidade="contratos" data-operacao="novo">
    <input type="checkbox" id="menu-fixado" class="controle-sidebar" aria-label="Fixar ou recolher menu">
    <input type="checkbox" id="menu-aberto" class="controle-sidebar" aria-label="Abrir ou fechar menu">
    <a class="pular-conteudo" href="#conteudo">
      Pular para o conteúdo
    </a>
    <label class="abrir-menu" for="menu-aberto">
      ☰ Menu
    </label>
    <aside class="menu-lateral" aria-label="Menu administrativo">
      <div class="topo-menu">
        <a class="marca-menu" href="${pageContext.request.contextPath}/analiticos" title="Página inicial" aria-label="Página inicial do FIXA">
          <img class="simbolo" src="${pageContext.request.contextPath}/assets/img/crud/simbolo-fixa.svg" alt="">
          <img class="logo-menu" src="${pageContext.request.contextPath}/assets/img/crud/logo-fixa.svg" alt="FIXA">
        </a>
        <label class="botao-travar fixar-menu" for="menu-fixado" title="Fixar menu">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <line x1="12" x2="12" y1="17" y2="22">
            </line>
            <path d="M5 17h14v-1.76a2 2 0 0 0-1.11-1.79l-1.78-.9A2 2 0 0 1 15 10.76V6h1a2 2 0 0 0 0-4H8a2 2 0 0 0 0 4h1v4.76a2 2 0 0 1-1.11 1.79l-1.78.9A2 2 0 0 0 5 15.24Z">
            </path>
          </svg>
          <span class="sr-only">
            Fixar menu
          </span>
        </label>
      </div>
      <nav class="navegacao" aria-label="Páginas do CRUD">
        <a href="${pageContext.request.contextPath}/analiticos" title="Página inicial">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M3 11.5 12 4l9 7.5">
            </path>
            <path d="M5.5 10v9a1 1 0 0 0 1 1H10v-6h4v6h3.5a1 1 0 0 0 1-1v-9">
            </path>
          </svg>
          <span class="texto-menu">
            Página inicial
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/instituicoes" title="Instituições">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <rect x="4" y="3" width="16" height="18" rx="1.5">
            </rect>
            <path d="M9 21v-5h6v5">
            </path>
            <path d="M8.5 7.5h1M14.5 7.5h1M8.5 11.5h1M14.5 11.5h1M8.5 15.5h1M14.5 15.5h1">
            </path>
          </svg>
          <span class="texto-menu">
            Instituições
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/usuarios" title="Usuários">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75">
            </path>
            <circle cx="9" cy="7" r="4">
            </circle>
          </svg>
          <span class="texto-menu">
            Usuários
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/enderecos" title="Endereços">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0Z">
            </path>
            <circle cx="12" cy="10" r="3">
            </circle>
          </svg>
          <span class="texto-menu">
            Endereços
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/superAdmin" title="Super administradores">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10Z">
            </path>
          </svg>
          <span class="texto-menu">
            Super administradores
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/planos" title="Planos">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="m12 3 10 5-10 5L2 8l10-5ZM2 12l10 5 10-5M2 16l10 5 10-5">
            </path>
          </svg>
          <span class="texto-menu">
            Planos
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/contratos" title="Contratos" aria-current="page">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8Z">
            </path>
            <polyline points="14 2 14 8 20 8">
            </polyline>
            <line x1="16" y1="13" x2="8" y2="13">
            </line>
            <line x1="16" y1="17" x2="8" y2="17">
            </line>
            <line x1="10" y1="9" x2="8" y2="9">
            </line>
          </svg>
          <span class="texto-menu">
            Contratos
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/pagamentos" title="Pagamentos">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <rect x="2" y="4" width="20" height="16" rx="2">
            </rect>
            <path d="M2 10h20M6 16h4">
            </path>
          </svg>
          <span class="texto-menu">
            Pagamentos
          </span>
        </a>
        <a href="${pageContext.request.contextPath}/fotoUsuario" title="Fotos de usuário">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
            <rect x="3" y="3" width="18" height="18" rx="2">
            </rect>
            <circle cx="8.5" cy="8.5" r="1.5">
            </circle>
            <polyline points="21 15 16 10 5 21">
            </polyline>
          </svg>
          <span class="texto-menu">
            Fotos de usuário
          </span>
        </a>
      </nav>
      <div class="rodape-menu">
        <a href="${pageContext.request.contextPath}/login" class="voltar-login">
          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9">
            </path>
          </svg>
          <span class="texto-menu">
            Sair
          </span>
        </a>
      </div>
      <label for="menu-aberto" class="fechar-menu">
        Fechar menu ×
      </label>
    </aside>
    <label class="fundo-menu" for="menu-aberto" aria-label="Fechar menu">
    </label>
    <div class="pagina">
      <header class="cabecalho">
        <div>
          <h1>
            Cadastrar contrato
          </h1>
          <p>
            Preencha os dados do registro.
          </p>
        </div>
        <a class="botao botao-secundario" href="${pageContext.request.contextPath}/contratos">
          Voltar para a lista
        </a>
      </header>
      <main class="conteudo" id="conteudo">
        <section class="cartao formulario-pagina" aria-labelledby="titulo-formulario">
          <div class="titulo-cartao">
            <h2 id="titulo-formulario">
              Cadastrar contrato
            </h2>
          </div>
          <form action="${pageContext.request.contextPath}/contratos" method="post" aria-describedby="aviso-formulario erro-formulario" id="registro-form">
            <input type="hidden" name="id" value="">
            <input type="hidden" name="action" value="create">
            <div class="corpo-formulario">
              <p class="orientacao" id="aviso-formulario">
                Campos com * são obrigatórios.
              </p>
              <p class="mensagem erro" id="erro-formulario" role="alert" aria-live="assertive">
                <%= erro == null ? "" : erro %>
              </p>
              <div class="campo">
                <label for="data_inicio">
                  Data de início (gerada automaticamente)
                </label>
                <input type="date" id="data_inicio" disabled value="">
              </div>
              <div class="campo">
                <label for="data_vencimento">
                  Data de vencimento
                </label>
                <input type="date" id="data_vencimento" name="dataVencimento" required aria-describedby="ajuda-data_vencimento" value="">
                <small class="ajuda" id="ajuda-data_vencimento">
                  Deve ser posterior à data de início.
                </small>
              </div>
              <div class="campo">
                <label for="fk_plano_id">
                  Plano
                </label>
                <select id="fk_plano_id" name="fkPlano" required>
                  <option value="">
                    Selecione um plano
                  </option>
                  <% if (planosDisponiveis != null) { for (Plano plano : planosDisponiveis) { %>
                  <option value="<%= com.utils.ListaWeb.escapar(plano.getId()) %>">
                    <%= com.utils.ListaWeb.escapar(plano.getNome()) %>
                  </option>
                  <% } } %>
                </select>
              </div>
              <div class="campo">
                <label for="fk_endereco_id">
                  Endereço
                </label>
                <select id="fk_endereco_id" name="fkEndereco" required>
                  <option value="">
                    Selecione um endereço
                  </option>
                  <% if (enderecosDisponiveis != null) { for (Endereco endereco : enderecosDisponiveis) { %>
                  <option value="<%= com.utils.ListaWeb.escapar(endereco.getId()) %>">
                    <%= com.utils.ListaWeb.escapar(endereco.getNome()) %>
                    -
                    <%= com.utils.ListaWeb.escapar(endereco.getRua()) %>
                    ,
                    <%= com.utils.ListaWeb.escapar(endereco.getNumero()) %>
                  </option>
                  <% } } %>
                </select>
              </div>
              <div class="campo">
                <label for="status_contrato">
                  Status do contrato *
                </label>
                <select id="status_contrato" name="statusContrato" required>
                  <option value="">
                    Selecione
                  </option>
                  <option value="1">
                    Ativo
                  </option>
                  <option value="2">
                    Inativo
                  </option>
                  <option value="3">
                    Cancelado
                  </option>
                </select>
              </div>
            </div>
            <div class="rodape-formulario">
              <a class="botao botao-secundario" href="${pageContext.request.contextPath}/contratos">
                Cancelar
              </a>
              <button class="botao" type="submit">
                Salvar
              </button>
            </div>
          </form>
        </section>
        <p class="mensagem" id="mensagem" role="status" aria-live="polite">
        </p>
      </main>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/feedback.js" defer>
    </script>
  </body>
</html>
