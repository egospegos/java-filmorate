DROP TABLE IF EXISTS FRIENDSHIP;
DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS FILM_GENRE;
DROP TABLE IF EXISTS GENRE;
DROP TABLE IF EXISTS FILM;
DROP TABLE IF EXISTS MPA;

CREATE TABLE IF NOT EXISTS USERS (
	id serial NOT NULL PRIMARY KEY,
	email varchar NOT NULL,
	login varchar NOT NULL,
	name varchar,
	birthday date
);

CREATE TABLE IF NOT EXISTS MPA (
	id serial NOT NULL PRIMARY KEY,
	name varchar NOT NULL
);


CREATE TABLE IF NOT EXISTS film (
	id serial NOT NULL PRIMARY KEY,
	name varchar NOT NULL,
	description varchar,
	release_date date,
	duration int,
	id_MPA int NOT NULL REFERENCES MPA(id)
);

CREATE TABLE IF NOT EXISTS LIKES (
	--id serial NOT NULL PRIMARY KEY,
	--id_film int NOT NULL REFERENCES FILM(id),
	--id_user int NOT NULL REFERENCES USERS(id)
	id_film int REFERENCES film (id),
    id_user int REFERENCES users (id),
    PRIMARY KEY (id_film, id_user)

    --id_film int NOT NULL,
    --id_user int NOT NULL,
    --constraint pk_id primary key (id_film, id_user),
    --constraint fk_film foreign key (id_film) references film (id),
    --constraint fk_property foreign key (id_user) references users (id)
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP (
	--id serial NOT NULL PRIMARY KEY,
	--id_user int NOT NULL REFERENCES USERS(id),
	--id_friend int NOT NULL REFERENCES USERS(id)

	id_user int REFERENCES users (id),
    id_friend int REFERENCES users (id),
    PRIMARY KEY (id_user, id_friend)

	--id_user int NOT NULL,
    --id_friend int NOT NULL,
    --constraint pk_id primary key (id_user, id_friend),
    --constraint fk_user foreign key (id_user) references users (id),
    --constraint fk_friend foreign key (id_friend) references users (id)
);

CREATE TABLE IF NOT EXISTS GENRE (
	id serial NOT NULL PRIMARY KEY,
	name varchar NOT NULL
);


CREATE TABLE IF NOT EXISTS FILM_GENRE (
	--id serial NOT NULL PRIMARY KEY,
	--id_film int NOT NULL REFERENCES FILM(id),
	--id_genre int NOT NULL REFERENCES GENRE(id)
	id_film int REFERENCES film (id),
    id_genre int REFERENCES genre (id),
    PRIMARY KEY (id_film, id_genre)

	--id_film int NOT NULL,
    --id_genre int NOT NULL,
    --constraint pk_id primary key (id_film, id_genre),
    --constraint fk_film foreign key (id_film) references film (id),
    --constraint fk_genre foreign key (id_genre) references genre (id)
);


