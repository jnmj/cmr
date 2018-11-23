package com.px.cmr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Constants {
    public static int M = 6;
    public static int K = 3;
    public static int Q = K;
    public static int r = 2;
}

class File {
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

class FileManager {
    int partNum;
    int partSize;
    List<Node> nodeList;
    List<File> allFile = new ArrayList<>();
    Map<List<Integer>, List<File>> combine2file = new HashMap<>();
    Map<List<File>, List<Integer>> file2combine = new HashMap<>();


    public void printF2C() {
        System.out.println("\nF2C:");
        for (List<File> list : file2combine.keySet()) {
            System.out.println(list + "\t" + file2combine.get(list));
        }
    }

    public void printC2F() {
        List<List<Integer>> sortedKeys = new ArrayList<>();
        for (List<Integer> list : combine2file.keySet()) {
            sortedKeys.add(list);
        }
        Collections.sort(sortedKeys, new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                for (int i = 0; i < o1.size(); i++) {
                    if (o1.get(i) != o2.get(i)) return o1.get(i) - o2.get(i);
                }
                return 0;
            }
        });
        System.out.println("\nC2F:");
        for (List<Integer> list : sortedKeys) {
            System.out.println(list + "  " + combine2file.get(list));
        }
    }

    public void fillNodes(List<Node> nodeList, List<List<Integer>> combine) {
        for (int i = 0; i < Constants.K; i++) {
            nodeList.add(new Node(i + 1));
        }
        for (int i = 0; i < combine.size(); i++) {
            for (int j : combine.get(i)) {
                nodeList.get(j - 1).allFiles.addAll(this.getPart(i + 1));
            }
        }

        System.out.println("\nnode MappedFiles:");
        for (int i = 0; i < Constants.K; i++) {
            nodeList.get(i).printMappedFiles();
        }
    }

    public void updateNodes(List<Node> nodeList) {
        for (int i = 0; i < Constants.K; i++) {
            nodeList.get(i).updateMappedFiles(this);
        }
    }

    public FileManager(List<List<Integer>> combine, List<Node> nodeList) {
        this.nodeList = nodeList;
        this.partNum = combine.size();
        partSize = Constants.M / partNum;
        for (int i = 0; i < Constants.M; i++) {
            allFile.add(new File(i + 1));
        }
        for (int i = 0; i < combine.size(); i++) {
            combine2file.put(combine.get(i), allFile.subList(i * partSize, (i + 1) * partSize));
            file2combine.put(allFile.subList(i * partSize, (i + 1) * partSize), combine.get(i));
        }

        fillNodes(nodeList, combine);
        updateNodes(nodeList);

        printC2F();
        printF2C();
    }

    public List<File> getPart(int num) {
        return allFile.subList((num - 1) * partSize, num * partSize);
    }
}

class Coded {
    int src;
    List<Character> targets = new ArrayList<>();
    List<Integer> files = new ArrayList<>();

    public Coded() {
    }

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
        if (coded.files.isEmpty()) return;
        for (int i = 0; i < coded.files.size(); i++) {
            targets.add(coded.targets.get(i));
            files.add(coded.files.get(i));
        }
    }
}

class Channel {
    List<Coded> codeds = new ArrayList<>();

    public void put(Coded coded) {
        codeds.add(coded);
    }

    public List<Coded> getCodeds() {
        return codeds;
    }

    public void addAll(Coded[] array) {
        for (int i = 0; i < array.length; i++) {
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
                return o1.src - o2.src;
            }
        });
    }

    public void printCodes() {
        System.out.println("\nchannel codeds:");
        for (Coded coded : codeds) {
            System.out.println(coded);
        }
    }

    public int getLoad() {
        return codeds.size();
    }
}

class Node {
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

