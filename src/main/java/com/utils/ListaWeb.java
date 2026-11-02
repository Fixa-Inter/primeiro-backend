package com.utils;

// Apoio das telas de listagem (JSP): ordenação pelas setas e texto seguro dentro do HTML.
// A ordenação viaja no formato CAMPO-ASC ou CAMPO-DESC (ex: RUA-ASC), o mesmo que os servlets leem.
public class ListaWeb {

    private ListaWeb() {
    }

    // valor de aria-sort do cabecalho: diz ao CSS qual seta destacar
    public static String ariaSort(String ordenacao, String campo) {
        String atual = ordenacao == null ? "" : ordenacao.toUpperCase();

        if ((campo + "-ASC").equals(atual)) {
            return "ascending";
        }
        if ((campo + "-DESC").equals(atual)) {
            return "descending";
        }
        return "none";
    }

    // ordenação que a seta envia ao ser clicada: crescente, e decrescente se já estiver crescente
    public static String proximaOrdenacao(String ordenacao, String campo) {
        String atual = ordenacao == null ? "" : ordenacao.toUpperCase();

        if ((campo + "-ASC").equals(atual)) {
            return campo + "-DESC";
        }
        return campo + "-ASC";
    }

    // evita que aspas ou < > quebrem o HTML quando o valor volta para a tela
    public static String escapar(Object valor) {
        if (valor == null) {
            return "";
        }
        return valor.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
