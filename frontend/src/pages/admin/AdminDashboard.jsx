import { useState, useEffect } from "react";
import { api } from "../../api/api.js";

export default function AdminDashboard({ token, toastShow }) {
  const [active, setActive] = useState("overview");
  const [doctors, setDoctors] = useState([]);
  const [hospitals, setHospitals] = useState([]);
  const [specializations, setSpecializations] = useState([]);

  useEffect(() => {
    loadDoctors();
    loadHospitals();
    loadSpecializations();
  }, []);

  const loadDoctors = async () => {
    try {
      setDoctors(await api.get("/doctors?pageNo=0&pageSize=10", token));
    } catch (e) {}
  };
  const loadHospitals = async () => {
    try {
      setHospitals(await api.get("/hospitals", token));
    } catch (e) {}
  };
  const loadSpecializations = async () => {
    try {
      setSpecializations(await api.get("/specializations", token));
    } catch (e) {}
  };

  // ── Create Hospital ──────────────────────────────────────────────────────
  const [hospitalForm, setHospitalForm] = useState({
    hospitalName: "",
    registrationNumber: "",
    email: "",
    phoneNumber: "",
    address: "",
    city: "",
    state: "",
    country: "",
    postalCode: "",
    website: "",
  });
  const [hospitalLoading, setHospitalLoading] = useState(false);

  const createHospital = async () => {
    setHospitalLoading(true);
    try {
      await api.post("/hospitals", hospitalForm, token);
      toastShow("Hospital created successfully!", "success");
      setHospitalForm({
        hospitalName: "",
        registrationNumber: "",
        email: "",
        phoneNumber: "",
        address: "",
        city: "",
        state: "",
        country: "",
        postalCode: "",
        website: "",
      });
      loadHospitals();
    } catch (e) {
      toastShow(e.message, "error");
    } finally {
      setHospitalLoading(false);
    }
  };

  // ── Onboard Doctor ───────────────────────────────────────────────────────
  const [doctorForm, setDoctorForm] = useState({
    userId: "",
    hospitalId: "",
    licenseNumber: "",
    specialization: "",
    qualification: "",
    experienceYears: "",
    consultationFee: "",
    bio: "",
  });
  const [doctorLoading, setDoctorLoading] = useState(false);

  const onboardDoctor = async () => {
    setDoctorLoading(true);
    try {
      await api.post(
        "/doctors",
        {
          userId: Number(doctorForm.userId),
          hospitalId: Number(doctorForm.hospitalId),
          licenseNumber: doctorForm.licenseNumber,
          specialization: doctorForm.specialization,
          qualification: doctorForm.qualification,
          experienceYears: Number(doctorForm.experienceYears),
          consultationFee: Number(doctorForm.consultationFee),
          bio: doctorForm.bio,
        },
        token,
      );
      toastShow("Doctor onboarded successfully!", "success");
      setDoctorForm({
        userId: "",
        hospitalId: "",
        licenseNumber: "",
        specialization: "",
        qualification: "",
        experienceYears: "",
        consultationFee: "",
        bio: "",
      });
      loadDoctors();
    } catch (e) {
      toastShow(e.message, "error");
    } finally {
      setDoctorLoading(false);
    }
  };

  // ── Create Specialization ────────────────────────────────────────────────
  const [specForm, setSpecForm] = useState({ name: "", description: "" });
  const [specLoading, setSpecLoading] = useState(false);

  const createSpec = async () => {
    setSpecLoading(true);
    try {
      await api.post("/specializations", specForm, token);
      toastShow("Specialization created!", "success");
      setSpecForm({ name: "", description: "" });
      loadSpecializations();
    } catch (e) {
      toastShow(e.message, "error");
    } finally {
      setSpecLoading(false);
    }
  };

  const deactivateSpec = async (id) => {
    try {
      await api.delete(`/specializations/${id}`, token);
      toastShow("Specialization deactivated", "success");
      loadSpecializations();
    } catch (e) {
      toastShow(e.message, "error");
    }
  };

  // ── Add Patient ──────────────────────────────────────────────────────────
  const [patientForm, setPatientForm] = useState({
    userId: "",
    dateOfBirth: "",
    gender: "",
    bloodGroup: "",
    address: "",
    emergencyContactName: "",
    emergencyContactPhone: "",
    medicalHistory: "",
    allergies: "",
  });
  const [patientLoading, setPatientLoading] = useState(false);

  const addPatient = async () => {
    setPatientLoading(true);
    try {
      await api.post(
        "/patients",
        {
          userId: Number(patientForm.userId),
          dateOfBirth: patientForm.dateOfBirth,
          gender: patientForm.gender || null,
          bloodGroup: patientForm.bloodGroup || null,
          address: patientForm.address || null,
          emergencyContactName: patientForm.emergencyContactName || null,
          emergencyContactPhone: patientForm.emergencyContactPhone || null,
          medicalHistory: patientForm.medicalHistory || null,
          allergies: patientForm.allergies || null,
        },
        token,
      );
      toastShow("Patient added successfully!", "success");
      setPatientForm({
        userId: "",
        dateOfBirth: "",
        gender: "",
        bloodGroup: "",
        address: "",
        emergencyContactName: "",
        emergencyContactPhone: "",
        medicalHistory: "",
        allergies: "",
      });
    } catch (e) {
      toastShow(e.message, "error");
    } finally {
      setPatientLoading(false);
    }
  };

  const setH = (k, v) => setHospitalForm((f) => ({ ...f, [k]: v }));
  const setD = (k, v) => setDoctorForm((f) => ({ ...f, [k]: v }));
  const setS = (k, v) => setSpecForm((f) => ({ ...f, [k]: v }));

  const doctorList = Array.isArray(doctors?.content)
    ? doctors.content
    : Array.isArray(doctors)
      ? doctors
      : [];

  const views = {
    overview: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">Admin Dashboard</div>
          <div className="page-subtitle">System-wide overview</div>
        </div>
        <div className="grid-3" style={{ marginBottom: "28px" }}>
          <div className="stat-card">
            <div className="stat-value">{hospitals.length}</div>
            <div className="stat-label">Hospitals</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{doctorList.length}</div>
            <div className="stat-label">Doctors</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{specializations.length}</div>
            <div className="stat-label">Specializations</div>
          </div>
        </div>

        <div className="grid-2" style={{ gap: "20px" }}>
          {/* Hospitals list */}
          <div className="card">
            <div className="section-title">🏥 Registered Hospitals</div>
            {hospitals.length === 0 ? (
              <div className="empty-state" style={{ padding: "24px" }}>
                <div className="empty-text">No hospitals yet</div>
              </div>
            ) : (
              hospitals.map((h) => (
                <div
                  key={h.id}
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    padding: "10px 0",
                    borderBottom: "1px solid var(--border)",
                  }}
                >
                  <div>
                    <div style={{ fontSize: "14px", fontWeight: 500 }}>
                      {h.name}
                    </div>
                    <div style={{ fontSize: "12px", color: "var(--muted)" }}>
                      {h.address}
                    </div>
                  </div>
                  <span className="badge badge-teal">Active</span>
                </div>
              ))
            )}
          </div>

          {/* Doctors list */}
          <div className="card">
            <div className="section-title">👨‍⚕️ Onboarded Doctors</div>
            {doctorList.length === 0 ? (
              <div className="empty-state" style={{ padding: "24px" }}>
                <div className="empty-text">No doctors yet</div>
              </div>
            ) : (
              doctorList.slice(0, 6).map((d) => (
                <div
                  key={d.id}
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    padding: "10px 0",
                    borderBottom: "1px solid var(--border)",
                  }}
                >
                  <div>
                    <div style={{ fontSize: "14px", fontWeight: 500 }}>
                      Dr. {d.name || d.fullName}
                    </div>
                    <div style={{ fontSize: "12px", color: "var(--muted)" }}>
                      {d.specialization || "General"}
                    </div>
                  </div>
                  <span className="badge badge-yellow">Doctor</span>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    ),

    hospitals: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">Hospital Management</div>
          <div className="page-subtitle">Register and view hospitals</div>
        </div>
        <div className="grid-2" style={{ gap: "24px", alignItems: "start" }}>
          {/* Create form */}
          <div className="card">
            <div className="section-title">➕ Register New Hospital</div>
            <div
              style={{ display: "flex", flexDirection: "column", gap: "14px" }}
            >
              <div className="input-group">
                <label className="input-label">Hospital Name *</label>
                <input
                  className="input"
                  placeholder="City General Hospital"
                  value={hospitalForm.hospitalName}
                  onChange={(e) => setH("hospitalName", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Registration Number *</label>
                <input
                  className="input"
                  placeholder="REG-2024-001"
                  value={hospitalForm.registrationNumber}
                  onChange={(e) => setH("registrationNumber", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Email *</label>
                <input
                  className="input"
                  type="email"
                  placeholder="hospital@example.com"
                  value={hospitalForm.email}
                  onChange={(e) => setH("email", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Phone Number *</label>
                <input
                  className="input"
                  placeholder="9876543210"
                  value={hospitalForm.phoneNumber}
                  onChange={(e) => setH("phoneNumber", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Address *</label>
                <input
                  className="input"
                  placeholder="123 Main Street"
                  value={hospitalForm.address}
                  onChange={(e) => setH("address", e.target.value)}
                />
              </div>
              <div className="grid-2" style={{ gap: "12px" }}>
                <div className="input-group">
                  <label className="input-label">City *</label>
                  <input
                    className="input"
                    placeholder="Mumbai"
                    value={hospitalForm.city}
                    onChange={(e) => setH("city", e.target.value)}
                  />
                </div>
                <div className="input-group">
                  <label className="input-label">State *</label>
                  <input
                    className="input"
                    placeholder="Maharashtra"
                    value={hospitalForm.state}
                    onChange={(e) => setH("state", e.target.value)}
                  />
                </div>
              </div>
              <div className="grid-2" style={{ gap: "12px" }}>
                <div className="input-group">
                  <label className="input-label">Country *</label>
                  <input
                    className="input"
                    placeholder="India"
                    value={hospitalForm.country}
                    onChange={(e) => setH("country", e.target.value)}
                  />
                </div>
                <div className="input-group">
                  <label className="input-label">Postal Code</label>
                  <input
                    className="input"
                    placeholder="400001"
                    value={hospitalForm.postalCode}
                    onChange={(e) => setH("postalCode", e.target.value)}
                  />
                </div>
              </div>
              <div className="input-group">
                <label className="input-label">Website</label>
                <input
                  className="input"
                  placeholder="https://hospital.com"
                  value={hospitalForm.website}
                  onChange={(e) => setH("website", e.target.value)}
                />
              </div>
              <button
                className="btn btn-primary"
                onClick={createHospital}
                disabled={hospitalLoading}
              >
                {hospitalLoading ? "Creating..." : "Create Hospital"}
              </button>
            </div>
          </div>

          {/* List */}
          <div className="card">
            <div className="section-title">
              🏥 All Hospitals ({hospitals.length})
            </div>
            {hospitals.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">🏥</div>
                <div className="empty-text">No hospitals registered</div>
              </div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Address</th>
                    <th>Contact</th>
                  </tr>
                </thead>
                <tbody>
                  {hospitals.map((h) => (
                    <tr key={h.id}>
                      <td style={{ fontWeight: 500 }}>{h.name}</td>
                      <td style={{ color: "var(--muted)" }}>{h.address}</td>
                      <td style={{ color: "var(--muted)" }}>
                        {h.contactNumber}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    ),

    doctors: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">Doctor Management</div>
          <div className="page-subtitle">Onboard and view doctors</div>
        </div>
        <div className="grid-2" style={{ gap: "24px", alignItems: "start" }}>
          {/* Onboard form */}
          <div className="card">
            <div className="section-title">➕ Onboard New Doctor</div>
            <div
              style={{ display: "flex", flexDirection: "column", gap: "14px" }}
            >
              <div className="input-group">
                <label className="input-label">User ID *</label>
                <input
                  className="input"
                  type="number"
                  placeholder="Existing registered user ID e.g. 5"
                  value={doctorForm.userId}
                  onChange={(e) => setD("userId", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Hospital *</label>
                <select
                  className="input"
                  value={doctorForm.hospitalId}
                  onChange={(e) => setD("hospitalId", e.target.value)}
                >
                  <option value="">Select Hospital</option>
                  {hospitals.map((h) => (
                    <option key={h.id} value={h.id}>
                      {h.hospitalName || h.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="input-group">
                <label className="input-label">License Number *</label>
                <input
                  className="input"
                  placeholder="MED-2024-001"
                  value={doctorForm.licenseNumber}
                  onChange={(e) => setD("licenseNumber", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Specialization *</label>
                <input
                  className="input"
                  placeholder="Cardiology"
                  value={doctorForm.specialization}
                  onChange={(e) => setD("specialization", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Qualification *</label>
                <input
                  className="input"
                  placeholder="MBBS, MD"
                  value={doctorForm.qualification}
                  onChange={(e) => setD("qualification", e.target.value)}
                />
              </div>
              <div className="grid-2" style={{ gap: "12px" }}>
                <div className="input-group">
                  <label className="input-label">Experience (years) *</label>
                  <input
                    className="input"
                    type="number"
                    placeholder="5"
                    value={doctorForm.experienceYears}
                    onChange={(e) => setD("experienceYears", e.target.value)}
                  />
                </div>
                <div className="input-group">
                  <label className="input-label">Consultation Fee *</label>
                  <input
                    className="input"
                    type="number"
                    placeholder="500"
                    value={doctorForm.consultationFee}
                    onChange={(e) => setD("consultationFee", e.target.value)}
                  />
                </div>
              </div>
              <div className="input-group">
                <label className="input-label">Bio *</label>
                <input
                  className="input"
                  placeholder="Brief professional bio..."
                  value={doctorForm.bio}
                  onChange={(e) => setD("bio", e.target.value)}
                />
              </div>
              <button
                className="btn btn-primary"
                onClick={onboardDoctor}
                disabled={doctorLoading}
              >
                {doctorLoading ? "Onboarding..." : "Onboard Doctor"}
              </button>
            </div>
          </div>

          {/* Doctors list */}
          <div className="card">
            <div className="section-title">
              👨‍⚕️ All Doctors ({doctorList.length})
            </div>
            {doctorList.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">👨‍⚕️</div>
                <div className="empty-text">No doctors onboarded</div>
              </div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Specialization</th>
                    <th>Hospital</th>
                  </tr>
                </thead>
                <tbody>
                  {doctorList.map((d) => (
                    <tr key={d.id}>
                      <td style={{ fontWeight: 500 }}>
                        Dr. {d.name || d.fullName}
                      </td>
                      <td style={{ color: "var(--muted)" }}>
                        {d.specialization || "—"}
                      </td>
                      <td style={{ color: "var(--muted)" }}>
                        {d.hospitalName || "—"}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    ),

    specializations: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">Specialization Management</div>
          <div className="page-subtitle">Manage medical specializations</div>
        </div>
        <div className="grid-2" style={{ gap: "24px", alignItems: "start" }}>
          {/* Create form */}
          <div className="card">
            <div className="section-title">➕ Add Specialization</div>
            <div
              style={{ display: "flex", flexDirection: "column", gap: "14px" }}
            >
              <div className="input-group">
                <label className="input-label">Name</label>
                <input
                  className="input"
                  placeholder="Cardiology"
                  value={specForm.name}
                  onChange={(e) => setS("name", e.target.value)}
                />
              </div>
              <div className="input-group">
                <label className="input-label">Description</label>
                <input
                  className="input"
                  placeholder="Heart and cardiovascular system"
                  value={specForm.description}
                  onChange={(e) => setS("description", e.target.value)}
                />
              </div>
              <button
                className="btn btn-primary"
                onClick={createSpec}
                disabled={specLoading}
              >
                {specLoading ? "Creating..." : "Create Specialization"}
              </button>
            </div>
          </div>

          {/* List */}
          <div className="card">
            <div className="section-title">
              🩺 All Specializations ({specializations.length})
            </div>
            {specializations.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">🩺</div>
                <div className="empty-text">No specializations yet</div>
              </div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {specializations.map((s) => (
                    <tr key={s.id}>
                      <td style={{ fontWeight: 500 }}>{s.name}</td>
                      <td style={{ color: "var(--muted)" }}>
                        {s.description || "—"}
                      </td>
                      <td>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => deactivateSpec(s.id)}
                        >
                          Deactivate
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    ),

    patients: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">Patient Management</div>
          <div className="page-subtitle">
            Promote a registered user to a patient profile
          </div>
        </div>
        <div style={{ maxWidth: "560px" }}>
          <div className="card">
            <div className="section-title">➕ Add Patient</div>
            <p
              style={{
                fontSize: "13px",
                color: "var(--muted)",
                marginBottom: "16px",
                lineHeight: 1.6,
              }}
            >
              The user must already be registered. Enter their User ID and fill
              in their medical details.
            </p>
            <div
              style={{ display: "flex", flexDirection: "column", gap: "14px" }}
            >
              <div className="input-group">
                <label className="input-label">User ID *</label>
                <input
                  className="input"
                  type="number"
                  placeholder="e.g. 5"
                  value={patientForm.userId}
                  onChange={(e) =>
                    setPatientForm((f) => ({ ...f, userId: e.target.value }))
                  }
                />
              </div>
              <div className="input-group">
                <label className="input-label">Date of Birth *</label>
                <input
                  className="input"
                  type="date"
                  value={patientForm.dateOfBirth}
                  onChange={(e) =>
                    setPatientForm((f) => ({
                      ...f,
                      dateOfBirth: e.target.value,
                    }))
                  }
                />
              </div>
              <div className="grid-2" style={{ gap: "12px" }}>
                <div className="input-group">
                  <label className="input-label">Gender</label>
                  <select
                    className="input"
                    value={patientForm.gender}
                    onChange={(e) =>
                      setPatientForm((f) => ({ ...f, gender: e.target.value }))
                    }
                  >
                    <option value="">Select Gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                    <option value="OTHER">Other</option>
                  </select>
                </div>
                <div className="input-group">
                  <label className="input-label">Blood Group</label>
                  <select
                    className="input"
                    value={patientForm.bloodGroup}
                    onChange={(e) =>
                      setPatientForm((f) => ({
                        ...f,
                        bloodGroup: e.target.value,
                      }))
                    }
                  >
                    <option value="">Select Blood Group</option>
                    <option value="A_POSITIVE">A+</option>
                    <option value="A_NEGATIVE">A-</option>
                    <option value="B_POSITIVE">B+</option>
                    <option value="B_NEGATIVE">B-</option>
                    <option value="O_POSITIVE">O+</option>
                    <option value="O_NEGATIVE">O-</option>
                    <option value="AB_POSITIVE">AB+</option>
                    <option value="AB_NEGATIVE">AB-</option>
                  </select>
                </div>
              </div>
              <div className="input-group">
                <label className="input-label">Address</label>
                <input
                  className="input"
                  placeholder="123 Main Street, Mumbai"
                  value={patientForm.address}
                  onChange={(e) =>
                    setPatientForm((f) => ({ ...f, address: e.target.value }))
                  }
                />
              </div>
              <div className="grid-2" style={{ gap: "12px" }}>
                <div className="input-group">
                  <label className="input-label">Emergency Contact Name</label>
                  <input
                    className="input"
                    placeholder="Jane Doe"
                    value={patientForm.emergencyContactName}
                    onChange={(e) =>
                      setPatientForm((f) => ({
                        ...f,
                        emergencyContactName: e.target.value,
                      }))
                    }
                  />
                </div>
                <div className="input-group">
                  <label className="input-label">Emergency Contact Phone</label>
                  <input
                    className="input"
                    placeholder="9876543210"
                    value={patientForm.emergencyContactPhone}
                    onChange={(e) =>
                      setPatientForm((f) => ({
                        ...f,
                        emergencyContactPhone: e.target.value,
                      }))
                    }
                  />
                </div>
              </div>
              <div className="input-group">
                <label className="input-label">Medical History</label>
                <input
                  className="input"
                  placeholder="Diabetes, Hypertension..."
                  value={patientForm.medicalHistory}
                  onChange={(e) =>
                    setPatientForm((f) => ({
                      ...f,
                      medicalHistory: e.target.value,
                    }))
                  }
                />
              </div>
              <div className="input-group">
                <label className="input-label">Allergies</label>
                <input
                  className="input"
                  placeholder="Penicillin, Peanuts..."
                  value={patientForm.allergies}
                  onChange={(e) =>
                    setPatientForm((f) => ({ ...f, allergies: e.target.value }))
                  }
                />
              </div>
              <button
                className="btn btn-primary"
                onClick={addPatient}
                disabled={patientLoading}
              >
                {patientLoading ? "Adding..." : "Add Patient"}
              </button>
            </div>
          </div>
        </div>
      </div>
    ),
  };

  return { active, setActive, views };
}
