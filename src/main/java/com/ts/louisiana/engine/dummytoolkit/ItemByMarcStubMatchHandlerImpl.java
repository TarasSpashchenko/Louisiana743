package com.ts.louisiana.engine.dummytoolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.MatchConditionToQueryCriteriaConverter;
import com.ts.louisiana.engine.api.MatchHandler;
import com.ts.louisiana.engine.api.EntityRepository;
import com.ts.louisiana.metadata.MatchCriteria;
import com.ts.louisiana.types.ItemEntity;
import com.ts.louisiana.types.MarcEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("test")
@Slf4j
public class ItemByMarcStubMatchHandlerImpl implements MatchHandler<MarcEntity, ItemEntity> {
    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private MatchConditionToQueryCriteriaConverter<MarcEntity> matchConditionToQueryCriteriaConverter;

    @Override
    public Task<ItemEntity> match(final MatchCriteria matchCriteria, final MarcEntity contextObject) {
        return null;
//        return
//                matchConditionToQueryCriteriaConverter.convertMatchConditionToQueryCriteria(matchCriteria, contextObject)
//                        .flatMap("MatchHandler match", queryCriteria -> entityRepository.findItemBy(queryCriteria));
    }
}
