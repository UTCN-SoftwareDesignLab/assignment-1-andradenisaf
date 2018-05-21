package application.entity;

import org.json.simple.JSONObject;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "id_card_no", nullable = false)
    private String identityCardNo;

    @Column(name = "cnp")
    private String cnp;

    @Column(name = "date_of_birth", nullable = false)
    private Date dateOfBirth;

    @Column(name = "address")
    private String address;

    public Patient() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityCardNo() {
        return identityCardNo;
    }

    public void setIdentityCardNo(String identityCardNo) {
        this.identityCardNo = identityCardNo;
    }

    public String getCNP() {
        return cnp;
    }

    public void setCNP(String cnp) {
        this.cnp = cnp;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = dateFormat.format(getDateOfBirth());

        jsonObject.put("id", getId());
        jsonObject.put("name", getName());
        jsonObject.put("idCardNo", getIdentityCardNo());
        jsonObject.put("cnp", getCNP());
        jsonObject.put("dateOfBirth", dateString);
        jsonObject.put("address", getAddress());

        return jsonObject;
    }
}


