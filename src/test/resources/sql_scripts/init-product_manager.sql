drop table if exists category CASCADE;

drop table if exists product CASCADE;

drop table if exists role CASCADE;

drop table if exists pm_user CASCADE;

drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;


create table category (
    id bigint not null,
    name varchar(255),
    primary key (id)
);

create table product (
    id bigint not null,
    created_date timestamp,
    description varchar(255),
    name varchar(255),
    price decimal(19,2),
    updated_date timestamp,
    category_id bigint,
    created_by bigint,
    updated_by bigint,
    primary key (id)
);

create table role (
    id bigint not null,
    name varchar(255),
    primary key (id)
);

create table pm_user (
    id bigint not null,
    active boolean,
    created_date timestamp,
    email varchar(255),
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    role_id bigint,
    primary key (id)
);

alter table product 
    add constraint FK1mtsbur82frn64de7balymq9s 
    foreign key (category_id) 
    references category;

alter table product 
    add constraint FKtnjhc9s7k2hp0e9iyhebd8sn9 
    foreign key (created_by) 
    references pm_user;

alter table product 
    add constraint FKorj7g4xxns1u2bqfkiw8xqcov 
    foreign key (updated_by) 
    references pm_user;

alter table pm_user 
    add constraint FKn82ha3ccdebhokx3a8fgdqeyy 
    foreign key (role_id) 
    references role;

INSERT INTO `role` VALUES (1,'ADMIN'),(2,'USER');

INSERT INTO `pm_user`(id, active, created_date, email, first_name, last_name, password, role_id) VALUES (3,true,'2022-08-03 13:03:32','admin@localhost.com','Admin','Admin','$2a$10$n4tVf1F/PE9E.bFlDDikBuSAH9hDa5V5uqqwBYTyoj8BIsFwPEhJy',1)
,(4,true,'2022-08-03 13:03:32','user@localhost.com','User','User','$2a$10$BP3dMG0OyXrUVgE0lj/aaeZKlo4bS5rI.97I.P/dv69znZyeykqwG',2);

UPDATE `role` SET name='ROLE_ADMIN' WHERE id=1;
UPDATE `role` SET name='ROLE_USER' WHERE id=2;