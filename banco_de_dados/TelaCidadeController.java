package banco_de_dados;

import java.sql.Connection;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import metodo.Mensagens;

public class TelaCidadeController {

	@FXML private TextField txtFiltroCidade;
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> txtUf;
	@FXML private TableView<Cidade> tblCidade;
    @FXML private TableColumn<Cidade, String> colNome;
    @FXML private TableColumn<Cidade, String> colUF;
    
    private ArrayList<String> estados = new ArrayList<>();
    private ArrayList<Cidade> cidades = new ArrayList<>();

    private Connection conn = Conexao.conn();
    
    private void populaEstados(){
    	estados.add("SP");
    	estados.add("SC");
    	estados.add("RS");
    	estados.add("PR");
    	estados.add("RJ");
    	estados.add("MG");
    	estados.add("GO");
    	estados.add("ES");
    }
    
    public void initialize() {
    	//Preenche combobox
    	populaEstados();
		txtUf.setItems(FXCollections.observableArrayList(estados));
		
		//Colunas Cidade
    	colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
    	colUF.setCellValueFactory(cellData -> cellData.getValue().ufProperty());
    	
    	//Atualiza Cidades
    	attTblCidade();
    }
    
  //Insere uma nova cidade
    @FXML
    public void insereCidade(){
     try{ 	
    	if(txtNome.getText().equals(""))
    		throw new NullPointerException();
    	
    	if(txtNome.getText().matches(".*\\d+.*"))
    		throw new TemNumeroException();
    	
    	if(txtUf.getSelectionModel().isEmpty())
    		throw new NullPointerException();

    	Cidade c = new Cidade();
    	c.setNome(txtNome.getText());
    	c.setUf(txtUf.getSelectionModel().getSelectedItem());
    	c.insere(conn);
    	attTblCidade();
    	limpaCidade();
    	Mensagens.msgInformacao("Parabéns","Cidade cadastrada com sucesso");
    	
     	}catch (TemNumeroException e){
     		Mensagens.msgErro("FALHA","O campo destacado não pode conter números");    
 		}catch (NullPointerException e){
 			Mensagens.msgErro("FALHA","Preencha todos os campos");    
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
  
    //Altera a cidade Selecionada
    @FXML
    public void alteraCidade(){
    	Cidade sel = tblCidade.getSelectionModel().getSelectedItem();
    	
    	if(sel!=null){
    		sel.setNome(txtNome.getText());
    		sel.setUf(txtUf.getSelectionModel().getSelectedItem());
    		sel.altera(conn);
    		attTblCidade();
    		limpaCidade();
    	}else{
    		Mensagens.msgErro("FALHA", "Selecione uma cidade");
    	}
    }
    
    //Exclui a cidade Selecionada
    @FXML
    public void excluiCidade(){
		Cidade sel = tblCidade.getSelectionModel().getSelectedItem();
		
    	if(sel!=null){
    		if(Mensagens.msgExcluir()){
    			sel.exclui(conn);
    			attTblCidade();
    			limpaCidade();
    			Mensagens.msgInformacao("Sucesso", "Cidade excluída com êxito");
    		}else
    			Mensagens.msgInformacao("Cancelado", "Cidade não excluída");
    	}else
    		Mensagens.msgErro("FALHA", "Selecione uma Cidade");
    }
    
    //Limpa os campos da cidade
    @FXML
    public void limpaCidade(){
    	txtNome.setText("");
    	txtUf.getSelectionModel().clearSelection();
    }
  
    //Atualiza a tabela de cidades
    @FXML
    public void attTblCidade(){
    	cidades = Cidade.listarTodas(conn, null);
    	tblCidade.setItems(FXCollections.observableArrayList(cidades));
    }
    
    //Faz a filtragem de cidades
    @FXML
    public void filtraCidade(){
    	String filtro = txtFiltroCidade.getText();
    	
    	if(filtro.equals(""))
    		filtro=null;
    	
    	cidades = Cidade.listarTodas(conn, filtro);
		tblCidade.setItems(FXCollections.observableArrayList(cidades));	
	}
    
    //Preenche os campo de texto quando clica no item
    @FXML
    public void clicaTblCidade(){
    	Cidade sel = tblCidade.getSelectionModel().getSelectedItem();
    	txtNome.setText(sel.getNome());
    	txtUf.getSelectionModel().select(sel.getUf());
    }    
    
    @FXML
    public void filtraCarac(){
    	//Cadastro cidade
    	if(txtNome.getText().matches(".*\\d+.*")){
			txtNome.setStyle("-fx-text-fill: red;");
    	}else{
			txtNome.setStyle("-fx-text-fill: black;");}
    }

}