    public Integer decode(Coded coded) {
        int cnt = 0;
        for (int i = 0; i < coded.files.size(); i++) {
            if (mappedFiles.contains(coded.files.get(i))) cnt++;
        }
        if (cnt == coded.files.size() - 1) {
            for (int i = 0; i < coded.files.size(); i++) {
                if (!mappedFiles.contains(coded.files.get(i))) {
                    if (coded.targets.get(i) - 'a' + 1 == num) {
                        decodeResult.add(coded.files.get(i));
                        return 1;
                    } else {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
        return null;
    }

    public void printMappedFiles() {
        StringBuilder builder = new StringBuilder();
        builder.append("Node[number=" + num + ", mappedFiles=[");
        for (File file : allFiles) {
            builder.append(file.num).append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]]");
        System.out.println(builder.toString());
    }

    public void updateMappedFiles(FileManager manager) {
        for (File file : allFiles) {
            mappedFiles.add(file.num);
        }
    }

    public boolean checkFinish() {
        boolean ret = true;
        for (int i = 1; i <= Constants.M; i++) {
            if (mappedFiles.contains(i) || decodeResult.contains(i)) {
                continue;
            } else {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public void printAfterFinished() {
        StringBuilder builder = new StringBuilder();
        builder.append("Node[number=" + num + ", mappedFiles=[");
        for (File file : allFiles) {
            builder.append(file.num).append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append("], receivedKVs=[");
        for (Integer dr : decodeResult) {
            builder.append(dr).append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        System.out.println(builder.toString());
    }
}

class VKS {
    int dst;
    int src;
    File file;

    public VKS(int dst, int src, File file) {
        this.dst = dst;
        this.src = src;
        this.file = file;
    }
}

public class Paper {

    public static int run() {
        Solution st = new Solution();
        List<List<Integer>> combine = st.combine(Constants.K, Constants.r);
        List<Node> nodeList = new ArrayList<>();
        FileManager manager = new FileManager(combine, nodeList);
        Channel channel = new Channel();

        List<List<Integer>> shuffleCombine = st.combine(Constants.K, Constants.r + 1);
        for (int i = 0; i < shuffleCombine.size(); i++) {
            Map<Integer, Integer> count = new HashMap<>();
            for (int y = 0; y < shuffleCombine.get(i).size(); y++) {
                count.put(shuffleCombine.get(i).get(y), 0);
            }
            for (int j : shuffleCombine.get(i)) {
                List<Integer> other = new ArrayList<>();
                for (int x : shuffleCombine.get(i)) {
                    if (x != j) other.add(x);
                }
                for (int z : other) {
                    nodeList.get(z - 1).vkses.add(new ArrayList<>());
                }
                List<File> subFiles = manager.combine2file.get(other);
                if (subFiles == null || subFiles.size() == 0) continue;
                for (int l = 0; l < subFiles.size(); l++) {
                    nodeList.get(other.get(l % other.size()) - 1).vkses.get(count.get(other.get(l % other.size()))).add(new VKS(j, other.get(l % other.size()), subFiles.get(l)));
                }
                for (int z : other) {
                    count.put(z, count.get(z) + 1);
                }
            }

            List<Coded> medium = new ArrayList<>();
            for (int j : shuffleCombine.get(i)) {
                Node node = nodeList.get(j - 1);
                int col = 0;
                while (true) {
                    Coded coded = new Coded(j);
                    for (int x = 0; x < node.vkses.size(); x++) {
                        if (col < node.vkses.get(x).size()) {
                            coded.put((char) ('a' + node.vkses.get(x).get(col).dst - 1), node.vkses.get(x).get(col).file.num);
                        }
                    }
                    if (coded.files.isEmpty()) break;
                    medium.add(coded);
                    channel.put(coded);
                    col++;
                }
                node.vkses.clear();
            }

            for (int j : shuffleCombine.get(i)) {
                for (int x = 0; x < medium.size(); x++) {
                    Integer decode = nodeList.get(j - 1).decode(medium.get(x));
                }
            }

        }

        //channel.sort();
        channel.printCodes();
        System.out.println();

        boolean finish = true;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).checkFinish()) {
                continue;
            } else {
                finish = false;
                break;
            }
        }
        System.out.println(finish ? "完成" : "未完成");
        System.out.println();
        for (int i = 0; i < nodeList.size(); i++) {
            nodeList.get(i).printAfterFinished();
        }
        return channel.getLoad();

    }

    public static void main(String[] args) {
        run();
        /*int sum = 0;
        int cnt = 0;
        int load;
        while (true) {
            load = run();
            if (load == -1) continue;
            sum += load;
            cnt++;
            System.out.println("cnt: " + cnt);
            if (cnt == 1) break;
        }
        System.out.println(sum * 1.0 / cnt);*/
    }

}
