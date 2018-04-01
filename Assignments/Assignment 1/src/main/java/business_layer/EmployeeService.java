package business_layer;


import data_access_layer.*;
import exceptions.*;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class EmployeeService extends GeneralService implements IEmployeeService {

    private IClientDAO clientDao;
    private IAccountDAO accountDao;
    private IActivityLogDAO activityLogDao;
    private IUtilityBillDAO utilityBillDao;

    public EmployeeService(IUserDAO userDao, IClientDAO clientDao, IAccountDAO accountDao, IActivityLogDAO activityLogDao, IUtilityBillDAO utilityBillDao) {
        super(userDao);
        this.clientDao = clientDao;
        this.accountDao = accountDao;
        this.activityLogDao = activityLogDao;
        this.utilityBillDao = utilityBillDao;
    }

    private void saveActivityLog(String employeeUsername, String description, IModel obj) {
        User emp = userDao.getUserByUsername(employeeUsername);
        LocalDateTime time = LocalDateTime.now();
        activityLogDao.save(new ActivityLog(emp.getId(), time + ": " + description + " " + obj));
    }

    @Override
    public List<Client> getClients() {
        return clientDao.getClients();
    }

    @Override
    public Client getClientInformation(String cnp) throws InexistentClientException {
        Client client = clientDao.getClientByCNP(cnp);
        if (client == null)
            throw new InexistentClientException("Inexistent client");
        return client;
    }

    @Override
    public Client addClientInformation(String employeeUsername, Client client) throws DuplicateClientException {
        Client c = clientDao.getClientByCNP(client.getCNP());
        if (c != null)
            throw new DuplicateClientException("Client already exists");

        boolean saved = clientDao.save(client);
        Client savedClient = null;
        if (saved)
            savedClient = clientDao.getClientByCNP(client.getCNP());

        if (savedClient != null) {
            saveActivityLog(employeeUsername, "Saved client information", savedClient);
        }
        return savedClient;
    }

    @Override
    public Client updateClientInformation(String employeeUsername, Client client) throws InexistentClientException {
        Client c = clientDao.getClientByCNP(client.getCNP());
        if (c == null) {
            throw new InexistentClientException("Inexistent client");
        }
        client.setId(c.getId());

        boolean updated = clientDao.update(client);
        if (updated)
            saveActivityLog(employeeUsername, "Updated client information", client);
        return client;
    }

    @Override
    public List<Account> getBankAccounts() {
        return accountDao.getAccounts();
    }

    @Override
    public List<Account> getBankAccountsForClient(String cnp) throws InexistentClientException {
        Client client = clientDao.getClientByCNP(cnp);

        if (client == null)
            throw new InexistentClientException("Could not find client");

        List<Account> accounts = accountDao.getAccounts();
        List<Account> clientAccounts = new ArrayList<>();
        if (accounts != null) {
            for (Account account : accounts) {
                if (account.getClientId() == client.getId())
                    clientAccounts.add(account);
            }
        }

        return clientAccounts;
    }


    @Override
    public Account addBankAccountForClient(String employeeUsername, Client client, Account account) throws DuplicateClientException {

        Client c = clientDao.getClientByCNP(client.getCNP());
        if (c == null) {
            this.addClientInformation(employeeUsername, client);
        }

        boolean saved = accountDao.save(account);
        Account savedAccount = null;
        if (saved)
            savedAccount = accountDao.getAccountById(account.getId());

        if (savedAccount != null) {
            saveActivityLog(employeeUsername, "Added bank account", savedAccount);
        }

        return savedAccount;
    }


    @Override
    public boolean deleteBankAccount(String employeeUsername, Account account) throws InexistentAccountException {
        Account dbAccount = accountDao.getAccountById(account.getId());
        if (dbAccount == null) {
            throw new InexistentAccountException("Could not find bank account");
        }

        boolean deleted = accountDao.delete(dbAccount.getId());
        if (deleted) {
            saveActivityLog(employeeUsername, "Deleted account", dbAccount);
        }
        return deleted;
    }

    @Override
    public Account updateAccount(String employeeUsername, Account account) throws InexistentAccountException {
        Account dbAccount = accountDao.getAccountById(account.getId());
        if (dbAccount == null) {
            throw new InexistentAccountException("Could not find bank account");
        }

        boolean updated = accountDao.update(account);

        if (updated) {
            saveActivityLog(employeeUsername, "Updated account", account);
        }

        return account;
    }


    @Override
    public boolean tranferMoneyBetweenBankAccounts(String employeeUsername, int senderAccountId, int receiverAccountId, float amountOfMoney) throws InexistentAccountException, IllegalAmountException {
        Account senderAccount = accountDao.getAccountById(senderAccountId);
        if (senderAccount == null) {
            throw new InexistentAccountException("Sender account could not be found");
        }

        Account receiverAccount = accountDao.getAccountById(receiverAccountId);
        if (receiverAccount == null) {
            throw new InexistentAccountException("Receiver account could not be found");
        }

        if (senderAccount.getAmount() < amountOfMoney)
            throw new IllegalAmountException("The sender does not have the necessary amount of money in the account. Current balance: " + senderAccount.getAmount());

        senderAccount.setAmount(senderAccount.getAmount() - amountOfMoney);
        receiverAccount.setAmount(receiverAccount.getAmount() + amountOfMoney);

        boolean sent = accountDao.update(senderAccount);
        boolean received = accountDao.update(receiverAccount);

        if (sent && received)
            saveActivityLog(employeeUsername, "Transferred " + amountOfMoney + "from account " + senderAccountId + " to account " + receiverAccount, null);

        return sent && received;
    }

    @Override
    public UtilityBill getUtilityBill(int billId) {
        return utilityBillDao.getUtilityBillById(billId);
    }

    @Override
    public List<UtilityBill> getUtilityBills() {
        return utilityBillDao.getUtilityBills();
    }

    @Override
    public UtilityBill processUtilitiesBill(String employeeUsername, int accountId, UtilityBill utilityBill) throws InexistentAccountException, IllegalAmountException, InexistentBillException, BillAlreadyPaidException {

        Account account = accountDao.getAccountById(accountId);

        if (account == null)
            throw new InexistentAccountException("Inexistent account");

        UtilityBill dbUtilityBill = utilityBillDao.getUtilityBillById(utilityBill.getId());
        if (dbUtilityBill == null)
            throw new InexistentBillException("Cannot find bill");

        if (dbUtilityBill.isPaid())
            throw new BillAlreadyPaidException("Bill was already paid");

        if (account.getAmount() < utilityBill.getAmount())
            throw new IllegalAmountException("Insufficient funds. Current balance: " + account.getAmount());

        account.setAmount(account.getAmount() - utilityBill.getAmount());
        utilityBill.setPaid(true);

        boolean updatedAccount = accountDao.update(account);
        boolean updatedBill = utilityBillDao.update(utilityBill);

        if (updatedAccount && updatedBill) {
            saveActivityLog(employeeUsername, "Accepted " + utilityBill.getType() + " payment ( " + utilityBill.getAmount() + "$) from account " + accountId, utilityBill);
        }

        return utilityBill;
    }

    @Override
    public UtilityBill addBill(String employeeUsername, UtilityBill ub) {

        boolean saved = utilityBillDao.save(ub);
        UtilityBill addedBill = null;
        if (saved) {
            addedBill = utilityBillDao.getUtilityBillById(ub.getId());
        }

        if (addedBill != null)
            saveActivityLog(employeeUsername, "Added utility bill", addedBill);

        return addedBill;
    }
}
