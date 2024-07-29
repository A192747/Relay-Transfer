USE filesHashesKeyspace;
CREATE TABLE IF NOT EXISTS file_hash (
    id UUID  PRIMARY KEY,
    hash varchar,
    file_name varchar
);