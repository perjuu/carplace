package ca.verticalidigital.carplace.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ca.verticalidigital.carplace.IntegrationTest;
import ca.verticalidigital.carplace.domain.CarModel;
import ca.verticalidigital.carplace.domain.Category;
import ca.verticalidigital.carplace.domain.VehicleListing;
import ca.verticalidigital.carplace.repository.CarModelRepository;
import ca.verticalidigital.carplace.service.CarModelService;
import ca.verticalidigital.carplace.service.criteria.CarModelCriteria;
import ca.verticalidigital.carplace.service.dto.CarModelDTO;
import ca.verticalidigital.carplace.service.mapper.CarModelMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CarModelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarModelResourceIT {

    private static final String DEFAULT_MAKE = "AAAAAAAAAA";
    private static final String UPDATED_MAKE = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Integer DEFAULT_LAUNCH_YEAR = 1;
    private static final Integer UPDATED_LAUNCH_YEAR = 2;
    private static final Integer SMALLER_LAUNCH_YEAR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/car-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CarModelRepository carModelRepository;

    @Mock
    private CarModelRepository carModelRepositoryMock;

    @Autowired
    private CarModelMapper carModelMapper;

    @Mock
    private CarModelService carModelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarModelMockMvc;

    private CarModel carModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModel createEntity(EntityManager em) {
        CarModel carModel = new CarModel().make(DEFAULT_MAKE).model(DEFAULT_MODEL).launchYear(DEFAULT_LAUNCH_YEAR);
        return carModel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModel createUpdatedEntity(EntityManager em) {
        CarModel carModel = new CarModel().make(UPDATED_MAKE).model(UPDATED_MODEL).launchYear(UPDATED_LAUNCH_YEAR);
        return carModel;
    }

    @BeforeEach
    public void initTest() {
        carModel = createEntity(em);
    }

    @Test
    @Transactional
    void createCarModel() throws Exception {
        int databaseSizeBeforeCreate = carModelRepository.findAll().size();
        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);
        restCarModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isCreated());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeCreate + 1);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getMake()).isEqualTo(DEFAULT_MAKE);
        assertThat(testCarModel.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testCarModel.getLaunchYear()).isEqualTo(DEFAULT_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void createCarModelWithExistingId() throws Exception {
        // Create the CarModel with an existing ID
        carModel.setId(1L);
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        int databaseSizeBeforeCreate = carModelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCarModels() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].launchYear").value(hasItem(DEFAULT_LAUNCH_YEAR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarModelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(carModelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carModelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarModelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carModelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carModelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get the carModel
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL_ID, carModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carModel.getId().intValue()))
            .andExpect(jsonPath("$.make").value(DEFAULT_MAKE))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.launchYear").value(DEFAULT_LAUNCH_YEAR));
    }

    @Test
    @Transactional
    void getCarModelsByIdFiltering() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        Long id = carModel.getId();

        defaultCarModelShouldBeFound("id.equals=" + id);
        defaultCarModelShouldNotBeFound("id.notEquals=" + id);

        defaultCarModelShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarModelShouldNotBeFound("id.greaterThan=" + id);

        defaultCarModelShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarModelShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarModelsByMakeIsEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where make equals to DEFAULT_MAKE
        defaultCarModelShouldBeFound("make.equals=" + DEFAULT_MAKE);

        // Get all the carModelList where make equals to UPDATED_MAKE
        defaultCarModelShouldNotBeFound("make.equals=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    void getAllCarModelsByMakeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where make not equals to DEFAULT_MAKE
        defaultCarModelShouldNotBeFound("make.notEquals=" + DEFAULT_MAKE);

        // Get all the carModelList where make not equals to UPDATED_MAKE
        defaultCarModelShouldBeFound("make.notEquals=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    void getAllCarModelsByMakeIsInShouldWork() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where make in DEFAULT_MAKE or UPDATED_MAKE
        defaultCarModelShouldBeFound("make.in=" + DEFAULT_MAKE + "," + UPDATED_MAKE);

        // Get all the carModelList where make equals to UPDATED_MAKE
        defaultCarModelShouldNotBeFound("make.in=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    void getAllCarModelsByMakeIsNullOrNotNull() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where make is not null
        defaultCarModelShouldBeFound("make.specified=true");

        // Get all the carModelList where make is null
        defaultCarModelShouldNotBeFound("make.specified=false");
    }

    @Test
    @Transactional
    void getAllCarModelsByMakeContainsSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where make contains DEFAULT_MAKE
        defaultCarModelShouldBeFound("make.contains=" + DEFAULT_MAKE);

        // Get all the carModelList where make contains UPDATED_MAKE
        defaultCarModelShouldNotBeFound("make.contains=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    void getAllCarModelsByMakeNotContainsSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where make does not contain DEFAULT_MAKE
        defaultCarModelShouldNotBeFound("make.doesNotContain=" + DEFAULT_MAKE);

        // Get all the carModelList where make does not contain UPDATED_MAKE
        defaultCarModelShouldBeFound("make.doesNotContain=" + UPDATED_MAKE);
    }

    @Test
    @Transactional
    void getAllCarModelsByModelIsEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where model equals to DEFAULT_MODEL
        defaultCarModelShouldBeFound("model.equals=" + DEFAULT_MODEL);

        // Get all the carModelList where model equals to UPDATED_MODEL
        defaultCarModelShouldNotBeFound("model.equals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarModelsByModelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where model not equals to DEFAULT_MODEL
        defaultCarModelShouldNotBeFound("model.notEquals=" + DEFAULT_MODEL);

        // Get all the carModelList where model not equals to UPDATED_MODEL
        defaultCarModelShouldBeFound("model.notEquals=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarModelsByModelIsInShouldWork() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where model in DEFAULT_MODEL or UPDATED_MODEL
        defaultCarModelShouldBeFound("model.in=" + DEFAULT_MODEL + "," + UPDATED_MODEL);

        // Get all the carModelList where model equals to UPDATED_MODEL
        defaultCarModelShouldNotBeFound("model.in=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarModelsByModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where model is not null
        defaultCarModelShouldBeFound("model.specified=true");

        // Get all the carModelList where model is null
        defaultCarModelShouldNotBeFound("model.specified=false");
    }

    @Test
    @Transactional
    void getAllCarModelsByModelContainsSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where model contains DEFAULT_MODEL
        defaultCarModelShouldBeFound("model.contains=" + DEFAULT_MODEL);

        // Get all the carModelList where model contains UPDATED_MODEL
        defaultCarModelShouldNotBeFound("model.contains=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarModelsByModelNotContainsSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where model does not contain DEFAULT_MODEL
        defaultCarModelShouldNotBeFound("model.doesNotContain=" + DEFAULT_MODEL);

        // Get all the carModelList where model does not contain UPDATED_MODEL
        defaultCarModelShouldBeFound("model.doesNotContain=" + UPDATED_MODEL);
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear equals to DEFAULT_LAUNCH_YEAR
        defaultCarModelShouldBeFound("launchYear.equals=" + DEFAULT_LAUNCH_YEAR);

        // Get all the carModelList where launchYear equals to UPDATED_LAUNCH_YEAR
        defaultCarModelShouldNotBeFound("launchYear.equals=" + UPDATED_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear not equals to DEFAULT_LAUNCH_YEAR
        defaultCarModelShouldNotBeFound("launchYear.notEquals=" + DEFAULT_LAUNCH_YEAR);

        // Get all the carModelList where launchYear not equals to UPDATED_LAUNCH_YEAR
        defaultCarModelShouldBeFound("launchYear.notEquals=" + UPDATED_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsInShouldWork() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear in DEFAULT_LAUNCH_YEAR or UPDATED_LAUNCH_YEAR
        defaultCarModelShouldBeFound("launchYear.in=" + DEFAULT_LAUNCH_YEAR + "," + UPDATED_LAUNCH_YEAR);

        // Get all the carModelList where launchYear equals to UPDATED_LAUNCH_YEAR
        defaultCarModelShouldNotBeFound("launchYear.in=" + UPDATED_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear is not null
        defaultCarModelShouldBeFound("launchYear.specified=true");

        // Get all the carModelList where launchYear is null
        defaultCarModelShouldNotBeFound("launchYear.specified=false");
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear is greater than or equal to DEFAULT_LAUNCH_YEAR
        defaultCarModelShouldBeFound("launchYear.greaterThanOrEqual=" + DEFAULT_LAUNCH_YEAR);

        // Get all the carModelList where launchYear is greater than or equal to UPDATED_LAUNCH_YEAR
        defaultCarModelShouldNotBeFound("launchYear.greaterThanOrEqual=" + UPDATED_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear is less than or equal to DEFAULT_LAUNCH_YEAR
        defaultCarModelShouldBeFound("launchYear.lessThanOrEqual=" + DEFAULT_LAUNCH_YEAR);

        // Get all the carModelList where launchYear is less than or equal to SMALLER_LAUNCH_YEAR
        defaultCarModelShouldNotBeFound("launchYear.lessThanOrEqual=" + SMALLER_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsLessThanSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear is less than DEFAULT_LAUNCH_YEAR
        defaultCarModelShouldNotBeFound("launchYear.lessThan=" + DEFAULT_LAUNCH_YEAR);

        // Get all the carModelList where launchYear is less than UPDATED_LAUNCH_YEAR
        defaultCarModelShouldBeFound("launchYear.lessThan=" + UPDATED_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void getAllCarModelsByLaunchYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModelList where launchYear is greater than DEFAULT_LAUNCH_YEAR
        defaultCarModelShouldNotBeFound("launchYear.greaterThan=" + DEFAULT_LAUNCH_YEAR);

        // Get all the carModelList where launchYear is greater than SMALLER_LAUNCH_YEAR
        defaultCarModelShouldBeFound("launchYear.greaterThan=" + SMALLER_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void getAllCarModelsByVehicleListingIsEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);
        VehicleListing vehicleListing;
        if (TestUtil.findAll(em, VehicleListing.class).isEmpty()) {
            vehicleListing = VehicleListingResourceIT.createEntity(em);
            em.persist(vehicleListing);
            em.flush();
        } else {
            vehicleListing = TestUtil.findAll(em, VehicleListing.class).get(0);
        }
        em.persist(vehicleListing);
        em.flush();
        carModel.addVehicleListing(vehicleListing);
        carModelRepository.saveAndFlush(carModel);
        Long vehicleListingId = vehicleListing.getId();

        // Get all the carModelList where vehicleListing equals to vehicleListingId
        defaultCarModelShouldBeFound("vehicleListingId.equals=" + vehicleListingId);

        // Get all the carModelList where vehicleListing equals to (vehicleListingId + 1)
        defaultCarModelShouldNotBeFound("vehicleListingId.equals=" + (vehicleListingId + 1));
    }

    @Test
    @Transactional
    void getAllCarModelsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        carModel.addCategory(category);
        carModelRepository.saveAndFlush(carModel);
        Long categoryId = category.getId();

        // Get all the carModelList where category equals to categoryId
        defaultCarModelShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the carModelList where category equals to (categoryId + 1)
        defaultCarModelShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarModelShouldBeFound(String filter) throws Exception {
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].launchYear").value(hasItem(DEFAULT_LAUNCH_YEAR)));

        // Check, that the count call also returns 1
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarModelShouldNotBeFound(String filter) throws Exception {
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCarModel() throws Exception {
        // Get the carModel
        restCarModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Update the carModel
        CarModel updatedCarModel = carModelRepository.findById(carModel.getId()).get();
        // Disconnect from session so that the updates on updatedCarModel are not directly saved in db
        em.detach(updatedCarModel);
        updatedCarModel.make(UPDATED_MAKE).model(UPDATED_MODEL).launchYear(UPDATED_LAUNCH_YEAR);
        CarModelDTO carModelDTO = carModelMapper.toDto(updatedCarModel);

        restCarModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getMake()).isEqualTo(UPDATED_MAKE);
        assertThat(testCarModel.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCarModel.getLaunchYear()).isEqualTo(UPDATED_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void putNonExistingCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();
        carModel.setId(count.incrementAndGet());

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();
        carModel.setId(count.incrementAndGet());

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(carModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();
        carModel.setId(count.incrementAndGet());

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(carModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarModelWithPatch() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Update the carModel using partial update
        CarModel partialUpdatedCarModel = new CarModel();
        partialUpdatedCarModel.setId(carModel.getId());

        partialUpdatedCarModel.make(UPDATED_MAKE).model(UPDATED_MODEL);

        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarModel))
            )
            .andExpect(status().isOk());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getMake()).isEqualTo(UPDATED_MAKE);
        assertThat(testCarModel.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCarModel.getLaunchYear()).isEqualTo(DEFAULT_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void fullUpdateCarModelWithPatch() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Update the carModel using partial update
        CarModel partialUpdatedCarModel = new CarModel();
        partialUpdatedCarModel.setId(carModel.getId());

        partialUpdatedCarModel.make(UPDATED_MAKE).model(UPDATED_MODEL).launchYear(UPDATED_LAUNCH_YEAR);

        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCarModel))
            )
            .andExpect(status().isOk());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
        CarModel testCarModel = carModelList.get(carModelList.size() - 1);
        assertThat(testCarModel.getMake()).isEqualTo(UPDATED_MAKE);
        assertThat(testCarModel.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testCarModel.getLaunchYear()).isEqualTo(UPDATED_LAUNCH_YEAR);
    }

    @Test
    @Transactional
    void patchNonExistingCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();
        carModel.setId(count.incrementAndGet());

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();
        carModel.setId(count.incrementAndGet());

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(carModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarModel() throws Exception {
        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();
        carModel.setId(count.incrementAndGet());

        // Create the CarModel
        CarModelDTO carModelDTO = carModelMapper.toDto(carModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarModelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(carModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CarModel in the database
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        int databaseSizeBeforeDelete = carModelRepository.findAll().size();

        // Delete the carModel
        restCarModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, carModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CarModel> carModelList = carModelRepository.findAll();
        assertThat(carModelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
