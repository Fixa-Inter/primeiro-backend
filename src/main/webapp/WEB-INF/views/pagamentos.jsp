<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>

<%@ page import="com.model.Pagamento" %>
<%@ page import="com.model.Filtro" %>

<%@ page import="com.model.enums.MetodoPagamento" %>
<%@ page import="com.model.enums.OperacaoFiltro" %>


<%
    List<Pagamento> pagamentos =
            (List<Pagamento>) request.getAttribute("pagamentos");

    List<Filtro> filtros =
            (List<Filtro>) request.getAttribute("filtros");

    String ordenacaoAtual =
            request.getParameter("ordenacao");
%>


<!DOCTYPE html>

<html lang="pt-BR">

<head>

    <meta charset="UTF-8">

    <title>Pagamentos</title>

</head>


<body>


<h1>Pagamentos</h1>


<a href="${pageContext.request.contextPath}/pagamentos?action=create">
    Cadastrar novo pagamento
</a>


<hr>


<!-- ===================================================== -->
<!-- FILTROS APLICADOS -->
<!-- ===================================================== -->

<form method="get" action="${pageContext.request.contextPath}/pagamentos">
    <input type="hidden" name="action" value="read">
    <input type="search" name="pesquisa" value="${param.pesquisa}" placeholder="Pesquisar...">
    <% if (filtros != null) for (Filtro filtro : filtros) { %>
    <input type="hidden" name="campoFiltro" value="<%= filtro.getCampoFiltravel() %>">
    <input type="hidden" name="valorFiltro" value="<%= filtro.getValor() %>">
    <input type="hidden" name="operacaoFiltro" value="<%= filtro.getOperacaoFiltro().name() %>">
    <% } %>
    <% if (ordenacaoAtual != null && !ordenacaoAtual.isBlank()) { %>
    <input type="hidden" name="ordenacao" value="<%= ordenacaoAtual %>">
    <% } %>
    <button type="submit">Pesquisar</button>
</form>

<h2>Filtros aplicados</h2>


<%
    if (filtros == null || filtros.isEmpty()) {
%>

<p>Nenhum filtro aplicado.</p>

<%
} else {

    for (int i = 0; i < filtros.size(); i++) {

        Filtro filtro = filtros.get(i);
%>


<div>

    <strong>
        <%= filtro.getCampoFiltravel() %>
    </strong>


    <%= filtro.getOperacaoFiltro().getNome() %>


    <%
        /*
         * Mostra o nome do método de pagamento
         * em vez do código.
         */
        if ("METODO_PAGAMENTO".equals(
                filtro.getCampoFiltravel())) {

            try {

                int codigo =
                        Integer.parseInt(
                                String.valueOf(
                                        filtro.getValor()
                                )
                        );

                out.print(
                        MetodoPagamento
                                .converterEnum(codigo)
                                .getNome()
                );

            } catch (Exception e) {

                out.print(filtro.getValor());

            }

        }

        /*
         * Mostra Sim/Não para boolean.
         */
        else if ("FOI_REALIZADO".equals(
                filtro.getCampoFiltravel())) {

            if (Boolean.TRUE.equals(filtro.getValor())) {

                out.print("Sim");

            } else {

                out.print("Não");

            }

        }

        /*
         * Demais campos.
         */
        else {

            out.print(filtro.getValor());

        }
    %>


    <!-- ================================================ -->
    <!-- REMOVER FILTRO -->
    <!-- ================================================ -->

    <form
            action="${pageContext.request.contextPath}/pagamentos"
            method="get"
            style="display:inline"
    >

        <input
                type="hidden"
                name="action"
                value="read"
        >


        <input
                type="hidden"
                name="removerFiltro"
                value="<%= i %>"
        >


        <%
            if (ordenacaoAtual != null
                    && !ordenacaoAtual.isBlank()) {
        %>

        <input
                type="hidden"
                name="ordenacao"
                value="<%= ordenacaoAtual %>"
        >

        <%
            }
        %>


        <!-- ============================================ -->
        <!-- REENVIA TODOS OS FILTROS -->
        <!-- ============================================ -->

        <%
            for (Filtro filtroAtual : filtros) {
        %>

        <input
                type="hidden"
                name="campoFiltro"
                value="<%= filtroAtual.getCampoFiltravel() %>"
        >


        <input
                type="hidden"
                name="operacaoFiltro"
                value="<%= filtroAtual.getOperacaoFiltro().name() %>"
        >


        <input
                type="hidden"
                name="valorFiltro"
                value="<%= filtroAtual.getValor() %>"
        >

        <%
            }
        %>


        <button type="submit">
            Excluir filtro
        </button>


    </form>


</div>


<%
        }
    }
