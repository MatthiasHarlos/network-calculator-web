package com.newenergytrading.networkcalculatorweb;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class NetworkController {

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello " + "test" + "test" + "!");
    }

    @GetMapping("calculate")
    public String calculator(Model model, @RequestParam IPAddress iPAddress, @RequestParam Subnetmask snm) {
        model.addAttribute("iPAddress", iPAddress);
        model.addAttribute("snm", snm);
        //editInput();
        IPAddress netID = calculateNetID(iPAddress, snm);
        model.addAttribute("netid", netID);
        IPAddress broadcast = calculateBroadcastIp(netID, snm);
        model.addAttribute("broadcast", broadcast);
        int hosts = snm.getHosts();
        model.addAttribute("hosts", hosts);

        Networks simpleNetwork = new Networks();
        simpleNetwork.setSimpleFirstAndLastIP(netID, broadcast);
        model.addAttribute("firstipandlast", simpleNetwork.getSimpleFirstAndLastIP());
        Networks networks = new Networks(netID, snm, broadcast, hosts);
        model.addAttribute("networks", networks.getNetworks());

        return "networkcalculate-template";
    }

    private static IPAddress calculateNetID(IPAddress ip, Subnetmask snm) {
        Objects.requireNonNull(ip);
        Objects.requireNonNull(snm);
        return ip.logicalAnd(snm);
    }
    public static IPAddress calculateBroadcastIp(IPAddress netId, Subnetmask subnetmask) {
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
