package stream;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamEx06 {

	public static void main(String[] args) {
		/* 6. 컬렉터
		 *   - 자바 8의 스트림이란 데이터 집합을 멋지게 처리하는 게으른 반복자라고 생각할 수 있다.
		 *   - 스트림 연산은 filter 또는 map 과 같은 중간 연산과
		 *      count, findFirst, forEach, reduce 등의 최종 연산으로 구분할 수 있다.
		 *       # 중간 연산 : 한 스트림을 다른 스트림으로 변환하는 연산
		 *           * 여러 연산을 연결할 수 있다.
		 *           * 스트림 파이프라인을 구성하며, 스트림의 요소를 소비하지 않는다.
		 *       # 최종 연산 : 스트림의 요소를 소비해서 최종 결과를 도출한다.
		 *           * 스트림 파이프라인을 최적화하면서 계산 과정을 짧게 생략하기도 한다.
		 *   - Collection, Collector, collect 를 헷갈리지 않도록 주의 !!
		 *   - collect 와 Collector 로 구현할 수 있는 질의 예제
		 *       # 통화별로 트랜잭션을 그룹화한 다음에 해당 통화로 일어난 모든 트랜잭션 합계를 계산하시오.
		 *           * Map<currency, Integer> 반환
		 *       # 트랜잭션을 비싼 트랜잭션과 저렴한 트랜잭션 두 그룹으로 분류하시오.
		 *           * Map<Boolean, List<Transaction>> 반환
		 *       # 트랜잭션을 도시 등의 다수준으로 그룹화 하시오.
		 *           그리고 각 트랜잭션이 비싼지 저렴한지 구분하시오.
		 *           * Map<String, Map<Boolean, List<Transaction>> 반환 */

		/* 통화별로 트랜잭션을 그룹화한 코드(명령형 버전) */
		// 그룹화한 트랜젹션을 저장할 맵을 생성.
		List<Transaction> transactions = Transaction.createTransaction();
		Map<Currency, List<Transaction>> transactionByCurrencies = new HashMap<>();
		for (Transaction transaction : transactions) {
			Currency currency = transaction.getCurrency(); // 트랜잭션의 통화 추출.

			// 현재 통화를 그룹화하는 맵에 항목이 없으면 항목을 만들고,
			// 항목이 있으면 같은 통화를 가진 트랜잭션 리스트에 현재 탐색 중인 트랜잭션 추가.
			transactionByCurrencies.computeIfAbsent(
					currency, transactionForCurrency -> new ArrayList<>())
					.add(transaction);
		}

		/* 간단한 작업임에도 코드가 너무 길다.
		 *   코드가 무억을 실행하는 지 한눈에 파악하기 어렵다.
		 *   하지만 스트림을 사용하면 간경하게 표현 가능하다. */
		Map<Currency, List<Transaction>> transactionsByCurrencies =
				transactions.stream().collect(Collectors.groupingBy(Transaction::getCurrency));

		/* 6.1 컬렉터란 무엇인가?
		 *   - 함수형 프로그래밍에서는 '무엇'을 원하는지 직접 명시할 수 있어서
		 *      어떤 방법으로 이를 얻을지는 신경 쓸 필요 없다.
		 *   - Collector 인터페이스 구현은 스트림의 요소를 어떤 식으로 도출할지 지정한다.
		 *       # 5장에서는 toList 를 Collector 인터페이스의 구현으로 사용.
		 *       # 6장에서는 groupingBy 를 이용해서 '각 키(통화) 버킷(bucket)
		 *          그리고 각 키 버킷에 대응하는 요소 리스트를 값으로 포함하는 맵을 만들라'는 동작을 수행
		 *   - 다수준(multilevel)으로 그룹화를 수행할 때 명령형 프로그래밍과
		 *      함수형 프로그래밍의 차이점이 더욱 두드러진다.
		 *       # 명령형 코드에서는 문제를 해결하는 과정에서 다중 루프와 조건문을 추가하여
		 *          가독성과 유지보수성이 크게 떨어진다.
		 *       # 함수형 프로그래밍에서는 필요한 컬렉터를 쉽게 추가할 수 있다.(6.3절 참조) */

		/* 6.1.1 고급 리듀싱 기능을 수행하는 컬렉터
		 *   - 함수형 API 의 또 다른 장점으로 높은 수준의 조합성과 재사용성을 꼽을 수 있다.
		 *       # collect 로 결과를 수집하는 과정을 간단하면서도 유연한 방식으로 정의할 수 있다는 점이 컬렉터의 강점!
		 *           * 스트림에 collect 를 호출하면 스트림의 요소에(컬렉터로 파라미터화된) 리듀싱 연산이 수행된다.
		 *       # 통화별로 트랜잭션을 그룹화하는 리듀싱 연산
		 *           * 명령형 프로그래밍에서 우리가 직접 구현해야했던 작업이 자동으로 수행된다.
		 *           * collect 에서는 리듀싱 연산을 이용해서 스트림의 각 요소를 방문하면서 컬렉터가 작업을 처리한다.
		 *           * 보통 함수를 요소로 변환(toList 처럼 데이터 자체를 변환하는 것보다는 데이터 저장구조를 변환할 때가 많다)
		 *               할 때는 컬렉터를 적용하며 최종 결과를 저장하는 자료구조에 값을 누적한다.
		 *               . 통화 트랜잭션 그룹화 예제에서 변환함수는 각 트랜젝션에서 통화를 추출한 다음에
		 *                   통화를 키로 사용해서 트랜잭션 자체를 결과 맵에 누적했다.
		 *   - Collector 인터페이스의 메서드를 어떻게 구현하느냐에 따라 스트림에 어떤 리듀싱 연산을 수행할지 결정된다.
		 *       # Collectors 유틸리티 클래스는 자주 사용하는 컬렉터 인스턴스를
		 *           손쉽게 생성할 수 있는 정적 팩토리 메서드를 제공.(대표적인 예 - toList) */
		// List<Transaction> transactions = transactionStream.collect(Collectors.toList());

		/* 6.1.2 미리 정의된 컬렉터
		 *   - Collectors 에서 제공하는 메서드의기능
		 *       # 스트림 요소를 하나의 값으로 리듀스하고 요약
		 *       # 요소 그룹화
		 *       # 요소 분할 */

		/* 6.2 리듀싱과 요약
		 *   - 컬렉터(Stream.collect 메서드의 인수)로 스트림의 항목을 컬렉션으로 재구성 가능
		 *   - 컬렉터로 스트림의 모든 항목을 하나의 결과로 합칠 수 있다. */
		// Ex01: counting() 이라는 팩토리 메서드가 반환하는 컬렉터로 메뉴에서 요리수를 계산.
		List<Dish> menu = Dish.createMenu();
		long howManyDishes = menu.stream().collect(Collectors.counting());
		System.out.println("\nEx01: counting() 메서드 사용\n" + howManyDishes);

		// Ex02: 불필요한 과정 생략가능(counting 컬렉터는 다른 컬렉터와 함께 사용할 때 위력을 발휘한다.)
		howManyDishes = menu.stream().count();
		System.out.println("\nEx02: count() 메서드 사용\n" + howManyDishes);

		/* 6.2.1 스트림값에서 최댓값과 최솟값 검색
		 *   - Collectors.maxBy
		 *   - Collectors.minBy
		 *   - 스트림에 있는 객체의 숫자 필드의 합계나 평균등을 반환하는 연산에도 리듀싱 기능이 자주 사용된다.
		 *       이러한 연산을 요약(summarization) 연산이라 부른다.*/

		// Ex03: 두 컬렉터는 스트림의 요소를 비교하는 데 사용할 Comparator 를 인수로 받는다.
		/* 추가. 칼로리로 요리를 비교하는 Comparator 를 구현한 다음에 Collectors.maxBy 로 전달.
		 *   Optional 은 값을 포함하거나 포함하지 않을 수 있는 컨테이너 (자바8) */
		Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
		Optional<Dish> mostCalorieDish = menu.stream().collect(Collectors.maxBy(dishCaloriesComparator));
		System.out.println("\nEx03: Collectors.maxBy 를 사용하여 최대값 계산\n" + mostCalorieDish);

		/* 6.2.2 요약 연산
		 *   - Collectors 클래스는 Collectors.summingInt 라는 특별한 요약 팩토리 메서드를 제공한다.
		 *   - summingInt 는 객체를 int 로 매핑하는 함수를 인수로 받는다.
		 *   - summingInt 의 인수로 전달된 함수는 객체를 int 로 매핑한 컬렉터를 반환한다.
		 *   - summingInt 가 collect 메서드로 전달되면 요약 작업을 수행한다.*/

		// Ex04: 합계 계산 - 메뉴의 총 칼로리 계산
		int totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
		System.out.println("\nEx04: Collectors.summingInt 를 사용하여 합계 계산\n" + totalCalories);

		/*
		 *   - summingInt 컬렉터의 누적 과정
		 *       # 칼로리로 매핑된 각 요리의 값을 탐색하면서 초깃값(여기서는 0)으로 설정되어 있는 누적자에 칼로리를 더한다.
		 *       # 다른 타입들도 존재
		 *           * summingLong
		 *           * summingDouble
		 *       # 다른 형식들도 존재
		 *           * averagingInt
		 *           * averagingLong
		 *           * averagingDouble
		 *   - 두개 이상의 연산을 한 번에 수행해야 할 경우 */

		// Ex05: 하나의 요약 연산(팩토리 메서드 summarizingInt 사용)
		IntSummaryStatistics menuStatistics =
				menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
		System.out.println("\nEx05: Collectors.summarizingInt 를 사용하여 하나의 요약 계산\n" + menuStatistics);

		/* 6.2.3 문자열 연결
		 *   - 컬렉터에 joining 팩토리 메서드를 이용하면 스트림의 각 객체에 toString 메서드를 호출해서
		 *       추출한 모든 문자열을 하나의 문자열로 연결해서 반환한다.*/

		// Ex06: joining 메서드는 내부적으로 StringBuilder 를 이용해 문자열을 하나로 만든다.
		String shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining());
		System.out.println("\nEx06: Collectors.joining 를 사용하여 하나의 문자열로 만듬.\n" + shortMenu);

		// Ex07: joining 메서드는 연결된 두 요소 사이에 구분자 넣을 수 있음.
		shortMenu = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));
		System.out.println("\nEx07: Collectors.joining 를 사용하여 구분자를 넣으며 하나의 문자열로 만듬.\n" + shortMenu);

		/* 6.2.4 범용 리듀싱 요약 연산
		 *   - 지금까지 살펴본 모든 컬렉터는 reducing 팩토리 메서드로도 정의할 수 있다.
		 *   - 범용 팩토리 메서드 대신 특화된 컬렉터를 사용한 이유는 프로그래밍적 편의성 때문이다.
		 *       # 편의성 뿐만 아니라 가독성도 중요하가는 사실을 명심!!
		 *   - reducing 메서드로 만들어진 컬렉터로도 메뉴의 모든 칼로리 합계를 계산할 수 있다.*/

		// Ex08: 모든 칼로리 합계 구하기
		totalCalories = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));
		/*  - 첫 번째 인수는 리듀싱 연산의 시작값이거나 스트림에 인수가 없을 때는 반환 값.
		 *   - 두 번째 인수는 요리를 칼로리 정수로 변환할 때 사용한 변환 함수.
		 *   - 세 번째 인수는 같은 종류의 두 항목을 하나의 값으로 더하는 BinaryOperator.*/

		// Ex09: 한개의 인수를 가진 reducing 버전을 이용해서 가장 칼로리가 높은 요리를 찾는 방법도 있다.
		mostCalorieDish = menu.stream().collect(Collectors.reducing(
				(d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2
		));
		System.out.println("\nEx09: reducing 버전을 이용해서 가장 칼로르가 높은 요리 검색\n" + mostCalorieDish);
		/*  - 스트림의 첫 번째 요소를 첫 번째 인수로 받으며,
		*       자신을 그대로 반환하는 항등함수를 두번째 인수로 받는 상황에 해당한다.
		*   - 시작 값이 없으므로 빈 스트림이 넘겨졌을 때 시작값이 설정되지 않는 상황이 벌어진다.
		*       # 그래서 Optional 객체를 반환한다.*/

		/* 6.2.5 collect 와 reduce */
		// Ex10: reduce 메서드를 누적자로 사용된 리스트를 변환 시키는 잘못 사용한 예제
		Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
		List<Integer> numbers = stream.reduce(
				new ArrayList<>(),
				(List<Integer> l, Integer e) -> {
					l.add(e);
					return l;
				}, (List<Integer> l1, List<Integer> l2) -> {
					l1.addAll(l2);
					return l1;
				});
		System.out.println("\nEX10: 누적자로 사용돈 리스를 변환 시키는 잘못된 예제\n" + numbers);
		/*  - collect 는 도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된 메서드.
		*   - reduce 는 두 값을 하나로 도출하는 불변형 연산.
		*   - 위 예제에서는 reduce 메서드를 누적자로 사용된 리스트를 변환시키므로
		*       같은 결과를 반환 하더라도 잘못 사용한 예에 해당한다.
		*       # 여러 스레드가 동시에 같은 데이터 구조체를 고치면 리스트 자체가 망갖버리므로
		*           리듀싱 연산을 병렬로 수행할 수 없다.
		*       # 이 문제를 해결하려고 매번 새로운 리스트를 할당한다면 성능이 저하.
		*   - 가변 컨테이너 관련 작업이면서 병렬성을 확보하려면 collect 메서드로 리듕싱 연산을 구현하는 것이 바람직하다.*/

		/* 6.2.6 컨렉션 프레임워크 유연성 : 같은 연산도 다양한 방식으로 수행할 수 있다.
		*   - 1. reducing 컬렉터를 사용한 이전 예제에서 람다 표현식 대신
		*       Integer 클래스의 sum 메서드 레퍼런스를 이용하면 코드를 좀 더 단순화할 수 있다.*/
		totalCalories = menu.stream().collect(Collectors.reducing(0, // 초깃값
				Dish::getCalories, // 변환 함수
				Integer::sum)); // 합계 함수
		System.out.println("\nEx11: 레퍼런스를 이용하여 간결하게 코드 작성\n" + totalCalories);

		/*      1. 메뉴의 모든 칼로리를 더하는 리듀싱 과정
		*           1. 누적자를 초깃값으로 초기화하고, 합계 함수를 이용해서
		*               각 요소를 변환 함수를 적용한 결과 숫자를 반복적으로 조합한다.
		*       2. 6.2절에서 언급했던 counting 컬렉터도 reducing 팩토리 메서드를 이용해서 구현할 수 있다.
		*           1. 제네릭 와일드카드 '?' 사용법
		*               1. 아래 counting 메서드 예제에서 count 팩토리 메서드가 반환하는 컬렉터 시그니처의
		*                   두번째 제네릭 형식으로 와일드 카드 '?' 사용.
		*               2. '?' 는 컬렉터의 누적자 형식이 알려지지 않았음을, 즉 누적자의 형식이 자유로움을 의미한다.
		*   2. 5장에서 컬렉터를 이용하지 않고 같은 연산을 수행했던 방법*/
		totalCalories = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
		System.out.println("\nEx12: 컬렉터를 이용하지 않고 모든 요리의 칼로리를 더하는 예제\n" + totalCalories);
		/*      1. 요리 스트림을 요리의 칼로리로 매핑한 다음 메서드 레퍼런스로 결과 스트림을 리듀싱
		*           1. reduce(Integer::sum)도 빈 스트림과 관련한 널 문제를 피할 수 있도록 int 가 아닌 Optional 를 반환한다.
		*           2. 요리 스트림이 비어있지 않다는 사실을 알고 있으므로 get 을 자유롭게 사용할 수 있지만
		*               일반적으로는 기본값을 제공할 수 있는 orElse, orElseGet 등을 이용하는 것이 좋다.
		*   3. 스트림을 IntStream 으로 매핑한 다음에 sum 메서드를 호출하는 방법*/
		totalCalories = menu.stream().mapToInt(Dish::getCalories).sum();
		System.out.println("\nEx13: IntStream 으로 매핑한 다음에 같은 연산을 수행하는 예제\n" + totalCalories);

		/* 6.2.7 자신의 상황에 맞는 최적의 해법 선택
		*   : 스트림 인터페이스에서 직접 제공하는 메서드를 이용하는 것에 비해 컬렉터를 이용하는 코드가 더 복잡하다.
		*   : 코드가 좀 더 복잡한 대신 재사용성과 커스터마이즈 가능성을 제공하는 높은 수준의 추상화와
		*       일반화를 얻을 수 있다.
		*   : 위 예제들 중에서는 IntStream 을 사용한 방법이 가독성이 가장 좋고 간결한다.
		*       # IntStream 덕분에 auto-unboxing 연산을 수행하거나 Integer 를
		*           int 로 변환하는 과정을 피할 수 있으므로 성능까지 좋다.*/

		/* 6.2.8 Quiz
		*   : 아래 joining 컬렉터를 6.2.4절에서 사용한 reducing 컬렉터로 올바르게 바꾼 코드를 모두 선택하시오.
		*       # 문제 : String shortMenu = menu.stream().map(Dish::getName).collect(joining());
		*           1. String shortMenu = menu.stream()
		*                   .map(Dish::getName)
		*                   .collect(reducing((s1, s2) -> s1 + s2))
		*                   .get()
		*           2. String shortMenu = menu.stream()
		*                   .map(Dish::getName)
		*                   .collect(reducing((d1, d2) -> d1.getName() + d2.getName())
		*                   .get()
		*           3. String shortMenu = menu.stream()
		*                   .map(Dish::getName)
		*                   .collect(reducing("", Dish::getName, (s1, s2) -> s1 + s2))
		*                   .get()
		*       # 답 : 1, 3번이 정답, 2번은 컴파일 되지 않는 코드.
		*           1. 원래의 joining 컬렉터처럼 각 요리를 요리명으로 변환한 다음에
		*               문자열을 누적자로 사용해서 문자열 스트림을 리듀스하면서 요리명을 하나씩 연결한다.
		*           2. reducing 은 BinaryOperator, 즉 BiFunction 를 인수로 받는다.
		*               * 두 인수를 받아 같은 형식을 반환하는 함수
		*               * 2번 람다 표현식은 두 개의 요리를 인수로 받아 문자열을 반환하므로 컴파일 오류.
		*           3. 빈 문자열을 포함하는 누적자를 이용해서 리듀싱 과정을 시작하며,
		*               스트림의 요리를 방문하면서 각 요리를 요리명으로 반환한 다음에 누적자로 추가한다.
		*               * 세개의 인수를 갖는 reducing 은 누적자 초깃값을 설정할 수 있으므로
		*                   Optional 을 반환할 필요가 없다.
		*       # 참고
		*           * 1, 3번이 올바른 코드이기는 하지만 범용 reducing 으로 joining 을 구현할 수 있음을 보여주는 예제일 뿐이다.
		*           * 실무에서는 joining 을 사용하는 것이 가독성과 성능에 좋다. */

		/* 6.3 그룹화*/

	}

	public static <T> Collector<T, ?, Long> counting() {
		return Collectors.reducing(0L, e -> 1L, Long::sum);
	}

}