%>


<br>


<a href="${pageContext.request.contextPath}/pagamentos?action=read">
    Limpar filtros
</a>


<hr>


<!-- ===================================================== -->
<!-- ADICIONAR FILTRO -->
<!-- ===================================================== -->

<h2>Adicionar filtro</h2>


<form
        action="${pageContext.request.contextPath}/pagamentos"
        method="get"
>


    <input
            type="hidden"
            name="action"
            value="read"
    >


    <%
        if (ordenacaoAtual != null
                && !ordenacaoAtual.isBlank()) {
    %>

    <input
            type="hidden"
            name="ordenacao"
            value="<%= ordenacaoAtual %>"
    >

    <%
        }
    %>


    <!-- ================================================ -->
    <!-- REENVIA OS FILTROS EXISTENTES -->
    <!-- ================================================ -->

    <%
        if (filtros != null) {

            for (Filtro filtro : filtros) {
    %>

    <input
            type="hidden"
            name="campoFiltro"
            value="<%= filtro.getCampoFiltravel() %>"
    >


    <input
            type="hidden"
            name="operacaoFiltro"
            value="<%= filtro.getOperacaoFiltro().name() %>"
    >


    <input
            type="hidden"
            name="valorFiltro"
            value="<%= filtro.getValor() %>"
    >

    <%
            }
        }
    %>


    <!-- ================================================ -->
    <!-- CAMPO -->
    <!-- ================================================ -->

    <label for="campoFiltro">
        Campo:
    </label>


    <select
            id="campoFiltro"
            name="campoFiltro"
            onchange="atualizarCampo()"
    >

        <option value="ID">
            ID
        </option>


        <option value="VALOR">
            Valor
        </option>


        <option value="DATA_PAGAMENTO">
            Data Pagamento
        </option>


        <option value="FOI_REALIZADO">
            Foi Realizado
        </option>


        <option value="FK_CONTRATO_ID">
            Contrato
        </option>


        <option value="METODO_PAGAMENTO">
            Método de Pagamento
        </option>

    </select>


    <!-- ================================================ -->
    <!-- OPERAÇÃO -->
    <!-- ================================================ -->

    <label for="operacaoFiltro">
        Operação:
    </label>


    <select
            id="operacaoFiltro"
            name="operacaoFiltro"
    >

        <option value="IGUAL">
            Igual
        </option>


        <option value="MAIOR_QUE">
            Maior que
        </option>


        <option value="MAIOR_OU_IGUAL">
            Maior ou igual
        </option>


        <option value="MENOR_QUE">
            Menor que
        </option>


        <option value="MENOR_OU_IGUAL">
            Menor ou igual
        </option>

    </select>


    <!-- ================================================ -->
    <!-- VALOR TEXTO -->
    <!-- ================================================ -->

    <label for="valorFiltroTexto">
        Valor:
    </label>


    <input
            type="text"
            id="valorFiltroTexto"
            name="valorFiltro"
            required
    >


    <!-- ================================================ -->
    <!-- VALOR BOOLEAN / ENUM -->
    <!-- ================================================ -->

    <select
            id="valorFiltroSelect"
            name="valorFiltro"
            style="display:none"
            disabled
    >

        <option value="Crédito">
            Crédito
        </option>


        <option value="Débito">
            Débito
        </option>


        <option value="Pix">
            Pix
        </option>

    </select>


    <!-- ================================================ -->
    <!-- VALOR BOOLEAN -->
    <!-- ================================================ -->

    <select
            id="valorFiltroBoolean"
            name="valorFiltro"
            style="display:none"
            disabled
    >

        <option value="true">
            Sim
        </option>


        <option value="false">
            Não
        </option>

    </select>


    <button type="submit">
        Adicionar filtro
    </button>


</form>


<hr>


<!-- ===================================================== -->
<!-- ORDENAÇÃO -->
<!-- ===================================================== -->

<h2>Ordenação</h2>


