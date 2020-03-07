package kattis.nac20;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class LetterWheels {
    public static void main(String[] args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        char[] lineA = in.readLine().toCharArray();
        char[] lineB = in.readLine().toCharArray();
        char[] lineC = in.readLine().toCharArray();
        int size = lineA.length;
        List<Integer> abShifts = new LinkedList<>();
        List<Integer> acShifts = new LinkedList<>();
        Set<Integer> bcShifts = new HashSet<>();

        for (int i = 0; i < size; i++) {
            boolean abGood = true;
            boolean acGood = true;
            boolean bcGood = true;
            for (int off = 0; off < size && (abGood || acGood || bcGood); off++) {
                abGood = abGood && (lineA[(i + off) % size] != lineB[off]);
                acGood = acGood && (lineA[(i + off) % size] != lineC[off]);
                bcGood = bcGood && (lineB[(i + off) % size] != lineC[off]);
            }
            int shift = (i <= size / 2) ? i : i - size;
            if (abGood) {
                abShifts.add(shift);
            }
            if (acGood) {
                acShifts.add((shift));
            }
            if (bcGood) {
                bcShifts.add(shift);
            }
        }
        int min = Integer.MAX_VALUE;
        for(int ab : abShifts){
            for(int ac : acShifts){
                int bc = (ac - ab);
                bc = (bc > -(size + 1)/2) ? bc: bc + size;
                bc = bc <= size/2 ? bc : bc - size;
                if(bcShifts.contains(bc)){
                    min = Math.min(min, Math.min(Math.abs(ab)+Math.abs(ac), Math.min(Math.abs(ab)+Math.abs(bc),Math.abs(bc)+Math.abs(ac))));
                }
            }
        }
        if(min == Integer.MAX_VALUE){
            System.out.print(-1);
        } else {
            System.out.print(min);
        }
    }
}
