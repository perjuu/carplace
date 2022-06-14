package ca.verticalidigital.carplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.verticalidigital.carplace.IntegrationTest;
import ca.verticalidigital.carplace.domain.CarModel;
import ca.verticalidigital.carplace.domain.VehicleListing;
import ca.verticalidigital.carplace.domain.enumeration.FuelType;
import ca.verticalidigital.carplace.domain.enumeration.ListingStatus;
import ca.verticalidigital.carplace.repository.VehicleListingRepository;
import ca.verticalidigital.carplace.service.criteria.VehicleListingCriteria;
import ca.verticalidigital.carplace.service.dto.VehicleListingDTO;
import ca.verticalidigital.carplace.service.mapper.VehicleListingMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VehicleListingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VehicleListingResourceIT {

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;
    private static final Integer SMALLER_PRICE = 1 - 1;

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final Integer DEFAULT_MILEAGE = 1;
    private static final Integer UPDATED_MILEAGE = 2;
    private static final Integer SMALLER_MILEAGE = 1 - 1;

    private static final FuelType DEFAULT_FUEL = FuelType.DIESEL;
    private static final FuelType UPDATED_FUEL = FuelType.PETROL;

    private static final ListingStatus DEFAULT_STATUS = ListingStatus.PENDING;
    private static final ListingStatus UPDATED_STATUS = ListingStatus.PUBLISHED;

    private static final String ENTITY_API_URL = "/api/vehicle-listings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VehicleListingRepository vehicleListingRepository;

    @Autowired
    private VehicleListingMapper vehicleListingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehicleListingMockMvc;

    private VehicleListing vehicleListing;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleListing createEntity(EntityManager em) {
        VehicleListing vehicleListing = new VehicleListing()
            .price(DEFAULT_PRICE)
            .year(DEFAULT_YEAR)
            .mileage(DEFAULT_MILEAGE)
            .fuel(DEFAULT_FUEL)
            .status(DEFAULT_STATUS);
        return vehicleListing;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VehicleListing createUpdatedEntity(EntityManager em) {
        VehicleListing vehicleListing = new VehicleListing()
            .price(UPDATED_PRICE)
            .year(UPDATED_YEAR)
            .mileage(UPDATED_MILEAGE)
            .fuel(UPDATED_FUEL)
            .status(UPDATED_STATUS);
        return vehicleListing;
    }

    @BeforeEach
    public void initTest() {
        vehicleListing = createEntity(em);
    }

    @Test
    @Transactional
    void createVehicleListing() throws Exception {
        int databaseSizeBeforeCreate = vehicleListingRepository.findAll().size();
        // Create the VehicleListing
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);
        restVehicleListingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isCreated());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeCreate + 1);
        VehicleListing testVehicleListing = vehicleListingList.get(vehicleListingList.size() - 1);
        assertThat(testVehicleListing.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVehicleListing.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testVehicleListing.getMileage()).isEqualTo(DEFAULT_MILEAGE);
        assertThat(testVehicleListing.getFuel()).isEqualTo(DEFAULT_FUEL);
        assertThat(testVehicleListing.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createVehicleListingWithExistingId() throws Exception {
        // Create the VehicleListing with an existing ID
        vehicleListing.setId(1L);
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);

        int databaseSizeBeforeCreate = vehicleListingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehicleListingMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVehicleListings() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList
        restVehicleListingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleListing.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].fuel").value(hasItem(DEFAULT_FUEL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getVehicleListing() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get the vehicleListing
        restVehicleListingMockMvc
            .perform(get(ENTITY_API_URL_ID, vehicleListing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vehicleListing.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.mileage").value(DEFAULT_MILEAGE))
            .andExpect(jsonPath("$.fuel").value(DEFAULT_FUEL.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getVehicleListingsByIdFiltering() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        Long id = vehicleListing.getId();

        defaultVehicleListingShouldBeFound("id.equals=" + id);
        defaultVehicleListingShouldNotBeFound("id.notEquals=" + id);

        defaultVehicleListingShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVehicleListingShouldNotBeFound("id.greaterThan=" + id);

        defaultVehicleListingShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVehicleListingShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price equals to DEFAULT_PRICE
        defaultVehicleListingShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the vehicleListingList where price equals to UPDATED_PRICE
        defaultVehicleListingShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price not equals to DEFAULT_PRICE
        defaultVehicleListingShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the vehicleListingList where price not equals to UPDATED_PRICE
        defaultVehicleListingShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultVehicleListingShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the vehicleListingList where price equals to UPDATED_PRICE
        defaultVehicleListingShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price is not null
        defaultVehicleListingShouldBeFound("price.specified=true");

        // Get all the vehicleListingList where price is null
        defaultVehicleListingShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price is greater than or equal to DEFAULT_PRICE
        defaultVehicleListingShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the vehicleListingList where price is greater than or equal to UPDATED_PRICE
        defaultVehicleListingShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price is less than or equal to DEFAULT_PRICE
        defaultVehicleListingShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the vehicleListingList where price is less than or equal to SMALLER_PRICE
        defaultVehicleListingShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price is less than DEFAULT_PRICE
        defaultVehicleListingShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the vehicleListingList where price is less than UPDATED_PRICE
        defaultVehicleListingShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where price is greater than DEFAULT_PRICE
        defaultVehicleListingShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the vehicleListingList where price is greater than SMALLER_PRICE
        defaultVehicleListingShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year equals to DEFAULT_YEAR
        defaultVehicleListingShouldBeFound("year.equals=" + DEFAULT_YEAR);

        // Get all the vehicleListingList where year equals to UPDATED_YEAR
        defaultVehicleListingShouldNotBeFound("year.equals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year not equals to DEFAULT_YEAR
        defaultVehicleListingShouldNotBeFound("year.notEquals=" + DEFAULT_YEAR);

        // Get all the vehicleListingList where year not equals to UPDATED_YEAR
        defaultVehicleListingShouldBeFound("year.notEquals=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year in DEFAULT_YEAR or UPDATED_YEAR
        defaultVehicleListingShouldBeFound("year.in=" + DEFAULT_YEAR + "," + UPDATED_YEAR);

        // Get all the vehicleListingList where year equals to UPDATED_YEAR
        defaultVehicleListingShouldNotBeFound("year.in=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year is not null
        defaultVehicleListingShouldBeFound("year.specified=true");

        // Get all the vehicleListingList where year is null
        defaultVehicleListingShouldNotBeFound("year.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year is greater than or equal to DEFAULT_YEAR
        defaultVehicleListingShouldBeFound("year.greaterThanOrEqual=" + DEFAULT_YEAR);

        // Get all the vehicleListingList where year is greater than or equal to UPDATED_YEAR
        defaultVehicleListingShouldNotBeFound("year.greaterThanOrEqual=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year is less than or equal to DEFAULT_YEAR
        defaultVehicleListingShouldBeFound("year.lessThanOrEqual=" + DEFAULT_YEAR);

        // Get all the vehicleListingList where year is less than or equal to SMALLER_YEAR
        defaultVehicleListingShouldNotBeFound("year.lessThanOrEqual=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsLessThanSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year is less than DEFAULT_YEAR
        defaultVehicleListingShouldNotBeFound("year.lessThan=" + DEFAULT_YEAR);

        // Get all the vehicleListingList where year is less than UPDATED_YEAR
        defaultVehicleListingShouldBeFound("year.lessThan=" + UPDATED_YEAR);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where year is greater than DEFAULT_YEAR
        defaultVehicleListingShouldNotBeFound("year.greaterThan=" + DEFAULT_YEAR);

        // Get all the vehicleListingList where year is greater than SMALLER_YEAR
        defaultVehicleListingShouldBeFound("year.greaterThan=" + SMALLER_YEAR);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage equals to DEFAULT_MILEAGE
        defaultVehicleListingShouldBeFound("mileage.equals=" + DEFAULT_MILEAGE);

        // Get all the vehicleListingList where mileage equals to UPDATED_MILEAGE
        defaultVehicleListingShouldNotBeFound("mileage.equals=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage not equals to DEFAULT_MILEAGE
        defaultVehicleListingShouldNotBeFound("mileage.notEquals=" + DEFAULT_MILEAGE);

        // Get all the vehicleListingList where mileage not equals to UPDATED_MILEAGE
        defaultVehicleListingShouldBeFound("mileage.notEquals=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage in DEFAULT_MILEAGE or UPDATED_MILEAGE
        defaultVehicleListingShouldBeFound("mileage.in=" + DEFAULT_MILEAGE + "," + UPDATED_MILEAGE);

        // Get all the vehicleListingList where mileage equals to UPDATED_MILEAGE
        defaultVehicleListingShouldNotBeFound("mileage.in=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage is not null
        defaultVehicleListingShouldBeFound("mileage.specified=true");

        // Get all the vehicleListingList where mileage is null
        defaultVehicleListingShouldNotBeFound("mileage.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage is greater than or equal to DEFAULT_MILEAGE
        defaultVehicleListingShouldBeFound("mileage.greaterThanOrEqual=" + DEFAULT_MILEAGE);

        // Get all the vehicleListingList where mileage is greater than or equal to UPDATED_MILEAGE
        defaultVehicleListingShouldNotBeFound("mileage.greaterThanOrEqual=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage is less than or equal to DEFAULT_MILEAGE
        defaultVehicleListingShouldBeFound("mileage.lessThanOrEqual=" + DEFAULT_MILEAGE);

        // Get all the vehicleListingList where mileage is less than or equal to SMALLER_MILEAGE
        defaultVehicleListingShouldNotBeFound("mileage.lessThanOrEqual=" + SMALLER_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsLessThanSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage is less than DEFAULT_MILEAGE
        defaultVehicleListingShouldNotBeFound("mileage.lessThan=" + DEFAULT_MILEAGE);

        // Get all the vehicleListingList where mileage is less than UPDATED_MILEAGE
        defaultVehicleListingShouldBeFound("mileage.lessThan=" + UPDATED_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByMileageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where mileage is greater than DEFAULT_MILEAGE
        defaultVehicleListingShouldNotBeFound("mileage.greaterThan=" + DEFAULT_MILEAGE);

        // Get all the vehicleListingList where mileage is greater than SMALLER_MILEAGE
        defaultVehicleListingShouldBeFound("mileage.greaterThan=" + SMALLER_MILEAGE);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByFuelIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where fuel equals to DEFAULT_FUEL
        defaultVehicleListingShouldBeFound("fuel.equals=" + DEFAULT_FUEL);

        // Get all the vehicleListingList where fuel equals to UPDATED_FUEL
        defaultVehicleListingShouldNotBeFound("fuel.equals=" + UPDATED_FUEL);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByFuelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where fuel not equals to DEFAULT_FUEL
        defaultVehicleListingShouldNotBeFound("fuel.notEquals=" + DEFAULT_FUEL);

        // Get all the vehicleListingList where fuel not equals to UPDATED_FUEL
        defaultVehicleListingShouldBeFound("fuel.notEquals=" + UPDATED_FUEL);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByFuelIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where fuel in DEFAULT_FUEL or UPDATED_FUEL
        defaultVehicleListingShouldBeFound("fuel.in=" + DEFAULT_FUEL + "," + UPDATED_FUEL);

        // Get all the vehicleListingList where fuel equals to UPDATED_FUEL
        defaultVehicleListingShouldNotBeFound("fuel.in=" + UPDATED_FUEL);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByFuelIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where fuel is not null
        defaultVehicleListingShouldBeFound("fuel.specified=true");

        // Get all the vehicleListingList where fuel is null
        defaultVehicleListingShouldNotBeFound("fuel.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleListingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where status equals to DEFAULT_STATUS
        defaultVehicleListingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the vehicleListingList where status equals to UPDATED_STATUS
        defaultVehicleListingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where status not equals to DEFAULT_STATUS
        defaultVehicleListingShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the vehicleListingList where status not equals to UPDATED_STATUS
        defaultVehicleListingShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultVehicleListingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the vehicleListingList where status equals to UPDATED_STATUS
        defaultVehicleListingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllVehicleListingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        // Get all the vehicleListingList where status is not null
        defaultVehicleListingShouldBeFound("status.specified=true");

        // Get all the vehicleListingList where status is null
        defaultVehicleListingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllVehicleListingsByCarModelIsEqualToSomething() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);
        CarModel carModel;
        if (TestUtil.findAll(em, CarModel.class).isEmpty()) {
            carModel = CarModelResourceIT.createEntity(em);
            em.persist(carModel);
            em.flush();
        } else {
            carModel = TestUtil.findAll(em, CarModel.class).get(0);
        }
        em.persist(carModel);
        em.flush();
        vehicleListing.setCarModel(carModel);
        vehicleListingRepository.saveAndFlush(vehicleListing);
        Long carModelId = carModel.getId();

        // Get all the vehicleListingList where carModel equals to carModelId
        defaultVehicleListingShouldBeFound("carModelId.equals=" + carModelId);

        // Get all the vehicleListingList where carModel equals to (carModelId + 1)
        defaultVehicleListingShouldNotBeFound("carModelId.equals=" + (carModelId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVehicleListingShouldBeFound(String filter) throws Exception {
        restVehicleListingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vehicleListing.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].mileage").value(hasItem(DEFAULT_MILEAGE)))
            .andExpect(jsonPath("$.[*].fuel").value(hasItem(DEFAULT_FUEL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restVehicleListingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVehicleListingShouldNotBeFound(String filter) throws Exception {
        restVehicleListingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVehicleListingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVehicleListing() throws Exception {
        // Get the vehicleListing
        restVehicleListingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVehicleListing() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();

        // Update the vehicleListing
        VehicleListing updatedVehicleListing = vehicleListingRepository.findById(vehicleListing.getId()).get();
        // Disconnect from session so that the updates on updatedVehicleListing are not directly saved in db
        em.detach(updatedVehicleListing);
        updatedVehicleListing.price(UPDATED_PRICE).year(UPDATED_YEAR).mileage(UPDATED_MILEAGE).fuel(UPDATED_FUEL).status(UPDATED_STATUS);
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(updatedVehicleListing);

        restVehicleListingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleListingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isOk());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
        VehicleListing testVehicleListing = vehicleListingList.get(vehicleListingList.size() - 1);
        assertThat(testVehicleListing.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVehicleListing.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testVehicleListing.getMileage()).isEqualTo(UPDATED_MILEAGE);
        assertThat(testVehicleListing.getFuel()).isEqualTo(UPDATED_FUEL);
        assertThat(testVehicleListing.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingVehicleListing() throws Exception {
        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();
        vehicleListing.setId(count.incrementAndGet());

        // Create the VehicleListing
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleListingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vehicleListingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVehicleListing() throws Exception {
        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();
        vehicleListing.setId(count.incrementAndGet());

        // Create the VehicleListing
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleListingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVehicleListing() throws Exception {
        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();
        vehicleListing.setId(count.incrementAndGet());

        // Create the VehicleListing
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleListingMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVehicleListingWithPatch() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();

        // Update the vehicleListing using partial update
        VehicleListing partialUpdatedVehicleListing = new VehicleListing();
        partialUpdatedVehicleListing.setId(vehicleListing.getId());

        partialUpdatedVehicleListing.price(UPDATED_PRICE).mileage(UPDATED_MILEAGE).fuel(UPDATED_FUEL).status(UPDATED_STATUS);

        restVehicleListingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleListing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleListing))
            )
            .andExpect(status().isOk());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
        VehicleListing testVehicleListing = vehicleListingList.get(vehicleListingList.size() - 1);
        assertThat(testVehicleListing.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVehicleListing.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testVehicleListing.getMileage()).isEqualTo(UPDATED_MILEAGE);
        assertThat(testVehicleListing.getFuel()).isEqualTo(UPDATED_FUEL);
        assertThat(testVehicleListing.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateVehicleListingWithPatch() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();

        // Update the vehicleListing using partial update
        VehicleListing partialUpdatedVehicleListing = new VehicleListing();
        partialUpdatedVehicleListing.setId(vehicleListing.getId());

        partialUpdatedVehicleListing
            .price(UPDATED_PRICE)
            .year(UPDATED_YEAR)
            .mileage(UPDATED_MILEAGE)
            .fuel(UPDATED_FUEL)
            .status(UPDATED_STATUS);

        restVehicleListingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVehicleListing.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVehicleListing))
            )
            .andExpect(status().isOk());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
        VehicleListing testVehicleListing = vehicleListingList.get(vehicleListingList.size() - 1);
        assertThat(testVehicleListing.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVehicleListing.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testVehicleListing.getMileage()).isEqualTo(UPDATED_MILEAGE);
        assertThat(testVehicleListing.getFuel()).isEqualTo(UPDATED_FUEL);
        assertThat(testVehicleListing.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingVehicleListing() throws Exception {
        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();
        vehicleListing.setId(count.incrementAndGet());

        // Create the VehicleListing
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehicleListingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vehicleListingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVehicleListing() throws Exception {
        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();
        vehicleListing.setId(count.incrementAndGet());

        // Create the VehicleListing
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleListingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVehicleListing() throws Exception {
        int databaseSizeBeforeUpdate = vehicleListingRepository.findAll().size();
        vehicleListing.setId(count.incrementAndGet());

        // Create the VehicleListing
        VehicleListingDTO vehicleListingDTO = vehicleListingMapper.toDto(vehicleListing);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVehicleListingMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vehicleListingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the VehicleListing in the database
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVehicleListing() throws Exception {
        // Initialize the database
        vehicleListingRepository.saveAndFlush(vehicleListing);

        int databaseSizeBeforeDelete = vehicleListingRepository.findAll().size();

        // Delete the vehicleListing
        restVehicleListingMockMvc
            .perform(delete(ENTITY_API_URL_ID, vehicleListing.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VehicleListing> vehicleListingList = vehicleListingRepository.findAll();
        assertThat(vehicleListingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
