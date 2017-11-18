package banco_de_dados;

import java.sql.Connection;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import metodo.Mensagens;

public class TelaCursoController {

	//Cadastro de cursos
    @FXML private TextField txtCurso;
    @FXML private TextField txtFiltroCurso;
	@FXML private TableView<Curso> tblCurso;
    @FXML private TableColumn<Curso, String> colCurso;
    
    private ArrayList<Curso> cursos = new ArrayList<>();

    private Connection conn = Conexao.conn();
    
    public void initialize() {
    	//Coluna curso
    	colCurso.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
    	attTblCurso();
    }
	
    //Insere um curso
    @FXML
    public void insereCurso(){
    	try{
    		if(txtCurso.getText().equals(""))
    			throw new NullPointerException();
    		
    		if(txtCurso.getText().matches(".*\\d+.*"))
    			throw new TemNumeroException();
    		
    		Curso c = new Curso();
    		c.setNome(txtCurso.getText());
    		c.insere(conn);
    		attTblCurso();
    		txtCurso.setText("");
    		
    	}catch (TemNumeroException e){
    		Mensagens.msgErro("FALHA","O campo destacado não pode conter números");    
    	}catch (NullPointerException e){
    		Mensagens.msgErro("FALHA","Preencha todos os campos");    
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
   //Modifica o curso selecionado
   @FXML
   public void alteraCurso(){
		   Curso sel = tblCurso.getSelectionModel().getSelectedItem();
	   
		   if(sel!=null){
			   sel.setNome(txtCurso.getText());
			   sel.altera(conn);
			   attTblCurso();
			   txtCurso.setText("");
			   
		   }else
			   Mensagens.msgErro("FALHA", "Selecione um Curso");
			   
   }

   //Inativa o curso selecionado
   @FXML
   public void excluiCurso(){
	   Curso sel = tblCurso.getSelectionModel().getSelectedItem();
	   
	   if(sel!=null){
		   if(Mensagens.msgExcluir()){		   
			   sel.exclui(conn);
			   attTblCurso();
			   txtCurso.setText("");
			   Mensagens.msgInformacao("Sucesso", "Curso excluído com êxito");
	   }else
	    	Mensagens.msgInformacao("Cancelado","Curso não excluído");
	   
	 }else
		 Mensagens.msgErro("FALHA", "Selecione um curso");
   }
     
   //Filtragem de cursos
   @FXML
   public void filtraCurso(){
	   String filtro = txtFiltroCurso.getText();
	   
	   if(filtro.equals(""))
		   filtro=null;
	   
	   cursos = Curso.listarTodas(conn, filtro);
	   tblCurso.setItems(FXCollections.observableArrayList(cursos));
   }  

   //Preenche o campo de texto quando clica no item
   @FXML
   public void clicaTblCurso(){
	   Curso sel = tblCurso.getSelectionModel().getSelectedItem();
	   txtCurso.setText(sel.getNome());
   }
   
   //Atualiza a tabela de cursos
   @FXML
   public void attTblCurso(){
	   cursos = Curso.listarTodas(conn, null);
	   tblCurso.setItems(FXCollections.observableArrayList(cursos));
   }
	
   @FXML
   public void filtraCarac(){
   	//Cadastro curso
   	if(txtCurso.getText().matches(".*\\d+.*")){
			txtCurso.setStyle("-fx-text-fill: red;");
   	}else{
			txtCurso.setStyle("-fx-text-fill: black;");}
   }
   
}
