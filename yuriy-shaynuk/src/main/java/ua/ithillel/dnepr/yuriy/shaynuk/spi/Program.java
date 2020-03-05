package ua.ithillel.dnepr.yuriy.shaynuk.spi;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        String userName = "User";
        List<HelloService> serviceList = ServiceProvider.getServices();
        serviceList.forEach(helloService -> helloService.sayHello(userName));
    }
}
