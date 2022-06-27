package ca.verticalidigital.carplace.service.dto;

import ca.verticalidigital.carplace.domain.Dealer;
import ca.verticalidigital.carplace.domain.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DealerDTO implements Serializable {

    private Long id;

    private String name;

    private String city;

    private String address;

    private String phone;

    public DealerDTO() {
    }

    public DealerDTO(Dealer dealer) {
        this.id = dealer.getId();
        this.name = dealer.getName();
        this.city = dealer.getCity();
        this.address = dealer.getAddress();
        this.phone = dealer.getPhone();
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DealerDTO)) {
            return false;
        }

        DealerDTO dealerDTO = (DealerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dealerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "DealerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", city='" + getCity() + "'" +
            ", address=" + getAddress() +
            ", phone=" + getPhone() +
            "}";
    }

}
