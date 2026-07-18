package com.hospital.management.hospitalmanagementsystem.medicalrecord.repository;

import com.hospital.management.hospitalmanagementsystem.medicalrecord.entity.MedicalRecord;
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
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT mr
        FROM MedicalRecord mr
        WHERE mr.id = :id
        """)
    @EntityGraph(attributePaths = {"patient", "doctor", "appointment"})
    Optional<MedicalRecord> findByIdForUpdate(@Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = {"patient", "doctor", "appointment"})
    Optional<MedicalRecord> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"patient", "doctor", "appointment"})
    Page<MedicalRecord> findAll(Pageable pageable);

    boolean existsByAppointment_Id(Long appointmentId);

    @EntityGraph(attributePaths = {"patient", "doctor", "appointment"})
    Page<MedicalRecord> findAllByPatient_Id(Long patientId, Pageable pageable);

    @EntityGraph(attributePaths = {"patient", "doctor", "appointment"})
    Page<MedicalRecord> findAllByDoctor_Id(Long doctorId, Pageable pageable);

    @EntityGraph(attributePaths = {"patient", "doctor", "appointment"})
    Optional<MedicalRecord> findByAppointment_Id(Long appointmentId);
}