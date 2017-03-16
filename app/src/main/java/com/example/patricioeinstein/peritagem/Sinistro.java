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
    private String descricaosinistro;
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

    public String getDescricaosinistro() { return descricaosinistro; }
    public void setDescricaosinistro(String descricaosinistro) { this.descricaosinistro = descricaosinistro; }

    public String getDanos() { return danos; }
    public void setDanos(String danos) { this.danos = danos;}

    public String getNometerceiro() {return nometerceiro;}
    public void setNometerceiro(String nometerceiro) {this.nometerceiro = nometerceiro;}

    public String getMatriculaterceiro() {return matriculaterceiro;}
    public void setMatriculaterceiro(String matriculaterceiro) {this.matriculaterceiro = matriculaterceiro;}

    public String getDanosterceiro() {return danosterceiro;}
    public void setDanosterceiro(String danosterceiro) {this.danosterceiro = danosterceiro;}


    @Override
    public String toString() {
        return "Número Apólice: "+getApolice()+ "\n"+
                "Nome:"+getNome()+"\n"+
                "Data do Sinistro:"+getDataa()+"\n"+
                "Local do Sinistro"+getLocal()+"\n"+
                "Matrícula da Viatura"+getMatricula();
    }
}
