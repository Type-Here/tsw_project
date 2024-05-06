-- Testing Elements

-- 1 Product
insert into retrogamer.products(name, price, type, platform, metadata, condition) values
('Crash Team Racing', 15.00, 0, 'ps1', '0000001.json','A');

-- Credentials
insert into retrogamer.credentials(pass_hash, pass_salt. creation_date) values
('timidone', 'dinosauri','2024-05-12');

-- Users
insert into retrogamer.users(id_client,firstname, lastname, telephone, email, birth, address, city, prov, cap, id_cred) values
(2,'Domenico','Amorelli','+3932010234455','timidone@so.org','2004-05-15','Via Girolamo Savonarola','Sapri','SA','84073',1);