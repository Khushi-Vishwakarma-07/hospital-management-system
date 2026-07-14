package com.hospital.management.hospitalmanagementsystem.doctor.repository;

import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NullMarked
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Override
    @EntityGraph(attributePaths = "specialization")
    Optional<Doctor> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "specialization")
    Page<Doctor> findAll(Pageable pageable);

    boolean existsBySpecialization_Id(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = "specialization")
    @Query("SELECT d FROM Doctor d WHERE d.id = :id")
    Optional<Doctor> findByIdForUpdate(@Param("id") Long id);
}