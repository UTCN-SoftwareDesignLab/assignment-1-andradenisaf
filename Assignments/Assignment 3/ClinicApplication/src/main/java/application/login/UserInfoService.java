package application.login;

import application.entity.User;
import application.service.IUserService;
import application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Transactional(readOnly = true)
    @Override
    public UserInfo loadUserByUsername(String name) throws UsernameNotFoundException {
        if (userService == null) {
            userService = UserService.getInstance();
        }
        System.out.println(name);
        User user = userService.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("User '" + name + "' does not exist");
        }
        UserInfo userDetails = new UserInfo(user);
        return userDetails;
    }
}
