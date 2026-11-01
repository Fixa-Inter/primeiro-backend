(() => {
  const operadores = {
    igual: { value: "IGUAL", label: "=" },
    contem: { value: "CONTEM", label: "Contém" },
    maior: { value: "MAIOR_QUE", label: ">" },
    maiorIgual: { value: "MAIOR_OU_IGUAL", label: ">=" },
    menor: { value: "MENOR_QUE", label: "<" },
    menorIgual: { value: "MENOR_OU_IGUAL", label: "<=" },
  };

  const camposComparaveis = new Set([
    "data_inicio", "data_vencimento", "data_pagamento", "data_registro",
    "data_nascimento", "data_criacao", "valor", "valor_mensal",
    "duracao_meses",
  ]);

  document.querySelectorAll(".filtros").forEach((formulario) => {
    const campo = formulario.querySelector('[name="campo"]');
    const seletorOperacao = formulario.querySelector('[name="operador"]');
    if (!campo || !seletorOperacao) return;

    function atualizarOperacoes() {
      const valorSelecionado = seletorOperacao.value;
      const nomeCampo = campo.value.toLowerCase();
      let disponiveis;

      if (nomeCampo === "id" || nomeCampo.startsWith("fk_") || ["cnpj", "cep"].includes(nomeCampo)) {
        disponiveis = [operadores.igual];
      } else if (camposComparaveis.has(nomeCampo)) {
        disponiveis = [operadores.igual, operadores.maior, operadores.maiorIgual, operadores.menor, operadores.menorIgual];
      } else if (["esta_ativo", "primeiro_acesso", "foi_realizado", "em_dia"].includes(nomeCampo)) {
        disponiveis = [operadores.igual];
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

    campo.addEventListener("change", atualizarOperacoes);
    atualizarOperacoes();
  });
})();
