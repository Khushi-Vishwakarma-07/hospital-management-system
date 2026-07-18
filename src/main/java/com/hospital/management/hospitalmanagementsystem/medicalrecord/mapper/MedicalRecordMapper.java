package com.hospital.management.hospitalmanagementsystem.medicalrecord.mapper;

import com.hospital.management.hospitalmanagementsystem.appointment.entity.Appointment;
import com.hospital.management.hospitalmanagementsystem.doctor.entity.Doctor;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordRequestDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordResponseDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.dto.MedicalRecordUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.medicalrecord.entity.MedicalRecord;
import com.hospital.management.hospitalmanagementsystem.patient.entity.Patient;

public final class MedicalRecordMapper {

    private MedicalRecordMapper() {
    }

    public static MedicalRecord toEntity(MedicalRecordRequestDTO dto,
                                         Patient patient,
                                         Doctor doctor,
                                         Appointment appointment) {
        if (dto == null) {
            return null;
        }

        return MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .chiefComplaint(dto.getChiefComplaint())
                .diagnosis(dto.getDiagnosis())
                .clinicalNotes(dto.getClinicalNotes())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .systolicBloodPressure(dto.getSystolicBloodPressure())
                .diastolicBloodPressure(dto.getDiastolicBloodPressure())
                .temperature(dto.getTemperature())
                .heartRate(dto.getHeartRate())
                .respiratoryRate(dto.getRespiratoryRate())
                .oxygenSaturation(dto.getOxygenSaturation())
                .followUpDate(dto.getFollowUpDate())
                .build();
    }

    public static void updateEntity(MedicalRecord entity, MedicalRecordUpdateDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setChiefComplaint(dto.getChiefComplaint());
        entity.setDiagnosis(dto.getDiagnosis());
        entity.setClinicalNotes(dto.getClinicalNotes());
        entity.setHeight(dto.getHeight());
        entity.setWeight(dto.getWeight());
        entity.setSystolicBloodPressure(dto.getSystolicBloodPressure());
        entity.setDiastolicBloodPressure(dto.getDiastolicBloodPressure());
        entity.setTemperature(dto.getTemperature());
        entity.setHeartRate(dto.getHeartRate());
        entity.setRespiratoryRate(dto.getRespiratoryRate());
        entity.setOxygenSaturation(dto.getOxygenSaturation());
        entity.setFollowUpDate(dto.getFollowUpDate());
    }

    public static MedicalRecordResponseDTO toResponseDTO(MedicalRecord medicalRecord) {
        if (medicalRecord == null) {
            return null;
        }

        Patient patient = medicalRecord.getPatient();
        Doctor doctor = medicalRecord.getDoctor();
        Appointment appointment = medicalRecord.getAppointment();

        return MedicalRecordResponseDTO.builder()
                .id(medicalRecord.getId())
                .patientId(patient != null ? patient.getId() : null)
                .patientName(patient != null ? patient.getFirstName() + " " + patient.getLastName() : null)
                .doctorId(doctor != null ? doctor.getId() : null)
                .doctorName(doctor != null ? doctor.getFirstName() + " " + doctor.getLastName() : null)
                .appointmentId(appointment != null ? appointment.getId() : null)
                .chiefComplaint(medicalRecord.getChiefComplaint())
                .diagnosis(medicalRecord.getDiagnosis())
                .clinicalNotes(medicalRecord.getClinicalNotes())
                .height(medicalRecord.getHeight())
                .weight(medicalRecord.getWeight())
                .systolicBloodPressure(medicalRecord.getSystolicBloodPressure())
                .diastolicBloodPressure(medicalRecord.getDiastolicBloodPressure())
                .temperature(medicalRecord.getTemperature())
                .heartRate(medicalRecord.getHeartRate())
                .respiratoryRate(medicalRecord.getRespiratoryRate())
                .oxygenSaturation(medicalRecord.getOxygenSaturation())
                .followUpDate(medicalRecord.getFollowUpDate())
                .createdAt(medicalRecord.getCreatedAt())
                .updatedAt(medicalRecord.getUpdatedAt())
                .build();
    }
}