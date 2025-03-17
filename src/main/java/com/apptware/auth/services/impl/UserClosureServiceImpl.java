package com.apptware.auth.services.impl;

import com.apptware.auth.models.User;
import com.apptware.auth.repositories.UserClosureRepository;
import com.apptware.auth.repositories.UserRepository;
import com.apptware.auth.services.UserClosureService;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserClosureServiceImpl implements UserClosureService {

    private final UserClosureRepository userClosureRepository;
    private final UserRepository userRepository;

    public UserClosureServiceImpl(UserClosureRepository userClosureRepository, UserRepository userRepository) {
        this.userClosureRepository = userClosureRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean isUserAHigherUp(Long potentialManagerId, Long potentialSubordinateId) {
        return userClosureRepository.isAncestor(potentialManagerId, potentialSubordinateId);
    }

    @Override
    public List<User> getSubordinates(Long ancestorId) {
        return userClosureRepository.findSubordinates(ancestorId);
    }

    @Override
    public List<User> getAncestors(Long descendantId) {
        return userClosureRepository.findAncestors(descendantId);
    }

    @Override
    public List<User> getColleagues(Long userId) {
        return userRepository.findColleagues(userId);
    }

    @Override
    public String determineRelationship(Long userId1, Long userId2) {
        // Check if userId1 is an ancestor (higher-up) of userId2
        if(userClosureRepository.isAncestor(userId1, userId2)) {
            return "User " + userId1 + " is higher up than User " + userId2;
        }
        // Check if userId2 is an ancestor (higher-up) of userId1 (i.e. userId1 is subordinate)
        else if(userClosureRepository.isAncestor(userId2, userId1)) {
            return "User " + userId1 + " is subordinate to User " + userId2;
        } else {
            // Check if they share the same reporting manager (colleagues)
            Optional<User> optionalUser1 = userRepository.findById(userId1);
            Optional<User> optionalUser2 = userRepository.findById(userId2);
            if(optionalUser1.isPresent() && optionalUser2.isPresent()) {
                User user1 = optionalUser1.get();
                User user2 = optionalUser2.get();
                if(user1.getReportingManagerId() != null &&
                   user1.getReportingManagerId().equals(user2.getReportingManagerId())) {
                    return "User " + userId1 + " and User " + userId2 + " are colleagues";
                }
            }
        }
        return "No direct hierarchical relationship found between User " + userId1 + " and User " + userId2;
    }
}
