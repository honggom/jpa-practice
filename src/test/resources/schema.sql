create table user(
    id integer auto_increment,
    name varchar(255) not null,
    email varchar(255) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key(id)
);
