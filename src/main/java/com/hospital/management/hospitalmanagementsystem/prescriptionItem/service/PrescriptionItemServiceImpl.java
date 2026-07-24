package com.hospital.management.hospitalmanagementsystem.prescriptionItem.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.common.util.ConstraintUtils;
import com.hospital.management.hospitalmanagementsystem.prescription.entity.Prescription;
import com.hospital.management.hospitalmanagementsystem.prescription.repository.PrescriptionRepository;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemResponseDTO;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.entity.PrescriptionItem;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.mapper.PrescriptionItemMapper;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.repository.PrescriptionItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionItemServiceImpl implements PrescriptionItemService {

    private final PrescriptionItemRepository repository;
    private final PrescriptionRepository prescriptionRepository;

    @Transactional
    @Override
    public PrescriptionItemResponseDTO addItem(Long prescriptionId, PrescriptionItemRequestDTO dto) {
        Prescription prescription = prescriptionRepository.findByIdForUpdate(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));

        validateUniqueMedicineName(prescriptionId, dto.getMedicineName());

        PrescriptionItem item = save(PrescriptionItemMapper.toEntity(dto, prescription));
        return PrescriptionItemMapper.toDTO(item);
    }

    @Transactional(readOnly = true)
    @Override
    public PrescriptionItemResponseDTO getItemById(Long prescriptionId, Long itemId) {
        return PrescriptionItemMapper.toDTO(getItemOrThrow(prescriptionId, itemId));
    }

    @Transactional(readOnly = true)
    @Override
    public List<PrescriptionItemResponseDTO> getItemsByPrescriptionId(Long prescriptionId) {
        if (!prescriptionRepository.existsById(prescriptionId)) {
            throw new ResourceNotFoundException("Prescription not found with id: " + prescriptionId);
        }
        return repository.findByPrescription_Id(prescriptionId).stream()
                .map(PrescriptionItemMapper::toDTO)
                .toList();
    }

    @Transactional
    @Override
    public PrescriptionItemResponseDTO updateItem(Long prescriptionId, Long itemId, PrescriptionItemRequestDTO dto) {
        PrescriptionItem item = getItemForUpdateOrThrow(prescriptionId, itemId);
        PrescriptionItemMapper.updateEntity(item, dto);
        return PrescriptionItemMapper.toDTO(save(item));
    }

    @Transactional
    @Override
    public void deleteItem(Long prescriptionId, Long itemId) {
        PrescriptionItem item = getItemForUpdateOrThrow(prescriptionId, itemId);
        repository.delete(item);
    }

    // ================= PRIVATE METHODS =================

    private PrescriptionItem getItemOrThrow(Long prescriptionId, Long itemId) {
        return repository.findByIdAndPrescription_Id(itemId, prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription item not found with id: " + itemId + " for prescription id: " + prescriptionId));
    }

    private PrescriptionItem getItemForUpdateOrThrow(Long prescriptionId, Long itemId) {
        prescriptionRepository.findByIdForUpdate(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found with id: " + prescriptionId));

        PrescriptionItem item = repository.findByIdForUpdate(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription item not found with id: " + itemId));

        if (!item.getPrescription().getId().equals(prescriptionId)) {
            throw new ResourceNotFoundException(
                    "Prescription item not found with id: " + itemId + " for prescription id: " + prescriptionId);
        }
        return item;
    }

    private PrescriptionItem save(PrescriptionItem item) {
        try {
            return repository.saveAndFlush(item);
        } catch (DataIntegrityViolationException ex) {
            String constraintName = ConstraintUtils.getConstraintName(ex);

            if ("uk_prescription_medicine".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "An item with medicine name '" + item.getMedicineName()
                                + "' already exists for prescription id: " + item.getPrescription().getId());
            }

            throw ex;
        }
    }

    private void validateUniqueMedicineName(Long prescriptionId, String medicineName) {
        if (repository.existsByPrescription_IdAndMedicineName(prescriptionId, medicineName)) {
            throw new DuplicateResourceException(
                    "An item with medicine name '" + medicineName
                            + "' already exists for prescription id: " + prescriptionId);
        }
    }
}