use retrogamer;

insert into reviews (voto, commento, review_date, id_prod, id_client) values
(4,'Meraviglioso.','2024-06-12',1,1),
(4,'C''è poco da dire.', '2024-06-12',2,1),
(5,'Il mio preferito OVERHEAD.', '24-06-17',58,1);

insert into cart (total, id_client) values
(90.00, 1),
(138.00, 1),
(416.90, 1),
(25.90, 1),
(25.90, 2),
(1499.95, 3),
(38.40, 4),
(46.98, 5),
(21.99, 6),
(9.99, 7),
(18.99, 8),
(22.70, 9),
(167.99, 1),
(300.30, 1),
(300.30, 1),
(35.00, 1);


insert into cart_items (id_prod, id_cart, quantity, real_price, refund, `condition`) VALUES
(1, 1, 3, 30.00, 1, 1),
(1, 2, 1, 30.00, 1, 1),
(2, 2, 1, 38.00, 1, 1),
(3, 2, 1, 70.00, 1, 1),
(5, 3, 1, 17.90, 1, 1),
(4, 3, 1, 399.00, 1, 1),
(7, 4, 1, 25.90, 1, 1),
(6, 5, 1, 25.90, 1, 1),
(11, 6, 1, 1499.95, 1, 1),
(12, 7, 1, 18.50, 1, 1),
(10, 8, 1, 14.99, 1, 1),
(8, 8, 1, 31.99, 1, 1),
(15, 9, 1, 21.99, 1, 1),
(18, 10, 1, 9.99, 1, 1),
(20, 11, 1, 18.99, 1, 1),
(21, 12, 1, 22.70, 1, 1),
(23, 13, 1, 69.00, 1, 1),
(24, 13, 1, 98.99, 1, 1),
(25, 14, 1, 125.50, 1, 1),
(27, 14, 1, 174.80, 1, 1),
(58, 15, 1, 35.00, 1, 1);

insert into orders (id_cart, id_client, id_add, status, order_date) values
(1, 1, 1, 'in process', '2024-05-20'),
(2, 1, 1, 'shipped', '2024-05-30'),
(3, 1, 1, 'delivering', '2024-06-01'),
(4, 1, 2, 'delivered', '2024-06-1'),
(5, 2, 3, 'shipped', '2024-06-1'),
(6, 3, 4, 'delivered', '2024-06-1'),
(7, 4, 5, 'in process', '2024-06-1'),
(8, 5, 6, 'shipped', '2024-06-1'),
(9, 6, 7, 'delivering', '2024-06-1'),
(10, 7, 8, 'delivered', '2024-06-1'),
(11, 8, 9, 'in process', '2024-06-1'),
(12, 9, 10, 'shipped', '2024-06-1'),
(13, 1, 1, 'delivered', '2024-06-1'),
(14, 1, 1, 'delivered', '2024-06-1'),
(15, 1, 1, 'delivered', '2024-06-1');