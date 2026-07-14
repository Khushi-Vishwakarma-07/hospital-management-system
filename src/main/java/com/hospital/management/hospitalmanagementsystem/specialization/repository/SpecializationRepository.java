package com.hospital.management.hospitalmanagementsystem.specialization.repository;

import com.hospital.management.hospitalmanagementsystem.specialization.entity.Specialization;
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
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    @Query("SELECT s FROM Specialization s")
    @EntityGraph(attributePaths = "department")
    Page<Specialization> findAllWithDepartment(Pageable pageable);

    @EntityGraph(attributePaths = "department")
    Page<Specialization> findByDepartmentId(Long departmentId, Pageable pageable);

    @EntityGraph(attributePaths = "department")
    @Query("SELECT s FROM Specialization s WHERE s.id = :id")
    Optional<Specialization> findWithDepartmentById(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = "department")
    @Query("SELECT s FROM Specialization s WHERE s.id = :id")
    Optional<Specialization> findByIdForUpdate(@Param("id") Long id);

    boolean existsByDepartmentId(Long departmentId);

    boolean existsByNameIgnoreCaseAndDepartmentId(
            String name,
            Long departmentId
    );

    boolean existsByNameIgnoreCaseAndDepartmentIdAndIdNot(
            String name,
            Long departmentId,
            Long id
    );
}