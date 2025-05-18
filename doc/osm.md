# OSM

This is some documentation for OpenStreetMap features used here.

Please pay attention to this page:
https://operations.osmfoundation.org/policies/nominatim/

The API does not allow bulk usage. This is the intention of this app, too.
Somethime we need to code some services, in that case the scheduler takes one by another to
get data. All results are saved to a cache (directory). 

The scheduler only takes one entry, parses it and finishes. Soem seconds later, it tries to find the
next one and so on. Starting point is ScheduledGeoCoder.java.