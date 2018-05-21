import application.entity.Role;
import application.entity.User;
import application.repository.UserRepository;
import application.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Before
    public void before() {
        userService = new UserService(userRepository);

        User user1 = new User();
        user1.setId(new Long(1));
        user1.setUsername("diana");
        user1.setPassword("dianapass");
        user1.setFullname("Diana Balaian");
        user1.setEmail("diana_balaian@yahoo.com");
        user1.setRole(new Role());

        User user2 = new User();
        user2.setId(new Long(2));
        user2.setUsername("carina");
        user2.setPassword("carinapass");
        user2.setFullname("Carina Sav");
        user2.setEmail("carina_sav@yahoo.com");
        user2.setRole(new Role());

        User user3 = new User();
        user3.setId(new Long(3));
        user3.setUsername("ligia");
        user3.setPassword("ligiapass");
        user3.setFullname("Ligia Bucovala");
        user3.setEmail("ligia_bucovala@yahoo.com");
        user3.setRole(new Role());

        User user4 = new User();
        user4.setId(new Long(4));
        user4.setUsername("ale");
        user4.setPassword("alepass");
        user4.setFullname("Alexandra Dobre");
        user4.setEmail("alexandra_dobre@yahoo.com");
        user4.setRole(new Role());

        ArrayList<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        Mockito.when(userRepository.findAll()).thenReturn(users);
        Optional<User> optional = Optional.of(user2);
        Mockito.when(userRepository.findById(new Long(2))).thenReturn(optional);
        Mockito.when(userRepository.findByUsername(new String("carina"))).thenReturn(optional);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user4);
    }

    @Test
    public void testFindAll() {
        Iterable<User> users = userService.findAll();
        ArrayList<User> userArrayList = new ArrayList<>();
        users.forEach(userArrayList::add);
        Assert.assertEquals(userArrayList.size(), 3);
    }

    @Test
    public void testFindById() {
        User user = userService.findById(new Long(2));
        Assert.assertEquals(user.getId(), new Long(2));

    }

    @Test
    public void testFindByName() {
        User user = userService.findByName(new String("carina"));
        Assert.assertEquals(user.getUsername(), new String("carina"));

    }

    @Test
    public void create() {
        User user = userService.create(new User());
        Assert.assertEquals(user.getId(), new Long(4));
    }

    @Test
    public void update() {
        User user = userService.create(new User());
        user.setUsername("Ligiuta");
        Assert.assertEquals(user.getUsername(), new String("Ligiuta"));
    }



}
