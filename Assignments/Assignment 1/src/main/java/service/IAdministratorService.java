package service;


import exceptions.InexistentUserException;
import exceptions.UsernameAlreadyExistsException;
import model.ActivityLog;
import model.User;

import java.util.List;

public interface IAdministratorService extends IGeneralService {

    public List<ActivityLog> generateActivityLogForEmployee(String employeeUsername) throws InexistentUserException;

    public User addEmployee(User user) throws UsernameAlreadyExistsException;

    public User updateEmployee(User user) throws InexistentUserException;

    public boolean deleteEmployee(String username) throws InexistentUserException;

    public List<User> getEmployees();

}
