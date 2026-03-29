package com.healthcare.config;

import com.healthcare.exception.ResourceNotFoundException;
import com.healthcare.model.User;
import com.healthcare.model.Hospital;
import com.healthcare.model.Specialization;
import com.healthcare.model.Doctor;
import com.healthcare.model.Patient;
import com.healthcare.model.enums.BloodGroup;
import com.healthcare.model.enums.Gender;
import com.healthcare.model.enums.UserRole;
import com.healthcare.repository.UserRepository;
import com.healthcare.repository.HospitalRepository;
import com.healthcare.repository.SpecializationRepository;
import com.healthcare.repository.DoctorRepository;
import com.healthcare.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final SpecializationRepository specializationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
        seedHospitals();
        seedSpecializations();
        seedDoctors();
        seedPatients();
        log.info("Data initialization complete");
    }

    private void seedUsers() {
        if (userRepository.count() > 0) {
            log.info("Users already seeded, skipping...");
            return;
        }

        String password = passwordEncoder.encode("Password@123");

        userRepository.save(User.builder()
                .fullName("Super Admin")
                .email("admin@healthcare.com")
                .passwordHash(password)
                .role(UserRole.ROLE_HOSPITAL_ADMIN)
                .phoneNumber("9000000001")
                .isActive(true)
                .isEmailVerified(true)
                .build());

        userRepository.save(User.builder()
                .fullName("Dr. Arjun Mehta")
                .email("arjun@healthcare.com")
                .passwordHash(password)
                .role(UserRole.ROLE_DOCTOR)
                .phoneNumber("9000000002")
                .isActive(true)
                .isEmailVerified(true)
                .build());

        userRepository.save(User.builder()
                .fullName("Dr. Priya Sharma")
                .email("priya@healthcare.com")
                .passwordHash(password)
                .role(UserRole.ROLE_DOCTOR)
                .phoneNumber("9000000003")
                .isActive(true)
                .isEmailVerified(true)
                .build());

        userRepository.save(User.builder()
                .fullName("Rahul Patient")
                .email("rahul@healthcare.com")
                .passwordHash(password)
                .role(UserRole.ROLE_PATIENT)
                .phoneNumber("9000000004")
                .isActive(true)
                .isEmailVerified(true)
                .build());

        userRepository.save(User.builder()
                .fullName("Sneha Patient")
                .email("sneha@healthcare.com")
                .passwordHash(password)
                .role(UserRole.ROLE_PATIENT)
                .phoneNumber("9000000005")
                .isActive(true)
                .isEmailVerified(true)
                .build());

        log.info("Users seeded");
    }

    private void seedHospitals() {
        if (hospitalRepository.count() > 0) {
            log.info("Hospitals already seeded, skipping...");
            return;
        }

        hospitalRepository.save(Hospital.builder()
                .hospitalName("City General Hospital")
                .registrationNumber("REG-2024-001")
                .email("contact@citygeneral.com")
                .phoneNumber("9100000001")
                .isVerified(false)
                .address("12 MG Road")
                .city("Mumbai")
                .state("Maharashtra")
                .country("India")
                .postalCode("400001")
                .website("https://citygeneral.com")
                .build());

        hospitalRepository.save(Hospital.builder()
                .hospitalName("Apollo Wellness Center")
                .registrationNumber("REG-2024-002")
                .email("contact@apollowellness.com")
                .phoneNumber("9100000002")
                .isVerified(false)
                .address("45 Bandra West")
                .city("Mumbai")
                .state("Maharashtra")
                .country("India")
                .postalCode("400050")
                .website("https://apollowellness.com")
                .build());

        hospitalRepository.save(Hospital.builder()
                .hospitalName("Sunrise Medical Hub")
                .registrationNumber("REG-2024-003")
                .email("contact@sunrisemedical.com")
                .phoneNumber("9100000003")
                .isVerified(false)
                .address("8 Koregaon Park")
                .city("Pune")
                .state("Maharashtra")
                .country("India")
                .postalCode("411001")
                .website("https://sunrisemedical.com")
                .build());

        log.info("Hospitals seeded");
    }

    private void seedSpecializations() {
        if (specializationRepository.count() > 0) {
            log.info("Specializations already seeded, skipping...");
            return;
        }

        String[][] specs = {
                {"Cardiology",       "Heart and cardiovascular system"},
                {"Orthopedics",      "Bones, joints, and musculoskeletal system"},
                {"Neurology",        "Brain, spine, and nervous system"},
                {"Dermatology",      "Skin, hair, and nail conditions"},
                {"Pediatrics",       "Medical care for infants and children"},
                {"General Medicine", "Primary and preventive healthcare"},
        };

        for (String[] s : specs) {
            specializationRepository.save(Specialization.builder()
                    .name(s[0])
                    .description(s[1])
                    .isActive(true)
                    .build());
        }

        log.info("Specializations seeded");
    }

    private void seedDoctors() {
        if (doctorRepository.count() > 0) {
            log.info("Doctors already seeded, skipping...");
            return;
        }

        User arjun   = userRepository.findByEmail("arjun@healthcare.com").orElse(null);
        User priya   = userRepository.findByEmail("priya@healthcare.com").orElse(null);
        Hospital h1  = hospitalRepository.findAll().stream().filter(h -> h.getRegistrationNumber().equals("REG-2024-001")).findFirst().orElse(null);
        Hospital h2  = hospitalRepository.findAll().stream().filter(h -> h.getRegistrationNumber().equals("REG-2024-002")).findFirst().orElse(null);
        if (arjun == null || priya == null || h1 == null || h2 == null) {
            log.warn("⚠ Skipping doctor seed — dependencies not found");
            return;
        }

        doctorRepository.save(Doctor.builder()
                .user(arjun)
                .hospital(h1)
                .isVerified(false)
                .licenseNumber("LIC-2024-001")
                .specialization(specializationRepository.findByNameIgnoreCase("Cardiology")
                        .orElseThrow(() -> new ResourceNotFoundException("Specialization not found: Cardiology")))
                .qualification("MBBS, MD Cardiology")
                .experienceYears(8)
                .consultationFee(new BigDecimal("800.00"))
                .bio("Experienced cardiologist with 8 years at City General Hospital.")
                .build());

        doctorRepository.save(Doctor.builder()
                .user(priya)
                .hospital(h2)
                .isVerified(false)
                .licenseNumber("LIC-2024-002")
                .specialization(specializationRepository.findByNameIgnoreCase("Neurology")
                        .orElseThrow(() -> new ResourceNotFoundException("Specialization not found: Neurology")))
                .qualification("MBBS, DM Neurology")
                .experienceYears(5)
                .consultationFee(new BigDecimal("900.00"))
                .bio("Specialist in neurological disorders at Apollo Wellness Center.")
                .build());

        log.info("Doctors seeded");
    }

    private void seedPatients() {
        if (patientRepository.count() > 0) {
            log.info("Patients already seeded, skipping...");
            return;
        }

        User rahul = userRepository.findByEmail("rahul@healthcare.com").orElse(null);
        User sneha = userRepository.findByEmail("sneha@healthcare.com").orElse(null);

        if (rahul == null || sneha == null) {
            log.warn("⚠️ Skipping patient seed — dependencies not found");
            return;
        }

        patientRepository.save(Patient.builder()
                .user(rahul)
                .dateOfBirth(LocalDate.of(1995, 6, 15))
                .gender(Gender.MALE)
                .bloodGroup(BloodGroup.B_POSITIVE)
                .address("22 Andheri East, Mumbai")
                .emergencyContactName("Ravi Kumar")
                .emergencyContactPhone("9800000001")
                .build());

        patientRepository.save(Patient.builder()
                .user(sneha)
                .dateOfBirth(LocalDate.of(1998, 11, 22))
                .gender(Gender.FEMALE)
                .bloodGroup(BloodGroup.O_POSITIVE)
                .address("77 Powai, Mumbai")
                .emergencyContactName("Meera Sneha")
                .emergencyContactPhone("9800000002")
                .build());

        log.info("Patients seeded");
    }
}