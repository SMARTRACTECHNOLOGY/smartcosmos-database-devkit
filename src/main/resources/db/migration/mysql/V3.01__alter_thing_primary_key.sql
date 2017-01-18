-- Update primary key to be id within tenantId
--
-- disable auto-commit for DDL updates so we don't have to jump through hoops if it fails.
-- see https://flywaydb.org/documentation/faq.html#repair and https://mariadb.com/kb/en/mariadb/start-transaction/
SET AUTOCOMMIT = 0
;

START TRANSACTION
;

ALTER TABLE thing
  DROP INDEX IF EXISTS UK_51ftm4jj6o6r8i4rxlv35mjdo
;

ALTER TABLE thing
  DROP PRIMARY KEY
;

ALTER TABLE thing
  ADD PRIMARY KEY (tenantId, id, type)
;

COMMIT
;

SET AUTOCOMMIT = 1
;