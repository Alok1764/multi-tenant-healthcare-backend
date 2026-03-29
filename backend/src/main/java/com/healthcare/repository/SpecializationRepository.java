package com.healthcare.repository;

import com.healthcare.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    Optional<Specialization> findByNameIgnoreCase(String name);

    List<Specialization> findByIsActive(Boolean isActive);

    boolean existsByNameIgnoreCase(String name);


    //This works when filter of isActive is applied
    @Query("SELECT s FROM Specialization s WHERE s.isActive = true ORDER BY s.name ASC")
    List<Specialization> findAllActiveSpecializations();

    @Query("SELECT s FROM Specialization s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "AND s.isActive = true")
    List<Specialization> searchActiveSpecializations(@Param("searchTerm") String searchTerm);


    //here clearAutomatically = true is closing the hibernate session

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Specialization s SET s.isActive = false WHERE s.id= :id")
    void deActivate(@Param("id") Long id);
}
