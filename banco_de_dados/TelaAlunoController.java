package banco_de_dados;

import java.sql.Connection;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import metodo.Mensagens;

public class TelaAlunoController {
	
	//Cadastro de alunos
		@FXML private TextField txtAluno;
	    @FXML private TextField txtIdade;
	    @FXML private TextField txtFiltroA;
	    @FXML private RadioButton rbFem;
	    @FXML private RadioButton rbMasc;
	    @FXML private ComboBox<Cidade> cidadeA;
		@FXML private TableView<Aluno> tblAluno;
		@FXML private TableColumn<Aluno, String> colNomeA;
		@FXML private TableColumn<Aluno, String> colCidadeA;
		@FXML private TableColumn<Aluno, String> colUFA;
		@FXML private TableColumn<Aluno, Number> colIdade;
		@FXML private TableColumn<Aluno, String> colSexo;
		
	    private ArrayList<Aluno> alunos = new ArrayList<>();
	    private ArrayList<Cidade> cidades = new ArrayList<>();

	    private Connection conn = Conexao.conn();
		
		public void initialize() {
			//Colunas aluno
	    	colNomeA.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
	    	colSexo.setCellValueFactory(cellData -> cellData.getValue().sexoProperty());
	    	colIdade.setCellValueFactory(cellData -> cellData.getValue().idadeProperty());
	    	colCidadeA.setCellValueFactory(cellData -> cellData.getValue().getCidade().nomeProperty());
	    	colUFA.setCellValueFactory(cellData -> cellData.getValue().getCidade().ufProperty());
	    	attTblAluno();
	    	abaAluno();
		}

	 //Atualiza a tabela de alunos
    @FXML
    private void attTblAluno(){
    	alunos = Aluno.listarTodas(conn, null);
    	tblAluno.setItems(FXCollections.observableArrayList(alunos));
    }
    
    //Preenche os campos quando clica no item da tabela
    @FXML
    public void clicaTblAluno(){
    	Aluno sel = tblAluno.getSelectionModel().getSelectedItem();
    	txtAluno.setText(sel.getNome());
    	txtIdade.setText(sel.getIdade()+"");
    	cidadeA.getSelectionModel().select(sel.getCidade());
    	
    	if(sel.getSexo().equals("M"))
    		rbMasc.setSelected(true);
    	else
    		rbFem.setSelected(true);
    }
    
    //Atualiza ComboBox quando abre a aba de aluno
    @FXML
    public void abaAluno(){
    	cidades = Cidade.listarTodas(conn, null);
    	cidadeA.setItems(FXCollections.observableArrayList(cidades));	
    }
    
    //Insere um novo aluno
    @FXML
    public void insereAluno(){
    	try{
    		if(txtAluno.getText().equals(""))
    			throw new NullPointerException();
		
    		if(txtAluno.getText().matches(".*\\d+.*"))
    			throw new TemNumeroException();
    		
    		if(!rbMasc.isSelected()){
    			if(!rbFem.isSelected())
    				throw new NullPointerException();
    		}    		
    		Aluno a = new Aluno();
    		a.setNome(txtAluno.getText());
    		a.setIdade(Integer.parseInt(txtIdade.getText()));
    		a.setSexo(rbMasc.isSelected()?"M":"F");
    		a.setCidade(cidadeA.getSelectionModel().getSelectedItem());
    		a.insere(conn);
    		alunos = Aluno.listarTodas(conn, null);
    		tblAluno.setItems(FXCollections.observableArrayList(alunos));
    		attTblAluno();
    		limpaAluno();
    		Mensagens.msgInformacao("Parabéns","Aluno cadastrado com sucesso");
    		
    	}catch (TemNumeroException e){
    		Mensagens.msgErro("FALHA","O campo destacado não pode conter números");    
    	}catch (NullPointerException e){
    		Mensagens.msgErro("FALHA","Preencha todos os campos");    
    	}catch (NumberFormatException e){
    		Mensagens.msgErro("FALHA","Erro de conversão numérica");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
  //Alteração do aluno selecionado
    @FXML
    public void alteraAluno(){
    	Aluno sel = tblAluno.getSelectionModel().getSelectedItem();
    		if(sel!=null){
    			sel.setNome(txtAluno.getText());
    			sel.setIdade(Integer.parseInt(txtIdade.getText()));
    			sel.setCidade(cidadeA.getSelectionModel().getSelectedItem());
    			sel.setSexo(rbMasc.isSelected()?"M":"F");
    			sel.altera(conn);
    			attTblAluno();
    	}else
    		Mensagens.msgErro("FALHA", "Selecione um Aluno");
    }
    
  //Inativação do Aluno selecionado
    @FXML
    public void excluiAluno(){
    	Aluno sel = tblAluno.getSelectionModel().getSelectedItem();
    	
    	if(sel!=null){
    		if(Mensagens.msgExcluir()){
    			sel.exclui(conn);
    			attTblAluno();
    			limpaAluno();
    			Mensagens.msgInformacao("Sucesso", "Aluno excluído com êxito");
    		}else
	    	Mensagens.msgInformacao("Cancelado","Aluno não excluído");
    	}else
    		Mensagens.msgErro("FALHA", "Selecione um Aluno");
    }
    
    //Filtro do aluno
    @FXML
    public void filtraAluno(){
    	String filtro = txtFiltroA.getText();
       	
       	if(filtro.equals(""))
       		filtro=null;
       	
       	alunos = Aluno.listarTodas(conn, filtro);
		tblAluno.setItems(FXCollections.observableArrayList(alunos));
    }
    
    //Reseta todos os campos do aluno
    @FXML
    public void limpaAluno(){
    	txtAluno.setText("");
    	rbMasc.setSelected(false);
    	rbFem.setSelected(false);
    	txtIdade.setText("");
    	cidadeA.getSelectionModel().clearSelection();
    }
    
    //Ainda em testes
    @FXML
    public void filtraCarac(){
    	//Cadastro aluno
    	if(txtAluno.getText().matches(".*\\d+.*")){
			txtAluno.setStyle("-fx-text-fill: red;");
    	}else{
    		txtAluno.setStyle("-fx-text-fill: black;");}
    	
    	if(txtIdade.getText().matches(".*[a-z].*")){
			txtIdade.setStyle("-fx-text-fill: red;");
    	}else{
    		txtIdade.setStyle("-fx-text-fill: black;");}
    }	

    
    
}