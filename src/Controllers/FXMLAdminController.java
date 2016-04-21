package Controllers;

import TaskAgent.TaskAgent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class FXMLAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Logout;

    @FXML
    void HandleLogoutButtonAction(ActionEvent event) {
        TaskAgent.logout();
    }
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
