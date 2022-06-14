package ca.verticalidigital.carplace.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link ca.verticalidigital.carplace.domain.CarModel} entity.
 */
public class CarModelDTO implements Serializable {

    private Long id;

    private String make;

    private String model;

    private Integer launchYear;

    private Set<CategoryDTO> categories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getLaunchYear() {
        return launchYear;
    }

    public void setLaunchYear(Integer launchYear) {
        this.launchYear = launchYear;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CarModelDTO)) {
            return false;
        }

        CarModelDTO carModelDTO = (CarModelDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, carModelDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarModelDTO{" +
            "id=" + getId() +
            ", make='" + getMake() + "'" +
            ", model='" + getModel() + "'" +
            ", launchYear=" + getLaunchYear() +
            ", categories=" + getCategories() +
            "}";
    }
}
