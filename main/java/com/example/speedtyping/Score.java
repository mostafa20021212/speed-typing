package com.example.speedtyping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    private int max15, max30, max60;
    private int acc15, acc30, acc60;
    private List<Entry<Character, Integer>> sortedList15,sortedList30,sortedList60;

    public int getAcc15() {
        return acc15;
    }

    public void setAcc15(int acc15) {
        this.acc15 = acc15;
    }

    public int getAcc30() {
        return acc30;
    }

    public void setAcc30(int acc30) {
        this.acc30 = acc30;
    }

    public int getAcc60() {
        return acc60;
    }

    public void setAcc60(int acc60) {
        this.acc60 = acc60;
    }

    public Score() {
        this.max15 = 0;
        this.max30 = 0;
        this.max60 = 0;
    }

    public int getMax15() {
        return max15;
    }

    public void setMax15(int max15) {
        this.max15 = max15;
    }

    public int getMax30() {
        return max30;
    }

    public void setMax30(int max30) {
        this.max30 = max30;
    }

    public int getMax60() {
        return max60;
    }

    public void setMax60(int max60) {
        this.max60 = max60;
    }

    public void updateWorstCh(Map<Character, Integer> storage,int time) {
        if(time == 15 ){
            sortedList15 = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : storage.entrySet()) {
                sortedList15.add(new Entry<>(entry.getKey(), entry.getValue()));
            }
            sortedList15.sort((e1, e2) -> e2.getValue() - e1.getValue());
        } else if (time == 30) {
            sortedList30 = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : storage.entrySet()) {
                sortedList30.add(new Entry<>(entry.getKey(), entry.getValue()));
            }
            sortedList30.sort((e1, e2) -> e2.getValue() - e1.getValue());
        }
        else{
            sortedList60 = new ArrayList<>();
            for (Map.Entry<Character, Integer> entry : storage.entrySet()) {
                sortedList60.add(new Entry<>(entry.getKey(), entry.getValue()));
            }
            sortedList60.sort((e1, e2) -> e2.getValue() - e1.getValue());
        }
    }


    public String toStringTime(int time) {
         List<Entry<Character, Integer>>sortedList;
        if(time == 15){
            sortedList = sortedList15;
        } else if (time == 30) {
            sortedList = sortedList30;
        }else   sortedList = sortedList60;
        StringBuilder str = new StringBuilder("[ ");
        int n = 5;
        for (Entry<Character, Integer> entry : sortedList) {
            str.append(entry.getKey()).append(" ");
            if (--n == 0) break;
        }
        str.append("]");

        return str.toString();
    }
    private static class Entry<K, V> implements Serializable {

        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}