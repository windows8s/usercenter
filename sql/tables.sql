-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '用户id'
        primary key,
    username     varchar(16)       null comment '用户昵称',
    userAccount  varchar(16)       null comment '账号',
    avatarUrl    varchar(1024)     null comment '头像',
    gender       tinyint           null comment '性别',
    userPassword varchar(255)      null comment '密码',
    phone        varchar(255)      null comment '手机号码',
    email        varchar(512)      null comment '邮箱',
    userStatus   tinyint default 0 null comment '状态',
    createTime   datetime          null on update CURRENT_TIMESTAMP comment '创建时间',
    updateTime   datetime          null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint default 0 not null comment '是否删除',
    userRole     int     default 0 not null comment '用户角色 0 - 普通用户 1 - 管理员',
    planetCode   varchar(512)      not null comment '星球编号'
) comment '用户中心';

