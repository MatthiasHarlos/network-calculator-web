package com.newenergytrading.networkcalculatorweb;

import forms.NetworkInputForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class NetworkController {

    @GetMapping("/")
    public void handleRequest() {
        throw new RuntimeException("test exception");
    }

    @GetMapping("formular")
    public String newFoo(Model model) {
        model.addAttribute("networkInputForm", new NetworkInputForm());
        return "input-template";
    }

    @PostMapping("calculate")
    public String calculator(Model model, @Valid NetworkInputForm networkInputForm, BindingResult bindingResult) {
        IPAddress iPAddress;
        Subnetmask snm = null;
        System.out.println("form" + networkInputForm);
        System.out.println("bindingResult: " + bindingResult);
        Integer shortsnm = 0;
        if(networkInputForm.getShortsnm() != null) {
            shortsnm = networkInputForm.getShortsnm();
        }
        snm = convertShortSNM(networkInputForm);
        validate(networkInputForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "input-template";
        }
            iPAddress = new IPAddress(networkInputForm.getIpString());
        if(shortsnm < 1) {
                snm = new Subnetmask(networkInputForm.getSnmString());
        }
        model.addAttribute("iPAddress", iPAddress);
        model.addAttribute("snm", snm);
        IPAddress netID = calculateNetID(iPAddress, snm);
        model.addAttribute("netid", netID);
        IPAddress broadcast = calculateBroadcastIp(netID, snm);
        model.addAttribute("broadcast", broadcast);
        int hosts = snm.getHosts();
        model.addAttribute("hosts", hosts);
        Networks simpleNetwork = new Networks();
        simpleNetwork.setSimpleFirstAndLastIP(netID, broadcast);
        model.addAttribute("firstip", simpleNetwork.getSimpleFirstAndLastIP().get(0));
        model.addAttribute("lastip", simpleNetwork.getSimpleFirstAndLastIP().get(1));
        Networks networks = new Networks(netID, snm, hosts);
        model.addAttribute("allnetworks", networks.getNetworks());
        return "networkcalculate-template";
    }

    private Subnetmask convertShortSNM(NetworkInputForm networkInputForm) {
        Integer shortsnm = 0;
        Subnetmask snm = null;
        if(networkInputForm.getShortsnm() != null) {
            shortsnm = networkInputForm.getShortsnm();
        }
        if(shortsnm > 1) {
            StringBuilder binarySubnetmask = new StringBuilder();
            binarySubnetmask.append("1".repeat(shortsnm));
            binarySubnetmask.append("0".repeat(Math.max(0, 32 - binarySubnetmask.length())));
            List<String> binaryParts = new ArrayList<>();
            for (int i = 0; i <= 24; i=i+8) {
                binaryParts.add(binarySubnetmask.substring(i, i+8));
            }
            List<Integer> snmPartsDecimal = new ArrayList<>();
            for (int i = 0; i < 4; i++) {

                snmPartsDecimal.add(Integer.parseInt(binaryParts.get(i), 2));
            }
            snm = new Subnetmask(snmPartsDecimal.get(0), snmPartsDecimal.get(1), snmPartsDecimal.get(2), snmPartsDecimal.get(3));
            networkInputForm.setSnmString(snm.toString());
            snm.setHosts(binarySubnetmask.toString());
        }
        return snm;
    }

    private void validate(NetworkInputForm networkInputForm, BindingResult bindingResult) {
        Objects.requireNonNull(networkInputForm);
        Objects.requireNonNull(bindingResult);
        if (!bindingResult.hasFieldErrors("ipString")) {
            try {
                new IPAddress(networkInputForm.getIpString());
            } catch (IllegalArgumentException e) {
                bindingResult.rejectValue("ipString", "invalid.ipString", "Ungültiges Format!");
            }
        }
        if (!bindingResult.hasFieldErrors("shortsnm")) {
            try {
                new Subnetmask(networkInputForm.getSnmString());
            } catch (IllegalArgumentException e) {
                bindingResult.rejectValue("shortsnm", "invalid.shortsnm", "Kürzel ungültig!");
            } catch (IllegalStateException e) {
                bindingResult.rejectValue("snmString", "invalid.snmString", "Sie bauen kein kommunikatives Netz auf!");
            }
        }
        if (!bindingResult.hasFieldErrors("snmString")) {
            try {
                new Subnetmask(networkInputForm.getSnmString());
            } catch (IllegalArgumentException e) {
                bindingResult.rejectValue("snmString", "invalid.snmString", "Ungültig!");
            } catch (IllegalStateException e) {
                bindingResult.rejectValue("snmString", "invalid.snmString", "Sie bauen kein kommunikatives Netz auf!");
            }
        }
    }

            private static IPAddress calculateNetID (IPAddress ip, Subnetmask snm){
                Objects.requireNonNull(ip);
                Objects.requireNonNull(snm);
                return ip.logicalAnd(snm);
            }

            public static IPAddress calculateBroadcastIp (IPAddress netId, Subnetmask subnetmask){
                Objects.requireNonNull(netId);
                Objects.requireNonNull(subnetmask);
                IPAddress invertedSnm = subnetmask.invert();
                int first = netId.getFirst() + invertedSnm.getFirst();
                int second = netId.getSecond() + invertedSnm.getSecond();
                int third = netId.getThird() + invertedSnm.getThird();
                int fourth = netId.getFourth() + invertedSnm.getFourth();
                return new IPAddress(first, second, third, fourth);
            }
        }