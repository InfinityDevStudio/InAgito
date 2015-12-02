package br.com.infinity.inagito;

/**
 * Created by Allison on 29/11/2015.
 */
public class marcadorDados {
String titulo, informacao, tipo, endereco, horario;
double posicaolat, posicaolng;

    public String getTitulo() {
        return titulo;
    }

    public String getInformacao() {
        return informacao;
    }

    public void setInformacao(String informacao) {
        this.informacao = informacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public double getPosicaolat() {
        return posicaolat;
    }

    public void setPosicaolat(double posicaolat) {
        this.posicaolat = posicaolat;
    }

    public double getPosicaolng() {
        return posicaolng;
    }

    public void setPosicaolng(double posicaolng) {
        this.posicaolng = posicaolng;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;

    }
}
