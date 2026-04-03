package org.domiot.webservice.services;

import org.domiot.webservice.repositories.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lankheet.domiot.entities.PermissionEntity;
import org.lankheet.domiot.entities.SiteEntity;
import org.lankheet.domiot.entities.UserEntity;
import org.lankheet.domiot.mapper.UserMapper;
import org.lankheet.domiot.model.User;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
        when(userEntityRepository.save(mapped)).thenReturn(saved);
        when(userMapper.map(saved)).thenReturn(expected);

        User result = userService.addUser(input);

        assertSame(expected, result);
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

