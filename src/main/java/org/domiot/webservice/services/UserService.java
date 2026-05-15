package org.domiot.webservice.services;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.UserEntityRepository;
import org.domiot.entities.UserEntity;
import org.domiot.mapper.UserMapper;
import org.domiot.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final UserMapper userMapper;

    public UserService(final UserEntityRepository userEntityRepository, final UserMapper userMapper) {
        this.userEntityRepository = userEntityRepository;
        this.userMapper = userMapper;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        UserEntity mapped = userMapper.map(user);
        if (mapped.getEmail() != null && userEntityRepository.existsByEmail(mapped.getEmail())) {
            throw new DuplicateUserException("A user with this email already exists");
        }
        if (mapped.getUserName() != null && userEntityRepository.existsByUserName(mapped.getUserName())) {
            throw new DuplicateUserException("A user with this username already exists");
        }

        try {
            UserEntity saved = userEntityRepository.saveAndFlush(mapped);
            return userMapper.map(saved);
        } catch (DataIntegrityViolationException ex) {
            // Covers races where another request inserts the same unique value between check and insert.
            throw new DuplicateUserException("A user with the same unique fields already exists", ex);
        }
    }

    public Optional<User> getUser(Long userId) {
        return userEntityRepository.findById(userId).map(userMapper::map);
    }

    public Optional<List<User>> updateUser(Long userId, User user) {
        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }
        Optional<UserEntity> existing = userEntityRepository.findById(userId);
        if (existing.isEmpty()) {
            log.info("No user found for update with userId={}", userId);
            return Optional.empty();
        }

        UserEntity mapped = userMapper.map(user);
        mapped.setId(userId);
        if (mapped.getSiteEntity() == null) {
            mapped.setSiteEntity(existing.get().getSiteEntity());
        }
        if (mapped.getPermissionEntities() == null || mapped.getPermissionEntities().isEmpty()) {
            mapped.setPermissionEntities(existing.get().getPermissionEntities());
        }

        UserEntity saved = userEntityRepository.save(mapped);
        return Optional.of(List.of(userMapper.map(saved)));
    }
}

