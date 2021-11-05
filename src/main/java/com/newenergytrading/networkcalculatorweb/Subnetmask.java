package com.newenergytrading.networkcalculatorweb;

import org.apache.commons.lang3.StringUtils;

public class Subnetmask extends IPAddress {

    private int hosts;

    public Subnetmask(){}
    public Subnetmask(String input) {
        super(input);
        if (this.getFirst() != 255) {
            throw new IllegalArgumentException();
        }
        if (this.getFourth() >253) {
            throw new IllegalStateException();
        }
        String binary = this.toBinaryString();
        if (!(binary.startsWith("1") && binary.endsWith("0") && !binary.contains("01"))) {
            throw new IllegalArgumentException("invalid subnetmask: " + binary);
        }
        setHosts(binary);
    }

    public void setHosts(String binary) {
        this.hosts = (int) (Math.pow(2, StringUtils.countMatches(binary, "0"))-2);
    }

    public int getHosts() {
        return hosts;
    }

    public Subnetmask(int first, int second, int third, int fourth) {
        this.setFirst(first);
        this.setSecond(second);
        this.setThird(third);
        this.setFourth(fourth);
    }

    public IPAddress invert() {
        int first = ~this.getFirst() & 255; // cut off leading zeros
        int second = ~this.getSecond() & 255; // cut off leading zeros
        int third = ~this.getThird() & 255; // cut off leading zeros
        int fourth = ~this.getFourth() & 255; // cut off leading zeros
        return new IPAddress(first, second, third, fourth);
    }
}