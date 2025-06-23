import json
import math
import subprocess
import matplotlib.pyplot as plot
import numpy as np
import os


color: tuple[int, int, int] = (0, 0, 0)

algos: dict[str, str] = {
    "LIB": "o",
    "DDA": "s",
    "BRS_INT": "^",
    "BRS_F": "*",
    "BRS_AA": "v",
    "WU": "d",
}

angles: list[int] = [15 * i for i in range(0, 7)]
lengths: list[int] = [200 * i for i in range(1, 6)]
offset: int = 10
count: int = 500

test_name: str = "test.json"


def generate_angle_test(
    algo: str, angle: float, canvas: int, length: int, count: int
) -> None:
    start = (offset, offset)
    end = (
        int(offset + length * math.sin(angle)),
        int(offset + length * math.cos(angle)),
    )

    line = (start, end)

    test = {
        "line": line,
        "color": color,
        "canvas": (canvas, canvas),
        "count": count,
        "algo": algo,
    }

    with open(test_name, "w") as testfile:
        json.dump(test, testfile, indent=4)


def run_test() -> float:
    result = subprocess.check_output(["./build/image/bin/app-cli", test_name])
    result_json = json.loads(result)
    return result_json["elTime"]


def main():
    if not os.path.isdir("plots"):
        os.mkdir("plots")

    for algo in algos.keys():
        times = []

        for length in lengths:
            time = 0

            for angle in angles:
                generate_angle_test(
                    algo, math.radians(angle), length + offset * 2, length, count
                )

                time += run_test()

            times.append(time / len(angles))

        kfc = np.polyfit(lengths, times, 2)
        poly = np.poly1d(kfc)
        plot.plot(lengths, poly(lengths), algos[algo] + "-", label=str(algo))

    plot.legend(loc="upper left")
    plot.xlabel("length, px")
    plot.ylabel("time, ms")
    plot.grid(True)
    plot.savefig("plots/data.svg")
    plot.close()

    for algo in algos.keys():
        times = []

        for length in lengths:
            time = 0

            for angle in angles:
                generate_angle_test(
                    algo, math.radians(angle), length + offset * 2, length, count
                )

                time += run_test()

            times.append(time / len(angles))

        kfc = np.polyfit(lengths, times, 2)
        poly = np.poly1d(kfc)

        plot.plot(lengths, poly(lengths), label=str(algo), alpha=0.7)
        plot.scatter(
            lengths, times, marker="^", color="red", label=f"Raw {str(algo).lower()}"
        )

        plot.legend(loc="upper left")
        plot.xlabel("length, px")
        plot.ylabel("time, ms")
        plot.grid(True)
        plot.savefig(f"plots/{str(algo).lower()}.svg")
        plot.close()


if __name__ == "__main__":
    main()
