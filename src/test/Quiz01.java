package test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Quiz01 {

	public static void main(String[] args) {
		System.out.println("\nQuiz: 피타고라스 수");
		Stream<int[]> pythagoreanTriples =
				IntStream.rangeClosed(1, 100)
						.boxed()
						.flatMap(a ->
								IntStream.rangeClosed(a, 100)
										.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
										.mapToObj(b ->
												new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));
		pythagoreanTriples.forEach(t -> System.out.println(Arrays.toString(t)));
	}

}

class Solution {
	public String solution(String[] participant, String[] completion) {

		List<String> par = Arrays.asList(participant);
		List<String> com = Arrays.asList(completion);

		List<Member> members = par.stream()
				.map(Member::new)
				.collect(Collectors.toList());


		Map<String, Boolean> runners =
				par.stream()
				.collect(HashMap::new, (m, v) -> m.put(v, false), HashMap::putAll);
		System.out.println(runners);


		String answer = "";
		return answer;
	}

	class Member {
		String name;
		boolean check;

		Member (String name) {
			this.name = name;
			this.check = false;
		}

		String getName() {
			return this.name;
		}

		boolean getCheck() {
			return this.check;
		}

		void setName(String name) {
			this.name = name;
		}

		void setCheck(boolean check) {
			this.check = check;
		}
	}
}