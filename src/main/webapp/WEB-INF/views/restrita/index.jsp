<%@ page import="java.util.ArrayList, java.util.List, com.DTO.analiticos.*, com.utils.ListaWeb" %>
<%
    // atributos enviados pelo AnaliticosServlet (cada um vem de uma view do banco)
    ResumoDTO resumo = (ResumoDTO) request.getAttribute("resumo");
    List<InstituicoesPorTipoDTO> instituicoes = (List<InstituicoesPorTipoDTO>) request.getAttribute("instituicoes");
    List<UsuariosPorAtividadeDTO> usuarios = (List<UsuariosPorAtividadeDTO>) request.getAttribute("usuarios");
    List<ContratosPorPlanoDTO> contratos = (List<ContratosPorPlanoDTO>) request.getAttribute("contratos");
    List<EnderecosPorInstituicaoDTO> enderecos = (List<EnderecosPorInstituicaoDTO>) request.getAttribute("enderecos");
    String erroIndicadores = (String) request.getAttribute("erroIndicadores");

    // se o banco falhou, o servlet nao manda nada: a tela abre zerada e mostra o aviso
    if (resumo == null) { resumo = new ResumoDTO(0, 0, 0); }
    if (instituicoes == null) { instituicoes = new ArrayList<>(); }
    if (usuarios == null) { usuarios = new ArrayList<>(); }
    if (contratos == null) { contratos = new ArrayList<>(); }
    if (enderecos == null) { enderecos = new ArrayList<>(); }

    // usuarios ativos e inativos (a view devolve uma linha para cada situacao)
    int ativos = 0, inativos = 0, percentualAtivos = 0, percentualInativos = 0;
    for (UsuariosPorAtividadeDTO situacao : usuarios) {
        if ("Ativo".equals(situacao.getAtividade())) {
            ativos = situacao.getUsuarios();
            percentualAtivos = situacao.getPercentual();
        } else {
            inativos = situacao.getUsuarios();
            percentualInativos = situacao.getPercentual();
        }
    }

    // total de contratos e os 5 planos com mais contratos
    int totalContratos = 0;
    for (ContratosPorPlanoDTO plano : contratos) { totalContratos += plano.getContratos(); }
    List<ContratosPorPlanoDTO> topPlanos = new ArrayList<>(contratos);
    topPlanos.sort((a, b) -> b.getContratos() - a.getContratos());
    if (topPlanos.size() > 5) { topPlanos = topPlanos.subList(0, 5); }
    int maiorPlano = topPlanos.isEmpty() ? 1 : Math.max(1, topPlanos.get(0).getContratos());

    // as 4 instituicoes com mais enderecos
    List<EnderecosPorInstituicaoDTO> topInstituicoes = new ArrayList<>(enderecos);
    topInstituicoes.sort((a, b) -> b.getEnderecos() - a.getEnderecos());
    if (topInstituicoes.size() > 4) { topInstituicoes = topInstituicoes.subList(0, 4); }

    String mediaEnderecos = resumo.getInstituicoes() == 0 ? "0"
            : String.format("%.1f", (double) resumo.getEnderecos() / resumo.getInstituicoes()).replace('.', ',');
