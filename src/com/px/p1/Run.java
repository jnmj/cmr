package com.px.p1;

import com.px.p1.cmr.PaperCMR;
import com.px.p1.cwdc.PaperCWDC;
import com.px.p1.ldcmr.PaperLDCMR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Run {
    public static void main(String[] args) {
        Map<Integer, Integer> density = new HashMap<>();
        System.out.println("\n密度: ");
        for (int i = 1; i <= Constants.K; i++) {
            int rand = Constants.getNormrnd();
            density.put(i, rand);
            System.out.println((char)('a'+ i - 1)+ ": "+rand);
        }
        List<List<Integer>> result = new ArrayList<>();
        System.out.println("\n******CMR******");
        result.add(PaperCMR.run(density));
        System.out.println("\n***************");

        System.out.println("\n******CWDC******");
        result.add(PaperCWDC.run(density));
        System.out.println("\n****************");

        System.out.println("\n******LDCMR******");
        result.add(PaperLDCMR.run(density));
        System.out.println("\n*****************");

        System.out.println("\nresult:");
        System.out.println("\t\tup\t\tdown\ttotal\tupNum\tdownNum\ttotalNum");

        System.out.print("cmr\t\t");
        for (int i = 0; i < result.get(0).size(); i++) {
            System.out.print(result.get(0).get(i)+"\t\t");
        }
        System.out.println();

        System.out.print("cwdc\t");
        for (int i = 0; i < result.get(1).size(); i++) {
            System.out.print(result.get(1).get(i)+"\t\t");
        }
        System.out.println();

        System.out.print("ldcmr\t");
        for (int i = 0; i < result.get(2).size(); i++) {
            System.out.print(result.get(2).get(i)+"\t\t");
        }
        System.out.println();
    }
}
