<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
      Endereço adicionado | FIXA
    </title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/img/comum/logo-claro.svg">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/fontes/fontes.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cadastro.css">
  </head>
  <body>
    <main class="cadastro">
      <a class="sair-cadastro" href="${pageContext.request.contextPath}/index.html" aria-label="Voltar ao site">
        ×
      </a>
      <div class="etapas-cadastro" aria-label="Acesso, CNPJ e Endereço">
        <div class="passo-cadastro">
          <span class="etapa concluida">
            1
          </span>
          <span class="nome-etapa">
            Acesso
          </span>
        </div>
        <span class="linha-etapa concluida" aria-hidden="true">
        </span>
        <span class="etapa concluida">
          2
        </span>
        <span class="nome-etapa">
          CNPJ
        </span>
      </div>
      <span class="linha-etapa concluida" aria-hidden="true">
      </span>
      <span class="etapa concluida">
        3
      </span>
      <span class="nome-etapa">
        Endereço
      </span>
    </div>
  </div>
  <img class="mascote-cadastro" src="${pageContext.request.contextPath}/assets/img/cadastro/manu-cadastro.svg" alt="Manu comemorando o endereço adicionado">
  <h1>
    Endereço adicionado!
  </h1>
  <p class="descricao-cadastro">
    Tudo pronto. O novo endereço já está vinculado à sua instituição.
  </p>
  <div class="aviso-cadastro">
    <h2>
      COMO ENTRAR
    </h2>
    <p>
      Nos próximos acessos, use o e-mail da instituição, a senha que você criou e o CNPJ.
    </p>
  </div>
  <a class="botao-principal" href="${pageContext.request.contextPath}/cadastrar-endereco?action=adicionar">
    Adicionar outro endereço
  </a>
  <a class="voltar-cadastro" href="${pageContext.request.contextPath}/cadastrar-endereco?action=logout">
    Sair
  </a>
</main>
</body>
</html>
