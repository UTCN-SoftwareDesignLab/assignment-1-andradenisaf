package controller;

import service.*;
import repository.*;

/**
 * Created by Alex on 18/03/2017.
 */
public class ComponentFactory {

    private final IGeneralService generalService;
    private final IAdministratorService administratorService;
    private final IEmployeeService employeeService;

    private final IUserDAO userDao;
    private final IClientDAO clientDao;
    private final IAccountDAO accountDao;
    private final IActivityLogDAO activityLogDao;
    private final IUtilityBillDAO utilityBillDao;


    private static ComponentFactory instance;

    public static ComponentFactory instance() {
        if (instance == null) {
            instance = new ComponentFactory();
        }
        return instance;
    }

    private ComponentFactory() {
        userDao = new UserDAO();
        clientDao = new ClientDAO();
        accountDao = new AccountDAO();
        activityLogDao = new ActivityLogDAO();
        utilityBillDao = new UtilityBillDAO();
        generalService = new GeneralService(userDao);
        administratorService = new AdministratorService(activityLogDao, userDao);
        employeeService = new EmployeeService(userDao,clientDao,accountDao,activityLogDao,utilityBillDao);
    }

    public IGeneralService getGeneralService() {
        return generalService;
    }

    public IAdministratorService getAdministratorService() {
        return administratorService;
    }

    public IEmployeeService getEmployeeService() {
        return employeeService;
    }
}
