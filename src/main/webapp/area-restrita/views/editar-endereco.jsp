<%@ page import="com.model.Endereco" %>

<%
    Endereco endereco =
            (Endereco) request.getAttribute("endereco");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Editar Endereço</title>
</head>

<body>

<h1>Editar Endereço</h1>

<% String erro = (String) request.getAttribute("erro"); %>
<form method="post"
      action="<%= request.getContextPath() %>
            <% if (erro != null && !erro.isBlank()) { %>
            <p>
                <%= erro %>
            </p>
            <% } %>
/enderecos">

    <input type="hidden"
           name="action"
           value="update">


    <!-- ID -->

    <input type="hidden"
           name="id"
           value="<%= endereco.getId() %>">


    <!-- DATA DE CRIAÇÃO -->

    <input type="hidden"
           name="dataCriacao"
           value="<%= endereco.getDataCriacao() %>">


    <!-- RUA -->

    <label for="rua">
        Rua:
    </label>

    <input type="text"
           id="rua"
           name="rua"
           value="<%= endereco.getRua() %>"
           required>

    <br><br>


    <!-- BAIRRO -->

    <label for="bairro">
        Bairro:
    </label>

    <input type="text"
           id="bairro"
           name="bairro"
           value="<%= endereco.getBairro() %>"
           required>

    <br><br>


    <!-- COMPLEMENTO -->

    <label for="complemento">
        Complemento:
    </label>

    <input type="text"
           id="complemento"
           name="complemento"
           value="<%= endereco.getComplemento() != null
                    ? endereco.getComplemento()
                    : "" %>">

    <br><br>


    <!-- CIDADE -->

    <label for="cidade">
        Cidade:
    </label>

    <input type="text"
           id="cidade"
           name="cidade"
           value="<%= endereco.getCidade() %>"
           required>

    <br><br>


    <!-- ESTADO -->

    <label for="estado">
        Estado:
    </label>

    <input type="text"
           id="estado"
           name="estado"
           maxlength="2"
           value="<%= endereco.getEstado() %>"
           required>

    <br><br>


    <!-- NÚMERO -->

    <label for="numero">
        Número:
    </label>

    <input type="text"
           id="numero"
           name="numero"
           value="<%= endereco.getNumero() %>"
           required>

    <br><br>


    <!-- CEP -->

    <label for="cep">
        CEP:
    </label>

    <input type="text"
           id="cep"
           name="cep"
           value="<%= endereco.getCep() %>"
           required>

    <br><br>


    <!-- CNPJ -->

    <label for="cnpj">
        CNPJ:
    </label>

    <input type="text"
           id="cnpj"
           name="cnpj"
           value="<%= endereco.getCnpj() %>"
           required>

    <br><br>


    <!-- INSTITUIÇÃO -->

    <label for="fkInstituicao">
        ID da Instituição:
    </label>

    <input type="number"
           id="fkInstituicao"
           name="fkInstituicao"
           value="<%= endereco.getFkInstituicao() %>"
           required>

    <br><br>


    <button type="submit">
        Salvar alterações
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/enderecos">
    Voltar para endereços
</a>

</body>
</html>