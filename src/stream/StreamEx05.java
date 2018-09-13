package stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
		collect1.forEach(arr -> System.out.println(Arrays.toString(arr)));

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
		 *       : 예를 들어 [1, 2, 3, 4, 5] 가 주어지면 [1, 4, 9, 16, 25] 를 반환.
		 *       : 답 - 숫자를 인수로 받아 제곱근을 반환하는 람다를 map 으로 넘겨주는 방식으로 해결 */
		System.out.println("\nQuiz01: 숫자 리스트의 각 숫자의 제곱근을 리스트로 반환 하시오.");
		List<Integer> numbers2 = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> squares1 = numbers2.stream()
				.map(n -> n * n)
				.collect(Collectors.toList());
		System.out.println(squares1);

		/* Quiz 02
		 *   : 두개의 숫자 리스트가 있을때 모든 숫자 쌍의 리스트를 반환하시오.
		 *       : [1, 2, 3]과 [3, 4]가 주어지면 [(1,3),(1,4),(2,3),(2,4),(3,3),(3,4)]를 반환.
		 *       : 답 - 두개의 map 을 이용해서 두 리스트를 반복한 다음에 숫자 쌍을 만들 수 있다.
		 *       : 결과로 Stream 으로 반환된다.
		 *       : 따라서 Stream 으로 평면화한 스트림이 필요하다.(flayMap 사용 필요)*/
		System.out.println("\nQuiz02: 두 개의 숫자 리스트가 있을 때 모든 숫자 쌍의 리스트를 반환하시오.");
		List<Integer> numbers3 = Arrays.asList(1, 2, 3);
		List<Integer> numbers4 = Arrays.asList(3, 4);
		List<int[]> pairs1 = numbers3.stream()
				.flatMap(
						i -> numbers4.stream()
								.map(j -> new int[]{i, j}))
				.collect(Collectors.toList());
		pairs1.forEach(arr -> System.out.print(Arrays.toString(arr)));
		System.out.println();

		/* Quiz 03
		 *   : 이전 문제에서 합이 3으로 나누어 떨어지는 쌍만 반환하려면 어떻게 해야 할까 ?
		 *       : (2, 4), (3, 3)을 반환.
		 *       : 답 - filter 를 프리디케이트와 함께 사용하면 스트림의 요소를 필터링 할 수 있다.
		 *       : flatMap 을 실행하면 숫자 쌍을 포함하는 int[] 스트림이 반환되므로
		 *       프레디케이트를 이용해서 숫자 쌍의 합이 3으로 나누어 쩔어지는지 확인 할 수 있다.*/
		System.out.println("\nQuiz03: 이전 문제에서 합이 3으로 나누어 떨어지는 쌍만 반환 하시오.");
		List<int[]> pairs2 = numbers3.stream()
				.flatMap(
						i -> numbers4.stream()
								.filter(j -> (i + j) % 3 == 0)
								.map(j -> new int[]{i, j}))
				.collect(Collectors.toList());
		pairs2.forEach(arr -> System.out.print(Arrays.toString(arr)));
		System.out.println();

		List<List<Integer>> intPairs = numbers3.stream()
				.flatMap(
						i -> numbers4.stream()
								.filter(j -> (i + j) % 3 == 0)
								.map(j -> Arrays.asList(i, j)))
				.collect(Collectors.toList());
		System.out.println(intPairs);

		/* 5.3 검색과 매칭
		 *   : anyMatch, allMatch, noneMatch 세가지 메소드가 있으며 쇼트서킷 기법이다.
		 *       : 때로는 전체 스트림을 처리하지 않았더라도 결과를 반환할 수 있다.
		 *           : 여러 and 연산으로 연결된 커다란 불린 표현식을 평가한다면,
		 *           표현식에서 하나라도 거직이라는 결과가 나오면 나머지 표현식의 결과와
		 *           상관없이 전체 결과도 거짓이된다. 이러한 상황을 쇼트서킷 이라고 한다.
		 *       : allMatch, noneMatch, findFirst, findAny 등의 연산은
		 *       모든 요소를 처리하지 않고도 결과를 반환할 수 있다.
		 *           : 원하는 요소를 찾았으면 즉시 결과를 반환.
		 *           : (주어진 크기의 스트림을 생성하는)limit 메서드도 쇼트서킷 연산.
		 *           : 무한한 요소를 가진 스트림을 유한한 크기로 줄일 수 있는 유용한 연산. */

		/* 5.3.1 프리케이트가 적어도 한 요소와 일치하는지 확인
		 *   : anyMatch 메서드 : 프리케이트가 주어진 스트림에서 적어도 한 요소와 일치 하는지 확인.
		 *   : anyMatch 는 boolean 을 반환 하므로 최종 연산이다.*/
		System.out.println("\n5.3.1 모든 요리중 채식요리가 하나라도 포함되어있는지 확인하는 예제.");
		if (menu.stream().anyMatch(Dish::isVegetarian)) {
			System.out.println("The menu is (somewhat) vegetarian friendly!!");
		}

		/* 5.3.2 프리케이트가 모든 요소와 일치하는지 검사
		 *   : allMatch 메서드 : 스트림의 모든 요소가 주어진 프레디케이트와 일치하는지 확인. */
		System.out.println("\n5.3.2\nCase01: 모든 요리가 1000칼로리 이하면 건강식으로 간주하는 예제.");
		if (menu.stream().allMatch(d -> d.getCalories() < 1000)) {
			System.out.println("The menu is healthy food!!");
		}

		System.out.println("\nCase02: 반대로 모두다 일치하지 않으면 건강식으로 간주하는 예제");
		if (menu.stream().noneMatch(d -> d.getCalories() >= 1000)) {
			System.out.println("The menu is healthy food!!");
		}

		/* 5.3.3 요소 검색
		*   : findAny 메서드는 현재 스트림에서 임의의 요소를 반환한다.
		*       : 다른 스트림 연산과 연결해서 사용할 수 있다.
		*       : 스트림 파이프라인은 내부적으로 단일 과정으로 실행할 수 있도록 최적화된다.
		*       : 쇼트 서킷을 이용해서 결과를 찾는 즉시 실행 종료*/
		System.out.println("\n5.3.3 메뉴중에서 채식인 메뉴를 찾으면 종료하고 반환하는 예제");
		Optional<Dish> dish1 = menu.stream()
				.filter(Dish::isVegetarian)
				.findAny();
		System.out.println(dish1);

		/* 추가. Optional 이란 ?
		*   : 값의 존재나 부재 여부를 표현하는 컨테이너 클래스(java.util.Optional)
		*   : 이전 예제에서 findAny 는 아무 요소도 반환하지 않을 수 있다.
		*       : null 은 쉽게 에러를 일으킬 수 있으므로
		*       자바 8라이브러리 설계자는 Optional 이라는 기능을 만들었다.
		*   : 메서드
		*       : isPresent() : Optional 이 값을 포함하면 참을 아니면 거짓을 반환.
		*       : ifPresent(Consumer block) : 값이 있으면 주어진 블록을 실행.
		*       : T get() : 값이 존재하면 값을 반환하고, 없으면 NoSuchElementException 발생.
		*       : T orElse(T other) : 값이 있으면 값을 반환하고, 없으면 기본값을 반환.*/
		System.out.println("\n추가 Optional 적용 예제");
		menu.stream()
				.filter(Dish::isVegetarian)
				.findAny() // Optional<Dish> 반환
				.ifPresent(d -> System.out.println(d.getName())); // 값이 있어야만 출력문 실행.

		/* 5.3.4 첫 번째 요소 찾기
		*   : 리스트 또는 정렬된 연속 데이터로부터 생성된 스트림처럼 일부 스트림에는 논리적인
		*   아이템 순서가 정해져 있을 수 있다. 이런 스트림에서 첫번째 요소를 찾으려면 어떻게 해야 할까>*/
		System.out.println("\n5.3.4 리스트 각요소를 제곱하고 그 중 3의 배수를 찾으면 종료 및 반환");
		List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
		Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream()
				.map(x -> x * x)
				.filter(x -> x % 3 == 0)
				.findFirst();
		System.out.println(firstSquareDivisibleByThree);

		/* 추가. 왜 findFirst 와 findAny 두가지 메서드가 모두 필요할까 ?
		*   : 병렬성 때문.
		*   : 병렬 실행에서는 첫 번째 요소를 찾기 어렵다.
		*   : 요소의 반환 순서가 상관없다면 병렬 스트림에서는 제약이 적은 findAny 를 사용.*/

		/* 5.4 리듀싱
		*   : 리듀싱 연산 : 모든 스트림 요소를 처리해서 값으로 도출
		*       : 함수형 프로그래밍 언어 용어로는 이 과정이 마치 종이(스트림)를
		*       작은 조각이 될 때까지 반복해서 접는 것과 비슷하다는 의미로 폴드(fold)라고 부른다.*/

		/* 5.4.1 요소의 합
		*   : for-each 루프를 이용해서 리스트의 숫자 요소를 더하는 코드
		*       : numbers 의 각 요소는 결과에 반복적으로 더해진다.
		*       : 리스트에서 하나의 숫자가 남을 때까지 이 과정을 반복.
		*       : 코드에는 두 개의 파라미터가 사용되었다.
		*           : sum 변수의 초기값 0
		*           : 리스트의 모든 요소를 조합하는 연산(+) */
		System.out.println("\n5.4.1\nCase01: for-each 루프를 이용해서 리스트의 숫자 요소를 더하는 코드");
		List<Integer> numbers5 = Arrays.asList(4, 5, 3, 9);
		int sum = 0;
		for (int x : numbers5) {
			sum += x;
		}
		System.out.println(sum);

		/* 추가. 위 코드에서 모든 숫자를 곱하는 연산을 추가로 구현하려면 ?
		*   : 같은 코드 복붙 필요.
		*   : reduce 를 이용하면 어플리케이션의 반복된 패턴을 추상화할 수 있다.
		*   : reduce 는 두개의 인수를 갖는다.
		*       : 초기값 0
		*       : 두 요소를 조합해서 새로운 값을 만드는 BinaryOperator,
		*       예제에서는 람다 표현식(a, b) -> a + b 를 사용했다.
		*   : reduce 로 다른 람다를 넘겨주면 곱셈 연산을 적용할 수 있다.
		*       : 스트림이 하나의 값으로 줄어들 때까지 람다는 각 요소를 반복해서 조합한다.
		*       : reduce 가 스트림의 모든 숫자를 더하는 과정
		*           1. 람다의 첫번째 파라미터(a)에 0이 사용된다.
		*           2. 스트림에서 4를 소비해서 두번째 파라미터(b)로 사용.
		*           3. 0 + 4의 결과인 4가 새로운 누적값이 됨.
		*           4. 누적값으로 람다를 다시 호출하며 다음 요소인 5를 소비한다.
		*           5. 결과는 9가 된다.
		*           6. 다음 요소 3을 소비하면서 누적값은 12가 된다.
		*           7. 누적값 12와 스트림의 마지막 요소 9로 람다를 호출하면 최종값으로 21이 도출.
		*       : 메서드 레퍼런스를 이용해서 코드를 좀 더 간결하게 만들 수 있다.
		*       : 자바 8에서는 Integer 클래스에 두 숫자를 더하는 정적 sum 메서드를 제공한다.*/
		System.out.println("\nCase02: reduce 를 이용해서 리스트의 숫자 요소를 더하는 코드");
		int sum1 = numbers5.stream().reduce(0, Integer::sum);
		System.out.println(sum1);

		/* 추가. 초기값을 받지 않도록 오버로드 된 reduce 는 Optional 객체를 반환한다.
		*   : 스트림에 아무 요소도 없다면, 초기값이 없으므로 reduce 는 합계를 반환 할 수 없다.
		*   : 따라서 합계가 없음을 가리킬 수 있도록 Optional 객체로 감싼 결과를 반환한다.*/
		System.out.println("\nCase03: reduce 를 이용해서 리스트의 숫자 요소를 곱하는 코드");
		Optional<Integer> sum2 = numbers5.stream().reduce((a, b) -> a * b);
		System.out.println(sum2);

		/* 5.4.2 최댓값과 최솟값
		*   : reduce 는 두 인수를 받는다.
		*       : 초기값
		*       : 스트림의 두 요소를 합쳐서 하나의 값으로 만드는데 사용할 람다
		*   : 두 요소에서 최대값을 반환하는 람다만 있으면 최대값을 구할 수 있다.
		*   : reduce 연산은 새로운 값을 이용해서 스트림의 모든 요소를 소비할 때까지
		*   람다를 반복 수행하면서 최대값을 생산한다.
		*   : Integer::max 대신 (x,y) -> x<y ? x:y 를 사용해도 무방하지만 레퍼런스가 가독성이 좋다.*/
		System.out.println("\nCase04: reduce 를 이용해서 리스트의 숫자 요소중 최대값을 구하는 코드");
		Optional<Integer> max = numbers5.stream().reduce(Integer::max);
		System.out.println(max);

		System.out.println("\nCase05: reduce 를 이용해서 리스트이 숫자 요소중 최소값을 구하는 코드");
		Optional<Integer> min = numbers5.stream().reduce(Integer::min);
		System.out.println(min);

		/* Quiz 01
		*   : map 과 reduce 메서드를 이용해서 스트림의 요리 개수를 계산하시오.
		*       : 스트림에서 각 요소를 1로 매핑한 다음에 reduce 로 이들의 합계를 계산.
		*           : 스트림에 저장된 숫자를 차례로 더한다.
		*       : map 과 reduce 를 연결하는 기법을 맵 리듀스 패턴이라 한다.
		*           : 쉽게 병렬화하는 특징 덕분에 구글이 웹 검색에 적용하면서 유명해졌다.
		*       : 4장에서 살펴본 count() 메서드로 동일한 결과를 얻을 수 있다.*/
		System.out.println("\nQuiz01 : menu 스트림의 요소 개수를 계산하시오.");
		int count1 = menu.stream()
				.map(d -> 1)
				.reduce(0, Integer::sum); // 또는 (a, b) -> a + b
		System.out.println(count1);

		System.out.println("\n추가. count() 메서드를 사용하여 계산");
		long count2 = menu.stream().count();
		System.out.println(count2);

		/* 5.4.3 reduce 메서드의 장점과 병렬화
		*   : reduce 를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce 를 실행 가능.
		*       : 반복적인 합계에서는 sum 변수를 공유해야 하므로 쉽게 병렬화하기 어렵다.
		*       : 강제적으로 동기화시킨다 해도 결국 병렬화로 얻어야 할 이득이
		*       스레드간의 소모적인 경쟁 때문에 상쇄되어 버린다.
		*   : 병렬화를 위해서는 입력을 분할 하고, 분할된 입력을 더한 다음에, 더한 값을 합쳐야 한다.
		*       : 7장에서 포크/조인 프레임워크를 이용하는 방법을 살펴본다.
		*   : 스트림의 모든 요소를 더하는 코드를 병렬로 만드는 방법
		*       : int sum = numbers.parallelStream().reduce(0, Integer::sum);
		*       : stream() 을 parallelStream() 으로 바꾼다. (7장에서 살펴봄)
		*       : 위 코드를 병렬로 실행하려면 대가를 지불해야 한다.
		*           : reduce 에 넘겨준 람다의 상태(인스턴스 변수 같은)가 바뀌지 말아야 하며,
		*           연산이 어떤 순서로 실행되더라도 결과가 바뀌지 않는 구조여야 한다. */

		/* 5.4.4 스트림연산 : 상태 없음과 상태 있음
		*   : 스트림 연산은 마치 만병통치약 같은 존재다.
		*       : 원하는 모든 연산을 쉽게 구현할 수 있다.
		*       : 컬렉션으로 스트림을 만드는 stream 메서드를 parallelStream 으로
		*       바꾸는 것만으로도 병렬성을 얻을 수 있다.
		*   : 다양하게 응용 가능.
		*       : 요리 리스트를 스트림으로 변환 할 수 있다.
		*       : filter 로 원하는 종류의 요리만 선택할 수 있다.
		*       : map 을 이용해서 칼로리를 추가한 다음에 reduce 로 요리의 총합 칼로리를 계산한다.
		*       : 병렬로 실행할 수 있다.
		*           : 각각의 연산은 다양한 연산을 수행하기 때문에 내부적인 상태를 고려해야 한다.
		*   : map, filter 등은 입력 스트림에서 각 요소를 받아 0 또는 결과를 출력 스트림으로 보낸다.
		*       : 사용자가 제공한 함다나 메서드 레퍼런스가 내부적인 가변 상태를 갖지않는다는
		*       가정 하에 보통 상태가 없는, 즉 내부 상태를 갖지 않는 연산이다.
		*   : reduce, sum, max 같은 연산은 결과를 누적할 내부 상태가 필요하다.
		*       : 스트림에서 처리하는 요소 수와 관계 없이 내부 상태의 크기는 한정(bounded) 되어 있다.
		*           : int 나 double 과 같은 내부 상태 사용.
		*   : sorted 나 distinct 같은 연산은 filter 나 map 과는 달리 스트림의 요소를 정렬하거나
		*   중복을 제거하려면 과거의 이력을 알고 있어야 한다.
		*       : 어떤 요소를 수행하는 데 필요한 저장소 크기는 정해져있지 않다.
		*           : 따라서 데이터 스트림의 크기가 크거나 무한이라면 문제가 생길 수 있다.
		*       : 이러한 연산은 내부 상태를 갖는 연산(stateful operation)으로 간주할 수 있다.*/
	}
}

