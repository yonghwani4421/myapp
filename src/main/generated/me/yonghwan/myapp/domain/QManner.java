package me.yonghwan.myapp.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QManner is a Querydsl query type for Manner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QManner extends EntityPathBase<Manner> {

    private static final long serialVersionUID = -1298773819L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QManner manner = new QManner("manner");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final QMember giver;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final QMember receiver;

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public QManner(String variable) {
        this(Manner.class, forVariable(variable), INITS);
    }

    public QManner(Path<? extends Manner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QManner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QManner(PathMetadata metadata, PathInits inits) {
        this(Manner.class, metadata, inits);
    }

    public QManner(Class<? extends Manner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.giver = inits.isInitialized("giver") ? new QMember(forProperty("giver"), inits.get("giver")) : null;
        this.receiver = inits.isInitialized("receiver") ? new QMember(forProperty("receiver"), inits.get("receiver")) : null;
    }

}

