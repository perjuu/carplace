package ca.verticalidigital.carplace.service.criteria;

import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

@ParameterObject
public class DealerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter city;

    private StringFilter address;

    private StringFilter phone;

    public DealerCriteria(DealerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
    }

    @Override
    public Criteria copy() {
        return new DealerCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name(){
        if (name == null){
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCity() {
        return city;
    }

    public StringFilter city(){
        if (city == null){
            city = new StringFilter();
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address(){
        if (address == null){
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone(){
        if (phone == null){
            phone = new StringFilter();
        }
        return  phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DealerCriteria that = (DealerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(city, that.city) &&
                Objects.equals(address, that.address) &&
                Objects.equals(phone, that.phone)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city, address, phone);
    }

    @Override
    public String toString() {
        return "DealerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (city != null ? "city=" + city + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            "}";
    }

}
