package data_access_layer;

import model.Account;

import java.util.List;

public interface IAccountDAO {

    public boolean save(Account account);

    public boolean delete(int id);

    public boolean update(Account account);

    public Account getAccountById(int id);

    public List<Account> getAccounts();
}
