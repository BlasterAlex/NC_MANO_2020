drop table if exists patient_symptom;
drop table if exists patient_medic;
drop table if exists patient;
drop table if exists symptom;
drop table if exists medic;

create table patient
(
    patient_id            serial primary key,
    surname               varchar not null,
    name                  varchar not null,
    middle_name           varchar,
    is_having_trip_abroad varchar(5) default 'false',
    contact_with_patients varchar(5) default 'false'
);

create table symptom
(
    symptom_id       serial primary key,
    name             varchar not null,
    is_covid_symptom varchar(5) default 'false'
);

create table medic
(
    medic_id    serial primary key,
    surname     varchar not null,
    name        varchar not null,
    middle_name varchar,
    position    varchar not null,
    category    int     not null
);

create table patient_symptom
(
    patient_id int references patient (patient_id) on update cascade on delete cascade,
    symptom_id int references symptom (symptom_id) on update cascade on delete cascade,
    constraint patient_symptom_pkey primary key (patient_id, symptom_id)
);

create table patient_medic
(
    patient_id int references patient (patient_id) on update cascade on delete cascade,
    medic_id   int references medic (medic_id) on update cascade on delete cascade,
    constraint patient_medic_pkey primary key (patient_id, medic_id)
);
