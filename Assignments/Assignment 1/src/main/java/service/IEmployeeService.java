package service;


import exceptions.*;
import model.Account;
import model.Client;
import model.UtilityBill;

import java.util.List;

public interface IEmployeeService extends IGeneralService {
    //CLIENT MANIPULATION
    public List<Client> getClients();

    public Client getClientInformation(String cnp) throws InexistentClientException;

    public Client getClientInformationById(int id) throws InexistentClientException;

    public Client addClientInformation(String employeeUsername, Client client)
            throws DuplicateClientException;

    public Client updateClientInformation(String employeeUsername, Client client)
            throws InexistentClientException;

    //BANK ACCOUNT MANIPULATION
    public List<Account> getBankAccounts();

    public List<Account> getBankAccountsForClient(String cnp) throws InexistentClientException;

    public Account addBankAccountForClient(String employeeUsername, Client client, Account account) throws DuplicateClientException;

    public boolean deleteBankAccount(String employeeUsername, Account account) throws InexistentAccountException;

    public Account updateAccount(String employeeUsername, Account account) throws InexistentAccountException;

    public Account getAccountById(int id) throws InexistentAccountException;

    //MONEY TRANSFER
    public boolean tranferMoneyBetweenBankAccounts(String employeeUsername, int senderAccountId, int receiverAccountId,
                                                   float amountOfMoney) throws InexistentAccountException, IllegalAmountException;

    //UTILITY BILLS
    public UtilityBill getUtilityBill(int billId);

    public List<UtilityBill> getUtilityBills();

    public UtilityBill processUtilitiesBill(String employeeUsername, int accountId, UtilityBill utilityBill)
            throws InexistentAccountException, IllegalAmountException, InexistentBillException, BillAlreadyPaidException;

    public UtilityBill addBill(String employeeUsername, UtilityBill ub);
}
