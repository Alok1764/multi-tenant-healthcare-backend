import { useState } from "react";
import { api } from "../api/api.js";

function AvailabilityForm({ token, toastShow }) {
  const days = [
    "MONDAY",
    "TUESDAY",
    "WEDNESDAY",
    "THURSDAY",
    "FRIDAY",
    "SATURDAY",
  ];
  const [slots, setSlots] = useState(
    days.map((d) => ({
      day: d,
      startTime: "09:00",
      endTime: "17:00",
      enabled: d !== "SATURDAY",
    })),
  );
  const [loading, setLoading] = useState(false);

  const toggle = (i) =>
    setSlots((s) =>
      s.map((x, j) => (j === i ? { ...x, enabled: !x.enabled } : x)),
    );
  const update = (i, k, v) =>
    setSlots((s) => s.map((x, j) => (j === i ? { ...x, [k]: v } : x)));

  const submit = async () => {
    setLoading(true);
    try {
      const availability = slots
        .filter((s) => s.enabled)
        .map((s) => ({
          dayOfWeek: s.day,
          startTime: s.startTime,
          endTime: s.endTime,
        }));
      await api.post("/doctors/availability", { availability }, token);
      toastShow("Availability updated!", "success");
    } catch (e) {
      toastShow(e.message, "error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
      {slots.map((s, i) => (
        <div
          key={s.day}
          style={{
            display: "flex",
            alignItems: "center",
            gap: "12px",
            padding: "12px",
            borderRadius: "8px",
            background: s.enabled ? "rgba(0,180,166,0.05)" : "transparent",
            border: "1px solid var(--border)",
          }}
        >
          <input
            type="checkbox"
            checked={s.enabled}
            onChange={() => toggle(i)}
            style={{
              accentColor: "var(--teal)",
              width: "16px",
              height: "16px",
            }}
          />
          <span
            style={{
              width: "100px",
              fontSize: "13px",
              fontWeight: 500,
              color: s.enabled ? "var(--white)" : "var(--muted)",
            }}
          >
            {s.day}
          </span>
          {s.enabled && (
            <>
              <input
                type="time"
                value={s.startTime}
                onChange={(e) => update(i, "startTime", e.target.value)}
                className="input"
                style={{ width: "120px", padding: "8px 12px" }}
              />
              <span style={{ color: "var(--muted)", fontSize: "13px" }}>
                to
              </span>
              <input
                type="time"
                value={s.endTime}
                onChange={(e) => update(i, "endTime", e.target.value)}
                className="input"
                style={{ width: "120px", padding: "8px 12px" }}
              />
            </>
          )}
        </div>
      ))}
      <button
        className="btn btn-primary"
        onClick={submit}
        disabled={loading}
        style={{ marginTop: "8px" }}
      >
        {loading ? "Saving..." : "Save Availability"}
      </button>
    </div>
  );
}
export default AvailabilityForm;
