DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS orders CASCADE ;
DROP TABLE IF EXISTS layout CASCADE ;
DROP TABLE IF EXISTS layout_name CASCADE ;
DROP TABLE IF EXISTS fragment CASCADE ;
DROP TABLE IF EXISTS linoleum CASCADE ;
DROP TABLE IF EXISTS address CASCADE ;
DROP TABLE IF EXISTS fragment_order CASCADE;
DROP TABLE IF EXISTS roll CASCADE;

CREATE TABLE users
(
   id     SERIAL PRIMARY KEY,
   name   VARCHAR(124) NOT NULL,
   email  VARCHAR(124) NOT NULL UNIQUE,
   password VARCHAR(50) NOT NULL,
   phone_number BIGINT NOT NULL UNIQUE ,
   role VARCHAR(20) NOT NULL

);

CREATE INDEX password_idx ON users(password);


CREATE TABLE layout_name
(
    id     SERIAL PRIMARY KEY,
    ln_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE address
(
    id     SERIAL PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(50) NOT NULL,
    home_num VARCHAR(10) NOT NULL
);

CREATE TABLE layout
(
    id     SERIAL PRIMARY KEY,
    room_count INTEGER NOT NULL,
    layout_img VARCHAR NOT NULL,
    row_type VARCHAR(50) NOT NULL,
    l_type VARCHAR(50) NOT NULL,
    address_id INTEGER NOT NULL,
    FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE,
    layout_name_id INTEGER NOT NULL,
    FOREIGN KEY (layout_name_id) REFERENCES layout_name(id) ON DELETE CASCADE
);

CREATE INDEX layouts_name_id_idx ON layout(layout_name_id);
CREATE INDEX address_id_idx ON layout(address_id);

CREATE TABLE fragment
(
    id     SERIAL PRIMARY KEY,
    f_width  FLOAT4 NOT NULL ,
    f_length FLOAT4 NOT NULL,
    f_type VARCHAR(30) NOT NULL,
    layout_name_id INTEGER NOT NULL,
    FOREIGN KEY (layout_name_id) REFERENCES layout_name(id) ON DELETE CASCADE

);

CREATE TABLE linoleum
(
    id     SERIAL PRIMARY KEY,
    l_name  VARCHAR(50) NOT NULL UNIQUE,
    protect FLOAT4 NOT NULL,
    thickness FLOAT4 NOT NULL,
    price INTEGER NOT NULL,
    image_path VARCHAR(100) NOT NULL UNIQUE
);


CREATE TABLE orders
(
    id     SERIAL PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    transporting_type VARCHAR(20) NOT NULL,
    transporting_date TIMESTAMP NOT NULL,
    cost INTEGER NOT NULL ,
    user_id INTEGER NOT NULL,
    FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    linoleum_id INTEGER NOT NULL,
    FOREIGN KEY (linoleum_id) REFERENCES linoleum(id) ON DELETE CASCADE,
    apartment_num INTEGER NOT NULL,
    address_id INTEGER NOT NULL,
    FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE CASCADE,
    layout_id INTEGER NOT NULL,
    FOREIGN KEY (layout_id) REFERENCES layout(id) ON DELETE CASCADE,
    type VARCHAR(50)

);

CREATE INDEX user_id_idx ON orders(user_id);
CREATE INDEX order_linoleum_id_idx ON orders(linoleum_id);

CREATE TABLE fragment_order
(
    id     SERIAL PRIMARY KEY,
    fragment_id  INTEGER NOT NULL ,
    FOREIGN KEY (fragment_id) REFERENCES fragment(id) ON DELETE CASCADE,
    order_id INTEGER NOT NULL ,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE INDEX order_id_idx ON fragment_order(order_id);

CREATE TABLE roll
(
    id     SERIAL PRIMARY KEY,
    part_num  INTEGER NOT NULL ,
    r_width FLOAT4 NOT NULL,
    r_length FLOAT4 NOT NULL,
    is_remain BOOLEAN NOT NULL,
    linoleum_id INTEGER NOT NULL,
    FOREIGN KEY (linoleum_id) REFERENCES linoleum(id) ON DELETE CASCADE
);

CREATE INDEX linoleum_id_idx ON roll(linoleum_id);
