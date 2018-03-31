package data_access_layer;


import model.Account;
import model.AccountType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements IAccountDAO {

    private Connection connection;

    public AccountDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    private Account getAccountFromStatementExecution(PreparedStatement preStmt) {
        try {
            ResultSet result = preStmt.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String type = result.getString("account_type");
                AccountType accountType = null;
                if (type.equalsIgnoreCase("SAVINGS")) {
                    accountType = AccountType.SAVINGS;
                } else {
                    accountType = AccountType.SPENDING;
                }
                float amount = result.getFloat("amount");
                Date creationDate = result.getDate("creationDate");

                Account acc = new Account(accountType, amount, creationDate);
                acc.setId(id);

                return acc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(Account account) {

        //query the database using parameters (?)
        try {
            PreparedStatement preStmt = connection.prepareStatement("insert into bank_account values (id,?,?,?)");
            //set the statement's parameters
            preStmt.setString(1, account.getType().toString());
            preStmt.setFloat(2, account.getAmount());
            preStmt.setDate(3, account.getCreationDate());

            //execute the query
            preStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("delete from bank_account where id=?");
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public boolean update(Account account) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("UPDATE bank_account SET " +
                    "account_type=?, amount=?,creationDate=?" +
                    "WHERE id = ?");
            //set the statement's parameters
            preStmt.setString(1, account.getType().toString());
            preStmt.setFloat(2, account.getAmount());
            preStmt.setDate(3, account.getCreationDate());
            preStmt.setInt(4, account.getId());

            int result = preStmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public Account getAccountById(int id) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from bank_account where id=?");
            preStmt.setInt(1, id);
            return this.getAccountFromStatementExecution(preStmt);
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public List<Account> getAccounts() {

        List<Account> accounts = new ArrayList<Account>();
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from bank_account");
            try {
                ResultSet result = preStmt.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    String type = result.getString("account_type");
                    AccountType accountType = null;
                    if (type.equalsIgnoreCase("SAVINGS")) {
                        accountType = AccountType.SAVINGS;
                    } else {
                        accountType = AccountType.SPENDING;
                    }
                    float amount = result.getFloat("amount");
                    Date creationDate = result.getDate("creationDate");
                    Account account = new Account(accountType, amount, creationDate);
                    account.setId(id);

                    accounts.add(account);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return accounts;
    }

}
