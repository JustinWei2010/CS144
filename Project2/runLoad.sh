#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
echo "Dropping tables in CS144"
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
echo "Creating tables in CS144"
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
echo "Converting xml files to sql load files"
ant run-all

# Run the load.sql batch file to load the data
mysql CS144 < load.sql
echo ""
echo "Loading data into sql tables"
echo ""

# Run the sql test queries
echo "Running your query.sql script..."
echo ""
mysql CS144 < queries.sql
echo ""
echo "Finished running queries!"
