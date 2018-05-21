import application.entity.Consultation;
import application.entity.Patient;
import application.entity.User;
import application.repository.ConsultationRepository;
import application.service.ConsultationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class ConsultationServiceTest {
    private ConsultationService consultationService;

    @Mock
    private ConsultationRepository consultationRepository;

    private Patient patient1 = new Patient();

    private Consultation consultation1 = new Consultation();
    private Consultation consultation2 = new Consultation();
    private Consultation consultation3 = new Consultation();
    private Consultation consultation4 = new Consultation();

    @Before
    public void before() {
        consultationService = new ConsultationService(consultationRepository);

        consultation1.setId(new Long(1));
        consultation1.setDescription("Endocrinologie");
        consultation1.setDate(new Date(2018, 5, 19));
        consultation1.setPatient(new Patient());
        consultation1.setDoctor(new User());

        consultation2.setId(new Long(2));
        consultation2.setDescription("Ginecologie");
        consultation2.setDate(new Date(2018, 6, 2));
        consultation2.setPatient(new Patient());
        consultation2.setDoctor(new User());

        consultation3.setId(new Long(3));
        consultation3.setDescription("Neurologie");
        consultation3.setDate(new Date(2018, 6, 8));
        consultation3.setPatient(new Patient());
        consultation3.setDoctor(new User());

        consultation4.setId(new Long(4));
        consultation4.setDescription("Dermatologie");
        consultation4.setDate(new Date(2018, 5, 21));
        consultation4.setPatient(new Patient());
        consultation4.setDoctor(new User());


        ArrayList<Consultation> consultations = new ArrayList<>();
        consultations.add(consultation1);
        consultations.add(consultation2);
        consultations.add(consultation3);


        List<Consultation> consultations1 = new ArrayList<Consultation>();
        consultations1.add(consultation1);
        consultations1.add(consultation2);

        patient1.setId(new Long(1));
        patient1.setName("Nicoleta Ghitescu");
        patient1.setIdentityCardNo("XH111111");
        patient1.setCNP("1111111111111");
        patient1.setDateOfBirth(new Date(1995,11,1));
        patient1.setAddress("str. Frumuselelor, nr. 1");
        patient1.setConsultations(consultations1);

        Mockito.when(consultationRepository.findAll()).thenReturn(consultations);
        Optional<Consultation> optional = Optional.of(consultation2);
        Mockito.when(consultationRepository.findById(new Long(2))).thenReturn(optional);
        Mockito.when(consultationRepository.findAllByPatient(Mockito.any())).thenReturn(consultations1);
        Mockito.when(consultationRepository.save(Mockito.any())).thenReturn(consultation4);
    }

    @Test
    public void testFindAll() {
        Iterable<Consultation> consultations = consultationService.findAll();
        ArrayList<Consultation> consultationArrayList = new ArrayList<>();
        consultations.forEach(consultationArrayList::add);
        Assert.assertEquals(3, consultationArrayList.size());
    }

    @Test
    public void testFindById() {
        Consultation consultation = consultationService.findById(new Long(2));
        Assert.assertEquals(new Long(2), consultation.getId());
    }

    @Test
    public void testFindAllByPatient() {
        List<Consultation> consultations = consultationService.findAllByPatient(patient1);
        Assert.assertEquals(consultations, patient1.getConsultations());
    }

    @Test
    public void create() {
        Consultation consultation = consultationService.create(new Consultation());
        Assert.assertEquals(new Long(4), consultation.getId());
    }
}

