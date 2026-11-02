<%@ page import="java.util.List" %>
<%@ page import="com.DTO.EnderecoDTO" %>
<% List<EnderecoDTO> enderecos = (List<EnderecoDTO>) request.getAttribute("enderecos"); %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Endereços | FIXA
    </title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/crud/simbolo-fixa.svg" type="image/svg+xml">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/crud.css">
    <script src="${pageContext.request.contextPath}/assets/js/operadores-filtro.js" defer>
    </script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feedback.css">
  </head>
  <body data-entidade="enderecos">
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
        <a href="${pageContext.request.contextPath}/enderecos" title="Endereços" aria-current="page">
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
        <a href="${pageContext.request.contextPath}/contratos" title="Contratos">
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
            Endereços
          </h1>
          <p>
            Consulte, filtre e edite os registros de endereços.
          </p>
        </div>
        <div class="acoes">
          <a class="botao" id="novo" href="${pageContext.request.contextPath}/enderecos?action=create">
            + Novo
          </a>
        </div>
      </header>
      <main class="conteudo" id="conteudo">
        <section class="cartao lista" aria-label="Registros de endereços">
          <div class="barra-tabela">
            <strong id="contagem" role="status">
              <%= enderecos == null ? 0 : enderecos.size() %>
              <%= enderecos != null && enderecos.size() == 1 ? "registro" : "registros" %>
            </strong>
            <%
    String urlLista = "enderecos";
    String[][] campos = {
        {"ID", "ID"},
        {"NOME", "Nome do endereço"},
        {"RUA", "Rua"},
        {"BAIRRO", "Bairro"},
        {"CIDADE", "Cidade"},
        {"ESTADO", "Estado (UF)"},
        {"CEP", "CEP"},
        {"FK_INSTITUICAO_ID", "Instituição (ID)"}
    };
