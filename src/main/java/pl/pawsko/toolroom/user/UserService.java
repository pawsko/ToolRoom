package pl.pawsko.toolroom.user;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    public List<UserDtoResponse> getAllUsers() {
    return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(userDtoMapper::map)
            .collect(Collectors.toList());
    }

    public Optional<UserDtoResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::map);
    }

    public UserDtoResponse saveUser(UserDtoRequest userDtoRequest) {
        User user = userDtoMapper.map(userDtoRequest);
        User savedUser = userRepository.save(user);
        return userDtoMapper.map(savedUser);
    }

    public Optional<UserDtoResponse> replaceUser(Long id, UserDtoRequest userDtoRequest) {
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
