package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import entidades.Aluno;

import java.awt.*;
import java.util.List;

public class ListagemDisciplinas extends JFrame{
    /* Atributos --------------------------------------------------------- */
    private JLabel labelNome;
    private JLabel labelCpf;
    private JLabel labelSigno;
    private JList listDisciplinas;
    private DefaultListModel<String> modelDisciplinas;

    /* Construtores ----------------------------------------------------- */
    public ListagemDisciplinas(Aluno aluno){
        super("Disciplinas do aluno"); // nome da janela

        // inicialização dos labels
        labelNome = new JLabel("Nome: "+aluno.getNome());
        labelCpf = new JLabel("CPF: "+aluno.getCpf());
        labelSigno = new JLabel("Signo: "+aluno.getSigno());

        // inicialização do model de disciplinas
        modelDisciplinas = new DefaultListModel<String>();

        // obtém a lista de disciplinas do aluno
        List<String> disciplinas = aluno.getDisciplinas();
        // obtenho a qtd de disciplinas do aluno
        int n = disciplinas.size(); 

        for(int i=0; i < n; i++){ // coloca as n dsiciplinas do aluno
            // obtem cada uma das disciplinas do aluno
            String disciplina = disciplinas.get(i);
            // coloca cada uma delas dentro do model
            modelDisciplinas.addElement(disciplina);
        }

        listDisciplinas = new JList(modelDisciplinas);
        listDisciplinas.setVisibleRowCount(5);
        listDisciplinas.setFixedCellWidth(200);
        listDisciplinas.setFixedCellHeight(15);
        listDisciplinas.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); // permite selecionar vários registros da lista

        // configuração do layout
        JPanel panel = (JPanel)getContentPane(); // obtem o panel da própria janela
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10,10,10,10) );
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx=1;
        constraints.weighty=1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5,5,5,5);

        // adiciona os componentes na janela
        constraints.gridx=0; // coluna 0
        constraints.gridy=0; // linha 0
        panel.add(labelNome,constraints);
        constraints.gridx=0; // coluna 0
        constraints.gridy=1; // linha 1
        panel.add(labelCpf,constraints);
        constraints.gridx=0; // coluna 0
        constraints.gridy=2; // linha 2
        panel.add(labelSigno,constraints);
        constraints.gridx=0; // coluna 0
        constraints.gridy=3; // linha 3
        panel.add( new JScrollPane(listDisciplinas) ,constraints);

        // configuração da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false); // impede o redimensionamento da janela
        setLocation(600,300);
        pack(); // define o tamanho da janela (menor possível para caber o conteúdo)
        setVisible(true);
    }

    /* Métodos -------------------------------------------------------------*/

    /* Classes internas ---------------------------------------------------- */
}
