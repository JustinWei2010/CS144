#might have to check if table exists before dropping index
ALTER TABLE item_location DROP INDEX coord_sp_index;
DROP TABLE IF EXISTS item_location;
