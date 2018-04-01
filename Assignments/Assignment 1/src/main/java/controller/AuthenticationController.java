package controller;

import exceptions.InexistentUserException;
import exceptions.InvalidPasswordException;
import exceptions.UsernameAlreadyExistsException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import model.UserRole;
import service.IAdministratorService;
import service.IGeneralService;
import javafx.fxml.FXML;


public class AuthenticationController extends AlertController {


    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField fullnameTextField;

    @FXML
    private TextField username1TextField;

    @FXML
    private PasswordField password1Field;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextArea addressTextArea;

    @FXML
    private TextField emailTextField;

    private Stage signUpStage;

    private Stage administratorStage;

    private Stage employeeStage;

    IGeneralService generalService;

    public AuthenticationController(IGeneralService generalService) {

        this.generalService = generalService;

        initComponents();
    }

    public void initComponents() {
        usernameTextField = new TextField();
        passwordField = new PasswordField();
        fullnameTextField = new TextField();
        username1TextField = new TextField();
        password1Field = new PasswordField();
        confirmPasswordField = new PasswordField();
        addressTextArea = new TextArea();
        emailTextField = new TextField();
        signUpStage = new Stage();
        administratorStage = new Stage();
        employeeStage = new Stage();

    }

    public void logIn() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (username.equals("")) {
            displayErrorBox("Username not provided", "Please enter a valid username");
            return;
        }


        if (password.equals("")) {
            displayErrorBox("Password not provided", "Please enter your password");
            return;
        }

        try {
            User user = generalService.logIn(username, password);
            if (user.getRole() == UserRole.ADMINISTRATOR) {
                switchToAdministratorView(user.getUsername());
            } else {
                //redirect to client page
            }
            displayInformationBox("SUCCESSFULL LOGIN", "You are logged in as " + user.getUsername());
        } catch (InexistentUserException e1) {
            displayErrorBox("Invalid username", "Please enter a valid username");
        } catch (InvalidPasswordException e) {
            displayErrorBox("Invalid password", "Please enter a valid password");

        }

    }

    public void switchToSignUpView() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signUpView.fxml"));


            loader.setController(this);
            signUpStage.setTitle("Sign up");

            Parent root = loader.load();
            signUpStage.setScene(new Scene(root, 500, 500));
            signUpStage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            displayErrorBox("RIP", "Something went wrong while switching views");
        }
    }

    public void signUp() {
        String fullname = fullnameTextField.getText();
        String username = username1TextField.getText();
        String password = password1Field.getText();
        String confirmPassword = confirmPasswordField.getText();
        String address = addressTextArea.getText();
        String email = emailTextField.getText();

        if (fullname.equals("")) {
            displayErrorBox("Full name not provided", "Please enter a fullname");
            return;
        }

        if (username.equals("")){
            displayErrorBox("Username not provided", "Please enter a username");
            return;
        }

        if (password.equals("")){
            displayErrorBox("Password not provided", "Please enter a password");
            return;
        }

        if (confirmPassword.equals("")){
            displayErrorBox("Password not confirmed", "Please reenter the password");
            return;
        }

        if (!password.equals(confirmPassword)){
            displayErrorBox("Password mismatch", "Passwords do not match");
            return;
        }

        if (address.equals("")){
            displayErrorBox("Address not provided", "Please enter an address");
            return;
        }

        if (email.equals("")){
            displayErrorBox("Email not provided", "Please enter an email address");
            return;
        }

        try {
            User user = new User(fullname, username, password, email, address, UserRole.EMPLOYEE);
            User signedUpUser = generalService.signUp(user);
            signUpStage.close();
        } catch (UsernameAlreadyExistsException e) {
            displayErrorBox("Username already exists", "Please select a new username");
        }

    }


    public void switchToAdministratorView(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adminMainView.fxml"));

            ComponentFactory componentFactory = ComponentFactory.instance();
            IAdministratorService administratorService = componentFactory.getAdministratorService();
            AdministratorController administratorController = new AdministratorController(username, administratorService);

            loader.setController(administratorController);
            administratorStage.setTitle("Administrator page");

            Parent root = loader.load();
            administratorStage.setScene(new Scene(root, 1200, 630));
            administratorStage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            displayErrorBox("RIP", "Something went wrong while switching views");
        }
    }

}
