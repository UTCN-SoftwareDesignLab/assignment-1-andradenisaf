package repository;

import model.Client;

import java.util.List;

public interface IClientDAO {

    public boolean save(Client client);

    public boolean delete(int id);

    public boolean update(Client client);

    public Client getClientByCNP(String CNP);

    public List<Client> getClients();
}
