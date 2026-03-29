function Sidebar({ role, active, setActive, onLogout }) {
  const patientNav = [
    { id: "overview", icon: "◈", label: "Overview" },
    { id: "appointments", icon: "📅", label: "My Appointments" },
    { id: "doctors", icon: "👨‍⚕️", label: "Find Doctors" },
    { id: "records", icon: "📋", label: "Medical Records" },
    { id: "profile", icon: "👤", label: "My Profile" },
  ];
  const doctorNav = [
    { id: "overview", icon: "◈", label: "Overview" },
    { id: "appointments", icon: "📅", label: "Appointments" },
    { id: "availability", icon: "🕐", label: "Availability" },
    { id: "profile", icon: "👤", label: "My Profile" },
  ];

  const adminNav = [
    { id: "overview", icon: "◈", label: "Overview" },
    { id: "hospitals", icon: "🏥", label: "Hospitals" },
    { id: "doctors", icon: "👨‍⚕️", label: "Doctors" },
    { id: "specializations", icon: "🩺", label: "Specializations" },
    { id: "patients", icon: "👥", label: "Patients" },
  ];

  const nav =
    role === "ROLE_DOCTOR"
      ? doctorNav
      : role === "ROLE_HOSPITAL_ADMIN"
        ? adminNav
        : patientNav;

  const badgeClass =
    role === "ROLE_DOCTOR"
      ? "badge-yellow"
      : role === "ROLE_HOSPITAL_ADMIN"
        ? "badge-muted"
        : "badge-teal";

  const badgeLabel =
    role === "ROLE_DOCTOR"
      ? "Doctor"
      : role === "ROLE_HOSPITAL_ADMIN"
        ? "Admin"
        : "Patient";

  return (
    <div className="sidebar">
      <div className="sidebar-logo">
        <span style={{ color: "var(--teal)" }}>Med</span>
        <span>Core</span>
      </div>

      <div style={{ flex: 1 }}>
        {nav.map((item) => (
          <div
            key={item.id}
            className={`nav-item ${active === item.id ? "active" : ""}`}
            onClick={() => setActive(item.id)}
          >
            <span className="nav-icon">{item.icon}</span>
            {item.label}
          </div>
        ))}
      </div>

      <div style={{ borderTop: "1px solid var(--border)", paddingTop: "16px" }}>
        <div
          className={`badge ${badgeClass}`}
          style={{
            marginBottom: "12px",
            width: "100%",
            justifyContent: "center",
          }}
        >
          {badgeLabel}
        </div>
        <div
          className="nav-item"
          onClick={onLogout}
          style={{ color: "var(--danger)" }}
        >
          <span className="nav-icon">↩</span> Sign Out
        </div>
      </div>
    </div>
  );
}
export default Sidebar;
