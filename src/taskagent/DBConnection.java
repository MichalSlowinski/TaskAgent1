package TaskAgent;

import Logic.WindowsOpener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	
    private static Connection conn;
    private static String url = "jdbc:mysql://sql7.freesqldatabase.com:3306/";
    private static String dbName = "sql7114809";
    private static String user = "sql7114809";
    private static String pass = "se4Ag7WSZ3";
    private static String parameters = "?useSSL=false&useUnicode=true&characterEncoding=UTF-8";
//parametry do łączenia z bazą danych
    private static PreparedStatement preparedStatement = null;

    public DBConnection() throws SQLException {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        }
        conn = DriverManager.getConnection(url + dbName + parameters, user, pass);
    }//metoda połączeniowa do bazt dancych

    public static ResultSet Query(String query) {
        ResultSet resultSet = null;
        try {
            preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }//metoda wykonująca zapytania typu select
    
    public static void Execute(String query) {
        try {
            conn.createStatement().execute(query);
        } catch (SQLException ex) {
            WindowsOpener.alert("Błąd", "Nie udało się wykonać zapytania!" + ex.getMessage());
        }
    }//metoda wykonująca zapytania

    
    
    public static void editTask(int id, String name, String desc) {
        Execute("UPDATE tasks SET name = \""+name+"\", description = \""+desc+"\" WHERE id = "+id);
        WindowsOpener.open("/TaskAgent/FXMLTasks.fxml", "Tasks", true);
    }// metoda służąca do edycji zadań
}