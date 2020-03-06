package ua.ithillel.dnepr.yuriy.shaynuk.spi.impl;

import ua.ithillel.dnepr.yuriy.shaynuk.spi.HelloService;

public class UkranianHello implements HelloService {
    @Override
    public void sayHello(String name) {
        System.out.println("Вітаю " +name);
    }
}
