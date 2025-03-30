CREATE TABLE IF NOT EXISTS category
(
    id   INT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS location
(
    id              BIGINT NOT NULL,
    name            VARCHAR(255),
    table_reference VARCHAR(255),
    address         VARCHAR(255),
    post_code       VARCHAR(255),
    city            VARCHAR(255),
    website         VARCHAR(255),
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS location_category
(
    id          BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    category_id INT    NOT NULL,
    CONSTRAINT pk_locationcategory PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uc_e8afc117007f39d3ac54d7b86
    ON location_category(location_id, category_id);