<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Erro 400 - FIXA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/erros.css">
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-escuro.svg">

    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

</head>
<body>
    <header class="cabecalho">
        <a href="${pageContext.request.contextPath}/index.html">
            <img
                class="logo"
                src="${pageContext.request.contextPath}/assets/img/comum/logo1.svg"
                alt="FIXA - Página inicial"
            >
        </a>
    </header>

    <main class="conteudo">
        <section class="informacoes">
            <span class="etiqueta">Erro 400</span>

            <p class="numero-erro">400</p>

            <h1>
                Não conseguimos<br>
                entender o pedido
            </h1>

            <p class="descricao">
                Alguma informação chegou incompleta ou de um jeito
                diferente do esperado. Confira os dados e tente de novo.
            </p>

            <div class="botoes">
                <a class="botao botao-azul" href="${pageContext.request.contextPath}/erros/erro-400.jsp">
                    Tentar de novo
                </a>

                <a class="botao botao-branco" href="${pageContext.request.contextPath}/index.html">
                    Voltar ao início
                </a>
            </div>

            <p class="suporte">
                Se continuar acontecendo, fale com o suporte
                e informe o código 400.
            </p>
        </section>

        <div class="circulo">
            <img
                class="mascote"
                src="${pageContext.request.contextPath}/assets/img/comum/manu-confusa.svg"
                alt="Manu confusa, segurando uma prancheta"
            >
        </div>
    </main>
</body>
</html>