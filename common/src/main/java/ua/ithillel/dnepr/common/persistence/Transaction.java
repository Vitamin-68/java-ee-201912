package ua.ithillel.dnepr.common.persistence;

public interface Transaction {
    void begin();

    void commit();

    void rollback();
}