%>
<%@ include file="/WEB-INF/views/restrita/_filtro.jspf" %>
            <div class="pesquisa">
              <span aria-hidden="true">
                <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
                  <circle cx="10.5" cy="10.5" r="6.5">
                  </circle>
                  <path d="m16 16 5 5">
                  </path>
                </svg>
              </span>
              <label class="sr-only" for="pesquisa">
                Pesquisar em todos os campos
              </label>
              <input id="pesquisa" type="search" name="pesquisa" form="filtro-form" value="<%= ListaWeb.escapar(pesquisa) %>" placeholder="Pesquisar em todos os campos" autocomplete="off" aria-label="Pesquisar em todos os campos">
            </div>
          </div>
          <%@ include file="/WEB-INF/views/restrita/_filtros-ativos.jspf" %>
          <div class="tabela-rolagem" tabindex="0" role="region" aria-label="Tabela de endereços, com rolagem horizontal e vertical">
            <table class="tabela-crud">
              <caption class="sr-only">
                Use os cabeçalhos para alternar entre ordem crescente e decrescente.
              </caption>
              <thead>
                <tr>
                  <th scope="col" data-campo="id" aria-sort="<%= ListaWeb.ariaSort(ordenacao, "ID") %>">
                    <button type="submit" form="filtro-form" class="ordenar" name="ordenacao" value="<%= ListaWeb.proximaOrdenacao(ordenacao, "ID") %>">
                      ID
                      <svg class="icone-ordenacao" viewBox="0 0 12 18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path class="seta-cima" d="m2 6 4-4 4 4">
                        </path>
                        <path class="seta-baixo" d="m2 12 4 4 4-4">
                        </path>
                      </svg>
                    </button>
                  </th>
                  <th scope="col" data-campo="nome" aria-sort="<%= ListaWeb.ariaSort(ordenacao, "NOME") %>">
                    <button type="submit" form="filtro-form" class="ordenar" name="ordenacao" value="<%= ListaWeb.proximaOrdenacao(ordenacao, "NOME") %>">
                      Nome do endereço
                      <svg class="icone-ordenacao" viewBox="0 0 12 18" fill="none" stroke="currentColor" stroke-width="2.5" aria-hidden="true">
                        <path class="seta-cima" d="m2 6 4-4 4 4">
                        </path>
                        <path class="seta-baixo" d="m2 12 4 4 4-4">
                        </path>
                      </svg>
                    </button>
                  </th>
                  <th scope="col" data-campo="rua" aria-sort="<%= ListaWeb.ariaSort(ordenacao, "RUA") %>">
                    <button type="submit" form="filtro-form" class="ordenar" name="ordenacao" value="<%= ListaWeb.proximaOrdenacao(ordenacao, "RUA") %>">
                      Rua
                      <svg class="icone-ordenacao" viewBox="0 0 12 18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path class="seta-cima" d="m2 6 4-4 4 4">
                        </path>
                        <path class="seta-baixo" d="m2 12 4 4 4-4">
                        </path>
                      </svg>
                    </button>
                  </th>
                  <th scope="col" data-campo="numero">Número</th>
                  <th scope="col" data-campo="complemento">Complemento</th>
                  <th scope="col" data-campo="bairro" aria-sort="<%= ListaWeb.ariaSort(ordenacao, "BAIRRO") %>">
                    <button type="submit" form="filtro-form" class="ordenar" name="ordenacao" value="<%= ListaWeb.proximaOrdenacao(ordenacao, "BAIRRO") %>">
                      Bairro
                      <svg class="icone-ordenacao" viewBox="0 0 12 18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path class="seta-cima" d="m2 6 4-4 4 4">
                        </path>
                        <path class="seta-baixo" d="m2 12 4 4 4-4">
                        </path>
                      </svg>
                    </button>
                  </th>
                  <th scope="col" data-campo="cidade" aria-sort="<%= ListaWeb.ariaSort(ordenacao, "CIDADE") %>">
                    <button type="submit" form="filtro-form" class="ordenar" name="ordenacao" value="<%= ListaWeb.proximaOrdenacao(ordenacao, "CIDADE") %>">
                      Cidade
                      <svg class="icone-ordenacao" viewBox="0 0 12 18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path class="seta-cima" d="m2 6 4-4 4 4">
                        </path>
                        <path class="seta-baixo" d="m2 12 4 4 4-4">
                        </path>
                      </svg>
                    </button>
                  </th>
                  <th scope="col" data-campo="estado" aria-sort="<%= ListaWeb.ariaSort(ordenacao, "ESTADO") %>">
                    <button type="submit" form="filtro-form" class="ordenar" name="ordenacao" value="<%= ListaWeb.proximaOrdenacao(ordenacao, "ESTADO") %>">
                      Estado (UF)
                      <svg class="icone-ordenacao" viewBox="0 0 12 18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path class="seta-cima" d="m2 6 4-4 4 4">
                        </path>
                        <path class="seta-baixo" d="m2 12 4 4 4-4">
                        </path>
                      </svg>
                    </button>
                  </th>
                  <th scope="col" data-campo="cep" aria-sort="<%= ListaWeb.ariaSort(ordenacao, "CEP") %>">
                    <button type="submit" form="filtro-form" class="ordenar" name="ordenacao" value="<%= ListaWeb.proximaOrdenacao(ordenacao, "CEP") %>">
                      CEP
                      <svg class="icone-ordenacao" viewBox="0 0 12 18" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path class="seta-cima" d="m2 6 4-4 4 4">
                        </path>
                        <path class="seta-baixo" d="m2 12 4 4 4-4">
                        </path>
                      </svg>
                    </button>
                  </th>
                  <th scope="col" data-campo="instituicao" class="coluna-relacionada">Instituição</th>
                  <th scope="col" class="coluna-acoes">
                    Ações
                  </th>
                </tr>
              </thead>
              <tbody id="registros">
                <%
                if (enderecos != null && !enderecos.isEmpty()) {
                for (EnderecoDTO endereco : enderecos) {
                %>
                <tr>
                  <td data-rotulo="ID" data-campo="id">
                    <%= ListaWeb.escapar(endereco.getId()) %>
                  </td>
                  <td data-rotulo="Nome do endereço" data-campo="nome" class=" registro-titulo">
                    <%= ListaWeb.escapar(endereco.getNome()) %>
                  </td>
                  <td data-rotulo="Rua" data-campo="rua" class=" campo-largo">
                    <%= ListaWeb.escapar(endereco.getRua()) %>
                  </td>
                  <td data-rotulo="Número" data-campo="numero">
                    <%= ListaWeb.escapar(endereco.getNumero()) %>
                  </td>
                  <td data-rotulo="Complemento" data-campo="complemento">
                    <%= ListaWeb.escapar(endereco.getComplemento()) %>
                  </td>
                  <td data-rotulo="Bairro" data-campo="bairro">
                    <%= ListaWeb.escapar(endereco.getBairro()) %>
                  </td>
                  <td data-rotulo="Cidade" data-campo="cidade">
                    <%= ListaWeb.escapar(endereco.getCidade()) %>
                  </td>
                  <td data-rotulo="Estado (UF)" data-campo="estado">
                    <%= ListaWeb.escapar(endereco.getEstado()) %>
                  </td>
                  <td data-rotulo="CEP" data-campo="cep">
                    <%= ListaWeb.escapar(endereco.getCep()) %>
                  </td>
                  <td class="coluna-relacionada" data-rotulo="Instituição" data-campo="instituicao">
                    <%= ListaWeb.escapar(endereco.getInstituicaoNome()) %>
                  </td>
                  <td class="coluna-acoes" data-rotulo="Ações" data-campo="acoes">
                    <div class="acoes-linha">
                      <a class="editar" href="${pageContext.request.contextPath}/enderecos?action=update&id=<%= ListaWeb.escapar(endereco.getId()) %>">
                        <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true">
                          <path d="M16 3l5 5L8 21H3v-5L16 3Z M14 5l5 5">
                          </path>
                        </svg>
                        Editar
                      </a>
                      <form action="${pageContext.request.contextPath}/enderecos" method="post" class="form-excluir">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="<%= ListaWeb.escapar(endereco.getId()) %>">
                        <button class="excluir" type="submit">
                          <svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true">
                            <path d="M3 6h18M9 6V3h6v3M5 6l1 15h12l1-15M10 10v7M14 10v7">
                            </path>
                          </svg>
                          Excluir
                        </button>
                      </form>
                    </div>
                  </td>
                </tr>
                <% } } else { %>
                <tr>
                  <td class="vazio" colspan="11">
                    Nenhum registro encontrado.
                  </td>
                </tr>
                <% } %>
              </tbody>
            </table>
