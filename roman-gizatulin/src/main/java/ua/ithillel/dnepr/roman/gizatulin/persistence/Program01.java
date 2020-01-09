package ua.ithillel.dnepr.roman.gizatulin.persistence;

import ua.ithillel.dnepr.common.persistence.CriteriaBuilder;
import ua.ithillel.dnepr.common.persistence.CriteriaQuery;
import ua.ithillel.dnepr.common.persistence.EntityManager;
import ua.ithillel.dnepr.common.persistence.Root;
import ua.ithillel.dnepr.roman.gizatulin.repository.entity.City;

public class Program01 {
    public static void main(String[] args) {
        EntityManager entityManager = null;
        CriteriaBuilder<Integer, City> criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Integer, City> cityCriteriaQuery = criteriaBuilder.createQuery(City.class);

        Root<Integer, City> cityRoot = cityCriteriaQuery.from(City.class);
//        cityCriteriaQuery.select(cityRoot);

//        CriteriaQuery<StudentEntity> select1 = ((CriteriaQuery<StudentEntity>) cq1).select(stud1);
        //cityAbstractQuery
        //.where(cb.greaterThan(stud1.get("s_age"), 22));
    }
}
