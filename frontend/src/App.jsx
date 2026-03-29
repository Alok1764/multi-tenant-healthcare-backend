import { useState, useEffect } from "react";
import { injectStyles } from "./styles/injectStyles";
import { useToast } from "./components/Toast";
import LandingPage from "./pages/LandingPage";
import AuthPage from "./pages/AuthPage";
import Dashboard from "./pages/Dashboard";

export default function App() {
  const { show, ToastEl } = useToast();
  useEffect(() => {
    injectStyles();
  }, []);
  const [authData, setAuthData] = useState(() => {
    const saved = sessionStorage.getItem("authData");
    return saved ? JSON.parse(saved) : null;
  });

  const [page, setPage] = useState(() => {
    const saved = sessionStorage.getItem("authData");
    return saved ? "dashboard" : "landing";
  });

  const handleAuth = (data) => {
    setAuthData(data);
    sessionStorage.setItem("authData", JSON.stringify(data));
    setPage("dashboard");
  };

  // const getRoleFromToken = (token) => {
  //   try {
  //     const payload = JSON.parse(atob(token.split(".")[1]));
  //     return (
  //       payload.role ||
  //       payload.authorities?.[0] ||
  //       payload.roles?.[0] ||
  //       "ROLE_PATIENT"
  //     );
  //   } catch (e) {
  //     return "ROLE_PATIENT";
  //   }
  // };

  const handleLogout = async () => {
    try {
      if (authData?.refreshToken) {
        await api.post("/auth/logout", { refreshToken: authData.refreshToken });
      }
    } catch (e) {}
    setAuthData(null);
    sessionStorage.removeItem("authData");
    setPage("landing");
  };

  return (
    <>
      {page === "landing" && <LandingPage onLogin={() => setPage("auth")} />}
      {page === "auth" && <AuthPage onAuth={handleAuth} toastShow={show} />}
      {page === "dashboard" && (
        <Dashboard
          authData={authData}
          onLogout={handleLogout}
          toastShow={show}
        />
      )}
      {ToastEl}
    </>
  );
}