<form
        action="${pageContext.request.contextPath}/pagamentos"
        method="get"
>


    <input
            type="hidden"
            name="action"
            value="read"
    >


    <!-- ================================================ -->
    <!-- MANTÉM OS FILTROS -->
    <!-- ================================================ -->

    <%
        if (filtros != null) {

            for (Filtro filtro : filtros) {
    %>

    <input
            type="hidden"
            name="campoFiltro"
            value="<%= filtro.getCampoFiltravel() %>"
    >


    <input
            type="hidden"
            name="operacaoFiltro"
            value="<%= filtro.getOperacaoFiltro().name() %>"
    >


    <input
            type="hidden"
            name="valorFiltro"
            value="<%= filtro.getValor() %>"
    >

    <%
            }
        }
    %>


    <label for="ordenacao">
        Ordenar por:
    </label>


    <select
            id="ordenacao"
            name="ordenacao"
    >

        <option value="">
            Padrão
        </option>


        <option
                value="ID-ASC"
                <%= "ID-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            ID - Crescente
        </option>


        <option
                value="ID-DESC"
                <%= "ID-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            ID - Decrescente
        </option>


        <option
                value="VALOR-ASC"
                <%= "VALOR-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Valor - Crescente
        </option>


        <option
                value="VALOR-DESC"
                <%= "VALOR-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Valor - Decrescente
        </option>


        <option
                value="DATA_PAGAMENTO-ASC"
                <%= "DATA_PAGAMENTO-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Data Pagamento - Crescente
        </option>


        <option
                value="DATA_PAGAMENTO-DESC"
                <%= "DATA_PAGAMENTO-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Data Pagamento - Decrescente
        </option>


        <option
                value="FK_CONTRATO_ID-ASC"
                <%= "FK_CONTRATO_ID-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Contrato - Crescente
        </option>


        <option
                value="FK_CONTRATO_ID-DESC"
                <%= "FK_CONTRATO_ID-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Contrato - Decrescente
        </option>


        <option
                value="METODO_PAGAMENTO-ASC"
                <%= "METODO_PAGAMENTO-ASC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Método Pagamento - Crescente
        </option>


        <option
                value="METODO_PAGAMENTO-DESC"
                <%= "METODO_PAGAMENTO-DESC".equals(ordenacaoAtual)
                        ? "selected"
                        : "" %>
        >
            Método Pagamento - Decrescente
        </option>


    </select>


    <button type="submit">
        Ordenar
    </button>


</form>


<hr>


<!-- ===================================================== -->
<!-- TABELA DE PAGAMENTOS -->
<!-- ===================================================== -->

<h2>Pagamentos encontrados</h2>


<%
    if (pagamentos == null || pagamentos.isEmpty()) {
%>

<p>Nenhum pagamento cadastrado.</p>

<%
} else {
%>


<table border="1">

    <tr>

        <th>ID</th>

        <th>Valor</th>

        <th>Data Pagamento</th>

        <th>Foi Realizado</th>

        <th>Contrato</th>

        <th>Método de Pagamento</th>

        <th>Ações</th>

    </tr>


    <%
        for (Pagamento pagamento : pagamentos) {
    %>


    <tr>


        <!-- ID -->

        <td>
            <%= pagamento.getId() %>
        </td>


        <!-- VALOR -->

        <td>
            <%= pagamento.getValor() %>
        </td>


        <!-- DATA -->

        <td>
            <%= pagamento.getDataPagamento() %>
        </td>


        <!-- FOI REALIZADO -->

        <td>

            <%
                if (pagamento.getFoiRealizado()) {
            %>

            Sim

            <%
            } else {
            %>

            Não

            <%
                }
            %>

        </td>


        <!-- CONTRATO -->

        <td>
            <%= pagamento.getFkContrato() %>
        </td>


        <!-- MÉTODO -->

        <td>

            <%
                if (pagamento.getMetodoPagamento() != null) {
            %>

            <%= pagamento
                    .getMetodoPagamento()
                    .getNome() %>

            <%
            } else {
            %>

            -

            <%
                }
            %>

        </td>


        <!-- AÇÕES -->

        <td>


            <a href="${pageContext.request.contextPath}/pagamentos?action=update&id=<%= pagamento.getId() %>">
                Editar
            </a>


            <form
                    action="${pageContext.request.contextPath}/pagamentos"
                    method="post"
                    style="display:inline"
            >

                <input
                        type="hidden"
                        name="action"
                        value="delete"
                >


                <input
                        type="hidden"
                        name="id"
                        value="<%= pagamento.getId() %>"
                >


                <button
                        type="submit"
                        onclick="return confirm('Deseja realmente excluir este pagamento?')"
                >
                    Excluir
                </button>


            </form>


        </td>


    </tr>


    <%
        }
    %>


</table>


<%
    }
