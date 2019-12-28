package ua.ithillel.dnepr.common.repository;

import java.io.Serializable;

public interface BaseEntity<IdType> extends Serializable {
    IdType getId();
}
