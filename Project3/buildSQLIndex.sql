CREATE TABLE item_location (
     item_id CHAR(100) PRIMARY KEY,     
     latitude VARCHAR(100) NOT NULL,     
     longitude VARCHAR(100) NOT NULL,     
     FOREIGN KEY (item_id) REFERENCES item(item_id) 
) ENGINE=MyISAM;

#Bulk load location data
INSERT INTO item_location (item_id, latitude, longitude)     
    SELECT item_id, latitude, longitude FROM item WHERE latitude IS NOT NULL AND longitude IS NOT NULL;

#Add spatial index
ALTER TABLE item_location ADD COLUMN coord POINT;
UPDATE item_location SET coord=POINT(latitude, longitude);
ALTER TABLE item_location MODIFY coord POINT NOT NULL;
CREATE SPATIAL INDEX coord_sp_index ON item_location(coord);
