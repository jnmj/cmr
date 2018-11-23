package com.px.rdhcmr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.naming.ldap.SortControl;

import org.omg.PortableInterceptor.DISCARDING;

public class Solution {
	
	public double calcIntraTotalLoadGain(int _K, int _P, int _Q, int _N, int _r) {
		double K = _K;
		double P = _P;
		double Q = _Q;
		double N = _N;
		double r = _r;
		return Q*N*(3-r+r*r/P-3*r/P+2*r/K-2*P/K);
	}
	public double calcIntraTotalLoadGain2(int _K, int _P, int _Q, int _N, int _r) {
		double K = _K;
		double P = _P;
		double Q = _Q;
		double N = _N;
		double r = _r;
		return Q*N*(P-2)*(P-2)/P/P;
	}
	
	public double calcIntraTotalLoad(int _K, int _P, int _Q, int _N, int _r) {
		double K = _K;
		double P = _P;
		double Q = _Q;
		double N = _N;
		double r = _r;
		return Q*N/r*(1-r/P)*(1+r*r)+2*Q*N*r*(1/P-1/K);
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

