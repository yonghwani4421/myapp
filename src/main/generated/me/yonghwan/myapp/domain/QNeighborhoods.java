package me.yonghwan.myapp.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNeighborhoods is a Querydsl query type for Neighborhoods
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNeighborhoods extends EntityPathBase<Neighborhoods> {

    private static final long serialVersionUID = 191142881L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNeighborhoods neighborhoods = new QNeighborhoods("neighborhoods");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath city = createString("city");

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final QMember member;

    public final StringPath placeName = createString("placeName");

    public final StringPath zipCode = createString("zipCode");

    public QNeighborhoods(String variable) {
        this(Neighborhoods.class, forVariable(variable), INITS);
    }

    public QNeighborhoods(Path<? extends Neighborhoods> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNeighborhoods(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNeighborhoods(PathMetadata metadata, PathInits inits) {
        this(Neighborhoods.class, metadata, inits);
    }

    public QNeighborhoods(Class<? extends Neighborhoods> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

