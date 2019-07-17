package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.DuplicatedUserException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void save(UserDto.Create userDto) {
        User newUser = userDto.toUser();
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedUserException();
        }
        userRepository.save(newUser);
    }

    public List<UserDto.Response> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.Response.class))
                .collect(Collectors.toList());
    }
}
