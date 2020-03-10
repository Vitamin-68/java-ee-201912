package ua.ithillel.dnepr.roman.gizatulin.ioc;

public class Program {
    public static void main(String[] args) {
        // Init
        //CarFacroty carFacroty = new RenoFacroty();
        ServiceLocator.register(Engine.class, new RenoEngine());

        Car car = new Reno(ServiceLocator.getInstance(Engine.class));
        ServiceLocator.register(Car.class, car);

        EngineInjector engineInjector = new Reno();
        engineInjector.addEngine(ServiceLocator.getInstance(Engine.class));

        // Using
        Car renoCar = ServiceLocator.getInstance(Car.class);
        renoCar.run("240");
        Engine engine = ServiceLocator.getInstance(Engine.class);
    }
}
