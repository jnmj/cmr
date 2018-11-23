package com.px.ldcmr;

import java.util.*;

class Constants{
	public static int M=6;
	public static int K=3;
	public static int Q=K;
	public static int r=2;
	public static int st=1;
}

class File{
	int num;
	public File(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "F[" + num + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + num;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		File other = (File) obj;
		if (num != other.num)
			return false;
		return true;
	}
}

class FileManager{
	int partNum;
	int partSize;
	List<Node> nodeList;
	Map<List<Integer>, Integer> partCombCount = new HashMap<>();
	int partCombLevel=0;
	List<File> allFile = new ArrayList<>();
	Map<List<Integer>, List<File>> combine2file = new HashMap<>();
	Map<List<File>, List<Integer>> file2combine = new HashMap<>();
	/*Map<List<File>, List<Integer>> file2combine = new TreeMap<>(new Comparator<List<File>>() {
		@Override
		public int compare(List<File> o1, List<File> o2) {
			return o1.get(0).num-o2.get(0).num;
		}
	});*/
	Map<List<Integer>, List<File>> shortCombine2file = new HashMap<>();
	Map<List<File>, List<Integer>> shortFile2combine = new HashMap<>();
	/*Map<List<File>, List<Integer>> shortFile2combine = new TreeMap<>(new Comparator<List<File>>() {
		@Override
		public int compare(List<File> o1, List<File> o2) {
			return o1.get(0).num-o2.get(0).num;
		}
	});*/
	
