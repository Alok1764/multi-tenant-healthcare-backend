package com.healthcare.repository;

import com.healthcare.model.Appointment;
import com.healthcare.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Multi-tenant queries
    List<Appointment> findByHospitalId(Long hospitalId);

    Optional<Appointment> findByIdAndHospitalId(Long id, Long hospitalId);

    // Doctor queries
    List<Appointment> findByDoctorId(Long doctorId);

    List<Appointment> findByDoctorIdAndAppointmentDate(Long doctorId, LocalDate date);

    List<Appointment> findByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    // Patient queries
    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId " +
           "AND a.hospital.id = :hospitalId ORDER BY a.appointmentDate DESC, a.appointmentTime DESC")
    List<Appointment> findByPatientAndHospital(@Param("patientId") Long patientId,
                                               @Param("hospitalId") Long hospitalId);

    // Status queries
    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByHospitalIdAndStatus(Long hospitalId, AppointmentStatus status);

    // Date-based queries
    @Query("SELECT a FROM Appointment a WHERE a.hospital.id = :hospitalId " +
           "AND a.appointmentDate = :date ORDER BY a.appointmentTime ASC")
    List<Appointment> findByHospitalAndDate(@Param("hospitalId") Long hospitalId,
                                            @Param("date") LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDate BETWEEN :startDate AND :endDate " +
           "ORDER BY a.appointmentDate ASC, a.appointmentTime ASC")
    List<Appointment> findByDoctorAndDateRange(@Param("doctorId") Long doctorId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    // Upcoming appointments
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId " +
           "AND a.appointmentDate >= :today " +
           "AND a.status IN ('SCHEDULED', 'CONFIRMED') " +
           "ORDER BY a.appointmentDate ASC, a.appointmentTime ASC")
    List<Appointment> findUpcomingAppointmentsByPatient(@Param("patientId") Long patientId,
                                                         @Param("today") LocalDate today);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
           "AND a.appointmentDate = :date " +
           "AND a.status IN ('SCHEDULED', 'CONFIRMED') " +
           "ORDER BY a.appointmentTime ASC")
    List<Appointment> findTodayAppointmentsByDoctor(@Param("doctorId") Long doctorId,
                                                     @Param("date") LocalDate date);

    // Count queries
    long countByHospitalIdAndStatus(Long hospitalId, AppointmentStatus status);

    long countByDoctorIdAndStatus(Long doctorId, AppointmentStatus status);

    long countByPatientIdAndStatus(Long patientId, AppointmentStatus status);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.hospital.id = :hospitalId " +
           "AND a.appointmentDate BETWEEN :startDate AND :endDate")
    long countByHospitalAndDateRange(@Param("hospitalId") Long hospitalId,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);

    // Statistics
    @Query("SELECT a.status, COUNT(a) FROM Appointment a WHERE a.hospital.id = :hospitalId " +
           "GROUP BY a.status")
    List<Object[]> getAppointmentStatsByHospital(@Param("hospitalId") Long hospitalId);
}
