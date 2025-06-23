package com.walhay.lab.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataLoader {

    public static Geometry.Point parsePointFromString(String string) throws NumberFormatException {
        String[] splitted = string.split(" ");
        double x = Double.parseDouble(splitted[0]);
        double y = Double.parseDouble(splitted[1]);

        return new Geometry.Point(x, y);
    }

    public static Set<Geometry.Point> importPointsFromFile(Path path) throws IOException, NumberFormatException {
        Stream<String> lines = Files.lines(path);
        var data = lines.map(DataLoader::parsePointFromString).collect(Collectors.toSet());

        lines.close();
        return data;
    }

    private static String getPointString(Geometry.Point point) {
        return String.format(Locale.US, "%f %f\n", point.x(), point.y());
    }

    public static void exportPointsToFile(Path path, Set<Geometry.Point> points) throws IOException {
        try (PrintWriter writer = new PrintWriter(path.toString())) {
            points.stream().map(DataLoader::getPointString).forEach(writer::write);
        }
    }
}