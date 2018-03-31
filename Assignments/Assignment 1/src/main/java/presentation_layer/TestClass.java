package presentation_layer;


import business_layer.GeneralService;
import business_layer.IGeneralService;
import exceptions.UsernameAlreadyExistsException;
import model.User;

public class TestClass {
    public static void main(String[] args) throws UsernameAlreadyExistsException
    {
        IGeneralService gs = new GeneralService();

        User loggedInUser = null;
        try {
            loggedInUser = gs.logIn("andrada1234", "pass123");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.out.println(loggedInUser);

    }
}
