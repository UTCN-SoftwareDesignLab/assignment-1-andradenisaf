package business_layer;

import data_access_layer.ActivityLogDAO;
import data_access_layer.IActivityLogDAO;
import data_access_layer.IUserDAO;
import data_access_layer.UserDAO;
import exceptions.InexistentUserException;
import exceptions.UsernameAlreadyExistsException;
import model.ActivityLog;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class AdministratorService extends GeneralService implements IAdministratorService {

    IActivityLogDAO activityLogDAO = null;
    IUserDAO userDAO = null;

    public AdministratorService() {
        activityLogDAO = new ActivityLogDAO();
        userDAO = new UserDAO();
    }

    @Override
    public List<ActivityLog> getActivityLogForEmployee(String employeeUsername) throws InexistentUserException

    {
        User employee = userDAO.getUserByUsername(employeeUsername);

        if (employee == null)
            throw new InexistentUserException("Inexistent user");

        List<ActivityLog> activityLogs = activityLogDAO.getActivityLogs();
        List<ActivityLog> searchedLogs = new ArrayList<ActivityLog>();

        for (ActivityLog activityLog : activityLogs) {
            if (activityLog.getEmployeeId() == employee.getId()) {
                searchedLogs.add(activityLog);
            }
        }
        return searchedLogs;
    }

    @Override
    public User addEmployee(User user) throws UsernameAlreadyExistsException {
        return this.signUp(user);
    }

    @Override
    public User updateEmployee(User user) throws InexistentUserException {

        User dbUser = userDAO.getUserByUsername(user.getUsername());
        if (dbUser == null)
            throw new InexistentUserException("Inexistent user");
        user.setId(dbUser.getId());
        userDAO.update(user);
        return user;
    }

    @Override
    public boolean deleteEmployee(String username) throws InexistentUserException {
        User dbUser = userDAO.getUserByUsername(username);
        if (dbUser == null)
            throw new InexistentUserException("Inexistent user");
        return userDAO.delete(dbUser.getUsername());
    }


}

