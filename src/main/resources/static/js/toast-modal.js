// Custom Professional Notification (Toast) and Confirm Modal Utility
(function() {
    // 1. Inject CSS stylesheet dynamically
    const css = `
    .toast-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 100000;
        display: flex;
        flex-direction: column;
        gap: 10px;
        pointer-events: none;
        font-family: 'Outfit', 'Poppins', sans-serif;
    }
    .toast-alert {
        background: #131b2e;
        border-left: 4px solid #3b82f6;
        border-radius: 8px;
        color: #f8fafc;
        padding: 14px 22px;
        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.4);
        font-size: 14px;
        font-weight: 500;
        display: flex;
        align-items: center;
        gap: 12px;
        min-width: 280px;
        max-width: 400px;
        pointer-events: auto;
        transform: translateX(120%);
        transition: transform 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55), opacity 0.3s;
        opacity: 0;
        border: 1px solid #1e293b;
    }
    .toast-alert.show {
        transform: translateX(0);
        opacity: 1;
    }
    .toast-alert i {
        font-size: 16px;
        flex-shrink: 0;
    }
    .toast-alert.success { border-left-color: #22c55e; }
    .toast-alert.error { border-left-color: #ef4444; }
    .toast-alert.info { border-left-color: #3b82f6; }
    .toast-alert.warning { border-left-color: #f59e0b; }

    /* Modal styles */
    .custom-modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(0, 0, 0, 0.6);
        backdrop-filter: blur(4px);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 99999;
        font-family: 'Outfit', 'Poppins', sans-serif;
        opacity: 0;
        transition: opacity 0.2s ease-out;
    }
    .custom-modal-overlay.show {
        opacity: 1;
    }
    .custom-modal-box {
        background: #131b2e;
        border: 1px solid #1e293b;
        border-radius: 12px;
        width: 90%;
        max-width: 400px;
        box-shadow: 0 15px 35px rgba(0, 0, 0, 0.5);
        transform: scale(0.9);
        transition: transform 0.2s ease-out;
        overflow: hidden;
    }
    .custom-modal-overlay.show .custom-modal-box {
        transform: scale(1);
    }
    .custom-modal-header {
        padding: 16px 20px;
        border-bottom: 1px solid #1e293b;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .custom-modal-header h4 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        color: #ffffff;
    }
    .custom-modal-header i {
        color: #64748b;
        cursor: pointer;
        transition: color 0.2s;
    }
    .custom-modal-header i:hover { color: #ffffff; }
    .custom-modal-body {
        padding: 20px;
        color: #cbd5e1;
        font-size: 14px;
        line-height: 1.5;
    }
    .custom-modal-footer {
        padding: 12px 20px;
        background: #0d1321;
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        border-top: 1px solid #1e293b;
    }
    .custom-modal-btn {
        padding: 8px 16px;
        border-radius: 6px;
        font-size: 13px;
        font-weight: 500;
        cursor: pointer;
        border: none;
        transition: all 0.2s;
    }
    .custom-modal-btn-secondary {
        background: #1e293b;
        color: #94a3b8;
        border: 1px solid #334155;
    }
    .custom-modal-btn-secondary:hover {
        background: #334155;
        color: #ffffff;
    }
    .custom-modal-btn-primary {
        background: #3b82f6;
        color: #ffffff;
        box-shadow: 0 0 10px rgba(59, 130, 246, 0.3);
    }
    .custom-modal-btn-primary:hover {
        background: #2563eb;
        box-shadow: 0 0 15px rgba(59, 130, 246, 0.5);
    }
    `;

    const styleEl = document.createElement("style");
    styleEl.innerHTML = css;
    document.head.appendChild(styleEl);

    // 2. Insert HTML markup dynamically once DOM is loaded
    function injectElements() {
        if (document.getElementById("toastContainer")) return;

        // Create Toast Container
        const toastContainer = document.createElement("div");
        toastContainer.id = "toastContainer";
        toastContainer.className = "toast-container";
        document.body.appendChild(toastContainer);

        // Create Modal Overlay
        const modalOverlay = document.createElement("div");
        modalOverlay.id = "customModal";
        modalOverlay.className = "custom-modal-overlay";
        modalOverlay.style.display = "none";
        modalOverlay.innerHTML = `
            <div class="custom-modal-box">
                <div class="custom-modal-header">
                    <h4 id="customModalTitle">Confirmation</h4>
                    <i class="fas fa-times" id="customModalCloseIcon"></i>
                </div>
                <div class="custom-modal-body">
                    <p id="customModalMessage">Are you sure?</p>
                </div>
                <div class="custom-modal-footer">
                    <button id="customModalCancelBtn" class="custom-modal-btn custom-modal-btn-secondary">Cancel</button>
                    <button id="customModalConfirmBtn" class="custom-modal-btn custom-modal-btn-primary">OK</button>
                </div>
            </div>
        `;
        document.body.appendChild(modalOverlay);
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", injectElements);
    } else {
        injectElements();
    }

    // 3. Define alert/toast function
    window.showToast = function(message, type = "info") {
        const container = document.getElementById("toastContainer");
        if (!container) return;

        const toast = document.createElement("div");
        toast.className = `toast-alert ${type}`;

        let icon = "fa-info-circle";
        const lowerMessage = message.toLowerCase();
        if (type === "success" || lowerMessage.includes("success") || lowerMessage.includes("thank you")) {
            icon = "fa-check-circle";
            toast.className = `toast-alert success`;
        } else if (type === "error" || lowerMessage.includes("error") || lowerMessage.includes("fail") || lowerMessage.includes("denied")) {
            icon = "fa-exclamation-circle";
            toast.className = `toast-alert error`;
        }

        toast.innerHTML = `
            <i class="fas ${icon}"></i>
            <span>${message}</span>
        `;

        container.appendChild(toast);
        toast.offsetHeight; // force reflow
        toast.classList.add("show");

        setTimeout(() => {
            toast.classList.remove("show");
            setTimeout(() => toast.remove(), 300);
        }, 3200);
    };

    // Override native window.alert
    window.alert = function(message) {
        window.showToast(message, "info");
    };

    // Define custom confirm helper (Promise-based)
    window.confirmAction = function(message, title = "Confirm Action") {
        return new Promise((resolve) => {
            const overlay = document.getElementById("customModal");
            const titleEl = document.getElementById("customModalTitle");
            const msgEl = document.getElementById("customModalMessage");
            const cancelBtn = document.getElementById("customModalCancelBtn");
            const confirmBtn = document.getElementById("customModalConfirmBtn");
            const closeIcon = document.getElementById("customModalCloseIcon");

            if (!overlay) {
                // Fallback to native confirm if DOM is not ready
                resolve(window.confirm(message));
                return;
            }

            titleEl.innerText = title;
            msgEl.innerText = message;
            overlay.style.display = "flex";
            overlay.offsetHeight; // force reflow
            overlay.classList.add("show");

            function cleanUp(result) {
                overlay.classList.remove("show");
                setTimeout(() => {
                    overlay.style.display = "none";
                }, 200);
                confirmBtn.removeEventListener("click", onConfirm);
                cancelBtn.removeEventListener("click", onCancel);
                closeIcon.removeEventListener("click", onCancel);
                resolve(result);
            }

            function onConfirm() { cleanUp(true); }
            function onCancel() { cleanUp(false); }

            confirmBtn.addEventListener("click", onConfirm);
            cancelBtn.addEventListener("click", onCancel);
            closeIcon.addEventListener("click", onCancel);
        });
    };
})();
