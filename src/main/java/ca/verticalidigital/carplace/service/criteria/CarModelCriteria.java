package ca.verticalidigital.carplace.service.criteria;

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
 * Criteria class for the {@link ca.verticalidigital.carplace.domain.CarModel} entity. This class is used
 * in {@link ca.verticalidigital.carplace.web.rest.CarModelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /car-models?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CarModelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter make;

    private StringFilter model;

    private IntegerFilter launchYear;

    private LongFilter vehicleListingId;

    private LongFilter categoryId;

    private Boolean distinct;

    public CarModelCriteria() {}

    public CarModelCriteria(CarModelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.make = other.make == null ? null : other.make.copy();
        this.model = other.model == null ? null : other.model.copy();
        this.launchYear = other.launchYear == null ? null : other.launchYear.copy();
        this.vehicleListingId = other.vehicleListingId == null ? null : other.vehicleListingId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CarModelCriteria copy() {
        return new CarModelCriteria(this);
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

    public StringFilter getMake() {
        return make;
    }

    public StringFilter make() {
        if (make == null) {
            make = new StringFilter();
        }
        return make;
    }

    public void setMake(StringFilter make) {
        this.make = make;
    }

    public StringFilter getModel() {
        return model;
    }

    public StringFilter model() {
        if (model == null) {
            model = new StringFilter();
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public IntegerFilter getLaunchYear() {
        return launchYear;
    }

    public IntegerFilter launchYear() {
        if (launchYear == null) {
            launchYear = new IntegerFilter();
        }
        return launchYear;
    }

    public void setLaunchYear(IntegerFilter launchYear) {
        this.launchYear = launchYear;
    }

    public LongFilter getVehicleListingId() {
        return vehicleListingId;
    }

    public LongFilter vehicleListingId() {
        if (vehicleListingId == null) {
            vehicleListingId = new LongFilter();
        }
        return vehicleListingId;
    }

    public void setVehicleListingId(LongFilter vehicleListingId) {
        this.vehicleListingId = vehicleListingId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final CarModelCriteria that = (CarModelCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(make, that.make) &&
            Objects.equals(model, that.model) &&
            Objects.equals(launchYear, that.launchYear) &&
            Objects.equals(vehicleListingId, that.vehicleListingId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, make, model, launchYear, vehicleListingId, categoryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CarModelCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (make != null ? "make=" + make + ", " : "") +
            (model != null ? "model=" + model + ", " : "") +
            (launchYear != null ? "launchYear=" + launchYear + ", " : "") +
            (vehicleListingId != null ? "vehicleListingId=" + vehicleListingId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
