package com.ts.louisiana.engine.toolkit;

import com.linkedin.parseq.Task;
import com.ts.louisiana.engine.api.JobHandler;
import com.ts.louisiana.metadata.Node;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class JobHandlerImpl implements JobHandler {

    @Override
    public Task<Void> composeChildTasks(List<Pair<Node, Task<Void>>> childTasks) {
        return childTasks
                .stream()
                .reduce(Pair.of(null, Task.action("Job Profile init", () -> log.warn("Job Profile init Task Created!"))), (p1, p2) -> Pair.of(p2.getLeft(), p1.getRight().andThen(p2.getLeft().getName(), p2.getRight())))
                .getValue();
    }
}
