package me.yonghwan.myapp.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardAttachment is a Querydsl query type for BoardAttachment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardAttachment extends EntityPathBase<BoardAttachment> {

    private static final long serialVersionUID = -1635672475L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardAttachment boardAttachment = new QBoardAttachment("boardAttachment");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath attachmentName = createString("attachmentName");

    public final NumberPath<Long> attachmentSize = createNumber("attachmentSize", Long.class);

    public final StringPath attachmentType = createString("attachmentType");

    public final StringPath attachmentUrl = createString("attachmentUrl");

    public final QBoard board;

    //inherited
    public final StringPath createBy = _super.createBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final StringPath lastModifiedBy = _super.lastModifiedBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public QBoardAttachment(String variable) {
        this(BoardAttachment.class, forVariable(variable), INITS);
    }

    public QBoardAttachment(Path<? extends BoardAttachment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardAttachment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardAttachment(PathMetadata metadata, PathInits inits) {
        this(BoardAttachment.class, metadata, inits);
    }

    public QBoardAttachment(Class<? extends BoardAttachment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
    }

}

