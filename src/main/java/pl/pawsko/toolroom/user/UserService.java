package pl.pawsko.toolroom.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.pawsko.toolroom.config.DbConfig;
import pl.pawsko.toolroom.user.nosql.UserRepositoryNosql;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryNosql userRepositoryNosql;
    private final DbConfig dbConfig;
    private final UserDtoMapper userDtoMapper;

    List<UserDtoResponse> getAllUsers() {
        if (dbConfig.getDataBase().equals("mysql")) {
            return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                    .map(userDtoMapper::map)
                    .collect(Collectors.toList());
        } else {
            return userRepositoryNosql.findAll().stream()
                    .map(userDtoMapper::mapFromNoSql)
                    .collect(Collectors.toList());
        }
    }

    Optional<UserDtoResponse> getUserById(Long id) {
        if (dbConfig.getDataBase().equals("mysql")) {
            return userRepository.findById(id)
                    .map(userDtoMapper::map);
        } else {
            return userRepositoryNosql.findById(id)
                    .map(userDtoMapper::mapFromNoSql);
        }
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