%>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Página inicial | FIXA
    </title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/crud/simbolo-fixa.svg" type="image/svg+xml">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/crud.css">
    <style>
      .painel-graficos {
      margin-top: 24px;
      color: #0e1240;
      font-family: Inter, Arial, sans-serif;
      }
      .painel-graficos * {
      box-sizing: border-box;
      }
      .painel-graficos .resumos,
      .painel-graficos .graficos {
      display: grid;
      gap: 20px;
      }
      .painel-graficos .resumos {
      grid-template-columns: repeat(4, 1fr);
      margin-bottom: 20px;
      }
      .painel-graficos .graficos {
      grid-template-columns: repeat(2, 1fr);
      }
      .painel-graficos .resumo,
      .painel-graficos .grafico {
      min-width: 0;
      padding: 24px;
      border: 1px solid #e5e8f2;
      border-radius: 16px;
      background-color: #ffffff;
      }
      .painel-graficos .resumo {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      gap: 6px;
      }
      .painel-graficos .resumo-titulo {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #6b7280;
      font-size: 15px;
      }
      .painel-graficos .resumo-icone {
      width: 18px;
      height: 18px;
      flex-shrink: 0;
      color: #0e1240;
      }
      .painel-graficos .resumo-numero {
      font-size: 32px;
      line-height: 1.2;
      }
      .painel-graficos .resumo-detalhe {
      margin-bottom: 12px;
      color: #6b7280;
      font-size: 13px;
      line-height: 1.5;
      }
      .painel-graficos .link-tabela {
      color: #173f7c;
      font-size: 13px;
      font-weight: 600;
      text-decoration: none;
      }
      .painel-graficos .resumo .link-tabela {
      margin-top: auto;
      }
      .painel-graficos .link-tabela:hover {
      text-decoration: underline;
      }
      .painel-graficos .link-tabela:focus-visible {
      outline: 2px solid #173f7c;
      outline-offset: 4px;
      }
      .painel-graficos .grafico-cabecalho {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: 12px;
      }
      .painel-graficos .grafico-cabecalho h2 {
      margin: 0;
      font-size: 17px;
      line-height: 1.4;
      }
      .painel-graficos .grafico-descricao {
      margin: 8px 0 24px;
      color: #6b7280;
      font-size: 13px;
      line-height: 1.5;
      }
      .painel-graficos .barra-linha {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 18px;
      }
      .painel-graficos .barra-nome {
      width: 110px;
      flex-shrink: 0;
      font-size: 14px;
      }
      .painel-graficos .barra-fundo {
      height: 12px;
      flex: 1;
      overflow: hidden;
      border-radius: 20px;
      background-color: #e9edf6;
      }
      .painel-graficos .barra-preenchida {
      height: 100%;
      border-radius: 20px;
      background-color: #173f7c;
      }
      .painel-graficos .barra-valor {
      width: 60px;
      flex-shrink: 0;
      color: #6b7280;
      font-size: 12px;
      text-align: right;
      }
      .painel-graficos .usuarios-grafico {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 24px;
      }
      .painel-graficos .rosquinha {
      width: 150px;
      height: 150px;
      flex-shrink: 0;
      border-radius: 50%;
      background: conic-gradient(#173f7c 80%, #d9deee 0);
      display: flex;
      align-items: center;
      justify-content: center;
      }
      .painel-graficos .rosquinha-centro {
      width: 106px;
      height: 106px;
      border-radius: 50%;
      background-color: #ffffff;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      }
      .painel-graficos .rosquinha-centro strong {
      font-size: 28px;
      }
      .painel-graficos .rosquinha-centro span {
      color: #6b7280;
      font-size: 13px;
      }
      .painel-graficos .legenda {
      margin: 0;
      padding: 0;
      list-style: none;
      font-size: 13px;
      }
      .painel-graficos .legenda li {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 8px;
      margin: 12px 0;
      }
      .painel-graficos .legenda-cor {
      width: 12px;
      height: 12px;
      border-radius: 3px;
      }
      .painel-graficos .cor-ativos {
      background-color: #173f7c;
      }
      .painel-graficos .cor-inativos {
      background-color: #d9deee;
      }
      .painel-graficos .aviso-usuarios {
      margin: 20px 0 0;
      padding: 12px;
      border: 1px solid #f3e1a0;
      border-radius: 10px;
      background-color: #fff7dd;
      color: #6b4e00;
      font-size: 12px;
      line-height: 1.6;
      }
      .painel-graficos .colunas {
      display: flex;
      justify-content: space-around;
      gap: 12px;
      }
      .painel-graficos .coluna {
      min-width: 0;
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      }
      .painel-graficos .coluna-valor {
      margin-bottom: 8px;
      font-size: 14px;
      font-weight: 700;
      }
      .painel-graficos .coluna-barra {
      width: 100%;
      max-width: 60px;
      height: 120px;
      border-radius: 6px 6px 0 0;
      background-color: #173f7c;
      }
      .painel-graficos .coluna-nome {
      margin-top: 10px;
      color: #6b7280;
      font-size: 12px;
      text-align: center;
      overflow-wrap: break-word;
      }
      .painel-graficos .ranking {
      margin: 0;
      padding: 0;
      list-style: none;
      }
      .painel-graficos .ranking li {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 14px 0;
      border-bottom: 1px solid #e5e8f2;
      font-size: 14px;
      }
      .painel-graficos .ranking li:last-child {
      border-bottom: 0;
      }
      .painel-graficos .ranking-posicao {
      width: 28px;
      height: 28px;
      flex-shrink: 0;
      border-radius: 6px;
      background-color: #eef1fa;
      color: #173f7c;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      font-weight: 700;
      }
      .painel-graficos .ranking-nome {
      flex: 1;
      }
      @media (max-width: 1200px) {
      .painel-graficos .resumos {
      grid-template-columns: repeat(2, 1fr);
      }
      }
      @media (max-width: 1050px) {
      .painel-graficos .graficos {
      grid-template-columns: 1fr;
      }
      }
      @media (max-width: 600px) {
      .painel-graficos .resumos {
      grid-template-columns: 1fr;
      }
      .painel-graficos .resumo,
      .painel-graficos .grafico {
      padding: 18px;
      }
      .painel-graficos .barra-linha {
      gap: 8px;
      }
      .painel-graficos .barra-nome {
      width: 85px;
      font-size: 12px;
      }
      .painel-graficos .barra-valor {
      width: 52px;
      font-size: 11px;
      }
      .painel-graficos .colunas {
      gap: 8px;
      }
      .painel-graficos .coluna-nome {
      font-size: 11px;
      }
      }
    </style>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/feedback.css">
  </head>
  <body>
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
        <a href="${pageContext.request.contextPath}/analiticos" title="Página inicial" aria-current="page">
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
            Página inicial
          </h1>
          <p>
            Painel administrativo
          </p>
        </div>
      </header>
      <main class="conteudo" id="conteudo">
        <section class="painel-graficos" aria-label="Resumo e gráficos do CRUD">
          <div class="resumos">
            <article class="resumo" tabindex="0" data-dica="Instituições <%= resumo.getInstituicoes() %> <%= instituicoes.size() %> tipos cadastrados">
              <span class="resumo-titulo">
                <svg class="resumo-icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true">
                  <rect x="4" y="3" width="16" height="18" rx="1.5">
                  </rect>
                  <path d="M9 21v-5h6v5M8 7h2M14 7h2M8 11h2M14 11h2">
                  </path>
                </svg>
                Instituições
              </span>
              <strong class="resumo-numero">
                <%= resumo.getInstituicoes() %>
              </strong>
              <span class="resumo-detalhe">
                <%= instituicoes.size() %> tipos cadastrados
              </span>
              <a class="link-tabela" href="${pageContext.request.contextPath}/instituicoes">
                Ver tabela →
              </a>
            </article>
            <article class="resumo" tabindex="0" data-dica="Usuários <%= resumo.getUsuarios() %> <%= ativos %> ativos (<%= percentualAtivos %>%)">
              <span class="resumo-titulo">
                <svg class="resumo-icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true">
                  <circle cx="9" cy="7" r="4">
                  </circle>
                  <path d="M2 21v-2a4 4 0 0 1 4-4h6a4 4 0 0 1 4 4v2M16 3a4 4 0 0 1 0 8M22 21v-2a4 4 0 0 0-3-4">
                  </path>
                </svg>
                Usuários
              </span>
              <strong class="resumo-numero">
                <%= resumo.getUsuarios() %>
              </strong>
              <span class="resumo-detalhe">
                <%= ativos %> ativos (<%= percentualAtivos %>%)
              </span>
              <a class="link-tabela" href="${pageContext.request.contextPath}/usuarios">
                Ver tabela →
              </a>
            </article>
            <article class="resumo" tabindex="0" data-dica="Endereços <%= resumo.getEnderecos() %> <%= mediaEnderecos %> por instituição, em média">
              <span class="resumo-titulo">
                <svg class="resumo-icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true">
                  <path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 0 1 18 0Z">
                  </path>
                  <circle cx="12" cy="10" r="3">
                  </circle>
                </svg>
                Endereços
              </span>
              <strong class="resumo-numero">
                <%= resumo.getEnderecos() %>
              </strong>
              <span class="resumo-detalhe">
                <%= mediaEnderecos %> por instituição, em média
              </span>
              <a class="link-tabela" href="${pageContext.request.contextPath}/enderecos">
                Ver tabela →
              </a>
            </article>
            <article class="resumo" tabindex="0" data-dica="Contratos <%= totalContratos %> Em <%= contratos.size() %> planos">
              <span class="resumo-titulo">
                <svg class="resumo-icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true">
                  <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8ZM14 2v6h6M8 13h8M8 17h8">
                  </path>
                </svg>
                Contratos
              </span>
              <strong class="resumo-numero">
                <%= totalContratos %>
              </strong>
              <span class="resumo-detalhe">
                Em <%= contratos.size() %> planos
              </span>
              <a class="link-tabela" href="${pageContext.request.contextPath}/contratos">
                Ver tabela →
              </a>
            </article>
          </div>
          <div class="graficos">
            <article class="grafico">
              <header class="grafico-cabecalho">
                <h2>
                  Instituições por tipo
                </h2>
                <a class="link-tabela" href="${pageContext.request.contextPath}/instituicoes">
                  Ver tabela →
                </a>
              </header>
              <p class="grafico-descricao">
                Quantas instituições existem de cada tipo
              </p>
              <% for (InstituicoesPorTipoDTO item : instituicoes) { %>
              <div class="barra-linha" tabindex="0" data-dica="<%= ListaWeb.escapar(item.getTipo()) %> <%= item.getInstituicoes() %> · <%= item.getPercentual() %>%">
                <span class="barra-nome"><%= ListaWeb.escapar(item.getTipo()) %></span>
                <div class="barra-fundo" aria-hidden="true">
                  <div class="barra-preenchida" style="width: <%= item.getPercentual() %>%;"></div>
                </div>
                <span class="barra-valor"><%= item.getInstituicoes() %> · <%= item.getPercentual() %>%</span>
              </div>
              <% } %>
            </article>
            <article class="grafico">
              <header class="grafico-cabecalho">
                <h2>
                  Usuários ativos x inativos
                </h2>
                <a class="link-tabela" href="${pageContext.request.contextPath}/usuarios">
                  Ver tabela →
                </a>
              </header>
              <p class="grafico-descricao">
                Situação das contas de usuário
              </p>
              <div class="usuarios-grafico">
                <div class="rosquinha" tabindex="0" data-dica="<%= percentualAtivos %>% ativos" style="background: conic-gradient(#173f7c <%= percentualAtivos %>%, #d9deee 0);">
                  <div class="rosquinha-centro">
                    <strong><%= percentualAtivos %>%</strong>
                    <span>ativos</span>
                  </div>
                </div>
                <ul class="legenda">
                  <li>
                    <span class="legenda-cor cor-ativos" aria-hidden="true"></span>
                    Ativos
                    <strong><%= ativos %> (<%= percentualAtivos %>%)</strong>
                  </li>
                  <li>
                    <span class="legenda-cor cor-inativos" aria-hidden="true"></span>
                    Inativos
                    <strong><%= inativos %> (<%= percentualInativos %>%)</strong>
                  </li>
                </ul>
              </div>
              <% if (inativos > 0) { %>
              <p class="aviso-usuarios">
                <%= inativos %> <%= inativos == 1 ? "usuário inativo" : "usuários inativos" %>. Vale revisar se ainda precisam de acesso.
              </p>
              <% } %>
            </article>
            <article class="grafico">
              <header class="grafico-cabecalho">
                <h2>
                  Contratos por plano
                </h2>
                <a class="link-tabela" href="${pageContext.request.contextPath}/contratos">
                  Ver tabela →
                </a>
              </header>
              <p class="grafico-descricao">
                Top <%= topPlanos.size() %> de <%= contratos.size() %> planos com contrato
              </p>
              <div class="colunas">
                <% for (ContratosPorPlanoDTO item : topPlanos) { %>
                <div class="coluna" tabindex="0" data-dica="<%= item.getContratos() %> <%= ListaWeb.escapar(item.getPlano()) %>">
                  <span class="coluna-valor"><%= item.getContratos() %></span>
                  <div class="coluna-barra" aria-hidden="true" style="height: <%= Math.max(6, 120 * item.getContratos() / maiorPlano) %>px;"></div>
                  <span class="coluna-nome"><%= ListaWeb.escapar(item.getPlano()) %></span>
                </div>
                <% } %>
              </div>
            </article>
            <article class="grafico">
              <header class="grafico-cabecalho">
                <h2>
                  Endereços por instituição
                </h2>
                <a class="link-tabela" href="${pageContext.request.contextPath}/enderecos">
                  Ver tabela →
                </a>
              </header>
              <p class="grafico-descricao">
                Top <%= topInstituicoes.size() %> de <%= resumo.getInstituicoes() %> instituições cadastradas
              </p>
              <ol class="ranking">
                <% int posicao = 1; for (EnderecosPorInstituicaoDTO item : topInstituicoes) { %>
                <li>
                  <span class="ranking-posicao"><%= posicao++ %></span>
                  <span class="ranking-nome"><%= ListaWeb.escapar(item.getInstituicao()) %></span>
                  <strong><%= item.getEnderecos() %></strong>
                </li>
                <% } %>
              </ol>
            </article>
          </div>
        </section>
        <p class="mensagem" id="mensagem" role="status"><%= erroIndicadores == null ? "" : ListaWeb.escapar(erroIndicadores) %></p>
      </main>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/feedback.js" defer>
    </script>
  </body>
</html>
