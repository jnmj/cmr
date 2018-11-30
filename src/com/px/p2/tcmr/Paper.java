package com.px.p2.tcmr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


class Constants{
	public static int M=6900;
	public static int K=25;
	public static int Q=K;
	public static int r=3;
	public static int P=5;
}

class FileManager{
	int partNum;
	int partSize;
	List<Node> nodeList;
	List<Integer> allFiles = new ArrayList<>();
	Map<List<Integer>, List<Integer>> combine2file = new HashMap<>();
	Map<List<Integer>, List<Integer>> file2combine = new HashMap<>();
	
	
	public void printF2C() {
		System.out.println("\nF2C:");
		for(List<Integer> list : file2combine.keySet()) {
			System.out.println(list+"\t"+file2combine.get(list));
		}
	}
	
	
	public void printC2F() {
		List<List<Integer>> sortedKeys = new ArrayList<>();
		for(List<Integer> list : combine2file.keySet()) {
			sortedKeys.add(list);
		}
		Collections.sort(sortedKeys, new Comparator<List<Integer>>() {
			@Override
			public int compare(List<Integer> o1, List<Integer> o2) {
				for(int i=0;i<o1.size();i++) {
					if(o1.get(i)!=o2.get(i)) return o1.get(i)-o2.get(i);
				}
				return 0;
			}
		});
		System.out.println("\nC2F:");
		for(List<Integer> list : sortedKeys) {
			System.out.println(list+"  "+combine2file.get(list));
		}
	}
	
	
	
	public void fillNodes(List<Node> nodeList, List<List<Integer>> combine) {
		
		for(int i=0;i<combine.size();i++) {
			for(int j:combine.get(i)) {
				nodeList.get(j-1).files.addAll(this.getPart(i+1));
			}
		}
		
		System.out.println("\nnode allFiles:");
		for(int i=0;i<Constants.K;i++) {
			nodeList.get(i).printFiles();
		}
	}
	
	
	
	public FileManager(List<List<Integer>> combine, List<Node> nodeList) {
		this.nodeList=nodeList;
		this.partNum = combine.size();
		partSize = Constants.M/partNum;
		for(int i=0;i<Constants.M;i++) {
			allFiles.add(i+1);
		}
		for(int i=0;i<combine.size();i++) {
			combine2file.put(combine.get(i), allFiles.subList(i*partSize, (i+1)*partSize));
			file2combine.put(allFiles.subList(i*partSize, (i+1)*partSize), combine.get(i));
		}
		
		fillNodes(nodeList, combine);
		
		printC2F();
		printF2C();
	}
	public List<Integer> getPart(int num){
		return allFiles.subList((num-1)*partSize, num*partSize);
	}
}

class UpDownLoad{
	int up1;
	int down1;
	int up2;
	int down2;
	
	@Override
	public String toString() {
		return "UpDownLoad [up1=" + up1 + ", down1=" + down1 + ", up2=" + up2 + ", down2=" + down2 + "]";
	}

	public void add(UpDownLoad other) {
		up1+=other.up1;
		up2+=other.up2;
		down1+=other.down1;
		down2+=other.down2;
	}
}

class Coded{
	int src;
	List<Character> targets = new ArrayList<>();
	List<Integer> files = new ArrayList<>();
	UpDownLoad upDownLoad = new UpDownLoad();
	public void calcUpDownLoad(List<Node> nodeList) {
		int intra=0;
		Set<Integer> cross = new HashSet<>();
		for(int i=0;i<targets.size();i++) {
			if(nodeList.get(targets.get(i)-'A').row==nodeList.get(src-1).row) {
				if(intra==0) intra++;
			}else {
				if(!cross.contains(nodeList.get(targets.get(i)-'A').row)) {
					cross.add(nodeList.get(targets.get(i)-'A').row);
				}
			}
		}
		upDownLoad.up2=1;
		upDownLoad.down2=intra;
		if(!cross.isEmpty()) {
			upDownLoad.up1=1;
			upDownLoad.down1=1;
			upDownLoad.down2+=cross.size();
		}
	}
	
	
	@Override
	public String toString() {
		return "Coded [src=" + src + ", t=" + targets + ", f=" + files + ", \tupDownLoad=" + upDownLoad + "]";
	}



	public Coded() {}
	
	public Coded(int src) {
		this.src = src;
	}
	public void put(char target, int file) {
		targets.add(target);
		files.add(file);
	}
	
}

class Channel{
	int crossLoad;
	int intraLoad;
	List<Coded> codeds = new ArrayList<>();
	public void put(Coded coded) {
		codeds.add(coded);
	}
	
	public List<Coded> getCodeds() {
		return codeds;
	}

	public void addAll(Coded[] array) {
		for(int i=0;i<array.length;i++) {
			codeds.add(array[i]);
		}
	}
	public void addAll(List<Coded> list) {
		codeds.addAll(list);
	}
	
	public void sort() {
		Collections.sort(codeds, new Comparator<Coded>() {
			@Override
			public int compare(Coded o1, Coded o2) {
				return o1.src-o2.src;
			}
		});
	}
	
	public void printCodes(List<Node> nodeList) {
		System.out.println("\nchannel codeds:");
		for(Coded coded: codeds) {
			coded.calcUpDownLoad(nodeList);
			if(coded.upDownLoad.up1==0) {
				intraLoad++;
			}else {
				crossLoad++;
			}
			System.out.println(coded);
		}
		
		System.out.println("\ncrossLoad: "+crossLoad);
		System.out.println("\nintraLoad: "+intraLoad);
	}
	
	public UpDownLoad getLoad() {
		UpDownLoad upDownLoad = new UpDownLoad();
		for(Coded coded: codeds) {
			upDownLoad.add(coded.upDownLoad);
		}
		return upDownLoad;
	}
}

