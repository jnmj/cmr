package com.px.rdhcmr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


class Constants{  
	public static int M=900;
	public static int K=100;
	public static int Q=K;
	public static int r=2;
	public static int P=10;
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
		
	
	public FileManager(List<Node> nodeList) {
		this.nodeList=nodeList;
		for(int i=0;i<Constants.M;i++) {
			allFiles.add(i+1);
		}
	}
	
	public void assignSingleLayer(List<Integer> nodes, List<Integer> files) {
		Solution st = new Solution();
		List<List<Integer>> singleLayerCombine = st.combine(nodes, Constants.r);
		int partSize = files.size()/singleLayerCombine.size();
		for(int i=0;i<singleLayerCombine.size();i++) {
			combine2file.put(singleLayerCombine.get(i), files.subList(i*partSize, (i+1)*partSize));
			file2combine.put(files.subList(i*partSize, (i+1)*partSize), singleLayerCombine.get(i));
			for(int j:singleLayerCombine.get(i)) {
				nodeList.get(j-1).files.addAll(files.subList(i*partSize, (i+1)*partSize));
			}
		}
		
	}
	
	public void assign() {
		Solution st = new Solution();
		List<List<Integer>> layerCombine = st.layerCombine(Constants.K, Constants.P);
		for(int layer=0;layer<layerCombine.size();layer++) {
			assignSingleLayer(layerCombine.get(layer), allFiles.subList(layer*Constants.M*Constants.P/Constants.K, (layer+1)*Constants.M*Constants.P/Constants.K));
		}
		
		printC2F();
		printF2C();
		System.out.println();
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
	List<Integer> dsts = new ArrayList<>();
	List<Character> targets = new ArrayList<>();
	List<Integer> files = new ArrayList<>();
	UpDownLoad upDownLoad = new UpDownLoad();
	
	/*
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
	*/
	

	public Coded() {}
	
	@Override
	public String toString() {
		return "Coded [src=" + src + ", dsts=" + dsts + ", targets=" + targets + ", files=" + files + "]";
	}

	public Coded(int src) {
		this.src = src;
	}
	public void put(char target, int file, int dst) {
		targets.add(target);
		files.add(file);
		dsts.add(dst);
	}
	
}

class Relay{
	int rack;
	List<Coded> codeds = new ArrayList<>();
	public Relay(int rack) {
		this.rack = rack;
	}
}

class Channel{
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
	
	/*
	public void calcUpDownLoad(List<Node> nodeList) {
		for(Coded coded: codeds) {
			coded.calcUpDownLoad(nodeList);
		}
	}
	*/
	public void printCodes() {
		for(Coded coded: codeds) {
			System.out.println(coded);
		}
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
		return "Node[number=" + num + ", files=" + files + "]";
	}
	
	
	public List<List<Coded>> decode(Coded coded) {
		int cnt=0;
		for(int i=0;i<coded.files.size();i++) {
			if(files.contains(coded.files.get(i))) cnt++;
		}
		List<List<Coded>> ret = new ArrayList<>();
		ret.add(new ArrayList<>());
		ret.add(new ArrayList<>());
		if(cnt==coded.files.size()-1) {
			for(int i=0;i<coded.files.size();i++) {
				Coded code = new Coded();
				code.files.add(coded.files.get(i));
				code.targets.add(coded.targets.get(i));
				if(!files.contains(coded.files.get(i))) {
					code.src = (this.num-1)/(Constants.K/Constants.P);
					ret.get(0).add(code);
				}else {
					code.src = this.num;
					ret.get(1).add(code);
					
				}
			}
		}else {
			return null;
		}
		return ret;
	}
	
