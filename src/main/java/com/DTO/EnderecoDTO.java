package com.DTO;

/** Dados de endereço combinados com o nome da instituição. */
public class EnderecoDTO {
    private Integer id;
    private String rua;
    private String bairro;
    private String complemento;
    private String cidade;
    private String estado;
    private String numero;
    private String cep;
    private String instituicaoNome;

    public EnderecoDTO(Integer id, String rua, String bairro, String complemento, String cidade, String estado, String numero, String cep, String instituicaoNome) {
        this.id = id;
        this.rua = rua;
        this.bairro = bairro;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.cep = cep;
        this.instituicaoNome = instituicaoNome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getInstituicaoNome() {
        return instituicaoNome;
    }

    public void setInstituicaoNome(String instituicaoNome) {
        this.instituicaoNome = instituicaoNome;
    }

    @Override
    public String toString() {
        return "%s %s %s %s %s %s %s %s %s"
                .formatted(id, rua, bairro, complemento, cidade, estado, numero, cep, instituicaoNome);
    }
}
