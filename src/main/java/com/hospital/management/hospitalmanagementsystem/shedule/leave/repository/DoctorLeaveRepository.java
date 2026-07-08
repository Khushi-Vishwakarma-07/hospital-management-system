package com.hospital.management.hospitalmanagementsystem.shedule.leave.repository;

import com.hospital.management.hospitalmanagementsystem.shedule.leave.entity.DoctorLeave;
import com.hospital.management.hospitalmanagementsystem.shedule.leave.enums.LeaveStatus;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@NullMarked
public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, Long> {

    @Override
    @EntityGraph(attributePaths = {"doctor", "reviewer"})
    Optional<DoctorLeave> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"doctor", "reviewer"})
    Page<DoctorLeave> findAll(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"doctor", "reviewer"})
    Optional<DoctorLeave> findByIdForUpdate(Long id);

    @Query("""
        SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END
        FROM DoctorLeave l
        WHERE l.doctor.id = :doctorId
          AND l.status IN :statuses
          AND l.startDate <= :endDate
          AND l.endDate >= :startDate
          AND (:excludeLeaveId IS NULL OR l.id <> :excludeLeaveId)
        """)
    boolean existsOverlappingLeave(
            @Param("doctorId") Long doctorId,
            @Param("statuses") List<LeaveStatus> statuses,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludeLeaveId") @Nullable Long excludeLeaveId
    );

    boolean existsByDoctor_Id(Long doctorId);
}