package ua.ithillel.dnepr.roman.gizatulin.persistence;

import org.apache.commons.lang3.StringUtils;
import ua.ithillel.dnepr.common.persistence.CriteriaBuilder;
import ua.ithillel.dnepr.common.persistence.CriteriaQuery;
import ua.ithillel.dnepr.common.persistence.EntityManager;
import ua.ithillel.dnepr.common.persistence.Root;
import ua.ithillel.dnepr.roman.gizatulin.repository.entity.City;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Program01 {
    public static void main(String[] args) throws Exception {
        Map<String, String> params = null;
        for (String arg : args) {
            if (params == null) {
                params = new HashMap<>();
            }
            String[] keyValue = arg.split("=");
            params.put(keyValue[0], keyValue[1]);
        }
        ParamValidatorDecorator paramValidator =
                new ParamValidatorDecoratorEmpty(
                        new ParamValidatorDecoratorSource(
                                new ParamValidatorDecoratorDest(new ParamValidator())
                        )
                );
        paramValidator.validate(params);

        EntityManager entityManager = null;
        CriteriaBuilder<Integer, City> criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer, City> cityCriteriaQuery = criteriaBuilder.createQuery(City.class);

        Root<Integer, City> cityRoot = cityCriteriaQuery.from(City.class);
//        cityCriteriaQuery.select(cityRoot);

//        CriteriaQuery<StudentEntity> select1 = ((CriteriaQuery<StudentEntity>) cq1).select(stud1);
        //cityAbstractQuery
        //.where(cb.greaterThan(stud1.get("s_age"), 22));
    }

    private static void validateParams(Map<String, String> params) throws IllegalAccessException {
        Objects.requireNonNull(params, "");
        if (params.isEmpty()) {
            throw new IllegalAccessException("sdgfhdfgh");
        }
        //if(params.containsKey(""))
    }
}


class ParamValidator {
    public void validate(Map<String, String> params) throws Exception {
        Objects.requireNonNull(params);
    }
}

abstract class ParamValidatorDecorator extends ParamValidator {
    private final ParamValidator validatorDecorator;

    public ParamValidatorDecorator(ParamValidator validatorDecorator) {
        this.validatorDecorator = validatorDecorator;
    }

    @Override
    public void validate(Map<String, String> params) throws Exception {
        validatorDecorator.validate(params);
    }
}

class ParamValidatorDecoratorEmpty extends ParamValidatorDecorator {
    public ParamValidatorDecoratorEmpty(ParamValidator validatorDecorator) {
        super(validatorDecorator);
    }

    @Override
    public void validate(Map<String, String> params) throws Exception {
        super.validate(params);
        if (params.isEmpty()) {
            throw new IllegalAccessException("sdfhgdfg");
        }
    }
}

class ParamValidatorDecoratorSource extends ParamValidatorDecorator {
    public ParamValidatorDecoratorSource(ParamValidator validatorDecorator) {
        super(validatorDecorator);
    }

    @Override
    public void validate(Map<String, String> params) throws Exception {
        super.validate(params);
        if (!params.containsKey("source-type")) {
            throw new IllegalAccessException("sdfhgdfg");
        }
        String s = params.get("source-type");
        if (StringUtils.isBlank(s)) {
            throw new IllegalAccessException("sdfhgdfg");
        }
        if (!params.containsKey("source-path")) {
            throw new IllegalAccessException("sdfhgdfg");
        }
        s = params.get("source-path");
        if (StringUtils.isBlank(s)) {
            throw new IllegalAccessException("sdfhgdfg");
        }
    }
}

class ParamValidatorDecoratorDest extends ParamValidatorDecorator {
    public ParamValidatorDecoratorDest(ParamValidator validatorDecorator) {
        super(validatorDecorator);
    }

    @Override
    public void validate(Map<String, String> params) throws Exception {
        super.validate(params);
        if (!params.containsKey("dest-type")) {
            throw new IllegalAccessException("sdfhgdfg");
        }
        String s = params.get("dest-type");
        if (StringUtils.isBlank(s)) {
            throw new IllegalAccessException("sdfhgdfg");
        }
        if (!params.containsKey("dest-path")) {
            throw new IllegalAccessException("sdfhgdfg");
        }
        s = params.get("dest-path");
        if (StringUtils.isBlank(s)) {
            throw new IllegalAccessException("sdfhgdfg");
        }
    }
}