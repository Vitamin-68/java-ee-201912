package ua.ithillel.dnepr.common.repository;

import java.util.List;

public interface IndexedRepository {
    void addIndex(String field);

    void addIndexes(List<String> fields);
}
