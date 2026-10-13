<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
    <title>Fixa Backend</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; padding: 32px; font-family: Arial, sans-serif; color: #172033; background: #f3f6fb; }
        .topo { display: flex; align-items: center; justify-content: space-between; gap: 16px; flex-wrap: wrap; }
        .grade { display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 18px; margin: 20px 0 32px; }
        .card { padding: 18px; border-radius: 12px; background: white; box-shadow: 0 3px 12px #17203312; }
        .card h3 { margin: 0 0 14px; color: #2457d6; }
        .item { display: flex; justify-content: space-between; gap: 12px; padding: 10px 0; border-top: 1px solid #e7ebf2; }
        .valor { font-weight: 700; white-space: nowrap; }
        .vazio { color: #667085; }
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
    <p class="vazio"><%= erroIndicadores %></p>
<% } else if (!indicadoresCarregados) { %>
    <p class="vazio">Carregando indicadores...</p>
    <form id="carregar-indicadores" method="post" action="${pageContext.request.contextPath}/analiticos">
        <noscript><button type="submit">Carregar indicadores</button></noscript>
    </form>
    <script>document.getElementById('carregar-indicadores').submit();</script>
<% } else { %>
    <div class="grade">
        <section class="card">
            <h3>Resumo</h3>
            <div class="item"><span>Total de instituições</span><span class="valor"><%= resumo == null ? 0 : ((com.DTO.analiticos.ResumoDTO) resumo).getInstituicoes() %></span></div>
            <div class="item"><span>Total de usuários</span><span class="valor"><%= resumo == null ? 0 : ((com.DTO.analiticos.ResumoDTO) resumo).getUsuarios() %></span></div>
            <div class="item"><span>Total de endereços</span><span class="valor"><%= resumo == null ? 0 : ((com.DTO.analiticos.ResumoDTO) resumo).getEnderecos() %></span></div>
        </section>
        <section class="card">
            <h3>Instituições por tipo</h3>
            <% if (instituicoes != null && !instituicoes.isEmpty()) { for (Object objeto : instituicoes) { com.DTO.analiticos.InstituicoesPorTipoDTO item = (com.DTO.analiticos.InstituicoesPorTipoDTO) objeto; %>
                <div class="item"><span><%= item.getTipo() %></span><span class="valor"><%= item.getInstituicoes() %></span></div>
            <% } } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </section>
        <section class="card">
            <h3>Usuários por atividade</h3>
            <% if (usuarios != null && !usuarios.isEmpty()) { for (Object objeto : usuarios) { com.DTO.analiticos.UsuariosPorAtividadeDTO item = (com.DTO.analiticos.UsuariosPorAtividadeDTO) objeto; %>
                <div class="item"><span><%= item.getAtividade() %></span><span class="valor"><%= item.getUsuarios() %></span></div>
            <% } } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </section>
        <section class="card">
            <h3>Contratos por plano</h3>
            <% if (contratos != null && !contratos.isEmpty()) { for (Object objeto : contratos) { com.DTO.analiticos.ContratosPorPlanoDTO item = (com.DTO.analiticos.ContratosPorPlanoDTO) objeto; %>
                <div class="item"><span><%= item.getPlano() %></span><span class="valor"><%= item.getContratos() %></span></div>
            <% } } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </section>
        <section class="card">
            <h3>Endereços por instituição</h3>
            <% if (enderecos != null && !enderecos.isEmpty()) { for (Object objeto : enderecos) { com.DTO.analiticos.EnderecosPorInstituicaoDTO item = (com.DTO.analiticos.EnderecosPorInstituicaoDTO) objeto; %>
                <div class="item"><span><%= item.getInstituicao() %></span><span class="valor"><%= item.getEnderecos() %></span></div>
            <% } } else { %><p class="vazio">Nenhum dado encontrado.</p><% } %>
        </section>
    </div>
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
