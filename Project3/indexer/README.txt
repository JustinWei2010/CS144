******************************************************************************
TEAM: Amazon Meets Facebook
******************************************************************************

For the lucene index we chose to store the item_id and name fields so that we
could retrieve and output them from search. We have a field called content
which is what we use as the default field to search for our indices. The 
content is a concatenation of the name, category, and description fields and 
it is what we use to search with keywords. Each tuple is also considered as 
a separate document in our implementation.