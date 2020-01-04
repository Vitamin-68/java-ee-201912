package repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

@Slf4j
public abstract class BaseFileRepository {
    protected final String repoRootPath;
    public static final String REGION_ID = "region_id";
    public static final String CITY_ID = "city_id";
    public static final String COUNTRY_ID = "country_id";
    public static final String NAME = "name";
    public static final char delimiter = ';';

    protected  BaseFileRepository(String repoRootPath){
        this.repoRootPath = repoRootPath;
    }
}
