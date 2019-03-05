package com.ts.louisiana.metadata;

import com.ts.louisiana.engine.api.ProfileVisitor;
import com.ts.louisiana.metadata.api.Job;

public class JobImpl extends NodeImpl implements Job {
    public JobImpl() {
        super();
    }

    public JobImpl(String name) {
        super(name);
    }

    @Override
    public void accept(ProfileVisitor visitor) {
        visitor.visitJob(this);
    }
}
