package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Quiz4673 {
	public static void main(String[] args) throws IOException {
		int n = 10000;
/*
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
*/
		List<Integer> generated = generateNumbers(n);
		System.out.println(generated);

		List<Integer> list = IntStream.range(0, n)
				.boxed()
				.collect(Collectors.toList());
		list.removeAll(generated);
		list.forEach(System.out::println);

	}

	private static List<Integer> generateNumbers(int n) {
		return IntStream.range(0, n)
				.map(i -> {
					int sum = i;
					while (i > 0) {
						sum += (i % 10);
						i /= 10;
					}
					return sum;
				})
				.boxed()
				.collect(Collectors.toList());
	}
}
