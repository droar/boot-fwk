package com.droar.boot.fwk.base.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractAudit is a Querydsl query type for AbstractAudit
 */
@Generated("com.querydsl.codegen.EmbeddableSerializer")
public class QAbstractAudit extends BeanPath<AbstractAudit> {

    private static final long serialVersionUID = -1773065372L;

    public static final QAbstractAudit abstractAudit = new QAbstractAudit("abstractAudit");

    public final NumberPath<Integer> deleted = createNumber("deleted", Integer.class);

    public final DateTimePath<java.util.Date> insertDate = createDateTime("insertDate", java.util.Date.class);

    public final StringPath owner = createString("owner");

    public final DateTimePath<java.util.Date> updateDate = createDateTime("updateDate", java.util.Date.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QAbstractAudit(String variable) {
        super(AbstractAudit.class, forVariable(variable));
    }

    public QAbstractAudit(Path<? extends AbstractAudit> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractAudit(PathMetadata metadata) {
        super(AbstractAudit.class, metadata);
    }

}

