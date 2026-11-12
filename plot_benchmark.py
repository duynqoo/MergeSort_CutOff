import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# 1. Đọc dữ liệu từ file CSV
df = pd.read_csv("benchmark_results.csv")

# 2. Tính thời gian trung bình, độ lệch chuẩn và số lượng mẫu theo từng cặp (N, Cutoff)
summary = (
    df.groupby(["N", "Cutoff"])["Time_ms"]
    .agg(mean="mean", std="std", count="count")
    .reset_index()
)

# 3. Cấu hình giao diện biểu đồ
sns.set_theme(style="whitegrid", font_scale=1.05)
fig, axes = plt.subplots(1, 3, figsize=(18, 5.8))

configs = [
    (1_000_000, "N = 10^6 (1 triệu phần tử - TB 10 bộ test)", "#1f77b4"),
    (10_000_000, "N = 10^7 (10 triệu phần tử - TB 10 bộ test)", "#ff7f0e"),
    (100_000_000, "N = 10^8 (100 triệu phần tử - 1 bộ test)", "#2ca02c"),
]

for ax, (n_val, title, color) in zip(axes, configs):
    sub = summary[summary["N"] == n_val].sort_values("Cutoff")
    best_row = sub.loc[sub["mean"].idxmin()]
    best_cutoff = int(best_row["Cutoff"])
    best_time = best_row["mean"]

    # Vẽ đường thời gian trung bình
    ax.plot(
        sub["Cutoff"],
        sub["mean"],
        marker="o",
        linewidth=2.2,
        markersize=6,
        color=color,
        label="Thời gian TB",
    )

    # Nếu có nhiều hơn 1 bộ test (N = 10^6, 10^7), vẽ vùng độ lệch chuẩn (±1 SD)
    if sub["count"].max() > 1:
        ax.fill_between(
            sub["Cutoff"],
            sub["mean"] - sub["std"],
            sub["mean"] + sub["std"],
            color=color,
            alpha=0.15,
            label="±1 Độ lệch chuẩn",
        )

    # Đánh dấu điểm có thời gian chạy nhỏ nhất (Cutoff tối ưu)
    ax.scatter(
        [best_cutoff],
        [best_time],
        color="red",
        s=110,
        zorder=5,
        label=f"Tối ưu: Cutoff={best_cutoff} ({best_time:.1f} ms)",
    )

    offset_x = -25 if best_cutoff >= 18 else 0
    ax.annotate(
        f"Min: {best_time:.1f} ms\n(Cutoff = {best_cutoff})",
        xy=(best_cutoff, best_time),
        xytext=(offset_x, 22),
        textcoords="offset points",
        ha="center",
        fontsize=9.5,
        fontweight="bold",
        color="darkred",
        bbox=dict(boxstyle="round,pad=0.3", fc="#fff3f3", ec="red", lw=1),
        arrowprops=dict(arrowstyle="->", color="red", lw=1.2),
    )

    # Mở rộng biên trục Y để nhãn chú thích không đè lên đường đồ thị
    y_min, y_max = ax.get_ylim()
    y_range = y_max - y_min
    ax.set_ylim(y_min - 0.08 * y_range, y_max + 0.12 * y_range)

    ax.set_title(title, fontsize=12, fontweight="bold", pad=12)
    ax.set_xlabel("Giá trị Cutoff", fontsize=11)
    ax.set_ylabel("Thời gian chạy trung bình (ms)", fontsize=11)
    ax.set_xticks(sub["Cutoff"])
    ax.tick_params(axis="x", rotation=45)
    ax.legend(loc="upper right", fontsize=9)

plt.suptitle(
    "Khảo sát thời gian chạy MergeSort theo giá trị Cutoff trên các quy mô dữ liệu",
    fontsize=14,
    fontweight="bold",
    y=1.02,
)
plt.tight_layout()
plt.savefig("cutoff_benchmark_chart.png", dpi=300, bbox_inches="tight")
plt.show()