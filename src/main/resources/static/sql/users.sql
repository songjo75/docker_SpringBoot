-- -- root 계정에서 --------
create database boot;
create user 'boot'@'localhost' identified by '1212';
flush privileges;

grant all privileges on boot.* to 'boot'@'localhost';

-- -- boot 계정에서 ---------
create table users(
    userId 		bigint auto_increment primary key,
    username 	varchar(50) not null,
    password 	varchar(255) not null,
    email 		varchar(100) unique not null,
    phone		varchar(20) unique not null,
    address		varchar(255),
    createdAt	timestamp default current_timestamp, -- 행추가시 현재시간 자동 설정
    -- on update current_timestamp : 행이 변경될 때마다 자동으로 현재시간으로 갱신
    updatedAt	timestamp default current_timestamp on update current_timestamp
);
