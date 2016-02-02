package algorithms;

import java.util.*;
public class RandomArray {
	public RandomArray(int n) {
		int[] array = new int[n];
		for(int a = 0; a < array.length; a++) {
			array[a] = (int) Math.random();
			System.out.println( Math.random());
		}
		
		System.out.println(Arrays.toString(array));
	}
}
