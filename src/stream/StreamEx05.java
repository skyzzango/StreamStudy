package stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamEx05 {
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

		/* 5.1 필터링과 슬라이싱
		*   5.1.1 프레디케이트로 필터링
		*       : 스트림 인터페이스는 filter 메서드를 지원한다.
		*           : filter 메서드는 프레디케이트(불린을 반환하는 함수)를 인수로 받아서
		*           일치하는 모든 요소를 포함하는 스트림을 반환한다. */
		System.out.println("\n5.1.1 모든 채식요리를 필터링 해서 리스트 반환.");
		List<Dish> vegetarianMenu = menu.stream()
				.filter(Dish::isVegetarian)
				.collect(Collectors.toList());
		System.out.println(vegetarianMenu);

		/* 5.1.2 고유 요소 필터링
		*   : 스트림은 고유 요소로 이루어진 스트림을 반환하는 distinct 메서드를 지원한다.
		*       : 고유 여부는 스트림에서 만든 객체의 hashCode, equals 로 결정 */
		System.out.println("\n5.1.2 모든 짝수를 선택하고 중복을 필터링해서 출력.");
		List<Integer> numbers1 = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
		numbers1.stream()
				.filter(i -> i % 2 == 0)
				.distinct()
				.forEach(i -> System.out.print(i + " "));

		/* 5.1.3 스트림 축소
		*   : 스트림은 주어진 사이즈 이하의 크기를 갖는 새로운 스트림을 반환하는 limit(n) 메서드를 지원한다.
		*       : 스트림이 정렬되어 있으면 최대 n 개의 요소를 반환할 수 있다. */
		System.out.println("\n\n5.1.3 300칼로리 이상의 세 요리를 선택해서 리스트 반환.");
		List<Dish> dishes1 = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.limit(3)
				.collect(Collectors.toList());
		System.out.println(dishes1);

		/* 5.1.4 요소 건너뛰기
		*   : 스트림은 처음 n 개 요소를 제외한 스트림을 반환하는 skip(n) 메서드를 지원한다.
		*       : n 개이하의 요소를 포함하는 스트림에 skip(n)을 호출하면 빈 스트림이 반환된다.
		*       : limit(n)과 skip(n)은 상호 보완적인 연산을 수행한다. */
		System.out.println("\n5.1.4 300칼로리 이상의 처음 두 요리를 건너뛴 다음에 나머지 요리를 리스트로 반환.");
		List<Dish> dishes2 = menu.stream()
				.filter(d -> d.getCalories() > 300)
				.skip(2)
				.collect(Collectors.toList());
		System.out.println(dishes2);

		/* Quiz
		*   : 스트림을 이용해서 처음 등장하는 두 고기 요리를 필터링 하시오. */
		System.out.println("\nQuiz: 스트림을 이용해서 처음 등장하는 두 고기 요리를 필터링 하시오.");
		List<Dish> dishes3 = menu.stream()
				.filter(d -> d.getType() == Dish.Type.MEAT)
				.skip(2)
				.collect(Collectors.toList());
		System.out.println(dishes3);

		/* 5.2 매핑
		*   : 특정 객체에서 특정 데이터를 선택하는 작업은 데이터 처리 과정에서 자주 수행되는 연산이다.
		*       : 예를 들어 SQL 의 테이블에서 특정 열만 선택.
		*       : 스트림 API 의 map 과 flatMap 메서드는 특정 데이터를 선택하는 기능을 제공. */

		/* 5.2.1 스트림의 각 요소에 함수 적용하기
		*   : 스트림은 함수를 인수로 받는 map 메서드를 지원한다.
		*   : 인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다.
		*       : 이 과정은 기존의 값을 고친다는 개념보다는 새로운 버전을 만든다라는 개념에 가까우므로
		*       변환에 가까운 매핑 이라는 단어를 사용한다. */

		System.out.println("\n5.2.1 Ex01) Dish::getName 을 map 메서드로 전달해서 스트림의 요리명을 추출하는 코드");
		System.out.println("getName() 은 문자열을 반환하므로 map 메서드의 출력 스트림은 Stream 형식을 갖는다.");
		List<String> dishNames1 = menu.stream()
				.map(Dish::getName)
				.collect(Collectors.toList());
		System.out.println(dishNames1);

		System.out.println("\nEx02) 단어 리스트가 주어졌을 때 각 단어가 포함하는 글자 수의 리스트를 반환.");
		List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
		List<Integer> wordLengths = words.stream()
				.map(String::length)
				.collect(Collectors.toList());
		System.out.println(wordLengths);

		System.out.println("\nEx02.1) Ex01 에서 각 요리명의 길이를 알고 싶다면?");
		List<Integer> dishNames2 = menu.stream()
				.map(Dish::getName)
				.map(String::length)
				.collect(Collectors.toList());
		System.out.println(dishNames2);

		/* 5.2.2 스트림 평면화
		*   : ["Hello", "World"] 리스트가 있다면 결과로 ["H", "e", "l", "l", "W", "r", "d"] 가
		*   반환되어야 한다.
		*   : distinct 로 중복된 문자를 필터링해서 쉽게 문제를 새결할 수 있을것이라 추측.
		*       : map 으로 전달되는 람다의 결과는 각 단어의 String 을 반환한다는 점이 문제다.
		*       : map 메서드가 반환한 스트림의 형식은 Stream 이 된다.*/

		System.out.println("\n5.2.2 스트림 평면화\nCase 01: 리스트 안에 String[] 으로 반환된다.");
		String[] arrayOfWord1 = {"Hello", "World"};
		List<String[]> collect1 = Arrays.stream(arrayOfWord1)
				.map(word -> word.split(""))
				.distinct()
				.collect(Collectors.toList());
		System.out.println(Arrays.toString(collect1.get(0)));

		System.out.println("\nCase 02: 리스트 안에 스트림<String> 으로 반환 된다.");
		String[] arrayOfWords2 = {"Goodbye", "World"};
		Stream<String> streamOfWords = Arrays.stream(arrayOfWords2);
		List<Stream<String>> collect2 = streamOfWords
				.map(word -> word.split(""))
				.map(Arrays::stream)
				.distinct()
				.collect(Collectors.toList());
		System.out.println(collect2.get(0).collect(Collectors.toList()));

		/* flatMap 사용
		*   : flatMap 은 각 배열을 스트림이 아니라 스트림의 콘텐트로 매핑한다. */
		System.out.println("\nCase 03: map(Arrays::stream)과 달리 flatMap 은 하나의 평면화된 스트림을 반환.");
		List<String> uniqueCharacters = Arrays.stream(arrayOfWords2)
				.map(word -> word.split(""))
				.flatMap(Arrays::stream)
				.distinct()
				.collect(Collectors.toList());
		System.out.println(uniqueCharacters);

		/* Quiz 01
		*   : 숫자 리스트가 주어졌을 때 각 숫자의 제곱근으로 이루어진 리스트를 반환 하시오.
		*       : 예를 들어 [1, 2, 3, 4, 5] 가 주어지면 [1, 4, 9, 16, 25] 를 반환해야함.
		*       : 답 - 숫자를 인수로 받아 제곱근을 반환하는 람다를 map 으로 넘겨주는 방식으로 해결 */
		System.out.println("\nQuiz01: 숫자 리스트의 각 숫자의 제곱근을 리스트로 반환 하시오.");
		List<Integer> numbers2 = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> squares1 = numbers2.stream()
				.map(n -> n * n)
				.collect(Collectors.toList());
		System.out.println(squares1);


	}
}

