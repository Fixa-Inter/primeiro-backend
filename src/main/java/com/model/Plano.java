package com.model;

public class Plano {

    private Integer id;            // coluna: id
    private String nome;           // coluna: nome
    private Double valorMensal;     // coluna: valor_mensal
    private Integer duracaoMeses;  // coluna: duracao_meses
    private String descricao;      // coluna: descricao

    // construtor

    public Plano(Integer id, String nome, Double valorMensal, Integer duracaoMeses, String descricao) {
        this.id = id;
        this.nome = nome;
        this.valorMensal = valorMensal;
        this.duracaoMeses = duracaoMeses;
        this.descricao = descricao;
    }

    // getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(Double valorMensal) {
        this.valorMensal = valorMensal;
    }

    public Integer getDuracaoMeses() {
        return duracaoMeses;
    }

    public void setDuracaoMeses(Integer duracaoMeses) {
        this.duracaoMeses = duracaoMeses;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // toString

    @Override
    public String toString(){
        return "Plano{id=%d, nome='%s', valorMensal=%f, duracaoMeses=%d, descricao='%s'}"
                .formatted(id, nome, valorMensal, duracaoMeses, descricao);
    }
}