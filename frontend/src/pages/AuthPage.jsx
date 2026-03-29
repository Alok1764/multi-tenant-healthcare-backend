import { useState } from "react";
import { api } from "../api/api.js";

function AuthPage({ onAuth, toastShow }) {
  const [mode, setMode] = useState("login");
  const [loading, setLoading] = useState(false);
  const [form, setForm] = useState({
    fullName: "",
    email: "",
    password: "",
    role: "ROLE_PATIENT",
  });

  const set = (k, v) => setForm((f) => ({ ...f, [k]: v }));

  const submit = async () => {
    setLoading(true);
    try {
      if (mode === "login") {
        const data = await api.post("/auth/login", {
          email: form.email,
          password: form.password,
        });
        onAuth(data);
        toastShow("Welcome back!", "success");
      } else {
        const data = await api.post("/auth/register", form);
        onAuth(data);
        toastShow("Account created successfully!", "success");
      }
    } catch (e) {
      toastShow(e.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        minHeight: "100vh",
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        padding: "24px",
        background: "var(--navy)",
      }}
    >
      {/* Background decoration */}
      <div
        style={{
          position: "fixed",
          top: "10%",
          right: "10%",
          width: "400px",
          height: "400px",
          borderRadius: "50%",
          background:
            "radial-gradient(circle, rgba(0,180,166,0.08) 0%, transparent 70%)",
          pointerEvents: "none",
        }}
      />

      <div style={{ width: "100%", maxWidth: "440px" }}>
        <div
          className="fade-up"
          style={{ textAlign: "center", marginBottom: "40px" }}
        >
          <div
            style={{
              fontFamily: "'DM Serif Display', serif",
              fontSize: "28px",
              marginBottom: "8px",
            }}
          >
            <span style={{ color: "var(--teal)" }}>Med</span>Core
          </div>
          <p style={{ color: "var(--muted)", fontSize: "14px" }}>
            Healthcare Management Platform
          </p>
        </div>

        <div className="card fade-up" style={{ animationDelay: "0.1s" }}>
          {/* Tabs */}
          <div
            style={{
              display: "flex",
              background: "rgba(255,255,255,0.04)",
              borderRadius: "8px",
              padding: "4px",
              marginBottom: "28px",
            }}
          >
            {["login", "register"].map((m) => (
              <button
                key={m}
                onClick={() => setMode(m)}
                style={{
                  flex: 1,
                  padding: "8px",
                  borderRadius: "6px",
                  border: "none",
                  background: mode === m ? "var(--teal)" : "transparent",
                  color: mode === m ? "var(--navy)" : "var(--muted)",
                  fontFamily: "'DM Sans', sans-serif",
                  fontSize: "14px",
                  fontWeight: 500,
                  cursor: "pointer",
                  transition: "all 0.2s",
                  textTransform: "capitalize",
                }}
              >
                {m === "login" ? "Sign In" : "Register"}
              </button>
            ))}
          </div>

          <div
            style={{ display: "flex", flexDirection: "column", gap: "16px" }}
          >
            {mode === "register" && (
              <div className="input-group">
                <label className="input-label">Full Name</label>
                <input
                  className="input"
                  placeholder="John Smith"
                  value={form.fullName}
                  onChange={(e) => set("fullName", e.target.value)}
                />
              </div>
            )}
            <div className="input-group">
              <label className="input-label">Email Address</label>
              <input
                className="input"
                type="email"
                placeholder="you@example.com"
                value={form.email}
                onChange={(e) => set("email", e.target.value)}
              />
            </div>
            <div className="input-group">
              <label className="input-label">Password</label>
              <input
                className="input"
                type="password"
                placeholder="••••••••"
                value={form.password}
                onChange={(e) => set("password", e.target.value)}
                onKeyDown={(e) => e.key === "Enter" && submit()}
              />
            </div>
            {mode === "register" && (
              <div className="input-group">
                <label className="input-label">Role</label>
                <select
                  className="input"
                  value={form.role}
                  onChange={(e) => set("role", e.target.value)}
                >
                  <option value="ROLE_PATIENT">Patient</option>
                  <option value="ROLE_DOCTOR">Doctor</option>
                  <option value="ROLE_HOSPITAL_ADMIN">Hospital Admin</option>
                </select>
              </div>
            )}
            <button
              className="btn btn-primary btn-full"
              style={{ marginTop: "8px" }}
              onClick={submit}
              disabled={loading}
            >
              {loading
                ? "Please wait..."
                : mode === "login"
                  ? "Sign In →"
                  : "Create Account →"}
            </button>
          </div>

          {mode === "login" && (
            <>
              <hr className="divider" />
              <div
                style={{
                  fontSize: "12px",
                  color: "var(--muted)",
                  lineHeight: 1.8,
                }}
              >
                <div
                  style={{
                    fontWeight: 600,
                    marginBottom: "6px",
                    color: "var(--white)",
                  }}
                >
                  Demo credentials
                </div>
                <div>
                  Admin:{" "}
                  <span style={{ color: "var(--teal)" }}>
                    admin@healthcare.com
                  </span>{" "}
                  / Admin@123
                </div>
                <div>
                  Doctor:{" "}
                  <span style={{ color: "var(--teal)" }}>doctor@test.com</span>{" "}
                  / Password@123
                </div>
                <div>
                  Patient:{" "}
                  <span style={{ color: "var(--teal)" }}>patient@test.com</span>{" "}
                  / Password@123
                </div>
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
export default AuthPage;
