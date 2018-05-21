import application.entity.Role;
import application.repository.RoleRepository;
import application.service.RoleService;
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
public class RoleServiceTest {
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Before
    public void before() {
        roleService = new RoleService(roleRepository);

        Role role1 = new Role();
        role1.setId(new Long(1));
        role1.setType("Administrator");

        Role role2 = new Role();
        role2.setId(new Long(2));
        role2.setType("Doctor");

        Role role3 = new Role();
        role3.setId(new Long(3));
        role3.setType("Secretary");

        ArrayList<Role> roles = new ArrayList<>();
        roles.add(role1);
        roles.add(role2);
        roles.add(role3);

        Mockito.when(roleRepository.findAll()).thenReturn(roles);
        Optional<Role> optional = Optional.of(role1);
        Mockito.when(roleRepository.findById(new Long(1))).thenReturn(optional);
        Mockito.when(roleRepository.findByType(new String("Administrator"))).thenReturn(optional);
    }

    @Test
    public void testFindAll() {
        Iterable<Role> roles = roleService.findAll();
        ArrayList<Role> roleArrayList = new ArrayList<>();
        roles.forEach(roleArrayList::add);
        Assert.assertEquals(3, roleArrayList.size());
    }

    @Test
    public void testFindById() {
        Role role = roleService.findById(new Long(1));
        Assert.assertEquals(new Long(1), role.getId());
    }

    @Test
    public void testFindByType() {
        Role role = roleService.findByType(new String("Administrator"));
        Assert.assertEquals(new String("Administrator"), role.getType());
    }
}
