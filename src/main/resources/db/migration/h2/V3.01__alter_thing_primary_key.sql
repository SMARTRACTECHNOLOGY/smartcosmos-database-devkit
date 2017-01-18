-- Update primary key to be id within tenantId for the H2 database used when building the devkit project.
--
ALTER TABLE thing
  DROP CONSTRAINT IF EXISTS UK_51ftm4jj6o6r8i4rxlv35mjdo
;

ALTER TABLE thing
  DROP PRIMARY KEY
;

ALTER TABLE thing
  ADD PRIMARY KEY (tenantId, id)
;
