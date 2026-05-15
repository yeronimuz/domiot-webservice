package org.domiot.webservice.services;

import org.domiot.webservice.repositories.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.domiot.entities.PermissionEntity;
import org.domiot.entities.SiteEntity;
import org.domiot.entities.UserEntity;
import org.domiot.mapper.UserMapper;
import org.domiot.model.User;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void addUserShouldThrowWhenUserIsNull() {
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(null));
    }

    @Test
    void addUserShouldMapAndSave() {
        User input = new User();
        UserEntity mapped = new UserEntity();
        UserEntity saved = new UserEntity();
        User expected = new User();

        when(userMapper.map(input)).thenReturn(mapped);
        when(userEntityRepository.saveAndFlush(mapped)).thenReturn(saved);
        when(userMapper.map(saved)).thenReturn(expected);

        User result = userService.addUser(input);

        assertSame(expected, result);
    }

    @Test
    void addUserShouldThrowDuplicateWhenEmailAlreadyExists() {
        User input = new User();
        UserEntity mapped = new UserEntity();
        mapped.setEmail("dup@example.org");

        when(userMapper.map(input)).thenReturn(mapped);
        when(userEntityRepository.existsByEmail("dup@example.org")).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.addUser(input));
        verify(userEntityRepository, never()).saveAndFlush(any(UserEntity.class));
    }

    @Test
    void addUserShouldThrowDuplicateWhenUserNameAlreadyExists() {
        User input = new User();
        UserEntity mapped = new UserEntity();
        mapped.setEmail("unique@example.org");
        mapped.setUserName("already-used");

        when(userMapper.map(input)).thenReturn(mapped);
        when(userEntityRepository.existsByEmail("unique@example.org")).thenReturn(false);
        when(userEntityRepository.existsByUserName("already-used")).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.addUser(input));
        verify(userEntityRepository, never()).saveAndFlush(any(UserEntity.class));
    }

    @Test
    void addUserShouldTranslateDataIntegrityViolationToDuplicateUserException() {
        User input = new User();
        UserEntity mapped = new UserEntity();
        mapped.setEmail("race@example.org");
        mapped.setUserName("race-user");

        when(userMapper.map(input)).thenReturn(mapped);
        when(userEntityRepository.existsByEmail("race@example.org")).thenReturn(false);
        when(userEntityRepository.existsByUserName("race-user")).thenReturn(false);
        when(userEntityRepository.saveAndFlush(mapped))
                .thenThrow(new DataIntegrityViolationException("unique key violation"));

        assertThrows(DuplicateUserException.class, () -> userService.addUser(input));
    }

    @Test
    void getUserShouldReturnMappedWhenFound() {
        UserEntity entity = new UserEntity();
        User expected = new User();

        when(userEntityRepository.findById(4L)).thenReturn(Optional.of(entity));
        when(userMapper.map(entity)).thenReturn(expected);

        Optional<User> result = userService.getUser(4L);

        assertTrue(result.isPresent());
        assertSame(expected, result.get());
    }

    @Test
    void updateUserShouldReturnEmptyWhenUnknownUser() {
        when(userEntityRepository.findById(4L)).thenReturn(Optional.empty());

        Optional<List<User>> result = userService.updateUser(4L, new User());

        assertFalse(result.isPresent());
    }

    @Test
    void updateUserShouldKeepExistingSiteAndPermissionsWhenMissingInPayload() {
        Long userId = 4L;
        User request = new User();

        SiteEntity existingSite = new SiteEntity();
        PermissionEntity permission = new PermissionEntity();
        List<PermissionEntity> existingPermissions = List.of(permission);

        UserEntity existing = new UserEntity();
        existing.setId(userId);
        existing.setSiteEntity(existingSite);
        existing.setPermissionEntities(existingPermissions);

        UserEntity mapped = new UserEntity();
        mapped.setPermissionEntities(Collections.emptyList());

        UserEntity saved = new UserEntity();
        User expected = new User();

        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(existing));
        when(userMapper.map(request)).thenReturn(mapped);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(saved);
        when(userMapper.map(saved)).thenReturn(expected);

        Optional<List<User>> result = userService.updateUser(userId, request);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userEntityRepository).save(captor.capture());
        assertEquals(userId, captor.getValue().getId());
        assertSame(existingSite, captor.getValue().getSiteEntity());
        assertSame(existingPermissions, captor.getValue().getPermissionEntities());
        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
        assertSame(expected, result.get().getFirst());
    }
}

