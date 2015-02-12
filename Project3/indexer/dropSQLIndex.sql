#drop table auto drops index, this is to avoid errors when table does not exist
#ALTER TABLE item_location DROP INDEX coord_sp_index;
DROP TABLE IF EXISTS item_location;
