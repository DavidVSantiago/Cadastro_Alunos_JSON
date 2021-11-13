package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;

import entidades.Aluno;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import utils.FabricaConexao;

public class Listagem extends JFrame{
    /* Atributos --------------------------------------------------------- */
    private JButton btnCadastrar;
    private JTable tableAlunos;
    private AlunoTableModel modelAlunos;
    ArrayList<Aluno> listaAlunos;
    private JButton btnShowDisciplinas;

    /* Construtores ----------------------------------------------------- */
    public Listagem(){
        super("Listagem dos alunos");

        // inicialização dos componentes
        btnCadastrar = new JButton("Cadastrar alunos");
        btnCadastrar.addActionListener(new EventoCadastrar());
    
        // inicializa o model
        listaAlunos = new ArrayList<Aluno>();
        modelAlunos = new AlunoTableModel(listaAlunos);
        buscaDados();

        // inicializa o JTable
        tableAlunos = new JTable(modelAlunos);
        tableAlunos.setPreferredScrollableViewportSize(new Dimension(500,200)); // definie a largura da tabela
        tableAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // restringe a seleção de um único registro na tabela
        modelAlunos.fireTableDataChanged();
        btnShowDisciplinas = new JButton("Mostrar disciplinas do aluno");
        btnShowDisciplinas.addActionListener(new EventoShowDisciplinas());

        // definição dos layouts
        JPanel panel = new JPanel(new BorderLayout(10,10)); // espaçamento de 5px entre os componentes
        panel.setBorder(new EmptyBorder(10,10,10,10)); // uma borda para afastar os compoentes da janela
        panel.add(btnCadastrar, BorderLayout.NORTH);
        panel.add( new JScrollPane(tableAlunos), BorderLayout.CENTER);
        panel.add(btnShowDisciplinas, BorderLayout.SOUTH);
        add(panel); // coloco o painel dentro da janela

        // configuração da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // impede o redimensionamento da janela
        setLocation(600,300);
        pack(); // define o tamanho da janela (menor possível para caber o conteúdo)
        setVisible(true);
    }

    /* Métodos -------------------------------------------------------------*/

    public void buscaDados(){
        listaAlunos.clear(); // limpa o arraylist de alunos
        JSONParser parser = new JSONParser(); // conversor Json
        // conecta o banco de dados para buscar as informações
        try{
            // obter uma conexão com o banco de dados
            Connection conexao = FabricaConexao.getInstance();
            // prepara a consulta sql
            PreparedStatement ps = conexao.prepareStatement("SELECT * FROM aluno");
            // executa a query sql e obtém o resultado
            ResultSet rs = ps.executeQuery();

            // percorrer a lista de resultados
            while(rs.next()){
                Aluno aluno = new Aluno(); // cria um novo objeto aluno
                
                // captura cada registro de aluno retornado pela consulta
                String jsonAlunoTexto = rs.getString("aluno");
                // converte(parse) o campo aluno para um objeto json
                JSONObject jsonAluno = (JSONObject) parser.parse(jsonAlunoTexto);
                // obtém cada um dos valores do Json
                String nome = (String) jsonAluno.get("nome");
                String cpf = (String) jsonAluno.get("cpf");
                String signo = (String)jsonAluno.get("signo");
                JSONArray disciplinas = (JSONArray) jsonAluno.get("disciplinas");

                // coloca os valores obtidos dentro do objeto aluno 
                aluno.setNome(nome);
                aluno.setCpf(cpf);
                aluno.setSigno(signo);
                aluno.setDisciplinas(disciplinas);

                // coloco cada novo aluno dentro da lista
                listaAlunos.add(aluno);
            }  
            
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ParseException e){
            e.printStackTrace();
        }
    }

    /* Classes internas ---------------------------------------------------- */
    private class EventoCadastrar implements ActionListener{
        public void actionPerformed(ActionEvent e){ // o método invocado quando o btn cadastrar for pressionado
            Cadastro janelaCadastro = new Cadastro(new EventoResposta());
        }
    }
    private class EventoShowDisciplinas implements ActionListener{
        public void actionPerformed(ActionEvent e){ // o método invocado quando o btn cadastrar for pressionado
            // captura o nº da linha selecionada no JTable
            int linhaSelecionada = tableAlunos.getSelectedRow();
            // caputa o aluno pela linha selecionada
            Aluno alunoSelecionado = modelAlunos.getAluno(linhaSelecionada);

            ListagemDisciplinas listagemDisciplinas = new ListagemDisciplinas(alunoSelecionado);
        }
    }
    // evento a ser chamada pela janela de cadastro, quando um cadastro for finalizado com sucesso
    public class EventoResposta{
        public void atualizarDados(){
            buscaDados();
            modelAlunos.fireTableDataChanged();
        }
    }
}