	public boolean validate(Integer nodeNum, File file) {
		for(List<File> key: shortFile2combine.keySet()) {
			if(key.contains(file)) {
				if(shortFile2combine.get(key).contains(nodeNum)) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	public void printF2C() {
		System.out.println("\nF2C:");
		for(List<File> list : file2combine.keySet()) {
			System.out.println(list+"\t"+file2combine.get(list));
		}
	}
	
	public void printShortF2C() {
		System.out.println("\nshortF2C:");
		for(List<File> list : shortFile2combine.keySet()) {
			System.out.println(list+"\t"+shortFile2combine.get(list));
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
	
	public void printShortC2F() {
		List<List<Integer>> sortedKeys = new ArrayList<>();
		for(List<Integer> list : shortCombine2file.keySet()) {
			sortedKeys.add(list);
		}
		Collections.sort(sortedKeys, new Comparator<List<Integer>>() {
			@Override
			public int compare(List<Integer> o1, List<Integer> o2) {
				for(int i=0;i<o1.size();i++) {
					if(o1.get(i)!=o2.get(i)) return o1.get(i)-o2.get(i);
				}
				return 0;
				//return shortCombine2file.get(o1).get(0).num-shortCombine2file.get(o2).get(0).num;
			}
		});
		System.out.println("\nshortC2F:");
		for(List<Integer> list : sortedKeys) {
			System.out.println(list+"  "+shortCombine2file.get(list));
		}
	}
	
	public void fillNodes(List<Node> nodeList, List<List<Integer>> combine) {
		for(int i = 0; i< Constants.K; i++) {
			nodeList.add(new Node(i+1));
		}
		for(int i=0;i<combine.size();i++) {
			for(int j:combine.get(i)) {
				nodeList.get(j-1).allFiles.addAll(this.getPart(i+1));
			}
		}
		
		System.out.println("\nnode allFiles:");
		for(int i = 0; i< Constants.K; i++) {
			nodeList.get(i).printAllFiles();
		}
	}
	
	public void updateNodes(List<Node> nodeList) {
		for(int i = 0; i< Constants.K; i++) {
			nodeList.get(i).updateMappedFiles(this);
		}
		
		System.out.println("\nnode mappedFiles:");
		for(int i = 0; i< Constants.K; i++) {
			System.out.println(nodeList.get(i).num+"  "+nodeList.get(i).mappedFiles.size());
			nodeList.get(i).printMappedFiles();
		}
	}
	
	public FileManager(List<List<Integer>> combine, List<Node> nodeList) {
		this.nodeList=nodeList;
		this.partNum = combine.size();
		partSize = Constants.M/partNum;
		for(int i = 0; i< Constants.M; i++) {
			allFile.add(new File(i+1));
		}
		for(int i=0;i<combine.size();i++) {
			combine2file.put(combine.get(i), allFile.subList(i*partSize, (i+1)*partSize));
			file2combine.put(allFile.subList(i*partSize, (i+1)*partSize), combine.get(i));
		}
		
		fillNodes(nodeList, combine);
		
		/*if(Constants.st==1) {
			Solution st = new Solution();
			List<List<Integer>> partComb = st.combine(Constants.K, Constants.r);
			for(int i=0;i<partComb.size();i++) {
				partCombCount.put(partComb.get(i), 0);
			}
			
			for(int i=0;i<combine.size();i++) {
				List<List<Integer>> comb = st.combine(Constants.pK, Constants.r);
				List<List<Integer>> candidates = new ArrayList<>();
				List<Integer> selected = null;
				for(int k=0;k<comb.size();k++) {
					List<Integer> list = new ArrayList<>();
					for(int j=0;j<combine.get(i).size();j++) {
						if(comb.get(k).contains(j+1)) list.add(combine.get(i).get(j));
					}
					candidates.add(list);
				}
				int minIndx=0;
				int minCount=Integer.MAX_VALUE;
				for(int indx=0;indx<candidates.size();indx++) {
					if(partCombCount.get(candidates.get(indx))<minCount) {
						minIndx=indx;
						minCount=partCombCount.get(candidates.get(indx));
					}
				}
				selected = candidates.get(minIndx);
				partCombCount.put(selected, partCombCount.get(selected)+1);
				
				boolean levelUp = true;
				for(List<Integer> key : partCombCount.keySet()) {
					if(partCombCount.get(key)==partCombLevel+1) {
						levelUp&=true;
					}else {
						levelUp&=false;
					}
				}
				if(levelUp) {
					partCombLevel++;
				}
				
				List<File> files = allFile.subList(i*partSize, (i+1)*partSize);
				List<File> tmp = new ArrayList<>();
				for(int k=0;k<files.size();k++) {
					tmp.add(files.get(k));
				}
				if(shortCombine2file.containsKey(selected)) {
					shortCombine2file.get(selected).addAll(tmp);
				}else {
					shortCombine2file.put(selected, tmp);
				}
			}
			for(List<Integer> key : shortCombine2file.keySet()) {
				shortFile2combine.put(shortCombine2file.get(key), key);
			}
		}else if(Constants.st==2) {
			for(int i=0;i<combine.size();i++) {
				shortFile2combine.put(allFile.subList(i*partSize, (i+1)*partSize), new ArrayList<>());
			}
			int depth = Constants.M*Constants.pK/Constants.K/partSize;
			int[] x = new int[nodeList.size()];
			Arrays.fill(x, 0);
			while(true) {
				for(int j=0;j<nodeList.size();j++) {
					if(x[j]%2==0) {
						if(x[j]>0&&x[j]<=2*depth&&shortFile2combine.get(nodeList.get(j).allFiles.subList((x[j]/2-1)*partSize, (x[j]/2)*partSize)).size()<Constants.r) {
							shortFile2combine.get(nodeList.get(j).allFiles.subList((x[j]/2-1)*partSize, x[j]/2*partSize)).add(nodeList.get(j).num);
						}
						x[j]+=1;
					}else {
						while(x[j]<2*depth&&shortFile2combine.get(nodeList.get(j).allFiles.subList(x[j]/2*partSize, (x[j]/2+1)*partSize)).size()>=Constants.r) {
							x[j]+=2;
						}
						x[j]+=1;
					}
				}
				boolean finish=true;
				for(int j=0;j<nodeList.size();j++) {
					if(x[j]<=2*depth) {
						finish=false;
						break;
					}
				}
				if(finish) break;
			}
			for(List<File> key : shortFile2combine.keySet()) {
				Collections.sort(shortFile2combine.get(key));
				List<File> tmp = new ArrayList<>();
				for(int k=0;k<key.size();k++) {
					tmp.add(key.get(k));
				}
				if(shortCombine2file.containsKey(shortFile2combine.get(key))){
					shortCombine2file.get(shortFile2combine.get(key)).addAll(tmp);
				}else {
					shortCombine2file.put(shortFile2combine.get(key), tmp);
				}
			}
			
		}*/
		
		updateNodes(nodeList);
		
		printC2F();
		printF2C();
		//printShortC2F();
		//printShortF2C();
	}
	public List<File> getPart(int num){
		return allFile.subList((num-1)*partSize, num*partSize);
	}
}

class Coded{
	int src;
	List<Character> targets = new ArrayList<>();
	List<Integer> files = new ArrayList<>();
	
	public Coded() {}
	
	public Coded(int src) {
		this.src = src;
	}
	public void put(char target, int file) {
		targets.add(target);
		files.add(file);
	}
	@Override
	public String toString() {
		return "Coded[src=" + src + ", t=" + targets + ", f=" + files + "]";
	}
	
	public void merge(Coded coded) {
		if(coded.files.isEmpty()) return;
		for(int i=0;i<coded.files.size();i++) {
			targets.add(coded.targets.get(i));
			files.add(coded.files.get(i));
		}
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
	
	public void printCodes() {
		System.out.println("\nchannel codeds:");
		for(Coded coded: codeds) {
			System.out.println(coded);
		}
	}
	
	public int getLoad() {
		return codeds.size();
	}
}

class Node{
	int num;
	List<File> allFiles = new ArrayList<>();
	List<List<VKS>> vkses = new ArrayList<>();
	List<Integer> mappedFiles = new ArrayList<>();
	List<Integer> decodeResult = new ArrayList<>();
	public Node(int num) {
		this.num = num;
	}
	
	@Override
	public String toString() {
		return "Node[number=" + num + ", allFiles=" + allFiles + "]";
	}
	
	public Integer decodeRandom(Coded coded) {
		int cnt=0;
		for(int i=0;i<coded.files.size();i++) {
			if(mappedFiles.contains(coded.files.get(i))||
					(coded.targets.get(i)==(char)('a'+this.num-1)&&decodeResult.contains(coded.files.get(i)))) {
				cnt++;
			}
		}
		if(cnt==coded.files.size()-1) {
			for(int i=0;i<coded.files.size();i++) {
				if(mappedFiles.contains(coded.files.get(i))||
						(coded.targets.get(i)=='a'+this.num-1&&decodeResult.contains(coded.files.get(i)))) {
					continue;
				}else {
					if(coded.targets.get(i)==(char)('a'+this.num-1)) {
						decodeResult.add(coded.files.get(i));
						return 1;
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
	
	public Integer decode(Coded coded) {
		int cnt=0;
		for(int i=0;i<coded.files.size();i++) {
			if(mappedFiles.contains(coded.files.get(i))) cnt++;
		}
		if(cnt==coded.files.size()-1) {
			for(int i=0;i<coded.files.size();i++) {
				if(!mappedFiles.contains(coded.files.get(i))) {
					if(coded.targets.get(i)-'a'+1==num) {
						return coded.files.get(i);
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
	
	public void printAllFiles() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node[number=" + num + ", allFiles=[");
		for(File file : allFiles) {
			builder.append(file.num).append(", ");
		}
		builder.deleteCharAt(builder.length()-1);
		builder.deleteCharAt(builder.length()-1);
		builder.append("]]");
		System.out.println(builder.toString());
	}
	
	public void updateMappedFiles(FileManager manager) {
		for(File file : allFiles) {
			if(manager.validate(num, file)) mappedFiles.add(file.num);
		}
	}
	
	public void printMappedFiles() {
		StringBuilder builder = new StringBuilder();
		System.out.println("Node[number=" + num + ", mappedFiles="+mappedFiles+"]");
	}
	
	public boolean checkFinish() {
		boolean ret = true;
		for(int i = 1; i<= Constants.M; i++) {
			if(mappedFiles.contains(i)||decodeResult.contains(i)) {
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
	File file;
	public VKS(int dst, int src, File file) {
		this.dst = dst;
		this.src = src;
		this.file = file;
	}
}

public class Paper{
	
	public static int run() {
		Solution st = new Solution();
		List<List<Integer>> combine = st.combine(Constants.K, Constants.r);
		List<Node> nodeList = new ArrayList<>();
		FileManager manager = new FileManager(combine, nodeList);
		Channel channel = new Channel();
		
		List<List<Integer>> shuffleCombine = st.combine(Constants.K, Constants.r+1);
		for(int i=0;i<shuffleCombine.size();i++) {
			Map<Integer, Integer> count = new HashMap<>();
			for(int y=0;y<shuffleCombine.get(i).size();y++) {
				count.put(shuffleCombine.get(i).get(y), 0);
			}
			for(int j:shuffleCombine.get(i)) {
				List<Integer> other = new ArrayList<>();
				for(int x:shuffleCombine.get(i)) {
					if(x!=j) other.add(x);
				}
				for(int z:other) {
					nodeList.get(z-1).vkses.add(new ArrayList<>());
				}
				List<File> subFiles = manager.combine2file.get(other);
				if(subFiles==null||subFiles.size()==0) continue;
				for(int l=0;l<subFiles.size();l++) {
					nodeList.get(other.get(l%other.size())-1).vkses.get(count.get(other.get(l%other.size()))).add(new VKS(j, other.get(l%other.size()), subFiles.get(l)));
				}
				for(int z:other) {
					count.put(z, count.get(z)+1);
				}
			}
			
			List<Coded> medium = new ArrayList<>();
			List<Coded> randomCode = new ArrayList<>();
			for(int j:shuffleCombine.get(i)) {
				Node node = nodeList.get(j-1);
				int col=0;
				while(true) {
					Coded coded = new Coded(j);
					for(int x=0;x<node.vkses.size();x++) {
						if(col<node.vkses.get(x).size()) {
							coded.put((char) ('a'+node.vkses.get(x).get(col).dst-1), node.vkses.get(x).get(col).file.num);
						}
					}
					if(coded.files.isEmpty()) break;
					medium.add(coded);
					//channel.put(coded);
					col++;
				}
				node.vkses.clear();
			}
			List<List<Integer>> randomCombine = st.combine(Constants.r+1, Constants.r);
			for(int j = 0; j< Constants.r; j++) {
				List<Integer> comb = randomCombine.get(j);
				Coded downLinkCoded = new Coded();
				for(int x=0;x<comb.size();x++) {
					downLinkCoded.merge(medium.get(comb.get(x)-1));
				}
				channel.put(downLinkCoded);
				randomCode.add(downLinkCoded);
			}
			
			for(int j:shuffleCombine.get(i)) {
				int totalCnt=0;
				Map<Integer, Integer> cnt = new HashMap<>();
				for(int x=0;x<randomCode.size();x++) {
					cnt.put(x, 0);
				}
				
				while(true) {
					for(int x=0;x<randomCode.size();x++) {
						if(cnt.get(x)==0) {
							Integer decode = nodeList.get(j-1).decodeRandom(randomCode.get(x));
							if(decode!=null) {
								cnt.put(x, 1);
								totalCnt++;
							}
						}
					}
					if(totalCnt==randomCode.size()) break;
				}
			}
			
		}
		
		//channel.sort();
		channel.printCodes();
		System.out.println();
		
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
		/*
		java.io.File file = new java.io.File("C:/Users/PX/Desktop/a.txt"); 
		DecimalFormat df = new DecimalFormat("0.00");  
        BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(channel.getLoad()+"\r\n");
			writer.write(Constants.K+"\r\n");
			List<Double> rate = new ArrayList<>();
			for(int i=0;i<nodeList.size();i++) {
				rate.add(nodeList.get(i).mappedFiles.size()*1.0/nodeList.get(i).allFiles.size());
				System.out.println(df.format(rate.get(i)));
				writer.write(df.format(rate.get(i))+"\r\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		System.out.println();
		/*
		for(int i=0;i<nodeList.size();i++) {
			for(int j=0;j<channel.codeds.size();j++) {
				if(channel.codeds.get(j).src==nodeList.get(i).num) continue;
				Integer decode = nodeList.get(i).decode(channel.codeds.get(j));
				if(decode!=null) {
					nodeList.get(i).mappedFiles.add(decode);
				}
			}
		}
		
		boolean finish = true;
		for(int i=0;i<nodeList.size();i++) {
			List<Integer> pivot = new ArrayList<>();
			for(int j=0;j<Constants.M;j++) {
				pivot.add(j+1);
			}
			Collections.sort(nodeList.get(i).mappedFiles);
			nodeList.get(i).printMappedFiles();
			if(pivot.equals(nodeList.get(i).mappedFiles)) {
				finish&=true;
			}else {
				finish&=false;
			}
		}
		
		System.out.println(finish?"\n完成":"\n未完成");
		System.out.println("\ncommunication load:\n"+channel.getLoad()+"\n");
		*/
		return channel.getLoad();
		
	}
	
	public static void main(String[] args) {
		int sum=0;
		int cnt=0;
		int load;
		while(true) {
			load=run();
			if(load==-1) continue;
			sum+=load;
			cnt++;
			System.out.println("cnt: "+cnt);
			if(cnt==1) break;
		}
		System.out.println(sum*1.0/cnt);
	}      
	
}
