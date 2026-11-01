<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Locale, com.DTO.analiticos.ResumoDTO, com.DTO.analiticos.InstituicoesPorTipoDTO, com.DTO.analiticos.UsuariosPorAtividadeDTO, com.DTO.analiticos.ContratosPorPlanoDTO, com.DTO.analiticos.EnderecosPorInstituicaoDTO" %>
<%!
  private String escaparHtml(Object valor) {
    if (valor == null) return "";
    return valor.toString().replace("&", "&amp;").replace("<", "&lt;")
        .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
  }
%>
<%
  ResumoDTO resumo = (ResumoDTO) request.getAttribute("resumo");
  List<InstituicoesPorTipoDTO> instituicoes = (List<InstituicoesPorTipoDTO>) request.getAttribute("instituicoes");
  List<UsuariosPorAtividadeDTO> usuarios = (List<UsuariosPorAtividadeDTO>) request.getAttribute("usuarios");
  List<ContratosPorPlanoDTO> contratos = (List<ContratosPorPlanoDTO>) request.getAttribute("contratos");
  List<EnderecosPorInstituicaoDTO> enderecos = (List<EnderecosPorInstituicaoDTO>) request.getAttribute("enderecos");
  String erroIndicadores = (String) request.getAttribute("erroIndicadores");
  int totalInstituicoes = resumo == null ? 0 : resumo.getInstituicoes();
  int totalUsuarios = resumo == null ? 0 : resumo.getUsuarios();
  int totalEnderecos = resumo == null ? 0 : resumo.getEnderecos();
  int usuariosAtivos = 0;
  int usuariosInativos = 0;
  if (usuarios != null) {
    for (UsuariosPorAtividadeDTO item : usuarios) {
      if (item.getAtividade() != null && item.getAtividade().toLowerCase(Locale.ROOT).startsWith("ativ")) {
        usuariosAtivos += item.getUsuarios();
      } else {
        usuariosInativos += item.getUsuarios();
      }
    }
  }
  int totalUsuariosAtividade = usuariosAtivos + usuariosInativos;
  int percentualAtivos = totalUsuariosAtividade == 0 ? 0 : (int) Math.round(usuariosAtivos * 100.0 / totalUsuariosAtividade);
  int percentualInativos = totalUsuariosAtividade == 0 ? 0 : 100 - percentualAtivos;
  String percentualAtivosCss = String.format(Locale.US, "%.1f", totalUsuariosAtividade == 0 ? 0 : usuariosAtivos * 100.0 / totalUsuariosAtividade);
  int totalContratos = 0;
  int maxContratos = 0;
  if (contratos != null) {
    for (ContratosPorPlanoDTO item : contratos) {
      totalContratos += item.getContratos();
      maxContratos = Math.max(maxContratos, item.getContratos());
    }
  }
  int mediaEnderecos = totalInstituicoes == 0 ? 0 : (int) Math.round(totalEnderecos * 1.0 / totalInstituicoes);
  String contexto = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Página inicial | FIXA</title>
    <link rel="icon" href="<%= contexto %>/assets/img/crud/simbolo-fixa.svg" type="image/svg+xml" />
    <link rel="stylesheet" href="<%= contexto %>/assets/fontes/fontes.css" />
    <link rel="stylesheet" href="<%= contexto %>/assets/css/crud.css" />
    <style>
      .painel { display: grid; gap: 20px; }
      .cartoes-resumo, .graficos { display: grid; gap: 20px; }
      .cartoes-resumo { grid-template-columns: repeat(4, minmax(0, 1fr)); }
      .graficos { grid-template-columns: repeat(2, minmax(0, 1fr)); }
      .resumo, .grafico { min-width: 0; padding: 22px; border: 1px solid #e7e3f4; border-radius: 14px; background: #fff; }
      .resumo { display: flex; flex-direction: column; gap: 8px; }
      .resumo span, .descricao, .barra-valor, .legenda { color: #8992a5; }
      .resumo strong { font-size: 32px; line-height: 1.2; }
      .resumo small { min-height: 18px; color: #8992a5; }
      .link-tabela { margin-top: auto; color: #193f7d; font-size: 13px; font-weight: 600; }
      .grafico h2 { margin: 0; font-size: 17px; }
      .descricao { margin: 7px 0 20px; font-size: 13px; }
      .grafico-cabecalho { display: flex; align-items: center; justify-content: space-between; gap: 12px; }
      .barra-linha { display: grid; grid-template-columns: minmax(90px, 140px) minmax(40px, 1fr) 70px; align-items: center; gap: 10px; margin: 16px 0; }
      .barra-nome { overflow-wrap: anywhere; }
      .barra-fundo { height: 12px; overflow: hidden; border-radius: 99px; background: #e9edf6; }
      .barra-preenchida { height: 100%; min-width: 0; border-radius: inherit; background: #193f7d; }
      .barra-valor { font-size: 12px; text-align: right; }
      .usuarios-grafico { display: flex; align-items: center; flex-wrap: wrap; gap: 22px; }
      .rosquinha { display: grid; width: 148px; height: 148px; flex: 0 0 auto; place-items: center; border-radius: 50%; background: conic-gradient(#193f7d <%= percentualAtivosCss %>%, #d9deee 0); }
      .rosquinha-centro { display: flex; width: 104px; height: 104px; flex-direction: column; align-items: center; justify-content: center; border-radius: 50%; background: #fff; }
      .rosquinha-centro strong { font-size: 26px; }
      .rosquinha-centro span { color: #8992a5; font-size: 12px; }
      .legenda { margin: 0; padding: 0; list-style: none; font-size: 13px; }
      .legenda li { display: flex; align-items: center; gap: 8px; margin: 12px 0; }
      .legenda-cor { width: 12px; height: 12px; flex: 0 0 auto; border-radius: 3px; background: #193f7d; }
      .legenda-cor.inativo { background: #d9deee; }
      .colunas { display: flex; min-height: 185px; align-items: flex-end; justify-content: space-around; gap: 12px; overflow-x: auto; }
      .coluna { display: flex; min-width: 48px; max-width: 90px; height: 170px; flex: 1 0 48px; flex-direction: column; align-items: center; justify-content: flex-end; }
      .coluna-barra { width: min(100%, 54px); min-height: 3px; border-radius: 5px 5px 0 0; background: #193f7d; }
      .coluna-nome { width: 100%; margin-top: 8px; color: #8992a5; font-size: 11px; text-align: center; overflow-wrap: anywhere; }
      .ranking { margin: 0; padding: 0; list-style: none; }
      .ranking li { display: flex; align-items: center; gap: 12px; padding: 13px 0; border-bottom: 1px solid #e7e3f4; }
      .ranking li:last-child { border-bottom: 0; }
      .ranking-posicao { display: grid; width: 28px; height: 28px; flex: 0 0 auto; place-items: center; border-radius: 6px; background: #eef1fa; color: #193f7d; font-size: 12px; font-weight: 700; }
      .ranking-nome { flex: 1; overflow-wrap: anywhere; }
      .indicadores-erro { padding: 14px 16px; border: 1px solid #f3e1a0; border-radius: 10px; background: #fff7dd; color: #6b4e00; }
      .vazio-painel { color: #8992a5; }
      @media (max-width: 1050px) { .cartoes-resumo { grid-template-columns: repeat(2, minmax(0, 1fr)); } .graficos { grid-template-columns: 1fr; } }
      @media (max-width: 600px) { .cartoes-resumo { grid-template-columns: 1fr; } .resumo, .grafico { padding: 18px; } .barra-linha { grid-template-columns: minmax(76px, 100px) minmax(35px, 1fr) 56px; gap: 7px; font-size: 12px; } }
    </style>
  </head>
  <body>
    <a class="pular-conteudo" href="#conteudo">Pular para o conteúdo</a>
    <aside class="menu-lateral" aria-label="Menu administrativo">
      <a class="marca-menu" href="<%= contexto %>/analiticos" aria-label="FIXA - página inicial">
        <img class="simbolo" src="<%= contexto %>/assets/img/crud/simbolo-fixa.svg" alt="" />
        <img class="logo-menu" src="<%= contexto %>/assets/img/crud/logo-fixa.svg" alt="FIXA" />
      </a>
      <nav class="navegacao" aria-label="Páginas do CRUD">
        <a href="<%= contexto %>/analiticos" aria-current="page"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path d="m3 11 9-7 9 7M5 10v10h14V10M9 20v-6h6v6" /></svg><span class="texto-menu">Página inicial</span></a>
        <a href="<%= contexto %>/instituicoes"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><rect x="4" y="3" width="16" height="18" rx="1.5" /><path d="M9 21v-5h6v5M8 8h1m6 0h1m-8 4h1m6 0h1" /></svg><span class="texto-menu">Instituições</span></a>
        <a href="<%= contexto %>/usuarios"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><circle cx="9" cy="7" r="4" /><path d="M2 21v-2a4 4 0 0 1 4-4h6a4 4 0 0 1 4 4v2m1-14a4 4 0 0 1 0 8m2 2a4 4 0 0 1 3 4v1" /></svg><span class="texto-menu">Usuários</span></a>
        <a href="<%= contexto %>/enderecos"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path d="M21 10c0 7-9 13-9 13S3 17 3 10a9 9 0 1 1 18 0Z" /><circle cx="12" cy="10" r="3" /></svg><span class="texto-menu">Endereços</span></a>
        <a href="<%= contexto %>/superAdmin"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10Z" /></svg><span class="texto-menu">Super administradores</span></a>
        <a href="<%= contexto %>/planos"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path d="m12 3 10 5-10 5L2 8l10-5ZM2 12l10 5 10-5M2 16l10 5 10-5" /></svg><span class="texto-menu">Planos</span></a>
        <a href="<%= contexto %>/contratos"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8Z" /><path d="M14 2v6h6M8 13h8m-8 4h8" /></svg><span class="texto-menu">Contratos</span></a>
        <a href="<%= contexto %>/pagamentos"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><rect x="2" y="4" width="20" height="16" rx="2" /><path d="M2 10h20M6 16h4" /></svg><span class="texto-menu">Pagamentos</span></a>
        <a href="<%= contexto %>/fotoUsuario"><svg class="icone" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" aria-hidden="true"><rect x="3" y="3" width="18" height="18" rx="2" /><circle cx="8.5" cy="8.5" r="1.5" /><path d="m21 15-5-5L5 21" /></svg><span class="texto-menu">Fotos de usuário</span></a>
      </nav>
      <div class="rodape-menu">
        <form action="<%= contexto %>/login-handler" method="post">
          <input type="hidden" name="action" value="logout" />
          <button class="voltar-login" type="submit"><span class="texto-menu">Sair</span></button>
        </form>
      </div>
    </aside>
    <div class="pagina">
      <header class="cabecalho">
        <div><h1>Página inicial</h1><p>Painel administrativo</p></div>
      </header>
      <main class="conteudo" id="conteudo">
        <% if (erroIndicadores != null) { %>
          <p class="indicadores-erro" role="alert"><%= escaparHtml(erroIndicadores) %></p>
        <% } else if (resumo == null && instituicoes == null && usuarios == null && contratos == null && enderecos == null) { %>
          <p class="vazio-painel">Os indicadores ainda não foram carregados.</p>
        <% } else { %>
        <section class="painel" aria-label="Resumo e indicadores">
          <div class="cartoes-resumo">
            <article class="resumo"><span>Instituições</span><strong><%= totalInstituicoes %></strong><small><%= instituicoes == null ? 0 : instituicoes.size() %> tipos encontrados</small><a class="link-tabela" href="<%= contexto %>/instituicoes">Abrir listagem →</a></article>
            <article class="resumo"><span>Usuários</span><strong><%= totalUsuarios %></strong><small><%= usuariosAtivos %> ativos (<%= percentualAtivos %>%)</small><a class="link-tabela" href="<%= contexto %>/usuarios">Abrir listagem →</a></article>
            <article class="resumo"><span>Endereços</span><strong><%= totalEnderecos %></strong><small><%= mediaEnderecos %> por instituição, em média</small><a class="link-tabela" href="<%= contexto %>/enderecos">Abrir listagem →</a></article>
            <article class="resumo"><span>Contratos</span><strong><%= totalContratos %></strong><small><%= contratos == null ? 0 : contratos.size() %> planos com dados</small><a class="link-tabela" href="<%= contexto %>/contratos">Abrir listagem →</a></article>
          </div>
          <div class="graficos">
            <article class="grafico">
              <header class="grafico-cabecalho"><h2>Instituições por tipo</h2><a class="link-tabela" href="<%= contexto %>/instituicoes">Listagem →</a></header>
              <p class="descricao">Distribuição por tipo cadastrado</p>
              <% if (instituicoes == null || instituicoes.isEmpty()) { %><p class="vazio-painel">Nenhum dado encontrado.</p><% } else { for (InstituicoesPorTipoDTO item : instituicoes) { int largura = Math.max(0, Math.min(100, item.getPercentual())); %>
                <div class="barra-linha"><span class="barra-nome"><%= escaparHtml(item.getTipo()) %></span><div class="barra-fundo" aria-hidden="true"><div class="barra-preenchida" style="width:<%= largura %>%"></div></div><span class="barra-valor"><%= item.getInstituicoes() %> · <%= largura %>%</span></div>
              <% } } %>
            </article>
            <article class="grafico">
              <header class="grafico-cabecalho"><h2>Usuários ativos e inativos</h2><a class="link-tabela" href="<%= contexto %>/usuarios">Listagem →</a></header>
              <p class="descricao">Situação das contas de usuário</p>
              <% if (totalUsuariosAtividade == 0) { %><p class="vazio-painel">Nenhum dado encontrado.</p><% } else { %>
                <div class="usuarios-grafico"><div class="rosquinha" aria-label="<%= percentualAtivos %>% de usuários ativos"><div class="rosquinha-centro"><strong><%= percentualAtivos %>%</strong><span>ativos</span></div></div>
                  <ul class="legenda"><li><span class="legenda-cor"></span>Ativos <strong><%= usuariosAtivos %> (<%= percentualAtivos %>%)</strong></li><li><span class="legenda-cor inativo"></span>Inativos <strong><%= usuariosInativos %> (<%= percentualInativos %>%)</strong></li></ul>
                </div>
              <% } %>
            </article>
            <article class="grafico">
              <header class="grafico-cabecalho"><h2>Contratos por plano</h2><a class="link-tabela" href="<%= contexto %>/contratos">Listagem →</a></header>
              <p class="descricao">Quantidade de contratos em cada plano</p>
              <% if (contratos == null || contratos.isEmpty()) { %><p class="vazio-painel">Nenhum dado encontrado.</p><% } else { %>
                <div class="colunas"><% for (ContratosPorPlanoDTO item : contratos) { int altura = maxContratos == 0 ? 0 : Math.max(3, Math.round(item.getContratos() * 100f / maxContratos)); %>
                  <div class="coluna"><strong><%= item.getContratos() %></strong><div class="coluna-barra" style="height:<%= altura %>%" aria-hidden="true"></div><span class="coluna-nome"><%= escaparHtml(item.getPlano()) %></span></div>
                <% } %></div>
              <% } %>
            </article>
            <article class="grafico">
              <header class="grafico-cabecalho"><h2>Endereços por instituição</h2><a class="link-tabela" href="<%= contexto %>/enderecos">Listagem →</a></header>
              <p class="descricao">Instituições com endereços cadastrados</p>
              <% if (enderecos == null || enderecos.isEmpty()) { %><p class="vazio-painel">Nenhum dado encontrado.</p><% } else { %>
                <ol class="ranking"><% int posicao = 0; for (EnderecosPorInstituicaoDTO item : enderecos) { posicao++; %>
                  <li><span class="ranking-posicao"><%= posicao %></span><span class="ranking-nome"><%= escaparHtml(item.getInstituicao()) %></span><strong><%= item.getEnderecos() %></strong></li>
                <% } %></ol>
              <% } %>
            </article>
          </div>
        </section>
        <% } %>
      </main>
    </div>
  </body>
</html>
