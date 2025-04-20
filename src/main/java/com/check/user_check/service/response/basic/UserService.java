package com.check.user_check.service.response.basic;

import com.check.user_check.aop.logtrace.annotation.NoLog;
import com.check.user_check.entity.User;
import com.check.user_check.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    private UsernameNotFoundException userNotFound(){
        throw new UsernameNotFoundException("계정 정보를 찾지 못했습니다.|010302");
    }

    public User findById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(this::userNotFound);
    }

    @NoLog
    public User findByUserId(String userId){
        return userRepository.findByUserId(userId)
                .orElseThrow(this::userNotFound);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public UUID save(User user){
        try{
            return userRepository.save(user).getId();
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            String message = dataIntegrityViolationException.getMessage();

            throw new DataIntegrityViolationException(message + "|010302");
        }
    }

    public void delete(UUID id){
        userRepository.deleteById(id);
    }
}
