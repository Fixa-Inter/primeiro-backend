<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Cadastrar Endereço</title>
</head>

<body>

<h1>Cadastrar Endereço</h1>

<form method="post"
      action="<%= request.getContextPath() %>/enderecos">

    <input type="hidden"
           name="action"
           value="create">


    <!-- RUA -->

    <label for="rua">
        Rua:
    </label>

    <input type="text"
           id="rua"
           name="rua"
           required>

    <br><br>


    <!-- BAIRRO -->

    <label for="bairro">
        Bairro:
    </label>

    <input type="text"
           id="bairro"
           name="bairro"
           required>

    <br><br>


    <!-- COMPLEMENTO -->

    <label for="complemento">
        Complemento:
    </label>

    <input type="text"
           id="complemento"
           name="complemento">

    <br><br>


    <!-- CIDADE -->

    <label for="cidade">
        Cidade:
    </label>

    <input type="text"
           id="cidade"
           name="cidade"
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
           required>

    <br><br>


    <!-- NÚMERO -->

    <label for="numero">
        Número:
    </label>

    <input type="text"
           id="numero"
           name="numero"
           required>

    <br><br>


    <!-- CEP -->

    <label for="cep">
        CEP:
    </label>

    <input type="text"
           id="cep"
           name="cep"
           required>

    <br><br>


    <!-- CNPJ -->

    <label for="cnpj">
        CNPJ:
    </label>

    <input type="text"
           id="cnpj"
           name="cnpj"
           required>

    <br><br>


    <!-- INSTITUIÇÃO -->

    <label for="fkInstituicao">
        ID da Instituição:
    </label>

    <input type="number"
           id="fkInstituicao"
           name="fkInstituicao"
           required>

    <br><br>


    <button type="submit">
        Cadastrar
    </button>

</form>

<br>

<a href="<%= request.getContextPath() %>/enderecos">
    Voltar para endereços
</a>

</body>
</html>