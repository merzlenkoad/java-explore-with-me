package ru.practicum.main.user.service;

import ru.practicum.main.user.dto.NewUserRequest;
import ru.practicum.main.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUserAdmin(NewUserRequest newUserRequest);

    List<UserDto> getUsersAdmin(List<Long> ids, Integer from, Integer size);

    void deleteUserAdmin(Long id);
}
