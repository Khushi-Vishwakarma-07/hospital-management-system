package com.hospital.management.hospitalmanagementsystem.patient.repository;

import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

}