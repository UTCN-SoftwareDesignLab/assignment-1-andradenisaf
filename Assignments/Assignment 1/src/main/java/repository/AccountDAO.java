package repository;


import database.DatabaseConnection;
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
                Date creationDate = result.getDate("creation_date");
                int clientId = result.getInt("client_id");
                Account acc = new Account(clientId, accountType, amount, creationDate);
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
            PreparedStatement preStmt = connection.prepareStatement("insert into bank_account values (id,?,?,?,?)");
            //set the statement's parameters
            preStmt.setInt(1,account.getClientId());
            preStmt.setString(2, account.getType().toString());
            preStmt.setFloat(3, account.getAmount());
            preStmt.setDate(4, account.getCreationDate());

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
                    "client_id=?, account_type=?, amount=?,creation_date=?" +
                    "WHERE id = ?");
            //set the statement's parameters
            preStmt.setInt(1,account.getClientId());
            preStmt.setString(2, account.getType().toString());
            preStmt.setFloat(3, account.getAmount());
            preStmt.setDate(4, account.getCreationDate());
            preStmt.setInt(5, account.getId());

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
                    Date creationDate = result.getDate("creation_date");
                    int clientId = result.getInt("client_id");
                    Account account = new Account(clientId, accountType, amount, creationDate);
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
