package application.login;

import application.entity.Role;
import application.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserInfo implements UserDetails {

    private User user;

    public UserInfo(User user) {
        this.user = user;
    }

    public UserInfo() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Role role = null;

        if (user != null) {
            role = user.getRole();
        }

        if (role != null) {
            ArrayList grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(role);
            System.out.println(role.getAuthority());
            return grantedAuthorities;
        }

        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
