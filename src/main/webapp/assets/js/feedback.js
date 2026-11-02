// Defina aqui o destino de Entrar quando a página estiver disponível
function mostrarAviso(mensagem, desfazer, erro = false) {
  let lista = document.querySelector('.avisos-fixa');
  if (!lista) {
    lista = document.createElement('div');
    lista.className = 'avisos-fixa';
    lista.setAttribute('aria-live', 'polite');
    document.body.append(lista);
  }
  const aviso = document.createElement('div');
  aviso.className = 'aviso-fixa' + (erro ? ' aviso-erro' : '');
  aviso.setAttribute('role', 'status');
  const texto = document.createElement('span');
  texto.className = 'aviso-texto'; texto.textContent = mensagem; aviso.append(texto);
  if (desfazer) {
    const botao = document.createElement('button');
    botao.type = 'button'; botao.className = 'aviso-desfazer'; botao.textContent = '↶ Desfazer';
    botao.onclick = function () { aviso.remove(); desfazer(); };
    aviso.append(botao);
  }
  const fechar = document.createElement('button');
  fechar.type = 'button'; fechar.className = 'aviso-fechar'; fechar.textContent = '×';
  fechar.setAttribute('aria-label', 'Fechar aviso'); fechar.onclick = function () { aviso.remove(); };
  aviso.append(fechar);
  const tempo = document.createElement('span');
  tempo.className = 'aviso-tempo'; tempo.setAttribute('aria-hidden', 'true');
  tempo.addEventListener('animationend', function () { aviso.remove(); });
  aviso.append(tempo); lista.append(aviso);
}
document.querySelectorAll('.guia-acao').forEach(function (balao) {
  balao.addEventListener('keydown', function (evento) {
    if (evento.key === ' ') { evento.preventDefault(); balao.click(); }
  });
});
