package ca.verticalidigital.carplace.service.dto;

import ca.verticalidigital.carplace.domain.enumeration.FuelType;
import ca.verticalidigital.carplace.domain.enumeration.ListingStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ca.verticalidigital.carplace.domain.VehicleListing} entity.
 */
public class VehicleListingDTO implements Serializable {

    private Long id;

    private Integer price;

    private Integer year;

    private Integer mileage;

    private FuelType fuel;

    private ListingStatus status;

    private CarModelDTO carModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public FuelType getFuel() {
        return fuel;
    }

    public void setFuel(FuelType fuel) {
        this.fuel = fuel;
    }

    public ListingStatus getStatus() {
        return status;
    }

    public void setStatus(ListingStatus status) {
        this.status = status;
    }

    public CarModelDTO getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModelDTO carModel) {
        this.carModel = carModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleListingDTO)) {
            return false;
        }

        VehicleListingDTO vehicleListingDTO = (VehicleListingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleListingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleListingDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", year=" + getYear() +
            ", mileage=" + getMileage() +
            ", fuel='" + getFuel() + "'" +
            ", status='" + getStatus() + "'" +
            ", carModel=" + getCarModel() +
            "}";
    }
}
