package Controllers;

import Logic.Querys;
import Logic.WindowsOpener;
import Models.Task;
import Models.User;
import static TaskAgent.DBConnection.Execute;
import static TaskAgent.TaskAgent.db;
import static TaskAgent.TaskAgent.user_state;
import static TaskAgent.TaskAgent.actual_option;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class FXMLAdminController implements Initializable {
    @FXML
    private Button AddUser;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField task_name, task_desc, LastName, Login, Email, FirstName, Password;
    @FXML
    private ComboBox task_supervisior, task_user, comboGroup;
    @FXML
    private Button Tasks;
    @FXML
    private Label LogAs;
    @FXML
    private Button Logout;
    @FXML
    private Button Users, task_button;
    @FXML
    private Button Back;
    @FXML
    private TableView<Task> task_table = new TableView<>();
    @FXML
    private ObservableList<Task> data1 = FXCollections.observableArrayList();
    @FXML
    private TableView<User> table_users = new TableView<>();
    @FXML
    private ObservableList<User> data2 = FXCollections.observableArrayList();
    @FXML
    public TableColumn colName, colDesc, colSupervisor, colUser, colFirstName, colGroup, colLastName;

    @FXML
    void HandleLogoutButtonAction(ActionEvent event) {
        WindowsOpener.logout();
    }

    @FXML
    void addTaskButton(ActionEvent event) {
        String name = task_name.getText();
        String desc = task_desc.getText();
        String[] supervisor = task_supervisior.getSelectionModel().getSelectedItem().toString().split(" ");
        String[] user = task_user.getSelectionModel().getSelectedItem().toString().split(" ");
        if(actual_option == 0) {
            Querys.addTask(name, desc, supervisor, user);
        } else {
            Querys.editTask(actual_option, name, desc, supervisor, user);
            actual_option = 0;
            task_button.setText("Dodaj");
        }
    }
    
    @FXML
    void handleDeleteUser(ActionEvent event) {
        int id = table_users.getSelectionModel().getSelectedItem().getId();
        if(id > 0) {
            Execute("DELETE FROM users WHERE id = "+id);
            WindowsOpener.open("/TaskAgent/FXMLUsers.fxml", "Tasks", true);
        }
    }

    @FXML
    void deleteTaskButton(ActionEvent event) {
        int id = task_table.getSelectionModel().getSelectedItem().getId();
        if(id > 0) {
            Execute("DELETE FROM tasks WHERE id = "+id);
            WindowsOpener.open("/TaskAgent/FXMLTasks.fxml", "Tasks", true);
        }
    }

    void fillTaskTable() {
        ResultSet p = db.Query("SELECT t.*, u.firstname AS super_name, u.lastname AS super_last, u2.firstname AS first_2, u2.lastname AS last_2 FROM tasks t JOIN users u ON u.id = t.id_supervisor LEFT JOIN users u2 ON u2.id = t.user_id");
        data1.clear();
        task_table.setEditable(true);
        try {
            while(p.next()) {
                data1.add(new Task(p.getInt("id"),p.getString("name"),p.getString("description"), p.getString("super_name") + " " + p.getString("super_last"), p.getString("first_2") + " " + p.getString("last_2")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colSupervisor.setCellValueFactory(new PropertyValueFactory<>("supervisor"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("user"));
        task_table.setItems(data1);
    }
    
    void fillUserTable() {
        ResultSet p = db.Query("SELECT * FROM users");
        data2.clear();
        table_users.setEditable(true);
        try {
            while(p.next()) {
                data2.add(new User(p.getInt("id"), p.getString("firstname"), p.getString("lastname"), p.getInt("id_groups")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        colGroup.setCellValueFactory(new PropertyValueFactory<>("groupname"));
        table_users.setItems(data2);
    }
    
    @FXML
    void editTask(ActionEvent event) {
        Task task = task_table.getSelectionModel().getSelectedItem();
        if(task.getId() > 0) {
            actual_option = task.getId();
            task_button.setText("Edytuj");
            task_name.setText(task.getName());
            task_desc.setText(task.getDescription());
        }
    }

    @FXML
    void HandleUsersButtonAction(ActionEvent event) {
        user_state = 3;
        WindowsOpener.open("/TaskAgent/FXMLUsers.fxml", "Users", true);
    }

    @FXML
    void HandleTasksButtonAction(ActionEvent event) {
        user_state = 1;
        WindowsOpener.open("/TaskAgent/FXMLTasks.fxml", "Tasks", true);
    }

    @FXML
    void HandleBackButtonAction(ActionEvent event) {
        user_state = 0;
        WindowsOpener.open("/TaskAgent/FXMLAdmin.fxml", "Administrator", false);
    }
 
    @FXML
    void HandleAddUserWindow(ActionEvent event) {
        user_state = 9;
        WindowsOpener.open("/TaskAgent/FXMLaddUser.fxml","Add User",true);
    }
    
    @FXML
    void HandleEditUserWindow(ActionEvent event) {
        int id = table_users.getSelectionModel().getSelectedItem().getId();
        if(id > 0) {
            actual_option = id;
            user_state = 10;
            WindowsOpener.open("/TaskAgent/FXMLaddUser.fxml","Add User",true);
        }
    }

    @FXML
    void HandleBackToUserButtonAction(ActionEvent event){
        user_state = 3;
        WindowsOpener.open("/TaskAgent/FXMLUsers.fxml","Users",false);
    }

    @FXML
    void ConfrimUserHandler(ActionEvent event){
        if(actual_option == 0)
            Querys.AddUser(FirstName.getText(), LastName.getText(), Login.getText(), Password.getText(), Email.getText(), comboGroup.getSelectionModel().getSelectedIndex() + 1);
        else
            Querys.editUser(actual_option, FirstName.getText(), LastName.getText(), Login.getText(), Password.getText(), Email.getText(), comboGroup.getSelectionModel().getSelectedIndex() + 1);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(user_state == 1) {
            fillTaskTable();
            ResultSet p = db.Query("SELECT * FROM users");
            try {
                while (p.next()) {
                    task_supervisior.getItems().add(
                        p.getString("firstname") + " " + p.getString("lastname")
                    );
                    task_user.getItems().add(
                        p.getString("firstname") + " " + p.getString("lastname")
                    );
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } else if(user_state == 3) {
            fillUserTable();
        } else if(user_state == 9) {
            comboGroup.getItems().addAll(
                "User", "Supervisor", "Administrator"
            );
        } else if(user_state == 10) {
            comboGroup.getItems().addAll(
                "User", "Supervisor", "Administrator"
            );
            ResultSet user = db.Query("SELECT * FROM users WHERE id = "+actual_option);
            try {
                if(user.next()) {
                    FirstName.setText(user.getString("firstname"));
                    LastName.setText(user.getString("lastname"));
                    Email.setText(user.getString("email"));
                    Login.setText(user.getString("login"));
                    Password.setText(user.getString("password"));
                    comboGroup.getSelectionModel().select(user.getInt("id_groups") - 1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLAdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
