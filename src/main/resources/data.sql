INSERT IGNORE INTO address (id, street, house_number, postal_code, city, country, created,version)
VALUES (1, '', '', '', '', '', CURRENT_DATE(), 0),
       (2, '', '', '', '', '', CURRENT_DATE(), 0),
       (3, '', '', '', '', '', CURRENT_DATE(), 0),
       (4, '', '', '', '', '', CURRENT_DATE(), 0);

INSERT IGNORE INTO user (id, email, password, display_name, first_name, last_name, date_of_birth, gender, address_id, role, is_locked, shipping_address_id, created, version)
VALUES (1, 'admin@firesale.nl', '$2a$10$zO.P/jNe8LedLkSAD67AIOXOktIBbsncYE7VcP/cUxvgkiZ4dBgRi', 'Admin', '', '', CURRENT_DATE(), 'OTHER', 1, 'ADMIN', 0, 2, CURRENT_DATE(), 0), /* password = admin */
       (2, 'user@firesale.nl', '$2a$10$aqnyqYtZbRbVa58vzFlHv.FauhaSYyfnwHcGUXCJJCaFTboubGxJq', 'User', '', '', CURRENT_DATE(), 'OTHER', 3, 'USER', 0, 4, CURRENT_DATE(), 0); /* password = user */

INSERT IGNORE INTO category (id, name, created,version)
VALUES  (1, 'Elektronica', CURRENT_DATE(), 0),
        (2, 'Huishouden', CURRENT_DATE(), 0),
        (3, 'Eten en drinken', CURRENT_DATE(), 0);