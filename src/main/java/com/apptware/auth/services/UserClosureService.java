package com.apptware.auth.services;

import com.apptware.auth.models.User;
import java.util.List;

public interface UserClosureService {

    boolean isUserAHigherUp(Long potentialManagerId, Long potentialSubordinateId);

    List<User> getSubordinates(Long ancestorId);

    List<User> getAncestors(Long descendantId);

    List<User> getColleagues(Long userId);

    String determineRelationship(Long userId1, Long userId2);
}