<% if (ordenacao != null) { %><input type="hidden" form="filtro-form" name="ordenacao" value="<%= ListaWeb.escapar(ordenacao) %>"><% } %>
          </div>
          <footer class="rodape-tabela" id="rodape-tabela">
            <%= enderecos == null ? 0 : enderecos.size() %>
            de
            <%= enderecos == null ? 0 : enderecos.size() %>
            <%= enderecos != null && enderecos.size() == 1 ? "registro" : "registros" %>
          </footer>
        </section>
        <p class="mensagem" id="mensagem" role="status" aria-live="polite"><%= erro == null ? "" : ListaWeb.escapar(erro) %></p>
      </main>
    </div>
    <dialog class="modal" id="exclusao-dialog" aria-labelledby="titulo-exclusao">
      <h2 id="titulo-exclusao">
        Tem certeza que deseja excluir?
      </h2>
      <p id="registro-exclusao">
      </p>
      <form method="dialog" class="acoes-exclusao">
        <button class="botao botao-secundario" type="submit">
          Cancelar
        </button>
        <button class="botao botao-excluir" id="confirmar-exclusao" type="button">
          Excluir
        </button>
      </form>
    </dialog>
    <script src="${pageContext.request.contextPath}/assets/js/feedback.js" defer>
    </script>
<script src="${pageContext.request.contextPath}/assets/js/confirmar-exclusao.js" defer></script>
<%@ include file="/WEB-INF/views/restrita/_aviso.jspf" %>
  </body>
</html>
