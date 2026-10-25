<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adicionar Endereço - Fixa</title>
</head>
<body>
    <h1>Adicionar Endereço</h1>

    <c:if test="${not empty erro}">
        <p style="color: red;">${erro}</p>
    </c:if>

    <!-- ===== ETAPA 1: Buscar CNPJ ===== -->
    <c:if test="${empty rua}">
        <h2>Passo 1: Identificar Endereço</h2>

        <form method="POST" action="/cadastro?action=buscarCnpjFluxo2">
            <div>
                <label for="cnpj">CNPJ da filial/matriz</label>
                <input type="text" id="cnpj" name="cnpj" required placeholder="00.000.000/0001-00" autofocus>
            </div>

            <button type="submit">Buscar Endereço</button>
        </form>

        <p><a href="/">Cancelar</a></p>
    </c:if>

    <!-- ===== ETAPA 2: Confirmar/Editar Endereço ===== -->
    <c:if test="${not empty rua}">
        <h2>Passo 2: Confirmar Endereço</h2>

        <form method="POST" action="/cadastro?action=finalizarEndereco">
            <div>
                <label for="rua">Rua/Logradouro</label>
                <input type="text" id="rua" name="rua" value="${rua}" required>
            </div>

            <div>
                <label for="numero">Número</label>
                <input type="text" id="numero" name="numero" value="${numero}" required>
            </div>

            <div>
                <label for="bairro">Bairro</label>
                <input type="text" id="bairro" name="bairro" value="${bairro}" required>
            </div>

            <div>
                <label for="complemento">Complemento (opcional)</label>
                <input type="text" id="complemento" name="complemento" value="${complemento}">
            </div>

            <div>
                <label for="cidade">Cidade</label>
                <input type="text" id="cidade" name="cidade" value="${cidade}" required>
            </div>

            <div>
                <label for="estado">Estado (UF)</label>
                <input type="text" id="estado" name="estado" value="${estado}" required maxlength="2">
            </div>

            <div>
                <label for="cep">CEP</label>
                <input type="text" id="cep" name="cep" value="${cep}" required>
            </div>

            <input type="hidden" name="cnpj" value="${cnpj}">

            <button type="submit">Salvar Endereço</button>
        </form>

        <p><a href="/login-handler?action=logout">Sair</a></p>
    </c:if>
</body>
</html>