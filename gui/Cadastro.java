package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import org.json.simple.JSONObject;

import gui.Listagem.EventoResposta;
import utils.FabricaConexao;

import org.json.simple.JSONArray;

public class Cadastro extends JFrame{
    /* Atributos --------------------------------------------------------- */
    private JLabel labelNome;
    private JTextField txtNome;
    private JLabel labelCpf;
    private JFormattedTextField txtCpf;
    private JLabel labelSigno;
    private JComboBox caixaSigno;
    private CaixaDisciplinas caixaDisciplinas;
    private JButton botaoSalvar;
    EventoResposta eventoResposta;

    /* Construtores ----------------------------------------------------- */
    public Cadastro(EventoResposta evento){
        super("Cadastrar aluno"); // atribuir o título da janela
        
        eventoResposta = evento; // armazena a ref para o evento de respsota
        // definição dos componentes
        labelNome = new JLabel("Nome:");
        txtNome = new JTextField(20);
        labelCpf = new JLabel("CPF:");
        try{
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            txtCpf = new JFormattedTextField(mascaraCpf);
        }catch(ParseException e){
            e.printStackTrace();
        }
        labelSigno = new JLabel("Signo:");
        // array de textos que será passado para o ComboBox
        String[] signos = {"ARIES","TOURO","GÊMEOS","CÂNCER","LEÃO","VIRGEM",
                           "LIBRA","ESCORPIÃO","SAGITÁRIO","CAPRICÓRNIO","AQUÁRIO","PEIXES"};
        caixaSigno = new JComboBox<String>(signos);
        caixaDisciplinas = new CaixaDisciplinas();
        botaoSalvar = new JButton("Salvar Aluno");
        botaoSalvar.addActionListener(new EventoSalvar());
       
        // definição dos layouts
        JPanel panel = (JPanel) getContentPane(); // obtém o painel de conteúdo desta janela
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10,10,10,10) );
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx=1;
        constraints.weighty=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10,5,5,10);

        // adição dos componentes na janela
        constraints.gridx=0; // coluna 0
        constraints.gridy=0; // linha 0
        panel.add(labelNome,constraints);
        constraints.gridx=1; // coluna 1
        constraints.gridy=0; // linha 0
        panel.add(txtNome, constraints);

        constraints.gridx=0; // coluna 0
        constraints.gridy=1; // linha 1
        panel.add(labelCpf, constraints);
        constraints.gridx=1; // coluna 1
        constraints.gridy=1; // linha 1
        panel.add(txtCpf, constraints);

        constraints.gridx=0; // coluna 0
        constraints.gridy=2; // linha 2
        panel.add(labelSigno, constraints);
        constraints.gridx=1; // coluna 1
        constraints.gridy=2; // linha 2
        panel.add(caixaSigno, constraints);

        constraints.gridx=0; // coluna 0
        constraints.gridy=3; // linha 3
        constraints.gridwidth=2; // ocupa 2 colunas
        panel.add(caixaDisciplinas, constraints);

        constraints.gridx=0; // coluna 0
        constraints.gridy=4; // linha 4
        constraints.gridwidth=2; // ocupa 2 colunas
        panel.add(botaoSalvar, constraints);

        // configuração da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false); // impede o redimensionamento da janela
        setLocation(600,300);
        pack(); // define o tamanho da janela (menor possível para caber o conteúdo)
        setVisible(true);
    }

    /* Métodos -------------------------------------------------------------*/
    private boolean validacaoSalvar(){
        // VALIDAÇÃO DO CAMPO NOME
        if(txtNome.getText().length() == 0){ // se o campo 'nome' está vazio
            JOptionPane.showMessageDialog(this, "O campo 'nome' deve estar preenchido!", "Erro de validação",JOptionPane.WARNING_MESSAGE);
            return false; 
        }

        // VALIDAÇÃO DO CAMPO CPF
        String cpf = txtCpf.getText(); // obter o cpf completo digitado
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        cpf = cpf.replace(" ", "");
        if(cpf.length() < 11){
            JOptionPane.showMessageDialog(this, "O campo 'cpf' deve ter 11 números!", "Erro de validação",JOptionPane.WARNING_MESSAGE);
            return false; 
        }

        // VALIDAÇÃO DO CAMPO DISCIPLINA
        JList listSelecionadas = caixaDisciplinas.getListSelecionadas();
        if(listSelecionadas.getModel().getSize() == 0){ // se não houver disciplinas selecionadas
            JOptionPane.showMessageDialog(this, "Deve selecionar, no mínimo, 1 disicplina!", "Erro de validação",JOptionPane.WARNING_MESSAGE);
            return false; 
        }
        return true;
    }

    private void salvarAlunoNoBanco(){
        JSONObject objetoJson = new JSONObject();
        JSONArray disciplinas = new JSONArray();
        DefaultListModel<String> selecionadas = caixaDisciplinas.getModelSelecionadas();
        for(int i=0; i<selecionadas.size();i++){ // percorrer toda a lista das disciplinas selecionadas
            disciplinas.add(selecionadas.get(i));
        }
        objetoJson.put("disciplinas", disciplinas);
        objetoJson.put("signo", caixaSigno.getSelectedItem());
        objetoJson.put("cpf", txtCpf.getText());
        objetoJson.put("nome", txtNome.getText());

        /* Salva o objeto json no banco de dados ------------- */
        Connection conexao = FabricaConexao.getInstance(); // obtém a instancia do banco de dados
        try{
            PreparedStatement ps = conexao.prepareStatement("INSERT INTO aluno(aluno) VALUES('" +objetoJson.toJSONString()+ "')");
            ps.execute(); // executar o sql no banco de dados
            JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!", "Inserção no Banco",JOptionPane.INFORMATION_MESSAGE);
        }catch(SQLException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar aluno no banco!", "Inserção no Banco",JOptionPane.INFORMATION_MESSAGE);
        }
        // antes de fechar a tela, atualiza os dados na janela listagem
        eventoResposta.atualizarDados(); // busca novamente os dados no banco
        dispose(); //Destroy the JFrame object
    }

    /* Classes internas ---------------------------------------------------- */
    private class EventoSalvar implements ActionListener{
        public void actionPerformed(ActionEvent e){
            // o código que será executado quando o btn salvar for pressionado    
            boolean validacao = validacaoSalvar();

            if(validacao == true){ // verificando se a validação ocorreu com sucesso
                // salvar aluno no banco de dados
                salvarAlunoNoBanco();
            }

        }
    }
}
