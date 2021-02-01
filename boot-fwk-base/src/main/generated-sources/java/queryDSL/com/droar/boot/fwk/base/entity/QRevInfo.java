package com.droar.boot.fwk.base.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QRevInfo is a Querydsl query type for RevInfo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRevInfo extends EntityPathBase<RevInfo> {

    private static final long serialVersionUID = -1834961220L;

    public static final QRevInfo revInfo = new QRevInfo("revInfo");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> revtstmp = createDateTime("revtstmp", java.util.Date.class);

    public QRevInfo(String variable) {
        super(RevInfo.class, forVariable(variable));
    }

    public QRevInfo(Path<? extends RevInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRevInfo(PathMetadata metadata) {
        super(RevInfo.class, metadata);
    }

}

