package application.controller;

import application.entity.Role;
import application.entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import application.service.IRoleService;
import application.service.IUserService;

import java.util.List;

@RestController
public class UserController {

    private IUserService userService;
    private IRoleService roleService;

    @Autowired
    public UserController(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsers() {
        List<User> users = userService.findAll();

        JSONArray jsonArray = new JSONArray();

        for (User user : users) {
            JSONObject userObject = user.toJSON();
            jsonArray.add(userObject);
        }

        JSONObject responseJSON = new JSONObject();
        responseJSON.put("users", jsonArray);

        return responseJSON.toJSONString();
    }

    @RequestMapping(path = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@RequestBody String requestJSON) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(requestJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(requestJSON);
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String username = (String) jsonObject.get("username");
        if (!validUsername(username)) {
            return new ResponseEntity("{\"message\":\"Invalid username.\"}", HttpStatus.BAD_REQUEST);
        }

        if (usernameTaken(username)) {
            return new ResponseEntity("{\"message\":\"Username already taken.\"}", HttpStatus.BAD_REQUEST);
        }

        String password = (String) jsonObject.get("password");
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setFullname((String) jsonObject.get("fullname"));
        user.setEmail((String) jsonObject.get("email"));

        Long roleId = (Long) jsonObject.get("role");
        Role role = roleService.findById(roleId);
        if (role != null) {
            user.setRole(role);
        } else {
            return new ResponseEntity("{\"message\":\"Role not found.\"}", HttpStatus.NOT_FOUND);
        }

        user = userService.create(user);
        return new ResponseEntity(user.toJSON().toJSONString(), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@PathVariable("id") Long id, @RequestBody String requestJSON) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) parser.parse(requestJSON);
        } catch (ParseException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        String username = (String) jsonObject.get("username");
        if (username != null) {
            if (!validUsername(username)) {
                return new ResponseEntity("{\"message\":\"Invalid username.\"}", HttpStatus.BAD_REQUEST);
            }
            if (usernameTaken(username)) {
                return new ResponseEntity("{\"message\":\"Username taken.\"}", HttpStatus.BAD_REQUEST);
            }
            user.setUsername(username);
        }

        String password = (String) jsonObject.get("password");
        if (password != null) {
            PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }

        String fullName = (String) jsonObject.get("fullname");
        if (fullName != null) {
            user.setFullname(fullName);
        }

        String email = (String) jsonObject.get("email");
        if (email != null) {
            user.setEmail(email);
        }

        String roleType = (String) jsonObject.get("role");
        if (roleType != null) {
            Role role = roleService.findByType(roleType);
            if (role == null) {
                return new ResponseEntity("{\"message\":\"Invalid role.\"}", HttpStatus.NOT_FOUND);
            }
            user.setRole(role);
        }

        user = userService.update(user);
        return new ResponseEntity(user.toJSON().toJSONString(), HttpStatus.OK);
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteUser(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return new ResponseEntity("{\"message\":\"User not found.\"}", HttpStatus.NOT_FOUND);
        }
        boolean success = userService.delete(user);
        if (!success) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    private boolean usernameTaken(String name) {
        User user = userService.findByName(name);
        return (user != null);
    }

    private boolean validUsername(String username) {
        return (username != null && username.length() > 2);
    }
}

