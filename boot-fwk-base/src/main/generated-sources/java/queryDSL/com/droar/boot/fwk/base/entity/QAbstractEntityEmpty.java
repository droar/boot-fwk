package com.droar.boot.fwk.base.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractEntityEmpty is a Querydsl query type for AbstractEntityEmpty
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractEntityEmpty extends EntityPathBase<AbstractEntityEmpty> {

    private static final long serialVersionUID = -406475789L;

    public static final QAbstractEntityEmpty abstractEntityEmpty = new QAbstractEntityEmpty("abstractEntityEmpty");

    public QAbstractEntityEmpty(String variable) {
        super(AbstractEntityEmpty.class, forVariable(variable));
    }

    public QAbstractEntityEmpty(Path<? extends AbstractEntityEmpty> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractEntityEmpty(PathMetadata metadata) {
        super(AbstractEntityEmpty.class, metadata);
    }

}

