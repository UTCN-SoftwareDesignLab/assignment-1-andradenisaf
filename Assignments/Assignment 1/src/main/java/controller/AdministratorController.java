package controller;

import exceptions.InexistentUserException;
import exceptions.InvalidPasswordException;
import exceptions.UsernameAlreadyExistsException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.ActivityLog;
import model.User;
import model.UserRole;
import service.IAdministratorService;

import java.util.List;


public class AdministratorController extends AlertController {

    @FXML
    private TableView<User> employeeTableView;
    @FXML
    private TableColumn<User, Integer> employeeId;
    @FXML
    private TableColumn<User, String> fullname;
    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TableColumn<User, String> password;
    @FXML
    private TableColumn<User, String> address;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private TableColumn<User, UserRole> role;


    @FXML
    private TextField fullnameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextArea addressTextArea;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;


    private String loggedInUsername;

    private Stage changePasswordStage;


    private IAdministratorService administratorService;

    public AdministratorController(String loggedInUsername, IAdministratorService administratorService) {

        this.administratorService = administratorService;
        this.loggedInUsername = loggedInUsername;
        initComponents();
    }


    @FXML
    public void initComponents() {
        employeeTableView = new TableView<>();
        employeeId = new TableColumn<>();
        fullname = new TableColumn<>();
        username = new TableColumn<>();
        password = new TableColumn<>();
        address = new TableColumn<>();
        email = new TableColumn<>();
        role = new TableColumn<>();

        fullnameTextField = new TextField();
        usernameTextField = new TextField();
        passwordTextField = new TextField();
        addressTextArea = new TextArea();
        emailTextField = new TextField();

        changePasswordStage = new Stage();
        oldPasswordField = new PasswordField();
        newPasswordField = new PasswordField();
        confirmPasswordField = new PasswordField();
    }


    public void initEmployees() {

        employeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));

        List<User> employeesList = administratorService.getEmployees();

        employeeTableView.getItems().setAll(employeesList);

        employeeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (employeeTableView.getSelectionModel().getSelectedItem() != null) {
                    User employee = employeeTableView.getSelectionModel().getSelectedItem();

                    fullnameTextField.setText(employee.getFullname());
                    usernameTextField.setText(employee.getUsername());
                    passwordTextField.setText(employee.getPassword());
                    addressTextArea.setText(employee.getAddress());
                    emailTextField.setText(employee.getEmail());
                }
            }
        });

    }

    @FXML
    public void initialize() {

        initEmployees();
    }



    public void addEmployee() {
        String fullname = fullnameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String address = addressTextArea.getText();
        String email = emailTextField.getText();

        if (!validateUser(fullname, username, password, address, email))
            return;


        try {
            User user = new User(fullname, username, password, email, address, UserRole.EMPLOYEE);
            User savedUser = administratorService.addEmployee(user);
            initEmployees();
            //System.out.print(savedUser);
        } catch (UsernameAlreadyExistsException e) {
            displayErrorBox("Username already exists", "Please select a new username");
        }

    }

    public void updateEmployee() {
        String fullname = fullnameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String address = addressTextArea.getText();
        String email = emailTextField.getText();

        if (!validateUser(fullname, username, password, address, email))
            return;

        try {
            User user = administratorService.getUserByUsername(username);
            if (user == null) {
                displayErrorBox("Inexistent user", "Please select an employee");
                return;

            }
            user.setFullname(fullname);
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setAddress(address);
            administratorService.updateEmployee(user);
            initEmployees();
        } catch (InexistentUserException e) {
            displayErrorBox("Inexistent user", "Please select an employee");
        }

    }

    public void deleteEmployee() {
        String username = usernameTextField.getText();

        if (username.equals("")) {
            displayErrorBox("Invalid username", "Please enter a username");
            return;
        }

        try {
            administratorService.deleteEmployee(username);
            initEmployees();
        } catch (InexistentUserException e) {
            displayErrorBox("Inexistent user", "Could not find user");
        }
    }

    public void generateReport() {
        try {
            administratorService.generateActivityLogForEmployee(usernameTextField.getText());
        }
        catch(InexistentUserException e)
        {
            displayErrorBox("Inexistent user", "Could not find user");
        }
    }

    public void switchToChangePasswordView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/changePasswordView.fxml"));


            loader.setController(this);
            changePasswordStage.setTitle("Change password");

            Parent root = loader.load();
            changePasswordStage.setScene(new Scene(root, 210, 210));
            changePasswordStage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            displayErrorBox("RIP", "Something went wrong while switching views");
        }
    }

    public void changePassword() {
        String oldPassword = oldPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(oldPassword.equals("") || newPassword.equals("") || confirmPassword.equals(""))
        {
            displayErrorBox("Incomplete form", "Please complete all the fields");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            displayErrorBox("Password mismatch", "Passwords do not match");
            return;
        }

        try {
            administratorService.changePassword(loggedInUsername, oldPassword, newPassword);
            displayInformationBox("Success", "Password changed successfully");
            changePasswordStage.close();
        } catch (InexistentUserException e) {
            displayErrorBox("Inexistent user", "Cannot find user");

        } catch (InvalidPasswordException e1) {
            displayErrorBox("Invalid old password", "Please enter a valid password");
        }
    }


    public boolean validateUser(String fullname, String username, String password, String address, String email) {

        if (fullname.equals("")) {
            displayErrorBox("Full name not provided", "Please enter a fullname");
            return false;
        }

        if (username.equals("")) {
            displayErrorBox("Username not provided", "Please enter a username");
            return false;
        }

        if (password.equals("")) {
            displayErrorBox("Password not provided", "Please enter a password");
            return false;
        }

        if (address.equals("")) {
            displayErrorBox("Address not provided", "Please enter an address");
            return false;
        }

        if (email.equals("")) {
            displayErrorBox("Email not provided", "Please enter an email address");
            return false;
        }

        return true;
    }

}
