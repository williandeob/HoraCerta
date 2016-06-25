package model;

import java.sql.Date;

/**
 * Created by willian on 21/06/2016.
 */
public class Medicamento {

    private Long id;
    private String nome;
    private String descricaoDoUso;
    private byte[] imagem;
    private Date dtInicio;
    private int intervaloEmMinutos;
    private Usuario usuario;

    public Medicamento(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

    public String getDescricaoDoUso() {
        return descricaoDoUso;
    }

    public void setDescricaoDoUso(String descricaoDoUso) {
        this.descricaoDoUso = descricaoDoUso;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public int getIntervaloEmMinutos() {
        return intervaloEmMinutos;
    }

    public void setIntervaloEmMinutos(int intervaloEmMinutos) {
        this.intervaloEmMinutos = intervaloEmMinutos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
