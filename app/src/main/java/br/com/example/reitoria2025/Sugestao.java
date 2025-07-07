package br.com.example.reitoria2025;

public class Sugestao {
    private String nome;
    private String texto;

    public Sugestao() {} // Necessário para o Firebase

    public Sugestao(String nome, String texto) {
        this.nome = nome;
        this.texto = texto;
    }

    public String getNome() {
        return nome;
    }

    public String getTexto() {
        return texto;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "Sugestão {" +
                "Nome: " + nome +
                " | Texto: " + texto +
                "}";
    }
}