	public void printFiles() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node[number=" + num + ", files=[");
		for(Integer file : files) {
			builder.append(file).append(", ");
		}
		builder.deleteCharAt(builder.length()-1);
		builder.deleteCharAt(builder.length()-1);
		builder.append("]]");
		System.out.println(builder.toString());
	}
	
	public boolean checkFinish() {
		for(int i=0;i<decodeResult.size();i++) {
			if(decodeResult.get(i).targets.get(0)=='A'+this.num-1) {
				decodeResultNumber.add(decodeResult.get(i).files.get(0));
			}
		}
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
	int target;
	public VKS(int dst, int src, int file, int target) {
		this.dst = dst;
		this.src = src;
		this.file = file;
		this.target = target;
	}
}

public class Paper{
	
	public static void run() {
		
		Channel crossChannel = new Channel();
		Channel intraChannelP1Down = new Channel();
		Channel intraChannelP1Up = new Channel();
		Channel intraChannelP2 = new Channel();
		
		List<Node> nodeList = new ArrayList<>();
		for(int i=0;i<Constants.K;i++) {
			nodeList.add(new Node(i+1));
			nodeList.get(i).row = i/(Constants.K/Constants.P);
			nodeList.get(i).col = i%(Constants.K/Constants.P);
		}
		List<Relay> relayList = new ArrayList<>();
		for(int i=0;i<Constants.P;i++) {
			relayList.add(new Relay(i));
		}
		FileManager manager = new FileManager(nodeList);
		manager.assign();
		
		for(int i=0;i<nodeList.size();i++) {
			nodeList.get(i).printFiles();
		}
		
		Solution st = new Solution();
		List<List<Integer>> layerNodes = st.layerCombine(Constants.K, Constants.P);
		for(int layer=0;layer<layerNodes.size();layer++) {
			List<Integer> singleLayerCombine = layerNodes.get(layer);
			List<List<Integer>> singleLayerShufferCombine = st.combine(singleLayerCombine, Constants.r+1);
			for(int i=0;i<singleLayerShufferCombine.size();i++) {
				for(int t=0;t<Constants.K/Constants.P;t++) {
					Map<Integer, Integer> count = new HashMap<>();
					for(int y=0;y<singleLayerShufferCombine.get(i).size();y++) {
						count.put(singleLayerShufferCombine.get(i).get(y), 0);
					}
					for(int j:singleLayerShufferCombine.get(i)) {
						List<Integer> other = new ArrayList<>();
						for(int x:singleLayerShufferCombine.get(i)) {
							if(x!=j) other.add(x);
						}
						for(int z:other) {
							nodeList.get(z-1).vkses.add(new ArrayList<>());
						}
						List<Integer> subFiles = manager.combine2file.get(other);
						if(subFiles==null||subFiles.size()==0) continue;
						for(int l=0;l<subFiles.size();l++) {
							nodeList.get(other.get(l%other.size())-1).vkses.get(count.get(other.get(l%other.size()))).add(new VKS(j, other.get(l%other.size()), subFiles.get(l),(j-1)/(Constants.K/Constants.P)*(Constants.K/Constants.P)+1+t));
						}
						for(int z:other) {
							count.put(z, count.get(z)+1);
						}
					}
					
					List<Coded> currentCodeds = new ArrayList<>();
					for(int j:singleLayerShufferCombine.get(i)) {
						Node node = nodeList.get(j-1);
						int col=0;
						while(true) {
							Coded coded = new Coded(j);
							for(int x=0;x<node.vkses.size();x++) {
								if(col<node.vkses.get(x).size()) {
									VKS currentVKS = node.vkses.get(x).get(col);
									coded.put((char) ('A'+currentVKS.target-1), currentVKS.file, currentVKS.dst);
								}
							}
							if(coded.files.isEmpty()) break;
							crossChannel.put(coded);
							currentCodeds.add(coded);
							col++;
						}
						node.vkses.clear();
					}
					for(int j:singleLayerShufferCombine.get(i)) {
						for(int x=0;x<currentCodeds.size();x++) {
							if(currentCodeds.get(x).src==j) continue;
							relayList.get((j-1)/(Constants.K/Constants.P)).codeds.add(currentCodeds.get(x));
						}
					}
					
				}
				
			}
		}
		
		System.out.println("\ncrossChannel Codeds");
		//crossChannel.printCodes();
		UpDownLoad crossLoad = new UpDownLoad();
		crossLoad.up1 = crossLoad.up2 = crossLoad.down1 = crossChannel.codeds.size();
		System.out.println("\ncrossLoad:");
		System.out.println(crossLoad);
		
		
		
		for(int i=0;i<Constants.P;i++) {
			for(int j=0;j<Constants.K/Constants.P;j++) {
				for(int x=0;x<relayList.get(i).codeds.size();x++) {
					Node node = nodeList.get(i*Constants.K/Constants.P+j);
					List<List<Coded>> decode = node.decode(relayList.get(i).codeds.get(x));
					if(decode!=null) {
						intraChannelP1Up.codeds.addAll(decode.get(1));
						intraChannelP1Down.codeds.add(decode.get(0).get(0));
					}
				}
			}
			for(int j=0;j<Constants.K/Constants.P;j++) {
				Node node = nodeList.get(i*Constants.K/Constants.P+j);
				for(int y=0;y<intraChannelP1Down.codeds.size();y++) {
					if(intraChannelP1Down.codeds.get(y).targets.get(0)-'A'+1==node.num) {
						node.decodeResult.add(intraChannelP1Down.codeds.get(y));
					}
				}
			}
		}
		
		System.out.println("\nintraChannelP1Up Codeds");
		//intraChannelP1Up.printCodes();
		System.out.println("\nintraChannelP1Down Codeds");
		//intraChannelP1Down.printCodes();
		UpDownLoad intraLoadP1 = new UpDownLoad();
		intraLoadP1.up2 = intraChannelP1Up.codeds.size();
		intraLoadP1.down2 = intraChannelP1Down.codeds.size();
		System.out.println("\nintraLoadP1:");
		System.out.println(intraLoadP1);
		
		
		for(int i=0;i<Constants.P;i++) {
			List<Coded> currentRackCodeds = new ArrayList<>();
			List<Node> singleRackNodes = nodeList.subList(i*Constants.K/Constants.P, (i+1)*Constants.K/Constants.P);
			for(int j=0;j<singleRackNodes.size();j++) {
				for(int x=0;x<singleRackNodes.get(j).files.size();x++) {
					for(int y=0;y<singleRackNodes.size();y++) {
						if(y!=j) {
							Coded coded = new Coded();
							coded.src = singleRackNodes.get(j).num;
							coded.dsts.add(singleRackNodes.get(y).num);
							coded.files.add(singleRackNodes.get(j).files.get(x));
							coded.targets.add((char) ('A'+singleRackNodes.get(y).num-1));
							intraChannelP2.put(coded);
							currentRackCodeds.add(coded);
						}
					}
				}
				
			}
			for(int j=0;j<singleRackNodes.size();j++) {
				for(int x=0;x<currentRackCodeds.size();x++) {
					if(currentRackCodeds.get(x).dsts.get(0)==singleRackNodes.get(j).num) {
						singleRackNodes.get(j).decodeResult.add(currentRackCodeds.get(x));
					}
				}
			}
		}
		
		System.out.println("\nintraChannelP2 Codeds");
		//intraChannelP2.printCodes();
		UpDownLoad intraLoadP2 = new UpDownLoad();
		intraLoadP2.up2 = intraLoadP2.down2 = intraChannelP2.codeds.size();
		System.out.println("\nintraLoadP2:");
		System.out.println(intraLoadP2);
		
		UpDownLoad totalLoad = new UpDownLoad();
		totalLoad.up1 = crossLoad.up1+intraLoadP1.up1+intraLoadP2.up1;
		totalLoad.up2 = crossLoad.up2+intraLoadP1.up2+intraLoadP2.up2;
		totalLoad.down1 = crossLoad.down1+intraLoadP1.down1+intraLoadP2.down1;
		totalLoad.down2 = crossLoad.down2+intraLoadP1.down2+intraLoadP2.down2;
		System.out.println("\ntotalLoad:");
		System.out.println(totalLoad);
		System.out.println("\ntotalIntraLoad:");
		System.out.println(totalLoad.down2+totalLoad.up2);
		
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
		System.out.println("\n公式计算: ");
		System.out.println(Math.round(st.calcIntraTotalLoad(Constants.K, Constants.P, Constants.Q, Constants.M, Constants.r)));
		System.out.println("\n公式计算增益: ");
		System.out.println(Math.round(st.calcIntraTotalLoadGain(Constants.K, Constants.P, Constants.Q, Constants.M, Constants.r)));
		System.out.println("\n公式计算增益2: ");
		System.out.println(Math.round(st.calcIntraTotalLoadGain2(Constants.K, Constants.P, Constants.Q, Constants.M, Constants.r)));

	}      
	
}
