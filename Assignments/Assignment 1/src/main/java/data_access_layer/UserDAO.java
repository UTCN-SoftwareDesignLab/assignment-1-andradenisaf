package data_access_layer;

import model.User;
import model.UserRole;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {
    private Connection connection;

    public UserDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    private User getUserFromStatementExecution(PreparedStatement preStmt) {
        try {
            ResultSet result = preStmt.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String fullname = result.getString("fullname");
                String username1 = result.getString("username");
                String password = result.getString("password");
                String address = result.getString("address");
                String email = result.getString("email");
                String role = result.getString("role");
                UserRole userRole = null;
                if (role.equalsIgnoreCase("ADMINISTRATOR")) {
                    userRole = UserRole.ADMINISTRATOR;
                } else {
                    userRole = UserRole.EMPLOYEE;
                }
                User u = new User(fullname, username1, password, address, email, userRole);
                u.setId(id);

                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(User user) {

        //query the database using parameters (?)
        try {
            PreparedStatement preStmt = connection.prepareStatement("insert into users values (id,?,?,?,?,?,?)");
            System.out.println("uaerDAO - PREP STATEMENTS" + preStmt);

            //set the statement's parameters
            preStmt.setString(1, user.getFullname());
            preStmt.setString(2, user.getUsername());
            preStmt.setString(3, user.getPassword());
            preStmt.setString(4, user.getEmail());
            preStmt.setString(5, user.getAddress());
            preStmt.setString(6, user.getRole().toString());

            //execute the query
            preStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public boolean delete(String username) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("delete from users where username=?");
            preStmt.setString(1, username);
            preStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("UPDATE users SET " +
                    "fullname=?, username=?,password=?,email=?,address=?,role=?" +
                    "WHERE id = ?");
            preStmt.setString(1, user.getFullname());
            preStmt.setString(2, user.getUsername());
            preStmt.setString(3, user.getPassword());
            preStmt.setString(4, user.getEmail());
            preStmt.setString(5, user.getAddress());
            preStmt.setString(6, user.getRole().toString());
            preStmt.setInt(7, user.getId());


            int result = preStmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from users where username=?");
            preStmt.setString(1, username);
            return this.getUserFromStatementExecution(preStmt);
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public List<User> getUsers() {

        List<User> users = new ArrayList<User>();
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from users");
            try {
                ResultSet result = preStmt.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    String fullname = result.getString("fullname");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    String address = result.getString("address");
                    String email = result.getString("email");
                    String role = result.getString("role");
                    UserRole userRole = null;
                    if (role.equalsIgnoreCase("ADMINISTRATOR")) {
                        userRole = UserRole.ADMINISTRATOR;
                    } else {
                        userRole = UserRole.EMPLOYEE;
                    }
                    User user = new User(fullname, username, password, address, email, userRole);
                    user.setId(id);

                    users.add(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return users;
    }

    public static void main(String[] args)
    {
        UserDAO ud = new UserDAO();
        System.out.println(ud.save(new User("Farcas Andrada Deni$@", "andrada123", "pass123", "andrada@mail.com", "Wonderland", UserRole.ADMINISTRATOR )));

    }
}
