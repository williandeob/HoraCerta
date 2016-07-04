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
    private String userName;
    private String email;
    private String password;

    private Usuario() {
    }

    public static synchronized Usuario getUsuarioInstance(){
        if(usuario == null){
            usuario = new Usuario();
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
