package model;

public class TipoDeAcesso {

    private Integer id;       // coluna: id
    private String nome;      // coluna: nome
    private String descricao; // coluna: descricao

    // construtor

    public TipoDeAcesso(Integer id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    // getter e setters

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // toString

    @Override
    public String toString(){
        return "TipoDeAcesso{id=%d, nome='%s', descricao='%s'}"
                .formatted(id, nome, descricao);
    }
}
