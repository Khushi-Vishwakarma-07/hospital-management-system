package com.hospital.management.hospitalmanagementsystem.appointment.repository;

import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
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
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NullMarked
@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    @EntityGraph(attributePaths = {"patient", "doctor"})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.id = :id
            """)
    Optional<Appointment> findByIdForUpdate(
            @Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = {"patient", "doctor"})
    Optional<Appointment> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {"patient", "doctor"})
    Page<Appointment> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"patient", "doctor"})
    Page<Appointment> findAllByPatient_Id(
            Long patientId,
            Pageable pageable);

    @EntityGraph(attributePaths = {"patient", "doctor"})
    Page<Appointment> findAllByDoctor_Id(
            Long doctorId,
            Pageable pageable);

    @EntityGraph(attributePaths = {"patient", "doctor"})
    Page<Appointment> findAllByAppointmentDateTimeBetween(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable);

    @Query("""
            SELECT a
            FROM Appointment a
            WHERE a.doctor.id = :doctorId
              AND a.status IN (
                    com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus.SCHEDULED,
                    com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus.CONFIRMED
              )
              AND (:excludeId IS NULL OR a.id <> :excludeId)
              AND a.appointmentDateTime < :endWindow
              AND a.appointmentDateTime >= :startWindow
            """)
    List<Appointment> findPotentialOverlappingAppointments(
            @Param("doctorId") Long doctorId,
            @Param("startWindow") LocalDateTime startWindow,
            @Param("endWindow") LocalDateTime endWindow,
            @Param("excludeId") @Nullable Long excludeId);

    boolean existsByDoctor_Id(Long doctorId);

    boolean existsByPatient_Id(Long patientId);
}