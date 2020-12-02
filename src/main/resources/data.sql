drop table if exists patient_medic;
drop table if exists patient;
drop table if exists medic;

create table patient
(
    patient_id          bigint(19) identity not null,
    surname             varchar(16)         not null,
    name                varchar(16)         not null,
    middleName          varchar(16),
    symptoms            text,
    isHavingTipAbroad   varchar(5) default 'false',
    contactWithPatients varchar(5) default 'false'
);

insert into patient (patient_id, surname, name, middleName, symptoms, isHavingTipAbroad, contactWithPatients)
values (1, 'Козлов', 'Тарас', 'Тихонович', 'Температура, сухой кашель', 'true', 'false'),
       (2, 'Александров', 'Руслан', 'Михаилович', 'Утомляемость', 'false', 'true'),
       (3, 'Ильин', 'Тихон', 'Федотович', 'Температура, головная боль', 'false', 'true'),
       (4, 'Носков', 'Григорий', 'Семенович', 'Сухой кашель, утомляемость', 'true', 'false'),
       (5, 'Федотов', 'Максим', 'Геннадьевич', 'Температура, боль в горле', 'true', 'true'),
       (6, 'Герасимов', 'Ефим', 'Натанович', 'Головная боль', 'false', 'false');

drop table if exists medic;

create table medic
(
    medic_id   bigint(19) identity not null,
    surname    varchar(16)         not null,
    name       varchar(16)         not null,
    middleName varchar(16),
    position   varchar(16)         not null,
    category   int                 not null
);

insert into medic (medic_id, surname, name, middleName, position, category)
values (1, 'Панфилов', 'Георгий', 'Семёнович', 'Инфекционист', 3),
       (2, 'Богданов', 'Влас', 'Иринеевич', 'УЗИ-специалист', 2),
       (3, 'Симонов', 'Клим', 'Игоревич', 'Отоларинголог', 2),
       (4, 'Уварова', 'София', 'Вячеславовна', 'Медсестра', 1),
       (5, 'Шилова', 'Мила', 'Антоновна', 'Кардиолог', 3),
       (6, 'Носкова', 'Наталья', 'Якововна', 'Хирург', 3);

create table patient_medic
(
    patient_id bigint(19) not null,
    medic_id   bigint(19) not null,
    constraint fk_patient foreign key (patient_id) references patient (patient_id)
        on delete cascade
        on update cascade,
    constraint fk_medic foreign key (medic_id) references medic (medic_id)
);

insert into patient_medic (patient_id, medic_id)
values (1, 2),
       (1, 3),
       (2, 2),
       (3, 5),
       (3, 4),
       (4, 2),
       (4, 3),
       (4, 4),
       (5, 1),
       (5, 2),
       (6, 5),
       (6, 6);

