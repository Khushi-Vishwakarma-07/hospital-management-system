package com.hospital.management.hospitalmanagementsystem.prescriptionItem.controller;

import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemRequestDTO;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.dto.PrescriptionItemResponseDTO;
import com.hospital.management.hospitalmanagementsystem.prescriptionItem.service.PrescriptionItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions/{prescriptionId}/items")
@RequiredArgsConstructor
@Validated
public class PrescriptionItemController {

    private final PrescriptionItemService prescriptionItemService;

    @PostMapping
    public ResponseEntity<PrescriptionItemResponseDTO> create(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long prescriptionId,
            @Valid @RequestBody PrescriptionItemRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(prescriptionItemService.addItem(prescriptionId, dto));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<PrescriptionItemResponseDTO> getById(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long prescriptionId,
            @PathVariable @Positive(message = "Item ID must be a positive number") Long itemId) {
        return ResponseEntity.ok(prescriptionItemService.getItemById(prescriptionId, itemId));
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionItemResponseDTO>> getAll(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long prescriptionId) {
        return ResponseEntity.ok(prescriptionItemService.getItemsByPrescriptionId(prescriptionId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<PrescriptionItemResponseDTO> update(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long prescriptionId,
            @PathVariable @Positive(message = "Item ID must be a positive number") Long itemId,
            @Valid @RequestBody PrescriptionItemRequestDTO dto) {
        return ResponseEntity.ok(prescriptionItemService.updateItem(prescriptionId, itemId, dto));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> delete(
            @PathVariable @Positive(message = "Prescription ID must be a positive number") Long prescriptionId,
            @PathVariable @Positive(message = "Item ID must be a positive number") Long itemId) {
        prescriptionItemService.deleteItem(prescriptionId, itemId);
        return ResponseEntity.noContent().build();
    }
}