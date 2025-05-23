create table aircraft
(
    aircraft_id   int auto_increment
        primary key,
    name          varchar(255) null,
    seat_capacity int          null
);

create table city
(
    city_id int auto_increment
        primary key,
    name    varchar(255) null
);

create table passenger
(
    passenger_id int auto_increment
        primary key,
    name         varchar(255) null
);

create table booking
(
    booking_id          int auto_increment
        primary key,
    booking_code        varchar(255)                 null,
    booking_state       enum ('BOOKED', 'CANCELLED') null,
    flight_type         enum ('DIRECT', 'TRANSIT')   null,
    departure_city_id   int                          null,
    destination_city_id int                          null,
    passenger_id        int                          null,
    constraint FKabxd6qpdfkp11yan46jj1td90
        foreign key (passenger_id) references passenger (passenger_id),
    constraint FKf36g26pp5hnfdkeruhxnpso9a
        foreign key (departure_city_id) references city (city_id),
    constraint FKlj91fuux8syoftg36dnc4njw2
        foreign key (destination_city_id) references city (city_id)
);

create table route
(
    route_id            int auto_increment
        primary key,
    schedule_day        int                                       null,
    aircraft_id         int                                       not null,
    departure_city_id   int                                       not null,
    destination_city_id int                                       not null,
    route_state         enum ('ARRIVED', 'DEPARTED', 'SCHEDULED') null,
    constraint FK1gdlk33i1s5p2j1nx8jrdi56p
        foreign key (destination_city_id) references city (city_id),
    constraint FKhmxsignt10g676bwrwd04kyb0
        foreign key (aircraft_id) references aircraft (aircraft_id),
    constraint FKjw6vdi6233wt56ea4ci1xkgqc
        foreign key (departure_city_id) references city (city_id)
);

create table booking_detail
(
    booking_detail_id int auto_increment
        primary key,
    seat_number       int null,
    booking_id        int not null,
    route_id          int not null,
    constraint FK59941ondg9nwaifm2umnrduev
        foreign key (booking_id) references booking (booking_id),
    constraint FKb2pi78gpeg3q6vrubndnxqeif
        foreign key (route_id) references route (route_id)
);

create table system_operational
(
    id              int auto_increment
        primary key,
    operational_day int                                 not null,
    state           enum ('PENDING', 'RUNNING', 'STOP') null,
    timestamps      datetime(6)                         null
);
