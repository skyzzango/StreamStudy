package test;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Quiz02 {
	public static void main(String[] args) {
		int[] result = new int[] {4, 5};
		int[][] arr = new int[][]{
				{1, 1, 1, 0},
				{1, 2, 2, 0},
				{1, 0, 0, 1},
				{0, 0, 0, 1},
				{0, 0, 0, 3},
				{0, 0, 0, 3}
		};

		Solution2.solution(6, 4, arr);
	}
}

class Solution2 {

	public static int[] solution(int m, int n, int[][] picture) {
		int zz = 0;
		int numberOfArea = 0;
		int maxSizeOfOneArea = 0;
		int[][] attrPicture = picture;
		boolean[][] check = new boolean[picture.length][picture[0].length];





		int[] answer = new int[2];
		answer[0] = numberOfArea;
		answer[1] = maxSizeOfOneArea;
		return answer;
	}

}

class Solution1 {
	private static boolean check[][];
	private static int dx[] = {-1, 0, 1, 0};
	private static int dy[] = {0, 1, 0, -1};
	private static int M;
	private static int N;

	class Pair {
		int x;
		int y;

		Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	private int pill(int i, int j, int[][] picture) {
		Queue<Pair> q = new LinkedList<>();
		q.add(new Pair(i, j));
		check[i][j] = true;
		int sum = 1;
		while (!q.isEmpty()) {
			Pair p = q.remove();
			int nextX, nextY;
			for (int c = 0; c < 4; c++) {
				nextX = p.x + dx[c];
				nextY = p.y + dy[c];
				if (nextX >= 0 && nextY >= 0 && nextX < M && nextY < N && !check[nextX][nextY]
						&& picture[p.x][p.y] == picture[nextX][nextY]) {
					check[nextX][nextY] = true;
					q.add(new Pair(nextX, nextY));
					sum++;
				}
			}
		}
		return sum;
	}

	public int[] solution(int m, int n, int[][] picture) {
		int numberOfArea = 0;
		int maxSizeOfOneArea = 0;
		M = m;
		N = n;
		Map<Integer, Integer> map = new HashMap<>();
		check = new boolean[M][N];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				if (!check[i][j] && picture[i][j] != 0) {
					int val = picture[i][j];
					map.putIfAbsent(val, 0);
					map.put(val, Math.max(map.get(val), pill(i, j, picture)));
					numberOfArea++;
				}
			}
		}
		for (Integer key : map.keySet()) {
			maxSizeOfOneArea = Math.max(maxSizeOfOneArea, map.get(key));
		}
		int[] answer = new int[2];
		answer[0] = numberOfArea;
		answer[1] = maxSizeOfOneArea;
		return answer;
	}
}


