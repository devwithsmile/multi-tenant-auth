package com.apptware.auth.repositories;

import com.apptware.auth.models.User;
import com.apptware.auth.models.UserClosure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserClosureRepository extends JpaRepository<UserClosure, Long> {

    @Query("SELECT CASE WHEN COUNT(uc) > 0 THEN true ELSE false END " +
           "FROM UserClosure uc " +
           "WHERE uc.ancestor.id = :ancestorId " +
           "AND uc.descendant.id = :descendantId")
    boolean isAncestor(@Param("ancestorId") Long ancestorId,
                       @Param("descendantId") Long descendantId);

    @Query("SELECT uc.descendant FROM UserClosure uc " +
           "WHERE uc.ancestor.id = :ancestorId " +
           "AND uc.depth > 0")
    List<User> findSubordinates(@Param("ancestorId") Long ancestorId);

    @Query("SELECT uc.ancestor FROM UserClosure uc " +
           "WHERE uc.descendant.id = :descendantId " +
           "AND uc.depth > 0")
    List<User> findAncestors(@Param("descendantId") Long descendantId);
}
