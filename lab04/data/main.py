import matplotlib.pyplot as plot
import numpy as np
import os


data = {}


def process_algo(algo_data: dict[int, float], count: int, time: float) -> None:
    if count in data:
        algo_data[count] += time
        algo_data[count] /= 2.0
    else:
        algo_data[count] = time


def process_data(line: str) -> None:
    splitted = line.split()
    algo = str(splitted[0])
    count = int(splitted[1])
    time = float(splitted[2])

    if algo not in data:
        data[algo] = {}

    process_algo(data[algo], count, time)


def load_data(filename: str) -> None:
    with open(filename, "r", encoding="utf-8") as file:
        for line in file.readlines():
            process_data(line)


def main():
    filename = "data/data.txt"

    load_data(filename)

    markers = {
        "GENERAL": "o",
        "PARAM": "s",
    }

    if not os.path.isdir("plots"):
        os.mkdir("plots")

    for key, value in data.items():
        x = list(value.keys())
        y = list(value.values())

        kfc = np.polyfit(x, y, 2)
        poly = np.poly1d(kfc)

        # plot.scatter(x, y, marker='^', label="Raw " + str(key))
        plot.plot(x, poly(x), markers[key] + "-", label=str(key))

    plot.legend(loc="upper left")
    plot.xlabel("circle radius, pixels")
    plot.ylabel("time, ms")
    plot.grid()
    plot.savefig("plots/data.svg")
    plot.close()

    for key, value in data.items():
        x = list(value.keys())
        y = list(value.values())

        kfc = np.polyfit(x, y, 2)
        poly = np.poly1d(kfc)

        plot.plot(x, poly(x), markers[key] + "-", label=str(key), alpha=0.7)
        plot.scatter(x, y, marker="^", label="Raw " + str(key), color="red")
        plot.legend(loc="upper left")
        plot.xlabel("circle radius, pixels")
        plot.ylabel("time, ms")
        plot.grid()
        plot.savefig(f"plots/{str(key).lower()}.svg")
        plot.close()


if __name__ == "__main__":
    main()
