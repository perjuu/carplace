package ca.verticalidigital.carplace.domain;

import ca.verticalidigital.carplace.domain.enumeration.FuelType;
import ca.verticalidigital.carplace.domain.enumeration.ListingStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VehicleListing.
 */
@Entity
@Table(name = "vehicle_listing")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VehicleListing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Integer price;

    @Column(name = "year")
    private Integer year;

    @Column(name = "mileage")
    private Integer mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel")
    private FuelType fuel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ListingStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vehicleListings", "categories" }, allowSetters = true)
    private CarModel carModel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VehicleListing id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrice() {
        return this.price;
    }

    public VehicleListing price(Integer price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getYear() {
        return this.year;
    }

    public VehicleListing year(Integer year) {
        this.setYear(year);
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMileage() {
        return this.mileage;
    }

    public VehicleListing mileage(Integer mileage) {
        this.setMileage(mileage);
        return this;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public FuelType getFuel() {
        return this.fuel;
    }

    public VehicleListing fuel(FuelType fuel) {
        this.setFuel(fuel);
        return this;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    public ListingStatus getStatus() {
        return this.status;
    }

    public VehicleListing status(ListingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    public CarModel getCarModel() {
        return this.carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public VehicleListing carModel(CarModel carModel) {
        this.setCarModel(carModel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleListing)) {
            return false;
        }
        return id != null && id.equals(((VehicleListing) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleListing{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", year=" + getYear() +
            ", mileage=" + getMileage() +
            ", fuel='" + getFuel() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
