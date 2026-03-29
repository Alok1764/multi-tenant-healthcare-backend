import { useState, useEffect } from "react";
import { api } from "../../api/api.js";
import AvailabilityForm from "../../components/AvailabilityForm";

function DoctorDashboard({ token, toastShow }) {
  const [active, setActive] = useState("overview");
  const [appointments, setAppointments] = useState([]);
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    loadProfile();
  }, []);
  useEffect(() => {
    if (profile) loadAppointments();
  }, [profile]);

  const loadProfile = async () => {
    try {
      const doctors = await api.get("/doctors?pageNo=0&pageSize=10", token);
      if (doctors?.content?.length > 0) setProfile(doctors.content[0]);
      else if (Array.isArray(doctors) && doctors.length > 0)
        setProfile(doctors[0]);
    } catch (e) {}
  };

  const loadAppointments = async () => {
    if (!profile?.id) return;
    try {
      setAppointments(
        await api.get(`/appointments/doctor/${profile.id}`, token),
      );
    } catch (e) {}
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
          <div className="page-title">Doctor Dashboard 🩺</div>
          <div className="page-subtitle">Your schedule at a glance</div>
        </div>
        <div className="grid-3" style={{ marginBottom: "28px" }}>
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
            <div className="stat-value">
              {appointments.filter((a) => a.status === "COMPLETED").length}
            </div>
            <div className="stat-label">Completed</div>
          </div>
        </div>
        <div className="card">
          <div className="section-title">📅 Today's Schedule</div>
          {appointments.slice(0, 5).length === 0 ? (
            <div className="empty-state" style={{ padding: "24px" }}>
              <div className="empty-text">No appointments scheduled</div>
            </div>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Patient</th>
                  <th>Date</th>
                  <th>Time</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {appointments.slice(0, 5).map((a) => (
                  <tr key={a.id}>
                    <td style={{ fontWeight: 500 }}>{a.patientName || "—"}</td>
                    <td style={{ color: "var(--muted)" }}>
                      {a.appointmentDate || "—"}
                    </td>
                    <td style={{ color: "var(--muted)" }}>
                      {a.appointmentTime || "—"}
                    </td>
                    <td>{statusBadge(a.status)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    ),

    appointments: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">All Appointments</div>
          <div className="page-subtitle">Complete appointment history</div>
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
                    <th>Patient</th>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Status</th>
                    <th>Payment</th>
                  </tr>
                </thead>
                <tbody>
                  {appointments.map((a) => (
                    <tr key={a.id}>
                      <td style={{ fontWeight: 500 }}>
                        {a.patientName || "—"}
                      </td>
                      <td style={{ color: "var(--muted)" }}>
                        {a.appointmentDate || "—"}
                      </td>
                      <td style={{ color: "var(--muted)" }}>
                        {a.appointmentTime || "—"}
                      </td>
                      <td>{statusBadge(a.status)}</td>
                      <td>
                        <span
                          className={`badge ${a.paymentStatus === "PAID" ? "badge-green" : "badge-yellow"}`}
                        >
                          {a.paymentStatus || "PENDING"}
                        </span>
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

    availability: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">Manage Availability</div>
          <div className="page-subtitle">
            Set your working hours for each day
          </div>
        </div>
        <div className="card" style={{ maxWidth: "560px" }}>
          <AvailabilityForm token={token} toastShow={toastShow} />
        </div>
      </div>
    ),

    profile: (
      <div className="fade-in">
        <div className="page-header">
          <div className="page-title">My Profile</div>
          <div className="page-subtitle">Your doctor profile</div>
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
                    background: "rgba(245,158,11,0.15)",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    fontSize: "28px",
                  }}
                >
                  👨‍⚕️
                </div>
                <div>
                  <div style={{ fontWeight: 600, fontSize: "18px" }}>
                    Dr. {profile.name}
                  </div>
                  <div style={{ color: "var(--muted)", fontSize: "13px" }}>
                    {profile.specialization || "General Practice"}
                  </div>
                </div>
                <span
                  className="badge badge-yellow"
                  style={{ marginLeft: "auto" }}
                >
                  Doctor
                </span>
              </div>
              {[
                ["Email", profile.email],
                ["Hospital", profile.hospitalName],
                [
                  "Experience",
                  profile.experience ? `${profile.experience} years` : null,
                ],
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
export default DoctorDashboard;
