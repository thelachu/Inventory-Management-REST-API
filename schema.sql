CREATE DATABASE IF NOT EXISTS inventory_db;
USE inventory_db;

-- Re-running this script resets the table and reloads the sample data
DROP TABLE IF EXISTS items;

CREATE TABLE items (
    id          BIGINT         NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(500),
    quantity    INT            NOT NULL,
    price       DECIMAL(12,2)  NOT NULL,
    PRIMARY KEY (id)
);

-- 30 sample items
INSERT INTO items (name, description, quantity, price) VALUES
('Wireless Mouse',        'Ergonomic 2.4GHz wireless mouse',             50,    599.00),
('Mechanical Keyboard',   'RGB backlit mechanical keyboard, blue switch', 25,   2499.00),
('USB-C Charger 65W',     'Fast charger for laptops and phones',          40,   1299.00),
('Laptop Stand',          'Adjustable aluminium laptop stand',            30,    899.50),
('HDMI Cable 2m',         'High speed HDMI 2.0 cable',                   100,    249.00),
('Webcam 1080p',          'Full HD webcam with built-in microphone',      18,   1799.00),
('Bluetooth Speaker',     'Portable waterproof speaker',                  35,   1599.00),
('Wired Earphones',       'In-ear earphones with mic',                   120,    299.00),
('Power Bank 10000mAh',   'Dual port fast charging power bank',           60,    999.00),
('Pen Drive 64GB',        'USB 3.0 flash drive',                          80,    449.00),
('External HDD 1TB',      'Portable 2.5 inch hard drive',                 22,   3999.00),
('SSD 512GB',             'SATA internal solid state drive',              15,   3299.00),
('Monitor 24 inch',       'Full HD IPS monitor',                          12,   8999.00),
('Router Wi-Fi 6',        'Dual band wireless router',                    20,   2799.00),
('Ethernet Cable 5m',     'Cat6 network cable',                           90,    199.00),
('Notebook A4',           '200 pages ruled notebook',                    250,     65.00),
('Ball Pen Blue',         'Smooth writing ball pen',                     500,     10.00),
('Gel Pen Black',         '0.5mm gel pen',                               400,     20.00),
('Highlighter Set',       'Pack of 5 colour highlighters',               150,    120.00),
('Stapler',               'Medium size stapler with pins',                70,     85.50),
('Desk Organizer',        'Wooden desk organizer with 4 slots',           28,    349.00),
('Whiteboard 2x3 ft',     'Magnetic whiteboard with marker tray',          9,   1099.00),
('Whiteboard Marker',     'Pack of 4 assorted markers',                  200,    140.00),
('A4 Paper Ream',         '500 sheets 75 GSM',                            45,    349.00),
('Office Chair',          'Ergonomic chair with lumbar support',           6,   5499.00),
('LED Desk Lamp',         'Rechargeable lamp with 3 brightness modes',    33,    799.00),
('Printer Ink Black',     'Compatible black ink cartridge',                0,    649.00),
('Calculator',            '12 digit scientific calculator',               55,    499.00),
('File Folder',           'Pack of 10 plastic folders',                  110,    180.00),
('Sticky Notes',          'Pack of 12 pads, 3x3 inch',                   300,     95.00);