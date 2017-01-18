
    create index source_index on relationship (tenantId, sourceType, sourceId);

    create index target_index on relationship (tenantId, targetType, targetId);
