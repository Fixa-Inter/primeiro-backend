<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.DTO.analiticos.*" %>

<%!
    // Escapa texto vindo do banco antes de colocar no HTML
    private static String esc(Object o) {
        if (o == null) return "";
        return o.toString().replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }
%>

<%
    Object resumo = request.getAttribute("resumo");
    java.util.List instituicoes = (java.util.List) request.getAttribute("instituicoes");
    java.util.List usuarios = (java.util.List) request.getAttribute("usuarios");
    java.util.List contratos = (java.util.List) request.getAttribute("contratos");
    java.util.List enderecos = (java.util.List) request.getAttribute("enderecos");
    String erroIndicadores = (String) request.getAttribute("erroIndicadores");
    boolean indicadoresCarregados = resumo != null || instituicoes != null || usuarios != null || contratos != null || enderecos != null;
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fixa Backend</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; padding: 32px; font-family: Inter, Arial, sans-serif; color: #0e1240; background: #f8f8ff; }
        .topo { display: flex; align-items: center; justify-content: space-between; gap: 16px; flex-wrap: wrap; }
        .vazio { color: #667085; }

        /* ---------- dashboard ---------- */
        .dash {
            --azul: #173f7c;
            --azul-claro: #d9deee;
            --trilho: #e9edf6;
            --texto: #0e1240;
            --muted: #6b7280;
            --borda: #e5e8f2;
            display: grid;
            gap: 20px;
            margin: 20px 0 32px;
        }
        .card { background: #fff; border: 1px solid var(--borda); border-radius: 16px; padding: 24px 28px; }
        .card h3 { margin: 0; font-size: 20px; color: var(--texto); }
        .muted { color: var(--muted); margin: 6px 0 24px; font-size: 15px; }
        .link { color: var(--azul); font-weight: 600; text-decoration: none; font-size: 15px; }
        .link:hover { text-decoration: underline; }
        .card-head { display: flex; justify-content: space-between; align-items: center; }

        .dash-cards { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; }
        .stat { display: flex; flex-direction: column; gap: 6px; }
        .stat-label { color: var(--muted); font-size: 17px; }
        .stat-value { font-size: 40px; line-height: 1.1; }
        .stat-sub { color: var(--muted); font-size: 15px; margin-bottom: 14px; }
        .dash-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 20px; }

        .hbar-row { display: grid; grid-template-columns: 165px 1fr 70px; align-items: center; margin-bottom: 18px; }
        .hbar-label { font-size: 17px; }
        .hbar-track { height: 14px; background: var(--trilho); border-radius: 999px; overflow: hidden; }
        .hbar-fill { height: 100%; background: var(--azul); border-radius: 999px; min-width: 6px; }
        .hbar-value { text-align: right; color: var(--muted); font-size: 15px; }

        .donut-wrap { display: flex; align-items: center; gap: 36px; flex-wrap: wrap; }
        .donut {
            --p: 0;
            width: 190px; height: 190px; border-radius: 50%;
            background: conic-gradient(var(--azul) calc(var(--p) * 1%), var(--azul-claro) 0);
            display: grid; place-items: center; flex-shrink: 0;
        }
        .donut-center { width: 130px; height: 130px; border-radius: 50%; background: #fff; display: flex; flex-direction: column; align-items: center; justify-content: center; }
        .donut-center strong { font-size: 32px; }
        .donut-center span { color: var(--muted); font-size: 15px; }
        .legend { list-style: none; margin: 0; padding: 0; display: grid; gap: 14px; font-size: 17px; }
        .legend b { margin-left: 8px; }
        .dot { display: inline-block; width: 14px; height: 14px; border-radius: 4px; margin-right: 8px; }
        .dot-on { background: var(--azul); }
        .dot-off { background: var(--azul-claro); }
        .alert { margin-top: 24px; padding: 14px 16px; border-radius: 10px; background: #fff7dd; border: 1px solid #f3e1a0; color: #6b4e00; font-size: 15px; }

        .cols { display: flex; justify-content: space-around; align-items: flex-end; height: 220px; }
        .col { display: flex; flex-direction: column; align-items: center; justify-content: flex-end; height: 100%; width: 70px; }
        .col-value { font-weight: 700; margin-bottom: 8px; }
        .col-bar { width: 100%; background: var(--azul); border-radius: 6px 6px 0 0; min-height: 4px; }
        .col-label { margin-top: 12px; color: var(--muted); font-size: 15px; text-align: center; }

        .rank { list-style: none; margin: 0; padding: 0; }
        .rank li { display: flex; align-items: center; gap: 16px; padding: 16px 0; border-bottom: 1px solid var(--borda); font-size: 17px; }
        .rank li:last-child { border-bottom: 0; }
        .rank-pos { width: 32px; height: 32px; border-radius: 8px; background: #eef1fa; color: var(--azul); display: grid; place-items: center; font-weight: 700; font-size: 14px; }
        .rank-name { flex: 1; }

        @media (max-width: 1000px) {
            .dash-cards { grid-template-columns: repeat(2, 1fr); }
            .dash-grid { grid-template-columns: 1fr; }
        }
        @media (max-width: 560px) {
            body { padding: 16px; }
            .dash-cards { grid-template-columns: 1fr; }
            .hbar-row { grid-template-columns: 120px 1fr 62px; }
        }
    </style>
</head>

<body>

<div class="topo">
    <h1>Fixa Backend</h1>
    <form method="post" action="${pageContext.request.contextPath}/login-handler">
        <input type="hidden" name="action" value="logout">
        <button type="submit">Sair</button>
    </form>
</div>

<h2>Indicadores</h2>
<% if (erroIndicadores != null) { %>
<div class="alert" style="margin-top:0"><%= esc(erroIndicadores) %></div>
<% } else if (!indicadoresCarregados) { %>
<p class="vazio">Carregando indicadores...</p>
<form id="carregar-indicadores" method="post" action="${pageContext.request.contextPath}/analiticos">
    <noscript><button type="submit">Carregar indicadores</button></noscript>
</form>
<script>document.getElementById('carregar-indicadores').submit();</script>
<% } else {
    // ===== valores calculados a partir dos DTOs =====
    ResumoDTO r = (ResumoDTO) resumo;
    int totInstituicoes = r == null ? 0 : r.getInstituicoes();
    int totUsuarios     = r == null ? 0 : r.getUsuarios();
    int totEnderecos    = r == null ? 0 : r.getEnderecos();

    int tiposCadastrados = instituicoes == null ? 0 : instituicoes.size();

    // usuários: atividade que começa com "ativ" (Ativo, ATIVO, Ativos...) conta como ativo
    int ativos = 0, inativos = 0;
    if (usuarios != null) {
        for (Object objeto : usuarios) {
            UsuariosPorAtividadeDTO u = (UsuariosPorAtividadeDTO) objeto;
            String atv = u.getAtividade() == null ? "" : u.getAtividade().trim().toLowerCase();
            if (atv.startsWith("ativ")) ativos += u.getUsuarios(); else inativos += u.getUsuarios();
        }
    }
    int totalUsuariosLista = ativos + inativos;
    double percAtivos = totalUsuariosLista > 0 ? ativos * 100.0 / totalUsuariosLista : 0;
    int percAtivosInt = (int) Math.round(percAtivos);
    int percInativosInt = totalUsuariosLista > 0 ? 100 - percAtivosInt : 0;
    String percAtivosCss = String.format(Locale.US, "%.1f", percAtivos);

    // contratos: soma da lista e maior valor (altura das colunas)
    int totContratos = 0, maxContratos = 1;
    if (contratos != null) {
        for (Object objeto : contratos) {
            ContratosPorPlanoDTO c = (ContratosPorPlanoDTO) objeto;
            totContratos += c.getContratos();
            if (c.getContratos() > maxContratos) maxContratos = c.getContratos();
        }
    }
    int planosListados = contratos == null ? 0 : contratos.size();

    int mediaEnderecos = totInstituicoes > 0 ? (int) Math.round((double) totEnderecos / totInstituicoes) : 0;
    String ctx = request.getContextPath();
%>
<section class="dash">

    <!-- ============ CARDS DE TOTAIS (ResumoDTO) ============ -->
    <div class="dash-cards">
        <div class="card stat">
            <span class="stat-label">Instituições</span>
            <strong class="stat-value"><%= totInstituicoes %></strong>
            <span class="stat-sub"><%= tiposCadastrados %> tipos cadastrados</span>
            <a class="link" href="<%= ctx %>/instituicoes">Ver tabela →</a>
        </div>
        <div class="card stat">
            <span class="stat-label">Usuários</span>
            <strong class="stat-value"><%= totUsuarios %></strong>
            <span class="stat-sub"><%= ativos %> ativos (<%= percAtivosInt %>%)</span>
            <a class="link" href="<%= ctx %>/usuarios">Ver tabela →</a>
        </div>
        <div class="card stat">
            <span class="stat-label">Endereços</span>
            <strong class="stat-value"><%= totEnderecos %></strong>
            <span class="stat-sub"><%= mediaEnderecos %> por instituição, em média</span>
            <a class="link" href="<%= ctx %>/enderecos">Ver tabela →</a>
        </div>
        <div class="card stat">
            <span class="stat-label">Contratos</span>
            <strong class="stat-value"><%= totContratos %></strong>
            <span class="stat-sub">Em <%= planosListados %> planos</span>
            <a class="link" href="<%= ctx %>/contratos">Ver tabela →</a>
        </div>
    </div>

    <div class="dash-grid">

        <!-- ============ INSTITUIÇÕES POR TIPO ============ -->
        <div class="card">
            <header class="card-head">
                <h3>Instituições por tipo</h3>
                <a class="link" href="<%= ctx %>/instituicoes">Ver tabela →</a>
            </header>
            <p class="muted">Quantas instituições existem de cada tipo</p>
            <% if (instituicoes != null && !instituicoes.isEmpty()) {
                for (Object objeto : instituicoes) {
                    InstituicoesPorTipoDTO item = (InstituicoesPorTipoDTO) objeto; %>
            <div class="hbar-row">
                <span class="hbar-label"><%= esc(item.getTipo()) %></span>
                <div class="hbar-track"><div class="hbar-fill" style="width:<%= item.getPercentual() %>%"></div></div>
                <span class="hbar-value"><%= item.getInstituicoes() %> · <%= item.getPercentual() %>%</span>
            </div>
            <% } } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </div>

        <!-- ============ USUÁRIOS ATIVOS x INATIVOS ============ -->
        <div class="card">
            <header class="card-head">
                <h3>Usuários ativos x inativos</h3>
                <a class="link" href="<%= ctx %>/usuarios">Ver tabela →</a>
            </header>
            <p class="muted">Situação das contas de usuário</p>
            <% if (totalUsuariosLista > 0) { %>
            <div class="donut-wrap">
                <div class="donut" style="--p:<%= percAtivosCss %>">
                    <div class="donut-center">
                        <strong><%= percAtivosInt %>%</strong>
                        <span>ativos</span>
                    </div>
                </div>
                <ul class="legend">
                    <li><i class="dot dot-on"></i> Ativos <b><%= ativos %> (<%= percAtivosInt %>%)</b></li>
                    <li><i class="dot dot-off"></i> Inativos <b><%= inativos %> (<%= percInativosInt %>%)</b></li>
                </ul>
            </div>
            <% if (inativos > 0) { %>
            <div class="alert"><%= inativos %> usuários inativos: estão há muito tempo sem usar o sistema. Vale revisar se ainda precisam de acesso.</div>
            <% } %>
            <% } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </div>

        <!-- ============ CONTRATOS POR PLANO ============ -->
        <div class="card">
            <header class="card-head">
                <h3>Contratos por plano</h3>
                <a class="link" href="<%= ctx %>/contratos">Ver tabela →</a>
            </header>
            <p class="muted">Contratos ativos em cada plano</p>
            <% if (contratos != null && !contratos.isEmpty()) { %>
            <div class="cols">
                <% for (Object objeto : contratos) {
                    ContratosPorPlanoDTO item = (ContratosPorPlanoDTO) objeto;
                    int altura = Math.round(item.getContratos() * 100f / maxContratos); %>
                <div class="col">
                    <span class="col-value"><%= item.getContratos() %></span>
                    <div class="col-bar" style="height:<%= altura %>%"></div>
                    <span class="col-label"><%= esc(item.getPlano()) %></span>
                </div>
                <% } %>
            </div>
            <% } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </div>

        <!-- ============ ENDEREÇOS POR INSTITUIÇÃO ============ -->
        <div class="card">
            <header class="card-head">
                <h3>Endereços por instituição</h3>
                <a class="link" href="<%= ctx %>/enderecos">Ver tabela →</a>
            </header>
            <p class="muted">Top <%= enderecos == null ? 0 : enderecos.size() %> de <%= totInstituicoes %> instituições cadastradas</p>
            <% if (enderecos != null && !enderecos.isEmpty()) { %>
            <ol class="rank">
                <% int pos = 0;
                    for (Object objeto : enderecos) {
                        EnderecosPorInstituicaoDTO item = (EnderecosPorInstituicaoDTO) objeto;
                        pos++; %>
                <li>
                    <span class="rank-pos"><%= pos %></span>
                    <span class="rank-name"><%= esc(item.getInstituicao()) %></span>
                    <b><%= item.getEnderecos() %></b>
                </li>
                <% } %>
            </ol>
            <% } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </div>

    </div>
</section>
<% } %>

<h2>CRUDs</h2>

<ul>

    <li>
        <a href="${pageContext.request.contextPath}/instituicoes">
            Instituições
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/planos">
            Planos
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/superAdmin">
            Super Administradores
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/enderecos">
            Endereços
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/contratos">
            Contratos
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/usuarios">
            Usuários
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/pagamentos">
            Pagamentos
        </a>
    </li>

    <li>
        <a href="${pageContext.request.contextPath}/fotoUsuario">
            Fotos dos Usuários
        </a>
    </li>

</ul>

</body>

</html>
