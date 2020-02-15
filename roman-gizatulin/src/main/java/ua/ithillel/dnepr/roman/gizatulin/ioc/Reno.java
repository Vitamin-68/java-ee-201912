package ua.ithillel.dnepr.roman.gizatulin.ioc;

public class Reno implements Car, EngineInjector {
    private Engine engine;

    public Reno() {
    }

    public Reno(Engine engine) {
        this.engine = engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String run(String speed) {
        engine.dirDirDir("2");
        return "reno dirDirDir";
    }

    @Override
    public void addEngine(Engine engine) {
        this.engine = engine;
    }
}
