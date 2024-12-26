package com.lotteon.entity.member;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {

    private static final long serialVersionUID = -1633532799L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomer customer = new QCustomer("customer");

    public final QAttendanceEvent attendanceEvent;

    public final StringPath custAddr = createString("custAddr");

    public final BooleanPath custAddrOption = createBoolean("custAddrOption");

    public final StringPath custBirth = createString("custBirth");

    public final StringPath custEmail = createString("custEmail");

    public final NumberPath<Integer> custEventChecker = createNumber("custEventChecker", Integer.class);

    public final BooleanPath custGender = createBoolean("custGender");

    public final StringPath custGrade = createString("custGrade");

    public final StringPath custHp = createString("custHp");

    public final StringPath custName = createString("custName");

    public final NumberPath<Integer> custPoint = createNumber("custPoint", Integer.class);

    public final BooleanPath custTermOption = createBoolean("custTermOption");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final ListPath<com.lotteon.entity.point.Point, com.lotteon.entity.point.QPoint> points = this.<com.lotteon.entity.point.Point, com.lotteon.entity.point.QPoint>createList("points", com.lotteon.entity.point.Point.class, com.lotteon.entity.point.QPoint.class, PathInits.DIRECT2);

    public QCustomer(String variable) {
        this(Customer.class, forVariable(variable), INITS);
    }

    public QCustomer(Path<? extends Customer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomer(PathMetadata metadata, PathInits inits) {
        this(Customer.class, metadata, inits);
    }

    public QCustomer(Class<? extends Customer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attendanceEvent = inits.isInitialized("attendanceEvent") ? new QAttendanceEvent(forProperty("attendanceEvent"), inits.get("attendanceEvent")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

