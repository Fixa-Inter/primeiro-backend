// Avança para o próximo dígito ao digitar e volta ao apagar.
document.querySelectorAll('.codigo-inputs input').forEach(function (campo, i, lista) {
  campo.addEventListener('input', function () {
    if (campo.value && lista[i + 1]) lista[i + 1].focus();
  });
  campo.addEventListener('keydown', function (e) {
    if (e.key === 'Backspace' && !campo.value && lista[i - 1]) lista[i - 1].focus();
  });
});
