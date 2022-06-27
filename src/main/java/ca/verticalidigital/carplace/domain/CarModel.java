package ca.verticalidigital.carplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CarModel.
 */
@Entity
@Table(name = "car_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CarModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "make")
    private String make;

    @NotNull
    @Column(name = "model")
    private String model;

    @Column(name = "launch_year")
    private Integer launchYear;

    @OneToMany(mappedBy = "carModel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carModel" }, allowSetters = true)
    private Set<VehicleListing> vehicleListings = new HashSet<>();

    @NotNull
    @ManyToMany
    @JoinTable(
        name = "rel_car_model__category",
        joinColumns = @JoinColumn(name = "car_model_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "carModels" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CarModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return this.make;
    }

    public CarModel make(String make) {
        this.setMake(make);
        return this;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return this.model;
    }

    public CarModel model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getLaunchYear() {
        return this.launchYear;
    }

    public CarModel launchYear(Integer launchYear) {
        this.setLaunchYear(launchYear);
        return this;
    }

    public void setLaunchYear(Integer launchYear) {
        this.launchYear = launchYear;
    }

    public Set<VehicleListing> getVehicleListings() {
        return this.vehicleListings;
    }

    public void setVehicleListings(Set<VehicleListing> vehicleListings) {
        if (this.vehicleListings != null) {
            this.vehicleListings.forEach(i -> i.setCarModel(null));
        }
        if (vehicleListings != null) {
            vehicleListings.forEach(i -> i.setCarModel(this));
        }
        this.vehicleListings = vehicleListings;
    }

    public CarModel vehicleListings(Set<VehicleListing> vehicleListings) {
        this.setVehicleListings(vehicleListings);
        return this;
    }

    public CarModel addVehicleListing(VehicleListing vehicleListing) {
        this.vehicleListings.add(vehicleListing);
        vehicleListing.setCarModel(this);
        return this;
    }

    public CarModel removeVehicleListing(VehicleListing vehicleListing) {
        this.vehicleListings.remove(vehicleListing);
        vehicleListing.setCarModel(null);
        return this;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public CarModel categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public CarModel addCategory(Category category) {
        this.categories.add(category);
        category.getCarModels().add(this);
        return this;
    }

    public CarModel removeCategory(Category category) {
        this.categories.remove(category);
        category.getCarModels().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarModel)) {
            return false;
        }
        return id != null && id.equals(((CarModel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarModel{" +
            "id=" + getId() +
            ", make='" + getMake() + "'" +
            ", model='" + getModel() + "'" +
            ", launchYear=" + getLaunchYear() +
            "}";
    }
}
