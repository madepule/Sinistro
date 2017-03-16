package com.example.patricioeinstein.peritagem;

/**
 * Created by Patricio  Einstein on 3/11/2017.
 */

public class Sinistro {

    private String nome;
    private String dataa;
    private String local;
    private String apolice;
    private String matricula;
    private String localsinistro;
    private String danos;
    private String nometerceiro;
    private String matriculaterceiro;
    private String danosterceiro;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataa() {
        return dataa;
    }
    public void setDataa(String dataa) {
        this.dataa = dataa;
    }

    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }

    public String getApolice() { return apolice; }
    public void setApolice(String apolice) {this.apolice = apolice; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getLocalsinistro() { return localsinistro; }
    public void setLocalsinistro(String localsinistro) { this.localsinistro = localsinistro; }

    public String getDanos() { return danos; }
    public void setDanos(String danos) { this.danos = danos;}

    public String getNometerceiro() {return nometerceiro;}
    public void setNometerceiro(String nometerceiro) {this.nometerceiro = nometerceiro;}

    public String getMatriculaterceiro() {return matriculaterceiro;}
    public void setMatriculaterceiro(String matriculaterceiro) {this.matriculaterceiro = matriculaterceiro;}

    public String getDanosterceiro() {return danosterceiro;}
    public void setDanosterceiro(String danosterceiro) {this.danosterceiro = danosterceiro;}
}
