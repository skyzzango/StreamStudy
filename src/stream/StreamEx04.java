package stream;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class StreamEx04 {
	public static void main(String[] args) {
		List<Dish> menu = Arrays.asList(
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

		// Java 7 Code
		List<Dish> lowCaloricDishes = new ArrayList<>();

		// 누적자로 요소 필터링
		for (Dish d : menu) {
			if (d.getCalories() < 400) {
				lowCaloricDishes.add(d);
			}
		}

		// 익명 클래스로 요리 정렬
		Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
			public int compare(Dish d1, Dish d2) {
				return Integer.compare(d1.getCalories(), d2.getCalories());
			}
		});

		// 정렬된 리스트를 처리하면서 요리 이름 선택
		List<String> lowCaloricDishesName1 = new ArrayList<>();
		for (Dish d : lowCaloricDishes) {
			lowCaloricDishesName1.add(d.getName());
		}

		// Java 8 Code
//		List<String> lowCaloricDishesName2 = menu.stream()
//				.filter(d -> d.getCalories() < 400)
//				.sorted(Comparator.comparing(Dish::getCalories))
//				.collect(toList());

		// Java 8 Parallel(병렬)
		List<String> lowCaloricDishesName3 = menu.parallelStream()
				.filter(d -> d.getCalories() < 400)
				.sorted(Comparator.comparing(Dish::getCalories))
				.map(d -> d.getName())
				.collect(toList());
		System.out.println(lowCaloricDishesName3);

		Map<Dish.Type, List<Dish>> dishesByType =
				menu.stream().collect(groupingBy(Dish::getType));
		System.out.println(dishesByType);

		List<String> threeHighCaloricDishNames =
				menu.stream() // 메뉴에서 스트림을 얻는다.(Source)
				.filter(d -> d.getCalories() > 300) // 고칼로리 요리 필터링
				.map(Dish::getName) // 요리명 추출
				.limit(3) // 선착순 3개만 선택
				.collect(toList()); // 결과를 다른 리스트로 반환
		System.out.println(threeHighCaloricDishNames);

		List<String> title = Arrays.asList("Java8", "In", "Action");
		Stream<String> s = title.stream();
		s.forEach(System.out::println); // title 의 각 단어를 출력
//		s.forEach(System.out::print); // IllegalStateException: 스트림이 이미 소비되었거나 닫힘.

		List<String> names = new ArrayList<>();
		for (Dish d : menu) { // 메뉴 리스트를 명시적으로 순차 반복
			names.add(d.getName()); // 이름을 추출해서 리스트에 추가
		}
		System.out.println(names);

		List<String> names1 = new ArrayList<>();
		Iterator<Dish> iterator = menu.iterator();
		while (iterator.hasNext()) {
			Dish d = iterator.next();
			names1.add(d.getName());
		}
		System.out.println(names1);

		List<String> names2 = menu.stream()
				.map(Dish::getName) // 요리명 추출
				.collect(toList()); // 파이프라인 실행(반복자는 필요없다.)
		System.out.println(names2);

		List<String> names3 = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.map(Dish::getName)
				.limit(3)
				.collect(toList());
		System.out.println(names3);

		List<String> names4 = menu.stream()
				.filter(d -> {
					System.out.println("filtering: " + d.getName());
					return d.getCalories() > 300;
				})
				.map(d -> {
					System.out.println("mapping: " + d.getName());
					return d.getName();
				})
				.limit(3)
				.collect(toList());
		System.out.println(names4);

		menu.stream().forEach(System.out::println);

		long count = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.distinct()
				.limit(3)
				.count();
		System.out.println(count);
	}

}
