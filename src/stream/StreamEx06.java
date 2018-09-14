package stream;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
		Map<Currency, List<Transaction>> transactionByCurrencies = new HashMap<>();
		for (Transaction transaction : transactions) {
			Currency currency = transaction.getCurrency(); // 트랜잭션의 통화 추출.
			List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);

			// 현재 통화를 그룹화하는 맵에 항목이 없으면 항목을 만든다.
			if (transactionsForCurrency == null) {
				taransactionsForCurrency = new ArrayList<>();
				transactionsByCurrencies.put(currency, transactionsForCurrency);
			}

			// 같은 통화를 가진 트랜잭션 리스트에 현재 탐색 중인 트랜잭션 추가.
			transactionsForCurrency.add(transaction);
		}

		/* 간단한 작업임에도 코드가 너무 길다.
		 *   코드가 무억을 실행하는 지 한눈에 파악하기 어렵다.
		 *   하지만 스트림을 사용하면 간경하게 표현 가능하다. */
		Map<Currency, List<Transaction>> transactionsByCurrencies =
				transactions.stream().collect(groupingBy(Transaction::getCurrency));

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

		/* 6.2.2 요약 연산
		*   - Collectors 클래스는 Collectors.summingInt 라는 특별한 요약 팩토리 메서드를 제공한다.
		*   - summingInt 는 객체를 int 로 매핑하는 함수를 인수로 받는다.
		*   - summingInt 의 인수로 전달된 함수는 객체를 int 로 매핑한 컬렉터를 반환한다.
		*   - summingInt 가 collect 메서드로 전달되면 요약 작업을 수행한다.*/

		// Ex04: 컬렉터 누적
		int totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
	}

}
