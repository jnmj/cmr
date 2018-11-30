package com.px.p2.hcmr;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	public double calcIntraTotalLoad(int _K, int _P, int _Q, int _N, int _r) {
		double K = _K;
		double P = _P;
		double Q = _Q;
		double N = _N;
		double r = _r;
		return Q*N*(3+1/r-1/P-r/P-2*P/K);
	}
	
	public List<List<Integer>> layerCombine(int n, int rack){
		int layer = n/rack;
		List<List<Integer>> ret = new ArrayList<>();
		for(int i=1;i<=layer;i++) {
			List<Integer> part = new ArrayList<>();
			for(int j=0;j<rack;j++) {
				part.add(i+j*layer);
			}
			ret.add(part);
		}
		return ret;
	}
	
	public List<List<Integer>> combine(List<Integer> src, int k){
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
		Integer[] nums = new Integer[src.size()];
		src.toArray(nums);
		getResult(ret, new ArrayList<Integer>(), nums, 0, k);
		return ret;
	}
	
	public List<List<Integer>> combine(int n, int k) {
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
		Integer[] nums = new Integer[n];
        for(int i=0;i<n;i++){
        	nums[i]=i+1;
        }
		getResult(ret, new ArrayList<Integer>(), nums, 0, k);
		return ret;
    }
	
	private void getResult(List<List<Integer>> ret, List<Integer> cur, Integer[] nums, int start ,int k){
		if(cur.size()<k){
			for(int i=start;i<nums.length;i++){
				cur.add(nums[i]);
				getResult(ret, cur, nums, i+1, k);
				cur.remove(cur.size()-1);
			}
		}else{
			ret.add(new ArrayList<Integer>(cur));
		}
		
	}
	
	public static void main(String[] args) {
		Solution st = new Solution();
		List<List<Integer>> ret = st.layerCombine(9, 3);
		System.out.println(ret);
	}
}

