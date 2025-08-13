package br.com.example.reitoria2025;

import android.widget.Spinner;
import com.google.firebase.Timestamp;

public class Sugestao {
    private String nome;
    private String texto;
    private String email;
    private boolean anonimo;
    private String categoria;
    private Timestamp dataInsercao;
    private String campus;

    public Sugestao() {} // Necessário para o Firebase

    public Sugestao(String nome, String texto, String email, boolean anonimo, String categoria, String campus) {
        this.nome = nome;
        this.texto = texto;
        this.email = email;
        this.anonimo = anonimo;
        this.categoria = categoria;
        this.campus = campus;
    }

    public String getNome() {
        return nome;
    }

    public String getTexto() {
        return texto;
    }

    public boolean isAnonimo() { // Getter padrão para boolean
        return anonimo;
    }

    public String getCategoria() {
        return categoria;
    }

    public Timestamp getDataInsercao() {
        return dataInsercao;
    }

    public String getCampus() {
        return campus;
    }

    public String getEmail() {
        return email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public void setAnonimo(boolean anonimo) {
        this.anonimo = anonimo;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDataInsercao(Timestamp dataInsercao) {
        this.dataInsercao = dataInsercao;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Sugestão {" +
                "Nome: " + nome +
                " | Texto: " + texto +
                " | Anônimo: " + anonimo +
                " | Categoria: " + categoria +
                " | Campus: " + campus +
                " | E-mail: " + email +
                "}";
    }
}