package org.sopt.confeti.domain.view.performance.infra.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

@Repository
public class PerformanceCriteriaRepository_DPRECATED {

    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_TYPE_ID = "typeId";
    private static final String COLUMN_END_AT = "endAt";

    @PersistenceContext
    private EntityManager em;

    public List<Performance_DPRECATED> findPerformancesByTypeAndTypeId(List<Pair<PerformanceType, Long>> pairs) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Performance_DPRECATED> cq = cb.createQuery(Performance_DPRECATED.class);
        Root<Performance_DPRECATED> root = cq.from(Performance_DPRECATED.class);

        // endAt >= CURRENT_DATE
        Predicate endAtPredicate = cb.greaterThanOrEqualTo(root.get(COLUMN_END_AT), LocalDate.now());

        List<Predicate> orPredicates = new ArrayList<>();

        // (type, typeId) IN (("concert", 1), ...)
        pairs.forEach(pair -> {
            Predicate predicate = cb.and(
                    cb.equal(root.get(COLUMN_TYPE), pair.getFirst()),
                    cb.equal(root.get(COLUMN_TYPE_ID), pair.getSecond())
            );
            orPredicates.add(predicate);
        });

        cq.where(
                cb.and(
                        endAtPredicate,
                        cb.or(orPredicates.toArray(new Predicate[0]))
                )
        );

        return em.createQuery(cq).getResultList();
    }
}
