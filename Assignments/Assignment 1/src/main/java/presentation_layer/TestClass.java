package presentation_layer;


import business_layer.GeneralService;
import business_layer.IGeneralService;
import data_access_layer.IUserDAO;
import data_access_layer.UserDAO;
import exceptions.UsernameAlreadyExistsException;
import model.User;
import model.UserRole;

public class TestClass {
    public static void main(String[] args) throws UsernameAlreadyExistsException
    {
        IUserDAO userDao = new UserDAO();
        IGeneralService gs = new GeneralService(userDao);

        /*
        User loggedInUser = null;
        try {
            loggedInUser = gs.logIn("andrada1234", "pass123");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println(loggedInUser);
        */

        User user = gs.signUp(new User("TestUserFullname", "testUsername", "testPassword", "testEmail@yahoo.com", "Home", UserRole.EMPLOYEE));
        System.out.println(user);
    }
}
