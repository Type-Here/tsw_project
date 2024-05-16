drop database if exists retrogamer;
create database retrogamer;
use retrogamer;

-- Tabella Prodotti
create table products (
    id_prod int not null auto_increment,
    name varchar(255) not null,
    price double not null, 
    type boolean not null, -- 0 Fisico - 1 Digitale
    platform varchar(255) not null,
    developer varchar(255) not null,
    description varchar(2047) not null,
    metadata varchar(2047),
    `key` varchar(15),
    discount double,
    primary key (id_prod)
);

-- Tabella Quantità per condizione
create table prod_quantity (
    id_prod int not null,
    id_cond int not null,
    quantity int not null,
    foreign key(id_prod) references products(id_prod),
    primary key(id_prod, id_cond)
);

-- Tabella Categorie
create table categories (
    id_cat int not null auto_increment,
    typename varchar(255) not null,
    primary key(id_cat)
);

-- Tabella che attribuisce ad ogni gioco 1 o più categorie di cui fa parte
create table prod_categories (
    id_cat int not null,
    id_prod int not null,
    foreign key(id_prod) references products(id_prod),
    foreign key(id_cat) references categories(id_cat),
    primary key(id_cat, id_prod)
);

-- Tabella Credenziali, gestisce solo le credenziali di accesso di utenti
create table credentials (
    id_cred int not null auto_increment, 
    pass_hash varchar(1024) not null,
    pass_salt varchar(127) not null,
    creation_date date not null,
    primary key(id_cred)
);


-- Tabella Dati Utente
create table users (
    id_client int not null auto_increment,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    telephone varchar(31) not null,
    email varchar(255) not null unique, -- Email unica per utente
    birth date not null,
    address varchar(255) not null,
    city varchar(255) not null,
    prov varchar(2) not null,
    cap varchar(5) not null,
    id_cred int not null,
    foreign key(id_cred) references credentials(id_cred),
    primary key(id_client) 
);


-- Tabella con Indirizzi di Spedizione /* TODO Aggiungere a Documentazione */
create table shipping_addresses(
    id_client int not null,
    id_add int not null auto_increment,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    address varchar(255) not null,
    city varchar(255) not null,
    prov varchar(2) not null,
    cap varchar(5) not null,
    foreign key(id_client) references users(id_client),
    primary key(id_add)
);


-- Tabella Carrello
create table cart(
    id_cart int not null auto_increment,
    total double not null,
    id_client int not null,
    discount_code varchar(15),
    foreign key (id_client) references users(id_client),
    primary key (id_cart)
);


-- Tabella Item del Carrello
create table cart_items (
    id_prod int not null,
    id_cart int not null,
    quantity int not null,
    real_price double not null,
    foreign key (id_cart) references cart(id_cart),
    primary key (id_prod, id_cart)
);


-- Tabella Ordini /*TODO Modificare Documentazione*/
create table orders (
    id_cart int not null,
    id_client int not null,
    id_add int not null,
    status enum('in process','shipped','delivering','delivered','refunded','canceled') not null,
    order_date date not null,
    foreign key(id_cart) references cart(id_cart),
    foreign key(id_client, id_add) references shipping_addresses(id_client, id_add),
    primary key(id_cart)
);


-- Tabella Recensioni /*TODO Add to Database */
create table reviews(
    id_review int not null auto_increment, 
    voto int not null, 
    commento varchar(2047), 
    review_date date not null, 
    id_prod int not null, 
    id_client int not null,
    foreign key(id_prod) references products(id_prod),
    foreign key(id_client) references users(id_client),
    primary key(id_review) 
);

-- Tabella Admin
create table admin(
    user varchar(255) primary key,
    hash varchar(255) not null
);