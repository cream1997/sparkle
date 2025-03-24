interface TipOptions {
  msg: string;
  type: string;
  duration?: number;
}

class Tip {
  private static el: HTMLElement | null = null;
  private static timeoutId: number | null = null;

  private static show(options: TipOptions): void {
    if (Tip.timeoutId) {
      clearTimeout(Tip.timeoutId);
      Tip.el?.remove();
    }

    const tipEl = document.createElement("div");
    tipEl.textContent = options.msg;

    Object.assign(tipEl.style, {
      position: "fixed",
      top: "50%",
      left: "50%",
      transform: "translate(-50%, -50%)",
      padding: "12px 24px",
      // todo 将来统一管理，先写个很大的值
      zIndex: 9999,
      fontSize: "16px"
    });

    tipEl.style.color = options.type === "info" ? "green" : "red";
    tipEl.style.backgroundColor = "transparent";

    document.body.appendChild(tipEl);
    Tip.el = tipEl;
    // 自动消失
    Tip.timeoutId = window.setTimeout(() => {
      tipEl.remove();
      Tip.timeoutId = null;
    }, options.duration || 2000);
  }

  static info(msg: string, duration?: number) {
    this.show({ msg, type: "info", duration });
  }

  static err(msg: string, duration?: number) {
    this.show({ msg, type: "error", duration });
  }
}

export default Tip;
