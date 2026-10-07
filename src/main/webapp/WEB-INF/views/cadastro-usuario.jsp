<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Cadastrar Usuário</title>
</head>

<body>

<h1>Cadastrar Usuário</h1>

<form action="${pageContext.request.contextPath}/usuarios"
      method="post">

    <input type="hidden"
           name="action"
           value="create">

    <label for="nome">
        Nome:
    </label>

    <input type="text"
           id="nome"
           name="nome"
           required>

    <br><br>

    <label for="senhaHash">
        Senha:
    </label>

    <input type="password"
           id="senhaHash"
           name="senhaHash"
           required>

    <br><br>

    <label for="email">
        Email:
    </label>

    <input type="email"
           id="email"
           name="email"
           required>

    <br><br>

    <label for="cargo">
        Cargo:
    </label>

    <input type="text"
           id="cargo"
           name="cargo"
           required>

    <br><br>

    <label for="tipoDeAcesso">
        Tipo de acesso:
    </label>

    <select id="tipoDeAcesso"
            name="tipoDeAcesso"
            required>

        <option value="">
            Selecione
        </option>

        <option value="ADMINISTRADOR">
            Administrador
        </option>

        <option value="GESTOR">
            Gestor
        </option>

        <option value="TECNICO">
            Técnico
        </option>

        <option value="SOLICITANTE">
            Solicitante
        </option>

    </select>

    <br><br>

    <label for="fkEndereco">
        ID do endereço:
    </label>

    <input type="number"
           id="fkEndereco"
           name="fkEndereco"
           required>

    <br><br>

    <label for="dataAniversario">
        Data de nascimento:
    </label>

    <input type="date"
           id="dataAniversario"
           name="dataAniversario"
           required>

    <br><br>

    <button type="submit">
        Cadastrar
    </button>

</form>

<br>

<a href="${pageContext.request.contextPath}/usuarios">
    Voltar
</a>

</body>
</html>