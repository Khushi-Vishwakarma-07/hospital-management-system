package com.hospital.management.hospitalmanagementsystem.medicalrecord.service;

import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordRequestDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordResponseDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicalRecordService {

    MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO dto);

    MedicalRecordResponseDTO getMedicalRecordById(Long id);

    Page<MedicalRecordResponseDTO> getAllMedicalRecords(Pageable pageable);

    MedicalRecordResponseDTO updateMedicalRecord(Long id, MedicalRecordUpdateDTO dto);

    void deleteMedicalRecord(Long id);

    Page<MedicalRecordResponseDTO> getMedicalRecordsByPatient(Long patientId, Pageable pageable);

    Page<MedicalRecordResponseDTO> getMedicalRecordsByDoctor(Long doctorId, Pageable pageable);

    MedicalRecordResponseDTO getMedicalRecordByAppointment(Long appointmentId);
}