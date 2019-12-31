



DROP DATABASE "TestDB";

CREATE DATABASE TestDB;

-- delete all tables
DROP TABLE PUBLIC."event",
  PUBLIC."news",
  PUBLIC."user_activity",
  PUBLIC."user_personal_data",
  PUBLIC."user_role",
  PUBLIC."user_system",
  PUBLIC."user_system_validation_hash";

-- /delete all tables


--insert news

insert into public."news" ("context", "date", "title", "user_activity_fk_id")
values ('Text1', NOW(), 'Title1', 19);


--  /insert news


--select all system_users , firstname and lastname and role

SELECT "email", "first_name", "last_name", "user_role"."name"
FROM public."user_system"
JOIN public."user_personal_data" ON "user_personal_data"."id"="user_system"."user_personal_data_fk_id"
JOIN public."user_role" ON "user_role"."id"="user_system"."user_role_fk_id"
LIMIT 100;

--select all system_users , firstname and lastname