%>


<!-- ===================================================== -->
<!-- JAVASCRIPT DOS CAMPOS -->
<!-- ===================================================== -->

<script>

    function atualizarCampo() {

        const campo =
            document.getElementById("campoFiltro");

        const operacao =
            document.getElementById("operacaoFiltro");

        const valorTexto =
            document.getElementById("valorFiltroTexto");

        const valorMetodo =
            document.getElementById("valorFiltroSelect");

        const valorBoolean =
            document.getElementById("valorFiltroBoolean");


        /*
         * MÉTODO DE PAGAMENTO
         */
        if (campo.value === "METODO_PAGAMENTO") {

            valorTexto.style.display = "none";
            valorTexto.disabled = true;
            valorTexto.required = false;


            valorMetodo.style.display = "inline";
            valorMetodo.disabled = false;
            valorMetodo.required = true;


            valorBoolean.style.display = "none";
            valorBoolean.disabled = true;
            valorBoolean.required = false;


            operacao.innerHTML = "";


            const opcao =
                document.createElement("option");

            opcao.value = "IGUAL";
            opcao.textContent = "Igual";

            operacao.appendChild(opcao);

        }


        /*
         * FOI REALIZADO
         */
        else if (campo.value === "FOI_REALIZADO") {

            valorTexto.style.display = "none";
            valorTexto.disabled = true;
            valorTexto.required = false;


            valorMetodo.style.display = "none";
            valorMetodo.disabled = true;
            valorMetodo.required = false;


            valorBoolean.style.display = "inline";
            valorBoolean.disabled = false;
            valorBoolean.required = true;


            operacao.innerHTML = "";


            const opcao =
                document.createElement("option");

            opcao.value = "IGUAL";
            opcao.textContent = "Igual";

            operacao.appendChild(opcao);

        }


        /*
         * OUTROS CAMPOS
         */
        else {

            valorTexto.style.display = "inline";
            valorTexto.disabled = false;
            valorTexto.required = true;


            valorMetodo.style.display = "none";
            valorMetodo.disabled = true;
            valorMetodo.required = false;


            valorBoolean.style.display = "none";
            valorBoolean.disabled = true;
            valorBoolean.required = false;


            operacao.innerHTML = "";


            adicionarOperacao(
                operacao,
                "IGUAL",
                "Igual"
            );


            /*
             * VALOR
             */
            if (campo.value === "VALOR") {

                adicionarOperacao(
                    operacao,
                    "MAIOR_QUE",
                    "Maior que"
                );

                adicionarOperacao(
                    operacao,
                    "MAIOR_OU_IGUAL",
                    "Maior ou igual"
                );

                adicionarOperacao(
                    operacao,
                    "MENOR_QUE",
                    "Menor que"
                );

                adicionarOperacao(
                    operacao,
                    "MENOR_OU_IGUAL",
                    "Menor ou igual"
                );

            }


            /*
             * DATA
             */
            else if (campo.value === "DATA_PAGAMENTO") {

                adicionarOperacao(
                    operacao,
                    "MAIOR_QUE",
                    "Maior que"
                );

                adicionarOperacao(
                    operacao,
                    "MAIOR_OU_IGUAL",
                    "Maior ou igual"
                );

                adicionarOperacao(
                    operacao,
                    "MENOR_QUE",
                    "Menor que"
                );

                adicionarOperacao(
                    operacao,
                    "MENOR_OU_IGUAL",
                    "Menor ou igual"
                );

            }

        }

    }


    function adicionarOperacao(
        select,
        valor,
        texto
    ) {

        const option =
            document.createElement("option");

        option.value = valor;

        option.textContent = texto;

        select.appendChild(option);

    }


    /*
     * Inicializa o formulário.
     */
    atualizarCampo();

</script>


</body>

</html>
