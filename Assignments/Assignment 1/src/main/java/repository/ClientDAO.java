package repository;

import database.DatabaseConnection;
import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ClientDAO implements IClientDAO {

    private Connection connection;

    public ClientDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    private Client getClientFromStatementExecution(PreparedStatement preStmt) {
        try {
            ResultSet result = preStmt.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String fullname = result.getString("fullname");
                String CNP = result.getString("CNP");
                int identityCardNo = result.getInt("id_card_no");
                String address = result.getString("address");
                String email = result.getString("email");
                Client c = new Client(fullname, CNP, identityCardNo, address, email);
                c.setId(id);

                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(Client client) {

        //query the database using parameters (?)
        try {
            PreparedStatement preStmt = connection.prepareStatement("insert into client values (id,?,?,?,?,?)");
            //set the statement's parameters
            preStmt.setString(1, client.getFullname());
            preStmt.setString(2, client.getCNP());
            preStmt.setInt(3, client.getIdentityCardNo());
            preStmt.setString(4, client.getAddress());
            preStmt.setString(5, client.getEmail());

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
            PreparedStatement preStmt = connection.prepareStatement("delete from client where id=?");
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public boolean update(Client client) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("UPDATE client SET " +
                    "fullname=?, CNP=?,idCardNo=?,address=?,email=?" +
                    "WHERE id = ?");
            preStmt.setString(1, client.getFullname());
            preStmt.setString(2, client.getCNP());
            preStmt.setInt(3, client.getIdentityCardNo());
            preStmt.setString(4, client.getAddress());
            preStmt.setString(5, client.getEmail());
            preStmt.setInt(6, client.getId());


            int result = preStmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public Client getClientByCNP(String CNP) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from client where username=?");
            preStmt.setString(1, CNP);
            return this.getClientFromStatementExecution(preStmt);
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public List<Client> getClients() {

        List<Client> clients = new ArrayList<Client>();
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from client");
            try {
                ResultSet result = preStmt.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    String fullname = result.getString("fullname");
                    String CNP = result.getString("CNP");
                    int identityCardNo = result.getInt("id_card_no");
                    String address = result.getString("address");
                    String email = result.getString("email");
                    Client client = new Client(fullname, CNP, identityCardNo, address, email);
                    client.setId(id);

                    clients.add(client);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return clients;
    }
}
