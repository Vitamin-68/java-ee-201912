package ua.ithillel.dnepr.yuriy.shaynuk.spi.impl;

import ua.ithillel.dnepr.yuriy.shaynuk.spi.HelloService;

public class RussianHello implements HelloService {
    @Override
    public void sayHello(String name) {
        System.out.println("Привет " +name);
    }
}
