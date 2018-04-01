package business_layer;

import data_access_layer.IActivityLogDAO;
import data_access_layer.IUserDAO;
import exceptions.InexistentUserException;
import exceptions.UsernameAlreadyExistsException;
import model.ActivityLog;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class AdministratorService extends GeneralService implements IAdministratorService {

    IActivityLogDAO activityLogDAO = null;

    public AdministratorService(IActivityLogDAO activityLogDao, IUserDAO userDao) {
        super(userDao);
        this.activityLogDAO = activityLogDao;
    }

    @Override
    public List<ActivityLog> getActivityLogForEmployee(String employeeUsername) throws InexistentUserException

    {
        User employee = userDao.getUserByUsername(employeeUsername);

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

        User dbUser = userDao.getUserByUsername(user.getUsername());
        if (dbUser == null)
            throw new InexistentUserException("Inexistent user");
        user.setId(dbUser.getId());
        userDao.update(user);
        return user;
    }

    @Override
    public boolean deleteEmployee(String username) throws InexistentUserException {
        User dbUser = userDao.getUserByUsername(username);
        if (dbUser == null)
            throw new InexistentUserException("Inexistent user");
        return userDao.delete(dbUser.getUsername());
    }


}

