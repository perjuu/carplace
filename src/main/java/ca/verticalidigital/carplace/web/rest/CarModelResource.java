package ca.verticalidigital.carplace.web.rest;

import ca.verticalidigital.carplace.repository.CarModelRepository;
import ca.verticalidigital.carplace.service.CarModelQueryService;
import ca.verticalidigital.carplace.service.CarModelService;
import ca.verticalidigital.carplace.service.criteria.CarModelCriteria;
import ca.verticalidigital.carplace.service.dto.CarModelDTO;
import ca.verticalidigital.carplace.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ca.verticalidigital.carplace.domain.CarModel}.
 */
@RestController
@RequestMapping("/api")
public class CarModelResource {

    private final Logger log = LoggerFactory.getLogger(CarModelResource.class);

    private static final String ENTITY_NAME = "carModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarModelService carModelService;

    private final CarModelRepository carModelRepository;

    private final CarModelQueryService carModelQueryService;

    public CarModelResource(
        CarModelService carModelService,
        CarModelRepository carModelRepository,
        CarModelQueryService carModelQueryService
    ) {
        this.carModelService = carModelService;
        this.carModelRepository = carModelRepository;
        this.carModelQueryService = carModelQueryService;
    }

    /**
     * {@code POST  /car-models} : Create a new carModel.
     *
     * @param carModelDTO the carModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carModelDTO, or with status {@code 400 (Bad Request)} if the carModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/car-models")
    public ResponseEntity<CarModelDTO> createCarModel(@RequestBody CarModelDTO carModelDTO) throws URISyntaxException {
        log.debug("REST request to save CarModel : {}", carModelDTO);
        if (carModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new carModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarModelDTO result = carModelService.save(carModelDTO);
        return ResponseEntity
            .created(new URI("/api/car-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /car-models/:id} : Updates an existing carModel.
     *
     * @param id the id of the carModelDTO to save.
     * @param carModelDTO the carModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carModelDTO,
     * or with status {@code 400 (Bad Request)} if the carModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/car-models/{id}")
    public ResponseEntity<CarModelDTO> updateCarModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CarModelDTO carModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CarModel : {}, {}", id, carModelDTO);
        if (carModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarModelDTO result = carModelService.update(carModelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carModelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /car-models/:id} : Partial updates given fields of an existing carModel, field will ignore if it is null
     *
     * @param id the id of the carModelDTO to save.
     * @param carModelDTO the carModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carModelDTO,
     * or with status {@code 400 (Bad Request)} if the carModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/car-models/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarModelDTO> partialUpdateCarModel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CarModelDTO carModelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CarModel partially : {}, {}", id, carModelDTO);
        if (carModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarModelDTO> result = carModelService.partialUpdate(carModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /car-models} : get all the carModels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carModels in body.
     */
    @GetMapping("/car-models")
    public ResponseEntity<List<CarModelDTO>> getAllCarModels(
        CarModelCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CarModels by criteria: {}", criteria);
        Page<CarModelDTO> page = carModelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /car-models/count} : count all the carModels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/car-models/count")
    public ResponseEntity<Long> countCarModels(CarModelCriteria criteria) {
        log.debug("REST request to count CarModels by criteria: {}", criteria);
        return ResponseEntity.ok().body(carModelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /car-models/:id} : get the "id" carModel.
     *
     * @param id the id of the carModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/car-models/{id}")
    public ResponseEntity<CarModelDTO> getCarModel(@PathVariable Long id) {
        log.debug("REST request to get CarModel : {}", id);
        Optional<CarModelDTO> carModelDTO = carModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carModelDTO);
    }

    /**
     * {@code DELETE  /car-models/:id} : delete the "id" carModel.
     *
     * @param id the id of the carModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/car-models/{id}")
    public ResponseEntity<Void> deleteCarModel(@PathVariable Long id) {
        log.debug("REST request to delete CarModel : {}", id);
        carModelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
