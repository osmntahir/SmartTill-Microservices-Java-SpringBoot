DO
$do$
    BEGIN
        IF NOT EXISTS (
            SELECT FROM pg_database WHERE datname = 'keycloak'
        ) THEN
            CREATE DATABASE keycloak;
        END IF;
    END
$do$;

DO
$do$
    BEGIN
        IF NOT EXISTS (
            SELECT FROM pg_database WHERE datname = 'storage'
        ) THEN
            CREATE DATABASE storage;
        END IF;
    END
$do$;
