package com.px.cmr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class ListNode {
	      int val;
	      ListNode next;
	      ListNode(int x) { val = x; }
	  }
class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
 }
public class Solution {
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
	public boolean containsNearbyDuplicate(int[] nums, int k) {
        if(nums==null||nums.length==0) return false;
		Set<Integer> set = new HashSet<Integer>();
        for(int i=0;i<nums.length;i++){
        	if(set.contains(nums[i])){
        		return true;
        	}
        	if(i>=k) {
        		set.remove(nums[i-k]);
        	}
        	set.add(nums[i]);
        }
		return false;
    }
	public static ListNode reverseList(ListNode head) {
        if(head==null||head.next==null){
        	return head;
        }
        ListNode pre=head;
        ListNode cur = head.next;
        ListNode tmp;
        while(cur!=null){
        	tmp = cur.next;
        	cur.next=pre;
        	pre = cur;
        	cur = tmp;
        }
        return pre;
    }
	
	public int maxProduct(int[] nums) {
        if(nums==null||nums.length==0){
        	return 0;
        }
        int maxMul = 1;
        int minMul = 1;
        int ret = Integer.MIN_VALUE;
        for(int i=0;i<nums.length;i++){
        	int a = maxMul*nums[i];
        	int b = minMul*nums[i];
        	maxMul = Math.max(Math.max(a, b),nums[i]);
        	minMul = Math.min(Math.min(a, b),nums[i]);
        	ret = Math.max(ret, maxMul);
        }
		return ret;
    }
	
	public int[] searchRange(int[] nums, int target) {
        int[] ret = new int[2];
        int i=0,j=nums.length-1;
        int mid;
        while(i<=j) {
        	mid=(i+j)/2;
        	if(target>nums[mid]) {
        		i=mid+1;
        	}else {
        		j=mid-1;
        	}
        }
        ret[0] = j+1;
        i=0;
        j=nums.length-1;
        while(i<=j) {
        	mid=(i+j)/2;
        	if(target<nums[mid]) {
        		j=mid-1;
        	}else {
        		i=mid+1;
        	}
        }
        ret[1] = i-1;
        if(ret[0]>ret[1]) {
        	ret[0]=-1;
        	ret[1]=-1;
        }
        return ret;
    }	
	
public static String multiply(String num1, String num2) {
		int[] d = new int[num1.length()+num2.length()];
		int a,b;
		for(int i=num1.length()-1;i>=0;i--) {
			a = num1.charAt(i)-'0';
			for(int j=num2.length()-1;j>=0;j--) {
				b = num2.charAt(j)-'0';
				d[i+j+1]+=a*b;
			}
		}
		int r=0,p=0;
		for(int i=d.length-1;i>=0;i--) {
			r=(p+d[i])%10;
			p=(p+d[i])/10;
			d[i]=r;
		}
        StringBuilder builder = new StringBuilder();
        
        int k=0;
        for(;k<d.length;k++) {
        	if(d[k]!=0) break;
        }
        if(k==d.length) return new String("0");
        for(int i=k;i<d.length;i++) {
        	builder.append(d[i]);
        }
        return builder.toString();
    }
	public static void main(String[] args) {
		ListNode head= new ListNode(1);
		head.next=new ListNode(2);
		reverseList(head);
	}
}

