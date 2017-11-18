package banco_de_dados;

import java.sql.Connection;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import metodo.Mensagens;

public class TelaMatriculaController {

    //Matrícula de alunos
    @FXML private ComboBox<Aluno> matriAluno;
    @FXML private ComboBox<Curso> matriCurso;
    @FXML private TableView<Matricula> tblMatricula;
    @FXML private TableColumn<Matricula, String> colResultado;
    
    private ArrayList<Curso> cursos = new ArrayList<>();
    private ArrayList<Aluno> alunos = new ArrayList<>();
    private ArrayList<Matricula> busca = new ArrayList<>();

    private Connection conn = Conexao.conn();

    public void initialize() {
    	//Colunas matricula
    	colResultado.setCellValueFactory(cellData -> cellData.getValue().dadosProperty());
    	attMatricula();
    }
    
    //Faz a matrícula do aluno
    @FXML
    public void matriculaAluno(){
    	try{	 
    		if(matriAluno.getSelectionModel().isEmpty() || matriCurso.getSelectionModel().isEmpty())
    			throw new NullPointerException();
 		 
    		Matricula mat = new Matricula();
    		mat.setAluno(matriAluno.getSelectionModel().getSelectedItem());
    		mat.setCurso(matriCurso.getSelectionModel().getSelectedItem());
    		mat.insere(conn);
    		limpaMat();
    		Mensagens.msgInformacao("Parabéns","O(A) aluno(a) "+mat.getAluno()+" foi matriculado no curso "+mat.getCurso()+" com sucesso!");
    		
    	}catch (NullPointerException e){
    		Mensagens.msgErro("ERRO","Selecione os 2 campos");
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    
    //Atualiza os combo boxes quando a aba matricula é aberta
    @FXML
    private void attMatricula(){
 	   alunos = Aluno.listarTodas(conn, null);
 	   matriAluno.setItems(FXCollections.observableArrayList(alunos));
 	   cursos = Curso.listarTodas(conn, null);
 	   matriCurso.setItems(FXCollections.observableArrayList(cursos));
    }

    @FXML
    public void filtraPorCurso(){
 	   Curso c = matriCurso.getSelectionModel().getSelectedItem();
 	   if(c==null){
 		   Mensagens.msgErro("ERRO", "Selecione um curso");
 	   }else{
 	   busca = Matricula.filtraPorCurso(conn, c);
 	   tblMatricula.setItems(FXCollections.observableArrayList(busca));
 	   }   
    }
    
    @FXML
    public void filtraPorAluno(){
 	   Aluno a = matriAluno.getSelectionModel().getSelectedItem();
 	   if(a==null){
 		   Mensagens.msgErro("ERRO", "Selecione um aluno");
 	   }else{
 		   busca = Matricula.filtraPorAluno(conn, a);
 		   tblMatricula.setItems(FXCollections.observableArrayList(busca));
 	   } 
    }
  
    @FXML
    public void limpaMat(){
    	matriAluno.getSelectionModel().clearSelection();
    	matriCurso.getSelectionModel().clearSelection();
    }
    
}