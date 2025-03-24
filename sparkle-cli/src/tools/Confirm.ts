interface ConfirmOptions {
  title?: string;
  msg: string;
  confirmText?: string;
  cancelText?: string;
}

class Confirm {
  private static modal: HTMLElement | null = null;
  private static mask: HTMLElement | null = null;
  private static resolve: ((value: boolean) => void) | null = null;

  static show(options: ConfirmOptions): Promise<boolean> {
    // 清理已有实例
    if (Confirm.modal) {
      Confirm._destroy();
    }
    
    return new Promise((resolve) => {
      Confirm.resolve = resolve;

      // 创建遮罩层
      Confirm.mask = document.createElement("div");
      Object.assign(Confirm.mask.style, {
        position: "fixed",
        top: "0",
        left: "0",
        width: "100%",
        height: "100%",
        backgroundColor: "rgba(0,0,0,0.5)",
        // fixme 后续统一管理
        zIndex: "1000"
      });

      // 创建模态框
      Confirm.modal = document.createElement("div");
      Confirm.modal.innerHTML = `
        <div class="confirm-content">
          ${options.title ? `<h3>${options.title}</h3>` : ""}
          <p>${options.msg}</p>
          <div class="button-group">
            <button class="cancel-btn">${options.cancelText || "取消"}</button>
            <button class="confirm-btn">${options.confirmText || "确认"}</button>
          </div>
        </div>
      `;

      // 动态样式
      Object.assign(Confirm.modal.style, {
        position: "fixed",
        top: "50%",
        left: "50%",
        transform: "translate(-50%, -50%) scale(0.8)",
        backgroundColor: "white",
        padding: "20px",
        borderRadius: "8px",
        opacity: "0",
        transition: "all 0.3s ease",
        // todo 后续统一管理
        zIndex: "1001"
      });

      // 事件绑定
      Confirm.modal
        .querySelector(".confirm-btn")
        ?.addEventListener("click", () => Confirm._handleConfirm(true));
      Confirm.modal
        .querySelector(".cancel-btn")
        ?.addEventListener("click", () => Confirm._handleConfirm(false));

      // 插入DOM
      document.body.appendChild(Confirm.mask);
      document.body.appendChild(Confirm.modal);

      // 触发动画
      requestAnimationFrame(() => {
        Object.assign(Confirm.modal!.style, {
          transform: "translate(-50%, -50%) scale(1)",
          opacity: "1"
        });
      });
    });
  }

  private static _handleConfirm(result: boolean) {
    Confirm.resolve?.(result);
    Confirm._destroy();
  }

  private static _destroy() {
    // 退出动画
    Object.assign(Confirm.modal!.style, {
      transform: "translate(-50%, -50%) scale(0.8)",
      opacity: "0"
    });

    setTimeout(() => {
      Confirm.modal?.remove();
      Confirm.mask?.remove();
      Confirm.modal = null;
      Confirm.mask = null;
    }, 100);
  }
}

export default Confirm;
