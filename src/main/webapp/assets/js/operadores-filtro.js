// Barra de filtros das listagens: ao escolher o campo, mostra só os operadores que o backend aceita
// e troca o campo de valor pelo tipo certo (data, numero, Sim/Nao ou lista de opcoes).
(() => {
  const operadores = {
    igual: { value: "IGUAL", label: "=" },
    contem: { value: "CONTEM", label: "Contém" },
    maior: { value: "MAIOR_QUE", label: ">" },
    maiorIgual: { value: "MAIOR_OU_IGUAL", label: ">=" },
    menor: { value: "MENOR_QUE", label: "<" },
    menorIgual: { value: "MENOR_OU_IGUAL", label: "<=" },
  };

  const camposData = new Set([
    "data_inicio", "data_vencimento", "data_pagamento", "data_registro",
    "data_nascimento", "data_criacao", "data_cadastro",
  ]);

  const camposNumericos = new Set(["valor", "valor_mensal", "duracao_meses"]);

  const camposSimNao = new Set(["esta_ativo", "primeiro_acesso", "foi_realizado"]);

  // os valores sao os codigos dos enums do backend
  const camposLista = {
    tipo_acesso: [["1", "Administrador"], ["2", "Gestor"], ["3", "Técnico"], ["4", "Solicitante"]],
    status_contrato: [["1", "Ativo"], ["2", "Inativo"], ["3", "Cancelado"]],
    tipo_instituicao: [["1", "Escola"], ["2", "Faculdade"], ["3", "Empresa"], ["4", "Órgão público"]],
    metodo_pagamento: [["1", "Crédito"], ["2", "Débito"], ["3", "Pix"]],
  };

  const formulario = document.getElementById("filtro-form");
  const campo = document.getElementById("campo-filtro");
  const seletorOperacao = document.getElementById("operador-filtro");
  if (!formulario || !campo || !seletorOperacao) return;

  function atualizarOperacoes(nomeCampo) {
    const valorSelecionado = seletorOperacao.value;
    let disponiveis;

    if (nomeCampo === "id" || nomeCampo.startsWith("fk_") || camposSimNao.has(nomeCampo) || camposLista[nomeCampo]) {
      disponiveis = [operadores.igual];
    } else if (camposData.has(nomeCampo) || camposNumericos.has(nomeCampo)) {
      disponiveis = [operadores.igual, operadores.maior, operadores.maiorIgual, operadores.menor, operadores.menorIgual];
    } else {
      disponiveis = [operadores.igual, operadores.contem];
    }

    seletorOperacao.replaceChildren(...disponiveis.map(({ value, label }) => {
      const opcao = document.createElement("option");
      opcao.value = value;
      opcao.textContent = label;
      return opcao;
    }));

    if (disponiveis.some(({ value }) => value === valorSelecionado)) {
      seletorOperacao.value = valorSelecionado;
    }
  }

  function atualizarValor(nomeCampo) {
    const antigo = document.getElementById("valor-filtro");
    let novo;

    if (camposSimNao.has(nomeCampo) || camposLista[nomeCampo]) {
      novo = document.createElement("select");
      const opcoes = camposSimNao.has(nomeCampo) ? [["true", "Sim"], ["false", "Não"]] : camposLista[nomeCampo];
      novo.add(new Option("Selecione", ""));
      opcoes.forEach(([valor, texto]) => novo.add(new Option(texto, valor)));
    } else {
      novo = document.createElement("input");
      novo.placeholder = "Valor";
      if (camposData.has(nomeCampo)) {
        novo.type = "date";
      } else if (nomeCampo === "id" || nomeCampo.startsWith("fk_") || camposNumericos.has(nomeCampo)) {
        novo.type = "number";
        novo.step = "any";
      } else {
        novo.type = "text";
      }
    }

    novo.id = "valor-filtro";
    novo.name = "valorFiltro";
    antigo.replaceWith(novo);
  }

  function atualizar() {
    // o backend recebe o campo em maiusculo (RUA); aqui a regra usa minusculo
    const nomeCampo = campo.value.toLowerCase();
    atualizarOperacoes(nomeCampo);
    atualizarValor(nomeCampo);
  }

  campo.addEventListener("change", atualizar);
  atualizar();
})();
