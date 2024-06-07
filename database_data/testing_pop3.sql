insert into reviews (voto, commento, review_date, id_prod, id_client) values
(4,'Meraviglioso','2024-03-12',1,1);

insert into cart (total, id_client) values
(90.00, 1),
(138.00, 1),
(416.90, 1),
(25.90, 1),
(25.90, 2);

insert into cart_items (id_prod, id_cart, quantity, real_price, refund, `condition`) VALUES
(1, 1, 3, 30.00, 1, 1),
(1, 2, 1, 30, 1, 1),
(2, 2, 1, 38.00, 1,1),
(3, 2, 1, 70.00, 1,1),
(5, 3, 1, 17.90, 1,1),
(4, 3, 1, 399.00, 1,1),
(7, 4, 1, 25.90, 1,1),
(6, 5, 1, 25.90, 1,1);

insert into orders (id_cart, id_client, id_add, status, order_date) values
(1, 1, 1, 'in process', '2024-05-20'),
(2, 1, 1, 'shipped', '2024-05-30'),
(3, 1, 1, 'delivering', '2024-06-01'),
(4, 1, 2, 'delivered', '2024-06-1'),
(5, 2, 3, 'shipped', '2024-06-1');