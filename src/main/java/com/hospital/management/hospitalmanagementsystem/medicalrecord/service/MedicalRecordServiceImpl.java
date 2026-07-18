package com.hospital.management.hospitalmanagementsystem.medicalrecord.service;

import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import com.hospital.management.hospitalmanagementsystem.appointment.enums.AppointmentStatus;
import com.hospital.management.hospitalmanagementsystem.appointment.repository.AppointmentRepository;
import com.hospital.management.hospitalmanagementsystem.common.exception.BusinessException;
import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.common.util.ConstraintUtils;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.doctor.repository.DoctorRepository;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordRequestDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordResponseDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.entity.MedicalRecord;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.mapper.MedicalRecordMapper;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.repository.MedicalRecordRepository;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;
import com.hospital.management.hospitalmanagementsystem.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository repository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Transactional
    @Override
    public MedicalRecordResponseDTO createMedicalRecord(MedicalRecordRequestDTO dto) {

        Appointment appointment = getAppointmentForUpdateOrThrow(dto.getAppointmentId());

        if (repository.existsByAppointment_Id(dto.getAppointmentId())) {
            throw new DuplicateResourceException(
                    "A medical record already exists for appointment id: "
                            + dto.getAppointmentId());
        }

        validateAppointment(appointment, dto);

        Patient patient = getPatientOrThrow(dto.getPatientId());
        Doctor doctor = getDoctorOrThrow(dto.getDoctorId());

        MedicalRecord medicalRecord =
                MedicalRecordMapper.toEntity(dto, patient, doctor, appointment);

        return MedicalRecordMapper.toResponseDTO(save(medicalRecord));
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalRecordResponseDTO getMedicalRecordById(Long id) {
        return MedicalRecordMapper.toResponseDTO(getMedicalRecordOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordResponseDTO> getAllMedicalRecords(Pageable pageable) {
        return repository.findAll(pageable)
                .map(MedicalRecordMapper::toResponseDTO);
    }

    @Transactional
    @Override
    public MedicalRecordResponseDTO updateMedicalRecord(
            Long id,
            MedicalRecordUpdateDTO dto) {

        MedicalRecord medicalRecord = getMedicalRecordForUpdateOrThrow(id);

        MedicalRecordMapper.updateEntity(medicalRecord, dto);

        return MedicalRecordMapper.toResponseDTO(save(medicalRecord));
    }

    @Transactional
    @Override
    public void deleteMedicalRecord(Long id) {

        MedicalRecord medicalRecord = getMedicalRecordForUpdateOrThrow(id);

        repository.delete(medicalRecord);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordResponseDTO> getMedicalRecordsByPatient(Long patientId, Pageable pageable) {

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException(
                    "Patient not found with ID: " + patientId);
        }

        return repository.findAllByPatient_Id(patientId, pageable)
                .map(MedicalRecordMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordResponseDTO> getMedicalRecordsByDoctor(
            Long doctorId, Pageable pageable) {

        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException(
                    "Doctor not found with ID: " + doctorId);
        }

        return repository.findAllByDoctor_Id(doctorId, pageable)
                .map(MedicalRecordMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalRecordResponseDTO getMedicalRecordByAppointment(Long appointmentId) {

        if (!appointmentRepository.existsById(appointmentId)) {
            throw new ResourceNotFoundException(
                    "Appointment not found with ID: " + appointmentId);
        }

        MedicalRecord medicalRecord = repository
                .findByAppointment_Id(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Medical record not found for appointment ID: " + appointmentId));

        return MedicalRecordMapper.toResponseDTO(medicalRecord);
    }

    // ============================================================
    // Private methods
    // ============================================================

    private MedicalRecord getMedicalRecordOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medical record not found with id: " + id));
    }

    private MedicalRecord getMedicalRecordForUpdateOrThrow(Long id) {
        return repository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Medical record not found with id: " + id));
    }

    private Patient getPatientOrThrow(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Patient not found with id: " + id));
    }

    private Doctor getDoctorOrThrow(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id));
    }

    private Appointment getAppointmentForUpdateOrThrow(Long id) {
        return appointmentRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Appointment not found with id: " + id));
    }

    private void validateAppointment(
            Appointment appointment,
            MedicalRecordRequestDTO dto) {

        if (!appointment.getPatient().getId().equals(dto.getPatientId())) {
            throw new BusinessException(
                    "Appointment does not belong to the specified patient.");
        }

        if (!appointment.getDoctor().getId().equals(dto.getDoctorId())) {
            throw new BusinessException(
                    "Appointment does not belong to the specified doctor.");
        }

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new BusinessException(
                    "Medical records can only be created for completed appointments.");
        }
    }

    private MedicalRecord save(MedicalRecord medicalRecord) {

        try {
            return repository.saveAndFlush(medicalRecord);

        } catch (DataIntegrityViolationException ex) {

            String constraintName = ConstraintUtils.getConstraintName(ex);

            if ("uk_medical_record_appointment".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "A medical record already exists for this appointment.");
            }

            throw ex;
        }
    }
}