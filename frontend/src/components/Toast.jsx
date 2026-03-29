import { useState, useEffect } from "react";

export function Toast({ message, type, onClose }) {
  useEffect(() => {
    const t = setTimeout(onClose, 3500);
    return () => clearTimeout(t);
  }, []);

  return (
    <div className={`toast toast-${type}`}>
      <span>{type === "success" ? "✓" : "✕"}</span>
      {message}
    </div>
  );
}

export function useToast() {
  const [toast, setToast] = useState(null);
  const show = (message, type = "success") => setToast({ message, type });
  const hide = () => setToast(null);
  const ToastEl = toast ? <Toast {...toast} onClose={hide} /> : null;
  return { show, ToastEl };
}
