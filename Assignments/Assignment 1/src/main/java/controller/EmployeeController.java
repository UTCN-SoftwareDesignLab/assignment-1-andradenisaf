package controller;

import exceptions.DuplicateClientException;
import exceptions.IllegalAmountException;
import exceptions.InexistentAccountException;
import exceptions.InexistentClientException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import service.IEmployeeService;

import javax.rmi.CORBA.Util;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class EmployeeController extends AlertController {

    private IEmployeeService employeeService;
    private String loggedInUsername;

    @FXML
    private TableView<Client> clientsTableView;
    @FXML
    private TableColumn<Client, Integer> clientId;
    @FXML
    private TableColumn<Client, String> fullname;
    @FXML
    private TableColumn<Client, String> CNP;
    @FXML
    private TableColumn<Client, Integer> identityCardNo;
    @FXML
    private TableColumn<Client, String> address;
    @FXML
    private TableColumn<Client, String> email;

    @FXML
    private TextField fullnameTextField;
    @FXML
    private TextField cnpTextField;
    @FXML
    private TextField identityCardNoTextField;
    @FXML
    private TextArea addressTextArea;
    @FXML
    private TextField emailTextField;

    @FXML
    private TableView<Account> bankAccountsTableView;
    @FXML
    private TableColumn<Account, Integer> accountId;
    @FXML
    private TableColumn<Account, Integer> clientId1;
    @FXML
    private TableColumn<Account, String> accountType;
    @FXML
    private TableColumn<Account, Float> amount;
    @FXML
    private TableColumn<Account, Date> creationDate;

    @FXML
    private TextField accountIdTextField;

    @FXML
    private TextField amountTextField;
    @FXML
    private TextField clientIdTextField;
    @FXML
    private ChoiceBox accountTypeChoiceBox;

    @FXML
    private TextField senderIdTextField;

    @FXML
    private TextField receiverIdTextField;

    @FXML
    private TextField getAmountTextField;


    @FXML
    private TableView<UtilityBill> utilityBillTableView;
    @FXML
    private TableColumn<UtilityBill, Integer> utilityBillid;
    @FXML
    private TableColumn<UtilityBill, String> billType;
    @FXML
    private TableColumn<UtilityBill, Integer> clientId2;
    @FXML
    private TableColumn<UtilityBill, Float> amount2;
    @FXML
    private TableColumn<UtilityBill, Integer> paid;


    @FXML
    private TextField utilityBillIdTextField;

    @FXML
    private TextField clientIdTextField2;

    @FXML
    private TextField amountTextField2;

    @FXML
    private TextField paidTextField;

    @FXML
    private TextField accountIdTextField2;

    @FXML
    private ChoiceBox billTypeChoiceBox;

    private ObservableList<String> billTypeList = FXCollections.observableArrayList("INTERNET_BILL", "PHONE_BILL", "ELECTRICITY_BILL", "WATER_AND_SEWER_BILL");


    private Stage transferStage;

    private Stage processBillStage;

    private ObservableList<String> accountTypesList = FXCollections.observableArrayList("SAVINGS", "SPENDING");

    public EmployeeController(String loggedInUsername, IEmployeeService employeeService) {

        this.employeeService = employeeService;
        this.loggedInUsername = loggedInUsername;
        initComponents();
    }

    @FXML
    public void initComponents() {
        clientsTableView = new TableView<>();
        clientId = new TableColumn<>();
        fullname = new TableColumn<>();
        CNP = new TableColumn<>();
        identityCardNo = new TableColumn<>();
        address = new TableColumn<>();
        email = new TableColumn<>();

        fullnameTextField = new TextField();
        cnpTextField = new TextField();
        identityCardNoTextField = new TextField();
        addressTextArea = new TextArea();
        emailTextField = new TextField();

        bankAccountsTableView = new TableView<>();
        accountId = new TableColumn<>();
        clientId1 = new TableColumn<>();
        identityCardNo = new TableColumn<>();
        accountType = new TableColumn<>();
        amount = new TableColumn<>();
        creationDate = new TableColumn<>();

        accountIdTextField = new TextField();
        amountTextField = new TextField();
        clientIdTextField = new TextField();
        accountTypeChoiceBox = new ChoiceBox();

        senderIdTextField = new TextField();
        receiverIdTextField = new TextField();
        amountTextField = new TextField();

        utilityBillTableView = new TableView<>();

        utilityBillid = new TableColumn<>();
        billType = new TableColumn<>();
        clientId2 = new TableColumn<>();
        amount2 = new TableColumn<>();
        paid = new TableColumn<>();

        billTypeChoiceBox = new ChoiceBox();

        utilityBillIdTextField = new TextField();
        billTypeChoiceBox = new ChoiceBox();
        clientIdTextField2 = new TextField();
        amountTextField2 = new TextField();
        paidTextField = new TextField();

        transferStage = new Stage();
        processBillStage = new Stage();

    }

    @FXML
    public void initClients() {
        clientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullname.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        CNP.setCellValueFactory(new PropertyValueFactory<>("CNP"));
        identityCardNo.setCellValueFactory(new PropertyValueFactory<>("identityCardNo"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));


        List<Client> clientsList = employeeService.getClients();

        clientsTableView.getItems().setAll(clientsList);

        clientsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (clientsTableView.getSelectionModel().getSelectedItem() != null) {
                    Client client = clientsTableView.getSelectionModel().getSelectedItem();

                    fullnameTextField.setText(client.getFullname());
                    cnpTextField.setText(client.getCNP());
                    identityCardNoTextField.setText(Integer.toString(client.getIdentityCardNo()));
                    addressTextArea.setText(client.getAddress());
                    emailTextField.setText(client.getEmail());
                }
            }
        });
    }

    @FXML
    public void initAccounts() {

        accountId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clientId1.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        accountType.setCellValueFactory(new PropertyValueFactory<>("type"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        creationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));


        List<Account> accountsList = employeeService.getBankAccounts();

        bankAccountsTableView.getItems().setAll(accountsList);


        bankAccountsTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (bankAccountsTableView.getSelectionModel().getSelectedItem() != null) {
                    Account account = bankAccountsTableView.getSelectionModel().getSelectedItem();
                    System.out.println(account);

                    accountIdTextField.setText(Integer.toString(account.getId()));
                    clientIdTextField.setText(Integer.toString(account.getClientId()));
                    amountTextField.setText(Float.toString(account.getAmount()));
                    accountTypeChoiceBox.setValue(account.getType());
                }
            }
        });

    }

    @FXML
    public void initBills() {

        utilityBillid.setCellValueFactory(new PropertyValueFactory<>("id"));
        billType.setCellValueFactory(new PropertyValueFactory<>("type"));
        clientId2.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        amount2.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paid.setCellValueFactory(new PropertyValueFactory<>("paid"));


        List<UtilityBill> billsList = employeeService.getUtilityBills();

        utilityBillTableView.getItems().setAll(billsList);

        utilityBillTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                if (utilityBillTableView.getSelectionModel().getSelectedItem() != null) {
                    UtilityBill bill = utilityBillTableView.getSelectionModel().getSelectedItem();

                    utilityBillIdTextField.setText(Integer.toString(bill.getId()));
                    clientIdTextField2.setText(Integer.toString(bill.getClientId()));
                    billTypeChoiceBox.setValue(String.valueOf(bill.getType()));
                    amountTextField2.setText(Float.toString(bill.getAmount()));
                    paidTextField.setText(Boolean.toString(bill.isPaid()));
                }
            }
        });
    }

    @FXML
    public void initialize() {
        accountTypeChoiceBox.setValue("SPENDING");
        accountTypeChoiceBox.setItems(accountTypesList);
        billTypeChoiceBox.setValue("PHONE_BILL");
        billTypeChoiceBox.setItems(billTypeList);
        initClients();
        initAccounts();
        initBills();

    }

    public void addClient() {
        String fullname = fullnameTextField.getText();
        String CNP = cnpTextField.getText();
        int identityCardNo = Integer.parseInt(identityCardNoTextField.getText());
        String address = addressTextArea.getText();
        String email = emailTextField.getText();

        if (!validateClient(fullname, CNP, identityCardNo, address, email)) {
            return;
        }

        try {
            employeeService.addClientInformation(loggedInUsername, new Client(fullname, CNP, identityCardNo, address, email));
            initClients();
        } catch (DuplicateClientException e) {
            displayErrorBox("Duplicate client", "Client is already in the database");
        }
    }

    public void updateClient() {
        String fullname = fullnameTextField.getText();
        String CNP = cnpTextField.getText();
        int identityCardNo = Integer.parseInt(identityCardNoTextField.getText());
        String address = addressTextArea.getText();
        String email = emailTextField.getText();

        if (!validateClient(fullname, CNP, identityCardNo, address, email)) {
            return;
        }

        try {
            Client client = employeeService.getClientInformation(CNP);
            if (client == null) {
                displayErrorBox("Client not found", "Could not find client in database");
                return;
            }
            client.setFullname(fullname);
            client.setIdentityCardNo(identityCardNo);
            client.setAddress(address);
            client.setEmail(email);
            employeeService.updateClientInformation(loggedInUsername, client);
            initClients();
        } catch (InexistentClientException e) {
            displayErrorBox("Client not found", "Could not find client in database");
        }
    }

    public void addAccount() {
        int clientId = Integer.parseInt(clientIdTextField.getText());
        String type = accountType.getText();
        AccountType accountType;
        if (type.equals("SAVINGS"))
            accountType = AccountType.SAVINGS;
        else
            accountType = AccountType.SPENDING;

        float amount = Float.parseFloat(amountTextField.getText());
        Date creationDate = new Date(Calendar.getInstance().getTime().getTime());

        if (!validateAccount(clientId, accountType, amount, creationDate))
            return;

        try {
            Client client = employeeService.getClientInformationById(clientId);
            Account acc = new Account(clientId, accountType, amount, creationDate);
            employeeService.addBankAccountForClient(loggedInUsername, client, acc);
            initAccounts();
        } catch (DuplicateClientException e) {
            displayErrorBox("Duplicate client", "Client already in database");
        } catch (InexistentClientException e) {
            displayErrorBox("Inexistent client", "Could not find client");
        }
    }

    public void updateAccount() {

        int accountId = Integer.parseInt(accountIdTextField.getText());
        int clientId = Integer.parseInt(clientIdTextField.getText());
        String type = accountType.getText();
        AccountType accountType;
        if (type.equals("SAVINGS"))
            accountType = AccountType.SAVINGS;
        else
            accountType = AccountType.SPENDING;

        float amount = Float.parseFloat(amountTextField.getText());
        Date creationDate = new Date(Calendar.getInstance().getTime().getTime());

        if (!validateAccount(clientId, accountType, amount, creationDate))
            return;

        try {
            Account acc = employeeService.getAccountById(accountId);
            acc.setClientId(clientId);
            acc.setType(accountType);
            acc.setAmount(amount);
            employeeService.updateAccount(loggedInUsername, acc);
            initAccounts();

        } catch (InexistentAccountException e) {
            displayErrorBox("Inexistent account", "Could not find account");
        }
    }

    public void deleteAccount() {
        int accountId = Integer.parseInt(accountIdTextField.getText());
        if (accountId <= 0) {
            displayErrorBox("Invalid id", "Invalid account id");
            return;
        }

        try {
            Account acc = employeeService.getAccountById(accountId);
            employeeService.deleteBankAccount(loggedInUsername, acc);
            initAccounts();
        } catch (InexistentAccountException e) {
            displayErrorBox("Inexistent account", "Could not find account");
        }
    }

    public void switchToTransferView() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transferMoneyView.fxml"));


            loader.setController(this);
            transferStage.setTitle("Transfer money");

            Parent root = loader.load();
            transferStage.setScene(new Scene(root, 300, 300));
            transferStage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            displayErrorBox("RIP", "Something went wrong while switching views");
        }
    }

    public void transferMoney() {
        int senderId = Integer.parseInt(senderIdTextField.getText());
        int receiverId = Integer.parseInt(receiverIdTextField.getText());
        float amount = Float.parseFloat(amountTextField.getText());

        if (amount < 0) {
            displayErrorBox("Illegal amount", "Amount must be a positive number");
            return;
        }
        try {
            employeeService.tranferMoneyBetweenBankAccounts(loggedInUsername, senderId, receiverId, amount);
            displayInformationBox("Succesfull transaction", "Transfer succeeded");
            transferStage.close();
            initAccounts();
        } catch (InexistentAccountException e) {
            displayErrorBox("Inexistent account", "One of the accounts could not be found");

        } catch (IllegalAmountException e1) {
            displayErrorBox("Illegal amount", "Insufficient funds");
        }
    }

    public void payBill()
    {
        int accountId = Integer.parseInt(accountIdTextField2.getText());
        int utilityBillId = Integer.parseInt(utilityBillIdTextField.getText());
        String billType1 = billType.getText();
        UtilityBillType utilityBillType;
        if(billType1.equals("PHONE_BILL"))
            utilityBillType = UtilityBillType.PHONE_BILL;

        if(billType1.equals("INTERNET_BILL"))
            utilityBillType = UtilityBillType.INTERNET_BILL;

        if(billType1.equals("ELECTRICITY_BILL"))
            utilityBillType = UtilityBillType.ELECTRICITY_BILL;

        if(billType1.equals("WATER_AND_SEWER_BILL"))
            utilityBillType = UtilityBillType.WATER_AND_SEWER_BILL;

        int clinetId = Integer.parseInt(clientIdTextField2.getText());

        float amount = Float.parseFloat(amountTextField2.getText());

        boolean paid = Boolean.parseBoolean(paidTextField.getText());

        UtilityBill ub = employeeService.getUtilityBill(utilityBillId);
        if(ub == null)
        {
            displayErrorBox("Invalid bill", "Could not find bill");
            return;
        }

        try {
            ub.setPaid(true);
            employeeService.processUtilitiesBill(loggedInUsername, accountId, ub);
            processBillStage.close();
            initBills();
            initAccounts();
        }
        catch (Exception e)
        {
            displayErrorBox("Error processing bill", "Something went wrong :(");
        }

    }

    public void switchToProcessBillView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/processBillsView.fxml"));


            loader.setController(this);
            processBillStage.setTitle("Process bills");

            Parent root = loader.load();
            processBillStage.setScene(new Scene(root, 700, 500));
            processBillStage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            displayErrorBox("RIP", "Something went wrong while switching views");
        }
    }

    public boolean validateClient(String fulllname, String CNP, int idCardNumber, String address, String email) {
        if (fullname.equals("")) {
            displayErrorBox("Full name not provided", "Please enter a fullname");
            return false;
        }

        if (CNP.equals("")) {
            displayErrorBox("CNP not provided", "Please enter a CNP");
            return false;
        }

        if (idCardNumber == 0) {
            displayErrorBox("Identity card number not provided", "Please enter an identity card number");
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

    public boolean validateAccount(int clientId, AccountType accountType, float amount, Date creationDate) {
        if (clientId == 0) {
            displayErrorBox("Client Id not provided", "Please enter a client Id");
            return false;
        }

        if (accountType == null) {
            displayErrorBox("Account type not provided", "Please enter the account type");
            return false;
        }


        if (amount == 0) {
            displayErrorBox("Amount not provided", "Please enter an amount");
            return false;
        }

        return true;
    }

}
