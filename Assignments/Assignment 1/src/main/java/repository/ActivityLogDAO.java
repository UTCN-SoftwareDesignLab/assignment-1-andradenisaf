package repository;

import model.ActivityLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ActivityLogDAO implements IActivityLogDAO {
    private Connection connection;

    public ActivityLogDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    private ActivityLog getActivityLogFromStatementExecution(PreparedStatement preStmt) {
        try {
            ResultSet result = preStmt.executeQuery();
            while (result.next()) {
                int id = result.getInt("id");
                int employeeId = result.getInt("employee_id");
                String description = result.getString("description");
                ActivityLog al = new ActivityLog(employeeId, description);
                al.setId(id);

                return al;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean save(ActivityLog activityLog) {

        //query the database using parameters (?)
        try {
            PreparedStatement preStmt = connection.prepareStatement("insert into activity_log values (id,?,?)");
            //set the statement's parameters
            preStmt.setInt(1, activityLog.getEmployeeId());
            preStmt.setString(2, activityLog.getDescription());

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
            PreparedStatement preStmt = connection.prepareStatement("delete from activity_log where id=?");
            preStmt.setInt(1, id);
            int result = preStmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public boolean update(ActivityLog activityLog) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("UPDATE activity_log SET " +
                    "clientId=?, accountId=?" +
                    "WHERE id = ?");
            preStmt.setInt(1, activityLog.getEmployeeId());
            preStmt.setString(2, activityLog.getDescription());

            int result = preStmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return false;
    }

    @Override
    public ActivityLog getActivityLogById(int id) {
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from activity_log where id=?");
            preStmt.setInt(1, id);
            return this.getActivityLogFromStatementExecution(preStmt);
        } catch (SQLException ex) {
            System.out.println("Error DB " + ex);
        }
        return null;
    }

    @Override
    public List<ActivityLog> getActivityLogs() {

        List<ActivityLog> activityLogs = new ArrayList<ActivityLog>();
        try {
            PreparedStatement preStmt = connection.prepareStatement("select * from activity_log");
            try {
                ResultSet result = preStmt.executeQuery();
                while (result.next()) {
                    int id = result.getInt("id");
                    int employeeId = result.getInt("employee_id");
                    String description = result.getString("description");
                    ActivityLog activityLog = new ActivityLog(employeeId, description);
                    activityLog.setId(id);
                    activityLogs.add(activityLog);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return activityLogs;
    }

}
