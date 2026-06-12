package com.hospital.management.hospitalmanagementsystem.doctor.repository;

import com.hospital.management.hospitalmanagementsystem.doctor.entity.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctor_IdOrderByDayOfWeekAscStartTimeAsc(Long doctorId);

    List<DoctorAvailability> findByDoctor_IdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);

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
            @Param("excludeId") Long excludeId
    );
}