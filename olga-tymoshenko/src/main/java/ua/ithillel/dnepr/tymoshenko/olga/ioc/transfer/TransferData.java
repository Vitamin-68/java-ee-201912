package ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface TransferData<EntityType extends BaseEntity<IdType>, IdType> {
    void createConnection(String source, String dest);
    void dataSending();
}
