package ua.ithillel.dnepr.tymoshenko.olga.ioc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer.TransferDataToCvsImpl;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer.TransferDataToH2Impl;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FactoryTransferData {
    @Autowired
    TransferDataToH2Impl transferToBase;

    @Autowired
    TransferDataToCvsImpl transferToFile;

    private List<String> list = new ArrayList<>();

    public FactoryTransferData(String source, String dest) {
        list.add(source);
        list.add(dest);
    }

    public TransferDataToH2Impl getTransferToBase() {
        transferToBase.createConnection(list.get(0), list.get(1));
        return transferToBase;
    }

    public void setTransferToBase(TransferDataToH2Impl transferData) {
        transferToBase = transferData;
    }

    public TransferDataToCvsImpl getTransferToFile() {
        transferToFile.createConnection(list.get(0), list.get(1));
        return transferToFile;
    }

    public void setTransferToFile(TransferDataToCvsImpl transferData) {
        transferToFile = transferData;
    }
}


