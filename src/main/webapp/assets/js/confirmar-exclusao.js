// Confirmação antes de excluir: o botão Excluir da linha abre o diálogo e só envia o formulário (POST) ao confirmar.
(() => {
  const dialogo = document.getElementById("exclusao-dialog");
  if (!dialogo) return;

  let formulario = null;

  document.addEventListener("click", (evento) => {
    const botao = evento.target.closest(".form-excluir .excluir");
    if (!botao) return;

    evento.preventDefault();
    formulario = botao.closest("form");
    document.getElementById("registro-exclusao").textContent = "Registro #" + formulario.elements.id.value;
    dialogo.showModal();
  });

  document.getElementById("confirmar-exclusao").addEventListener("click", () => {
    if (formulario) formulario.submit();
  });
})();
