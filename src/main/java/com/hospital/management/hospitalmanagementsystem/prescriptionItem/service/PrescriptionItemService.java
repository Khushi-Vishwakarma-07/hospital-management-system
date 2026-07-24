package com.hospital.management.hospitalmanagementsystem.prescriptionItem.service;

import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemResponseDTO;

import java.util.List;

public interface PrescriptionItemService {

    PrescriptionItemResponseDTO addItem(Long prescriptionId, PrescriptionItemRequestDTO dto);

    PrescriptionItemResponseDTO getItemById(Long prescriptionId, Long itemId);

    List<PrescriptionItemResponseDTO> getItemsByPrescriptionId(Long prescriptionId);

    PrescriptionItemResponseDTO updateItem(Long prescriptionId, Long itemId, PrescriptionItemRequestDTO dto);

    void deleteItem(Long prescriptionId, Long itemId);
}