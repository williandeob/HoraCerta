package model;

/**
 * Created by willian on 11/06/2016.
 */
public class Usuario {
    /*
    padrao Singleston para o objeto usuario
     */
    private static Usuario usuario;

    private Long id;
    private String nome;
    private String email;
    private String senha;

    private Usuario() {
    }

    public static Usuario getUsuarioInstance(){
        if(usuario == null){
            return new Usuario();
        }
        return usuario;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