class Node{
	int num;
	int row;
	int col;
	List<Integer> files = new ArrayList<>();
	List<List<VKS>> vkses = new ArrayList<>();
	List<Coded> decodeResult = new ArrayList<>();
	List<Integer> decodeResultNumber = new ArrayList<>();
	public Node(int num) {
		this.num = num;
	}
	
	@Override
	public String toString() {
		return "Node[number=" + num + ", allFiles=" + files + "]";
	}
	
	
	public Coded decode(Coded coded) {
		int cnt=0;
		for(int i=0;i<coded.files.size();i++) {
			if(files.contains(coded.files.get(i))) cnt++;
		}
		if(cnt==coded.files.size()-1) {
			for(int i=0;i<coded.files.size();i++) {
				if(!files.contains(coded.files.get(i))) {
					if(coded.targets.get(i)-'A'+1==num) {
						Coded ret = new Coded();
						ret.files.add(coded.files.get(i));
						ret.targets.add((char) (coded.targets.get(i)-'A'+1));
						return ret;
					}else {
						return null;
					}
				}
			}
		}else {
			return null;
		}
		return null;
	}
	
	public void printFiles() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node[number=" + num + ", allFiles=[");
		for(Integer file : files) {
			builder.append(file).append(", ");
		}
		builder.deleteCharAt(builder.length()-1);
		builder.deleteCharAt(builder.length()-1);
		builder.append("]]");
		System.out.println(builder.toString());
	}
	
	public boolean checkFinish() {
		boolean ret = true;
		for(int i=1;i<=Constants.M;i++) {
			if(files.contains(i)||decodeResultNumber.contains(i)) {
				continue;
			}else {
				ret = false;
				break;
			}
		}
		return ret;
	}
}

class VKS{
	int dst;
	int src;
	int file;
	public VKS(int dst, int src, int file) {
		this.dst = dst;
		this.src = src;
		this.file = file;
	}
}

public class Paper{
	
	public static void run() {
		Solution st = new Solution();
		List<List<Integer>> combine = st.combine(Constants.K, Constants.r);
		List<Node> nodeList = new ArrayList<>();
		for(int i=0;i<Constants.K;i++) {
			nodeList.add(new Node(i+1));
			nodeList.get(i).row = i/(Constants.K/Constants.P);
			nodeList.get(i).col = i%(Constants.K/Constants.P);
		}
		FileManager manager = new FileManager(combine, nodeList);
		Channel channel = new Channel();
		
		List<List<Integer>> shufferCombine = st.combine(Constants.K, Constants.r+1);
		for(int i=0;i<shufferCombine.size();i++) {
			Map<Integer, Integer> count = new HashMap<>();
			for(int y=0;y<shufferCombine.get(i).size();y++) {
				count.put(shufferCombine.get(i).get(y), 0);
			}
			for(int j:shufferCombine.get(i)) {
				List<Integer> other = new ArrayList<>();
				for(int x:shufferCombine.get(i)) {
					if(x!=j) other.add(x);
				}
				for(int z:other) {
					nodeList.get(z-1).vkses.add(new ArrayList<>());
				}
				List<Integer> subFiles = manager.combine2file.get(other);
				if(subFiles==null||subFiles.size()==0) continue;
				for(int l=0;l<subFiles.size();l++) {
					nodeList.get(other.get(l%other.size())-1).vkses.get(count.get(other.get(l%other.size()))).add(new VKS(j, other.get(l%other.size()), subFiles.get(l)));
				}
				for(int z:other) {
					count.put(z, count.get(z)+1);
				}
			}
			
			List<Coded> currentCodeds = new ArrayList<>();
			for(int j:shufferCombine.get(i)) {
				Node node = nodeList.get(j-1);
				int col=0;
				while(true) {
					Coded coded = new Coded(j);
					for(int x=0;x<node.vkses.size();x++) {
						if(col<node.vkses.get(x).size()) {
							coded.put((char) ('A'+node.vkses.get(x).get(col).dst-1), node.vkses.get(x).get(col).file);
						}
					}
					if(coded.files.isEmpty()) break;
					channel.put(coded);
					currentCodeds.add(coded);
					col++;
				}
				node.vkses.clear();
			}
			
			for(int y:shufferCombine.get(i)) {
				for(int j=0;j<currentCodeds.size();j++) {
					if(currentCodeds.get(j).src==nodeList.get(y-1).num) continue;
					Coded decode = nodeList.get(y-1).decode(currentCodeds.get(j));
					if(decode!=null) {
						nodeList.get(y-1).decodeResult.add(decode);
						nodeList.get(y-1).decodeResultNumber.add(decode.files.get(0));
					}
				}
			}
		}
		//channel.sort();
		channel.printCodes(nodeList);
		UpDownLoad load = channel.getLoad();
		System.out.println("\n"+load);
		
		boolean finish = true;
		for(int i=0;i<nodeList.size();i++) {
			if(nodeList.get(i).checkFinish()) {
				continue;
			}else {
				finish = false;
				break;
			}
		}
		System.out.println(finish?"完成":"未完成");
		
		
	}
	
	public static void main(String[] args) {
		run();
		Solution st = new Solution();
		int cl = st.calcCrossLoadByFormula(Constants.K, Constants.P, Constants.Q, Constants.M, Constants.r);
		int il = st.calcIntraLoadByFormula(Constants.K, Constants.P, Constants.Q, Constants.M, Constants.r);
		System.out.println("\n公式计算crossLoad: "+cl);
		System.out.println("\n公式计算intraLoad: "+il);
	}      
	
}
