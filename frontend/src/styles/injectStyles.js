export const injectStyles = () => {
  const style = document.createElement("style");
  style.textContent = `
    @import url('https://fonts.googleapis.com/css2?family=DM+Serif+Display:ital@0;1&family=DM+Sans:wght@300;400;500;600&display=swap');

    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

    :root {
      --navy: #0a1628;
      --navy-mid: #112240;
      --navy-light: #1e3a5f;
      --teal: #00b4a6;
      --teal-light: #00d4c4;
      --white: #f8fafc;
      --muted: #94a3b8;
      --border: rgba(255,255,255,0.08);
      --card: rgba(17, 34, 64, 0.8);
      --danger: #ef4444;
      --success: #10b981;
      --warning: #f59e0b;
    }

    html, body, #root {
      height: 100%;
      font-family: 'DM Sans', sans-serif;
      background: var(--navy);
      color: var(--white);
    }

    @keyframes fadeUp {
      from { opacity: 0; transform: translateY(24px); }
      to   { opacity: 1; transform: translateY(0); }
    }
    @keyframes fadeIn {
      from { opacity: 0; }
      to   { opacity: 1; }
    }
    @keyframes pulse {
      0%, 100% { opacity: 1; }
      50%       { opacity: 0.5; }
    }
    @keyframes float {
      0%, 100% { transform: translateY(0px); }
      50%       { transform: translateY(-12px); }
    }

    .fade-up   { animation: fadeUp 0.6s ease both; }
    .fade-in   { animation: fadeIn 0.4s ease both; }

    .btn {
      display: inline-flex; align-items: center; gap: 8px;
      padding: 12px 24px; border-radius: 8px; border: none;
      font-family: 'DM Sans', sans-serif; font-size: 14px; font-weight: 500;
      cursor: pointer; transition: all 0.2s ease; letter-spacing: 0.01em;
    }
    .btn-primary { background: var(--teal); color: var(--navy); }
    .btn-primary:hover { background: var(--teal-light); transform: translateY(-1px); box-shadow: 0 8px 24px rgba(0,180,166,0.3); }
    .btn-secondary { background: transparent; color: var(--white); border: 1px solid var(--border); }
    .btn-secondary:hover { border-color: var(--teal); color: var(--teal); }
    .btn-danger { background: rgba(239,68,68,0.15); color: var(--danger); border: 1px solid rgba(239,68,68,0.3); }
    .btn-danger:hover { background: rgba(239,68,68,0.25); }
    .btn:disabled { opacity: 0.5; cursor: not-allowed; transform: none !important; }
    .btn-sm { padding: 8px 16px; font-size: 13px; }
    .btn-full { width: 100%; justify-content: center; }

    .input {
      width: 100%; padding: 12px 16px;
      background: rgba(255,255,255,0.05); border: 1px solid var(--border);
      border-radius: 8px; color: var(--white);
      font-family: 'DM Sans', sans-serif; font-size: 14px;
      transition: border-color 0.2s; outline: none;
    }
    .input:focus { border-color: var(--teal); background: rgba(255,255,255,0.08); }
    .input::placeholder { color: var(--muted); }
    .input-group { display: flex; flex-direction: column; gap: 6px; }
    .input-label { font-size: 13px; color: var(--muted); font-weight: 500; }

    .card {
      background: var(--card); border: 1px solid var(--border);
      border-radius: 16px; padding: 24px; backdrop-filter: blur(12px);
    }

    .badge {
      display: inline-flex; align-items: center; gap: 4px;
      padding: 4px 10px; border-radius: 999px;
      font-size: 11px; font-weight: 600; letter-spacing: 0.05em; text-transform: uppercase;
    }
    .badge-teal   { background: rgba(0,180,166,0.15);  color: var(--teal); }
    .badge-yellow { background: rgba(245,158,11,0.15); color: var(--warning); }
    .badge-green  { background: rgba(16,185,129,0.15); color: var(--success); }
    .badge-red    { background: rgba(239,68,68,0.15);  color: var(--danger); }
    .badge-muted  { background: rgba(148,163,184,0.15); color: var(--muted); }

    .toast {
      position: fixed; bottom: 24px; right: 24px; z-index: 9999;
      padding: 14px 20px; border-radius: 10px;
      font-size: 14px; font-weight: 500;
      animation: fadeUp 0.3s ease;
      max-width: 360px; display: flex; align-items: center; gap: 10px;
    }
    .toast-success { background: rgba(16,185,129,0.15); border: 1px solid rgba(16,185,129,0.3); color: var(--success); }
    .toast-error   { background: rgba(239,68,68,0.15);  border: 1px solid rgba(239,68,68,0.3);  color: var(--danger); }

    .divider { border: none; border-top: 1px solid var(--border); margin: 20px 0; }

    .stat-card {
      background: var(--card); border: 1px solid var(--border);
      border-radius: 12px; padding: 20px;
      display: flex; flex-direction: column; gap: 8px;
    }
    .stat-value { font-family: 'DM Serif Display', serif; font-size: 36px; color: var(--teal); }
    .stat-label { font-size: 13px; color: var(--muted); }

    .sidebar {
      width: 240px; min-height: 100vh;
      background: var(--navy-mid); border-right: 1px solid var(--border);
      display: flex; flex-direction: column; padding: 24px 16px;
      position: fixed; top: 0; left: 0;
    }
    .sidebar-logo {
      font-family: 'DM Serif Display', serif; font-size: 20px;
      color: var(--teal); padding: 0 8px 24px;
      border-bottom: 1px solid var(--border); margin-bottom: 24px;
    }
    .nav-item {
      display: flex; align-items: center; gap: 10px;
      padding: 10px 12px; border-radius: 8px;
      font-size: 14px; color: var(--muted); cursor: pointer;
      transition: all 0.2s; margin-bottom: 2px;
    }
    .nav-item:hover { background: rgba(255,255,255,0.05); color: var(--white); }
    .nav-item.active { background: rgba(0,180,166,0.12); color: var(--teal); font-weight: 500; }
    .nav-icon { font-size: 16px; width: 20px; text-align: center; }

    .main-content { margin-left: 240px; min-height: 100vh; padding: 32px; background: var(--navy); }

    .page-header { margin-bottom: 32px; }
    .page-title { font-family: 'DM Serif Display', serif; font-size: 28px; margin-bottom: 4px; }
    .page-subtitle { color: var(--muted); font-size: 14px; }

    .grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
    .grid-3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 16px; }
    .grid-4 { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }

    .table-wrapper { overflow-x: auto; }
    table { width: 100%; border-collapse: collapse; font-size: 14px; }
    th { padding: 12px 16px; text-align: left; color: var(--muted); font-size: 12px; font-weight: 600; letter-spacing: 0.05em; text-transform: uppercase; border-bottom: 1px solid var(--border); }
    td { padding: 14px 16px; border-bottom: 1px solid var(--border); color: var(--white); vertical-align: middle; }
    tr:last-child td { border-bottom: none; }
    tr:hover td { background: rgba(255,255,255,0.02); }

    .empty-state { text-align: center; padding: 48px; color: var(--muted); }
    .empty-icon { font-size: 40px; margin-bottom: 12px; }
    .empty-text { font-size: 15px; }

    .section-title { font-size: 16px; font-weight: 600; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; }

    select.input { cursor: pointer; }
    select.input option { background: var(--navy-mid); }

    ::-webkit-scrollbar { width: 6px; }
    ::-webkit-scrollbar-track { background: transparent; }
    ::-webkit-scrollbar-thumb { background: var(--border); border-radius: 3px; }
  `;
  document.head.appendChild(style);
};
