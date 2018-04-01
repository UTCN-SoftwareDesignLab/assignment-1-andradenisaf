package service;

import model.UserRole;
import repository.IActivityLogDAO;
import repository.IUserDAO;
import exceptions.InexistentUserException;
import exceptions.UsernameAlreadyExistsException;
import model.ActivityLog;
import model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AdministratorService extends GeneralService implements IAdministratorService {

    IActivityLogDAO activityLogDAO = null;

    public AdministratorService(IActivityLogDAO activityLogDao, IUserDAO userDao) {
        super(userDao);
        this.activityLogDAO = activityLogDao;
    }

    @Override
    public List<ActivityLog> generateActivityLogForEmployee(String employeeUsername) throws InexistentUserException

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


        String fileName = String.format("ActivityLogs/employee%s.txt",employee.getUsername().toUpperCase());

        //create the file if it does not exist
        File activityReportFile = new File(fileName);
        try {
            activityReportFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //print details to file
        try(  PrintWriter out = new PrintWriter( fileName )  ){
            out.println("Employee id:" + employee.getId());
            out.println("Employee username: " + employee.getUsername().toUpperCase());
            out.println("\nActivity Log:");
            for(ActivityLog log: searchedLogs)
            {
                out.println("\t"+ log.getDescription());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return searchedLogs;
    }

    @Override
    public List<User> getEmployees()
    {
        List<User> users = userDao.getUsers();
        List<User> employees = new ArrayList<>();

        for(User user: users){
            if(user.getRole().equals(UserRole.EMPLOYEE))
                employees.add(user);
        }

        return employees;
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

