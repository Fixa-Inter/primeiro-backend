package com.utils;

// Apoio das listagens (JSP): ordenação pelas setas e texto seguro no HTML
public class ListaWeb {

    // Classe utilitária, não deve ser instanciada
    private ListaWeb() {
    }

    // Devolve o aria-sort do cabeçalho, que diz ao CSS qual seta destacar
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

    // Devolve a próxima ordenação do clique: crescente, decrescente e vazia (ordem padrão)
    public static String proximaOrdenacao(String ordenacao, String campo) {
        String atual = ordenacao == null ? "" : ordenacao.toUpperCase();

        if ((campo + "-ASC").equals(atual)) {
            return campo + "-DESC";
        }
        if ((campo + "-DESC").equals(atual)) {
            return "";
        }
        return campo + "-ASC";
    }

    // Escapa os caracteres especiais para não quebrar o HTML
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
