import { useState } from "react";
import Sidebar from "../components/Sidebar";
import PatientDashboard from "./patient/PatientDashboard";
import DoctorDashboard from "./doctor/DoctorDashboard";
import AdminDashboard from "./admin/AdminDashboard";

function Dashboard({ authData, onLogout, toastShow }) {
  const role = authData?.user?.role || authData?.role || "ROLE_PATIENT";

  const getDashboard = () => {
    if (role === "ROLE_HOSPITAL_ADMIN")
      return AdminDashboard({ token: authData.accessToken, toastShow });
    if (role === "ROLE_DOCTOR")
      return DoctorDashboard({ token: authData.accessToken, toastShow });
    return PatientDashboard({ token: authData.accessToken, toastShow });
  };

  const { active, setActive, views } = getDashboard();

  return (
    <div style={{ display: "flex" }}>
      <Sidebar
        role={role}
        active={active}
        setActive={setActive}
        onLogout={onLogout}
      />
      <main className="main-content">
        {views[active] || <div className="empty-state">Page not found</div>}
      </main>
    </div>
  );
}

export default Dashboard;
