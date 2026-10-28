<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.DTO.InstituicaoLogadaDTO" %>
<%
    String erro = (String) request.getAttribute("erro");
    boolean confirmar = "confirmar".equals(request.getAttribute("etapa"));
    InstituicaoLogadaDTO instituicao = (InstituicaoLogadaDTO) session.getAttribute("instituicaoLogada");
    String razaoSocial = (String) request.getAttribute("razaoSocial");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adicionar Endereço</title>
</head>
<body>
    <main>
        <h1>Adicionar Endereço</h1>
        <p>Instituição: <strong><%= instituicao.getNome() %></strong></p>

        <% if (erro != null) { %>
        <p style="color: red;"><%= erro %></p>
        <% } %>

        <% if (!confirmar) { %>
        <!-- passo 1: buscar o cnpj -->
        <h2>Passo 1: Identificar endereço</h2>

        <form method="post" action="${pageContext.request.contextPath}/login-endereco?action=buscarCnpj">
            <div>
                <label for="cnpj">CNPJ da filial/matriz</label>
                <input type="text" id="cnpj" name="cnpj" value="${cnpj}" maxlength="18" placeholder="00.000.000/0001-00" required autofocus>
            </div>
            <button type="submit">Buscar endereço</button>
        </form>
        <% } else { %>
        <!-- passo 2: confirmar ou editar o endereco -->
        <h2>Passo 2: Confirmar endereço</h2>

        <% if (razaoSocial != null && !razaoSocial.isBlank()) { %>
        <p><strong>Razão social:</strong> <%= razaoSocial %></p>
        <% } %>

        <form method="post" action="${pageContext.request.contextPath}/login-endereco?action=finalizar">
            <input type="hidden" name="cnpj" value="${cnpj}">

            <div>
                <label for="nomeEndereco">Nome do endereço</label>
                <input type="text" id="nomeEndereco" name="nomeEndereco" value="${nomeEndereco}" maxlength="50" placeholder="Ex.: Matriz, Filial Centro" required autofocus>
            </div>
            <div>
                <label for="rua">Rua</label>
                <input type="text" id="rua" name="rua" value="${rua}" maxlength="40" required>
            </div>
            <div>
                <label for="numero">Número</label>
                <input type="text" id="numero" name="numero" value="${numero}" maxlength="10" required>
            </div>
            <div>
                <label for="complemento">Complemento</label>
                <input type="text" id="complemento" name="complemento" value="${complemento}" maxlength="50">
            </div>
            <div>
                <label for="bairro">Bairro</label>
                <input type="text" id="bairro" name="bairro" value="${bairro}" maxlength="40" required>
            </div>
            <div>
                <label for="cidade">Cidade</label>
                <input type="text" id="cidade" name="cidade" value="${cidade}" maxlength="40" required>
            </div>
            <div>
                <label for="estado">Estado (UF)</label>
                <input type="text" id="estado" name="estado" value="${estado}" maxlength="2" required>
            </div>
            <div>
                <label for="cep">CEP</label>
                <input type="text" id="cep" name="cep" value="${cep}" maxlength="9" required>
            </div>
            <button type="submit">Salvar endereço</button>
        </form>

        <a href="${pageContext.request.contextPath}/login-endereco?action=adicionar">Buscar outro CNPJ</a>
        <% } %>

        <br>
        <a href="${pageContext.request.contextPath}/login-endereco?action=logout">Sair</a>
    </main>
</body>
</html>
