package com.hospital.management.hospitalmanagementsystem.prescriptionItem.repository;

import com.hospital.management.hospitalmanagementsystem.prescriptionItem.entity.PrescriptionItem;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface PrescriptionItemRepository extends JpaRepository<PrescriptionItem, Long> {

    @EntityGraph(attributePaths = {"prescription"})
    List<PrescriptionItem> findByPrescription_Id(Long prescriptionId);

    @EntityGraph(attributePaths = {"prescription"})
    Optional<PrescriptionItem> findByIdAndPrescription_Id(Long id, Long prescriptionId);

    boolean existsByPrescription_IdAndMedicineName(Long prescriptionId, String medicineName);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pi FROM PrescriptionItem pi WHERE pi.id = :id")
    @EntityGraph(attributePaths = {"prescription"})
    Optional<PrescriptionItem> findByIdForUpdate(@Param("id") Long id);
}