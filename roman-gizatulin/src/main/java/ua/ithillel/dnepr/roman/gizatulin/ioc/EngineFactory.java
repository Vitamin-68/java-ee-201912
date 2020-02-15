package ua.ithillel.dnepr.roman.gizatulin.ioc;

public final class EngineFactory {
    public static Engine createRenoEngine() {
        Engine engine = new RenoEngine();
        // setup engine
        return engine;
    }
}
