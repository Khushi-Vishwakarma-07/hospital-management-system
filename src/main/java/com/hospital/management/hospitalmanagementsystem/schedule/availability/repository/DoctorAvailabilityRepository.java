package com.hospital.management.hospitalmanagementsystem.schedule.availability.repository;

import com.hospital.management.hospitalmanagementsystem.schedule.availability.entity.DoctorAvailability;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@NullMarked
public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctor_IdOrderByDayOfWeekAscStartTimeAsc(Long doctorId);

    List<DoctorAvailability> findByDoctor_IdAndDayOfWeekOrderByStartTimeAsc(Long doctorId, DayOfWeek dayOfWeek);

    void deleteByDoctor_Id(Long doctorId);

    @Query("""
    SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
    FROM DoctorAvailability a
    WHERE a.doctor.id = :doctorId
    AND a.dayOfWeek = :dayOfWeek
    AND a.startTime < :endTime
    AND a.endTime > :startTime
    AND (:excludeId IS NULL OR a.id <> :excludeId)
    """)
    boolean existsOverlappingSlot(
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime,
            @Param("excludeId") @Nullable Long excludeId
    );

    @Query("""
            SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
            FROM DoctorAvailability a
            WHERE a.doctor.id = :doctorId
              AND a.dayOfWeek = :dayOfWeek
              AND a.startTime <= :startTime
              AND a.endTime >= :endTime
            """)
    boolean existsAvailableSlot(
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM DoctorAvailability a WHERE a.id = :id")
    Optional<DoctorAvailability> findByIdForUpdate(@Param("id") Long id);
}