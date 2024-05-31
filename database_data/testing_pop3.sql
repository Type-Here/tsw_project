insert into reviews (voto, commento, review_date, id_prod, id_client) values
(4,'Meraviglioso','2024-03-12',1,1);

insert into cart_items (id_prod, id_cart, quantity, real_price) VALUES
(1, 1, 3, 30.00);

insert into cart (total, id_client) values
(90.00, 1);

insert into orders (id_cart, id_client, id_add, status, order_date) values
(1, 1, 1, 'in process', '2024-03-12');