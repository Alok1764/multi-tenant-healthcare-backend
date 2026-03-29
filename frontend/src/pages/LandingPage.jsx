import { useState } from "react";

function LandingPage({ onLogin }) {
  const features = [
    {
      icon: "🏥",
      title: "Multi-Hospital Support",
      desc: "Manage multiple hospitals, departments, and staff from a single platform.",
    },
    {
      icon: "👨‍⚕️",
      title: "Doctor Management",
      desc: "Onboard doctors, assign specializations, and manage availability schedules.",
    },
    {
      icon: "📅",
      title: "Smart Appointments",
      desc: "Idempotent booking system prevents duplicate appointments on retries.",
    },
    {
      icon: "🔐",
      title: "Role-Based Access",
      desc: "Granular access control across Patient, Doctor, and Admin roles.",
    },
    {
      icon: "💳",
      title: "Payment Processing",
      desc: "Track appointment payments with full audit history per transaction.",
    },
    {
      icon: "📋",
      title: "Medical Records",
      desc: "Secure per-appointment clinical records accessible to authorized roles only.",
    },
  ];

  const roles = [
    {
      role: "Patient",
      color: "teal",
      things: [
        "Book & cancel appointments",
        "View own medical history",
        "Update personal profile",
        "Process appointment payments",
      ],
    },
    {
      role: "Doctor",
      color: "yellow",
      things: [
        "Set weekly availability",
        "View scheduled appointments",
        "Create medical records",
        "Manage appointment slots",
      ],
    },
    {
      role: "Hospital Admin",
      color: "muted",
      things: [
        "Register hospitals",
        "Onboard doctors",
        "Manage specializations",
        "Add patient profiles",
      ],
    },
  ];

  return (
    <div style={{ minHeight: "100vh", background: "var(--navy)" }}>
      {/* Nav */}
      <nav
        style={{
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          padding: "20px 48px",
          borderBottom: "1px solid var(--border)",
          position: "sticky",
          top: 0,
          background: "rgba(10,22,40,0.9)",
          backdropFilter: "blur(12px)",
          zIndex: 100,
        }}
      >
        <div
          style={{ fontFamily: "'DM Serif Display', serif", fontSize: "22px" }}
        >
          <span style={{ color: "var(--teal)" }}>Med</span>Core
        </div>
        <div style={{ display: "flex", gap: "12px" }}>
          <button className="btn btn-secondary btn-sm" onClick={onLogin}>
            Sign In
          </button>
          <button className="btn btn-primary btn-sm" onClick={onLogin}>
            Get Started
          </button>
        </div>
      </nav>

      {/* Hero */}
      <div
        style={{
          padding: "96px 48px 80px",
          maxWidth: "1100px",
          margin: "0 auto",
          textAlign: "center",
        }}
      >
        <div
          className="badge badge-teal fade-up"
          style={{ marginBottom: "24px", animationDelay: "0s" }}
        >
          Healthcare Management Platform
        </div>
        <h1
          className="fade-up"
          style={{
            fontFamily: "'DM Serif Display', serif",
            fontSize: "clamp(48px, 6vw, 72px)",
            lineHeight: 1.1,
            marginBottom: "24px",
            animationDelay: "0.1s",
          }}
        >
          Backend-first healthcare
          <br />
          <span style={{ color: "var(--teal)" }}>infrastructure</span>
        </h1>
        <p
          className="fade-up"
          style={{
            fontSize: "18px",
            color: "var(--muted)",
            maxWidth: "560px",
            margin: "0 auto 40px",
            lineHeight: 1.7,
            animationDelay: "0.2s",
          }}
        >
          A production-grade REST API demonstrating secure JWT authentication,
          role-based access control, and scalable multi-tenant architecture.
        </p>
        <div
          className="fade-up"
          style={{
            display: "flex",
            gap: "12px",
            justifyContent: "center",
            flexWrap: "wrap",
            animationDelay: "0.3s",
          }}
        >
          <button
            className="btn btn-primary"
            onClick={onLogin}
            style={{ padding: "14px 32px", fontSize: "16px" }}
          >
            Try the Dashboard →
          </button>
          <a
            href="http://localhost:8080/swagger-ui/index.html"
            target="_blank"
            rel="noreferrer"
            className="btn btn-secondary"
            style={{ padding: "14px 32px", fontSize: "16px" }}
          >
            View API Docs
          </a>
        </div>
      </div>

      {/* Tech badges */}
      <div
        style={{
          display: "flex",
          justifyContent: "center",
          gap: "12px",
          flexWrap: "wrap",
          padding: "0 48px 80px",
        }}
      >
        {[
          "Spring Boot 3",
          "JWT Auth",
          "MySQL 8",
          "Redis Cache",
          "Docker",
          "Swagger UI",
        ].map((t) => (
          <span
            key={t}
            className="badge badge-muted"
            style={{ padding: "6px 14px", fontSize: "12px" }}
          >
            {t}
          </span>
        ))}
      </div>

      {/* Features */}
      <div
        style={{
          background: "var(--navy-mid)",
          borderTop: "1px solid var(--border)",
          borderBottom: "1px solid var(--border)",
          padding: "80px 48px",
        }}
      >
        <div style={{ maxWidth: "1100px", margin: "0 auto" }}>
          <h2
            style={{
              fontFamily: "'DM Serif Display', serif",
              fontSize: "36px",
              textAlign: "center",
              marginBottom: "48px",
            }}
          >
            Everything built.{" "}
            <span style={{ color: "var(--teal)" }}>Nothing missing.</span>
          </h2>
          <div className="grid-3" style={{ gap: "20px" }}>
            {features.map((f, i) => (
              <div
                key={i}
                className="card fade-up"
                style={{ animationDelay: `${i * 0.08}s` }}
              >
                <div style={{ fontSize: "28px", marginBottom: "12px" }}>
                  {f.icon}
                </div>
                <div
                  style={{
                    fontWeight: 600,
                    marginBottom: "8px",
                    fontSize: "15px",
                  }}
                >
                  {f.title}
                </div>
                <div
                  style={{
                    color: "var(--muted)",
                    fontSize: "13px",
                    lineHeight: 1.6,
                  }}
                >
                  {f.desc}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Roles */}
      <div
        style={{ padding: "80px 48px", maxWidth: "1100px", margin: "0 auto" }}
      >
        <h2
          style={{
            fontFamily: "'DM Serif Display', serif",
            fontSize: "36px",
            textAlign: "center",
            marginBottom: "48px",
          }}
        >
          Three roles. One platform.
        </h2>
        <div className="grid-3" style={{ gap: "20px" }}>
          {roles.map((r, i) => (
            <div
              key={i}
              className="card"
              style={{
                borderColor:
                  r.color === "teal" ? "rgba(0,180,166,0.3)" : "var(--border)",
              }}
            >
              <div
                className={`badge badge-${r.color}`}
                style={{ marginBottom: "16px" }}
              >
                {r.role}
              </div>
              <ul
                style={{
                  listStyle: "none",
                  display: "flex",
                  flexDirection: "column",
                  gap: "10px",
                }}
              >
                {r.things.map((t, j) => (
                  <li
                    key={j}
                    style={{
                      display: "flex",
                      gap: "8px",
                      fontSize: "13px",
                      color: "var(--muted)",
                    }}
                  >
                    <span style={{ color: "var(--teal)", flexShrink: 0 }}>
                      ✓
                    </span>{" "}
                    {t}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </div>

      {/* CTA */}
      <div
        style={{
          textAlign: "center",
          padding: "80px 48px",
          borderTop: "1px solid var(--border)",
        }}
      >
        <h2
          style={{
            fontFamily: "'DM Serif Display', serif",
            fontSize: "36px",
            marginBottom: "16px",
          }}
        >
          Ready to explore?
        </h2>
        <p
          style={{
            color: "var(--muted)",
            marginBottom: "32px",
            fontSize: "15px",
          }}
        >
          Register an account or sign in to access the dashboard.
        </p>
        <button
          className="btn btn-primary"
          onClick={onLogin}
          style={{ padding: "14px 40px", fontSize: "16px" }}
        >
          Open Dashboard →
        </button>
      </div>

      <footer
        style={{
          borderTop: "1px solid var(--border)",
          padding: "24px 48px",
          textAlign: "center",
          color: "var(--muted)",
          fontSize: "13px",
        }}
      >
        MedCore Healthcare API · Built with Spring Boot 3 · Full Swagger docs at{" "}
        <a
          href="http://localhost:8080/swagger-ui/index.html"
          target="_blank"
          rel="noreferrer"
          style={{ color: "var(--teal)" }}
        >
          /swagger-ui/index.html
        </a>
      </footer>
    </div>
  );
}
export default LandingPage;
