const senha = document.getElementById('senha');
const confirmar = document.getElementById('confirmarSenha');
const olho = document.querySelector('.mostrar-senha');
if (olho) olho.addEventListener('click', function () {
  const mostrar = senha.type === 'password';
  senha.type = mostrar ? 'text' : 'password';
  if (confirmar) confirmar.type = senha.type;
  olho.setAttribute('aria-pressed', String(mostrar));
  olho.setAttribute('aria-label', mostrar ? 'Ocultar senhas' : 'Mostrar senhas');
});
const regras = document.querySelectorAll('.regras-senha li');
if (regras.length) {
  function atualizarRegras() {
    const atende = [senha.value.length >= 8, /[A-Za-z]/.test(senha.value), /[0-9]/.test(senha.value)];
    regras.forEach(function (regra, i) { regra.classList.toggle('atendida', atende[i]); });
  }
  senha.addEventListener('input', atualizarRegras);
  atualizarRegras();
}
