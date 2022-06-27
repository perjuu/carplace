package ca.verticalidigital.carplace.web.rest;

import ca.verticalidigital.carplace.IntegrationTest;
import ca.verticalidigital.carplace.domain.Dealer;
import ca.verticalidigital.carplace.repository.DealerRepository;
import ca.verticalidigital.carplace.service.DealerMapper;
import ca.verticalidigital.carplace.service.DealerMapperImpl;
import ca.verticalidigital.carplace.service.dto.DealerDTO;
import ca.verticalidigital.carplace.service.mapper.DealerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DealerResourceIT {

    private static final String DEFAULT_NAME = "NAME";
    private static final String UPDATED_NAME = "UP_NAME";

    private static final String DEFAULT_CITY = "CITY";
    private static final String UPDATED_CITY = "UP_CITY";

    private static final String DEFAULT_ADDRESS = "ADDRESS";
    private static final String UPDATED_ADDRESS = "UP_ADDRESS";

    private static final String DEFAULT_PHONE = "PHONE";
    private static final String UPDATED_PHONE = "UP_PHONE";

    private static final String ENTITY_API_URL = "/api/dealer";

    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DealerRepository dealerRepository;

    @Mock
    private DealerRepository dealerRepositoryMock;

    @Autowired
    private DealerMapper dealerMapper;

    @Mock
    private DealerService dealerServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealerMockMvc;

    private Dealer dealer;

    public static Dealer createEntity(EntityManager em){
        Dealer dealer = new Dealer()
            .name(DEFAULT_NAME)
            .city(DEFAULT_CITY)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE);
        return dealer;
    }

    public static Dealer createUpdatedEntity(EntityManager em) {
        Dealer dealer = new Dealer()
            .name(UPDATED_NAME)
            .city(UPDATED_CITY)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE);
        return dealer;
    }

    @BeforeEach
    public void initTest(){ dealer = createEntity(em); }

    @Test
    @Transactional
    void createDealer() throws  Exception{
        int databaseSizeBeforeCreate = dealerRepository.findAll().size();
        // Create the Dealer
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);
        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database
        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate +1);
        Dealer testDealer = dealerList.get(dealerList.size() -1);
        assertThat(testDealer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDealer.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testDealer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testDealer.getPhone()).isEqualTo(DEFAULT_PHONE);

    }

    @Test
    @Transactional
    void createCarModelWithExistingId() throws Exception {
        // Create the CarModel with an existing ID
        dealer.setId(1L);
        DealerDTO dealerDTO = dealerMapper.toDto(dealer);

        int databaseSizeBeforeCreate = dealerRepository.findAll().size();

        restDealerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dealer in the database

        List<Dealer> dealerList = dealerRepository.findAll();
        assertThat(dealerList).hasSize(databaseSizeBeforeCreate);

    }

    @Test
    @Transactional
    void getAllDealers() throws Exception {
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList
        restDealerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }

    @Test
    @Transactional
    void getDealer() throws Exception {
        dealerRepository.saveAndFlush(dealer);

        // Get all the dealerList
        restDealerMockMvc
            .perform(get(ENTITY_API_URL_ID, dealer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }


}
