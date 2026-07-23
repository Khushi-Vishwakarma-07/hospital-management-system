package com.hospital.management.hospitalmanagementsystem.prescription.repository;

import com.hospital.management.hospitalmanagementsystem.prescription.entity.Prescription;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NullMarked
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    boolean existsByMedicalRecord_Id(Long medicalRecordId);

    @EntityGraph(attributePaths = {"medicalRecord"})
    Optional<Prescription> findByMedicalRecord_Id(Long medicalRecordId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Prescription p WHERE p.id = :id")
    @EntityGraph(attributePaths = {"medicalRecord"})
    Optional<Prescription> findByIdForUpdate(@Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = {"medicalRecord"})
    Optional<Prescription> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"medicalRecord"})
    Page<Prescription> findAll(Pageable pageable);
}