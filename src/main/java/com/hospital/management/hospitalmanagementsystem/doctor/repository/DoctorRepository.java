package com.hospital.management.hospitalmanagementsystem.doctor.repository;

import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NullMarked
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Override
    @EntityGraph(attributePaths = "specialization")
    Optional<Doctor> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "specialization")
    Page<Doctor> findAll(Pageable pageable);

    boolean existsBySpecializationId(Long id);
}