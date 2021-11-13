package entidades;

import java.util.ArrayList;
import java.util.List;

public class Aluno {
    // atributos -------------------------------
    String nome, cpf, signo;
    List<String> disciplinas;
    
    // construtor ------------------------------
    public Aluno(){
        disciplinas = new ArrayList<String>();
    }
    
    // getters e Setters -------------------------------
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getSigno() {
        return signo;
    }
    public void setSigno(String signo) {
        this.signo = signo;
    }
    public List<String> getDisciplinas() {
        return disciplinas;
    }
    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = disciplinas;
    }
    // métodos -------------------------------
    public void print(){
        System.out.println("*********************************");
        System.out.println("Nome: "+nome);
        System.out.println("Cpf: "+cpf);
        System.out.println("Signo: "+signo);
        System.out.println("DISCIPLINAS -------------");
        for(int i=0; i<disciplinas.size(); i++){
            System.out.println("-> "+disciplinas.get(i));
        }
        System.out.println("*********************************");
    }
}
