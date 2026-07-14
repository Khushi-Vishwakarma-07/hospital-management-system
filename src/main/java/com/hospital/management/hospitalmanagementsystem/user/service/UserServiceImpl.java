package com.hospital.management.hospitalmanagementsystem.user.service;

import com.hospital.management.hospitalmanagementsystem.common.exception.DuplicateResourceException;
import com.hospital.management.hospitalmanagementsystem.common.exception.ResourceNotFoundException;
import com.hospital.management.hospitalmanagementsystem.common.util.ConstraintUtils;
import com.hospital.management.hospitalmanagementsystem.role.entity.Role;
import com.hospital.management.hospitalmanagementsystem.role.repo.RoleRepository;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserRequestDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserResponseDTO;
import com.hospital.management.hospitalmanagementsystem.user.dto.UserUpdateDTO;
import com.hospital.management.hospitalmanagementsystem.user.entity.User;
import com.hospital.management.hospitalmanagementsystem.user.mapper.UserMapper;
import com.hospital.management.hospitalmanagementsystem.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {

        validateEmailAvailable(request.getEmail(), null);

        Role role = getRoleForUpdateOrThrow(request.getRoleId());

        User user = userMapper.toEntity(request, role);

        return userMapper.toResponse(save(user));
    }

    @Override
    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO request) {

        User user = getUserForUpdateOrThrow(id);

        validateEmailAvailable(request.getEmail(), id);

        Role role = getRoleForUpdateOrThrow(request.getRoleId());

        userMapper.updateEntity(user, request, role);

        return userMapper.toResponse(save(user));
    }

    @Override
    public UserResponseDTO getById(Long id) {
        return userMapper.toResponse(getUserOrThrow(id));
    }

    @Override
    public Page<UserResponseDTO> getAll(Pageable pageable) {
        return userRepository.findAllWithRole(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    @Transactional
    public UserResponseDTO activate(Long id) {
        return updateActiveStatus(id, true);
    }

    @Override
    @Transactional
    public UserResponseDTO deactivate(Long id) {
        return updateActiveStatus(id, false);
    }

    // -------------------- Helpers --------------------

    private User getUserOrThrow(Long id) {
        return userRepository.findWithRoleById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id));
    }

    private User getUserForUpdateOrThrow(Long id) {
        return userRepository.findByIdForUpdate(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with ID: " + id));
    }

    private Role getRoleForUpdateOrThrow(Long roleId) {
        return roleRepository.findByIdForUpdate(roleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with ID: " + roleId));
    }

    private void validateEmailAvailable(String email, Long userId) {

        String normalized = email.trim().toLowerCase();

        boolean exists = (userId == null)
                ? userRepository.existsByEmail(normalized)
                : userRepository.existsByEmailAndIdNot(normalized, userId);

        if (exists) {
            throw new DuplicateResourceException(
                    "User already exists with email: " + normalized);
        }
    }

    private UserResponseDTO updateActiveStatus(Long id, boolean active) {

        User user = getUserForUpdateOrThrow(id);

        if (!user.getActive().equals(active)) {
            user.setActive(active);
        }

        return userMapper.toResponse(user);
    }

    private User save(User user) {
        try {
            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {

            String constraintName = ConstraintUtils.getConstraintName(ex);

            if ("uk_user_email".equals(constraintName)) {
                throw new DuplicateResourceException(
                        "User already exists with email: " + user.getEmail()
                );
            }

            throw ex;
        }
    }
}