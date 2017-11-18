package banco_de_dados;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TelaHomeController {

    @FXML private TabPane tabPane;

    private Tab tabAberta(String titulo){
    	for(Tab tb : tabPane.getTabs()){
    		if(tb.getText().equals(titulo))
    			return tb;
    	}
    	return null;
    }
    
    private void selecionaTab(Tab tab){
    	tabPane.getSelectionModel().select(tab);
    }
   
    private void abreTab(String titulo, String path){
    	try{
    		Tab tab = tabAberta(titulo);
    		if(tab==null){
    			tab = new Tab(titulo);
    			tabPane.getTabs().add(tab);
    			tab.setContent((Node) FXMLLoader.load(getClass().getResource(path)));
    		}
    		selecionaTab(tab);
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
   @FXML
   public void abreJanelaCidade(){
	   abreTab("Cadastro de Cidades", "TelaCidade.fxml");
   }
   
   @FXML
   public void abreJanelaCurso(){
	   abreTab("Cadastro de Cursos", "TelaCurso.fxml");
   }
   
   @FXML
   public void abreJanelaAluno(){
	   abreTab("Cadastro de Alunos", "TelaAluno.fxml");
   }
   
   @FXML
   public void abreJanelaMatricula(){
	   abreTab("Matrículas", "TelaMatricula.fxml");
   }
   
}
