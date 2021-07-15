--insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values(1, 'hong', 'hong@naver.com', now(), now());
--insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values(2, 'a', 'a@naver.com', now(), now());
--insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values(3, 'b', 'b@naver.com', now(), now());
--insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values(4, 'c', 'c@naver.com', now(), now());
--insert into user(`id`, `name`, `email`, `created_at`, `updated_at`) values(5, 'd', 'd@naver.com', now(), now());

insert into publisher(`id`, `name`) values (1, '홍이');
insert into book(`id`, `name`, `publisher_id`) values (1, '홍곰책', 1);
insert into book(`id`, `name`, `publisher_id`) values (2, '홍곰책책책', 1);
