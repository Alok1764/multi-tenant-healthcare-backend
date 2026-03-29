import { useState, useEffect } from "react";
import { api } from "../../api/api.js";

function PatientDashboard({ token, toastShow }) {
  const [active, setActive] = useState("overview");
  const [profile, setProfile] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadProfile();
    loadDoctors();
  }, []);
  useEffect(() => {
    if (profile) {
      loadAppointments();
      loadRecords();
    }
  }, [profile]);

  const loadProfile = async () => {
    try {
      setProfile(await api.get("/patients/me", token));
    } catch (e) {}
  };
  const loadDoctors = async () => {
    try {
      setDoctors(await api.get("/doctors?pageNo=0&pageSize=20", token));
    } catch (e) {}
  };
  const loadAppointments = async () => {
    if (!profile?.id) return;
    try {
      setAppointments(
        await api.get(`/appointments/patient/${profile.id}`, token),
      );
    } catch (e) {}
  };
  const loadRecords = async () => {
    if (!profile?.id) return;
    try {
      setRecords(
        await api.get(`/medical-records/patient/${profile.id}`, token),
      );
    } catch (e) {}
  };

  const cancelAppointment = async (id) => {
    try {
      await api.post(`/appointments/${id}/cancel`, {}, token);
      toastShow("Appointment cancelled", "success");
      loadAppointments();
    } catch (e) {
      toastShow(e.message, "error");
    }
  };

  const statusBadge = (s) => {
    const map = {
      SCHEDULED: "badge-teal",
      CANCELLED: "badge-red",
      COMPLETED: "badge-green",
      PENDING: "badge-yellow",
    };
    return <span className={`badge ${map[s] || "badge-muted"}`}>{s}</span>;
  };

  const views = {
    overview: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">
            Good day, {profile?.name || "Patient"} 👋
          </div>
          <div className="page-subtitle">Here's your health summary</div>
        </div>
        <div className="grid-4" style={{ marginBottom: "28px" }}>
          <div className="stat-card">
            <div className="stat-value">{appointments.length}</div>
            <div className="stat-label">Total Appointments</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">
              {appointments.filter((a) => a.status === "SCHEDULED").length}
            </div>
            <div className="stat-label">Upcoming</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{records.length}</div>
            <div className="stat-label">Medical Records</div>
          </div>
          <div className="stat-card">
            <div className="stat-value">{doctors.length || "—"}</div>
            <div className="stat-label">Available Doctors</div>
          </div>
        </div>
        <div className="grid-2">
          <div className="card">
            <div className="section-title">📅 Recent Appointments</div>
            {appointments.slice(0, 4).length === 0 ? (
              <div className="empty-state" style={{ padding: "24px" }}>
                <div className="empty-text">No appointments yet</div>
              </div>
            ) : (
              appointments.slice(0, 4).map((a) => (
                <div
                  key={a.id}
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
                      Dr. {a.doctorName || "—"}
                    </div>
                    <div style={{ fontSize: "12px", color: "var(--muted)" }}>
                      {a.appointmentDate || "—"}
                    </div>
                  </div>
                  {statusBadge(a.status)}
                </div>
              ))
            )}
          </div>
          <div className="card">
            <div className="section-title">👨‍⚕️ Available Doctors</div>
            {doctors.slice(0, 4).map((d) => (
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
                    Dr. {d.name}
                  </div>
                  <div style={{ fontSize: "12px", color: "var(--muted)" }}>
                    {d.specialization || "General"}
                  </div>
                </div>
                <span className="badge badge-teal">Available</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    ),

    appointments: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">My Appointments</div>
          <div className="page-subtitle">
            All your scheduled and past appointments
          </div>
        </div>
        <div className="card">
          <div className="table-wrapper">
            {appointments.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">📅</div>
                <div className="empty-text">No appointments found</div>
              </div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Doctor</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Status</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {appointments.map((a) => (
                    <tr key={a.id}>
                      <td style={{ fontWeight: 500 }}>
                        Dr. {a.doctorName || "—"}
                      </td>
                      <td style={{ color: "var(--muted)" }}>
                        {a.appointmentDate || "—"}
                      </td>
                      <td style={{ color: "var(--muted)" }}>
                        {a.appointmentTime || "—"}
                      </td>
                      <td>{statusBadge(a.status)}</td>
                      <td>
                        {a.status === "SCHEDULED" && (
                          <button
                            className="btn btn-danger btn-sm"
                            onClick={() => cancelAppointment(a.id)}
                          >
                            Cancel
                          </button>
                        )}
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
          <div className="page-title">Find a Doctor</div>
          <div className="page-subtitle">Browse all available doctors</div>
        </div>
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fill, minmax(280px, 1fr))",
            gap: "16px",
          }}
        >
          {doctors.map((d) => (
            <div
              key={d.id}
              className="card"
              style={{ display: "flex", flexDirection: "column", gap: "12px" }}
            >
              <div
                style={{ display: "flex", alignItems: "center", gap: "12px" }}
              >
                <div
                  style={{
                    width: "44px",
                    height: "44px",
                    borderRadius: "50%",
                    background: "rgba(0,180,166,0.15)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    fontSize: "20px",
                    flexShrink: 0,
                  }}
                >
                  👨‍⚕️
                </div>
                <div>
                  <div style={{ fontWeight: 600, fontSize: "15px" }}>
                    Dr. {d.name}
                  </div>
                  <div style={{ fontSize: "12px", color: "var(--muted)" }}>
                    {d.specialization || "General Practice"}
                  </div>
                </div>
              </div>
              <div style={{ display: "flex", gap: "8px", flexWrap: "wrap" }}>
                <span className="badge badge-teal">Available</span>
                {d.hospitalName && (
                  <span className="badge badge-muted">{d.hospitalName}</span>
                )}
              </div>
              <div style={{ fontSize: "12px", color: "var(--muted)" }}>
                {d.experience
                  ? `${d.experience} yrs experience`
                  : "Book via appointment slots"}
              </div>
            </div>
          ))}
          {doctors.length === 0 && (
            <div className="empty-state">
              <div className="empty-icon">👨‍⚕️</div>
              <div className="empty-text">No doctors found</div>
            </div>
          )}
        </div>
      </div>
    ),

    records: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">Medical Records</div>
          <div className="page-subtitle">Your complete health history</div>
        </div>
        <div className="card">
          <div className="table-wrapper">
            {records.length === 0 ? (
              <div className="empty-state">
                <div className="empty-icon">📋</div>
                <div className="empty-text">No medical records found</div>
              </div>
            ) : (
              <table>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Doctor</th>
                    <th>Diagnosis</th>
                    <th>Notes</th>
                  </tr>
                </thead>
                <tbody>
                  {records.map((r) => (
                    <tr key={r.id}>
                      <td style={{ color: "var(--muted)" }}>
                        {r.recordDate || "—"}
                      </td>
                      <td style={{ fontWeight: 500 }}>
                        Dr. {r.doctorName || "—"}
                      </td>
                      <td>{r.diagnosis || "—"}</td>
                      <td
                        style={{
                          color: "var(--muted)",
                          maxWidth: "200px",
                          overflow: "hidden",
                          textOverflow: "ellipsis",
                          whiteSpace: "nowrap",
                        }}
                      >
                        {r.notes || "—"}
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

    profile: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">My Profile</div>
          <div className="page-subtitle">Your personal information</div>
        </div>
        <div className="card" style={{ maxWidth: "520px" }}>
          {profile ? (
            <div
              style={{ display: "flex", flexDirection: "column", gap: "16px" }}
            >
              <div
                style={{
                  display: "flex",
                  alignItems: "center",
                  gap: "16px",
                  paddingBottom: "20px",
                  borderBottom: "1px solid var(--border)",
                }}
              >
                <div
                  style={{
                    width: "60px",
                    height: "60px",
                    borderRadius: "50%",
                    background: "rgba(0,180,166,0.15)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    fontSize: "28px",
                  }}
                >
                  👤
                </div>
                <div>
                  <div style={{ fontWeight: 600, fontSize: "18px" }}>
                    {profile.name}
                  </div>
                  <div style={{ color: "var(--muted)", fontSize: "13px" }}>
                    {profile.email}
                  </div>
                </div>
                <span
                  className="badge badge-teal"
                  style={{ marginLeft: "auto" }}
                >
                  Patient
                </span>
              </div>
              {[
                ["Date of Birth", profile.dateOfBirth],
                ["Phone", profile.phoneNumber],
                ["Blood Group", profile.bloodGroup],
                ["Address", profile.address],
              ].map(
                ([k, v]) =>
                  v && (
                    <div
                      key={k}
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                        fontSize: "14px",
                      }}
                    >
                      <span style={{ color: "var(--muted)" }}>{k}</span>
                      <span style={{ fontWeight: 500 }}>{v}</span>
                    </div>
                  ),
              )}
            </div>
          ) : (
            <div className="empty-state">
              <div style={{ animation: "pulse 1.5s infinite" }}>Loading...</div>
            </div>
          )}
        </div>
      </div>
    ),
  };

  return { active, setActive, views };
}
export default PatientDashboard;
