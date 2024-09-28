package fall24.swp391.g1se1868.koiauction.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class KoiFish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KoiID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID")
    private User userID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CountryID")
    private KoiOrigin countryID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KoiTypeID")
    private KoiType koiTypeID;

    @Column(name = "BreederID")
    private Integer breederID;

    @Column(name = "Weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Nationalized
    @Column(name = "Sex", length = 10)
    private String sex;

    @Column(name = "Birthday")
    private LocalDate birthday;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "Length", precision = 10, scale = 2)
    private BigDecimal length;

    @Nationalized
    @Column(name = "Status", length = 50)
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserID() {
        return userID;
    }

    public void setUserID(User userID) {
        this.userID = userID;
    }

    public KoiOrigin getCountryID() {
        return countryID;
    }

    public void setCountryID(KoiOrigin countryID) {
        this.countryID = countryID;
    }

    public KoiType getKoiTypeID() {
        return koiTypeID;
    }

    public void setKoiTypeID(KoiType koiTypeID) {
        this.koiTypeID = koiTypeID;
    }

    public Integer getBreederID() {
        return breederID;
    }

    public void setBreederID(Integer breederID) {
        this.breederID = breederID;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}