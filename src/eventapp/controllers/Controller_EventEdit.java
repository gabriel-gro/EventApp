/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eventapp.controllers;

import eventapp.DAO.EventoDAO;
import eventapp.models.Evento;
import eventapp.models.Usuario;
import eventapp.util.SceneManager;
import eventapp.util.Seguranca;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gabriel_Note
 */
public class Controller_EventEdit implements Initializable {

    @FXML
    private TextField txNome;
    @FXML
    private TextField txLocal;
    @FXML
    private DatePicker dpDataIni;
    @FXML
    private DatePicker dpDataFim;
    @FXML
    private TextArea txDescricao;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SceneManager.getInstance().getPrimaryStage().setResizable(false);
    }    
    
    public void btnEditarOnClick(){
        
    }
    
    public void btnCancelarOnClick(){
        boolean confirm = SceneManager.getInstance().alertMsg("Confirmação",
                                                              "Deseja cancelar o cadastro?",
                                                              "Clique em cancelar para continuar cadastrando");
        if(confirm){
            SceneManager.getInstance().getSecondaryStage().close();
        }
    }
}
