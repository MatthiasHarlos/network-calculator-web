package com.newenergytrading.networkcalculatorweb;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class IPAddress {
    private int first;
    private int second;
    private int third;
    private int fourth;

    public IPAddress(String input) {
        if (!checkDots(input)) {
            throw new IllegalArgumentException("no valid IP");
        }
        List<String> parts = Arrays.asList(input.split("\\."));

        if (parts.size() != 4) {
            throw new IllegalArgumentException();
        }

        List<Integer> componentsInt = new ArrayList<>();

        for (String part : parts) {
            componentsInt.add(Integer.parseInt(part));
        }
        if (componentsInt.get(0) < 1) {
            throw new IllegalArgumentException("IP darf nicht mit 0 beginnen!");
        }
        setFirst(componentsInt.get(0));
        setSecond(componentsInt.get(1));
        setThird(componentsInt.get(2));
        setFourth(componentsInt.get(3));
    }

    public IPAddress() {
    }

    public IPAddress(int first, int second, int third, int fourth) {
        this.setFirst(first);
        this.setSecond(second);
        this.setThird(third);
        this.setFourth(fourth);
    }

    public boolean checkDots(String value) {
        int count = StringUtils.countMatches(value, ".");
        return count == 3;
    }

    @Override
    public String toString() {
        return this.first + "." + this.second + "." + this.third + "." + this.fourth;
    }

    public String toBinaryString() {
        String result = "";
        result += componentToBinaryString(this.getFirst());
        result += componentToBinaryString(this.getSecond());
        result += componentToBinaryString(this.getThird());
        result += componentToBinaryString(this.getFourth());

        return result;
    }

    private String componentToBinaryString(int component) {
        StringBuilder result = new StringBuilder(Integer.toBinaryString(component));

        while (result.length() <= 7) {
            result.insert(0, "0");
        }
        return result.toString();
    }


    public void setFirst(int first) {
        validateComponentRange(first, "first");
        this.first = first;
    }

    public void setSecond(int second) {
        validateComponentRange(second, "second");
        this.second = second;
    }

    public void setThird(int third) {
        validateComponentRange(third, "third");
        this.third = third;
    }

    public void setFourth(int fourth) {
        validateComponentRange(fourth, "fourth");
        this.fourth = fourth;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() {
        return third;
    }

    public int getFourth() {
        return fourth;
    }

    public void validateComponentRange(int value, String component) {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException(component + " out of Range");
        }
    }

    public IPAddress logicalAnd(IPAddress another) {
        Objects.requireNonNull(another);
        int first = this.getFirst() & another.getFirst();
        int second = this.getSecond() & another.getSecond();
        int third = this.getThird() & another.getThird();
        int fourth = this.getFourth() & another.getFourth();
        IPAddress result = new IPAddress();
        result.setFirst(first);
        result.setSecond(second);
        result.setThird(third);
        result.setFourth(fourth);
        return result;
    }
}