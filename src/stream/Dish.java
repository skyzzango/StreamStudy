package stream;

import java.util.Arrays;
import java.util.List;

public class Dish {
	private final String name;
	private final boolean vegetarian;
	private final int calories;
	private final Type type;

	public Dish(String name, boolean vegetarian, int calories, Type type) {
		this.name = name;
		this.vegetarian = vegetarian;
		this.calories = calories;
		this.type = type;
	}

	public enum Type {
		MEAT, FISH, OTHER
	}

	public String getName() {
		return name;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}

	public int getCalories() {
		return calories;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "Dish{" +
				"name='" + name + '\'' +
				'}';
	}

	public static List<Dish> createMenu() {
		return Arrays.asList(
				new Dish("pork", false, 200, Dish.Type.MEAT),
				new Dish("beef", false, 300, Dish.Type.MEAT),
				new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french", true, 500, Dish.Type.OTHER),
				new Dish("rice", true, 500, Dish.Type.OTHER),
				new Dish("season fruit", true, 500, Dish.Type.OTHER),
				new Dish("pizza", true, 500, Dish.Type.OTHER),
				new Dish("prawns", true, 500, Dish.Type.FISH),
				new Dish("salmon", true, 600, Dish.Type.FISH)
		);
	}
}