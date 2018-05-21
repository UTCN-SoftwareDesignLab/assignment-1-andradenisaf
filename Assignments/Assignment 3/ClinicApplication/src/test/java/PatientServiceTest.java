import application.entity.Consultation;
import application.entity.Patient;
import application.entity.User;
import application.repository.PatientRepository;
import application.service.PatientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class PatientServiceTest {

    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Before
    public void before() {
        patientService = new PatientService(patientRepository);

        Patient patient1 = new Patient();
        Patient patient2 = new Patient();
        Patient patient3 = new Patient();
        Patient patient4 = new Patient();

        Consultation consultation1 = new Consultation(new Long(1), "Endocrinologie", new Date(2018,5,21),patient1,new User());
        Consultation consultation2 = new Consultation(new Long(2), "Ginecologie", new Date(2018,6,2),patient2,new User());
        Consultation consultation3 = new Consultation(new Long(3), "Neurologie", new Date(2018,6,4),patient3,new User());
        Consultation consultation4 = new Consultation(new Long(4), "Dermatologie", new Date(2018,5,28),patient4,new User());

        List<Consultation> consultations1 = new ArrayList<Consultation>();
        consultations1.add(consultation1);

        List<Consultation> consultations2 = new ArrayList<Consultation>();
        consultations1.add(consultation2);

        List<Consultation> consultations3 = new ArrayList<Consultation>();
        consultations1.add(consultation3);

        List<Consultation> consultations4 = new ArrayList<Consultation>();
        consultations1.add(consultation4);

        patient1.setId(new Long(1));
        patient1.setName("Nicoleta Ghitescu");
        patient1.setIdentityCardNo("XH111111");
        patient1.setCNP("1111111111111");
        patient1.setDateOfBirth(new Date(1995,11,1));
        patient1.setAddress("str. Frumuselelor, nr. 1");
        patient1.setConsultations(consultations1);

        patient2.setId(new Long(2));
        patient2.setName("Medeea Constantinescu");
        patient2.setIdentityCardNo("XH222222");
        patient2.setCNP("2222222222222");
        patient2.setDateOfBirth(new Date(1996,5,26));
        patient2.setAddress("str. Avram Iancu, nr. 2");
        patient2.setConsultations(consultations2);

        patient3.setId(new Long(3));
        patient3.setName("Dumitrascu Dora");
        patient3.setIdentityCardNo("XH333333");
        patient3.setCNP("3333333333333");
        patient3.setDateOfBirth(new Date(1996,8,20));
        patient3.setAddress("str. Miercurea Ciuc, nr. 3");
        patient3.setConsultations(consultations3);

        patient4.setId(new Long(4));
        patient4.setName("Mercea Vanessa");
        patient4.setIdentityCardNo("XH444444");
        patient4.setCNP("3333333333333");
        patient4.setDateOfBirth(new Date(1996,8,28));
        patient4.setAddress("str. Deva, nr. 4");
        patient4.setConsultations(consultations4);

        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(patient1);
        patients.add(patient2);
        patients.add(patient3);

        Mockito.when(patientRepository.findAll()).thenReturn(patients);
        Optional<Patient> optional = Optional.of(patient1);
        Mockito.when(patientRepository.findById(new Long(1))).thenReturn(optional);
        Mockito.when(patientRepository.findAllByName(new String("Nicoleta Ghitescu"))).thenReturn(patients);
        Mockito.when(patientRepository.findByIdentityCardNo("XH111111")).thenReturn(optional);
        Mockito.when(patientRepository.findByCnp("1111111111111")).thenReturn(optional);
        Mockito.when(patientRepository.save(Mockito.any())).thenReturn(patient4);
    }

    @Test
    public void testFindAll() {
        Iterable<Patient> patients = patientService.findAll();
        ArrayList<Patient> patientsArrayList = new ArrayList<>();
        patients.forEach(patientsArrayList::add);
        Assert.assertEquals(3, patientsArrayList.size());
    }

    @Test
    public void testFindById() {
        Patient patient = patientService.findById(new Long(1));
        Assert.assertEquals(new Long(1), patient.getId());
    }

    @Test
    public void testFindAllByName() {
        Iterable<Patient> patients = patientService.findAllByName("Nicoleta Ghitescu");
        ArrayList<Patient> patientsArrayList = new ArrayList<>();
        patients.forEach(patientsArrayList::add);
        Assert.assertEquals(new String("Nicoleta Ghitescu"), patientsArrayList.get(0).getName());

    }

    @Test
    public void testFindByIdentityCardNo() {
        Patient patient = patientService.findByIdentityCardNo("XH111111");
        Assert.assertEquals(new String("XH111111"), patient.getIdentityCardNo());
    }

    @Test
    public void testFindByCNP() {
        Patient patient = patientService.findByCNP("1111111111111");
        Assert.assertEquals(new String("1111111111111"), patient.getCNP());
    }

    @Test
    public void create() {
        Patient patient = patientService.create(new Patient());
        Assert.assertEquals(new Long(4), patient.getId());
    }

    @Test
    public void update() {
        Patient patient = patientService.create(new Patient());
        patient.setName("Ciontescu Nicoleta");
        Assert.assertEquals(new String("Ciontescu Nicoleta"), patient.getName());
    }
}