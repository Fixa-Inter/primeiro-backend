package model;

public class SuperAdministrador {

    private Integer id;                // coluna: id
    private String cargo;              // coluna: cargo
    private String nome;               // coluna: nome
    private String senhaHash;          // coluna: senha_hash

    // construtor

    public SuperAdministrador(Integer id, String cargo, String nome, String senhaHash) {
        this.id = id;
        this.cargo = cargo;
        this.nome = nome;
        this.senhaHash = senhaHash;
    }

    // getters e setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    // toString

    @Override
    public String toString(){
        return "SuperAdministrador{id=%d, cargo='%s', nome='%s', senhaHash='%s'}"
                .formatted(id, cargo, nome, senhaHash);
    }
}
