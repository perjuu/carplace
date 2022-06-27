package ca.verticalidigital.carplace.service.dto;

import ca.verticalidigital.carplace.domain.enumeration.*;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
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

    @Size(max = 40)
    private String internalNumber;

    private Integer performance;

    private Instant mot;

    private Instant regDate;

    private Boolean vat;

    private String vin;

    @Size(max = 32)
    private String colour;

    private Ac ac;

    private Boolean esp;

    private Boolean abs;

    private Integer doors;

    private Integer cubicCapacity;

    private Integer numberOfSeats;

    private EmissionClass emissionClass;

    private Integer emission;

    private Gearbox gearbox;
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

    public String getInternalNumber() {
        return internalNumber;
    }

    public void setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
    }

    public Integer getPerformance() {
        return performance;
    }

    public void setPerformance(Integer performance) {
        this.performance = performance;
    }

    public Instant getMot() {
        return mot;
    }

    public void setMot(Instant mot) {
        this.mot = mot;
    }

    public Instant getRegDate() {
        return regDate;
    }

    public void setRegDate(Instant regDate) {
        this.regDate = regDate;
    }

    public Boolean getVat() {
        return vat;
    }

    public void setVat(Boolean vat) {
        this.vat = vat;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Ac getAc() {
        return ac;
    }

    public void setAc(Ac ac) {
        this.ac = ac;
    }

    public Boolean getEsp() {
        return esp;
    }

    public void setEsp(Boolean esp) {
        this.esp = esp;
    }

    public Boolean getAbs() {
        return abs;
    }

    public void setAbs(Boolean abs) {
        this.abs = abs;
    }

    public Integer getDoors() {
        return doors;
    }

    public void setDoors(Integer doors) {
        this.doors = doors;
    }

    public Integer getCubicCapacity() {
        return cubicCapacity;
    }

    public void setCubicCapacity(Integer cubicCapacity) {
        this.cubicCapacity = cubicCapacity;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public EmissionClass getEmissionClass() {
        return emissionClass;
    }

    public void setEmissionClass(EmissionClass emissionClass) {
        this.emissionClass = emissionClass;
    }

    public Integer getEmission() {
        return emission;
    }

    public void setEmission(Integer emission) {
        this.emission = emission;
    }

    public Gearbox getGearbox() {
        return gearbox;
    }

    public void setGearbox(Gearbox gearbox) {
        this.gearbox = gearbox;
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
