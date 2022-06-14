package ca.verticalidigital.carplace.service.criteria;

import ca.verticalidigital.carplace.domain.enumeration.FuelType;
import ca.verticalidigital.carplace.domain.enumeration.ListingStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ca.verticalidigital.carplace.domain.VehicleListing} entity. This class is used
 * in {@link ca.verticalidigital.carplace.web.rest.VehicleListingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /vehicle-listings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class VehicleListingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FuelType
     */
    public static class FuelTypeFilter extends Filter<FuelType> {

        public FuelTypeFilter() {}

        public FuelTypeFilter(FuelTypeFilter filter) {
            super(filter);
        }

        @Override
        public FuelTypeFilter copy() {
            return new FuelTypeFilter(this);
        }
    }

    /**
     * Class for filtering ListingStatus
     */
    public static class ListingStatusFilter extends Filter<ListingStatus> {

        public ListingStatusFilter() {}

        public ListingStatusFilter(ListingStatusFilter filter) {
            super(filter);
        }

        @Override
        public ListingStatusFilter copy() {
            return new ListingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter price;

    private IntegerFilter year;

    private IntegerFilter mileage;

    private FuelTypeFilter fuel;

    private ListingStatusFilter status;

    private LongFilter carModelId;

    private Boolean distinct;

    public VehicleListingCriteria() {}

    public VehicleListingCriteria(VehicleListingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.year = other.year == null ? null : other.year.copy();
        this.mileage = other.mileage == null ? null : other.mileage.copy();
        this.fuel = other.fuel == null ? null : other.fuel.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.carModelId = other.carModelId == null ? null : other.carModelId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public VehicleListingCriteria copy() {
        return new VehicleListingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPrice() {
        return price;
    }

    public IntegerFilter price() {
        if (price == null) {
            price = new IntegerFilter();
        }
        return price;
    }

    public void setPrice(IntegerFilter price) {
        this.price = price;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public IntegerFilter year() {
        if (year == null) {
            year = new IntegerFilter();
        }
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public IntegerFilter getMileage() {
        return mileage;
    }

    public IntegerFilter mileage() {
        if (mileage == null) {
            mileage = new IntegerFilter();
        }
        return mileage;
    }

    public void setMileage(IntegerFilter mileage) {
        this.mileage = mileage;
    }

    public FuelTypeFilter getFuel() {
        return fuel;
    }

    public FuelTypeFilter fuel() {
        if (fuel == null) {
            fuel = new FuelTypeFilter();
        }
        return fuel;
    }

    public void setFuel(FuelTypeFilter fuel) {
        this.fuel = fuel;
    }

    public ListingStatusFilter getStatus() {
        return status;
    }

    public ListingStatusFilter status() {
        if (status == null) {
            status = new ListingStatusFilter();
        }
        return status;
    }

    public void setStatus(ListingStatusFilter status) {
        this.status = status;
    }

    public LongFilter getCarModelId() {
        return carModelId;
    }

    public LongFilter carModelId() {
        if (carModelId == null) {
            carModelId = new LongFilter();
        }
        return carModelId;
    }

    public void setCarModelId(LongFilter carModelId) {
        this.carModelId = carModelId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final VehicleListingCriteria that = (VehicleListingCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(year, that.year) &&
            Objects.equals(mileage, that.mileage) &&
            Objects.equals(fuel, that.fuel) &&
            Objects.equals(status, that.status) &&
            Objects.equals(carModelId, that.carModelId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, year, mileage, fuel, status, carModelId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleListingCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (year != null ? "year=" + year + ", " : "") +
            (mileage != null ? "mileage=" + mileage + ", " : "") +
            (fuel != null ? "fuel=" + fuel + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (carModelId != null ? "carModelId=" + carModelId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
