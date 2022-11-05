package pl.pawsko.toolroom.user;

import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.category.Category;
import pl.pawsko.toolroom.category.CategoryDto;

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

    public List<UserDto> getAllUsers() {
    return StreamSupport.stream(userRepository.findAll().spliterator(), false)
            .map(userDtoMapper::map)
            .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userDtoMapper::map);
    }

    public UserDto saveUser(UserDto userDto) {
        User user = userDtoMapper.map(userDto);
        User savedUser = userRepository.save(user);
        return userDtoMapper.map(savedUser);
    }

    public Optional<UserDto> replaceUser(Long id, UserDto userDto) {
        if (!userRepository.existsById(id)) {
            return Optional.empty();
        } else {
             Optional<User> userRepositoryById = userRepository.findById(id);
            userDto.setId(id);
            User userToUpdate = userDtoMapper.map(userDto);
            userToUpdate.setCreated(userRepositoryById.orElseThrow().getCreated());
            userToUpdate.setRating(userDto.getRating());
            User updatedEntity = userRepository.save(userToUpdate);
            return Optional.of(userDtoMapper.map(updatedEntity));
        }
    }
}
