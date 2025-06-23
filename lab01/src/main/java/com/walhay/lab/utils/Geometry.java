package com.walhay.lab.utils;

import java.util.*;

public class Geometry {
	private final Set<Point> points = new HashSet<>();

	public void addPoint(double x, double y) { points.add(new Point(x, y)); }

	public void removePoint(double x, double y) { points.removeIf(p -> p.x() == x && p.y() == y); }

	public void removePoint(Point point) { points.remove(point); }

	public static double getDistanceBetweenPoints(Point a, Point b) {
		return Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2));
	}

	public Set<Triangle> getAllTriangles() {
		Set<Triangle> triangles = new HashSet<>();
		final List<Point> pointList = new ArrayList<>(points);

		int count = pointList.size();

		for (int i = 0; i < count - 2; ++i) {
			for (int j = i + 1; j < count - 1; ++j) {
				for (int k = j + 1; k < count; ++k) {
					List<Point> trianglePoints = List.of(pointList.get(i), pointList.get(j), pointList.get(k));

					Triangle triangle = new Triangle(trianglePoints);
					if (Triangle.validateTriangle(triangle)) triangles.add(triangle);
				}
			}
		}

		return triangles;
	}

	/**
	 * @param triangles
	 * @return array of triangles with the smallest possible result of division of their perimeters
	 */
	public List<Geometry.Triangle> findTriangles(Set<Triangle> triangles) {
		double rel = 0;
		Triangle smolOne = null, smolTwo = null;

		for (Triangle first : triangles) {
			for (Triangle second : triangles) {
				if (!first.hasEqualPoints(second)) {
					double pairRel = getTrianglePerimeterRelation(first, second);
					if (rel == 0 || pairRel < rel) {
						smolOne = first;
						smolTwo = second;

						rel = pairRel;
					}
				}
			}
		}

		if (smolOne == null || smolTwo == null) return Collections.emptyList();
		return Arrays.asList(smolOne, smolTwo);
	}

	public Set<Point> getPoints() { return points; }

	public double getTrianglePerimeterRelation(Triangle a, Triangle b) { return a.getPerimeter() / b.getPerimeter(); }

	public record Point(double x, double y) {
		public double getDistanceTo(Point point) { return getDistanceBetweenPoints(this, point); }

		@Override
		public String toString() {
			return String.format("x: %f, y: %f", x, y);
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof Point point) return point.x == this.x && point.y == this.y;
			return false;
		}
	}

	public static class Triangle {
		private final List<Point> points;

		private final double area;
		private final double perimeter;

		Triangle(List<Point> points) {
			this.points = points;
			this.area = countArea(points);
			this.perimeter = countPerimeter(points);
		}

		/**
		 * Use determinant to count triangle area
		 *
		 * @param points
		 * @return area of triangle
		 */
		public static double countArea(List<Point> points) {
			Point a = points.get(0);
			Point b = points.get(1);
			Point c = points.get(2);
			return Math.abs(a.x() * (b.y() - c.y()) + b.x() * (c.y() - a.y()) + c.x() * (a.y() - b.y())) / 2;
		}

		public static List<Double> getSidesLength(List<Point> points) {
			Point a = points.get(0);
			Point b = points.get(1);
			Point c = points.get(2);

			double ab = a.getDistanceTo(b);
			double bc = b.getDistanceTo(c);
			double ac = a.getDistanceTo(c);
			return List.of(ab, ac, bc);
		}

		public static double countPerimeter(List<Point> points) {
			return getSidesLength(points).stream().mapToDouble(d -> d).sum();
		}

		public boolean hasEqualPoints(Triangle triangle) {
			for (Point p1 : triangle.points)
				for (Point p2 : this.points)
					if (p1.equals(p2)) return true;
			return false;
		}

		public double getArea() { return this.area; }

		public double getPerimeter() { return this.perimeter; }

		public List<Point> getPoints() { return points; }

		public static boolean validateTriangle(Triangle triangle) {
			if (triangle.area == 0) return false;

			List<Double> sides = getSidesLength(triangle.points);
			double ab = sides.get(0);
			double ac = sides.get(1);
			double bc = sides.get(2);

			return ab + ac > bc && ab + bc > ac && ac + bc > ab;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			for (Point point : points) builder.append(point.toString()).append("\n");
			builder.append(String.format("Perimeter: %f\n", perimeter));
			return builder.toString();
		}

		@Override
		public int hashCode() {
			return Objects.hash(points, area, perimeter);
		}

		@Override
		public boolean equals(Object object) {
			if (object instanceof Triangle triangle) {
				if (this == triangle) return true;

				int points = 3;
				for (int i = 0; i < points; ++i) {
					for (int j = 0; j < points; ++j) {
						if (!this.points.get(i).equals(triangle.points.get(j))) return false;
					}
				}
				return true;
			}
			return false;
		}
	}
}
