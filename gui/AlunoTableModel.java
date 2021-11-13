package gui;

import entidades.Aluno;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class AlunoTableModel extends AbstractTableModel{

   
    /* Atributos ----------------------------------------------------------- */
    String[] colunas = {"nome","cpf","signo"}; // nomes das colunas
    private ArrayList<Aluno> listaAlunos; // onde ficam os dados do model
    /* Contrutor ----------------------------------------------------------- */
    public AlunoTableModel(ArrayList<Aluno> a){
        // quando este model é inicalizado, devemos passar
        // para o construtor a lista de alunos que o alimentará.
        listaAlunos = a;
    }

    /* Métodos ------------------------------------------------------------- */
    public Aluno getAluno(int linha){
        // retorna um registro específico da lista de alunos
        return listaAlunos.get(linha);
    }

    /* Métodos sobrecarregados - p/ fazer o model funcionar --------------- */
    @Override
    public int getRowCount() { 
        return listaAlunos.size();
    }

    // especifica a quantidade de colunas
    @Override
    public int getColumnCount() {
        // a qtd de colunas será o tamnho do array colunas
        return colunas.length;
    }

    // especifica onde estão os nomes das colunas
    @Override
    public String getColumnName(int indice) {
        return colunas[indice];
    }

    

    // preenche cada célula da tabela
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // obtem o registro do aluno da linha especificada
        Aluno linha = listaAlunos.get(rowIndex);

        if(columnIndex == 0){ // coluna NOME
            return linha.getNome();
        }else if(columnIndex == 1){ // coluna CPF
            return linha.getCpf();
        }else if(columnIndex == 2){ // coluna SIGNO
            return linha.getSigno();
        }
        return null; // se houver algum erro de índice
    }
}
