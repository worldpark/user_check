package com.check.user_check.service.response.basic;

import com.check.user_check.aop.logtrace.annotation.NoLog;
import com.check.user_check.entity.User;
import com.check.user_check.exception.custom.DataIntegrityViolationWithCodeException;
import com.check.user_check.exception.custom.UsernameNotFoundWithCodeException;
import com.check.user_check.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    private UsernameNotFoundWithCodeException userNotFound(){
        throw new UsernameNotFoundWithCodeException("계정 정보를 찾지 못했습니다.", "010302");
    }

    public User findById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(this::userNotFound);
    }

    @NoLog
    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(this::userNotFound);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public UUID save(User user){
        try{
            return userRepository.save(user).getUserId();
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationWithCodeException(message, "010302");
        }
    }

    public void delete(UUID id){
        userRepository.deleteById(id);
    }

    public List<User> findByNotAttendanceTarget(){

        return userRepository.findByAttendanceTargets(null);
    }
}
