package com.px.tcmr;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Test {

	public static void main(String[] args) {
		for(int i=0;i<6;i++) {
			
			System.out.println(new Random().nextInt(5));
		}
		Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);
		map.get(1);
		System.out.println();
	}
}
