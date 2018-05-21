package application.login;

import application.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new Pbkdf2PasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/images/**", "/js/**", "/vendor/**", "/patients").permitAll()
                .antMatchers("/patients**", "/consultations**").authenticated()
                .antMatchers( "/admin").hasAuthority(Constants.Roles.ADMINISTRATOR)
                .antMatchers("/users**").hasAuthority(Constants.Roles.ADMINISTRATOR)
                .antMatchers("/secretary").hasAuthority(Constants.Roles.SECRETARY)
                .antMatchers("/doctor", "/patients**").hasAuthority(Constants.Roles.DOCTOR)
                .antMatchers("/login**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(new LoginSuccess())
                .failureHandler(new LoginFailure())
                .and()
                .logout()
                .permitAll();
    }

    @Override
    protected UserDetailsService userDetailsService() {
        return new UserInfoService();
    }
}
