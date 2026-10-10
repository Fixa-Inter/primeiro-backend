<%@ page import="java.util.List" %>
<%@ page import="com.model.Endereco" %>
<%@ page import="com.model.Filtro" %>
<%@ page import="com.model.enums.OperacaoFiltro" %>

<%
    List<Endereco> enderecos =
            (List<Endereco>) request.getAttribute("enderecos");

    List<Filtro> filtros =
            (List<Filtro>) request.getAttribute("filtros");

    String ordenacaoAtual =
            request.getParameter("ordenacao");
%>

<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <title>Endereços</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 30px;
        }

        h1 {
            margin-bottom: 20px;
        }

        .filtros-ativos {
            margin-bottom: 20px;
        }

        .filtro {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 8px 12px;
            margin: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .filtro form {
            display: inline;
            margin: 0;
        }

        .filtro button {
            border: none;
            background: none;
            cursor: pointer;
            font-weight: bold;
        }

        .form-filtro {
            margin-bottom: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #eee;
        }

        form {
            margin-bottom: 10px;
        }

        select,
        input {
            padding: 6px;
            margin-right: 5px;
        }

        button {
            padding: 6px 12px;
            cursor: pointer;
        }

        .acoes {
            white-space: nowrap;
        }
    </style>
</head>

<body>

<h1>Endereços</h1>

<a href="<%= request.getContextPath() %>/enderecos?action=create">
    Cadastrar endereço
</a>

<form method="get"
      action="<%= request.getContextPath() %>/enderecos">

    <input type="hidden" name="action" value="read">
    <input type="search"
           name="pesquisa"
           value="${param.pesquisa}"
           placeholder="Pesquisar...">

    <%
        if (filtros != null) {
            for (Filtro filtro : filtros) {
    %>
    <input type="hidden" name="campoFiltro" value="<%= filtro.getCampoFiltravel() %>">
    <input type="hidden" name="operacaoFiltro" value="<%= filtro.getOperacaoFiltro().name() %>">
    <input type="hidden" name="valorFiltro" value="<%= filtro.getValor() %>">
    <%
            }
        }
    %>

    <% if (ordenacaoAtual != null && !ordenacaoAtual.isBlank()) { %>
    <input type="hidden" name="ordenacao" value="<%= ordenacaoAtual %>">
    <% } %>

    <button type="submit">Pesquisar</button>
</form>

<hr>

<!-- ========================= -->
<!-- FILTROS ATIVOS -->
<!-- ========================= -->

<div class="filtros-ativos">

    <h3>Filtros ativos</h3>

    <%
        if (filtros != null && !filtros.isEmpty()) {

            for (int i = 0; i < filtros.size(); i++) {

                Filtro filtro = filtros.get(i);

                String valorExibicao =
                        String.valueOf(filtro.getValor());
    %>

    <div class="filtro">

            <span>
                <strong>
                    <%= filtro.getCampoFiltravel() %>
                </strong>

                <%= filtro.getOperacaoFiltro().getNome() %>

                <%= valorExibicao %>
            </span>

        <!-- REMOVER FILTRO -->
        <form method="get"
              action="<%= request.getContextPath() %>/enderecos">

            <input type="hidden"
                   name="action"
                   value="read">

            <input type="hidden"
                   name="removerFiltro"
                   value="<%= i %>">

            <%
                for (Filtro filtroAtual : filtros) {
            %>

            <input type="hidden"
                   name="campoFiltro"
                   value="<%= filtroAtual.getCampoFiltravel() %>">

            <input type="hidden"
                   name="operacaoFiltro"
                   value="<%= filtroAtual.getOperacaoFiltro().name() %>">

            <input type="hidden"
                   name="valorFiltro"
                   value="<%= filtroAtual.getValor() %>">

            <%
                }
            %>

            <%
                if (ordenacaoAtual != null &&
                        !ordenacaoAtual.isBlank()) {
            %>

            <input type="hidden"
                   name="ordenacao"
                   value="<%= ordenacaoAtual %>">

            <%
                }
            %>

            <button type="submit">
                X
            </button>

        </form>

    </div>

    <%
        }

    } else {
    %>

    <p>Nenhum filtro aplicado.</p>

    <%
        }
    %>

</div>


<!-- ========================= -->
<!-- ADICIONAR FILTRO -->
<!-- ========================= -->

<h3>Adicionar filtro</h3>

<form method="get"
      action="<%= request.getContextPath() %>/enderecos"
      class="form-filtro">

    <input type="hidden"
           name="action"
           value="read">

    <!--
        Mantém os filtros que já existem.
        Assim, ao adicionar um novo filtro,
        o anterior não é perdido.
    -->

    <%
        if (filtros != null) {

            for (Filtro filtro : filtros) {
    %>

    <input type="hidden"
           name="campoFiltro"
           value="<%= filtro.getCampoFiltravel() %>">

    <input type="hidden"
           name="operacaoFiltro"
           value="<%= filtro.getOperacaoFiltro().name() %>">

    <input type="hidden"
           name="valorFiltro"
           value="<%= filtro.getValor() %>">

    <%
            }
        }
    %>


    <!-- CAMPO -->

    <select name="campoFiltro"
            id="campoFiltro"
            onchange="atualizarCampo()">

        <option value="ID">ID</option>
        <option value="RUA">Rua</option>
        <option value="BAIRRO">Bairro</option>
        <option value="CIDADE">Cidade</option>
        <option value="ESTADO">Estado</option>
        <option value="CEP">CEP</option>
        <option value="CNPJ">CNPJ</option>
        <option value="FK_INSTITUICAO_ID">
            Instituição
        </option>

    </select>


    <!-- OPERAÇÃO -->

    <select name="operacaoFiltro"
            id="operacaoFiltro">

        <option value="IGUAL">
            Igual
        </option>

        <option value="CONTEM">
            Contém
        </option>

    </select>


    <!-- VALOR -->

    <input type="text"
           name="valorFiltro"
           id="valorFiltro"
           placeholder="Valor"
           required>


    <button type="submit">
        Adicionar filtro
    </button>

</form>


<!-- ========================= -->
<!-- ORDENAÇÃO -->
<!-- ========================= -->

<h3>Ordenação</h3>

<form method="get"
      action="<%= request.getContextPath() %>/enderecos">

    <input type="hidden"
           name="action"
           value="read">

    <!-- Mantém os filtros -->

    <%
        if (filtros != null) {

            for (Filtro filtro : filtros) {
    %>

    <input type="hidden"
           name="campoFiltro"
           value="<%= filtro.getCampoFiltravel() %>">

    <input type="hidden"
           name="operacaoFiltro"
           value="<%= filtro.getOperacaoFiltro().name() %>">

    <input type="hidden"
           name="valorFiltro"
           value="<%= filtro.getValor() %>">

    <%
            }
        }
    %>


    <select name="ordenacao">

        <option value="">
            Selecionar ordenação
        </option>

        <option value="ID-ASC"
                <%= "ID-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            ID crescente
        </option>

        <option value="ID-DESC"
                <%= "ID-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            ID decrescente
        </option>

        <option value="RUA-ASC"
                <%= "RUA-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Rua A-Z
        </option>

        <option value="RUA-DESC"
                <%= "RUA-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Rua Z-A
        </option>

        <option value="BAIRRO-ASC"
                <%= "BAIRRO-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Bairro A-Z
        </option>

        <option value="BAIRRO-DESC"
                <%= "BAIRRO-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Bairro Z-A
        </option>

        <option value="CIDADE-ASC"
                <%= "CIDADE-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Cidade A-Z
        </option>

        <option value="CIDADE-DESC"
                <%= "CIDADE-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Cidade Z-A
        </option>

        <option value="ESTADO-ASC"
                <%= "ESTADO-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Estado A-Z
        </option>

        <option value="ESTADO-DESC"
                <%= "ESTADO-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Estado Z-A
        </option>

        <option value="CEP-ASC"
                <%= "CEP-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            CEP crescente
        </option>

        <option value="CEP-DESC"
                <%= "CEP-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            CEP decrescente
        </option>

        <option value="CNPJ-ASC"
                <%= "CNPJ-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            CNPJ crescente
        </option>

        <option value="CNPJ-DESC"
                <%= "CNPJ-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            CNPJ decrescente
        </option>

        <option value="FK_INSTITUICAO_ID-ASC"
                <%= "FK_INSTITUICAO_ID-ASC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Instituição crescente
        </option>

        <option value="FK_INSTITUICAO_ID-DESC"
                <%= "FK_INSTITUICAO_ID-DESC".equals(ordenacaoAtual)
                        ? "selected" : "" %>>
            Instituição decrescente
        </option>

    </select>

    <button type="submit">
        Ordenar
    </button>

</form>


<!-- ========================= -->
<!-- TABELA -->
<!-- ========================= -->

<table>

    <thead>

    <tr>
        <th>ID</th>
        <th>Rua</th>
        <th>Bairro</th>
        <th>Complemento</th>
        <th>Cidade</th>
        <th>Estado</th>
        <th>Número</th>
        <th>CEP</th>
        <th>CNPJ</th>
        <th>Data de criação</th>
        <th>Instituição</th>
        <th>Ações</th>
    </tr>

    </thead>

    <tbody>

    <%
        if (enderecos != null && !enderecos.isEmpty()) {

            for (Endereco endereco : enderecos) {
    %>

    <tr>

        <td>
            <%= endereco.getId() %>
        </td>

        <td>
            <%= endereco.getRua() %>
        </td>

        <td>
            <%= endereco.getBairro() %>
        </td>

        <td>
            <%= endereco.getComplemento() %>
        </td>

        <td>
            <%= endereco.getCidade() %>
        </td>

        <td>
            <%= endereco.getEstado() %>
        </td>

        <td>
            <%= endereco.getNumero() %>
        </td>

        <td>
            <%= endereco.getCep() %>
        </td>

        <td>
            <%= endereco.getCnpj() %>
        </td>

        <td>
            <%= endereco.getDataCriacao() %>
        </td>

        <td>
            <%= endereco.getFkInstituicao() %>
        </td>

        <td class="acoes">

            <!-- EDITAR -->

            <form method="get"
                  action="<%= request.getContextPath() %>/enderecos">

                <input type="hidden"
                       name="action"
                       value="update">

                <input type="hidden"
                       name="id"
                       value="<%= endereco.getId() %>">

                <button type="submit">
                    Editar
                </button>

            </form>


            <!-- EXCLUIR -->

            <form method="post"
                  action="<%= request.getContextPath() %>/enderecos">

                <input type="hidden"
                       name="action"
                       value="delete">

                <input type="hidden"
                       name="id"
                       value="<%= endereco.getId() %>">

                <button type="submit"
                        onclick="return confirm('Deseja realmente excluir este endereço?');">
                    Excluir
                </button>

            </form>

        </td>

    </tr>

    <%
        }

    } else {
    %>

    <tr>

        <td colspan="12">
            Nenhum endereço encontrado.
        </td>

    </tr>

    <%
        }
    %>

    </tbody>

</table>


<script>

    function atualizarCampo() {

        const campo =
            document.getElementById("campoFiltro").value;

        const operacao =
            document.getElementById("operacaoFiltro");

        /*
         * Limpa as opções atuais
         */
        operacao.innerHTML = "";


        /*
         * ID e FK_INSTITUICAO_ID
         * aceitam somente IGUAL.
         */

        if (
            campo === "ID" ||
            campo === "FK_INSTITUICAO_ID"
        ) {

            const option =
                document.createElement("option");

            option.value = "IGUAL";
            option.text = "Igual";

            operacao.appendChild(option);

        } else {

            /*
             * Campos de texto aceitam
             * IGUAL e CONTEM.
             */

            const igual =
                document.createElement("option");

            igual.value = "IGUAL";
            igual.text = "Igual";

            operacao.appendChild(igual);


            const contem =
                document.createElement("option");

            contem.value = "CONTEM";
            contem.text = "Contém";

            operacao.appendChild(contem);
        }
    }

</script>

</body>
</html>
