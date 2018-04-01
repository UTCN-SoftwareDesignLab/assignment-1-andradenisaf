package repository;

import database.DatabaseConnection;
import model.UtilityBill;
import model.UtilityBillType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UtilityBillDAO implements IUtilityBillDAO {

    private Connection connection;

    public UtilityBillDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    private UtilityBillType getUtilityBillType(String type) {
        UtilityBillType billType = null;
        switch (type) {
            case "INTERNET_BILL":
                billType = UtilityBillType.INTERNET_BILL;
                break;
            case "PHONE_BILL":
                billType = UtilityBillType.PHONE_BILL;
                break;
            case "ELECTRICITY_BILL":
                billType = UtilityBillType.ELECTRICITY_BILL;
                break;
            case "WATER_AND_SEWER_BILL":
                billType = UtilityBillType.WATER_AND_SEWER_BILL;
                break;
            default:
                break;

        }
        return billType;
    }

    private UtilityBill getUtilityBillFromStatementExecution(PreparedStatement preStmt) {
        try {
            ResultSet result = preStmt.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                String type = result.getString("bill_type");
                UtilityBillType billType = getUtilityBillType(type);
                int clientId = result.getInt("client_id");
                float amount = result.getFloat("amount_to_pay");
                boolean paid = result.getBoolean("paid");

                UtilityBill ub = new UtilityBill(billType, clientId, amount, paid);
                ub.setId(id);

                return ub;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(UtilityBill utilityBill) {

        //query the database using parameters (?)
        try {
            PreparedStatement preStmt = connection.prepareStatement("insert into utility_bill values (id,?,?,?)");
            //set the statement's parameters
            preStmt.setString(1, utilityBill.getType().toString());
            preStmt.setFloat(2, utilityBill.getAmount());
            preStmt.setBoolean(3, utilityBill.isPaid());

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
            PreparedStatement preStmt = connection.prepareStatement("delete from utility_bill where id=?");
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public boolean update(UtilityBill utilityBill) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("UPDATE utility_bill SET " +
                    "bill_type=?, client_id=?,amount_to_pay=?,paid=?" +
                    "WHERE id = ?");
            //set the statement's parameters
            preStmt.setString(1, utilityBill.getType().toString());
            preStmt.setInt(2, utilityBill.getClientId());
            preStmt.setFloat(3, utilityBill.getAmount());
            preStmt.setBoolean(4, utilityBill.isPaid());

            int result = preStmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public UtilityBill getUtilityBillById(int id) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from utility_bill where id=?");
            preStmt.setInt(1, id);
            return this.getUtilityBillFromStatementExecution(preStmt);
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public List<UtilityBill> getUtilityBills() {

        List<UtilityBill> utilityBills = new ArrayList<UtilityBill>();
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from utility_bill");
            try {
                ResultSet result = preStmt.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    String type = result.getString("bill_type");
                    UtilityBillType billType = getUtilityBillType(type);
                    int clientId = result.getInt("client_id");
                    float amount = result.getFloat("amount_to_pay");
                    boolean paid = result.getBoolean("paid");
                    UtilityBill utilityBill = new UtilityBill(billType, clientId, amount, paid);
                    utilityBill.setId(id);

                    utilityBills.add(utilityBill);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return utilityBills;
    }

}
