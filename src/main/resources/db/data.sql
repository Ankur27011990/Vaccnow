INSERT INTO vaccinaton_centres(id, capacity, name) VALUES ('1', '2', 'Columbia Asia Hospital');
INSERT INTO vaccinaton_centres(id, capacity, name) VALUES ('2', '3', 'Manipal Hospital');

INSERT INTO vaccines(id, manufacturer, name) VALUES ('1', 'Serum Institute', 'Covishield');
INSERT INTO vaccines(id, manufacturer, name) VALUES ('2', 'Bharat Biotech', 'Covaxin');

INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('1','1');
INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('1','2');
INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('2','1');
INSERT INTO vaccine_inventory(centre_id, vaccine_id) VALUES ('2','2');

INSERT INTO users(id, email_id, name) VALUES (1,'abc@gmail.com','ABC');
INSERT INTO users(id, email_id, name) VALUES (2,'def@gmail.com','DEF');
INSERT INTO users(id, email_id, name) VALUES (3,'uvw@gmail.com','UVW');
INSERT INTO users(id, email_id, name) VALUES (4,'xyz@gmail.com','XYZ');