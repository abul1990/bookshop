ALTER TABLE orders
  DROP COLUMN total_price;

ALTER TABLE orders
  ADD COLUMN currency varchar(255) NOT NULL;

ALTER TABLE orders
  ADD COLUMN amount NUMERIC NOT NULL;