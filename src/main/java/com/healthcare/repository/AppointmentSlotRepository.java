package com.healthcare.repository;

import com.healthcare.model.AppointmentSlot;
import com.healthcare.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

    // Multi-tenant queries
    List<AppointmentSlot> findByHospitalId(Long hospitalId);

    Optional<AppointmentSlot> findByIdAndHospitalId(Long id, Long hospitalId);

    // Doctor-specific queries
    List<AppointmentSlot> findByDoctorId(Long doctorId);

    List<AppointmentSlot> findByDoctorIdAndSlotDate(Long doctorId, LocalDate slotDate);


    // Availability queries
    @Query("SELECT a FROM AppointmentSlot a WHERE a.doctor.id = :doctorId " +
           "AND a.slotDate = :date AND a.isAvailable = true " +
           "ORDER BY a.startTime ASC")
    List<AppointmentSlot> findAvailableSlotsByDoctorAndDate(@Param("doctorId") Long doctorId,
                                                             @Param("date") LocalDate date);

    @Query("SELECT a FROM AppointmentSlot a WHERE a.hospital.id = :hospitalId " +
           "AND a.slotDate BETWEEN :startDate AND :endDate " +
           "AND a.isAvailable = true " +
           "ORDER BY a.slotDate ASC, a.startTime ASC")
    List<AppointmentSlot> findAvailableSlotsByHospitalAndDateRange(
            @Param("hospitalId") Long hospitalId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Specific slot lookup
    @Query("SELECT a FROM AppointmentSlot a WHERE a.doctor.id = :doctorId " +
           "AND a.slotDate = :date AND a.startTime = :time")
    Optional<AppointmentSlot> findByDoctorAndDateTime(@Param("doctorId") Long doctorId,
                                                       @Param("date") LocalDate date,
                                                       @Param("time") LocalTime time);


    // Fully booked slots
    @Query("SELECT a FROM AppointmentSlot a WHERE a.hospital.id = :hospitalId " +
           "AND a.slotDate = :date AND a.bookedAppointments >= a.maxAppointments")
    List<AppointmentSlot> findFullyBookedSlots(@Param("hospitalId") Long hospitalId,
                                               @Param("date") LocalDate date);

    // Count queries
    @Query("SELECT COUNT(a) FROM AppointmentSlot a WHERE a.doctor.id = :doctorId " +
           "AND a.slotDate = :date AND a.isAvailable = true")
    long countAvailableSlotsByDoctorAndDate(@Param("doctorId") Long doctorId,
                                            @Param("date") LocalDate date);

    // Delete old slots
    @Query("DELETE FROM AppointmentSlot a WHERE a.slotDate < :date")
    void deleteOldSlots(@Param("date") LocalDate date);
}
