<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.Contrato" %>
<%@ page import="com.model.Endereco" %>
<%@ page import="com.model.Plano" %>
<%
    Contrato contrato = (Contrato) request.getAttribute("contrato");
    List<Endereco> enderecosDisponiveis = (List<Endereco>) request.getAttribute("enderecosDisponiveis");
    List<Plano> planosDisponiveis = (List<Plano>) request.getAttribute("planosDisponiveis");
    String erro = (String) request.getAttribute("erro");
%>
<%!
    private String escaparHtml(Object valor) {
        if (valor == null) return "";
        return valor.toString().replace("&", "&amp;").replace("\"", "&quot;")
                .replace("<", "&lt;").replace(">", "&gt;");
    }
%>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Editar contrato | FIXA</title>
    <link rel="icon" href="<%= request.getContextPath() %>/assets/img/crud/simbolo-fixa.svg" type="image/svg+xml" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/fontes/fontes.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/crud.css" />
  </head>
  <body data-entidade="contratos" data-operacao="editar">
    <a class="pular-conteudo" href="#conteudo">Pular para o conteúdo</a>
    <aside class="menu-lateral" aria-label="Menu administrativo">
      <button
        class="marca-menu"
        type="button"
        id="controle-menu"
        aria-label="Expandir menu lateral"
        aria-expanded="false"
      >
        <img class="simbolo" src="<%= request.getContextPath() %>/assets/img/crud/simbolo-fixa.svg" alt="" />
        <img class="logo-menu" src="<%= request.getContextPath() %>/assets/img/crud/logo-fixa.svg" alt="FIXA" />
      </button>
      <nav class="navegacao" aria-label="Páginas do CRUD">
        <a href="<%= request.getContextPath() %>/analiticos" title="Página inicial">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path d="M3 11.5 12 4l9 7.5"></path>
            <path d="M5.5 10v9a1 1 0 0 0 1 1H10v-6h4v6h3.5a1 1 0 0 0 1-1v-9"></path>
          </svg>
          <span class="texto-menu">Página inicial</span>
        </a>
        <a href="<%= request.getContextPath() %>/instituicoes" title="Instituições">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <rect x="4" y="3" width="16" height="18" rx="1.5"></rect>
            <path d="M9 21v-5h6v5"></path>
            <path d="M8.5 7.5h1M14.5 7.5h1M8.5 11.5h1M14.5 11.5h1M8.5 15.5h1M14.5 15.5h1"></path>
          </svg>
          <span class="texto-menu">Instituições</span>
        </a>
        <a href="<%= request.getContextPath() %>/usuarios" title="Usuários">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path
              d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2M22 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"
            ></path>
            <circle cx="9" cy="7" r="4"></circle>
          </svg>
          <span class="texto-menu">Usuários</span>
        </a>
        <a href="<%= request.getContextPath() %>/enderecos" title="Endereços">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0Z"></path>
            <circle cx="12" cy="10" r="3"></circle>
          </svg>
          <span class="texto-menu">Endereços</span>
        </a>
        <a href="<%= request.getContextPath() %>/superAdmin" title="Super administradores">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10Z"></path>
          </svg>
          <span class="texto-menu">Super administradores</span>
        </a>
        <a href="<%= request.getContextPath() %>/planos" title="Planos">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path d="m12 3 10 5-10 5L2 8l10-5ZM2 12l10 5 10-5M2 16l10 5 10-5"></path>
          </svg>
          <span class="texto-menu">Planos</span>
        </a>
        <a href="<%= request.getContextPath() %>/contratos" title="Contratos" aria-current="page">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8Z"></path>
            <polyline points="14 2 14 8 20 8"></polyline>
            <line x1="16" y1="13" x2="8" y2="13"></line>
            <line x1="16" y1="17" x2="8" y2="17"></line>
            <line x1="10" y1="9" x2="8" y2="9"></line>
          </svg>
          <span class="texto-menu">Contratos</span>
        </a>
        <a href="<%= request.getContextPath() %>/pagamentos" title="Pagamentos">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <rect x="2" y="4" width="20" height="16" rx="2"></rect>
            <path d="M2 10h20M6 16h4"></path>
          </svg>
          <span class="texto-menu">Pagamentos</span>
        </a>
        <a href="<%= request.getContextPath() %>/fotoUsuario" title="Fotos de usuário">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            stroke-linecap="round"
            stroke-linejoin="round"
            aria-hidden="true"
          >
            <rect x="3" y="3" width="18" height="18" rx="2"></rect>
            <circle cx="8.5" cy="8.5" r="1.5"></circle>
            <polyline points="21 15 16 10 5 21"></polyline>
          </svg>
          <span class="texto-menu">Fotos de usuário</span>
        </a>
      </nav>
      <div class="rodape-menu"><form action="<%= request.getContextPath() %>/login-handler" method="post"><input type="hidden" name="action" value="logout" /><button class="voltar-login" type="submit">
          <svg
            class="icone"
            viewbox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="1.8"
            aria-hidden="true"
          >
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4M16 17l5-5-5-5M21 12H9"></path>
          </svg>
          <span class="texto-menu">Sair</span>
        </button></form></div>
    </aside>
    <div class="pagina">
      <header class="cabecalho">
        <div>
          <h1>Editar contrato</h1>
          <p>Atualize os dados do registro.</p>
        </div>
        <a class="botao botao-secundario" href="<%= request.getContextPath() %>/contratos">Voltar para a lista</a>
      </header>
      <main class="conteudo" id="conteudo">
        <section class="cartao formulario-pagina" aria-labelledby="titulo-formulario">
          <div class="titulo-cartao">
            <h2 id="titulo-formulario">Editar contrato</h2>
          </div>
          <form
            action="<%= request.getContextPath() %>/contratos"
            method="post"
            aria-describedby="aviso-formulario erro-formulario"
            id="registro-form"
          >
            <input type="hidden" name="ID" value="<%= contrato.getId() %>" />
            <input type="hidden" name="action" value="update" />
            <div class="corpo-formulario">
              <p class="orientacao" id="aviso-formulario">Campos com * são obrigatórios.</p>
              <p class="mensagem erro" id="erro-formulario" role="alert" aria-live="assertive"><%= escaparHtml(erro) %></p>
              <div class="campo">
                <label for="data_inicio">Data de início (gerada automaticamente)</label>
                <input type="date" id="data_inicio" disabled value="<%= escaparHtml(contrato.getDataInicio()) %>" />
              </div>
              <div class="campo">
                <label for="data_vencimento">Data de vencimento</label>
                <input
                  type="date"
                  id="data_vencimento"
                  name="DATA_VENCIMENTO" required
                  aria-describedby="ajuda-data_vencimento"
                  value="<%= escaparHtml(contrato.getDataVencimento()) %>"
                />
                <small class="ajuda" id="ajuda-data_vencimento">
                  Deve ser posterior à data de início.
                </small>
              </div>
              <div class="campo">
                <label for="fk_plano_id">Plano</label>
                <select id="fk_plano_id" name="FK_PLANO_ID" required>
                  <option value="">Selecione um plano</option>
                  <% if (planosDisponiveis != null) {
                    for (Plano planoOpcao : planosDisponiveis) { %>
                      <option value="<%= planoOpcao.getId() %>" <%= planoOpcao.getId().equals(contrato.getFkPlano()) ? "selected" : "" %>><%= escaparHtml(planoOpcao.getNome()) %></option>
                  <%  }
                  } %>
                </select>
              </div>
              <div class="campo">
                <label for="fk_endereco_id">Endereço</label>
                <select id="fk_endereco_id" name="FK_ENDERECO_ID" required>
                  <option value="">Selecione um endereço</option>
                  <% if (enderecosDisponiveis != null) {
                    for (Endereco enderecoOpcao : enderecosDisponiveis) { %>
                      <option value="<%= enderecoOpcao.getId() %>" <%= enderecoOpcao.getId().equals(contrato.getFkEndereco()) ? "selected" : "" %>><%= escaparHtml(enderecoOpcao.getNome()) %> — <%= escaparHtml(enderecoOpcao.getRua()) %>, <%= escaparHtml(enderecoOpcao.getNumero()) %></option>
                  <%  }
                  } %>
                </select>
              </div>
              <div class="campo">
                <label for="status_contrato">Status do contrato *</label>
                <select id="status_contrato" name="STATUS_CONTRATO" required>
                  <option value="">Selecione</option>
                  <option value="1" <%= contrato.getStatusContrato() != null && contrato.getStatusContrato().getCodigo() == 1 ? "selected" : "" %>>Ativo</option>
                  <option value="2" <%= contrato.getStatusContrato() != null && contrato.getStatusContrato().getCodigo() == 2 ? "selected" : "" %>>Inativo</option>
                  <option value="3" <%= contrato.getStatusContrato() != null && contrato.getStatusContrato().getCodigo() == 3 ? "selected" : "" %>>Cancelado</option>
                </select>
              </div>
            </div>
            <div class="rodape-formulario">
              <a class="botao botao-secundario" href="<%= request.getContextPath() %>/contratos">Cancelar</a>
              <button class="botao" type="submit">Salvar</button>
            </div>
          </form>
        </section>
        <p class="mensagem" id="mensagem" role="status" aria-live="polite"></p>
      </main>
    </div>
  </body>
</html>




