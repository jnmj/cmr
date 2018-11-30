package com.px.p2.tcmr;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	public int getFactorial(int n) {
		if(n==0) return 1;
		int ret = 1;
		for(int i=1;i<=n;i++) {
			ret*=i;
		}
		return ret;
	}
	
	public int getCombineNum(int n, int k) {
		if(k==0||k==n) return 1;
		int a = 1;
		for(int i=0;i<k;i++) {
			a*=n-i;
		}
		return a/getFactorial(k);
	}
	
	public int calcCrossLoadByFormula(int _K, int _P, int _Q, int _M, int _r) {
		double K = _K;
		double P = _P;
		double Q = _Q;
		double M = _M;
		double r = _r;
		
		return (int) (Q*M/r*(1-r/K)*(1-getCombineNum(_K/_P, _r+1)*P/getCombineNum(_K, _r+1)));
	}
	
	public int calcIntraLoadByFormula(int _K, int _P, int _Q, int _M, int _r) {
		double K = _K;
		double P = _P;
		double Q = _Q;
		double M = _M;
		double r = _r;
		
		return (int) (Q*M/r*(1-r/K)*getCombineNum(_K/_P, _r+1)*P/getCombineNum(_K, _r+1));
	}
	
	public List<List<Integer>> combine(int n, int k) {
		List<List<Integer>> ret = new ArrayList<List<Integer>>();
        int[] nums = new int[n];
        for(int i=0;i<n;i++){
        	nums[i]=i+1;
        }
		getResult(ret, new ArrayList<Integer>(), nums, 0, k);
		return ret;
    }
	
	private void getResult(List<List<Integer>> ret, List<Integer> cur, int[] nums, int start ,int k){
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
	}
}

