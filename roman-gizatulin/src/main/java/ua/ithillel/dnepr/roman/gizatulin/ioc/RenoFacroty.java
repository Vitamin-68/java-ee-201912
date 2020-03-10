package ua.ithillel.dnepr.roman.gizatulin.ioc;

public class RenoFacroty implements CarFacroty {
    @Override
    public Car createCar() {
        return new Reno(createEngine());
    }

    @Override
    public Engine createEngine() {
        return new RenoEngine();
    }
}
