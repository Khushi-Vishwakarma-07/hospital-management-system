package com.hospital.management.hospitalmanagementsystem.schedule.leave.service;

import com.hospital.management.hospitalmanagementsystem.schedule.leave.dto.DoctorLeaveRequestDTO;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.dto.DoctorLeaveResponseDTO;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.dto.DoctorLeaveStatusUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.schedule.leave.dto.DoctorLeaveUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorLeaveService {

    DoctorLeaveResponseDTO createLeave(DoctorLeaveRequestDTO requestDTO);

    DoctorLeaveResponseDTO getLeaveById(Long leaveId);

    Page<DoctorLeaveResponseDTO> getAllLeaves(Pageable pageable);

    DoctorLeaveResponseDTO updateLeave(Long leaveId, DoctorLeaveUpdateDTO requestDTO);

    DoctorLeaveResponseDTO updateLeaveStatus(Long leaveId, DoctorLeaveStatusUpdateDTO requestDTO);

    void deleteLeave(Long leaveId);
}