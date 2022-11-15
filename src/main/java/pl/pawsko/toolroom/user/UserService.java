package pl.pawsko.toolroom.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    List<UserDtoResponse> getAllUsers() {
    return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(userDtoMapper::map)
            .collect(Collectors.toList());
    }

    Optional<UserDtoResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::map);
    }

    UserDtoResponse saveUser(UserDtoRequest userDtoRequest) {
        User user = userDtoMapper.map(userDtoRequest);
        User savedUser = userRepository.save(user);
        return userDtoMapper.map(savedUser);
    }

    Optional<UserDtoResponse> replaceUser(Long id, UserDtoRequest userDtoRequest) {
        if (!userRepository.existsById(id)) {
            return Optional.empty();
        } else {
             Optional<User> userRepositoryById = userRepository.findById(id);
            User userToUpdate = userDtoMapper.map(userDtoRequest);
            userToUpdate.setId(id);
            userToUpdate.setCreated(userRepositoryById.orElseThrow().getCreated());
            User updatedEntity = userRepository.save(userToUpdate);
            return Optional.of(userDtoMapper.map(updatedEntity));
        }
    }
}
