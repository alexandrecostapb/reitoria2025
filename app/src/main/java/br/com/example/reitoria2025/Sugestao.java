package br.com.example.reitoria2025;

import android.widget.Spinner;

public class Sugestao {
    private String nome;
    private String texto;
    private boolean anonimo;
    private String categoria;

    public Sugestao() {} // Necessário para o Firebase

    public Sugestao(String nome, String texto, boolean anonimo, String categoria) {
        this.nome = nome;
        this.texto = texto;
        this.anonimo = anonimo;
        this.categoria = categoria;
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

    @Override
    public String toString() {
        return "Sugestão {" +
                "Nome: " + nome +
                " | Texto: " + texto +
                " | Anônimo: " + anonimo +
                " | Categoria: " + categoria +
                "}";
    }
}