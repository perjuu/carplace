package ca.verticalidigital.carplace.web.rest;

import ca.verticalidigital.carplace.repository.VehicleListingRepository;
import ca.verticalidigital.carplace.service.VehicleListingQueryService;
import ca.verticalidigital.carplace.service.VehicleListingService;
import ca.verticalidigital.carplace.service.criteria.VehicleListingCriteria;
import ca.verticalidigital.carplace.service.dto.VehicleListingDTO;
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
 * REST controller for managing {@link ca.verticalidigital.carplace.domain.VehicleListing}.
 */
@RestController
@RequestMapping("/api")
public class VehicleListingResource {

    private final Logger log = LoggerFactory.getLogger(VehicleListingResource.class);

    private static final String ENTITY_NAME = "vehicleListing";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VehicleListingService vehicleListingService;

    private final VehicleListingRepository vehicleListingRepository;

    private final VehicleListingQueryService vehicleListingQueryService;

    public VehicleListingResource(
        VehicleListingService vehicleListingService,
        VehicleListingRepository vehicleListingRepository,
        VehicleListingQueryService vehicleListingQueryService
    ) {
        this.vehicleListingService = vehicleListingService;
        this.vehicleListingRepository = vehicleListingRepository;
        this.vehicleListingQueryService = vehicleListingQueryService;
    }

    /**
     * {@code POST  /vehicle-listings} : Create a new vehicleListing.
     *
     * @param vehicleListingDTO the vehicleListingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicleListingDTO, or with status {@code 400 (Bad Request)} if the vehicleListing has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicle-listings")
    public ResponseEntity<VehicleListingDTO> createVehicleListing(@RequestBody VehicleListingDTO vehicleListingDTO)
        throws URISyntaxException {
        log.debug("REST request to save VehicleListing : {}", vehicleListingDTO);
        if (vehicleListingDTO.getId() != null) {
            throw new BadRequestAlertException("A new vehicleListing cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VehicleListingDTO result = vehicleListingService.save(vehicleListingDTO);
        return ResponseEntity
            .created(new URI("/api/vehicle-listings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vehicle-listings/:id} : Updates an existing vehicleListing.
     *
     * @param id the id of the vehicleListingDTO to save.
     * @param vehicleListingDTO the vehicleListingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleListingDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleListingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicleListingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicle-listings/{id}")
    public ResponseEntity<VehicleListingDTO> updateVehicleListing(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleListingDTO vehicleListingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update VehicleListing : {}, {}", id, vehicleListingDTO);
        if (vehicleListingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleListingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleListingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        VehicleListingDTO result = vehicleListingService.update(vehicleListingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleListingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vehicle-listings/:id} : Partial updates given fields of an existing vehicleListing, field will ignore if it is null
     *
     * @param id the id of the vehicleListingDTO to save.
     * @param vehicleListingDTO the vehicleListingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicleListingDTO,
     * or with status {@code 400 (Bad Request)} if the vehicleListingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the vehicleListingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the vehicleListingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vehicle-listings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VehicleListingDTO> partialUpdateVehicleListing(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VehicleListingDTO vehicleListingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update VehicleListing partially : {}, {}", id, vehicleListingDTO);
        if (vehicleListingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vehicleListingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vehicleListingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VehicleListingDTO> result = vehicleListingService.partialUpdate(vehicleListingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicleListingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /vehicle-listings} : get all the vehicleListings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicleListings in body.
     */
    @GetMapping("/vehicle-listings")
    public ResponseEntity<List<VehicleListingDTO>> getAllVehicleListings(
        VehicleListingCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get VehicleListings by criteria: {}", criteria);
        Page<VehicleListingDTO> page = vehicleListingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vehicle-listings/count} : count all the vehicleListings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/vehicle-listings/count")
    public ResponseEntity<Long> countVehicleListings(VehicleListingCriteria criteria) {
        log.debug("REST request to count VehicleListings by criteria: {}", criteria);
        return ResponseEntity.ok().body(vehicleListingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /vehicle-listings/:id} : get the "id" vehicleListing.
     *
     * @param id the id of the vehicleListingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicleListingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicle-listings/{id}")
    public ResponseEntity<VehicleListingDTO> getVehicleListing(@PathVariable Long id) {
        log.debug("REST request to get VehicleListing : {}", id);
        Optional<VehicleListingDTO> vehicleListingDTO = vehicleListingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vehicleListingDTO);
    }

    /**
     * {@code DELETE  /vehicle-listings/:id} : delete the "id" vehicleListing.
     *
     * @param id the id of the vehicleListingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicle-listings/{id}")
    public ResponseEntity<Void> deleteVehicleListing(@PathVariable Long id) {
        log.debug("REST request to delete VehicleListing : {}", id);
        vehicleListingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
