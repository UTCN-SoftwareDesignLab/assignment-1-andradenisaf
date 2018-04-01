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
import service.IGeneralService;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;


public class AuthenticationController {


    @FXML private TextField usernameTextField;

    @FXML private PasswordField passwordField;

    @FXML private TextField fullnameTextField;

    @FXML private TextField username1TextField;

    @FXML private PasswordField password1Field;

    @FXML private PasswordField confirmPasswordField;

    @FXML private TextArea addressTextArea;

    @FXML private TextField emailTextField;

    private Stage signUpStage;

    IGeneralService generalService;

    public AuthenticationController() {
        initComponents();
    }

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

    }

    public void logIn() {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (username.equals(""))
            displayErrorBox("Username not provided", "Please enter a valid username");


        if (password.equals(""))
            displayErrorBox("Password not provided", "Please enter your password");

        try {
            User user = generalService.logIn(username, password);
            if (user.getRole() == UserRole.ADMINISTRATOR) {
                //redirect to admin page
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

    public void switchToSignUpView()
    {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signUpView.fxml"));


            loader.setController(this);
            signUpStage.setTitle("Sign up");

            Parent root = loader.load();
            signUpStage.setScene(new Scene(root, 500, 500));
            signUpStage.show();
        }
        catch(java.io.IOException e)
        {
            e.printStackTrace();
            displayErrorBox("RIP", "Something went wrong while switching views");
        }
    }

    public void signUp()
    {
        String fullname = fullnameTextField.getText();
        String username = username1TextField.getText();
        String password = password1Field.getText();
        String confirmPassword = confirmPasswordField.getText();
        String address = addressTextArea.getText();
        String email = emailTextField.getText();

        if (fullname.equals(""))
            displayErrorBox("Full name not provided", "Please enter a fullname");

        if (username.equals(""))
            displayErrorBox("Username not provided", "Please enter a username");

        if (password.equals(""))
            displayErrorBox("Password not provided", "Please enter a password");

        if (confirmPassword.equals(""))
            displayErrorBox("Password not confirmed", "Please reenter the password");

        if(!password.equals(confirmPassword))
            displayErrorBox("Password mismatch", "Passwords do not match");

        if (address.equals(""))
            displayErrorBox("Address not provided", "Please enter an address");

        if (email.equals(""))
            displayErrorBox("Email not provided", "Please enter an email address");

        try
        {
            User user = new User(fullname,username,password,email,address,UserRole.EMPLOYEE);
            User signedUpUser = generalService.signUp(user);
            signUpStage.close();
        }
        catch(UsernameAlreadyExistsException e)
        {
            displayErrorBox("Username already exists", "Please select a new username");
        }

    }


    public void displayErrorBox(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void displayInformationBox(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
